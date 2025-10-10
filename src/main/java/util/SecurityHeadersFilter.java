package util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Security headers filter
 */
@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHeadersFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prevent clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Prevent MIME sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Enable XSS protection
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Strict Transport Security (HTTPS only)
        // httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Content Security Policy
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; " +
                        "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; " +
                        "img-src 'self' data: https:; " +
                        "font-src 'self' https://fonts.gstatic.com;");

        chain.doFilter(request, response);
    }
}