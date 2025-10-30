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
 * CustomerFilter - Restrict access to customer-only pages
 * Only users with CUSTOMER role can access customer URLs
 */
@WebFilter({
        "/my-promotions",
        "/cart/*",
        "/checkout/*",
        "/my-orders",
        "/profile"
})
public class CustomerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CustomerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // ✅ Check if user is logged in
        if (!SessionUtils.isLoggedIn(session)) {
            String requestURI = httpRequest.getRequestURI();
            logger.info("Guest user attempting to access customer page: {}", requestURI);

            session = httpRequest.getSession(true);
            session.setAttribute("redirectAfterLogin", requestURI);
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để tiếp tục");

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // ✅ Check if user is a customer
        if (!SessionUtils.isCustomer(session)) {
            String requestURI = httpRequest.getRequestURI();
            String userRole = SessionUtils.getUserRole(session);
            String userEmail = SessionUtils.getUserEmail(session);

            logger.warn("Non-customer user (role: {}) attempted to access customer page: {} - User: {}",
                    userRole, requestURI, userEmail);

            session.setAttribute("error", "Chức năng này chỉ dành cho khách hàng!");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
            return;
        }

        // ✅ Customer authenticated, allow access
        logger.debug("Customer access granted to: {} for user: {}",
                httpRequest.getRequestURI(), SessionUtils.getUserEmail(session));

        chain.doFilter(request, response);
    }
}