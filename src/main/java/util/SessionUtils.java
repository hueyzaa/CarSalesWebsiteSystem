package util;

import model.Customer;
import model.Staff;
import model.Admin;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionUtils - Utility class for session management and user authentication
 * Provides methods to extract user information from session objects
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
     * Get user ID from session object
     * @param userObject User object from session (Customer/Staff/Admin)
     * @return User ID or null if invalid/not logged in
     */
    public static Integer getUserId(Object userObject) {
        if (userObject == null) {
            return null;
        }

        if (userObject instanceof Customer) {
            return ((Customer) userObject).getCustomerId();
        } else if (userObject instanceof Staff) {
            return ((Staff) userObject).getStaffId();
        } else if (userObject instanceof Admin) {
            return ((Admin) userObject).getAdminId();
        }

        logger.warn("Unknown user object type: {}", userObject.getClass().getName());
        return null;
    }

    /**
     * Get user ID from session
     * @param session HTTP session
     * @return User ID or null if not logged in
     */
    public static Integer getUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userObject = session.getAttribute("user");
        return getUserId(userObject);
    }


    /**
     * Get user role from session object
     * @param userObject User object from session
     * @return "CUSTOMER", "STAFF", "ADMIN", or null if invalid
     */
    public static String getUserRole(Object userObject) {
        if (userObject == null) {
            return null;
        }

        if (userObject instanceof Customer) {
            return "CUSTOMER";
        } else if (userObject instanceof Staff) {
            return "STAFF";
        } else if (userObject instanceof Admin) {
            return "ADMIN";
        }

        logger.warn("Unknown user object type: {}", userObject.getClass().getName());
        return null;
    }

    /**
     * Get user role from session
     * @param session HTTP session
     * @return "CUSTOMER", "STAFF", "ADMIN", or null
     */
    public static String getUserRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userObject = session.getAttribute("user");
        return getUserRole(userObject);
    }


    /**
     * Get user email from session object
     * @param userObject User object from session
     * @return User email or null if invalid
     */
    public static String getUserEmail(Object userObject) {
        if (userObject == null) {
            return null;
        }

        if (userObject instanceof Customer) {
            return ((Customer) userObject).getEmail();
        } else if (userObject instanceof Staff) {
            return ((Staff) userObject).getEmail();
        } else if (userObject instanceof Admin) {
            return ((Admin) userObject).getEmail();
        }

        return null;
    }

    /**
     * Get user email from session
     * @param session HTTP session
     * @return User email or null
     */
    public static String getUserEmail(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userObject = session.getAttribute("user");
        return getUserEmail(userObject);
    }

    /**
     * Get user name from session object
     * @param userObject User object from session
     * @return User name or null if invalid
     */
    public static String getUserName(Object userObject) {
        if (userObject == null) {
            return null;
        }

        if (userObject instanceof Customer) {
            return ((Customer) userObject).getName();
        } else if (userObject instanceof Staff) {
            return ((Staff) userObject).getName();
        } else if (userObject instanceof Admin) {
            return ((Admin) userObject).getName();
        }

        return null;
    }

    /**
     * Get user name from session
     * @param session HTTP session
     * @return User name or null
     */
    public static String getUserName(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userObject = session.getAttribute("user");
        return getUserName(userObject);
    }


    /**
     * Get Customer object from session
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


    /**
     * Check if user is logged in
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
     * @param session HTTP session
     * @return true if user is a customer
     */
    public static boolean isCustomer(HttpSession session) {
        return "CUSTOMER".equals(getUserRole(session));
    }

    /**
     * Check if user is a staff member
     * @param session HTTP session
     * @return true if user is staff
     */
    public static boolean isStaff(HttpSession session) {
        return "STAFF".equals(getUserRole(session));
    }

    /**
     * Check if user is an admin
     * @param session HTTP session
     * @return true if user is admin
     */
    public static boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(getUserRole(session));
    }

    /**
     * Check if user is staff or admin
     * @param session HTTP session
     * @return true if user is staff or admin
     */
    public static boolean isStaffOrAdmin(HttpSession session) {
        String role = getUserRole(session);
        return "STAFF".equals(role) || "ADMIN".equals(role);
    }

    /**
     * Check if user has specific role
     * @param session HTTP session
     * @param role Role to check ("CUSTOMER", "STAFF", "ADMIN")
     * @return true if user has the specified role
     */
    public static boolean hasRole(HttpSession session, String role) {
        if (role == null) {
            return false;
        }
        return role.equalsIgnoreCase(getUserRole(session));
    }


    /**
     * Set user in session with additional attributes
     * @param session HTTP session
     * @param user User object (Customer/Staff/Admin)
     */
    public static void setUser(HttpSession session, Object user) {
        if (session == null || user == null) {
            logger.warn("Cannot set user: session or user is null");
            return;
        }

        // Store user object
        session.setAttribute("user", user);

        // Store additional attributes for easy access
        session.setAttribute("userId", getUserId(user));
        session.setAttribute("userName", getUserName(user));
        session.setAttribute("userEmail", getUserEmail(user));
        session.setAttribute("userRole", getUserRole(user));

        // Set session timeout
        session.setMaxInactiveInterval(SESSION_TIMEOUT);

        logger.info("User logged in: {} (Role: {}, ID: {})",
                getUserEmail(user), getUserRole(user), getUserId(user));
    }

    /**
     * Update user object in session
     * Useful after profile updates
     * @param session HTTP session
     * @param user Updated user object
     */
    public static void updateUser(HttpSession session, Object user) {
        if (session == null || user == null) {
            logger.warn("Cannot update user: session or user is null");
            return;
        }

        setUser(session, user);
        logger.info("User session updated: {} (ID: {})",
                getUserEmail(user), getUserId(user));
    }

    /**
     * Remove user from session (logout)
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
     * Check if customer account is active
     * @param session HTTP session
     * @return true if customer is active
     */
    public static boolean isCustomerActive(HttpSession session) {
        Customer customer = getCustomer(session);
        return (customer != null) && customer.isActive();
    }

    /**
     * Check if staff account is active
     * @param session HTTP session
     * @return true if staff is active
     */
    public static boolean isStaffActive(HttpSession session) {
        Staff staff = getStaff(session);
        return (staff != null) && staff.isActive();
    }

    /**
     * Check if admin account is active
     * @param session HTTP session
     * @return true if admin is active
     */
    public static boolean isAdminActive(HttpSession session) {
        Admin admin = getAdmin(session);
        return (admin != null) && admin.isActive();
    }

    /**
     * Get customer total spent from session
     * @param session HTTP session
     * @return Total spent or 0.0 if not a customer
     */
    public static double getCustomerTotalSpent(HttpSession session) {
        Customer customer = getCustomer(session);
        return (customer != null) ? customer.getTotalSpent() : 0.0;
    }

    /**
     * Get customer total orders from session
     * @param session HTTP session
     * @return Total orders or 0 if not a customer
     */
    public static int getCustomerTotalOrders(HttpSession session) {
        Customer customer = getCustomer(session);
        return (customer != null) ? customer.getTotalOrders() : 0;
    }
}