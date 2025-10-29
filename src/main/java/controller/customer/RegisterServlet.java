package controller.customer;

import dao.UserDAO;
import model.User;
import util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
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
            // Security validation
            validateCsrfToken(request);

            // Input validation
            String name = ValidationUtil.validateString(request.getParameter("name"), "Họ và tên", 100);
            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String phone = validateOptionalPhone(request.getParameter("phone"));
            String address = validateOptionalString(request.getParameter("address"), "Địa chỉ", 255);
            String password = validatePasswordMatch(
                    request.getParameter("password"),
                    request.getParameter("confirmPassword")
            );

            // Check email exists
            if (userDAO.emailExists(email)) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            // Register and auto-login
            if (userDAO.register(name, email, password, phone, address)) {
                logger.info("User registered: {}", email);
                User user = userDAO.login(email, password);
                createUserSession(request, user);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                throw new RuntimeException("Đăng ký thất bại. Vui lòng thử lại.");
            }

        } catch (IllegalArgumentException | SecurityException e) {
            logger.warn("Validation/Security error: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Registration error", e);
            handleError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }

    /**
     * Validate optional phone field
     */
    private String validateOptionalPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return ValidationUtil.validatePhone(phone);
    }

    /**
     * Validate optional string field
     */
    private String validateOptionalString(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return ValidationUtil.validateString(value, fieldName, maxLength);
    }

    /**
     * Validate password and confirm password match
     */
    private String validatePasswordMatch(String password, String confirmPassword) {
        ValidationUtil.validatePassword(password);

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }

        return password;
    }


    /**
     * Set CSRF token in session and request
     */
    private void setCSRFToken(HttpServletRequest request) {
        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);
    }

    /**
     * Validate CSRF token
     */
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


    /**
     * Create user session after successful registration
     */
    private void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        // Prevent session fixation
        request.changeSessionId();

        logger.info("Session created for: {}", user.getEmail());
    }


    /**
     * Handle error and redisplay form with preserved data
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {

        request.setAttribute("error", errorMessage);

        // Preserve form data (except password)
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("phone", request.getParameter("phone"));
        request.setAttribute("address", request.getParameter("address"));

        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
}