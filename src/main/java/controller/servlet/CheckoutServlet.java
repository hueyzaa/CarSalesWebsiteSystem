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

            logger.info("Checkout total calculated: {}", total);

            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            request.setAttribute("user", user);

            logger.info("Forwarding to checkout.jsp");
            // Forward to checkout page
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
            String depositAmountStr = request.getParameter("depositAmount");

            if (paymentType == null || paymentType.trim().isEmpty()) {
                logger.warn("Payment type not selected by user {}", userId);
                session.setAttribute("error", "Vui lòng chọn hình thức thanh toán!");
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

            // Create order
            int orderId = ordersDAO.createOrder(userId, cartItems);

            if (orderId == -1) {
                logger.error("Failed to create order for user {}", userId);
                session.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            logger.info("Order created successfully with ID: {} for user {}", orderId, userId);

            // Decrease stock for all items
            for (CartItem item : cartItems) {
                boolean stockUpdated = carDAO.decreaseStock(item.getCarId(), item.getQuantity());
                if (!stockUpdated) {
                    logger.warn("Failed to decrease stock for car ID: {} in order {}",
                            item.getCarId(), orderId);
                } else {
                    logger.debug("Stock decreased successfully for car ID: {} by {} units",
                            item.getCarId(), item.getQuantity());
                }
            }

            // Create transaction based on payment type
            double paymentAmount = 0;
            String transactionType = "";

            if ("FULL".equals(paymentType)) {
                paymentAmount = orderTotal;
                transactionType = "FULL";
                logger.info("Full payment selected: {} for order {}", paymentAmount, orderId);
            } else if ("DEPOSIT".equals(paymentType)) {
                try {
                    paymentAmount = Double.parseDouble(depositAmountStr);

                    // Validate deposit amount (at least 20% of total)
                    double minDeposit = orderTotal * 0.2;
                    if (paymentAmount < minDeposit) {
                        logger.warn("Deposit amount {} is less than minimum {} for order {}",
                                paymentAmount, minDeposit, orderId);
                        session.setAttribute("error", "Số tiền đặt cọc tối thiểu là 20% tổng giá trị đơn hàng!");
                        response.sendRedirect(request.getContextPath() + "/checkout");
                        return;
                    }

                    if (paymentAmount > orderTotal) {
                        logger.info("Deposit amount {} exceeds total {}, adjusting to total",
                                paymentAmount, orderTotal);
                        paymentAmount = orderTotal;
                    }

                    transactionType = "DEPOSIT";
                    logger.info("Deposit payment selected: {} for order {}", paymentAmount, orderId);
                } catch (NumberFormatException e) {
                    logger.error("Invalid deposit amount format: {} for order {}",
                            depositAmountStr, orderId);
                    session.setAttribute("error", "Số tiền đặt cọc không hợp lệ!");
                    response.sendRedirect(request.getContextPath() + "/checkout");
                    return;
                }
            }

            // Create transaction record
            int transactionId = transactionDAO.createTransaction(orderId, paymentAmount, transactionType);

            if (transactionId == -1) {
                logger.warn("Failed to create transaction for order: {}", orderId);
            } else {
                logger.info("Transaction created successfully with ID: {} for order {}",
                        transactionId, orderId);
            }

            // Clear cart after successful order
            boolean cartCleared = cartDAO.clearCart(userId);
            logger.info("Cart cleared for user {}: {}", userId, cartCleared);

            // Update cart count in session
            session.setAttribute("cartCount", 0);

            // Set success message with more details
            String successMessage = String.format(
                    "Đặt hàng thành công! Mã đơn hàng: #%d. %s",
                    orderId,
                    "FULL".equals(transactionType)
                            ? "Đã thanh toán toàn bộ."
                            : String.format("Đã đặt cọc %,.0f₫.", paymentAmount)
            );
            session.setAttribute("success", successMessage);

            logger.info("Order {} created successfully for user {}. Payment type: {}, Amount: {}. Redirecting to order detail page.",
                    orderId, userId, transactionType, paymentAmount);

            // Redirect to order detail page
            response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);

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