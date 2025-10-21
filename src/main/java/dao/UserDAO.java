package dao;

import model.User;
import util.DBContext;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Authenticate user (login)
     */
    public User login(String email, String password) {
        String sql = "SELECT user_id, name, email, password_hash, role, phone, address, created_at " +
                "FROM AppUsers WHERE email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");

                    // Verify password using BCrypt
                    if (BCrypt.checkpw(password, storedHash)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(rs.getString("role"));
                        user.setPhone(rs.getString("phone"));
                        user.setAddress(rs.getString("address"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));

                        logger.info("User logged in successfully: {}", email);
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
     * Register new user with phone and address
     */
    public boolean register(String name, String email, String password, String phone, String address) {
        String sql = "INSERT INTO AppUsers (name, email, password_hash, role, phone, address) " +
                "VALUES (?, ?, ?, 'CUSTOMER', ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hash password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            stmt.setString(1, name.trim());
            stmt.setString(2, email.toLowerCase().trim());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, phone); // Can be null
            stmt.setString(5, address); // Can be null

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("User registered successfully: {} with phone: {}",
                        email, phone != null ? "Yes" : "No");
            } else {
                logger.warn("Failed to register user: {}", email);
            }

            return success;

        } catch (SQLException e) {
            if (e.getMessage().contains("Violation of UNIQUE KEY constraint")) {
                logger.warn("Email already exists: {}", email);
                return false;
            }
            logger.error("Error registering user: {}", email, e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    /**
     * Register new user (backward compatibility - without phone/address)
     */
    public boolean register(String name, String email, String password) {
        return register(name, email, password, null, null);
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
     * Get user by ID
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, name, email, role, phone, address, created_at " +
                "FROM AppUsers WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));

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
     * Get all users (admin)
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, role, phone, address, created_at " +
                "FROM AppUsers ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }

            logger.debug("Retrieved {} users", users.size());
            return users;

        } catch (SQLException e) {
            logger.error("Error getting all users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    /**
     * Update user profile
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE AppUsers SET name = ?, email = ?, phone = ?, address = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName().trim());
            stmt.setString(2, user.getEmail().toLowerCase().trim());
            stmt.setString(3, user.getPhone()); // Can be null
            stmt.setString(4, user.getAddress()); // Can be null
            stmt.setInt(5, user.getUserId());

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated user: {}", user.getUserId());
            } else {
                logger.warn("No user updated with ID: {}", user.getUserId());
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getUserId(), e);
            throw new RuntimeException("Failed to update user", e);
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
     * Delete user (admin only)
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM AppUsers WHERE user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("User deleted: {}", userId);
            } else {
                logger.warn("No user deleted with ID: {}", userId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error deleting user: {}", userId, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Update user role (admin only)
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

    /**
     * Verify user password (for password change operations)
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
     * Search users by keyword (name or email)
     */
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return users;
        }

        String sql = "SELECT user_id, name, email, role, phone, address, created_at " +
                "FROM AppUsers WHERE name LIKE ? OR email LIKE ? " +
                "ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    users.add(user);
                }
            }

            logger.debug("Search for '{}' returned {} users", keyword, users.size());
            return users;

        } catch (SQLException e) {
            logger.error("Error searching users with keyword: {}", keyword, e);
            throw new RuntimeException("Failed to search users", e);
        }
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