package controller.customer;

import dao.AuthDAO;
import model.User;
import service.GoogleOAuthService;
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
import java.util.Map;
import java.util.UUID;

/**
 * Google OAuth 2.0 Servlet
 */
@WebServlet(urlPatterns = {"/oauth2/google", "/oauth2/callback/google"})
public class GoogleOAuthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuthServlet.class);

    private GoogleOAuthService oauthService;
    private AuthDAO authDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        oauthService = new GoogleOAuthService();
        authDAO = new AuthDAO();
        logger.info("GoogleOAuthServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/oauth2/google".equals(path)) {
            handleGoogleLogin(request, response);
        } else if ("/oauth2/callback/google".equals(path)) {
            handleGoogleCallback(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Step 1: Redirect to Google OAuth
     */
    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // Generate state token for CSRF protection
            String state = UUID.randomUUID().toString();
            HttpSession session = request.getSession();
            session.setAttribute("oauth_state", state);

            // Generate Google authorization URL
            String authUrl = oauthService.getAuthorizationUrl(state);

            if (authUrl != null) {
                logger.info("Redirecting to Google OAuth");
                response.sendRedirect(authUrl);
            } else {
                logger.error("Failed to generate Google OAuth URL");
                session.setAttribute("error", "Không thể kết nối với Google");
                response.sendRedirect(request.getContextPath() + "/login");
            }

        } catch (Exception e) {
            logger.error("Error in Google OAuth", e);
            request.getSession().setAttribute("error", "Lỗi kết nối Google");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * Step 2: Handle Google OAuth callback
     */
    private void handleGoogleCallback(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        try {
            // Get parameters from Google
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            String error = request.getParameter("error");

            // Check for errors
            if (error != null) {
                logger.warn("Google OAuth error: {}", error);
                session.setAttribute("error", "Đăng nhập Google thất bại");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Validate state (CSRF protection)
            String sessionState = (String) session.getAttribute("oauth_state");
            if (sessionState == null || !sessionState.equals(state)) {
                logger.warn("Invalid OAuth state");
                session.setAttribute("error", "Yêu cầu không hợp lệ (CSRF)");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Remove state from session
            session.removeAttribute("oauth_state");

            // Exchange code for access token
            String accessToken = oauthService.getAccessToken(code);
            if (accessToken == null) {
                logger.error("Failed to get access token");
                session.setAttribute("error", "Không thể xác thực với Google");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Get user info from Google
            Map<String, String> userInfo = oauthService.getUserInfo(accessToken);
            if (userInfo == null) {
                logger.error("Failed to get user info");
                session.setAttribute("error", "Không thể lấy thông tin người dùng");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Extract user data
            String email = userInfo.get("email");
            String name = userInfo.get("name");
            String googleId = userInfo.get("googleId");

            logger.info("Google user info: {} ({})", name, email);

            // SECURITY CHECK: Is this a password account?
            if (authDAO.isPasswordAccount(email)) {
                logger.warn("Password account attempting Google login: {}", email);
                session.setAttribute("error",
                        "Email này đã được đăng ký bằng mật khẩu. " +
                                "Vui lòng đăng nhập bằng email và mật khẩu.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // SECURITY CHECK: Different OAuth provider?
            String existingProvider = authDAO.getOAuthProvider(email);
            if (existingProvider != null && !"GOOGLE".equals(existingProvider)) {
                logger.warn("Email linked to different OAuth: {}", existingProvider);
                session.setAttribute("error",
                        "Email này đã được liên kết với " + existingProvider + ". " +
                                "Vui lòng đăng nhập bằng " + existingProvider + ".");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Login or register with Google OAuth
            User user = authDAO.loginOrRegisterWithGoogle(email, name, googleId);

            if (user == null) {
                logger.error("Failed to login/register Google user");
                session.setAttribute("error", "Không thể đăng nhập với Google. Vui lòng thử lại.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Check if account is active
            if (!user.isActive()) {
                logger.warn("Inactive Google account: {}", email);
                session.setAttribute("error", "Tài khoản đã bị vô hiệu hóa");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Login successful - Create session
            SessionUtils.setUser(session, user);
            SessionUtils.preventSessionFixation(request);

            logger.info("Google login successful: {} (Role: {})", email, user.getRole());

            // Redirect based on role
            String redirectUrl;
            if (user.isAdmin()) {
                redirectUrl = request.getContextPath() + "/Admin/dashboard";
            } else if (user.isStaff()) {
                redirectUrl = request.getContextPath() + "/staff/dashboard";
            } else {
                redirectUrl = request.getContextPath() + "/home";
            }

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            logger.error("Error in Google OAuth callback", e);
            session.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng nhập");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("GoogleOAuthServlet destroyed");
    }
}