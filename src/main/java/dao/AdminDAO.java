package dao;

import model.Staff;
import util.DBContext;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO - Admin manages Staff accounts
 * UPDATED: Removed position, department, salary, notes, created_by fields
 */
public class AdminDAO {
    private static final Logger logger = LoggerFactory.getLogger(AdminDAO.class);

    /**
     * Admin creates Staff account - UPDATED: Simplified parameters
     */
    public boolean createStaff(int adminId, String email, String password, String name,
                               String phone, String address) {
        String sql = "{CALL sp_AdminCreateStaff(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            stmt.setInt(1, adminId);
            stmt.setString(2, email.toLowerCase().trim());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, name.trim());
            stmt.setString(5, phone);
            stmt.setString(6, address);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                if ("SUCCESS".equals(result)) {
                    logger.info("Admin {} created staff: {}", adminId, email);
                    return true;
                } else {
                    logger.warn("Failed to create staff: {}", rs.getString("Message"));
                }
            }

        } catch (SQLException e) {
            logger.error("Error creating staff", e);
        }

        return false;
    }

    /**
     * Admin updates Staff info - UPDATED: Simplified parameters
     */
    public boolean updateStaff(int adminId, int staffId, String name, String phone, String address) {
        String sql = "{CALL sp_AdminUpdateStaff(?, ?, ?, ?, ?)}";

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, adminId);
            stmt.setInt(2, staffId);
            stmt.setString(3, name.trim());
            stmt.setString(4, phone);
            stmt.setString(5, address);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean success = "SUCCESS".equals(rs.getString("Result"));
                if (success) {
                    logger.info("Admin {} updated staff: {}", adminId, staffId);
                }
                return success;
            }

        } catch (SQLException e) {
            logger.error("Error updating staff", e);
        }

        return false;
    }

    /**
     * Admin toggles Staff active status
     */
    public boolean toggleStaffStatus(int adminId, int staffId, boolean isActive) {
        String sql = "{CALL sp_AdminToggleStaffStatus(?, ?, ?)}";

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, adminId);
            stmt.setInt(2, staffId);
            stmt.setBoolean(3, isActive);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean success = "SUCCESS".equals(rs.getString("Result"));
                if (success) {
                    logger.info("Admin {} toggled staff {} to {}", adminId, staffId, isActive ? "active" : "inactive");
                }
                return success;
            }

        } catch (SQLException e) {
            logger.error("Error toggling staff status", e);
        }

        return false;
    }

    /**
     * Admin resets Staff password
     */
    public boolean resetStaffPassword(int adminId, int staffId, String newPassword) {
        String sql = "{CALL sp_AdminResetStaffPassword(?, ?, ?)}";

        try (Connection conn = DBContext.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

            stmt.setInt(1, adminId);
            stmt.setInt(2, staffId);
            stmt.setString(3, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean success = "SUCCESS".equals(rs.getString("Result"));
                if (success) {
                    logger.info("Admin {} reset password for staff {}", adminId, staffId);
                }
                return success;
            }

        } catch (SQLException e) {
            logger.error("Error resetting staff password", e);
        }

        return false;
    }

    /**
     * Get all Staff - UPDATED: Simplified view query
     */
    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM vw_StaffManagement ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Staff staff = mapStaffFromResultSet(rs);
                staffList.add(staff);
            }

            logger.debug("Retrieved {} staff members", staffList.size());

        } catch (SQLException e) {
            logger.error("Error getting all staff", e);
        }

        return staffList;
    }

    /**
     * Get Staff by ID
     */
    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM vw_StaffManagement WHERE staff_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapStaffFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.error("Error getting staff by ID", e);
        }

        return null;
    }

    /**
     * Search Staff by name or email
     */
    public List<Staff> searchStaff(String keyword) {
        List<Staff> staffList = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStaff();
        }

        String sql = "SELECT * FROM vw_StaffManagement " +
                "WHERE name LIKE ? OR email LIKE ? " +
                "ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Staff staff = mapStaffFromResultSet(rs);
                staffList.add(staff);
            }

            logger.debug("Search for '{}' returned {} staff", keyword, staffList.size());

        } catch (SQLException e) {
            logger.error("Error searching staff", e);
        }

        return staffList;
    }

    /**
     * Get active Staff only
     */
    public List<Staff> getActiveStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM vw_StaffManagement WHERE is_active = 1 ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Staff staff = mapStaffFromResultSet(rs);
                staffList.add(staff);
            }

        } catch (SQLException e) {
            logger.error("Error getting active staff", e);
        }

        return staffList;
    }

    /**
     * Get active Staff count
     */
    public int getActiveStaffCount() {
        String sql = "SELECT COUNT(*) FROM AppUsers WHERE role = 'STAFF' AND is_active = 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error getting active staff count", e);
        }

        return 0;
    }

    /**
     * Get total Staff count
     */
    public int getStaffCount() {
        String sql = "SELECT COUNT(*) FROM Staff";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error getting staff count", e);
        }

        return 0;
    }

    /**
     * Map ResultSet to Staff object - UPDATED: Removed unused fields
     */
    private static Staff mapStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setEmail(rs.getString("email"));
        staff.setName(rs.getString("name"));
        staff.setPhone(rs.getString("phone"));
        staff.setAddress(rs.getString("address"));
        staff.setActive(rs.getBoolean("is_active"));
        staff.setCreatedAt(rs.getTimestamp("created_at"));
        staff.setLastLogin(rs.getTimestamp("last_login"));
        staff.setTotalOrders(rs.getInt("total_orders"));
        staff.setTotalBlogs(rs.getInt("total_blogs"));

        return staff;
    }
}