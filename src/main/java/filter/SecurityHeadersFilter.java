package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHeadersFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prevent clickjacking attacks
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Prevent MIME-sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Enable XSS protection in older browsers
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Control referrer information
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // Restrict browser features
        httpResponse.setHeader("Permissions-Policy",
                "geolocation=(), microphone=(), camera=()");

        // Content Security Policy (CSP) - Removed 'unsafe-eval'
        httpResponse.setHeader("Content-Security-Policy",
                buildContentSecurityPolicy());

        chain.doFilter(request, response);
    }

    /**
     * Build Content Security Policy string
     * Removed 'unsafe-eval' for better security
     * Bootstrap 5.3+ doesn't need eval
     *
     * @return CSP header value
     */
    private String buildContentSecurityPolicy() {
        return String.join(" ",
                // Default source - self only
                "default-src 'self';",

                // Scripts - Allow Bootstrap, jQuery, and inline scripts
                // Removed 'unsafe-eval' for better security
                "script-src 'self' 'unsafe-inline'",
                "https://cdn.jsdelivr.net",
                "https://cdnjs.cloudflare.com",
                "https://code.jquery.com;",

                // Styles - Allow Bootstrap, Font Awesome, Google Fonts, and inline styles
                "style-src 'self' 'unsafe-inline'",
                "https://cdn.jsdelivr.net",
                "https://cdnjs.cloudflare.com",
                "https://fonts.googleapis.com;",

                // Images - Allow from self, data URIs, HTTPS, and blob
                "img-src 'self' data: https: blob:;",

                // Fonts - Allow from Google Fonts and CDNs
                "font-src 'self'",
                "https://fonts.gstatic.com",
                "https://cdn.jsdelivr.net",
                "https://cdnjs.cloudflare.com;",

                // AJAX/Fetch connections - Allow CDN connections
                "connect-src 'self'",
                "https://cdn.jsdelivr.net",
                "https://cdnjs.cloudflare.com",
                "https://fonts.googleapis.com",
                "https://fonts.gstatic.com;",

                // Prevent framing from any origin
                "frame-ancestors 'none';",

                // Base tag restrictions
                "base-uri 'self';",

                // Form submissions - Allow self and VNPay
                "form-action 'self' https://sandbox.vnpayment.vn;"
        );
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("SecurityHeadersFilter initialized - Strict CSP without unsafe-eval");
    }

    @Override
    public void destroy() {
        logger.info("SecurityHeadersFilter destroyed");
    }
}