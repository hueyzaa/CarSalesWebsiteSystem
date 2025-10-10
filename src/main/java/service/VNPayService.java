package service;

import util.VNPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * VNPay Payment Service
 */
public class VNPayService {
    private static final Logger logger = LoggerFactory.getLogger(VNPayService.class);

    /**
     * Create payment URL for VNPay
     *
     * @param orderId Order ID
     * @param amount Amount in VND (will be multiplied by 100 for VNPay)
     * @param orderInfo Order information
     * @param ipAddress User's IP address
     * @return Payment URL
     */
    public String createPaymentUrl(int orderId, long amount, String orderInfo, String ipAddress) {
        try {
            // VNPay requires amount in smallest unit (VND * 100)
            long vnpAmount = amount * 100;

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", VNPayConfig.VNP_VERSION);
            vnpParams.put("vnp_Command", VNPayConfig.VNP_COMMAND);
            vnpParams.put("vnp_TmnCode", VNPayConfig.VNP_TMN_CODE);
            vnpParams.put("vnp_Amount", String.valueOf(vnpAmount));
            vnpParams.put("vnp_CurrCode", "VND");

            // Transaction reference (unique)
            String txnRef = orderId + "_" + System.currentTimeMillis();
            vnpParams.put("vnp_TxnRef", txnRef);

            vnpParams.put("vnp_OrderInfo", orderInfo);
            vnpParams.put("vnp_OrderType", VNPayConfig.VNP_ORDER_TYPE);
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", VNPayConfig.VNP_RETURN_URL);
            vnpParams.put("vnp_IpAddr", ipAddress);

            // Create date and expire date
            String vnpCreateDate = VNPayConfig.getVNPayDate();
            vnpParams.put("vnp_CreateDate", vnpCreateDate);

            // Expire after 15 minutes
            java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Etc/GMT+7"));
            calendar.add(java.util.Calendar.MINUTE, 15);
            String vnpExpireDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
            vnpParams.put("vnp_ExpireDate", vnpExpireDate);

            // Debug logging
            logger.info("=== VNPay Payment Parameters ===");
            logger.info("Order ID: {}", orderId);
            logger.info("Amount: {} VND (VNPay: {})", amount, vnpAmount);
            logger.info("TxnRef: {}", txnRef);
            logger.info("TMN Code: {}", VNPayConfig.VNP_TMN_CODE);

            // Build payment URL
            String paymentUrl = VNPayConfig.buildPaymentUrl(vnpParams);

            logger.info("Created VNPay payment URL for order {}: txnRef={}", orderId, txnRef);
            logger.info("Payment URL length: {}", paymentUrl.length());

            return paymentUrl;

        } catch (Exception e) {
            logger.error("Error creating VNPay payment URL for order {}", orderId, e);
            return null;
        }
    }

    /**
     * Verify payment callback from VNPay
     *
     * @param params Parameters from VNPay callback
     * @return true if payment is valid and successful
     */
    public PaymentResult verifyPaymentCallback(Map<String, String> params) {
        PaymentResult result = new PaymentResult();

        try {
            // Verify signature
            boolean isValidSignature = VNPayConfig.verifyPaymentCallback(new HashMap<>(params));
            result.setValidSignature(isValidSignature);

            if (!isValidSignature) {
                logger.warn("Invalid VNPay signature");
                result.setSuccess(false);
                result.setMessage("Chữ ký không hợp lệ");
                return result;
            }

            // Get response code
            String responseCode = params.get("vnp_ResponseCode");
            result.setResponseCode(responseCode);

            // Get transaction info
            String txnRef = params.get("vnp_TxnRef");
            result.setTransactionRef(txnRef);

            // Extract order ID from txnRef (format: orderId_timestamp)
            if (txnRef != null && txnRef.contains("_")) {
                String orderIdStr = txnRef.split("_")[0];
                result.setOrderId(Integer.parseInt(orderIdStr));
            }

            // Get amount (divide by 100 to get VND)
            String amountStr = params.get("vnp_Amount");
            if (amountStr != null) {
                long amount = Long.parseLong(amountStr) / 100;
                result.setAmount(amount);
            }

            // Get transaction number from VNPay
            result.setTransactionNo(params.get("vnp_TransactionNo"));
            result.setBankCode(params.get("vnp_BankCode"));
            result.setPayDate(params.get("vnp_PayDate"));

            // Check if payment is successful (response code 00)
            if ("00".equals(responseCode)) {
                result.setSuccess(true);
                result.setMessage("Giao dịch thành công");
                logger.info("Payment successful: txnRef={}, amount={}", txnRef, result.getAmount());
            } else {
                result.setSuccess(false);
                result.setMessage(getResponseMessage(responseCode));
                logger.warn("Payment failed: txnRef={}, responseCode={}", txnRef, responseCode);
            }

        } catch (Exception e) {
            logger.error("Error verifying VNPay payment callback", e);
            result.setSuccess(false);
            result.setMessage("Lỗi xử lý callback");
        }

        return result;
    }

    /**
     * Get response message by response code
     */
    private String getResponseMessage(String responseCode) {
        switch (responseCode) {
            case "00":
                return "Giao dịch thành công";
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10":
                return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13":
                return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51":
                return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65":
                return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì.";
            case "79":
                return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
            default:
                return "Giao dịch không thành công";
        }
    }

    /**
     * Payment Result class
     */
    public static class PaymentResult {
        private boolean success;
        private boolean validSignature;
        private String message;
        private String responseCode;
        private String transactionRef;
        private int orderId;
        private long amount;
        private String transactionNo;
        private String bankCode;
        private String payDate;

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public boolean isValidSignature() { return validSignature; }
        public void setValidSignature(boolean validSignature) { this.validSignature = validSignature; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getResponseCode() { return responseCode; }
        public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

        public String getTransactionRef() { return transactionRef; }
        public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

        public int getOrderId() { return orderId; }
        public void setOrderId(int orderId) { this.orderId = orderId; }

        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }

        public String getTransactionNo() { return transactionNo; }
        public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }

        public String getBankCode() { return bankCode; }
        public void setBankCode(String bankCode) { this.bankCode = bankCode; }

        public String getPayDate() { return payDate; }
        public void setPayDate(String payDate) { this.payDate = payDate; }
    }
}