package controller.customer;

import dao.OrdersDAO;
import dao.TransactionDAO;
import model.Order;
import service.VNPayService;
import util.SessionUtils;
import util.VNPayConfig;
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
    private TransactionDAO transactionDAO;

    @Override
    public void init() {
        vnPayService = new VNPayService();
        ordersDAO = new OrdersDAO();
        transactionDAO = new TransactionDAO();
        logger.info("PaymentServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processPayment(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processPayment(request, response);
    }

    // ============ PAYMENT PROCESSING ============

    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirect(response, request.getContextPath() + "/login");
            return;
        }

        try {
            Integer orderId = getOrderId(request);
            if (orderId == null) {
                redirectWithError(response, session, "Không tìm thấy thông tin đơn hàng!",
                        request.getContextPath() + "/orders");
                return;
            }

            Order order = ordersDAO.getOrderById(orderId);
            if (!validateOrder(order, orderId, session, response, request.getContextPath())) {
                return;
            }

            if (isAlreadyPaid(orderId, session, response, request.getContextPath())) {
                return;
            }

            String paymentUrl = createPaymentUrl(order, request);
            if (paymentUrl == null) {
                redirectWithError(response, session, "Không thể tạo liên kết thanh toán!",
                        request.getContextPath() + "/checkout");
                return;
            }

            session.setAttribute("paymentOrderId", orderId);
            redirect(response, paymentUrl);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            redirectWithError(response, session, "Mã đơn hàng không hợp lệ!",
                    request.getContextPath() + "/orders");
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            redirectWithError(response, session, "Đã xảy ra lỗi khi xử lý thanh toán!",
                    request.getContextPath() + "/checkout");
        }
    }

    // ============ VALIDATION ============

    private Integer getOrderId(HttpServletRequest request) {
        String param = request.getParameter("orderId");
        if (isEmpty(param)) {
            return null;
        }

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean validateOrder(Order order, int orderId, HttpSession session,
                                  HttpServletResponse response, String contextPath)
            throws IOException {
        if (order == null) {
            redirectWithError(response, session, "Không tìm thấy đơn hàng!",
                    contextPath + "/orders");
            return false;
        }

        if (!isDepositOrder(order)) {
            redirectWithError(response, session, "Đơn hàng này không thể thanh toán online!",
                    contextPath + "/order-detail?id=" + orderId);
            return false;
        }

        if (!hasValidDepositAmount(order)) {
            redirectWithError(response, session, "Không tìm thấy số tiền đặt cọc!",
                    contextPath + "/order-detail?id=" + orderId);
            return false;
        }

        return true;
    }

    private boolean isDepositOrder(Order order) {
        return "DEPOSIT".equals(order.getPaymentType());
    }

    private boolean hasValidDepositAmount(Order order) {
        return order.getDepositAmount() != null && order.getDepositAmount() > 0;
    }

    private boolean isAlreadyPaid(int orderId, HttpSession session, HttpServletResponse response,
                                  String contextPath) throws IOException {
        double totalPaid = transactionDAO.getTotalPaidAmount(orderId);
        if (totalPaid > 0) {
            redirectWithError(response, session, "Đơn hàng đã được đặt cọc!",
                    contextPath + "/order-detail?id=" + orderId);
            return true;
        }
        return false;
    }

    // ============ PAYMENT URL CREATION ============

    private String createPaymentUrl(Order order, HttpServletRequest request) {
        long amount = order.getDepositAmount().longValue();
        String orderInfo = buildOrderInfo(order.getOrderId());
        String ipAddress = VNPayConfig.getIpAddress(request);

        logger.info("Creating payment for order {}: {}₫", order.getOrderId(), amount);

        return vnPayService.createPaymentUrl(order.getOrderId(), amount, orderInfo, ipAddress);
    }

    private String buildOrderInfo(int orderId) {
        return String.format("Dat coc 10%% don hang so %d", orderId);
    }

    // ============ UTILITY METHODS ============

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    private void redirectWithError(HttpServletResponse response, HttpSession session,
                                   String message, String url) throws IOException {
        session.setAttribute("error", message);
        logger.warn("Payment error: {}", message);
        redirect(response, url);
    }
}