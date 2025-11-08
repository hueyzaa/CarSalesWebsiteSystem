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
import java.util.HashMap;
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

    // ============ BUSINESS LOGIC (moved from Model) ============

    /**
     * Check if user is an admin
     */
    private boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    /**
     * Check if user is a staff member
     */
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

            // Check email exists in database
            if (authDAO.emailExists(email)) {
                throw new IllegalArgumentException("Email đã được sử dụng");
            }

            // Generate verification token
            String token = UUID.randomUUID().toString();
            String ip = getClientIP(request);
            String userAgent = request.getHeader("User-Agent");

            // Hash password before storing in session
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // Store registration data in session (NOT in DB yet)
            HttpSession session = request.getSession();
            Map<String, Object> pendingRegistration = new HashMap<>();
            pendingRegistration.put("email", email);
            pendingRegistration.put("hashedPassword", hashedPassword);
            pendingRegistration.put("name", name);
            pendingRegistration.put("phone", phone);
            pendingRegistration.put("address", address);
            pendingRegistration.put("token", token);
            pendingRegistration.put("ipAddress", ip);
            pendingRegistration.put("userAgent", userAgent);
            pendingRegistration.put("createdAt", System.currentTimeMillis());
            pendingRegistration.put("expiryAt", System.currentTimeMillis() + (24 * 60 * 60 * 1000)); // 24 hours

            session.setAttribute("pendingRegistration", pendingRegistration);
            session.setAttribute("registeredEmail", email);

            // Send verification email
            try {
                String verifyUrl = buildUrl(request, "/verify?token=" + token);
                emailService.sendVerificationEmail(request, email, name, verifyUrl);
                logger.info("Verification email sent to: {}", email);
            } catch (Exception e) {
                logger.error("Failed to send verification email", e);
                throw new RuntimeException("Không thể gửi email xác thực. Vui lòng thử lại.");
            }

            // Redirect to pending page
            request.getRequestDispatcher("/WEB-INF/views/verification-pending.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.warn("Register error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/register.jsp", e.getMessage());
        }
    }


    private void handleVerifyEmail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        HttpSession session = request.getSession(false);

        if (token == null || token.isEmpty()) {
            request.setAttribute("error", "Token không hợp lệ");
            request.getRequestDispatcher("/WEB-INF/views/verify-error.jsp").forward(request, response);
            return;
        }

        try {
            // Get pending registration from session
            if (session == null) {
                throw new IllegalStateException("Phiên làm việc đã hết hạn. Vui lòng đăng ký lại.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> pendingRegistration =
                    (Map<String, Object>) session.getAttribute("pendingRegistration");

            if (pendingRegistration == null) {
                throw new IllegalStateException("Không tìm thấy thông tin đăng ký. Vui lòng đăng ký lại.");
            }

            // Validate token
            String storedToken = (String) pendingRegistration.get("token");
            if (!token.equals(storedToken)) {
                throw new IllegalArgumentException("Token không hợp lệ");
            }

            // Check expiry (24 hours)
            long expiryAt = (long) pendingRegistration.get("expiryAt");
            if (System.currentTimeMillis() > expiryAt) {
                // Clear expired registration
                session.removeAttribute("pendingRegistration");
                session.removeAttribute("registeredEmail");
                throw new IllegalArgumentException("Token đã hết hạn. Vui lòng đăng ký lại.");
            }

            // Extract registration data
            String email = (String) pendingRegistration.get("email");
            String hashedPassword = (String) pendingRegistration.get("hashedPassword");
            String name = (String) pendingRegistration.get("name");
            String phone = (String) pendingRegistration.get("phone");
            String address = (String) pendingRegistration.get("address");
            String ipAddress = (String) pendingRegistration.get("ipAddress");

            // Check email still available (race condition check)
            if (authDAO.emailExists(email)) {
                session.removeAttribute("pendingRegistration");
                session.removeAttribute("registeredEmail");
                throw new IllegalStateException("Email đã được sử dụng bởi người khác");
            }

            boolean success = authDAO.registerVerifiedCustomer(
                    email, hashedPassword, name, phone, address, ipAddress
            );

            if (!success) {
                throw new RuntimeException("Không thể hoàn tất đăng ký. Vui lòng thử lại.");
            }

            // Clean up session
            session.removeAttribute("pendingRegistration");
            session.removeAttribute("registeredEmail");

            logger.info("Email verified and customer registered: {}", email);

            // Show success page
            request.setAttribute("success", "Email đã được xác thực thành công!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/verify-success.jsp")
                    .forward(request, response);

        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.warn("Verification error: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/verify-error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Verification error", e);
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            request.getRequestDispatcher("/WEB-INF/views/verify-error.jsp")
                    .forward(request, response);
        }
    }


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

            User user = authDAO.login(email, password);

            if (user == null) {
                throw new IllegalArgumentException("Email hoặc mật khẩu không đúng");
            }


            // Create session
            HttpSession session = request.getSession();
            SessionUtils.setUser(session, user);
            SessionUtils.preventSessionFixation(request);

            logger.info("User logged in: {} (Role: {})", email, user.getRole());

            // Redirect based on role - Using local helper methods instead of user.isAdmin()
            String redirectUrl;
            if (isAdmin(user)) {
                redirectUrl = request.getContextPath() + "/admin/dashboard";
            } else if (isStaff(user)) {
                redirectUrl = request.getContextPath() + "/staff/dashboard";
            } else {
                String redirect = request.getParameter("redirect");
                if (redirect != null && !redirect.isEmpty()) {
                    redirectUrl = request.getContextPath() + redirect;
                } else {
                    redirectUrl = request.getContextPath() + "/home";
                }
            }

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            logger.warn("Login error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/login.jsp", e.getMessage());
        }
    }


    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionUtils.removeUser(session);
            session.invalidate();
            logger.info("User logged out");
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }


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
                User user = authDAO.getUserByEmail(email);
                if (user != null) {
                    String resetUrl = buildUrl(request, "/reset-password?token=" + token);
                    emailService.sendPasswordResetEmail(request, email, user.getName(), resetUrl);
                    logger.info("Password reset email sent to: {}", email);
                }
            }

            request.setAttribute("success", "Nếu email tồn tại, link đặt lại mật khẩu đã được gửi");
            setCSRFToken(request);
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(request, response);

        } catch (Exception e) {
            logger.warn("Forgot password error: {}", e.getMessage());
            handleError(request, response, "/WEB-INF/views/forgot-password.jsp", e.getMessage());
        }
    }


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