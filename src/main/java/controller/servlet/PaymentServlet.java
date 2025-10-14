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

            // Validate payment type
            if ("SHOWROOM".equals(order.getPaymentType())) {
                logger.error("Cannot process online payment for SHOWROOM order {}", orderId);
                session.setAttribute("error", "Đơn hàng này thanh toán tại showroom!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            // Calculate total paid amount để xác định lần thanh toán
            double totalPaid = transactionDAO.getTotalPaidAmount(orderId);

            logger.info("Order {} payment status:", orderId);
            logger.info("  - Payment Type: {}", order.getPaymentType());
            logger.info("  - Total Paid: {}", totalPaid);
            logger.info("  - Deposit Amount: {}", order.getDepositAmount());

            // Determine payment amount based on payment type and status
            long paymentAmount;
            String orderInfo;

            if ("FULL".equals(order.getPaymentType())) {
                // FULL payment
                if (totalPaid > 0) {
                    logger.warn("Order {} already paid", orderId);
                    session.setAttribute("error", "Đơn hàng đã được thanh toán!");
                    response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                    return;
                }

                // Calculate total from OrderDetail
                double orderTotal = orderDetailDAO.calculateOrderTotal(orderId);
                paymentAmount = (long) orderTotal;
                orderInfo = String.format("Thanh toan don hang so %d", orderId);

                logger.info("FULL payment: {} VND", paymentAmount);

            } else if ("DEPOSIT".equals(order.getPaymentType())) {
                // DEPOSIT payment - Check if first payment or remaining

                if (totalPaid == 0) {
                    // FIRST PAYMENT - Deposit amount
                    if (order.getDepositAmount() == null || order.getDepositAmount() <= 0) {
                        logger.error("Deposit amount not found for order {}", orderId);
                        session.setAttribute("error", "Không tìm thấy số tiền đặt cọc!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                        return;
                    }

                    paymentAmount = order.getDepositAmount().longValue();
                    orderInfo = String.format("Dat coc don hang so %d", orderId);

                    logger.info("DEPOSIT - First payment (deposit): {} VND", paymentAmount);

                } else {
                    // SECOND PAYMENT - Remaining amount
                    Double remainingAmount = order.getRemainingAmount();

                    if (remainingAmount == null || remainingAmount <= 0) {
                        logger.warn("No remaining amount for order {}", orderId);
                        session.setAttribute("error", "Đơn hàng đã được thanh toán đầy đủ!");
                        response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                        return;
                    }

                    paymentAmount = remainingAmount.longValue();
                    orderInfo = String.format("Thanh toan phan con lai don hang so %d", orderId);

                    logger.info("DEPOSIT - Remaining payment: {} VND", paymentAmount);
                }

            } else {
                logger.error("Invalid payment type: {} for order {}", order.getPaymentType(), orderId);
                session.setAttribute("error", "Hình thức thanh toán không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            logger.info("Final payment amount for order {}: {} VND", orderId, paymentAmount);
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
            logger.info("Payment URL created with amount: {} VND", paymentAmount);

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
            response.sendRedirect(request.getContextPath() + "/checko ut");
        }
    }
}