package controller.servlet;

import dao.CartDAO;
import dao.OrdersDAO;
import dao.CarDAO;
import dao.TransactionDAO;
import model.CartItem;
import model.User;
import exception.DatabaseException;
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
    private CartDAO cartDAO;
    private OrdersDAO ordersDAO;
    private CarDAO carDAO;
    private TransactionDAO transactionDAO;

    // Deposit percentage - 10% as per business requirement
    private static final double DEPOSIT_PERCENTAGE = 0.10;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        ordersDAO = new OrdersDAO();
        carDAO = new CarDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            int userId = user.getId();
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

                logger.debug("Validating stock for car: {} - Stock: {}, Requested: {}",
                        item.getCar().getName(), item.getCar().getStock(), item.getQuantity());

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

            // Calculate deposit amount (10%)
            double depositAmount = total * DEPOSIT_PERCENTAGE;

            logger.info("Checkout total calculated: {}, Deposit (10%): {}", total, depositAmount);

            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("depositAmount", depositAmount);
            request.setAttribute("depositPercentage", DEPOSIT_PERCENTAGE * 100);
            request.setAttribute("user", user);

            logger.info("Forwarding to checkout.jsp");
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);

        } catch (DatabaseException e) {
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
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("User not logged in during checkout POST");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            int userId = user.getId();
            logger.info("Processing checkout POST for user ID: {}", userId);

            // Get payment type
            String paymentType = request.getParameter("paymentType");

            if (paymentType == null || paymentType.trim().isEmpty()) {
                logger.warn("Payment type not selected by user {}", userId);
                session.setAttribute("error", "Vui lòng chọn hình thức thanh toán!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            // Only allow DEPOSIT or SHOWROOM
            if (!paymentType.equals("DEPOSIT") && !paymentType.equals("SHOWROOM")) {
                logger.error("Invalid payment type: {} for user {}", paymentType, userId);
                session.setAttribute("error", "Hình thức thanh toán không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            logger.info("Payment type selected: {} for user {}", paymentType, userId);

            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            if (cartItems == null || cartItems.isEmpty()) {
                logger.warn("Cart is empty during checkout POST for user {}", userId);
                session.setAttribute("error", "Giỏ hàng của bạn đang trống!");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            logger.info("Processing {} cart items for checkout", cartItems.size());

            // Validate stock again before creating order
            for (CartItem item : cartItems) {
                if (item.getCar().getStock() < item.getQuantity()) {
                    logger.warn("Insufficient stock for car {} during checkout. Stock: {}, Requested: {}",
                            item.getCar().getName(), item.getCar().getStock(), item.getQuantity());
                    session.setAttribute("error", "Số lượng xe " + item.getCar().getName() + " không đủ!");
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
            }

            // Calculate total
            double orderTotal = 0;
            for (CartItem item : cartItems) {
                orderTotal += item.getSubtotal();
            }

            logger.info("Order total calculated: {} for user {}", orderTotal, userId);

            // Process based on payment type
            int orderId;
            double paymentAmount = 0;
            Double depositAmount = null;
            String notes = null;
            String paymentStatus = "PENDING";

            switch (paymentType) {
                case "SHOWROOM":
                    // Showroom payment - no upfront payment, no stock decrease yet
                    paymentAmount = 0;
                    paymentStatus = "PENDING";
                    notes = String.format(
                            "Khách hàng sẽ thanh toán toàn bộ %,.0f₫ tại showroom. " +
                                    "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                            orderTotal
                    );
                    logger.info("Showroom payment selected for order. User: {}", userId);
                    break;

                case "DEPOSIT":
                    // Deposit payment - 10% of total
                    depositAmount = orderTotal * DEPOSIT_PERCENTAGE;
                    paymentAmount = depositAmount;
                    paymentStatus = "PENDING";
                    notes = String.format(
                            "Chờ thanh toán đặt cọc %,.0f₫ (10%%). " +
                                    "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ tại showroom khi nhận xe.",
                            depositAmount, orderTotal - depositAmount
                    );
                    logger.info("Deposit payment (10%) selected: {} for user {}", paymentAmount, userId);
                    break;

                default:
                    logger.error("Invalid payment type: {} for user {}", paymentType, userId);
                    session.setAttribute("error", "Hình thức thanh toán không hợp lệ!");
                    response.sendRedirect(request.getContextPath() + "/checkout");
                    return;
            }

            // Create order with payment information
            orderId = ordersDAO.createOrder(userId, cartItems, paymentType, depositAmount, notes);

            if (orderId == -1) {
                logger.error("Failed to create order for user {}", userId);
                session.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            logger.info("Order created successfully with ID: {} for user {}", orderId, userId);

            // ⚠️ KHÔNG GIẢM STOCK TẠI ĐÂY
            // Stock sẽ chỉ giảm khi:
            // - DEPOSIT: sau khi thanh toán đặt cọc thành công (PaymentCallbackServlet)
            // - SHOWROOM: khi admin/staff xác nhận đã thanh toán đủ tại showroom
            logger.info("Stock will be decreased after payment confirmation for order {}", orderId);

            // Create transaction record
            int transactionId = transactionDAO.createTransaction(orderId, paymentAmount, paymentType, paymentStatus);

            if (transactionId == -1) {
                logger.warn("Failed to create transaction for order: {}", orderId);
            } else {
                logger.info("Transaction created successfully with ID: {} for order {}. Type: {}, Amount: {}, Status: {}",
                        transactionId, orderId, paymentType, paymentAmount, paymentStatus);
            }

            // Clear cart after successful order
            boolean cartCleared = cartDAO.clearCart(userId);
            logger.info("Cart cleared for user {}: {}", userId, cartCleared);

            // Update cart count in session
            session.setAttribute("cartCount", 0);

            // Handle redirect based on payment type
            if ("SHOWROOM".equals(paymentType)) {
                // SHOWROOM - Go directly to order detail
                String successMessage = String.format(
                        "Đặt hàng thành công! Mã đơn hàng: #%d. " +
                                "Chúng tôi sẽ liên hệ với bạn để xác nhận và hẹn lịch đến showroom. " +
                                "Vui lòng mang theo CMND/CCCD và chuẩn bị thanh toán toàn bộ %,.0f₫ khi đến showroom.",
                        orderId, orderTotal
                );
                session.setAttribute("success", successMessage);

                logger.info("Order {} created with SHOWROOM payment. Redirecting to order detail.", orderId);
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);

            } else {
                // DEPOSIT - Redirect to payment gateway for 10% deposit
                logger.info("Order {} created with DEPOSIT payment. Redirecting to payment gateway for 10% deposit.",
                        orderId);
                response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);
            }

        } catch (DatabaseException e) {
            logger.error("Database error in CheckoutServlet POST", e);
            session.setAttribute("error", "Không thể xử lý đơn hàng. Vui lòng thử lại!");
            response.sendRedirect(request.getContextPath() + "/checkout");

        } catch (Exception e) {
            logger.error("Unexpected error in CheckoutServlet POST", e);
            session.setAttribute("error", "Đã xảy ra lỗi không mong muốn!");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}