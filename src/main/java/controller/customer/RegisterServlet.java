package controller.customer;

import dao.UserDAO;
import model.Customer;
import util.ValidationUtil;
import util.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * RegisterServlet - Handle customer registration
 * Features: Input validation, CSRF protection, auto-login
 * UPDATED: No changes needed - already compatible with new schema
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        logger.info("RegisterServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect if already logged in
        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            validateCsrfToken(request);

            // Validate inputs
            String name = ValidationUtil.validateString(request.getParameter("name"), "Họ và tên", 100);
            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String phone = validateOptional(request.getParameter("phone"), ValidationUtil::validatePhone);
            String address = validateOptional(request.getParameter("address"), s ->
                    ValidationUtil.validateString(s, "Địa chỉ", 255));
            String password = validatePasswordMatch(
                    request.getParameter("password"),
                    request.getParameter("confirmPassword")
            );

            // Check email exists
            if (userDAO.emailExists(email)) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            // Register customer (oauthProvider = null for normal registration)
            if (!userDAO.registerCustomer(name, email, password, phone, address, null)) {
                throw new RuntimeException("Đăng ký thất bại. Vui lòng thử lại.");
            }

            logger.info("Customer registered: {}", email);

            // Auto-login
            Customer customer = (Customer) userDAO.login(email, password);
            if (customer == null) {
                throw new RuntimeException("Không thể tải thông tin tài khoản");
            }

            // Create session
            SessionUtils.setUser(request.getSession(), customer);
            SessionUtils.preventSessionFixation(request);

            logger.info("Customer auto-logged in: {} (ID: {})", email, customer.getCustomerId());
            response.sendRedirect(request.getContextPath() + "/home?registered=true");

        } catch (IllegalArgumentException | SecurityException e) {
            logger.warn("Validation error: {}", e.getMessage());
            handleError(request, response, e.getMessage());
        } catch (Exception e) {
            logger.error("Registration error", e);
            handleError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }

    // VALIDATION

    @FunctionalInterface
    private interface Validator {
        String validate(String input);
    }

    private String validateOptional(String value, Validator validator) {
        return (value == null || value.trim().isEmpty()) ? null : validator.validate(value);
    }

    private String validatePasswordMatch(String password, String confirmPassword) {
        ValidationUtil.validatePassword(password);
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
        return password;
    }

    // CSRF

    private void setCSRFToken(HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute("csrfToken", token);
        request.setAttribute("csrfToken", token);
    }

    private void validateCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SecurityException("Phiên làm việc đã hết hạn");
        }

        String sessionToken = (String) session.getAttribute("csrfToken");
        String requestToken = request.getParameter("csrfToken");

        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            throw new SecurityException("Yêu cầu không hợp lệ (CSRF)");
        }
    }

    // ERROR HANDLING

    private void handleError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {

        request.setAttribute("error", error);

        // Preserve form data (except password)
        String[] fields = {"name", "email", "phone", "address"};
        for (String field : fields) {
            request.setAttribute(field, request.getParameter(field));
        }

        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("RegisterServlet destroyed");
    }
}