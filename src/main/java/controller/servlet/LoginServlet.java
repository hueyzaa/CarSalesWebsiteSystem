package controller.servlet;

import dao.UserDAO;
import model.User;
import util.RateLimitFilter;
import util.ValidationUtil;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
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

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
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
            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String password = request.getParameter("password");

            if (password == null || password.trim().isEmpty()) {
                throw new ValidationException("password", "Mật khẩu không được để trống");
            }

            // Attempt login
            User user = userDAO.login(email, password);

            if (user != null) {
                // Success - reset rate limit
                RateLimitFilter.resetAttempts(email);

                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());

                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);

                // Prevent session fixation
                request.changeSessionId();

                logger.info("User logged in successfully: {}", email);
                response.sendRedirect(request.getContextPath() + "/home");

            } else {
                // Failed login - record attempt
                RateLimitFilter.recordFailedAttempt(email);
                logger.warn("Failed login attempt for email: {}", email);

                throw new ValidationException("Thông tin đăng nhập không chính xác");
            }

        } catch (ValidationException e) {
            logger.debug("Validation error in login: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error in login", e);
            handleError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }

    /**
     * Validate CSRF token
     */
    private void validateCsrfToken(HttpServletRequest request) throws ValidationException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ValidationException("Phiên làm việc đã hết hạn");
        }

        String sessionToken = (String) session.getAttribute("csrfToken");
        String requestToken = request.getParameter("csrfToken");

        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            logger.warn("CSRF token validation failed");
            throw new ValidationException("Yêu cầu không hợp lệ (CSRF)");
        }
    }

    /**
     * Handle error and show login form again
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);

        // Preserve email
        request.setAttribute("email", request.getParameter("email"));

        // Generate new CSRF token
        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}