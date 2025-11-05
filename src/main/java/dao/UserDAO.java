package dao;

import model.Admin;
import model.Customer;
import model.Staff;
import model.User;
import util.DBContext;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Handles authentication and user management for separated tables
 * UPDATED: Added email_verified, oauth_id support, and email verification flow
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Authenticate user (login) - Returns appropriate user type
     */
    public Object login(String email, String password) {
        String sql = "SELECT user_id, email, password_hash, role, is_active, email_verified " +
                "FROM AppUsers WHERE email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Check if account is active
                    if (!rs.getBoolean("is_active")) {
                        logger.warn("Login attempt for inactive account: {}", email);
                        return null;
                    }

                    String storedHash = rs.getString("password_hash");

                    // Verify password using BCrypt
                    if (BCrypt.checkpw(password, storedHash)) {
                        int userId = rs.getInt("user_id");
                        String role = rs.getString("role");
                        boolean emailVerified = rs.getBoolean("email_verified");

                        // Update last login
                        updateLastLogin(userId);

                        // Return appropriate user object based on role
                        Object user = getUserDetailsByRole(userId, role);

                        if (user != null) {
                            logger.info("User logged in successfully: {} (role: {}, email_verified: {})",
                                    email, role, emailVerified);
                        }

                        return user;
                    } else {
                        logger.warn("Failed login attempt for email: {} (wrong password)", email);
                    }
                } else {
                    logger.warn("Failed login attempt for email: {} (email not found)", email);
                }
            }

        } catch (SQLException e) {
            logger.error("Error during login for email: {}", email, e);
            throw new RuntimeException("Failed to login", e);
        }

        return null;
    }

    /**
     * Get user details by role
     */
    private Object getUserDetailsByRole(int userId, String role) throws SQLException {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return getAdminDetails(userId);
            case "STAFF":
                return getStaffDetails(userId);
            case "CUSTOMER":
                return getCustomerDetails(userId);
            default:
                logger.error("Unknown role: {}", role);
                return null;
        }
    }

    /**
     * Get Admin details
     * UPDATED: Added email_verified
     */
    private Admin getAdminDetails(int adminId) throws SQLException {
        String sql = "SELECT u.user_id, u.email, u.is_active, u.email_verified, u.created_at, u.last_login " +
                "FROM AppUsers u WHERE u.user_id = ? AND u.role = 'ADMIN'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("user_id"));
                    admin.setEmail(rs.getString("email"));
                    admin.setActive(rs.getBoolean("is_active"));
                    admin.setEmailVerified(rs.getBoolean("email_verified"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    admin.setLastLogin(rs.getTimestamp("last_login"));
                    return admin;
                }
            }
        }
        return null;
    }

    /**
     * Get Staff details
     * UPDATED: Added email_verified
     */
    private Staff getStaffDetails(int staffId) throws SQLException {
        String sql = "SELECT * FROM vw_StaffManagement WHERE staff_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Staff staff = new Staff();
                    staff.setStaffId(rs.getInt("staff_id"));
                    staff.setEmail(rs.getString("email"));
                    staff.setName(rs.getString("name"));
                    staff.setPhone(rs.getString("phone"));
                    staff.setAddress(rs.getString("address"));
                    staff.setActive(rs.getBoolean("is_active"));
                    staff.setEmailVerified(rs.getBoolean("email_verified"));
                    staff.setCreatedAt(rs.getTimestamp("created_at"));
                    staff.setLastLogin(rs.getTimestamp("last_login"));
                    staff.setTotalOrders(rs.getInt("total_orders"));
                    staff.setTotalBlogs(rs.getInt("total_blogs"));
                    return staff;
                }
            }
        }
        return null;
    }

    /**
     * Get Customer details
     * UPDATED: Added oauth_id and email_verified
     */
    private Customer getCustomerDetails(int customerId) throws SQLException {
        String sql = "SELECT * FROM vw_CustomerList WHERE customer_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setEmail(rs.getString("email"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    customer.setOauthProvider(rs.getString("oauth_provider"));
                    customer.setOauthId(rs.getString("oauth_id"));
                    customer.setActive(rs.getBoolean("is_active"));
                    customer.setEmailVerified(rs.getBoolean("email_verified"));
                    customer.setCreatedAt(rs.getTimestamp("created_at"));
                    customer.setLastLogin(rs.getTimestamp("last_login"));
                    customer.setTotalOrders(rs.getInt("total_orders"));
                    customer.setTotalSpent(rs.getDouble("total_spent"));
                    return customer;
                }
            }
        }
        return null;
    }

    /**
     * Update last login timestamp
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE AppUsers SET last_login = GETDATE() WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error updating last login for user: {}", userId, e);
        }
    }

    /**
     * Register new customer using stored procedure
     * UPDATED: Added email verification parameters
     */
    public int registerCustomer(String name, String email, String password,
                                String phone, String address, String oauthProvider,
                                String oauthId, String verificationToken,
                                String ipAddress, String userAgent) {
        String sql = "{CALL sp_RegisterCustomer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            String hashedPassword = (password != null && !password.isEmpty())
                    ? BCrypt.hashpw(password, BCrypt.gensalt(12))
                    : "";

            stmt.setString(1, email.toLowerCase().trim());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, name.trim());
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, oauthProvider);
            stmt.setString(7, oauthId);
            stmt.setString(8, verificationToken);
            stmt.setString(9, ipAddress);
            stmt.setString(10, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                if ("SUCCESS".equals(result)) {
                    int customerId = rs.getInt("CustomerId");
                    logger.info("Customer registered successfully: {} (ID: {})", email, customerId);
                    return customerId;
                } else {
                    logger.warn("Failed to register customer: {}", rs.getString("Message"));
                }
            }

        } catch (SQLException e) {
            logger.error("Error registering customer: {}", email, e);
            throw new RuntimeException("Failed to register customer", e);
        }

        return -1;
    }

    /**
     * Register customer (backward compatibility - OLD VERSION)
     */
    public boolean registerCustomer(String name, String email, String password,
                                    String phone, String address, String oauthProvider) {
        int customerId = registerCustomer(name, email, password, phone, address,
                oauthProvider, null, null, null, null);
        return customerId > 0;
    }

    /**
     * Register customer (backward compatibility)
     */
    public boolean register(String name, String email, String password, String phone, String address) {
        return registerCustomer(name, email, password, phone, address, null);
    }

    /**
     * Register customer (backward compatibility - without phone/address)
     */
    public boolean register(String name, String email, String password) {
        return registerCustomer(name, email, password, null, null, null);
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM AppUsers WHERE email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    logger.debug("Email exists check for {}: {}", email, exists);
                    return exists;
                }
            }

        } catch (SQLException e) {
            logger.error("Error checking email exists: {}", email, e);
            throw new RuntimeException("Failed to check email existence", e);
        }

        return false;
    }

    /**
     * Get user by ID (returns base User object)
     * UPDATED: Added email_verified
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, email, role, is_active, email_verified, created_at, last_login " +
                "FROM AppUsers WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setEmailVerified(rs.getBoolean("email_verified"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setLastLogin(rs.getTimestamp("last_login"));

                    logger.debug("Retrieved user: {}", userId);
                    return user;
                }
            }

            logger.debug("User not found: {}", userId);
            return null;

        } catch (SQLException e) {
            logger.error("Error getting user by ID: {}", userId, e);
            throw new RuntimeException("Failed to retrieve user", e);
        }
    }

    /**
     * Update customer profile
     */
    public boolean updateCustomer(int customerId, String name, String phone, String address) {
        String sql = "UPDATE Customers SET name = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name.trim());
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setInt(4, customerId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated customer: {}", customerId);
            } else {
                logger.warn("No customer updated with ID: {}", customerId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating customer: {}", customerId, e);
            throw new RuntimeException("Failed to update customer", e);
        }
    }

    /**
     * Update user password
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE AppUsers SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Password updated for user: {}", userId);
            } else {
                logger.warn("No user found to update password for ID: {}", userId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating password for user: {}", userId, e);
            throw new RuntimeException("Failed to update password", e);
        }
    }

    /**
     * Verify user password
     */
    public boolean verifyPassword(int userId, String password) {
        String sql = "SELECT password_hash FROM AppUsers WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    return BCrypt.checkpw(password, storedHash);
                }
            }

        } catch (SQLException e) {
            logger.error("Error verifying password for user: {}", userId, e);
            throw new RuntimeException("Failed to verify password", e);
        }

        return false;
    }

    /**
     * Update user role (admin only - manual via SQL)
     */
    public boolean updateUserRole(int userId, String role) {
        String sql = "UPDATE AppUsers SET role = ? WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setInt(2, userId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated role for user {} to {}", userId, role);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating user role for ID: {}", userId, e);
            throw new RuntimeException("Failed to update user role", e);
        }
    }

    /**
     * Get user statistics
     */
    public UserStats getUserStats() {
        String sql = "SELECT " +
                "COUNT(*) as total_users, " +
                "SUM(CASE WHEN role = 'ADMIN' THEN 1 ELSE 0 END) as admin_count, " +
                "SUM(CASE WHEN role = 'CUSTOMER' THEN 1 ELSE 0 END) as customer_count, " +
                "SUM(CASE WHEN role = 'STAFF' THEN 1 ELSE 0 END) as staff_count " +
                "FROM AppUsers";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                UserStats stats = new UserStats();
                stats.setTotalUsers(rs.getInt("total_users"));
                stats.setAdminCount(rs.getInt("admin_count"));
                stats.setCustomerCount(rs.getInt("customer_count"));
                stats.setStaffCount(rs.getInt("staff_count"));

                logger.debug("Retrieved user statistics");
                return stats;
            }

        } catch (SQLException e) {
            logger.error("Error getting user statistics", e);
        }

        return new UserStats();
    }

    // ============================================
    // USER MANAGEMENT METHODS
    // ============================================

    /**
     * Get all users from vw_AllUsers view
     * UPDATED: Added email_verified and oauth_provider
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, email, role, is_active, email_verified, created_at, last_login, " +
                "name, phone, address, oauth_provider " +
                "FROM vw_AllUsers " +
                "WHERE role != 'ADMIN' " +
                "ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = mapUserFromResultSet(rs);
                users.add(user);
            }

            logger.debug("Retrieved {} non-admin users from vw_AllUsers", users.size());

        } catch (SQLException e) {
            logger.error("Error getting all users", e);
            throw new RuntimeException("Failed to retrieve all users", e);
        }

        return users;
    }

    /**
     * Get users by role
     * UPDATED: Added email_verified and oauth_provider
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, email, role, is_active, email_verified, created_at, last_login, " +
                "name, phone, address, oauth_provider FROM vw_AllUsers WHERE role = ? ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = mapUserFromResultSet(rs);
                users.add(user);
            }

            logger.debug("Retrieved {} users with role: {}", users.size(), role);

        } catch (SQLException e) {
            logger.error("Error getting users by role: {}", role, e);
            throw new RuntimeException("Failed to retrieve users by role", e);
        }

        return users;
    }

    /**
     * Search users by keyword
     * UPDATED: Added email_verified and oauth_provider
     */
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }

        String sql = "SELECT user_id, email, role, is_active, email_verified, created_at, last_login, " +
                "name, phone, address, oauth_provider FROM vw_AllUsers " +
                "WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? " +
                "ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = mapUserFromResultSet(rs);
                users.add(user);
            }

            logger.debug("Search for '{}' returned {} users", keyword, users.size());

        } catch (SQLException e) {
            logger.error("Error searching users", e);
            throw new RuntimeException("Failed to search users", e);
        }

        return users;
    }

    /**
     * Toggle user active status
     */
    public boolean toggleUserStatus(int userId, boolean isActive) {
        String sql = "UPDATE AppUsers SET is_active = ? WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Toggled status for user {} to {}", userId, isActive ? "active" : "inactive");
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error toggling user status for ID: {}", userId, e);
            throw new RuntimeException("Failed to toggle user status", e);
        }
    }

    /**
     * Deactivate user (soft delete)
     */
    public boolean deactivateUser(int userId) {
        return toggleUserStatus(userId, false);
    }

    /**
     * Activate user
     */
    public boolean activateUser(int userId) {
        return toggleUserStatus(userId, true);
    }

    /**
     * Map ResultSet to User object
     * UPDATED: Added email_verified and oauth_provider
     */
    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        user.setEmailVerified(rs.getBoolean("email_verified"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setName(rs.getString("name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setOauthProvider(rs.getString("oauth_provider"));
        return user;
    }

    /**
     * Inner class for user statistics
     */
    public static class UserStats {
        private int totalUsers;
        private int adminCount;
        private int customerCount;
        private int staffCount;

        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int total) { this.totalUsers = total; }

        public int getAdminCount() { return adminCount; }
        public void setAdminCount(int count) { this.adminCount = count; }

        public int getCustomerCount() { return customerCount; }
        public void setCustomerCount(int count) { this.customerCount = count; }

        public int getStaffCount() { return staffCount; }
        public void setStaffCount(int count) { this.staffCount = count; }
    }
}