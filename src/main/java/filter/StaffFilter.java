package filter;

import util.SessionUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * StaffFilter - Restrict access to staff-only pages
 * Only users with STAFF or ADMIN role can access staff URLs
 */
@WebFilter({
        "/staff/*",
        "/orders/manage/*"
})
public class StaffFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(StaffFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (!SessionUtils.isStaffOrAdmin(session)) {
            String requestURI = httpRequest.getRequestURI();
            String userRole = SessionUtils.getUserRole(session);
            String userEmail = SessionUtils.getUserEmail(session);

            logger.warn("Unauthorized access attempt to {} by user: {} (role: {})",
                    requestURI, userEmail, userRole);

            // Save attempted URL for redirect after login
            if (session == null || !SessionUtils.isLoggedIn(session)) {
                session = httpRequest.getSession(true);
                session.setAttribute("redirectAfterLogin", requestURI);
                session.setAttribute("loginMessage", "Vui lòng đăng nhập với tài khoản Staff hoặc Admin");
            } else {
                session.setAttribute("error", "Bạn không có quyền truy cập trang này!");
            }

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        logger.debug("Staff/Admin access granted to: {} for user: {}",
                httpRequest.getRequestURI(), SessionUtils.getUserEmail(session));

        chain.doFilter(request, response);
    }
}