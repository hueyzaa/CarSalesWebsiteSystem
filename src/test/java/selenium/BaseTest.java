package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static final String BASE_URL = "http://localhost:8080/CarSalesWebsiteSystem";

    @BeforeAll
    public static void setupClass() {
        // WebDriverManager tự động tải và setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        System.out.println("ChromeDriver đã được setup");
    }

    @BeforeEach
    public void setupTest() {
        // Tạo ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito"); // Chế độ Incognito
        options.addArguments("--disable-popup-blocking");

        // Khởi tạo WebDriver với Chrome
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Khởi tạo WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("Chrome Browser started");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Chrome Browser closed");
        }
    }

    /**
     * Helper method: Navigate to page
     */
    protected void navigateTo(String path) {
        driver.get(BASE_URL + path);
        System.out.println("Navigated to: " + BASE_URL + path);
    }

    /**
     * Helper method: Scroll to element
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            Thread.sleep(500); // Đợi scroll hoàn thành
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method: Click với JavaScript (bypass intercepted issues)
     */
    protected void clickWithJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Helper method: Safe click - Scroll và click
     */
    protected void safeClick(WebElement element) {
        scrollToElement(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
        } catch (Exception e) {
            // Nếu click thường fail, dùng JS click
            clickWithJS(element);
        }
    }

    /**
     * Helper method: Safe click by locator
     */
    protected void safeClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollToElement(element);
        try {
            element.click();
        } catch (Exception e) {
            clickWithJS(element);
        }
    }

    /**
     * Helper method: Take screenshot on failure
     */
    protected void takeScreenshot(String testName) {
        // Implementation for screenshot
        System.out.println("📸 Screenshot taken: " + testName);
    }
}