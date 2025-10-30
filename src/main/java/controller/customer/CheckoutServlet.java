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
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        ordersDAO = new OrdersDAO();
        transactionDAO = new TransactionDAO();
        promotionService = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirectToLogin(response, request.getContextPath());
            return;
        }

        Integer userId = SessionUtils.getUserId(session);
        if (userId == null) {
            redirectToLogin(response, request.getContextPath());
            return;
        }

        try {
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            if (cartItems == null || cartItems.isEmpty()) {
                setError(session, "Giỏ hàng của bạn đang trống!");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String stockError = validateStock(cartItems);
            if (stockError != null) {
                setError(session, stockError);
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            double total = calculateTotal(cartItems);
            List<Promotion> availablePromotions = promotionService
                    .getAvailablePromotionsForCart(userId, cartItems);

            setCheckoutAttributes(request, cartItems, total, availablePromotions);
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error in checkout GET", e);
            forwardToError(request, response, "Không thể tải trang thanh toán.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirectToLogin(response, request.getContextPath());
            return;
        }

        Integer userId = SessionUtils.getUserId(session);
        if (userId == null) {
            redirectToLogin(response, request.getContextPath());
            return;
        }

        try {
            // Handle retry payment
            String retryOrderId = request.getParameter("retryOrderId");
            if (retryOrderId != null && !retryOrderId.trim().isEmpty()) {
                handleRetryPayment(request, response, session, userId, retryOrderId);
                return;
            }

            // Handle normal checkout
            handleCheckout(request, response, session, userId);

        } catch (Exception e) {
            logger.error("Error in checkout POST", e);
            setError(session, "Đã xảy ra lỗi không mong muốn!");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }

    // ============ HELPER METHODS ============

    private void handleRetryPayment(HttpServletRequest request, HttpServletResponse response,
                                    HttpSession session, Integer userId, String retryOrderIdParam)
            throws IOException {
        try {
            int orderId = Integer.parseInt(retryOrderIdParam);
            Order order = ordersDAO.getOrderById(orderId);

            if (!validateRetryPayment(session, response, order, userId, orderId,
                    request.getContextPath())) {
                return;
            }

            session.setAttribute("success", "Tiếp tục thanh toán cho đơn hàng #" + orderId);
            response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);

        } catch (NumberFormatException e) {
            logger.error("Invalid retry order ID: {}", retryOrderIdParam);
            setError(session, "Mã đơn hàng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }

    private boolean validateRetryPayment(HttpSession session, HttpServletResponse response,
                                         Order order, Integer userId, int orderId,
                                         String contextPath) throws IOException {
        if (order == null) {
            setError(session, "Đơn hàng không tồn tại!");
            response.sendRedirect(contextPath + "/orders");
            return false;
        }

        if (order.getUserId() != userId) {
            setError(session, "Bạn không có quyền thực hiện thao tác này!");
            response.sendRedirect(contextPath + "/orders");
            return false;
        }

        if (!"PENDING".equals(order.getStatus())) {
            setError(session, "Chỉ có thể thanh toán lại cho đơn hàng đang chờ xử lý!");
            response.sendRedirect(contextPath + "/order-detail?id=" + orderId);
            return false;
        }

        if (order.getPaidAmount() > 0) {
            setError(session, "Đơn hàng này đã có giao dịch thanh toán!");
            response.sendRedirect(contextPath + "/order-detail?id=" + orderId);
            return false;
        }

        return true;
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response,
                                HttpSession session, Integer userId) throws IOException {
        String paymentType = request.getParameter("paymentType");

        if (!isValidPaymentType(paymentType)) {
            setError(session, "Vui lòng chọn hình thức thanh toán hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        Integer promotionId = getPromotionId(request, paymentType, session);
        if (promotionId == null && session.getAttribute("checkout_error") != null) {
            session.removeAttribute("checkout_error");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            setError(session, "Giỏ hàng của bạn đang trống!");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String stockError = validateStock(cartItems);
        if (stockError != null) {
            setError(session, stockError);
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        CheckoutData data = calculateCheckoutData(userId, cartItems, paymentType,
                promotionId, session);
        if (data == null) {
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        int orderId = createOrder(userId, cartItems, paymentType, data, promotionId);
        if (orderId == -1) {
            setError(session, "Không thể tạo đơn hàng. Vui lòng thử lại!");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        createTransaction(orderId, data.paymentAmount, paymentType);
        cartDAO.clearCart(userId);
        session.setAttribute("cartCount", 0);

        redirectAfterCheckout(response, session, paymentType, orderId, data.successMessage,
                request.getContextPath());
    }

    private CheckoutData calculateCheckoutData(Integer userId, List<CartItem> cartItems,
                                               String paymentType, Integer promotionId,
                                               HttpSession session) {
        double orderTotal = calculateTotal(cartItems);
        double discount = 0;

        if (promotionId != null && "DEPOSIT".equals(paymentType)) {
            String error = promotionService.validatePromotionForCart(userId, promotionId, cartItems);
            if (error != null) {
                setError(session, error);
                return null;
            }
            discount = promotionService.calculateTotalDiscount(promotionId, cartItems);
        }

        double finalTotal = Math.max(0, orderTotal - discount);

        CheckoutData data = new CheckoutData();
        data.orderTotal = orderTotal;
        data.discount = discount;
        data.finalTotal = finalTotal;

        if ("SHOWROOM".equals(paymentType)) {
            data.paymentAmount = 0;
            data.depositAmount = null;
            data.notes = String.format(
                    "Khách hàng sẽ thanh toán toàn bộ %,.0f₫ tại showroom. " +
                            "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                    finalTotal);
        } else {
            data.depositAmount = finalTotal * DEPOSIT_PERCENTAGE;
            data.paymentAmount = data.depositAmount;
            double remaining = finalTotal - data.depositAmount;

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
        }

        return data;
    }

    private int createOrder(Integer userId, List<CartItem> cartItems, String paymentType,
                            CheckoutData data, Integer promotionId) {
        return ordersDAO.createOrderWithPromotion(userId, cartItems, paymentType,
                data.depositAmount, data.notes, promotionId);
    }

    private void createTransaction(int orderId, double amount, String paymentType) {
        int transactionId = transactionDAO.createTransaction(orderId, amount, paymentType, "PENDING");
        if (transactionId != -1) {
            logger.info("Transaction created with ID: {}", transactionId);
        }
    }

    private void redirectAfterCheckout(HttpServletResponse response, HttpSession session,
                                       String paymentType, int orderId, String successMessage,
                                       String contextPath) throws IOException {
        if ("SHOWROOM".equals(paymentType)) {
            String message = String.format(
                    "Đặt hàng thành công! Mã đơn hàng: #%d. " +
                            "Chúng tôi sẽ liên hệ với bạn để xác nhận.", orderId);
            session.setAttribute("success", message);
            response.sendRedirect(contextPath + "/order-detail?id=" + orderId);
        } else {
            if (successMessage != null) {
                session.setAttribute("success", successMessage);
            }
            response.sendRedirect(contextPath + "/payment?orderId=" + orderId);
        }
    }

    // ============ VALIDATION & CALCULATION ============

    private boolean isValidPaymentType(String paymentType) {
        return "DEPOSIT".equals(paymentType) || "SHOWROOM".equals(paymentType);
    }

    private Integer getPromotionId(HttpServletRequest request, String paymentType,
                                   HttpSession session) {
        String param = request.getParameter("promotionId");
        if (param == null || param.trim().isEmpty()) {
            return null;
        }

        try {
            Integer promotionId = Integer.parseInt(param);

            if ("SHOWROOM".equals(paymentType)) {
                setError(session, "Khuyến mãi chỉ áp dụng cho hình thức Đặt Cọc Online!");
                session.setAttribute("checkout_error", "error");
                return null;
            }

            return promotionId;
        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID: {}", param);
            return null;
        }
    }

    private String validateStock(List<CartItem> cartItems) {
        StringBuilder errors = new StringBuilder();

        for (CartItem item : cartItems) {
            if (item.getCar() == null) {
                logger.error("CartItem {} has null Car!", item.getId());
                continue;
            }

            if (item.getCar().getStock() < item.getQuantity()) {
                errors.append(item.getCar().getName())
                        .append(" chỉ còn ")
                        .append(item.getCar().getStock())
                        .append(" xe. ");
            }
        }

        return errors.length() > 0 ? "Số lượng không đủ: " + errors.toString() : null;
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

    // ============ UTILITY METHODS ============

    private void redirectToLogin(HttpServletResponse response, String contextPath)
            throws IOException {
        logger.warn("User not logged in");
        response.sendRedirect(contextPath + "/login");
    }

    private void setError(HttpSession session, String message) {
        session.setAttribute("error", message);
        logger.warn("Error: {}", message);
    }

    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }

    // ============ INNER CLASS ============

    private static class CheckoutData {
        double orderTotal;
        double discount;
        double finalTotal;
        double paymentAmount;
        Double depositAmount;
        String notes;
        String successMessage;
    }
}