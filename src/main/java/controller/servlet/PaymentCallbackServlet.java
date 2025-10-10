package controller.servlet;

import dao.TransactionDAO;
import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
import service.VNPayService;
import service.VNPayService.PaymentResult;
import jakarta.servlet.ServletException;
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
    public void init() throws ServletException {
        vnPayService = new VNPayService();
        transactionDAO = new TransactionDAO();
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        try {
            logger.info("Received payment callback from VNPay");

            // Get all parameters from VNPay callback
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();

            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue()[0];
                params.put(key, value);
                logger.debug("VNPay param: {}={}", key, value);
            }

            // Verify payment
            PaymentResult result = vnPayService.verifyPaymentCallback(params);

            logger.info("Payment verification result: success={}, orderId={}, amount={}",
                    result.isSuccess(), result.getOrderId(), result.getAmount());

            if (result.isSuccess()) {
                // Payment successful - Update transaction status to PAID
                int orderId = result.getOrderId();

                // Get the transaction for this order
                var transactions = transactionDAO.getTransactionsByOrderId(orderId);
                if (!transactions.isEmpty()) {
                    int transactionId = transactions.get(0).getTransactionId();

                    // Update transaction status to PAID
                    boolean updated = transactionDAO.markAsPaid(transactionId);

                    if (updated) {
                        logger.info("Updated transaction {} status to PAID for order {}",
                                transactionId, orderId);

                        // Update order status to APPROVED
                        ordersDAO.approveOrder(orderId);
                        logger.info("Approved order {}", orderId);

                        // ✅ GIẢM STOCK SAU KHI THANH TOÁN THÀNH CÔNG
                        var orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

                        if (orderDetails != null && !orderDetails.isEmpty()) {
                            logger.info("Processing stock decrease for {} items in order {}",
                                    orderDetails.size(), orderId);

                            for (var detail : orderDetails) {
                                boolean stockUpdated = carDAO.decreaseStock(detail.getCarId(), detail.getQuantity());
                                if (stockUpdated) {
                                    logger.info("Successfully decreased stock for car ID: {} by {} units after payment",
                                            detail.getCarId(), detail.getQuantity());
                                } else {
                                    logger.error("CRITICAL: Failed to decrease stock for car ID: {} after successful payment. " +
                                                    "Order: {}, Quantity: {}. Manual stock adjustment required!",
                                            detail.getCarId(), orderId, detail.getQuantity());
                                }
                            }
                        } else {
                            logger.error("No order details found for order {}", orderId);
                        }

                        // Set success message
                        if (session != null) {
                            session.setAttribute("paymentSuccess", true);
                            session.setAttribute("paymentMessage", result.getMessage());
                            session.setAttribute("paymentOrderId", orderId);
                            session.setAttribute("paymentAmount", result.getAmount());
                            session.setAttribute("paymentTransactionNo", result.getTransactionNo());
                        }
                    } else {
                        logger.error("Failed to update transaction status for order {}", orderId);
                    }
                } else {
                    logger.error("No transaction found for order {}", orderId);
                }

                // Redirect to success page
                response.sendRedirect(request.getContextPath() + "/payment-result?success=true&orderId=" + orderId);

            } else {
                // Payment failed
                logger.warn("Payment failed: {}", result.getMessage());

                if (session != null) {
                    session.setAttribute("paymentSuccess", false);
                    session.setAttribute("paymentMessage", result.getMessage());
                    session.setAttribute("paymentResponseCode", result.getResponseCode());
                }

                // Redirect to failure page
                response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
            }

        } catch (Exception e) {
            logger.error("Error processing payment callback", e);

            if (session != null) {
                session.setAttribute("error", "Lỗi xử lý kết quả thanh toán!");
            }

            response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
        }
    }
}