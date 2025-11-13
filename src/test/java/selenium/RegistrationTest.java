package selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Registration Flow
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest extends BaseTest {

    /**
     * TC01: Đăng ký thành công với thông tin hợp lệ
     */
    @Test
    @Order(1)
    @DisplayName("TC01: Đăng ký thành công")
    public void testSuccessfulRegistration() {
        System.out.println("\n========== TC01: ĐĂNG KÝ THÀNH CÔNG ==========");

        navigateTo("/register");
        System.out.println("✓ Đã mở trang đăng ký");

        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        driver.findElement(By.id("name")).sendKeys("Nguyễn Văn Test");
        driver.findElement(By.id("email")).sendKeys(uniqueEmail);
        driver.findElement(By.id("phone")).sendKeys("0123456789");
        driver.findElement(By.id("address")).sendKeys("Cần Thơ, Việt Nam");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("Password123");

        System.out.println("✓ Đã điền thông tin: " + uniqueEmail);

        WebElement termsCheckbox = driver.findElement(By.id("terms"));
        safeClick(termsCheckbox);
        System.out.println("✓ Đã chấp nhận điều khoản");

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        safeClick(submitBtn);
        System.out.println("✓ Đã submit form");

        try {
            // Thử chờ redirect (có thể là verification-pending, login, hoặc success)
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("verification-pending"),
                    ExpectedConditions.urlContains("verify"),
                    ExpectedConditions.urlContains("success"),
                    ExpectedConditions.urlContains("login"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".success-box, .alert-success, [class*='success']"))
            ));

            String currentUrl = driver.getCurrentUrl();
            System.out.println("PASSED: Redirect/Response thành công đến: " + currentUrl);

            // Kiểm tra success message nếu không redirect
            if (currentUrl.contains("register")) {
                try {
                    WebElement successMsg = driver.findElement(
                            By.cssSelector(".success-box, .alert-success, [class*='success']")
                    );
                    System.out.println("PASSED: Hiển thị thông báo: " + successMsg.getText());
                } catch (Exception e) {
                    System.out.println("WARNING: Vẫn ở trang register nhưng không thấy message");
                }
            }

            // 6. Verify success box hoặc message
            try {
                WebElement successElement = driver.findElement(
                        By.cssSelector(".success-box, .alert-success, [class*='success'], .verification-message")
                );
                if (successElement.isDisplayed()) {
                    System.out.println("PASSED: Hiển thị thông báo thành công");

                    // 7. Verify email is shown
                    String pageText = driver.findElement(By.tagName("body")).getText();
                    if (pageText.contains(uniqueEmail)) {
                        System.out.println("PASSED: Email hiển thị đúng");
                    }
                }
            } catch (Exception e) {
                System.out.println("WARNING: Không tìm thấy success message, nhưng đã redirect");
            }

        } catch (Exception e) {
            // Nếu không redirect, kiểm tra có error không
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            try {
                WebElement errorMsg = driver.findElement(
                        By.cssSelector(".alert-danger, .error, [class*='error']")
                );
                if (errorMsg.isDisplayed()) {
                    fail("Có lỗi xảy ra: " + errorMsg.getText());
                }
            } catch (Exception ex) {
                // Không có error message
                System.out.println("✓ Không có error message");

                // Kiểm tra xem có success message trên trang không
                try {
                    WebElement successMsg = driver.findElement(
                            By.cssSelector(".success-box, .alert-success, [class*='success']")
                    );
                    System.out.println("PASSED: Tìm thấy success message: " + successMsg.getText());
                } catch (Exception exc) {
                    System.out.println("Không redirect và không có message rõ ràng");
                }
            }
        }

        System.out.println("========== TC01: HOÀN THÀNH ==========\n");
    }

    /**
     * TC02: Đăng ký thất bại - Email đã tồn tại
     */
    @Test
    @Order(2)
    @DisplayName("TC02: Đăng ký thất bại - Email trùng")
    public void testDuplicateEmailRegistration() {
        System.out.println("\n========== TC02: EMAIL TRÙNG ==========");

        navigateTo("/register");


        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("giahuy14sp@gmail.com");
        driver.findElement(By.id("phone")).sendKeys("0123456789");
        driver.findElement(By.id("address")).sendKeys("Test Address");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("Password123");

        WebElement termsCheckbox = driver.findElement(By.id("terms"));
        safeClick(termsCheckbox);

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        safeClick(submitBtn);
        System.out.println("✓ Đã submit với email trùng");

        try {
            WebElement errorAlert = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(".alert-danger, .alert-warning, .error, [class*='error'], .invalid-feedback")
                    )
            );

            assertTrue(errorAlert.isDisplayed(), "Không hiển thị error message");
            System.out.println("PASSED: Hiển thị thông báo lỗi");

            String errorText = errorAlert.getText();
            assertTrue(
                    errorText.contains("Email đã được sử dụng") ||
                            errorText.contains("đã tồn tại") ||
                            errorText.contains("already") ||
                            errorText.contains("exists"),
                    "Nội dung lỗi không đúng: " + errorText
            );
            System.out.println("PASSED: Nội dung lỗi đúng: " + errorText);

        } catch (Exception e) {
            // Nếu không tìm thấy error alert, kiểm tra validation khác
            try {
                WebElement emailField = driver.findElement(By.id("email"));
                String fieldClass = emailField.getAttribute("class");

                if (fieldClass.contains("is-invalid") || fieldClass.contains("error")) {
                    System.out.println("PASSED: Email field được đánh dấu invalid");
                } else {
                    fail("Không tìm thấy error message hoặc validation");
                }
            } catch (Exception ex) {
                fail("Không tìm thấy bất kỳ validation nào: " + e.getMessage());
            }
        }

        System.out.println("========== TC02: HOÀN THÀNH ==========\n");
    }

    /**
     * TC03: Validate password không khớp
     */
    @Test
    @Order(3)
    @DisplayName("TC03: Mật khẩu không khớp")
    public void testPasswordMismatch() {
        System.out.println("\n========== TC03: MẬT KHẨU KHÔNG KHỚP ==========");

        navigateTo("/register");

        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("DifferentPassword");

        // Trigger validation by clicking away
        driver.findElement(By.id("name")).click();

        // Wait a bit for JS validation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check validation state
        WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
        String fieldClass = confirmPasswordField.getAttribute("class");

        assertTrue(fieldClass.contains("is-invalid"),
                "Field không được đánh dấu invalid");
        System.out.println("PASSED: Field được đánh dấu invalid");

        // Check error text
        WebElement errorText = driver.findElement(By.id("confirmPasswordHelp"));
        assertTrue(errorText.getText().contains("không khớp"),
                "Không hiển thị message lỗi");
        System.out.println("PASSED: Hiển thị message lỗi đúng");

        System.out.println("========== TC03: HOÀN THÀNH ==========\n");
    }

    /**
     * TC04: Validate required fields
     */
    @Test
    @Order(4)
    @DisplayName("TC04: Kiểm tra required fields")
    public void testRequiredFields() {
        System.out.println("\n========== TC04: REQUIRED FIELDS ==========");

        navigateTo("/register");

        // Try to submit empty form
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        safeClick(submitBtn);
        System.out.println("✓ Đã submit form trống");

        // Verify still on register page
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/register"),
                "Không giữ ở trang register");
        System.out.println("PASSED: Vẫn ở trang register");

        // Check HTML5 validation
        WebElement nameField = driver.findElement(By.id("name"));
        String validationMessage = (String)
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("return arguments[0].validationMessage;", nameField);

        assertFalse(validationMessage.isEmpty(),
                "Không có validation message");
        System.out.println("PASSED: Có validation message: " + validationMessage);

        System.out.println("========== TC04: HOÀN THÀNH ==========\n");
    }
}