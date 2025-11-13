package dao;

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
 * UPDATED: Public getUserById() for stateless email verification
 */
public class AuthDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuthDAO.class);

    // ============================================
    // LOGIN - All roles (CUSTOMER, STAFF, ADMIN)
    // ============================================

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
                    logger.warn("Login attempt for inactive account: {}", email);
                    return null;
                }

                // Check email verification
                if (!rs.getBoolean("email_verified")) {
                    logger.warn("Login attempt for unverified account: {}", email);
                    return null;
                }

                String storedHash = rs.getString("password_hash");
                String oauthProvider = rs.getString("oauth_provider");

                if (oauthProvider != null && !oauthProvider.isEmpty()) {
                    logger.warn("Attempted password login for OAuth account: {} ({})", email, oauthProvider);
                    return null;
                }

                if (storedHash != null && !storedHash.isEmpty() && BCrypt.checkpw(password, storedHash)) {
                    updateLastLogin(rs.getInt("user_id"));
                    User user = mapUserFromResultSet(rs);
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
    // CUSTOMER REGISTRATION (Database-based verification)
    // ============================================

    /**
     * Register customer with unverified email (email_verified = 0)
     * Returns userId if successful, -1 if failed
     */
    public int registerCustomerUnverified(String email, String hashedPassword,
                                          String name, String phone, String address) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            // Insert into AppUsers with email_verified = 0
            int userId = insertUnverifiedUser(conn, email, hashedPassword);

            // Insert into Customers
            insertCustomer(conn, userId, name, phone, address);

            conn.commit();
            logger.info("Unverified customer registered: {} (ID: {})", email, userId);
            return userId;

        } catch (SQLException e) {
            logger.error("Error registering unverified customer: {}", email, e);
            rollbackConnection(conn);
            return -1;
        } finally {
            closeConnection(conn);
        }
    }

    private int insertUnverifiedUser(Connection conn, String email, String hashedPassword)
            throws SQLException {
        String sql = "INSERT INTO AppUsers (email, password_hash, role, is_active, email_verified, created_at) " +
                "OUTPUT INSERTED.user_id " +
                "VALUES (?, ?, 'CUSTOMER', 1, 0, GETDATE())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase().trim());
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create user");
        }
    }

    /**
     * Generate email verification token using stored procedure
     */
    public Map<String, Object> generateEmailVerificationToken(int userId, String token,
                                                              String ipAddress, String userAgent) {
        String sql = "{CALL sp_GenerateEmailVerificationToken(?, ?, 24, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, token);
            stmt.setString(3, ipAddress);
            stmt.setString(4, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                boolean success = "SUCCESS".equals(status);
                result.put("success", success);
                result.put("message", rs.getString("Message"));

                if (success) {
                    result.put("token", rs.getString("Token"));
                    result.put("expiryDate", rs.getTimestamp("ExpiryDate"));
                    logger.info("Verification token generated for user: {}", userId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error generating verification token for user: {}", userId, e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    /**
     * Verify email using stored procedure
     */
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
                boolean success = "SUCCESS".equals(status);
                result.put("success", success);
                result.put("message", rs.getString("Message"));

                if (success) {
                    result.put("userId", rs.getInt("UserId"));
                    logger.info("Email verified for user: {}", rs.getInt("UserId"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error verifying email with token", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    /**
     * Resend verification email using stored procedure
     */
    public Map<String, Object> resendVerificationEmail(String email, String newToken,
                                                       String ipAddress, String userAgent) {
        String sql = "{CALL sp_ResendVerificationEmail(?, ?, ?, ?)}";
        Map<String, Object> result = new HashMap<>();

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, email.toLowerCase().trim());
            stmt.setString(2, newToken);
            stmt.setString(3, ipAddress);
            stmt.setString(4, userAgent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Result");
                boolean success = "SUCCESS".equals(status);
                result.put("success", success);
                result.put("message", rs.getString("Message"));

                logger.info("Verification email resent for: {}", email);
            }
        } catch (SQLException e) {
            logger.error("Error resending verification email for: {}", email, e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống");
        }

        return result;
    }

    /**
     * DEPRECATED: Use registerCustomerUnverified() instead
     */
    @Deprecated
    public boolean registerVerifiedCustomer(String email, String hashedPassword,
                                            String name, String phone, String address,
                                            @SuppressWarnings("unused") String ipAddress) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            int userId = insertAppUser(conn, email, hashedPassword);
            insertCustomer(conn, userId, name, phone, address);

            conn.commit();
            logger.info("Verified customer registered: {} (ID: {})", email, userId);
            return true;

        } catch (SQLException e) {
            logger.error("Error registering verified customer: {}", email, e);
            rollbackConnection(conn);
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    private int insertAppUser(Connection conn, String email, String hashedPassword) throws SQLException {
        String sql = "INSERT INTO AppUsers (email, password_hash, role, is_active, email_verified, created_at) " +
                "OUTPUT INSERTED.user_id " +
                "VALUES (?, ?, 'CUSTOMER', 1, 1, GETDATE())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase().trim());
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create user");
        }
    }

    private void insertCustomer(Connection conn, int userId, String name, String phone, String address)
            throws SQLException {
        String sql = "INSERT INTO Customers (customer_id, name, phone, address, oauth_provider, oauth_id) " +
                "VALUES (?, ?, ?, ?, NULL, NULL)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.executeUpdate();
        }
    }

    // ============================================
    // PASSWORD RESET (Database-based tokens)
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
                boolean success = "SUCCESS".equals(status);
                result.put("success", success);
                result.put("message", rs.getString("Message"));
                if (success) {
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
                boolean success = "SUCCESS".equals(status);
                result.put("success", success);
                result.put("message", rs.getString("Message"));
                if (success) {
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
    // GOOGLE OAUTH (CUSTOMER only)
    // ============================================

    public User loginOrRegisterWithGoogle(String email, String name, String googleId) {
        try (Connection conn = DBContext.getConnection()) {

            String checkSql = "SELECT u.user_id, u.password_hash, u.role, " +
                    "c.oauth_provider, c.oauth_id " +
                    "FROM AppUsers u " +
                    "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                    "WHERE u.email = ?";

            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String passwordHash = rs.getString("password_hash");
                    String role = rs.getString("role");
                    String oauthProvider = rs.getString("oauth_provider");
                    String oauthId = rs.getString("oauth_id");

                    if (!"CUSTOMER".equals(role)) {
                        logger.warn("Non-customer role attempting Google OAuth: {} ({})", email, role);
                        return null;
                    }

                    if ("GOOGLE".equals(oauthProvider) && googleId.equals(oauthId)) {
                        logger.info("Existing Google OAuth user login: {}", email);
                        updateLastLogin(userId);
                        return getUserById(userId);
                    }

                    if (passwordHash != null && !passwordHash.isEmpty() && oauthProvider == null) {
                        logger.warn("Email registered with password, blocking Google OAuth: {}", email);
                        return null;
                    }

                    if (oauthProvider != null && !"GOOGLE".equals(oauthProvider)) {
                        logger.warn("Email already linked to: {}", oauthProvider);
                        return null;
                    }

                    if ("GOOGLE".equals(oauthProvider) && !googleId.equals(oauthId)) {
                        logger.error("Google ID mismatch for user: {}", email);
                        return null;
                    }
                }
            }

            logger.info("Registering new Google OAuth user: {}", email);
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
            int userId = insertOAuthUser(conn, email);
            insertOAuthCustomer(conn, userId, name, googleId);

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

    private int insertOAuthUser(Connection conn, String email) throws SQLException {
        String sql = "INSERT INTO AppUsers (email, password_hash, role, is_active, email_verified, created_at) " +
                "OUTPUT INSERTED.user_id " +
                "VALUES (?, '', 'CUSTOMER', 1, 1, GETDATE())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create OAuth user");
        }
    }

    private void insertOAuthCustomer(Connection conn, int userId, String name, String googleId)
            throws SQLException {
        String sql = "INSERT INTO Customers (customer_id, name, oauth_provider, oauth_id) " +
                "VALUES (?, ?, 'GOOGLE', ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, googleId);
            ps.executeUpdate();
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
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

    public User getUserById(int userId) {
        String sql = "SELECT u.user_id, u.email, u.role, u.is_active, u.email_verified, " +
                "u.created_at, u.last_login, " +
                "COALESCE(c.name, s.name) as name, " +
                "COALESCE(c.phone, s.phone) as phone, " +
                "COALESCE(c.address, s.address) as address, " +
                "c.oauth_provider, c.oauth_id " +
                "FROM AppUsers u " +
                "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                "LEFT JOIN Staff s ON u.user_id = s.staff_id " +
                "WHERE u.user_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error getting user by ID: {}", userId, e);
        }

        return null;
    }

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
                return mapUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error getting user by email", e);
        }
        return null;
    }

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

    public boolean isOAuthAccount(String email) {
        String sql = "SELECT c.oauth_provider FROM AppUsers u " +
                "JOIN Customers c ON u.user_id = c.customer_id " +
                "WHERE u.email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String oauthProvider = rs.getString("oauth_provider");
                return oauthProvider != null && !oauthProvider.isEmpty();
            }

        } catch (SQLException e) {
            logger.error("Error checking OAuth status", e);
        }

        return false;
    }

    public boolean isPasswordAccount(String email) {
        String sql = "SELECT u.password_hash, c.oauth_provider " +
                "FROM AppUsers u " +
                "LEFT JOIN Customers c ON u.user_id = c.customer_id " +
                "WHERE u.email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String passwordHash = rs.getString("password_hash");
                String oauthProvider = rs.getString("oauth_provider");
                return (passwordHash != null && !passwordHash.isEmpty() && oauthProvider == null);
            }

        } catch (SQLException e) {
            logger.error("Error checking account type", e);
        }

        return false;
    }

    public String getOAuthProvider(String email) {
        String sql = "SELECT c.oauth_provider FROM AppUsers u " +
                "JOIN Customers c ON u.user_id = c.customer_id " +
                "WHERE u.email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("oauth_provider");
            }

        } catch (SQLException e) {
            logger.error("Error getting OAuth provider", e);
        }

        return null;
    }

    // ============================================
    // CONNECTION UTILITIES
    // ============================================

    private void rollbackConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                logger.error("Rollback failed", ex);
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
}