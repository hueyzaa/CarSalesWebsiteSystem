package controller.customer;

import dao.CartDAO;
import dao.OrdersDAO;
import dao.TransactionDAO;
import service.PromotionService;
import model.CartItem;
import model.Order;
import model.Promotion;
import util.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    private static final double DEPOSIT_PERCENTAGE = 0.10;

    private CartDAO cartDAO;
    private OrdersDAO ordersDAO;
    private TransactionDAO transactionDAO;
    private PromotionService promotionService;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        ordersDAO = new OrdersDAO();
        transactionDAO = new TransactionDAO();
        promotionService = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            if (hasCartValidationError(cartItems, request, response)) {
                return;
            }

            double total = calculateTotal(cartItems);
            List<Promotion> promotions = promotionService
                    .getAvailablePromotionsForCart(userId, cartItems);

            setCheckoutAttributes(request, cartItems, total, promotions);
            forward(request, response, "/WEB-INF/views/checkout.jsp");

        } catch (Exception e) {
            logger.error("Error in checkout GET", e);
            forwardToError(request, response, "Không thể tải trang thanh toán.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            String retryOrderId = request.getParameter("retryOrderId");
            if (isNotEmpty(retryOrderId)) {
                handleRetryPayment(request, response, userId, retryOrderId);
            } else {
                handleCheckout(request, response, userId);
            }
        } catch (Exception e) {
            logger.error("Error in checkout POST", e);
            String contextPath = request.getContextPath();
            redirectWithError(response, request.getSession(),
                    contextPath + "/checkout", "Đã xảy ra lỗi không mong muốn!");
        }
    }

    // USER VALIDATION

    private Integer validateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirect(response, request.getContextPath() + "/login");
            return null;
        }

        Integer userId = SessionUtils.getUserId(session);
        if (userId == null) {
            redirect(response, request.getContextPath() + "/login");
            return null;
        }

        return userId;
    }

    // RETRY PAYMENT

    private void handleRetryPayment(HttpServletRequest request, HttpServletResponse response,
                                    Integer userId, String retryOrderIdParam) throws IOException {
        try {
            int orderId = Integer.parseInt(retryOrderIdParam);
            Order order = ordersDAO.getOrderById(orderId);

            if (!validateRetryOrder(order, userId, orderId, request, response)) {
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("success", "Tiếp tục thanh toán cho đơn hàng #" + orderId);
            redirect(response, request.getContextPath() + "/payment?orderId=" + orderId);

        } catch (NumberFormatException e) {
            logger.error("Invalid retry order ID: {}", retryOrderIdParam);
            String contextPath = request.getContextPath();
            redirectWithError(response, request.getSession(),
                    contextPath + "/orders", "Mã đơn hàng không hợp lệ!");
        }
    }

    private boolean validateRetryOrder(Order order, Integer userId, int orderId,
                                       HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession();

        if (order == null) {
            return redirectWithError(response, session,
                    contextPath + "/orders", "Đơn hàng không tồn tại!");
        }

        if (order.getUserId() != userId) {
            return redirectWithError(response, session,
                    contextPath + "/orders", "Bạn không có quyền thực hiện thao tác này!");
        }

        if (!"PENDING".equals(order.getStatus())) {
            return redirectWithError(response, session,
                    contextPath + "/order-detail?id=" + orderId,
                    "Chỉ có thể thanh toán lại cho đơn hàng đang chờ xử lý!");
        }

        if (order.getPaidAmount() > 0) {
            return redirectWithError(response, session,
                    contextPath + "/order-detail?id=" + orderId,
                    "Đơn hàng này đã có giao dịch thanh toán!");
        }

        return true;
    }

    // NORMAL CHECKOUT

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response,
                                Integer userId) throws IOException {
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession();
        String paymentType = request.getParameter("paymentType");

        if (!isValidPaymentType(paymentType)) {
            redirectWithError(response, session, contextPath + "/checkout",
                    "Vui lòng chọn hình thức thanh toán hợp lệ!");
            return;
        }

        Integer promotionId = getPromotionId(request, paymentType, session);
        if (session.getAttribute("checkout_error") != null) {
            session.removeAttribute("checkout_error");
            redirect(response, contextPath + "/checkout");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
        if (hasCartValidationError(cartItems, request, response)) {
            return;
        }

        CheckoutData data = calculateCheckoutData(userId, cartItems, paymentType,
                promotionId, session);
        if (data == null) {
            redirect(response, contextPath + "/checkout");
            return;
        }

        int orderId = ordersDAO.createOrderWithPromotion(userId, cartItems, paymentType,
                data.depositAmount, data.notes, promotionId);

        if (orderId == -1) {
            redirectWithError(response, session, contextPath + "/checkout",
                    "Không thể tạo đơn hàng. Vui lòng thử lại!");
            return;
        }

        createTransaction(orderId, data.paymentAmount, paymentType);
        cartDAO.clearCart(userId);
        session.setAttribute("cartCount", 0);

        redirectAfterCheckout(response, session, paymentType, orderId,
                data.successMessage, contextPath);
    }

    // CART VALIDATION

    private boolean hasCartValidationError(List<CartItem> cartItems, HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();

        if (isEmpty(cartItems)) {
            redirectWithError(response, request.getSession(),
                    contextPath + "/cart", "Giỏ hàng của bạn đang trống!");
            return true;
        }

        String stockError = validateStock(cartItems);
        if (stockError != null) {
            redirectWithError(response, request.getSession(),
                    contextPath + "/cart", stockError);
            return true;
        }

        return false;
    }

    private String validateStock(List<CartItem> cartItems) {
        StringBuilder errors = new StringBuilder();

        for (CartItem item : cartItems) {
            if (item.getCar() == null) {
                logger.error("CartItem {} has null Car!", item.getId());
                continue;
            }

            int stock = item.getCar().getStock();
            int quantity = item.getQuantity();

            if (stock < quantity) {
                if (!errors.isEmpty()) errors.append(" ");
                errors.append(item.getCar().getName())
                        .append(" chỉ còn ")
                        .append(stock)
                        .append(" xe.");
            }
        }

        return !errors.isEmpty() ? "Số lượng không đủ: " + errors : null;
    }

    // CHECKOUT DATA CALCULATION

    private CheckoutData calculateCheckoutData(Integer userId, List<CartItem> cartItems,
                                               String paymentType, Integer promotionId,
                                               HttpSession session) {
        double orderTotal = calculateTotal(cartItems);
        double discount = calculateDiscount(userId, cartItems, paymentType, promotionId, session);

        if (discount < 0) return null; // Error occurred

        double finalTotal = Math.max(0, orderTotal - discount);

        return "SHOWROOM".equals(paymentType)
                ? createShowroomData(finalTotal)
                : createDepositData(finalTotal, discount);
    }

    private double calculateDiscount(Integer userId, List<CartItem> cartItems,
                                     String paymentType, Integer promotionId,
                                     HttpSession session) {
        if (promotionId == null || !"DEPOSIT".equals(paymentType)) {
            return 0;
        }

        String error = promotionService.validatePromotionForCart(userId, promotionId, cartItems);
        if (error != null) {
            session.setAttribute("error", error);
            logger.warn("Promotion validation failed: {}", error);
            return -1; // Signal error
        }

        return promotionService.calculateTotalDiscount(promotionId, cartItems);
    }

    private CheckoutData createShowroomData(double total) {
        CheckoutData data = new CheckoutData();
        data.finalTotal = total;
        data.paymentAmount = 0;
        data.notes = String.format(
                "Khách hàng sẽ thanh toán toàn bộ %,.0f₫ tại showroom. " +
                        "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                total);
        return data;
    }

    private CheckoutData createDepositData(double total, double discount) {
        CheckoutData data = new CheckoutData();
        data.finalTotal = total;
        data.discount = discount;
        data.depositAmount = total * DEPOSIT_PERCENTAGE;
        data.paymentAmount = data.depositAmount;

        double remaining = total - data.depositAmount;

        data.notes = discount > 0
                ? String.format(
                "Chờ thanh toán đặt cọc %,.0f₫ (10%%). " +
                        "Đã áp dụng khuyến mãi giảm %,.0f₫. " +
                        "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ " +
                        "tại showroom khi nhận xe.",
                data.depositAmount, discount, remaining)
                : String.format(
                "Chờ thanh toán đặt cọc %,.0f₫ (10%%). " +
                        "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ " +
                        "tại showroom khi nhận xe.",
                data.depositAmount, remaining);

        if (discount > 0) {
            data.successMessage = String.format("Bạn đã tiết kiệm %,.0f₫!", discount);
        }

        return data;
    }

    // TRANSACTION & REDIRECT

    private void createTransaction(int orderId, double amount, String paymentType) {
        int txId = transactionDAO.createTransaction(orderId, amount, paymentType, "PENDING");
        if (txId != -1) {
            logger.info("Transaction {} created for order {}", txId, orderId);
        }
    }

    private void redirectAfterCheckout(HttpServletResponse response, HttpSession session,
                                       String paymentType, int orderId, String successMessage,
                                       String contextPath) throws IOException {
        if ("SHOWROOM".equals(paymentType)) {
            session.setAttribute("success", String.format(
                    "Đặt hàng thành công! Mã đơn hàng: #%d. " +
                            "Chúng tôi sẽ liên hệ với bạn để xác nhận.", orderId));
            redirect(response, contextPath + "/order-detail?id=" + orderId);
        } else {
            if (successMessage != null) {
                session.setAttribute("success", successMessage);
            }
            redirect(response, contextPath + "/payment?orderId=" + orderId);
        }
    }

    // VALIDATION HELPERS

    private boolean isValidPaymentType(String paymentType) {
        return "DEPOSIT".equals(paymentType) || "SHOWROOM".equals(paymentType);
    }

    private Integer getPromotionId(HttpServletRequest request, String paymentType,
                                   HttpSession session) {
        String param = request.getParameter("promotionId");
        if (isEmpty(param)) return null;

        try {
            Integer promotionId = Integer.parseInt(param);

            if ("SHOWROOM".equals(paymentType)) {
                session.setAttribute("error",
                        "Khuyến mãi chỉ áp dụng cho hình thức Đặt Cọc Online!");
                session.setAttribute("checkout_error", "error");
                return null;
            }

            return promotionId;
        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID: {}", param);
            return null;
        }
    }

    private double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    private void setCheckoutAttributes(HttpServletRequest request, List<CartItem> cartItems,
                                       double total, List<Promotion> promotions) {
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total", total);
        request.setAttribute("depositAmount", total * DEPOSIT_PERCENTAGE);
        request.setAttribute("depositPercentage", DEPOSIT_PERCENTAGE * 100);
        request.setAttribute("availablePromotions", promotions);
    }

    // UTILITY METHODS

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    private boolean redirectWithError(HttpServletResponse response, HttpSession session,
                                      String url, String message) throws IOException {
        session.setAttribute("error", message);
        logger.warn("Error: {}", message);
        response.sendRedirect(url);
        return false;
    }

    @SuppressWarnings("SameParameterValue")
    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    // INNER CLASS

    private static class CheckoutData {
        double finalTotal;
        double discount;
        double paymentAmount;
        Double depositAmount;
        String notes;
        String successMessage;
    }
}