package selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Login Flow
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest extends BaseTest {

    /**
     * TC05: Đăng nhập thành công với tài khoản đã verify
     */
    @Test
    @Order(1)
    @DisplayName("TC05: Đăng nhập thành công")
    public void testSuccessfulLogin() {
        System.out.println("\n========== TC05: ĐĂNG NHẬP THÀNH CÔNG ==========");

        navigateTo("/login");
        driver.findElement(By.id("email")).sendKeys("giahuy14sp@gmail.com");
        driver.findElement(By.id("password")).sendKeys("30042004Ngh@");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        System.out.println("✓ Đã submit login form");

        // Verify redirect to home
        wait.until(ExpectedConditions.urlContains("home"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("home"),
                "Không redirect đến home");
        System.out.println("PASSED: Redirect đến home thành công");

        System.out.println("========== TC05: HOÀN THÀNH ==========\n");
    }

    /**
     * TC06: Đăng nhập thất bại - Email chưa verify
     */
    @Test
    @Order(2)
    @DisplayName("TC06: Login thất bại - Email chưa verify")
    public void testLoginUnverifiedEmail() {
        System.out.println("\n========== TC06: EMAIL CHƯA VERIFY ==========");

        navigateTo("/login");
        driver.findElement(By.id("email")).sendKeys("Test@gmail.com");
        driver.findElement(By.id("password")).sendKeys("30042004Ngh@");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify error message
        WebElement errorAlert = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger"))
        );

        String errorText = errorAlert.getText();
        assertTrue(errorText.contains("chưa được xác thực"),
                "Error message không đúng");
        System.out.println("PASSED: Hiển thị lỗi email chưa verify");

        System.out.println("========== TC06: HOÀN THÀNH ==========\n");
    }

    /**
     * TC07: Đăng nhập thất bại - Sai mật khẩu
     */
    @Test
    @Order(3)
    @DisplayName("TC07: Login thất bại - Sai password")
    public void testLoginWrongPassword() {
        System.out.println("\n========== TC07: SAI MẬT KHẨU ==========");

        navigateTo("/login");

        driver.findElement(By.id("email")).sendKeys("verified@example.com");
        driver.findElement(By.id("password")).sendKeys("WrongPassword123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement errorAlert = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-danger"))
        );

        String errorText = errorAlert.getText();
        assertTrue(errorText.contains("không đúng"),
                "Error message không đúng");
        System.out.println("PASSED: Hiển thị lỗi mật khẩu sai");

        System.out.println("========== TC07: HOÀN THÀNH ==========\n");
    }
}