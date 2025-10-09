package util;

import exception.ValidationException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {

    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]+$");

    /**
     * Validate string field
     */
    public static String validateString(String input, String fieldName, int maxLength)
            throws ValidationException {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(fieldName, fieldName + " không được để trống");
        }

        input = input.trim();

        if (input.length() > maxLength) {
            throw new ValidationException(fieldName,
                    fieldName + " quá dài (tối đa " + maxLength + " ký tự)");
        }

        return input;
    }

    /**
     * Validate string field with allowed characters
     */
    public static String validateAlphanumeric(String input, String fieldName, int maxLength)
            throws ValidationException {
        input = validateString(input, fieldName, maxLength);

        if (!ALPHANUMERIC_PATTERN.matcher(input).matches()) {
            throw new ValidationException(fieldName,
                    fieldName + " chỉ được chứa chữ cái và số");
        }

        return input;
    }

    /**
     * Validate email format
     */
    public static String validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("email", "Email không được để trống");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 255) {
            throw new ValidationException("email", "Email quá dài");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("email", "Email không hợp lệ");
        }

        return email;
    }

    /**
     * Validate password strength
     */
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "Mật khẩu không được để trống");
        }

        if (password.length() < 6) {
            throw new ValidationException("password",
                    "Mật khẩu phải có ít nhất 6 ký tự");
        }

        if (password.length() > 100) {
            throw new ValidationException("password", "Mật khẩu quá dài");
        }

        // Check for at least one uppercase, lowercase, and digit
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        if (!hasUpper || !hasLower || !hasDigit) {
            throw new ValidationException("password",
                    "Mật khẩu phải chứa chữ hoa, chữ thường và số");
        }
    }

    /**
     * Validate price
     */
    public static BigDecimal validatePrice(String priceStr) throws ValidationException {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            throw new ValidationException("price", "Giá không được để trống");
        }

        try {
            BigDecimal price = new BigDecimal(priceStr.trim());

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("price", "Giá phải lớn hơn 0");
            }

            if (price.compareTo(new BigDecimal("999999999999")) > 0) {
                throw new ValidationException("price", "Giá quá lớn");
            }

            // Check decimal places (max 2)
            if (price.scale() > 2) {
                throw new ValidationException("price",
                        "Giá chỉ được có tối đa 2 chữ số thập phân");
            }

            return price;

        } catch (NumberFormatException e) {
            throw new ValidationException("price", "Giá không hợp lệ");
        }
    }

    /**
     * Validate positive integer
     */
    public static int validatePositiveInt(String value, String fieldName)
            throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName, fieldName + " không được để trống");
        }

        try {
            int intValue = Integer.parseInt(value.trim());

            if (intValue <= 0) {
                throw new ValidationException(fieldName, fieldName + " phải lớn hơn 0");
            }

            return intValue;

        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName, fieldName + " không hợp lệ");
        }
    }

    /**
     * Validate quantity
     */
    public static int validateQuantity(String quantityStr) throws ValidationException {
        int quantity = validatePositiveInt(quantityStr, "Số lượng");

        if (quantity > 100) {
            throw new ValidationException("quantity", "Số lượng tối đa là 100");
        }

        return quantity;
    }

    /**
     * Validate status
     */
    public static String validateStatus(String status) throws ValidationException {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("status", "Trạng thái không được để trống");
        }

        status = status.trim().toUpperCase();

        if (!status.matches("AVAILABLE|UNAVAILABLE")) {
            throw new ValidationException("status", "Trạng thái không hợp lệ");
        }

        return status;
    }

    /**
     * Validate URL
     */
    public static String validateUrl(String url) throws ValidationException {
        if (url == null || url.trim().isEmpty()) {
            return null; // URL is optional
        }

        url = url.trim();

        if (url.length() > 255) {
            throw new ValidationException("url", "URL quá dài");
        }

        if (!url.matches("^https?://.*")) {
            throw new ValidationException("url", "URL phải bắt đầu bằng http:// hoặc https://");
        }

        return url;
    }

    /**
     * Sanitize string input (remove dangerous characters)
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        return input.trim()
                .replaceAll("[<>\"'`]", "") // Remove HTML/script characters
                .replaceAll("\\s+", " "); // Normalize whitespace
    }

    /**
     * Validate search keyword
     */
    public static String validateSearchKeyword(String keyword) throws ValidationException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "";
        }

        keyword = keyword.trim();

        if (keyword.length() > 100) {
            throw new ValidationException("keyword", "Từ khóa tìm kiếm quá dài");
        }

        // Remove SQL special characters
        keyword = keyword.replaceAll("[';\"\\\\%_]", "");

        return keyword;
    }

    /**
     * Validate phone number
     */
    public static String validatePhone(String phone) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("phone", "Số điện thoại không được để trống");
        }

        phone = phone.trim().replaceAll("\\s+", "");

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("phone",
                    "Số điện thoại không hợp lệ (phải có 10-11 chữ số)");
        }

        return phone;
    }
}