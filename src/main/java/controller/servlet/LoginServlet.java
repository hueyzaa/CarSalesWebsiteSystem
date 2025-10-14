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

        HttpSession session = request.getSession(false);

        // Nếu đã đăng nhập -> chuyển hướng theo vai trò
        if (session != null && session.getAttribute("userRole") != null) {
            String role = (String) session.getAttribute("userRole");
            redirectByRole(role, request, response);
            return;
        }

        // Tạo CSRF token
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
            // Kiểm tra CSRF token
            validateCsrfToken(request);

            // Lấy dữ liệu form
            String email = ValidationUtil.validateEmail(request.getParameter("email"));
            String password = request.getParameter("password");

            if (password == null || password.trim().isEmpty()) {
                throw new ValidationException("password", "Mật khẩu không được để trống");
            }

            // Kiểm tra đăng nhập
            User user = userDAO.login(email, password);

            if (user != null) {
                // Reset giới hạn
                RateLimitFilter.resetAttempts(email);

                // Tạo session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole() != null ? user.getRole().toUpperCase() : "GUEST");
                session.setMaxInactiveInterval(30 * 60); // 30 phút

                request.changeSessionId(); // chống session fixation

                logger.info("User '{}' logged in successfully with role '{}'", email, user.getRole());
                redirectByRole(user.getRole(), request, response);

            } else {
                // Đăng nhập thất bại
                RateLimitFilter.recordFailedAttempt(email);
                throw new ValidationException("Thông tin đăng nhập không chính xác");
            }

        } catch (ValidationException e) {
            logger.warn("Validation error in login: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error in login", e);
            handleError(request, response, "Đã xảy ra lỗi không xác định.");
        }
    }

    /**
     * Điều hướng theo vai trò
     */
    private void redirectByRole(String role, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (role == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        switch (role.toUpperCase()) {
            case "ADMIN":
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
                break;
            case "STAFF":
                response.sendRedirect(request.getContextPath() + "/Staff/dashboard");
                break;
            case "CUSTOMER":
            case "GUEST":
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }

    /**
     *  Kiểm tra CSRF token
     */
    private void validateCsrfToken(HttpServletRequest request) throws ValidationException {
        HttpSession session = request.getSession(false);
        if (session == null) throw new ValidationException("Phiên làm việc đã hết hạn");

        String sessionToken = (String) session.getAttribute("csrfToken");
        String requestToken = request.getParameter("csrfToken");

        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            logger.warn("CSRF token validation failed");
            throw new ValidationException("Yêu cầu không hợp lệ (CSRF)");
        }
    }

    /**
     *  Hiển thị lại form khi lỗi
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {

        request.setAttribute("error", errorMessage);
        request.setAttribute("email", request.getParameter("email"));

        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
