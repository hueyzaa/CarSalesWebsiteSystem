package util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Logging filter to track all HTTP requests
 */
@WebFilter("/*")
public class LoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String method = httpRequest.getMethod();
            String uri = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String fullUrl = queryString != null ? uri + "?" + queryString : uri;

            logger.info("{} {} - {}ms - IP: {}",
                    method, fullUrl, duration,
                    httpRequest.getRemoteAddr());
        }
    }
}