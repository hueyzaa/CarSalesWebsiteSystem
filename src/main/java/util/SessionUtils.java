package util;

import model.Customer;
import model.Staff;
import model.Admin;
import model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionUtils - Utility class for session management and user authentication
 */
public class SessionUtils {
    private static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

    // Session configuration
    private static final int SESSION_TIMEOUT = 30 * 60; // 30 minutes in seconds

    // Private constructor to prevent instantiation
    private SessionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }


    /**
     * Set User in session (PRIMARY METHOD for authentication)
     * Use this for login with User object
     *
     * @param session HTTP session
     * @param user User object (supports ADMIN, STAFF, CUSTOMER)
     */
    public static void setUser(HttpSession session, User user) {
        if (session == null || user == null) {
            logger.warn("Cannot set user: session or user is null");
            return;
        }

        // Store user object
        session.setAttribute("user", user);

        // Store additional attributes for easy access in JSP
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole());

        // Set session timeout
        session.setMaxInactiveInterval(SESSION_TIMEOUT);

        logger.info("User logged in: {} (Role: {}, ID: {})",
                user.getEmail(), user.getRole(), user.getUserId());
    }

    /**
     * Get User from session (PRIMARY METHOD)
     * Returns User object for all authenticated users
     *
     * @param session HTTP session
     * @return User object or null if not logged in
     */
    public static User getUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("user");

        if (userObject instanceof User) {
            return (User) userObject;
        }

        if (userObject instanceof Customer) {
            Customer customer = (Customer) userObject;
            User user = new User();
            user.setUserId(customer.getCustomerId());
            user.setEmail(customer.getEmail());
            user.setName(customer.getName());
            user.setPhone(customer.getPhone());
            user.setAddress(customer.getAddress());
            user.setRole("CUSTOMER");
            user.setActive(customer.isActive());
            user.setEmailVerified(customer.isEmailVerified());
            user.setCreatedAt(customer.getCreatedAt());
            user.setLastLogin(customer.getLastLogin());
            user.setOauthProvider(customer.getOauthProvider());
            return user;
        }

        if (userObject instanceof Staff) {
            Staff staff = (Staff) userObject;
            User user = new User();
            user.setUserId(staff.getStaffId());
            user.setEmail(staff.getEmail());
            user.setName(staff.getName());
            user.setPhone(staff.getPhone());
            user.setAddress(staff.getAddress());
            user.setRole("STAFF");
            user.setActive(staff.isActive());
            user.setEmailVerified(staff.isEmailVerified());
            user.setCreatedAt(staff.getCreatedAt());
            user.setLastLogin(staff.getLastLogin());
            return user;
        }

        if (userObject instanceof Admin) {
            Admin admin = (Admin) userObject;
            User user = new User();
            user.setUserId(admin.getAdminId());
            user.setEmail(admin.getEmail());
            user.setName(admin.getName());
            user.setPhone(admin.getPhone());
            user.setRole("ADMIN");
            user.setActive(admin.isActive());
            user.setEmailVerified(admin.isEmailVerified());
            user.setCreatedAt(admin.getCreatedAt());
            user.setLastLogin(admin.getLastLogin());
            return user;
        }

        return null;
    }

    /**
     * Update user in session
     * Useful after profile updates
     *
     * @param session HTTP session
     * @param user Updated user object
     */
    public static void updateUser(HttpSession session, User user) {
        if (session == null || user == null) {
            logger.warn("Cannot update user: session or user is null");
            return;
        }

        setUser(session, user);
        logger.info("User session updated: {} (ID: {})",
                user.getEmail(), user.getUserId());
    }

    // ============================================
    // GET USER INFORMATION
    // ============================================

    /**
     * Get user ID from session
     * Works with both new User object and legacy objects
     *
     * @param session HTTP session
     * @return User ID or null if not logged in
     */
    public static Integer getUserId(HttpSession session) {
        if (session == null) {
            return null;
        }

        // Try direct attribute first (fast path)
        Object userIdAttr = session.getAttribute("userId");
        if (userIdAttr instanceof Integer) {
            return (Integer) userIdAttr;
        }

        // Fallback: extract from user object
        User user = getUser(session);
        return user != null ? user.getUserId() : null;
    }

    /**
     * Get user email from session
     *
     * @param session HTTP session
     * @return User email or null
     */
    public static String getUserEmail(HttpSession session) {
        if (session == null) {
            return null;
        }

        // Try direct attribute first
        Object emailAttr = session.getAttribute("userEmail");
        if (emailAttr instanceof String) {
            return (String) emailAttr;
        }

        // Fallback
        User user = getUser(session);
        return user != null ? user.getEmail() : null;
    }

    /**
     * Get user name from session
     *
     * @param session HTTP session
     * @return User name or null
     */
    public static String getUserName(HttpSession session) {
        if (session == null) {
            return null;
        }

        // Try direct attribute first
        Object nameAttr = session.getAttribute("userName");
        if (nameAttr instanceof String) {
            return (String) nameAttr;
        }

        // Fallback
        User user = getUser(session);
        return user != null ? user.getName() : null;
    }

    /**
     * Get user role from session
     *
     * @param session HTTP session
     * @return "CUSTOMER", "STAFF", "ADMIN", or null
     */
    public static String getUserRole(HttpSession session) {
        if (session == null) {
            return null;
        }

        // Try direct attribute first
        Object roleAttr = session.getAttribute("userRole");
        if (roleAttr instanceof String) {
            return (String) roleAttr;
        }

        // Fallback
        User user = getUser(session);
        return user != null ? user.getRole() : null;
    }

    // ============================================
    // AUTHENTICATION CHECKS
    // ============================================

    /**
     * Check if user is logged in
     *
     * @param session HTTP session
     * @return true if user is logged in
     */
    public static boolean isLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        return session.getAttribute("user") != null;
    }

    /**
     * Check if user is a customer
     *
     * @param session HTTP session
     * @return true if user is a customer
     */
    public static boolean isCustomer(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isCustomer();
    }

    /**
     * Check if user is a staff member
     *
     * @param session HTTP session
     * @return true if user is staff
     */
    public static boolean isStaff(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isStaff();
    }

    /**
     * Check if user is an admin
     *
     * @param session HTTP session
     * @return true if user is admin
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isAdmin();
    }

    /**
     * Check if user is staff or admin
     * Useful for authorization checks
     *
     * @param session HTTP session
     * @return true if user is staff or admin
     */
    public static boolean isStaffOrAdmin(HttpSession session) {
        User user = getUser(session);
        return user != null && (user.isStaff() || user.isAdmin());
    }

    /**
     * Check if user has specific role
     *
     * @param session HTTP session
     * @param role Role to check ("CUSTOMER", "STAFF", "ADMIN")
     * @return true if user has the specified role
     */
    public static boolean hasRole(HttpSession session, String role) {
        if (role == null) {
            return false;
        }
        String userRole = getUserRole(session);
        return role.equalsIgnoreCase(userRole);
    }


    /**
     * Get Customer object from session
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return Customer object or null if not a customer
     */
    public static Customer getCustomer(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("user");
        if (userObject instanceof Customer) {
            return (Customer) userObject;
        }

        return null;
    }

    /**
     * Get Staff object from session
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return Staff object or null if not a staff
     */
    public static Staff getStaff(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("user");
        if (userObject instanceof Staff) {
            return (Staff) userObject;
        }

        return null;
    }

    /**
     * Get Admin object from session
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return Admin object or null if not an admin
     */
    public static Admin getAdmin(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("user");
        if (userObject instanceof Admin) {
            return (Admin) userObject;
        }

        return null;
    }

    // ============================================
    // SESSION MANAGEMENT
    // ============================================

    /**
     * Remove user from session (logout)
     * Clears all user-related attributes
     *
     * @param session HTTP session
     */
    public static void removeUser(HttpSession session) {
        if (session == null) {
            return;
        }

        String email = getUserEmail(session);
        String role = getUserRole(session);
        Integer userId = getUserId(session);

        session.removeAttribute("user");
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.removeAttribute("userRole");

        logger.info("User logged out: {} (Role: {}, ID: {})", email, role, userId);
    }

    /**
     * Invalidate session (complete logout)
     * More thorough than removeUser - destroys entire session
     *
     * @param session HTTP session
     */
    public static void invalidateSession(HttpSession session) {
        if (session == null) {
            return;
        }

        String email = getUserEmail(session);
        String role = getUserRole(session);

        try {
            session.invalidate();
            logger.info("Session invalidated for user: {} (Role: {})", email, role);
        } catch (IllegalStateException e) {
            logger.warn("Session already invalidated");
        }
    }

    /**
     * Prevent session fixation attack
     * Call this after successful login
     * Changes session ID to prevent hijacking
     *
     * @param request HTTP servlet request
     */
    public static void preventSessionFixation(HttpServletRequest request) {
        if (request == null) {
            return;
        }

        String oldSessionId = request.getSession().getId();
        request.changeSessionId();
        String newSessionId = request.getSession().getId();

        logger.debug("Session ID changed: {} -> {}", oldSessionId, newSessionId);
    }

    /**
     * Check if session is valid and not expired
     *
     * @param session HTTP session
     * @return true if session is valid
     */
    public static boolean isSessionValid(HttpSession session) {
        if (session == null) {
            return false;
        }

        try {
            // Try to access session attribute - will throw exception if session is invalid
            session.getAttribute("user");
            return true;
        } catch (IllegalStateException e) {
            logger.warn("Session is invalid or expired");
            return false;
        }
    }

    /**
     * Extend session timeout
     * Useful for "remember me" functionality
     *
     * @param session HTTP session
     * @param seconds Timeout in seconds
     */
    public static void extendSessionTimeout(HttpSession session, int seconds) {
        if (session == null || seconds <= 0) {
            return;
        }

        session.setMaxInactiveInterval(seconds);
        logger.debug("Session timeout extended to {} seconds", seconds);
    }

    /**
     * Get remaining session time in seconds
     *
     * @param session HTTP session
     * @return Remaining time in seconds, or -1 if cannot determine
     */
    public static int getRemainingSessionTime(HttpSession session) {
        if (session == null) {
            return -1;
        }

        try {
            long lastAccessedTime = session.getLastAccessedTime();
            int maxInactiveInterval = session.getMaxInactiveInterval();
            long currentTime = System.currentTimeMillis();

            long elapsedTime = (currentTime - lastAccessedTime) / 1000; // Convert to seconds
            int remainingTime = maxInactiveInterval - (int) elapsedTime;

            return Math.max(0, remainingTime);
        } catch (IllegalStateException e) {
            logger.warn("Cannot get session time - session invalid");
            return -1;
        }
    }

    // ============================================
    // USER STATUS CHECKS
    // ============================================

    /**
     * Check if user account is active
     *
     * @param session HTTP session
     * @return true if user is active
     */
    public static boolean isUserActive(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isActive();
    }

    /**
     * Check if customer account is active
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return true if customer is active
     */
    public static boolean isCustomerActive(HttpSession session) {
        Customer customer = getCustomer(session);
        return customer != null && customer.isActive();
    }

    /**
     * Check if staff account is active
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return true if staff is active
     */
    public static boolean isStaffActive(HttpSession session) {
        Staff staff = getStaff(session);
        return staff != null && staff.isActive();
    }

    /**
     * Check if admin account is active
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return true if admin is active
     */
    public static boolean isAdminActive(HttpSession session) {
        Admin admin = getAdmin(session);
        return admin != null && admin.isActive();
    }

    /**
     * Check if user's email is verified
     * Important for security
     *
     * @param session HTTP session
     * @return true if email is verified
     */
    public static boolean isEmailVerified(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isEmailVerified();
    }

    /**
     * Check if user is OAuth user
     *
     * @param session HTTP session
     * @return true if user logged in via OAuth (Google, Facebook, etc.)
     */
    public static boolean isOAuthUser(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isOAuthUser();
    }

    // ============================================
    // CUSTOMER-SPECIFIC LEGACY METHODS
    // ============================================

    /**
     * Get customer total spent from session
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return Total spent or 0.0 if not a customer
     */
    public static double getCustomerTotalSpent(HttpSession session) {
        Customer customer = getCustomer(session);
        return customer != null ? customer.getTotalSpent() : 0.0;
    }

    /**
     * Get customer total orders from session
     * Legacy method for backward compatibility
     *
     * @param session HTTP session
     * @return Total orders or 0 if not a customer
     */
    public static int getCustomerTotalOrders(HttpSession session) {
        Customer customer = getCustomer(session);
        return customer != null ? customer.getTotalOrders() : 0;
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Get user display name (name or email)
     *
     * @param session HTTP session
     * @return Display name or "Guest" if not logged in
     */
    public static String getUserDisplayName(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "Guest";
        }

        String name = user.getName();
        return (name != null && !name.trim().isEmpty()) ? name : user.getEmail();
    }

    /**
     * Get user initials for avatar
     *
     * @param session HTTP session
     * @return User initials (1-2 characters)
     */
    public static String getUserInitials(HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "?";
        }
        return user.getInitials();
    }

    /**
     * Debug: Log session information
     * Useful for troubleshooting
     *
     * @param session HTTP session
     */
    public static void logSessionInfo(HttpSession session) {
        if (session == null) {
            logger.debug("Session: NULL");
            return;
        }

        try {
            logger.debug("Session ID: {}", session.getId());
            logger.debug("User ID: {}", getUserId(session));
            logger.debug("User Email: {}", getUserEmail(session));
            logger.debug("User Role: {}", getUserRole(session));
            logger.debug("Is Logged In: {}", isLoggedIn(session));
            logger.debug("Is Active: {}", isUserActive(session));
            logger.debug("Remaining Time: {}s", getRemainingSessionTime(session));
        } catch (Exception e) {
            logger.error("Error logging session info", e);
        }
    }
}