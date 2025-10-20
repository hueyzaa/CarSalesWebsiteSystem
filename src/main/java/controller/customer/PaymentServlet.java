package controller.customer;

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

            // Validate payment type - Only DEPOSIT is allowed for online payment
            if (!"DEPOSIT".equals(order.getPaymentType())) {
                logger.error("Invalid payment type for online payment: {} for order {}",
                        order.getPaymentType(), orderId);
                session.setAttribute("error", "Đơn hàng này không thể thanh toán online!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            // Check deposit amount
            if (order.getDepositAmount() == null || order.getDepositAmount() <= 0) {
                logger.error("Deposit amount not found for order {}", orderId);
                session.setAttribute("error", "Không tìm thấy số tiền đặt cọc!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            // Check if already paid
            double totalPaid = transactionDAO.getTotalPaidAmount(orderId);
            if (totalPaid > 0) {
                logger.warn("Order {} already has payment", orderId);
                session.setAttribute("error", "Đơn hàng đã được đặt cọc!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            // Payment amount is the deposit (10%)
            long paymentAmount = order.getDepositAmount().longValue();
            String orderInfo = String.format("Dat coc 10%% don hang so %d", orderId);

            logger.info("DEPOSIT payment for order {}: {} VND", orderId, paymentAmount);
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

            logger.info("Redirecting to VNPay payment page for order: {}", orderId);
            logger.info("Payment URL created with deposit amount: {} VND", paymentAmount);

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