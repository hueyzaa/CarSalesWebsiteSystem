package controller.customer;

import dao.AuthDAO;
import model.User;
import service.EmailService;
import util.SessionUtils;
import util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * AuthServlet - Handles all authentication routes
 * UPDATED: Stateless email verification (no session dependency)
 * Version: 2.0 - Fixed for cross-device verification
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
        logger.info("AuthServlet initialized - Stateless verification enabled");
    }

    private boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    private boolean isStaff(User user) {
        return user != null && "STAFF".equalsIgnoreCase(user.getRole());
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
    // REGISTRATION (Stateless - No Session Dependency)
    // ============================================

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/Customer/register.jsp").forward(request, response);
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

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // Register user in database with email_verified = 0
            int userId = authDAO.registerCustomerUnverified(email, hashedPassword, name, phone, address);

            if (userId <= 0) {
                throw new RuntimeException("Không thể tạo tài khoản. Vui lòng thử lại.");
            }

            // Generate verification token
            String token = UUID.randomUUID().toString();
            String ip = getClientIP(request);
            String userAgent = request.getHeader("User-Agent");

            Map<String, Object> tokenResult = authDAO.generateEmailVerificationToken(
                    userId, token, ip, userAgent
            );

            if (!(boolean) tokenResult.get("success")) {
                throw new RuntimeException("Không thể tạo mã xác thực. Vui lòng thử lại.");
            }

            // Send verification email
            String verifyUrl = buildUrl(request, "/verify?token=" + token);
            emailService.sendVerificationEmail(request, email, name, verifyUrl);
            logger.info("Verification email sent to: {}", email);

            request.setAttribute("registeredEmail", email);
            request.setAttribute("registeredName", name);

            request.getRequestDispatcher("/WEB-INF/views/Customer/verification-pending.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.warn("Register error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/Customer/register.jsp", e.getMessage());
        }
    }

    // ============================================
    // EMAIL VERIFICATION (Stateless - Works from Any Device)
    // ============================================

    private void handleVerifyEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            showVerifyError(request, response, "Token không hợp lệ");
            return;
        }

        try {
            String ip = getClientIP(request);
            Map<String, Object> result = authDAO.verifyEmail(token, ip);

            if ((boolean) result.get("success")) {
                int userId = (int) result.get("userId");

                User user = authDAO.getUserById(userId);

                if (user == null) {
                    logger.error("User not found after successful verification: {}", userId);
                    showVerifyError(request, response, "Không tìm thấy thông tin người dùng");
                    return;
                }

                logger.info("Email verified successfully for user: {} (ID: {})", user.getEmail(), userId);

                // Set attributes for success page
                request.setAttribute("success", "Email đã được xác thực thành công!");
                request.setAttribute("email", user.getEmail());
                request.setAttribute("userName", user.getName());

                request.getRequestDispatcher("/WEB-INF/views/Customer/verify-success.jsp")
                        .forward(request, response);
            } else {
                String message = (String) result.get("message");
                showVerifyError(request, response, message);
            }

        } catch (Exception e) {
            logger.error("Verification error for token: {}", token, e);
            showVerifyError(request, response, "Đã xảy ra lỗi. Vui lòng thử lại.");
        }
    }

    private void showVerifyError(HttpServletRequest request, HttpServletResponse response,
                                 String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/Customer/verify-error.jsp").forward(request, response);
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
        request.getRequestDispatcher("/WEB-INF/views/Customer/login.jsp").forward(request, response);
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

            User user = authDAO.login(email, password);

            if (user == null) {
                // Check if email is not verified
                User unverifiedUser = authDAO.getUserByEmail(email);
                if (unverifiedUser != null && !unverifiedUser.isEmailVerified()) {
                    throw new IllegalArgumentException("Email chưa được xác thực. Vui lòng kiểm tra email của bạn.");
                }
                throw new IllegalArgumentException("Email hoặc mật khẩu không đúng");
            }

            HttpSession session = request.getSession();
            SessionUtils.setUser(session, user);
            SessionUtils.preventSessionFixation(request);

            logger.info("User logged in: {} (Role: {})", email, user.getRole());

            String redirectUrl = determineRedirectUrl(request, user);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            logger.warn("Login error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/Customer/login.jsp", e.getMessage());
        }
    }

    private String determineRedirectUrl(HttpServletRequest request, User user) {
        if (isAdmin(user)) {
            return request.getContextPath() + "/Admin/dashboard";
        } else if (isStaff(user)) {
            return request.getContextPath() + "/staff/dashboard";
        } else {
            String redirect = request.getParameter("redirect");
            if (redirect != null && !redirect.isEmpty()) {
                return request.getContextPath() + redirect;
            }
            return request.getContextPath() + "/home";
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionUtils.removeUser(session);
            session.invalidate();
            logger.info("User logged out");
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

    // ============================================
    // PASSWORD RESET
    // ============================================

    private void showForgotPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/Customer/forgot-password.jsp").forward(request, response);
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            validateCsrfToken(request);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));

            if (!authDAO.emailExists(email)) {
                showForgotPasswordSuccess(request, response);
                return;
            }

            if (authDAO.isOAuthAccount(email)) {
                String provider = authDAO.getOAuthProvider(email);
                throw new IllegalArgumentException(
                        "Tài khoản này được đăng nhập bằng " + provider + ". " +
                                "Vui lòng sử dụng " + provider + " để đăng nhập."
                );
            }

            String token = UUID.randomUUID().toString();
            String ip = getClientIP(request);
            String userAgent = request.getHeader("User-Agent");

            Map<String, Object> result = authDAO.requestPasswordReset(email, token, ip, userAgent);

            if ((boolean) result.get("success")) {
                User user = authDAO.getUserByEmail(email);
                if (user != null) {
                    String resetUrl = buildUrl(request, "/reset-password?token=" + token);
                    emailService.sendPasswordResetEmail(request, email, user.getName(), resetUrl);
                    logger.info("Password reset email sent to: {}", email);
                }
            }

            showForgotPasswordSuccess(request, response);

        } catch (Exception e) {
            logger.warn("Forgot password error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/Customer/forgot-password.jsp", e.getMessage());
        }
    }

    private void showForgotPasswordSuccess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("success", "Nếu email tồn tại, link đặt lại mật khẩu đã được gửi");
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/Customer/forgot-password.jsp").forward(request, response);
    }

    private void showResetPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("/WEB-INF/views/Customer/reset-error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("token", token);
        setCSRFToken(request);
        request.getRequestDispatcher("/WEB-INF/views/Customer/reset-password.jsp").forward(request, response);
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
                request.getRequestDispatcher("/WEB-INF/views/Customer/reset-success.jsp").forward(request, response);
            } else {
                throw new RuntimeException((String) result.get("message"));
            }

        } catch (Exception e) {
            logger.warn("Reset password error: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.setAttribute("token", request.getParameter("token"));
            setCSRFToken(request);
            request.getRequestDispatcher("/WEB-INF/views/Customer/reset-password.jsp").forward(request, response);
        }
    }

    // ============================================
    // UTILITY METHODS
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
