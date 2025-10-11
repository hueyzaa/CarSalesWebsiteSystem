package controller.servlet;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.TransactionDAO;
import model.Order;
import service.VNPayService;
import util.VNPayConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServlet.class);
    private VNPayService vnPayService;
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        vnPayService = new VNPayService();
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPayment(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPayment(request, response);
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get order ID
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                logger.warn("Order ID not provided");
                session.setAttribute("error", "Không tìm thấy thông tin đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            logger.info("Processing payment for order ID: {}", orderId);

            // Get order details
            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                session.setAttribute("error", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            // Calculate order total
            double orderTotal = orderDetailDAO.calculateOrderTotal(orderId);

            // ✅ Calculate total paid amount
            double totalPaid = transactionDAO.getTotalPaidAmount(orderId);

            // ✅ Calculate remaining amount
            double remainingAmount = orderTotal - totalPaid;

            logger.info("Order {} payment info:", orderId);
            logger.info("  - Total: {}", orderTotal);
            logger.info("  - Paid: {}", totalPaid);
            logger.info("  - Remaining: {}", remainingAmount);

            // Get payment amount and order info
            long paymentAmount;
            String orderInfo;

            if ("DEPOSIT".equals(order.getPaymentType())) {
                // ✅ Check if this is first payment (deposit) or remaining payment
                if (totalPaid == 0) {
                    // First payment - deposit
                    if (order.getDepositAmount() != null && order.getDepositAmount() > 0) {
                        paymentAmount = order.getDepositAmount().longValue();
                        orderInfo = String.format("Dat coc don hang so %d", orderId);
                        logger.info("First payment (deposit): {} VND", paymentAmount);
                    } else {
                        logger.error("Deposit amount not found for order {}", orderId);
                        session.setAttribute("error", "Không tìm thấy số tiền đặt cọc!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                        return;
                    }
                } else {
                    // ✅ Remaining payment - use calculated remaining amount
                    if (remainingAmount > 0) {
                        paymentAmount = (long) remainingAmount;
                        orderInfo = String.format("Thanh toan phan con lai don hang so %d", orderId);
                        logger.info("Remaining payment: {} VND", paymentAmount);
                    } else {
                        logger.warn("No remaining amount for order {}", orderId);
                        session.setAttribute("error", "Đơn hàng đã được thanh toán đầy đủ!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                        return;
                    }
                }
            } else if ("FULL".equals(order.getPaymentType())) {
                // Full payment
                if (totalPaid > 0) {
                    // Already paid
                    logger.warn("Order {} already paid", orderId);
                    session.setAttribute("error", "Đơn hàng đã được thanh toán!");
                    response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                    return;
                }
                paymentAmount = (long) orderTotal;
                orderInfo = String.format("Thanh toan don hang so %d", orderId);
                logger.info("Full payment: {} VND", paymentAmount);
            } else {
                // SHOWROOM payment
                logger.error("Cannot process online payment for SHOWROOM order {}", orderId);
                session.setAttribute("error", "Đơn hàng này thanh toán tại showroom!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            logger.info("Payment amount for order {}: {} VND", orderId, paymentAmount);
            logger.info("Order info: {}", orderInfo);

            // Get user IP address
            String ipAddress = VNPayConfig.getIpAddress(request);

            // Create payment URL
            String paymentUrl = vnPayService.createPaymentUrl(
                    orderId,
                    paymentAmount,
                    orderInfo,
                    ipAddress
            );

            if (paymentUrl == null) {
                logger.error("Failed to create payment URL for order: {}", orderId);
                session.setAttribute("error", "Không thể tạo liên kết thanh toán!");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            logger.info("✅ Redirecting to VNPay payment page for order: {}", orderId);

            // Store order ID in session
            session.setAttribute("paymentOrderId", orderId);

            // Redirect to VNPay
            response.sendRedirect(paymentUrl);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            session.setAttribute("error", "Mã đơn hàng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (Exception e) {
            logger.error("Error processing payment", e);
            session.setAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán!");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}