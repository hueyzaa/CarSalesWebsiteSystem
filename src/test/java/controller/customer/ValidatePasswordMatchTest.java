package controller.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test for validatePasswordMatch function
 * Coverage: Black Box (EP, BVA) + White Box (Statement, Decision)
 */
public class ValidatePasswordMatchTest {

    // Method under test (extracted from AuthServlet for testing)
    private String validatePasswordMatch(String password, String confirmPassword) {
        // Validate password strength
        validatePassword(password);

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }

        return password;
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ hoa");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ thường");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất một chữ số");
        }
    }

    // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

    @Test
    @DisplayName("UTCID01 - Valid password match")
    public void testValidPasswordMatch() {
        String result = validatePasswordMatch("Password123", "Password123");
        assertEquals("Password123", result);
    }

    @Test
    @DisplayName("UTCID02 - Valid complex password")
    public void testValidComplexPassword() {
        String result = validatePasswordMatch("Complex@Pass123", "Complex@Pass123");
        assertEquals("Complex@Pass123", result);
    }

    @Test
    @DisplayName("UTCID03 - Password too short")
    public void testPasswordTooShort() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("Pass1", "Pass1");
        });
        assertEquals("Mật khẩu phải có ít nhất 8 ký tự", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID04 - Password missing uppercase")
    public void testPasswordMissingUppercase() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("password123", "password123");
        });
        assertEquals("Mật khẩu phải chứa ít nhất một chữ hoa", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID05 - Password missing lowercase")
    public void testPasswordMissingLowercase() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("PASSWORD123", "PASSWORD123");
        });
        assertEquals("Mật khẩu phải chứa ít nhất một chữ thường", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID06 - Password missing digit")
    public void testPasswordMissingDigit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("Password", "Password");
        });
        assertEquals("Mật khẩu phải chứa ít nhất một chữ số", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID07 - Passwords do not match")
    public void testPasswordsDoNotMatch() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("Password123", "Password456");
        });
        assertEquals("Mật khẩu xác nhận không khớp", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID08 - Null password")
    public void testNullPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch(null, "Password123");
        });
        assertEquals("Mật khẩu không được để trống", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID09 - Empty password")
    public void testEmptyPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("", "Password123");
        });
        assertEquals("Mật khẩu không được để trống", exception.getMessage());
    }

    // ============= BLACK BOX - BOUNDARY VALUE ANALYSIS =============

    @Test
    @DisplayName("UTCID10 - Password exactly 7 chars (below boundary)")
    public void testPassword7Chars() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("Pass12A", "Pass12A");
        });
        assertEquals("Mật khẩu phải có ít nhất 8 ký tự", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID11 - Password exactly 8 chars (boundary)")
    public void testPassword8Chars() {
        String result = validatePasswordMatch("Pass123A", "Pass123A");
        assertEquals("Pass123A", result);
    }

    @Test
    @DisplayName("UTCID12 - Password exactly 9 chars (above boundary)")
    public void testPassword9Chars() {
        String result = validatePasswordMatch("Pass1234A", "Pass1234A");
        assertEquals("Pass1234A", result);
    }

    @Test
    @DisplayName("UTCID13 - Very long password (100 chars)")
    public void testVeryLongPassword() {
        String longPassword = "A".repeat(50) + "a".repeat(49) + "1";
        String result = validatePasswordMatch(longPassword, longPassword);
        assertEquals(longPassword, result);
    }

    // ============= WHITE BOX - STATEMENT COVERAGE =============

    @Test
    @DisplayName("UTCID14 - Statement: Trigger validatePassword")
    public void testStatementValidatePassword() {
        // This test ensures validatePassword() method is called
        assertDoesNotThrow(() -> {
            validatePasswordMatch("ValidPass123", "ValidPass123");
        });
    }

    @Test
    @DisplayName("UTCID15 - Statement: Trigger password.equals check")
    public void testStatementPasswordEquals() {
        // This test ensures password.equals() statement is executed
        String result = validatePasswordMatch("Password123", "Password123");
        assertNotNull(result);
    }

    // ============= WHITE BOX - DECISION COVERAGE =============

    @Test
    @DisplayName("UTCID16 - Decision: password.equals returns false")
    public void testDecisionEqualsFalse() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validatePasswordMatch("Password123", "DifferentPass123");
        });
        assertEquals("Mật khẩu xác nhận không khớp", exception.getMessage());
    }

    @Test
    @DisplayName("UTCID17 - Decision: password.equals returns true")
    public void testDecisionEqualsTrue() {
        String result = validatePasswordMatch("Password123", "Password123");
        assertEquals("Password123", result);
    }

    // ============= PARAMETERIZED TEST (Optional - for multiple similar tests) =============

    @ParameterizedTest(name = "{0} - {1}")
    @CsvSource({
            "Valid match, Password123, Password123, true",
            "Different passwords, Password123, Password456, false",
            "Short password, Pass1, Pass1, false"
    })
    @DisplayName("Parameterized test for password validation")
    public void testPasswordMatchParameterized(String testName, String password, String confirmPassword, boolean shouldPass) {
        if (shouldPass) {
            assertDoesNotThrow(() -> validatePasswordMatch(password, confirmPassword));
        } else {
            assertThrows(IllegalArgumentException.class, () -> validatePasswordMatch(password, confirmPassword));
        }
    }
}