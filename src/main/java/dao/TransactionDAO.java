package dao;

import model.Transaction;
import util.DBContext;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private static final Logger logger = LoggerFactory.getLogger(TransactionDAO.class);

    /**
     * Create a new transaction
     */
    public int createTransaction(int orderId, double amount, String type) {
        String sql = "INSERT INTO Transactions (order_id, amount, type) VALUES (?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderId);
            stmt.setDouble(2, amount);
            stmt.setString(3, type);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int transactionId = rs.getInt(1);
                        logger.info("Created transaction {} for order {}: {} - {}đ",
                                transactionId, orderId, type, amount);
                        return transactionId;
                    }
                }
            }

            return -1;

        } catch (SQLException e) {
            logger.error("Error creating transaction for order: {}", orderId, e);
            throw new DatabaseException("Failed to create transaction", e);
        }
    }

    /**
     * Get transactions by order ID
     */
    public List<Transaction> getTransactionsByOrderId(int orderId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, order_id, amount, type, created_at " +
                "FROM Transactions WHERE order_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setOrderId(rs.getInt("order_id"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setType(rs.getString("type"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(transaction);
                }
            }

            logger.debug("Retrieved {} transactions for order: {}", transactions.size(), orderId);
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting transactions for order: {}", orderId, e);
            throw new DatabaseException("Failed to retrieve transactions", e);
        }
    }

    /**
     * Get transactions by user ID
     */
    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.transaction_id, t.order_id, t.amount, t.type, t.created_at " +
                "FROM Transactions t " +
                "JOIN Orders o ON t.order_id = o.order_id " +
                "WHERE o.user_id = ? ORDER BY t.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setOrderId(rs.getInt("order_id"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setType(rs.getString("type"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(transaction);
                }
            }

            logger.debug("Retrieved {} transactions for user: {}", transactions.size(), userId);
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting transactions for user: {}", userId, e);
            throw new DatabaseException("Failed to retrieve transactions", e);
        }
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT transaction_id, order_id, amount, type, created_at " +
                "FROM Transactions WHERE transaction_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setOrderId(rs.getInt("order_id"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setType(rs.getString("type"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    return transaction;
                }
            }

            return null;

        } catch (SQLException e) {
            logger.error("Error getting transaction: {}", transactionId, e);
            throw new DatabaseException("Failed to retrieve transaction", e);
        }
    }

    /**
     * Calculate total paid amount for an order
     */
    public double getTotalPaidAmount(int orderId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total_paid FROM Transactions WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_paid");
                }
            }

            return 0.0;

        } catch (SQLException e) {
            logger.error("Error calculating total paid for order: {}", orderId, e);
            return 0.0;
        }
    }

    /**
     * Check if order is fully paid
     */
    public boolean isOrderFullyPaid(int orderId, double orderTotal) {
        double totalPaid = getTotalPaidAmount(orderId);
        return totalPaid >= orderTotal;
    }

    /**
     * Get remaining balance for an order
     */
    public double getRemainingBalance(int orderId, double orderTotal) {
        double totalPaid = getTotalPaidAmount(orderId);
        return Math.max(0, orderTotal - totalPaid);
    }
}