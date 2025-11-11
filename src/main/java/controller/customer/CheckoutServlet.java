package controller.customer;

import dao.OrdersDAO;
import dao.TransactionDAO;
import service.CheckoutService;
import service.CheckoutService.CheckoutCalculationResult;
import service.CheckoutService.CheckoutSubmitResult;
import service.PromotionService;
import model.Order;
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

/**
 * Controller cho checkout - CHỈ ĐIỀU PHỐI, KHÔNG XỬ LÝ LOGIC
 * Tất cả business logic đã được di chuyển vào CheckoutService
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);

    private CheckoutService checkoutService;
    private OrdersDAO ordersDAO;
    private TransactionDAO transactionDAO;
    private PromotionService promotionService;

    @Override
    public void init() {
        checkoutService = new CheckoutService();
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
            // ============ CONTROLLER CHỈ GỌI SERVICE ============
            CheckoutCalculationResult result = checkoutService.calculateAllScenarios(userId);

            if (!result.isSuccess()) {
                redirectWithError(response, request.getSession(),
                        request.getContextPath() + "/cart", result.getError());
                return;
            }

            // Convert Models to DTOs for display
            var cartItemDTOs = promotionService.toCartItemDTOs(result.getCartItems());
            var promotionDTOs = promotionService.toPromotionDTOs(result.getAvailablePromotions());

            // Set attributes - Controller chỉ chuyển dữ liệu từ Service sang View
            request.setAttribute("cartItems", cartItemDTOs);
            request.setAttribute("originalTotal", result.getOriginalTotal());
            request.setAttribute("total", result.getOriginalTotal()); // Add this for compatibility
            request.setAttribute("availablePromotions", promotionDTOs);
            request.setAttribute("promotionPreviews", result.getPromotionPreviews());
            request.setAttribute("noPromotionDeposit", result.getNoPromotionDeposit());
            request.setAttribute("noPromotionShowroom", result.getNoPromotionShowroom());
            request.setAttribute("depositPercentage", 10.0);

            logger.info("CHECKOUT: Forwarding to JSP with pre-calculated data for user {}", userId);
            forward(request, response, "/WEB-INF/views/Customer/checkout.jsp");

        } catch (Exception e) {
            logger.error("CHECKOUT ERROR: Exception in doGet for user {}", userId, e);
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
            logger.error("Error in checkout POST for user {}", userId, e);
            String contextPath = request.getContextPath();
            redirectWithError(response, request.getSession(),
                    contextPath + "/checkout", "Đã xảy ra lỗi không mong muốn!");
        }
    }

    // ============ PRIVATE METHODS - CHỈ XỬ LÝ HTTP, KHÔNG CÓ BUSINESS LOGIC ============

    /**
     * Validate user session
     */
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

    /**
     * Handle retry payment for existing order
     */
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

    /**
     * Validate retry order permissions
     */
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

    /**
     * Handle normal checkout process
     */
    private void handleCheckout(HttpServletRequest request, HttpServletResponse response,
                                Integer userId) throws IOException {
        String contextPath = request.getContextPath();
        HttpSession session = request.getSession();

        // Get parameters
        String paymentType = request.getParameter("paymentType");
        Integer promotionId = parsePromotionId(request.getParameter("promotionId"));

        // ============ GỌI SERVICE ĐỂ TÍNH TOÁN ============
        CheckoutSubmitResult result = checkoutService.calculateFinalCheckout(
                userId, paymentType, promotionId);

        if (!result.isSuccess()) {
            redirectWithError(response, session, contextPath + "/checkout", result.getError());
            return;
        }

        // ============ TẠO ORDER VÀ TRANSACTION ============
        int orderId = ordersDAO.createOrderWithPromotion(
                userId,
                result.getCartItems(),
                paymentType,
                result.getDepositAmount(),
                result.getNotes(),
                result.getPromotionId()
        );

        if (orderId == -1) {
            redirectWithError(response, session, contextPath + "/checkout",
                    "Không thể tạo đơn hàng. Vui lòng thử lại!");
            return;
        }

        // Create transaction
        createTransaction(orderId, result.getPaymentAmount(), paymentType);

        // Clear cart
        checkoutService.validateStock(result.getCartItems()); // Using service method to access cartDAO
        session.setAttribute("cartCount", 0);

        logger.info("Order {} created for user {} with {} items (payment type: {})",
                orderId, userId, result.getCartItems().size(), paymentType);

        // Redirect appropriately
        redirectAfterCheckout(response, session, paymentType, orderId,
                result.getSuccessMessage(), contextPath);
    }

    /**
     * Create transaction record
     */
    private void createTransaction(int orderId, double amount, String paymentType) {
        int txId = transactionDAO.createTransaction(orderId, amount, paymentType, "PENDING");
        if (txId != -1) {
            logger.info("Transaction {} created for order {}", txId, orderId);
        } else {
            logger.error("Failed to create transaction for order {}", orderId);
        }
    }

    /**
     * Redirect user after successful checkout
     */
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

    // ============ UTILITY METHODS ============

    /**
     * Parse promotion ID from string parameter
     */
    private Integer parsePromotionId(String param) {
        if (isEmpty(param)) {
            return null;
        }

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID: {}", param);
            return null;
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
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
        logger.warn("Redirect with error: {}", message);
        response.sendRedirect(url);
        return false;
    }

    @SuppressWarnings("SameParameterValue")
    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/Customer/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}