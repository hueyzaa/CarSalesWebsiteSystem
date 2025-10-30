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

/**
 * CheckoutServlet - Handle checkout process
 * UPDATED: Use SessionUtils for user management
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    private CartDAO cartDAO;
    private OrdersDAO ordersDAO;
    private TransactionDAO transactionDAO;
    private PromotionService promotionService;

    private static final double DEPOSIT_PERCENTAGE = 0.10;

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

        // Check if user is logged in using SessionUtils
        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get user ID using SessionUtils
            Integer userId = SessionUtils.getUserId(session);
            if (userId == null) {
                logger.error("User ID is null despite being logged in");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            logger.info("Processing checkout for user ID: {}", userId);

            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
            logger.info("Retrieved {} cart items for user {}",
                    cartItems != null ? cartItems.size() : 0, userId);

            if (cartItems == null || cartItems.isEmpty()) {
                logger.warn("Cart is empty for user {}", userId);
                session.setAttribute("error", "Giỏ hàng của bạn đang trống!");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Validate stock for all items
            boolean stockValid = true;
            StringBuilder stockErrors = new StringBuilder();

            for (CartItem item : cartItems) {
                if (item.getCar() == null) {
                    logger.error("CartItem {} has null Car object!", item.getId());
                    continue;
                }

                if (item.getCar().getStock() < item.getQuantity()) {
                    stockValid = false;
                    stockErrors.append(item.getCar().getName())
                            .append(" chỉ còn ")
                            .append(item.getCar().getStock())
                            .append(" xe. ");
                }
            }

            if (!stockValid) {
                logger.warn("Stock validation failed: {}", stockErrors.toString());
                session.setAttribute("error", "Số lượng không đủ: " + stockErrors.toString());
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Calculate total
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getSubtotal();
            }

            // Get available promotions for this cart
            List<Promotion> availablePromotions = promotionService.getAvailablePromotionsForCart(
                    userId, cartItems);

            logger.info("Found {} available promotions for checkout", availablePromotions.size());

            // Calculate deposit amount (10%)
            double depositAmount = total * DEPOSIT_PERCENTAGE;

            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("depositAmount", depositAmount);
            request.setAttribute("depositPercentage", DEPOSIT_PERCENTAGE * 100);
            request.setAttribute("userId", userId);
            request.setAttribute("userName", SessionUtils.getUserName(session));
            request.setAttribute("userEmail", SessionUtils.getUserEmail(session));
            request.setAttribute("availablePromotions", availablePromotions);

            logger.info("Forwarding to checkout.jsp");
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error in CheckoutServlet", e);
            request.setAttribute("error", "Không thể tải trang thanh toán.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error in CheckoutServlet", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if user is logged in using SessionUtils
        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("User not logged in during checkout POST");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get user ID using SessionUtils
            Integer userId = SessionUtils.getUserId(session);
            if (userId == null) {
                logger.error("User ID is null despite being logged in");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // ===== RETRY PAYMENT FUNCTIONALITY =====
            // Check if this is a retry payment request
            String retryOrderIdParam = request.getParameter("retryOrderId");
            if (retryOrderIdParam != null && !retryOrderIdParam.trim().isEmpty()) {
                try {
                    int retryOrderId = Integer.parseInt(retryOrderIdParam);
                    logger.info("Retry payment requested for order ID: {} by user ID: {}", retryOrderId, userId);

                    // Validate order exists and belongs to user
                    Order order = ordersDAO.getOrderById(retryOrderId);
                    if (order == null) {
                        logger.warn("Order {} not found for retry payment", retryOrderId);
                        session.setAttribute("error", "Đơn hàng không tồn tại!");
                        response.sendRedirect(request.getContextPath() + "/orders");
                        return;
                    }

                    // Check ownership
                    if (order.getUserId() != userId) {
                        logger.warn("User {} attempted to retry payment for order {} belonging to user {}",
                                userId, retryOrderId, order.getUserId());
                        session.setAttribute("error", "Bạn không có quyền thực hiện thao tác này!");
                        response.sendRedirect(request.getContextPath() + "/orders");
                        return;
                    }

                    // Validate order status is PENDING
                    if (!"PENDING".equals(order.getStatus())) {
                        logger.warn("Cannot retry payment for order {} with status {}", retryOrderId, order.getStatus());
                        session.setAttribute("error", "Chỉ có thể thanh toán lại cho đơn hàng đang chờ xử lý!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + retryOrderId);
                        return;
                    }

                    // Validate order is not paid
                    if (order.getPaidAmount() > 0) {
                        logger.warn("Order {} already has payment amount: {}, cannot retry",
                                retryOrderId, order.getPaidAmount());
                        session.setAttribute("error", "Đơn hàng này đã có giao dịch thanh toán!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + retryOrderId);
                        return;
                    }

                    // All validations passed - redirect to payment page
                    logger.info("Redirecting to payment page for retry order {}", retryOrderId);
                    session.setAttribute("success", "Tiếp tục thanh toán cho đơn hàng #" + retryOrderId);
                    response.sendRedirect(request.getContextPath() + "/payment?orderId=" + retryOrderId);
                    return;

                } catch (NumberFormatException e) {
                    logger.error("Invalid retry order ID format: {}", retryOrderIdParam);
                    session.setAttribute("error", "Mã đơn hàng không hợp lệ!");
                    response.sendRedirect(request.getContextPath() + "/orders");
                    return;
                }
            }
            // ===== END RETRY PAYMENT FUNCTIONALITY =====

            // Normal checkout flow continues here
            logger.info("Processing normal checkout POST for user ID: {}", userId);

            // Get payment type
            String paymentType = request.getParameter("paymentType");
            if (paymentType == null || paymentType.trim().isEmpty()) {
                logger.warn("Payment type not selected by user {}", userId);
                session.setAttribute("error", "Vui lòng chọn hình thức thanh toán!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            if (!paymentType.equals("DEPOSIT") && !paymentType.equals("SHOWROOM")) {
                logger.error("Invalid payment type: {} for user {}", paymentType, userId);
                session.setAttribute("error", "Hình thức thanh toán không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            // Get selected promotion (if any)
            String promotionIdParam = request.getParameter("promotionId");
            Integer selectedPromotionId = null;

            if (promotionIdParam != null && !promotionIdParam.trim().isEmpty()) {
                try {
                    selectedPromotionId = Integer.parseInt(promotionIdParam);
                    logger.info("User selected promotion: {}", selectedPromotionId);

                    // VALIDATION: Promotions only allowed for DEPOSIT payment type
                    if ("SHOWROOM".equals(paymentType)) {
                        logger.warn("User {} attempted to use promotion with SHOWROOM payment", userId);
                        session.setAttribute("error", "Khuyến mãi chỉ áp dụng cho hình thức Đặt Cọc Online!");
                        response.sendRedirect(request.getContextPath() + "/checkout");
                        return;
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid promotion ID format: {}", promotionIdParam);
                }
            }

            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            if (cartItems == null || cartItems.isEmpty()) {
                logger.warn("Cart is empty during checkout POST for user {}", userId);
                session.setAttribute("error", "Giỏ hàng của bạn đang trống!");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Validate stock again
            for (CartItem item : cartItems) {
                if (item.getCar().getStock() < item.getQuantity()) {
                    logger.warn("Insufficient stock for car {} during checkout", item.getCar().getName());
                    session.setAttribute("error", "Số lượng xe " + item.getCar().getName() + " không đủ!");
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
            }

            // Calculate order total
            double orderTotal = 0;
            for (CartItem item : cartItems) {
                orderTotal += item.getSubtotal();
            }

            // Validate and calculate promotion discount (only for DEPOSIT)
            double promotionDiscount = 0;
            if (selectedPromotionId != null && "DEPOSIT".equals(paymentType)) {
                String validationError = promotionService.validatePromotionForCart(
                        userId, selectedPromotionId, cartItems);

                if (validationError != null) {
                    logger.warn("Promotion validation failed: {}", validationError);
                    session.setAttribute("error", validationError);
                    response.sendRedirect(request.getContextPath() + "/checkout");
                    return;
                }

                // Calculate discount
                promotionDiscount = promotionService.calculateTotalDiscount(
                        selectedPromotionId, cartItems);

                logger.info("Promotion discount calculated: {}₫", promotionDiscount);
            } else if (selectedPromotionId != null && "SHOWROOM".equals(paymentType)) {
                // Double check - this should have been caught earlier
                logger.error("Promotion selected with SHOWROOM payment - this should not happen!");
                selectedPromotionId = null;
            }

            // Calculate final total after discount
            double finalTotal = orderTotal - promotionDiscount;
            if (finalTotal < 0) {
                finalTotal = 0;
            }

            logger.info("Order total: {}₫, Discount: {}₫, Final: {}₫",
                    orderTotal, promotionDiscount, finalTotal);

            // Process based on payment type
            int orderId;
            double paymentAmount = 0;
            Double depositAmount = null;
            String notes = null;
            String paymentStatus = "PENDING";

            switch (paymentType) {
                case "SHOWROOM":
                    paymentAmount = 0;
                    paymentStatus = "PENDING";
                    notes = String.format(
                            "Khách hàng sẽ thanh toán toàn bộ %,.0f₫ tại showroom. " +
                                    "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                            finalTotal
                    );
                    break;

                case "DEPOSIT":
                    depositAmount = finalTotal * DEPOSIT_PERCENTAGE;
                    paymentAmount = depositAmount;
                    paymentStatus = "PENDING";

                    // Calculate remaining after deposit
                    double remainingAfterDeposit = finalTotal - depositAmount;

                    // Build notes based on whether promotion was applied
                    if (promotionDiscount > 0) {
                        notes = String.format(
                                "Chờ thanh toán đặt cọc %,.0f₫ (10%%). " +
                                        "Đã áp dụng khuyến mãi giảm %,.0f₫. " +
                                        "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ tại showroom khi nhận xe.",
                                depositAmount, promotionDiscount, remainingAfterDeposit
                        );
                    } else {
                        notes = String.format(
                                "Chờ thanh toán đặt cọc %,.0f₫ (10%%). " +
                                        "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ tại showroom khi nhận xe.",
                                depositAmount, remainingAfterDeposit
                        );
                    }
                    break;

                default:
                    logger.error("Invalid payment type: {} for user {}", paymentType, userId);
                    session.setAttribute("error", "Hình thức thanh toán không hợp lệ!");
                    response.sendRedirect(request.getContextPath() + "/checkout");
                    return;
            }

            // Create order with promotion (only if DEPOSIT)
            orderId = ordersDAO.createOrderWithPromotion(
                    userId, cartItems, paymentType, depositAmount, notes, selectedPromotionId);

            if (orderId == -1) {
                logger.error("Failed to create order for user {}", userId);
                session.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            logger.info("Order created successfully with ID: {}", orderId);

            // Create transaction record
            int transactionId = transactionDAO.createTransaction(
                    orderId, paymentAmount, paymentType, paymentStatus);

            if (transactionId != -1) {
                logger.info("Transaction created with ID: {}", transactionId);
            }

            // Clear cart
            cartDAO.clearCart(userId);
            session.setAttribute("cartCount", 0);

            // Redirect based on payment type
            if ("SHOWROOM".equals(paymentType)) {
                String successMessage = String.format(
                        "Đặt hàng thành công! Mã đơn hàng: #%d. Chúng tôi sẽ liên hệ với bạn để xác nhận.",
                        orderId
                );
                session.setAttribute("success", successMessage);
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
            } else {
                String successMessage = promotionDiscount > 0
                        ? String.format("Bạn đã tiết kiệm %,.0f₫!", promotionDiscount)
                        : null;
                if (successMessage != null) {
                    session.setAttribute("success", successMessage);
                }
                response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);
            }

        } catch (RuntimeException e) {
            logger.error("Database error in CheckoutServlet POST", e);
            session.setAttribute("error", "Không thể xử lý đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/checkout");

        } catch (Exception e) {
            logger.error("Unexpected error in CheckoutServlet POST", e);
            session.setAttribute("error", "Đã xảy ra lỗi không mong muốn!");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}