package controller.customer;

import dao.AuthDAO;
import model.Customer;
import service.EmailService;
import util.SessionUtils;
import util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * AuthServlet - Handles all authentication routes
 */
@WebServlet(urlPatterns = {"/register", "/verify", "/login", "/logout", "/forgot-password", "/reset-password"})
public class AuthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);
    private AuthDAO authDAO;
    private EmailService emailService;

    @Override
    public void init() throws ServletException {
        super.init();
        authDAO = new AuthDAO();
        emailService = new EmailService();
        logger.info("AuthServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/register":
                showRegisterPage(request, response);
                break;
            case "/verify":
                handleVerifyEmail(request, response);
                break;
            case "/login":
                showLoginPage(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/forgot-password":
                showForgotPasswordPage(request, response);
                break;
            case "/reset-password":
                showResetPasswordPage(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/register":
                handleRegister(request, response);
                break;
            case "/login":
                handleLogin(request, response);
                break;
            case "/forgot-password":
                handleForgotPassword(request, response);
                break;
            case "/reset-password":
                handleResetPassword(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ============================================
    // REGISTER
    // ============================================

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            validateCsrfToken(request);

            String name = ValidationUtil.validateString(request.getParameter("name"), "Họ và tên", 100);
            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String password = validatePasswordMatch(
                    request.getParameter("password"),
                    request.getParameter("confirmPassword")
            );

            if (authDAO.emailExists(email)) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            String token = UUID.randomUUID().toString();
            String ip = getClientIP(request);
            String userAgent = request.getHeader("User-Agent");

            Map<String, Object> result = authDAO.register(email, password, name, phone, address, token, ip, userAgent);

            if ((boolean) result.get("success")) {
                // Send verification email
                try {
                    String verifyUrl = buildUrl(request, "/verify?token=" + token);
                    emailService.sendVerificationEmail(request, email, name, verifyUrl);
                    logger.info("Verification email sent to: {}", email);
                } catch (Exception e) {
                    logger.error("Failed to send verification email", e);
                }

                request.getSession().setAttribute("registeredEmail", email);
                request.getRequestDispatcher("/WEB-INF/views/verification-pending.jsp")
                        .forward(request, response);
            } else {
                throw new RuntimeException((String) result.get("message"));
            }

        } catch (Exception e) {
            logger.warn("Register error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/register.jsp", e.getMessage());
        }
    }

    // ============================================
    // EMAIL VERIFICATION
    // ============================================

    private void handleVerifyEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("/WEB-INF/views/verify-error.jsp").forward(request, response);
            return;
        }

        String ip = getClientIP(request);
        Map<String, Object> result = authDAO.verifyEmail(token, ip);

        if ((boolean) result.get("success")) {
            request.setAttribute("success", "Email đã được xác thực thành công!");
            request.getRequestDispatcher("/WEB-INF/views/verify-success.jsp").forward(request, response);
        } else {
            request.setAttribute("error", result.get("message"));
            request.getRequestDispatcher("/WEB-INF/views/verify-error.jsp").forward(request, response);
        }
    }

    // ============================================
    // LOGIN
    // ============================================

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            validateCsrfToken(request);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String password = request.getParameter("password");

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }

            Customer customer = authDAO.login(email, password);

            if (customer == null) {
                throw new IllegalArgumentException("Email hoặc mật khẩu không đúng");
            }

            // Check if email is verified
            if (!customer.isEmailVerified()) {
                request.setAttribute("needsVerification", true);
                request.setAttribute("email", email);
                throw new IllegalArgumentException("Vui lòng xác thực email trước khi đăng nhập");
            }

            // Create session
            HttpSession session = request.getSession();
            SessionUtils.setUser(session, customer);
            SessionUtils.preventSessionFixation(request);

            logger.info("User logged in: {}", email);

            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (Exception e) {
            logger.warn("Login error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/login.jsp", e.getMessage());
        }
    }

    // ============================================
    // LOGOUT
    // ============================================

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("User logged out");
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

    // ============================================
    // FORGOT PASSWORD
    // ============================================

    private void showForgotPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(request, response);
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            validateCsrfToken(request);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));

            if (!authDAO.emailExists(email)) {
                // Security: Don't reveal if email exists
                request.setAttribute("success", "Nếu email tồn tại, link đặt lại mật khẩu đã được gửi");
                setCSRFToken(request);
                request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(request, response);
                return;
            }

            String token = UUID.randomUUID().toString();
            String ip = getClientIP(request);
            String userAgent = request.getHeader("User-Agent");

            Map<String, Object> result = authDAO.requestPasswordReset(email, token, ip, userAgent);

            if ((boolean) result.get("success")) {
                Customer customer = authDAO.getCustomerByEmail(email);
                String resetUrl = buildUrl(request, "/reset-password?token=" + token);
                emailService.sendPasswordResetEmail(request, email, customer.getName(), resetUrl);
                logger.info("Password reset email sent to: {}", email);
            }

            request.setAttribute("success", "Nếu email tồn tại, link đặt lại mật khẩu đã được gửi");
            setCSRFToken(request);
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(request, response);

        } catch (Exception e) {
            logger.warn("Forgot password error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/forgot-password.jsp", e.getMessage());
        }
    }

    // ============================================
    // RESET PASSWORD
    // ============================================

    private void showResetPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("/WEB-INF/views/reset-error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("token", token);
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(request, response);
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            validateCsrfToken(request);

            String token = request.getParameter("token");
            String password = validatePasswordMatch(
                    request.getParameter("password"),
                    request.getParameter("confirmPassword")
            );

            String ip = getClientIP(request);
            Map<String, Object> result = authDAO.resetPassword(token, password, ip);

            if ((boolean) result.get("success")) {
                request.setAttribute("success", "Mật khẩu đã được đặt lại thành công!");
                request.getRequestDispatcher("/WEB-INF/views/reset-success.jsp").forward(request, response);
            } else {
                throw new RuntimeException((String) result.get("message"));
            }

        } catch (Exception e) {
            logger.warn("Reset password error: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.setAttribute("token", request.getParameter("token"));
            setCSRFToken(request);
            request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(request, response);
        }
    }

    // ============================================
    // UTILITIES
    // ============================================

    private String validatePasswordMatch(String password, String confirmPassword) {
        ValidationUtil.validatePassword(password);
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
        return password;
    }

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

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String buildUrl(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        if ((scheme.equals("http") && serverPort == 80) ||
                (scheme.equals("https") && serverPort == 443)) {
            return scheme + "://" + serverName + contextPath + path;
        }
        return scheme + "://" + serverName + ":" + serverPort + contextPath + path;
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String view, String error) throws ServletException, IOException {
        request.setAttribute("error", error);

        // Preserve form data
        String[] fields = {"name", "email", "phone", "address"};
        for (String field : fields) {
            String value = request.getParameter(field);
            if (value != null) {
                request.setAttribute(field, value);
            }
        }

        setCSRFToken(request);
        request.getRequestDispatcher(view).forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("AuthServlet destroyed");
    }
}