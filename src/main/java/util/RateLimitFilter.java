package util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limiting filter to prevent brute force attacks
 */
@WebFilter("/login")
public class RateLimitFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME = 15 * 60 * 1000; // 15 minutes

    private static final Map<String, AttemptInfo> attemptMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String email = httpRequest.getParameter("email");

        if (email != null && httpRequest.getMethod().equals("POST")) {
            String key = email.toLowerCase().trim();
            AttemptInfo info = attemptMap.computeIfAbsent(key, k -> new AttemptInfo());

            if (info.isLocked()) {
                logger.warn("Login blocked for email: {} (too many attempts)", email);
                httpRequest.setAttribute("error",
                        "Tài khoản tạm thời bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.");
                httpRequest.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(httpRequest, httpResponse);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Call this method after failed login attempt
     */
    public static void recordFailedAttempt(String email) {
        if (email == null) return;

        String key = email.toLowerCase().trim();
        AttemptInfo info = attemptMap.computeIfAbsent(key, k -> new AttemptInfo());
        info.incrementAttempts();

        if (info.getAttempts() >= MAX_ATTEMPTS) {
            info.lock();
            logger.warn("Account locked for email: {} after {} failed attempts",
                    email, MAX_ATTEMPTS);
        }
    }

    /**
     * Call this method after successful login
     */
    public static void resetAttempts(String email) {
        if (email == null) return;
        attemptMap.remove(email.toLowerCase().trim());
    }

    /**
     * Class to track login attempts
     */
    private static class AttemptInfo {
        private final AtomicInteger attempts = new AtomicInteger(0);
        private long lockTime = 0;

        public int incrementAttempts() {
            return attempts.incrementAndGet();
        }

        public int getAttempts() {
            return attempts.get();
        }

        public void lock() {
            lockTime = System.currentTimeMillis();
        }

        public boolean isLocked() {
            if (lockTime == 0) return false;

            long elapsed = System.currentTimeMillis() - lockTime;
            if (elapsed > LOCK_TIME) {
                // Auto-unlock after time expires
                attempts.set(0);
                lockTime = 0;
                return false;
            }
            return true;
        }
    }
}
