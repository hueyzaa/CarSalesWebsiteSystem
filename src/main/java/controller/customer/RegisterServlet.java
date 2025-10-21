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

        // Check if already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Generate CSRF token
        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Verify CSRF token
            validateCsrfToken(request);

            // Validate input
            String name = ValidationUtil.validateString(
                    request.getParameter("name"), "Họ và tên", 100);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));

            // Phone (optional)
            String phone = request.getParameter("phone");
            if (phone != null && !phone.trim().isEmpty()) {
                phone = ValidationUtil.validatePhone(phone);
            } else {
                phone = null;
            }

            // Address (optional)
            String address = request.getParameter("address");
            if (address != null && !address.trim().isEmpty()) {
                address = ValidationUtil.validateString(address, "Địa chỉ", 255);
            } else {
                address = null;
            }

            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            // Validate password
            ValidationUtil.validatePassword(password);

            // Check password match
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
            }

            // Check email exists
            if (userDAO.emailExists(email)) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            // Register user with phone and address
            boolean success = userDAO.register(name, email, password, phone, address);

            if (success) {
                logger.info("User registered successfully: {}", email);

                // Auto-login after registration
                User user = userDAO.login(email, password);

                if (user != null) {
                    createUserSession(request, user);
                    response.sendRedirect(request.getContextPath() + "/home");
                } else {
                    // Redirect to login if auto-login fails
                    HttpSession session = request.getSession();
                    session.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                    response.sendRedirect(request.getContextPath() + "/login");
                }
            } else {
                throw new RuntimeException("Đăng ký thất bại. Vui lòng thử lại.");
            }

        } catch (IllegalArgumentException e) {
            // Validation errors (name, email, phone, password mismatch, email exists)
            logger.debug("Validation error in register: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (SecurityException e) {
            // CSRF token validation error
            logger.warn("Security error in register: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (RuntimeException e) {
            // Database errors
            logger.error("Runtime error in register", e);
            handleError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error in register", e);
            handleError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
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
            logger.warn("CSRF token validation failed during registration");
            throw new SecurityException("Yêu cầu không hợp lệ (CSRF)");
        }
    }

    /**
     * Create session for user
     */
    private void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole());

        // Security: Set session timeout (30 minutes)
        session.setMaxInactiveInterval(30 * 60);

        // Security: Regenerate session ID to prevent session fixation
        request.changeSessionId();

        logger.info("User session created for: {}", user.getEmail());
    }

    /**
     * Handle error and show registration form again
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);

        // Preserve form data
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("phone", request.getParameter("phone"));
        request.setAttribute("address", request.getParameter("address"));

        // Generate new CSRF token
        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
}