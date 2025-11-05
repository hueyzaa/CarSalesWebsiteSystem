package controller.customer;

import dao.TransactionDAO;
import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
import model.Order;
import service.VNPayService;
import service.VNPayService.PaymentResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/payment-callback")
public class PaymentCallbackServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackServlet.class);

    private VNPayService vnPayService;
    private TransactionDAO transactionDAO;
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private CarDAO carDAO;

    @Override
    public void init() {
        vnPayService = new VNPayService();
        transactionDAO = new TransactionDAO();
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        carDAO = new CarDAO();
        logger.info("PaymentCallbackServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        try {
            logSeparator("RECEIVED PAYMENT RETURN FROM VNPAY");

            Map<String, String> params = collectVNPayParams(request);
            PaymentResult result = vnPayService.verifyPaymentCallback(params);

            logPaymentResult(result);

            if (result.isSuccess()) {
                handleSuccessfulPayment(request, response, session, result);
            } else {
                handleFailedPayment(request, response, session, result);
            }

        } catch (Exception e) {
            handleError(request, response, session, e);
        }
    }

    // ============ PAYMENT PROCESSING ============

    private void handleSuccessfulPayment(HttpServletRequest request, HttpServletResponse response,
                                         HttpSession session, PaymentResult result) throws IOException {
        int orderId = result.getOrderId();
        logger.info("Processing successful DEPOSIT payment for order {}", orderId);

        Order order = ordersDAO.getOrderById(orderId);
        if (order == null) {
            logger.error("Order not found: {}", orderId);
            redirect(response, request.getContextPath() + "/payment-result?success=false");
            return;
        }

        if (!isDepositOrder(order, orderId)) {
            redirect(response, request.getContextPath() + "/payment-result?success=false");
            return;
        }

        boolean success = processTransactionAndStock(orderId, result.getAmount());

        if (success) {
            setSuccessSession(session, orderId, result);
            logSeparator("Redirecting to payment result page: success=true");
            redirect(response, request.getContextPath() + "/payment-result?success=true&orderId=" + orderId);
        } else {
            redirect(response, request.getContextPath() + "/payment-result?success=false");
        }
    }

    private void handleFailedPayment(HttpServletRequest request, HttpServletResponse response,
                                     HttpSession session, PaymentResult result) throws IOException {
        logger.warn("Payment failed: Code={}, Message={}",
                result.getResponseCode(), result.getMessage());

        if (session != null) {
            session.setAttribute("paymentSuccess", false);
            session.setAttribute("paymentMessage", result.getMessage());
            session.setAttribute("paymentResponseCode", result.getResponseCode());
        }

        logSeparator("Redirecting to payment result page: success=false");
        redirect(response, request.getContextPath() + "/payment-result?success=false");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             HttpSession session, Exception e) throws IOException {
        logSeparator("CRITICAL ERROR processing payment callback");
        logger.error("Error details:", e);

        if (session != null) {
            session.setAttribute("error", "Lỗi xử lý kết quả thanh toán: " + e.getMessage());
        }

        redirect(response, request.getContextPath() + "/payment-result?success=false");
    }

    // ============ TRANSACTION & STOCK ============

    private boolean processTransactionAndStock(int orderId, long paidAmount) {
        var transactions = transactionDAO.getTransactionsByOrderId(orderId);

        if (transactions.isEmpty()) {
            logger.error("No transaction found for order {}", orderId);
            return false;
        }

        var transaction = transactions.get(0);
        int transactionId = transaction.getTransactionId();

        if ("PAID".equals(transaction.getPaymentStatus())) {
            logger.info("Transaction {} already marked as PAID, skipping update", transactionId);
            return true;
        }

        boolean updated = transactionDAO.markAsPaid(transactionId);

        if (!updated) {
            logger.error("Failed to update transaction status for order {}", orderId);
            return false;
        }

        logger.info("Updated transaction {} to PAID for order {}", transactionId, orderId);

        decreaseStock(orderId);
        ordersDAO.approveOrder(orderId);
        logger.info("Approved order {} - Deposit paid, waiting for showroom payment", orderId);

        boolean notesUpdated = updateOrderNotes(orderId, paidAmount);
        if (!notesUpdated) {
            logger.warn("Order notes update failed, but payment was successful");
        }

        return true;
    }

    private void decreaseStock(int orderId) {
        var orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

        if (orderDetails == null || orderDetails.isEmpty()) {
            logger.error("No order details found for order {}", orderId);
            return;
        }

        logger.info("Processing stock decrease for {} items", orderDetails.size());

        for (var detail : orderDetails) {
            if (carDAO.decreaseStock(detail.getCarId(), detail.getQuantity())) {
                logger.info("Decreased stock for car ID: {} by {} units",
                        detail.getCarId(), detail.getQuantity());
            } else {
                logger.error("CRITICAL: Failed to decrease stock for car ID: {}. " +
                                "Order: {}, Quantity: {}. Manual stock adjustment required!",
                        detail.getCarId(), orderId, detail.getQuantity());
            }
        }
    }

    private boolean updateOrderNotes(int orderId, long paidAmount) {
        try {
            Order updatedOrder = ordersDAO.getOrderById(orderId);

            if (updatedOrder == null || updatedOrder.getRemainingAmount() == null) {
                logger.warn("Cannot update order notes: order or remaining amount is null");
                return false;
            }

            String updatedNotes = buildOrderNotes(paidAmount, updatedOrder.getRemainingAmount().longValue());
            boolean notesUpdated = ordersDAO.updateOrderNotes(orderId, updatedNotes);

            if (notesUpdated) {
                logger.info("Updated order notes for order {}", orderId);
                return true;
            } else {
                logger.warn("Failed to update order notes for order {}", orderId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error updating order notes for order {}", orderId, e);
            return false;
        }
    }

    // ============ HELPER METHODS ============

    private Map<String, String> collectVNPayParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            params.put(key, value);
            logger.info("VNPay param: {}={}", key, value);
        }

        return params;
    }

    private boolean isDepositOrder(Order order, int orderId) {
        if (!"DEPOSIT".equals(order.getPaymentType())) {
            logger.error("Order {} is not a DEPOSIT type order", orderId);
            return false;
        }
        return true;
    }

    private String buildOrderNotes(long paidAmount, long remainingAmount) {
        return """
            Đã thanh toán đặt cọc %,d₫ (10%%) qua VNPay thành công.
            Khách hàng cần thanh toán %,d₫ tại showroom khi nhận xe.
            Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom."""
                .formatted(paidAmount, remainingAmount);
    }

    private void setSuccessSession(HttpSession session, int orderId, PaymentResult result) {
        if (session != null) {
            session.setAttribute("paymentSuccess", true);
            session.setAttribute("paymentMessage",
                    "Đặt cọc thành công! Vui lòng đến showroom để thanh toán phần còn lại và nhận xe.");
            session.setAttribute("paymentOrderId", orderId);
            session.setAttribute("paymentAmount", result.getAmount());
            session.setAttribute("paymentTransactionNo", result.getTransactionNo());
        }
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    // ============ LOGGING ============

    private void logSeparator(String message) {
        logger.info("========================================");
        logger.info(message);
        logger.info("========================================");
    }

    private void logPaymentResult(PaymentResult result) {
        logger.info("Payment verification result:");
        logger.info("  - Success: {}", result.isSuccess());
        logger.info("  - Valid Signature: {}", result.isValidSignature());
        logger.info("  - Order ID: {}", result.getOrderId());
        logger.info("  - Amount: {}", result.getAmount());
        logger.info("  - Response Code: {}", result.getResponseCode());
        logger.info("  - Message: {}", result.getMessage());
    }
}