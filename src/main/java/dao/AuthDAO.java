package dao;

import model.Customer;
import model.User;
import util.DBContext;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthDAO - All authentication operations
 */
public class AuthDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuthDAO.class);

    // ============================================
    // REGISTER
    // ============================================

    public Map<String, Object> register(String email, String password, String name,
                                        String phone, String address,
                                        String verificationToken, String ipAddress, String userAgent) {
        String sql = "{CALL sp_RegisterCustomer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            stmt.setString(1, email.toLowerCase().trim());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, name.trim());
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, null); // oauthProvider
            stmt.setString(7, null); // oauthId
            stmt.setString(8, verificationToken);
            stmt.setString(9, ipAddress);
            stmt.setString(10, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                result.put("success", "SUCCESS".equals(status));
                result.put("message", rs.getString("Message"));
                if ("SUCCESS".equals(status)) {
                    result.put("customerId", rs.getInt("CustomerId"));
                    logger.info("User registered: {} (ID: {})", email, rs.getInt("CustomerId"));
                }
            }
        } catch (SQLException e) {
            logger.error("Registration error for: {}", email, e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống: " + e.getMessage());
        }

        return result;
    }


    /**
     * Login user - Returns User object for ALL roles (ADMIN, STAFF, CUSTOMER)
     */
    public User login(String email, String password) {
        String sql = "SELECT u.user_id, u.email, u.password_hash, u.role, u.is_active, u.email_verified, " +
                "u.created_at, u.last_login, " +
                "COALESCE(c.name, s.name) as name, " +
                "COALESCE(c.phone, s.phone) as phone, " +
                "COALESCE(c.address, s.address) as address, " +
                "c.oauth_provider, c.oauth_id " +
                "FROM AppUsers u " +
                "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                "LEFT JOIN Staff s ON u.user_id = s.staff_id " +
                "WHERE u.email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (!rs.getBoolean("is_active")) {
                    logger.warn("⚠️ Login attempt for inactive account: {}", email);
                    return null;
                }

                String storedHash = rs.getString("password_hash");
                if (BCrypt.checkpw(password, storedHash)) {
                    // Update last login
                    updateLastLogin(rs.getInt("user_id"));

                    // User object
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role")); // ADMIN, STAFF, or CUSTOMER
                    user.setName(rs.getString("name"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setOauthProvider(rs.getString("oauth_provider"));
                    user.setActive(true);
                    user.setEmailVerified(rs.getBoolean("email_verified"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setLastLogin(rs.getTimestamp("last_login"));

                    logger.info("User logged in: {} (Role: {})", email, user.getRole());
                    return user;
                } else {
                    logger.warn("Wrong password for: {}", email);
                }
            } else {
                logger.warn("Email not found: {}", email);
            }
        } catch (SQLException e) {
            logger.error("Login error for: {}", email, e);
        }

        return null;
    }

    private void updateLastLogin(int userId) {
        String sql = "UPDATE AppUsers SET last_login = GETDATE() WHERE user_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating last login", e);
        }
    }

    // ============================================
    // EMAIL VERIFICATION
    // ============================================

    public Map<String, Object> verifyEmail(String token, String ipAddress) {
        String sql = "{CALL sp_VerifyEmail(?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, token);
            stmt.setString(2, ipAddress);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                result.put("success", "SUCCESS".equals(status));
                result.put("message", rs.getString("Message"));
                if ("SUCCESS".equals(status)) {
                    result.put("userId", rs.getInt("UserId"));
                    logger.info("Email verified for user: {}", rs.getInt("UserId"));
                } else {
                    logger.warn("Email verification failed: {}", rs.getString("Message"));
                }
            }
        } catch (SQLException e) {
            logger.error("Email verification error", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    public Map<String, Object> resendVerification(String email, String newToken,
                                                  String ipAddress, String userAgent) {
        String sql = "{CALL sp_ResendVerificationEmail(?, ?, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, newToken);
            stmt.setString(3, ipAddress);
            stmt.setString(4, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                result.put("success", "SUCCESS".equals(status));
                result.put("message", rs.getString("Message"));
                logger.info("Resent verification to: {}", email);
            }
        } catch (SQLException e) {
            logger.error("Resend verification error for: {}", email, e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    // ============================================
    // PASSWORD RESET
    // ============================================

    public Map<String, Object> requestPasswordReset(String email, String token,
                                                    String ipAddress, String userAgent) {
        String sql = "{CALL sp_GeneratePasswordResetToken(?, ?, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setString(3, ipAddress);
            stmt.setString(4, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                result.put("success", "SUCCESS".equals(status));
                result.put("message", rs.getString("Message"));
                if ("SUCCESS".equals(status)) {
                    result.put("userId", rs.getInt("UserId"));
                    logger.info("Password reset requested for: {}", email);
                }
            }
        } catch (SQLException e) {
            logger.error("Password reset request error for: {}", email, e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    public Map<String, Object> resetPassword(String token, String newPassword, String ipAddress) {
        String sql = "{CALL sp_ResetPassword(?, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            stmt.setString(1, token);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, ipAddress);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                result.put("success", "SUCCESS".equals(status));
                result.put("message", rs.getString("Message"));
                if ("SUCCESS".equals(status)) {
                    result.put("userId", rs.getInt("UserId"));
                    logger.info("Password reset for user: {}", rs.getInt("UserId"));
                }
            }
        } catch (SQLException e) {
            logger.error("Password reset error", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    // ============================================
    // UTILITIES
    // ============================================

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM AppUsers WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking email exists", e);
        }
        return false;
    }

    /**
     * Get user by email - Returns User object
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT u.user_id, u.email, u.role, u.is_active, u.email_verified, " +
                "u.created_at, u.last_login, " +
                "COALESCE(c.name, s.name) as name, " +
                "COALESCE(c.phone, s.phone) as phone, " +
                "COALESCE(c.address, s.address) as address, " +
                "c.oauth_provider, c.oauth_id " +
                "FROM AppUsers u " +
                "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                "LEFT JOIN Staff s ON u.user_id = s.staff_id " +
                "WHERE u.email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setOauthProvider(rs.getString("oauth_provider"));
                user.setActive(rs.getBoolean("is_active"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                return user;
            }
        } catch (SQLException e) {
            logger.error("Error getting user by email", e);
        }
        return null;
    }

    /**
     * Get customer by email - Backward compatibility
     */
    public Customer getCustomerByEmail(String email) {
        User user = getUserByEmail(email);
        if (user != null && user.isCustomer()) {
            // Convert User to Customer
            Customer customer = new Customer();
            customer.setCustomerId(user.getUserId());
            customer.setEmail(user.getEmail());
            customer.setName(user.getName());
            customer.setPhone(user.getPhone());
            customer.setAddress(user.getAddress());
            customer.setOauthProvider(user.getOauthProvider());
            customer.setActive(user.isActive());
            customer.setEmailVerified(user.isEmailVerified());
            customer.setCreatedAt(user.getCreatedAt());
            customer.setLastLogin(user.getLastLogin());
            return customer;
        }
        return null;
    }
    /**
     * Login or register user with Google OAuth
     */
    public User loginOrRegisterWithGoogle(String email, String name, String googleId) {
        try (Connection conn = DBContext.getConnection()) {
            // Check if user exists
            String checkSql = "SELECT user_id FROM AppUsers WHERE email = ?";

            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // User exists - login
                    int userId = rs.getInt("user_id");
                    updateLastLogin(userId);
                    return getUserById(userId);
                }
            }

            // User doesn't exist - register
            return registerGoogleUser(conn, email, name, googleId);

        } catch (SQLException e) {
            logger.error("Error with Google OAuth login", e);
            return null;
        }
    }

    private User registerGoogleUser(Connection conn, String email, String name, String googleId)
            throws SQLException {

        conn.setAutoCommit(false);

        try {
            // Insert into AppUsers
            String userSql = "INSERT INTO AppUsers (email, password_hash, role, is_active, email_verified, created_at) " +
                    "OUTPUT INSERTED.user_id " +
                    "VALUES (?, '', 'CUSTOMER', 1, 1, GETDATE())";

            int userId;
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to create user");
                }
            }

            // Insert into Customers
            String customerSql = "INSERT INTO Customers (customer_id, name, oauth_provider, oauth_id) " +
                    "VALUES (?, ?, 'GOOGLE', ?)";

            try (PreparedStatement ps = conn.prepareStatement(customerSql)) {
                ps.setInt(1, userId);
                ps.setString(2, name);
                ps.setString(3, googleId);
                ps.executeUpdate();
            }

            conn.commit();

            logger.info("Google user registered: {}", email);
            return getUserById(userId);

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private User getUserById(int userId) throws SQLException {
        String sql = "SELECT u.user_id, u.email, u.role, u.is_active, u.email_verified, " +
                "u.created_at, u.last_login, " +
                "c.name, c.phone, c.address, c.oauth_provider, c.oauth_id " +
                "FROM AppUsers u " +
                "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                "WHERE u.user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setOauthProvider(rs.getString("oauth_provider"));
                user.setActive(rs.getBoolean("is_active"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                return user;
            }
        }

        return null;
    }

}