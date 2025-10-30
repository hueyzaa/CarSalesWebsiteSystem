package dao;

import model.Customer;
import util.DBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO - Customer-specific operations
 * UPDATED: Removed all loyalty_points related methods
 */
public class CustomerDAO {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    /**
     * Get all customers with statistics
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM vw_CustomerList ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

            logger.debug("Retrieved {} customers", customers.size());

        } catch (SQLException e) {
            logger.error("Error getting all customers", e);
        }
        return customers;
    }

    /**
     * Get active customers only
     */
    public List<Customer> getActiveCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM vw_CustomerList WHERE is_active = 1 ORDER BY name";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            logger.error("Error getting active customers", e);
        }
        return customers;
    }

    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM vw_CustomerList WHERE customer_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapCustomerFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.error("Error getting customer by ID: {}", customerId, e);
        }
        return null;
    }

    /**
     * Get customer by email
     */
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM vw_CustomerList WHERE email = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapCustomerFromResultSet(rs);
            }

        } catch (SQLException e) {
            logger.error("Error getting customer by email: {}", email, e);
        }
        return null;
    }

    /**
     * Update customer profile
     */
    public boolean updateCustomer(int customerId, String name, String phone, String address) {
        String sql = "UPDATE Customers SET name = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setInt(4, customerId);

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                logger.info("Updated customer: {}", customerId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("Error updating customer: {}", customerId, e);
        }
        return false;
    }

    /**
     * Search customers by keyword
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCustomers();
        }

        String sql = "SELECT * FROM vw_CustomerList " +
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
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

            logger.debug("Search for '{}' returned {} customers", keyword, customers.size());

        } catch (SQLException e) {
            logger.error("Error searching customers", e);
        }
        return customers;
    }

    /**
     * Get top customers by spending
     */
    public List<Customer> getTopCustomers(int limit) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM vw_CustomerList ORDER BY total_spent DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            logger.error("Error getting top customers", e);
        }
        return customers;
    }

    /**
     * Get customers by OAuth provider
     */
    public List<Customer> getCustomersByOAuthProvider(String provider) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM vw_CustomerList WHERE oauth_provider = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, provider);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            logger.error("Error getting customers by OAuth provider", e);
        }
        return customers;
    }

    /**
     * Get customer count
     */
    public int getCustomerCount() {
        String sql = "SELECT COUNT(*) FROM Customers";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error getting customer count", e);
        }
        return 0;
    }

    /**
     * Get active customer count
     */
    public int getActiveCustomerCount() {
        String sql = "SELECT COUNT(*) FROM vw_CustomerList WHERE is_active = 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error getting active customer count", e);
        }
        return 0;
    }

    /**
     * Deactivate customer (soft delete)
     */
    public boolean deactivateCustomer(int customerId) {
        String sql = "UPDATE AppUsers SET is_active = 0 WHERE user_id = ? AND role = 'CUSTOMER'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Deactivated customer: {}", customerId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("Error deactivating customer", e);
        }
        return false;
    }

    /**
     * Activate customer
     */
    public boolean activateCustomer(int customerId) {
        String sql = "UPDATE AppUsers SET is_active = 1 WHERE user_id = ? AND role = 'CUSTOMER'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Activated customer: {}", customerId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("Error activating customer", e);
        }
        return false;
    }

    /**
     * Map ResultSet to Customer object
     * UPDATED: Removed loyalty_points mapping
     */
    private Customer mapCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setEmail(rs.getString("email"));
        customer.setName(rs.getString("name"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setOauthProvider(rs.getString("oauth_provider"));
        customer.setActive(rs.getBoolean("is_active"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        customer.setLastLogin(rs.getTimestamp("last_login"));
        customer.setTotalOrders(rs.getInt("total_orders"));
        customer.setTotalSpent(rs.getDouble("total_spent"));
        return customer;
    }
}