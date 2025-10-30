package controller.customer;

import dao.UserDAO;
import model.Customer;
import model.Staff;
import model.Admin;
import filter.RateLimitFilter;
import util.ValidationUtil;
import util.SessionUtils;
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

/**
 * LoginServlet - Handle user authentication
 * Supports Customer/Staff/Admin login with role-based redirect
 * UPDATED: Removed loyalty_points, position, department storage in session
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        logger.info("LoginServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            logger.debug("User already logged in, redirecting to home");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            validateCsrfToken(request);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String password = request.getParameter("password");

            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }

            logger.info("Login attempt for email: {}", email);

            Object userObject = userDAO.login(email, password);

            if (userObject == null) {
                RateLimitFilter.recordFailedAttempt(email);
                logger.warn("Failed login attempt for email: {}", email);
                throw new SecurityException("Thông tin đăng nhập không chính xác");
            }

            // Success
            RateLimitFilter.resetAttempts(email);
            handleSuccessfulLogin(request, response, userObject, email);

        } catch (IllegalArgumentException | SecurityException e) {
            logger.debug("Login error: {}", e.getMessage());
            handleError(request, response, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in login", e);
            handleError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }

    /**
     * Handle successful login
     * UPDATED: Removed storage of loyalty_points, position, department
     */
    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response,
                                       Object userObject, String email) throws IOException {
        HttpSession session = request.getSession();

        // Set user in session (this handles all user types)
        SessionUtils.setUser(session, userObject);

        // Prevent session fixation
        SessionUtils.preventSessionFixation(request);

        String role = SessionUtils.getUserRole(userObject);
        Integer userId = SessionUtils.getUserId(userObject);

        logger.info("User logged in: {} (role: {}, ID: {})", email, role, userId);

        // Handle redirect
        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            session.removeAttribute("loginMessage");
            logger.info("Redirecting to saved URL: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
            return;
        }

        // Role-based default redirect
        response.sendRedirect(request.getContextPath() + getDefaultRedirectByRole(role));
    }

    /**
     * Get default redirect URL based on role
     */
    private String getDefaultRedirectByRole(String role) {
        switch (role) {
            case "ADMIN":
                return "/admin/dashboard";
            case "STAFF":
                return "/staff/dashboard";
            case "CUSTOMER":
            default:
                return "/home";
        }
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
            logger.warn("CSRF token validation failed");
            throw new SecurityException("Yêu cầu không hợp lệ (CSRF)");
        }
    }

    /**
     * Handle error and redisplay login form
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("email", request.getParameter("email"));
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("LoginServlet destroyed");
    }
}