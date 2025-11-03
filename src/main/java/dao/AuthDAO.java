package dao;

import model.Customer;
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

    // ============================================
    // LOGIN
    // ============================================

    public Customer login(String email, String password) {
        String sql = "SELECT u.user_id, u.email, u.password_hash, u.is_active, u.email_verified, " +
                "c.name, c.phone, c.address, c.oauth_provider, c.oauth_id " +
                "FROM AppUsers u " +
                "JOIN Customers c ON u.user_id = c.customer_id " +
                "WHERE u.email = ? AND u.role = 'CUSTOMER'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (!rs.getBoolean("is_active")) {
                    logger.warn("Login attempt for inactive account: {}", email);
                    return null;
                }

                String storedHash = rs.getString("password_hash");
                if (BCrypt.checkpw(password, storedHash)) {
                    // Update last login
                    updateLastLogin(rs.getInt("user_id"));

                    // Build Customer object
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("user_id"));
                    customer.setEmail(rs.getString("email"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    customer.setOauthProvider(rs.getString("oauth_provider"));
                    customer.setOauthId(rs.getString("oauth_id"));
                    customer.setActive(true);
                    customer.setEmailVerified(rs.getBoolean("email_verified"));

                    logger.info("User logged in: {}", email);
                    return customer;
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

    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM vw_CustomerList WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
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
                return customer;
            }
        } catch (SQLException e) {
            logger.error("Error getting customer by email", e);
        }
        return null;
    }
}