package util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayConfig {

    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_RETURN_URL = "http://localhost:8080/CarSalesWebsiteSystem/payment-callback";
    public static final String VNP_TMN_CODE = "NUB9I46D";
    public static final String VNP_HASH_SECRET = "19E3P8RMSBVMDKHU26STIOEPRXITNOHS";
    public static final String VNP_API_URL = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";
    public static final String VNP_ORDER_TYPE = "other";

    /**
     * Build payment URL - Follow VNPay's sample code exactly
     */
    public static String buildPaymentUrl(Map<String, String> params) {
        try {
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = params.get(fieldName);

                if (fieldValue != null && fieldValue.length() > 0) {
                    // ✅ Build hash data - ENCODE giống VNPay sample
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    // Build query - ENCODE
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnpSecureHash = hmacSHA512(VNP_HASH_SECRET, hashData.toString());

            // Debug
            System.out.println("=== VNPay Payment URL Debug ===");
            System.out.println("Hash Data: " + hashData.toString());
            System.out.println("Secure Hash: " + vnpSecureHash);
            System.out.println("================================");

            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

            return VNP_PAY_URL + "?" + queryUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verify payment callback from VNPay
     */
    public static boolean verifyPaymentCallback(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get("vnp_SecureHash");
            params.remove("vnp_SecureHash");
            params.remove("vnp_SecureHashType");

            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();

            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = params.get(fieldName);

                if (fieldValue != null && fieldValue.length() > 0) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(fieldValue);

                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }

            String signValue = hmacSHA512(VNP_HASH_SECRET, hashData.toString());

            // Debug
            System.out.println("=== VNPay Callback Verification ===");
            System.out.println("Hash Data: " + hashData.toString());
            System.out.println("Expected: " + signValue);
            System.out.println("Received: " + vnpSecureHash);
            System.out.println("Valid: " + signValue.equals(vnpSecureHash));
            System.out.println("====================================");

            return signValue.equals(vnpSecureHash);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generate HMAC SHA512 hash
     */
    public static String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }

            Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);

            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Generate random number string
     */
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Get VNPay formatted date
     */
    public static String getVNPayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        return formatter.format(calendar.getTime());
    }

    /**
     * Get client IP address
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}