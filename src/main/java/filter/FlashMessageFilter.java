package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * FlashMessageFilter - Automatically clears flash messages after displaying
 * This filter runs AFTER response is sent, cleaning up session messages
 */
@WebFilter("/*")
public class FlashMessageFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(FlashMessageFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        // Save current messages before processing request
        String error = null;
        String errorMessage = null;
        String success = null;
        String successMessage = null;

        if (session != null) {
            error = (String) session.getAttribute("error");
            errorMessage = (String) session.getAttribute("errorMessage");
            success = (String) session.getAttribute("success");
            successMessage = (String) session.getAttribute("successMessage");
        }

        // Process the request
        chain.doFilter(request, response);

        // After response is sent, clear the messages that were displayed
        if (session != null) {
            // Only clear if messages exist and were displayed
            if (error != null || errorMessage != null || success != null || successMessage != null) {

                // Clear error messages
                if (error != null) {
                    session.removeAttribute("error");
                    logger.debug("Cleared 'error' message: {}", error);
                }
                if (errorMessage != null) {
                    session.removeAttribute("errorMessage");
                    logger.debug("Cleared 'errorMessage' message: {}", errorMessage);
                }

                // Clear success messages
                if (success != null) {
                    session.removeAttribute("success");
                    logger.debug("Cleared 'success' message: {}", success);
                }
                if (successMessage != null) {
                    session.removeAttribute("successMessage");
                    logger.debug("Cleared 'successMessage' message: {}", successMessage);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("FlashMessageFilter initialized");
    }

    @Override
    public void destroy() {
        logger.info("FlashMessageFilter destroyed");
    }
}