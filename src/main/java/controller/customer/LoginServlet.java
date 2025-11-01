package controller.customer;

import dao.UserDAO;
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
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        logger.info("LoginServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtils.isLoggedIn(request.getSession(false))) {
            logger.debug("User already logged in, redirecting to home");
            redirect(response, request.getContextPath() + "/home");
            return;
        }

        setCSRFToken(request);
        forward(request, response, "/WEB-INF/views/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            validateCsrfToken(request);

            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String password = request.getParameter("password");

            if (isEmpty(password)) {
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

    // ============ LOGIN LOGIC ============

    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response,
                                       Object userObject, String email) throws IOException {
        HttpSession session = request.getSession();

        SessionUtils.setUser(session, userObject);
        SessionUtils.preventSessionFixation(request);

        String role = SessionUtils.getUserRole(userObject);
        Integer userId = SessionUtils.getUserId(userObject);

        logger.info("User logged in: {} (role: {}, ID: {})", email, role, userId);

        String redirectUrl = getSavedRedirectUrl(session);
        if (isNotEmpty(redirectUrl)) {
            clearRedirectAttributes(session);
            logger.info("Redirecting to saved URL: {}", redirectUrl);
            redirect(response, redirectUrl);
        } else {
            redirect(response, request.getContextPath() + getDefaultRedirectByRole(role));
        }
    }

    private String getDefaultRedirectByRole(String role) {
        return switch (role) {
            case "ADMIN" -> "/admin/dashboard";
            case "STAFF" -> "/staff/dashboard";
            default -> "/home";
        };
    }

    // ============ CSRF PROTECTION ============

    private void setCSRFToken(HttpServletRequest request) {
        String csrfToken = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);
    }

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

    // ============ HELPER METHODS ============

    private String getSavedRedirectUrl(HttpSession session) {
        return (String) session.getAttribute("redirectAfterLogin");
    }

    private void clearRedirectAttributes(HttpSession session) {
        session.removeAttribute("redirectAfterLogin");
        session.removeAttribute("loginMessage");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("email", request.getParameter("email"));
        setCSRFToken(request);
        forward(request, response, "/WEB-INF/views/login.jsp");
    }

    // ============ UTILITY METHODS ============

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}