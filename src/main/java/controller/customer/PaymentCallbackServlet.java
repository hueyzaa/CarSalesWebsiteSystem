package controller.customer;

import dao.TransactionDAO;
import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
import model.Order;
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
            logger.info("========================================");
            logger.info("RECEIVED PAYMENT RETURN FROM VNPAY");
            logger.info("========================================");

            // Get all parameters from VNPay
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();

            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue()[0];
                params.put(key, value);
                logger.info("VNPay param: {}={}", key, value);
            }

            // Verify payment
            PaymentResult result = vnPayService.verifyPaymentCallback(params);

            logger.info("Payment verification result:");
            logger.info("  - Success: {}", result.isSuccess());
            logger.info("  - Valid Signature: {}", result.isValidSignature());
            logger.info("  - Order ID: {}", result.getOrderId());
            logger.info("  - Amount: {}", result.getAmount());
            logger.info("  - Response Code: {}", result.getResponseCode());
            logger.info("  - Message: {}", result.getMessage());

            if (result.isSuccess()) {
                // PAYMENT SUCCESSFUL - DEPOSIT 10% PAID
                int orderId = result.getOrderId();
                logger.info("Processing successful DEPOSIT payment for order {}", orderId);

                // Get order information
                Order order = ordersDAO.getOrderById(orderId);
                if (order == null) {
                    logger.error("Order not found: {}", orderId);
                    response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
                    return;
                }

                // Verify this is a DEPOSIT order
                if (!"DEPOSIT".equals(order.getPaymentType())) {
                    logger.error("Order {} is not a DEPOSIT type order", orderId);
                    response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
                    return;
                }

                // Get transaction for this order
                var transactions = transactionDAO.getTransactionsByOrderId(orderId);

                if (!transactions.isEmpty()) {
                    int transactionId = transactions.get(0).getTransactionId();

                    // Check if already processed
                    if ("PAID".equals(transactions.get(0).getPaymentStatus())) {
                        logger.info("Transaction {} already marked as PAID, skipping update", transactionId);
                    } else {
                        // Update transaction status to PAID
                        boolean updated = transactionDAO.markAsPaid(transactionId);

                        if (updated) {
                            logger.info("Updated transaction {} to PAID for order {}", transactionId, orderId);

                            // DECREASE STOCK after successful deposit payment
                            var orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

                            if (orderDetails != null && !orderDetails.isEmpty()) {
                                logger.info("Processing stock decrease for {} items", orderDetails.size());

                                for (var detail : orderDetails) {
                                    boolean stockUpdated = carDAO.decreaseStock(detail.getCarId(), detail.getQuantity());
                                    if (stockUpdated) {
                                        logger.info("Decreased stock for car ID: {} by {} units",
                                                detail.getCarId(), detail.getQuantity());
                                    } else {
                                        logger.error("CRITICAL: Failed to decrease stock for car ID: {}. " +
                                                        "Order: {}, Quantity: {}. Manual stock adjustment required!",
                                                detail.getCarId(), orderId, detail.getQuantity());
                                    }
                                }
                            } else {
                                logger.error("No order details found for order {}", orderId);
                            }

                            // Update order status to APPROVED (deposit paid, waiting for final payment at showroom)
                            ordersDAO.approveOrder(orderId);
                            logger.info("Approved order {} - Deposit paid, waiting for showroom payment", orderId);

                            // Update order notes
                            updateOrderNotesForDeposit(orderId, order, result.getAmount());

                        } else {
                            logger.error("Failed to update transaction status for order {}", orderId);
                        }
                    }
                } else {
                    logger.error("No transaction found for order {}", orderId);
                }

                // Set success message in session
                if (session != null) {
                    session.setAttribute("paymentSuccess", true);
                    session.setAttribute("paymentMessage", "Đặt cọc thành công! Vui lòng đến showroom để thanh toán phần còn lại và nhận xe.");
                    session.setAttribute("paymentOrderId", orderId);
                    session.setAttribute("paymentAmount", result.getAmount());
                    session.setAttribute("paymentTransactionNo", result.getTransactionNo());
                }

                logger.info("Redirecting to payment result page: success=true");
                logger.info("========================================");
                response.sendRedirect(request.getContextPath() + "/payment-result?success=true&orderId=" + orderId);

            } else {
                // PAYMENT FAILED
                logger.warn("Payment failed:");
                logger.warn("  - Response Code: {}", result.getResponseCode());
                logger.warn("  - Message: {}", result.getMessage());

                if (session != null) {
                    session.setAttribute("paymentSuccess", false);
                    session.setAttribute("paymentMessage", result.getMessage());
                    session.setAttribute("paymentResponseCode", result.getResponseCode());
                }

                logger.info("Redirecting to payment result page: success=false");
                logger.info("========================================");
                response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
            }

        } catch (Exception e) {
            logger.error("========================================");
            logger.error("CRITICAL ERROR processing payment callback", e);
            logger.error("========================================");
            e.printStackTrace();

            if (session != null) {
                session.setAttribute("error", "Lỗi xử lý kết quả thanh toán: " + e.getMessage());
            }

            response.sendRedirect(request.getContextPath() + "/payment-result?success=false");
        }
    }

    /**
     * Update order notes after successful deposit payment
     */
    private void updateOrderNotesForDeposit(int orderId, Order order, long paidAmount) {
        try {
            // Calculate remaining amount
            Order updatedOrder = ordersDAO.getOrderById(orderId);

            if (updatedOrder != null && updatedOrder.getRemainingAmount() != null) {
                String updatedNotes = String.format(
                                "Đã thanh toán đặt cọc %,d₫ (10%%) qua VNPay thành công.\n" +
                                "Khách hàng cần thanh toán %,d₫ tại showroom khi nhận xe.\n" +
                                "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                        paidAmount,
                        updatedOrder.getRemainingAmount().longValue()
                );

                boolean notesUpdated = ordersDAO.updateOrderNotes(orderId, updatedNotes);

                if (notesUpdated) {
                    logger.info("Updated order notes for order {}", orderId);
                } else {
                    logger.warn("Failed to update order notes for order {}", orderId);
                }
            }

        } catch (Exception e) {
            logger.error("Error updating order notes for order {}", orderId, e);
        }
    }
}