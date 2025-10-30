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

        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        httpResponse.setHeader("Permissions-Policy",
                "geolocation=(), microphone=(), camera=()");

        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' " +
                        "https://cdn.jsdelivr.net " +
                        "https://cdnjs.cloudflare.com " +
                        "https://code.jquery.com; " +
                        "style-src 'self' 'unsafe-inline' " +
                        "https://cdn.jsdelivr.net " +
                        "https://cdnjs.cloudflare.com " +
                        "https://fonts.googleapis.com; " +
                        "img-src 'self' data: https: blob:; " +
                        "font-src 'self' " +
                        "https://fonts.gstatic.com " +
                        "https://cdn.jsdelivr.net " +
                        "https://cdnjs.cloudflare.com; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none'; " +
                        "base-uri 'self'; " +
                        "form-action 'self' https://sandbox.vnpayment.vn;");

        chain.doFilter(request, response);
    }
}