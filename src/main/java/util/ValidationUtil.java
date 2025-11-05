package util;

import java.math.BigDecimal;
import java.time.Year;
import java.util.regex.Pattern;

/**
 * Utility class for input validation (ENHANCED VERSION)
 */
public class ValidationUtil {

    // Regex patterns (compiled once for performance)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]+$");
    private static final Pattern VIN_PATTERN = Pattern.compile("^[A-HJ-NPR-Z0-9]{17}$");
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^\\d{2}[A-Z]-?\\d{3,5}\\.?\\d{0,2}$");


    /**
     * Validate string field
     */
    public static String validateString(String input, String fieldName, int maxLength) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống");
        }

        input = input.trim();

        if (input.length() > maxLength) {
            throw new IllegalArgumentException(
                    fieldName + " quá dài (tối đa " + maxLength + " ký tự)");
        }

        return input;
    }

    /**
     * Validate String of Brand
     */
    public static String validateBrand(String input) throws Exception {
        if(input == null || input.trim().isEmpty()){
            throw new Exception(input + " hãng xe không được để trống");
        } else if(input.length() < 2 || input.length() > 50) {
            throw new Exception(input + " hãng xe không được quá ngắn hơn 2 kí tự");
        } else{
            throw new Exception("Lỗi khi thêm hãng xe");
        }
    }

    /**
     * Validate string field with alphanumeric characters only
     */
    public static String validateAlphanumeric(String input, String fieldName, int maxLength) {
        input = validateString(input, fieldName, maxLength);

        if (!ALPHANUMERIC_PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException(
                    fieldName + " chỉ được chứa chữ cái và số");
        }

        return input;
    }


    /**
     * Validate email format
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }

        email = email.trim().toLowerCase();

        if (email.length() > 255) {
            throw new IllegalArgumentException("Email quá dài");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }

        return email;
    }

    /**
     * Validate password strength
     */
    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự");
        }

        if (password.length() > 100) {
            throw new IllegalArgumentException("Mật khẩu quá dài");
        }

        // Check for at least one uppercase, lowercase, and digit
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        if (!hasUpper || !hasLower || !hasDigit) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải chứa chữ hoa, chữ thường và số");
        }
    }


    /**
     * Validate price
     */
    public static BigDecimal validatePrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Giá không được để trống");
        }

        try {
            BigDecimal price = new BigDecimal(priceStr.trim());

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Giá phải lớn hơn 0");
            }

            if (price.compareTo(new BigDecimal("999999999999")) > 0) {
                throw new IllegalArgumentException("Giá quá lớn");
            }

            // Check decimal places (max 2)
            if (price.scale() > 2) {
                throw new IllegalArgumentException(
                        "Giá chỉ được có tối đa 2 chữ số thập phân");
            }

            return price;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá không hợp lệ");
        }
    }

    /**
     * Validate positive integer
     */
    public static int validatePositiveInt(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống");
        }

        try {
            int intValue = Integer.parseInt(value.trim());

            if (intValue <= 0) {
                throw new IllegalArgumentException(fieldName + " phải lớn hơn 0");
            }

            return intValue;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " không hợp lệ");
        }
    }

    /**
     * Validate quantity
     */
    public static int validateQuantity(String quantityStr) {
        int quantity = validatePositiveInt(quantityStr, "Số lượng");

        if (quantity > 100) {
            throw new IllegalArgumentException("Số lượng tối đa là 100");
        }

        return quantity;
    }

    /**
     * Validate car year (NEW)
     */
    public static int validateYear(String yearStr) {
        if (yearStr == null || yearStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Năm sản xuất không được để trống");
        }

        try {
            int year = Integer.parseInt(yearStr.trim());
            int currentYear = Year.now().getValue();

            if (year < 1900 || year > currentYear + 1) {
                throw new IllegalArgumentException(
                        "Năm sản xuất phải từ 1900 đến " + (currentYear + 1));
            }

            return year;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Năm sản xuất không hợp lệ");
        }
    }


    /**
     * Validate status
     */
    public static String validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }

        status = status.trim().toUpperCase();

        if (!status.matches("AVAILABLE|UNAVAILABLE")) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }

        return status;
    }


    /**
     * Validate URL
     */
    public static String validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null; // URL is optional
        }

        url = url.trim();

        if (url.length() > 255) {
            throw new IllegalArgumentException("URL quá dài");
        }

        if (!url.matches("^https?://.*")) {
            throw new IllegalArgumentException("URL phải bắt đầu bằng http:// hoặc https://");
        }

        return url;
    }

    /**
     * Validate phone number
     */
    public static String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }

        phone = phone.trim().replaceAll("\\s+", "");

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "Số điện thoại không hợp lệ (phải có 10-11 chữ số)");
        }

        return phone;
    }


    /**
     * Validate VIN (Vehicle Identification Number) - NEW
     */
    public static String validateVIN(String vin) {
        if (vin == null || vin.trim().isEmpty()) {
            throw new IllegalArgumentException("Số VIN không được để trống");
        }

        vin = vin.trim().toUpperCase();

        if (vin.length() != 17) {
            throw new IllegalArgumentException("Số VIN phải có 17 ký tự");
        }

        if (!VIN_PATTERN.matcher(vin).matches()) {
            throw new IllegalArgumentException("Số VIN không hợp lệ (không chứa I, O, Q)");
        }

        return vin;
    }

    /**
     * Validate Vietnamese license plate - NEW
     */
    public static String validateLicensePlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            throw new IllegalArgumentException("Biển số xe không được để trống");
        }

        plate = plate.trim().toUpperCase().replaceAll("\\s+", "");

        // Vietnam format: 99A-999.99 or 99A-99999
        if (!LICENSE_PLATE_PATTERN.matcher(plate).matches()) {
            throw new IllegalArgumentException(
                    "Biển số xe không hợp lệ (định dạng: 29A-12345 hoặc 29A-123.45)");
        }

        return plate;
    }


    /**
     * Sanitize string input (ENHANCED - prevent XSS)
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        return input.trim()
                .replaceAll("[<>\"'`]", "")           // Remove HTML/script characters
                .replaceAll("&", "&amp;")              // Encode ampersand
                .replaceAll("javascript:", "")         // Remove javascript: protocol
                .replaceAll("on\\w+\\s*=", "")        // Remove event handlers
                .replaceAll("\\s+", " ");              // Normalize whitespace
    }

    /**
     * Validate search keyword (ENHANCED)
     */
    public static String validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "";
        }

        keyword = keyword.trim();

        if (keyword.length() > 100) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm quá dài");
        }

        // Remove SQL injection characters
        keyword = keyword.replaceAll("[';\"\\\\]", "");

        // Note: If using PreparedStatement, LIKE wildcards (%, _) are safe
        // If NOT using PreparedStatement, uncomment below:
        // keyword = keyword.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");

        return keyword;
    }
}