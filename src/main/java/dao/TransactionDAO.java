package dao;

import model.Transaction;
import util.DBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private static final Logger logger = LoggerFactory.getLogger(TransactionDAO.class);

    /**
     * Create a new transaction with payment status
     */
    public int createTransaction(int orderId, double amount, String type, String paymentStatus) {
        String sql = "INSERT INTO Transactions (order_id, amount, type, payment_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderId);
            stmt.setDouble(2, amount);
            stmt.setString(3, type);
            stmt.setString(4, paymentStatus);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int transactionId = rs.getInt(1);
                        logger.info("Created transaction {} for order {}: {} - {}₫ - Status: {}",
                                transactionId, orderId, type, amount, paymentStatus);
                        return transactionId;
                    }
                }
            }

            return -1;

        } catch (SQLException e) {
            logger.error("Error creating transaction for order: {}", orderId, e);
            throw new RuntimeException("Failed to create transaction", e);
        }
    }

    /**
     * Create a new transaction (default to PENDING status)
     */
    public int createTransaction(int orderId, double amount, String type) {
        return createTransaction(orderId, amount, type, "PENDING");
    }

    /**
     * Update transaction payment status
     */
    public boolean updatePaymentStatus(int transactionId, String paymentStatus) {
        String sql = "UPDATE Transactions SET payment_status = ? WHERE transaction_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paymentStatus);
            stmt.setInt(2, transactionId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated transaction {} payment status to {}", transactionId, paymentStatus);
            } else {
                logger.warn("No transaction updated with ID: {}", transactionId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating payment status for transaction: {}", transactionId, e);
            throw new RuntimeException("Failed to update payment status", e);
        }
    }

    /**
     * Mark transaction as paid
     */
    public boolean markAsPaid(int transactionId) {
        return updatePaymentStatus(transactionId, "PAID");
    }

    /**
     * Mark transaction as cancelled
     */
    public boolean markAsCancelled(int transactionId) {
        return updatePaymentStatus(transactionId, "CANCELLED");
    }

    /**
     * Get transactions by order ID
     */
    public List<Transaction> getTransactionsByOrderId(int orderId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, order_id, amount, type, payment_status, created_at " +
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
                    transaction.setPaymentStatus(rs.getString("payment_status"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(transaction);
                }
            }

            logger.debug("Retrieved {} transactions for order: {}", transactions.size(), orderId);
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting transactions for order: {}", orderId, e);
            throw new RuntimeException("Failed to retrieve transactions", e);
        }
    }

    /**
     * Get transactions by user ID
     */
    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.transaction_id, t.order_id, t.amount, t.type, t.payment_status, t.created_at " +
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
                    transaction.setPaymentStatus(rs.getString("payment_status"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(transaction);
                }
            }

            logger.debug("Retrieved {} transactions for user: {}", transactions.size(), userId);
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting transactions for user: {}", userId, e);
            throw new RuntimeException("Failed to retrieve transactions", e);
        }
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT transaction_id, order_id, amount, type, payment_status, created_at " +
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
                    transaction.setPaymentStatus(rs.getString("payment_status"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    return transaction;
                }
            }

            return null;

        } catch (SQLException e) {
            logger.error("Error getting transaction: {}", transactionId, e);
            throw new RuntimeException("Failed to retrieve transaction", e);
        }
    }

    /**
     * Calculate total paid amount for an order (only PAID transactions)
     */
    public double getTotalPaidAmount(int orderId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total_paid " +
                "FROM Transactions " +
                "WHERE order_id = ? AND payment_status = 'PAID'";

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

    /**
     * Get transactions by type (FULL, DEPOSIT, SHOWROOM)
     */
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, order_id, amount, type, payment_status, created_at " +
                "FROM Transactions WHERE type = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transaction_id"));
                    transaction.setOrderId(rs.getInt("order_id"));
                    transaction.setAmount(rs.getDouble("amount"));
                    transaction.setType(rs.getString("type"));
                    transaction.setPaymentStatus(rs.getString("payment_status"));
                    transaction.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(transaction);
                }
            }

            logger.debug("Retrieved {} transactions of type: {}", transactions.size(), type);
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting transactions by type: {}", type, e);
            throw new RuntimeException("Failed to retrieve transactions by type", e);
        }
    }

    /**
     * Get pending showroom payments
     */
    public List<Transaction> getPendingShowroomPayments() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.transaction_id, t.order_id, t.amount, t.type, t.payment_status, t.created_at " +
                "FROM Transactions t " +
                "WHERE t.type = 'SHOWROOM' AND t.payment_status = 'PENDING' " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setOrderId(rs.getInt("order_id"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setType(rs.getString("type"));
                transaction.setPaymentStatus(rs.getString("payment_status"));
                transaction.setCreatedAt(rs.getTimestamp("created_at"));
                transactions.add(transaction);
            }

            logger.debug("Retrieved {} pending showroom payments", transactions.size());
            return transactions;

        } catch (SQLException e) {
            logger.error("Error getting pending showroom payments", e);
            throw new RuntimeException("Failed to retrieve pending showroom payments", e);
        }
    }
    // ... phần import & class TransactionDAO giữ nguyên

    /** Struct tham số query cho list giao dịch */
    public static class TxnQuery {
        public String status;            // PENDING | PAID | CANCELLED
        public String type;              // FULL | DEPOSIT | SHOWROOM
        public String keyword;           // tìm theo transaction_id hoặc order_id
        public Date from;                // created_at >= from (DATE)
        public Date to;                  // created_at <= to (DATE)
        public String sort;              // "t.created_at DESC" | "t.amount DESC" ...
        public int page = 1;             // 1-based
        public int size = 10;            // page size
    }

    /** Tìm kiếm + phân trang danh sách giao dịch */
    public List<Transaction> find(TxnQuery q) {
        // Nếu bạn có bảng Orders/Users muốn join để lấy thêm tên KH, có thể mở comment phần JOIN
        StringBuilder sql = new StringBuilder(
                "SELECT t.transaction_id, t.order_id, t.amount, t.type, t.payment_status, t.created_at " +
                        // ", o.order_code, u.full_name " +     // <-- mở nếu có cột này
                        "FROM Transactions t "
                        // + "JOIN Orders o ON o.order_id = t.order_id "
                        // + "LEFT JOIN Users u ON u.user_id = o.user_id "
                        + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (q.status != null && !q.status.isBlank()) { sql.append(" AND t.payment_status = ?"); params.add(q.status); }
        if (q.type != null && !q.type.isBlank())     { sql.append(" AND t.type = ?");           params.add(q.type); }
        if (q.from != null)                          { sql.append(" AND CAST(t.created_at AS date) >= ?"); params.add(q.from); }
        if (q.to != null)                            { sql.append(" AND CAST(t.created_at AS date) <= ?"); params.add(q.to); }
        if (q.keyword != null && !q.keyword.isBlank()){
            sql.append(" AND (CAST(t.transaction_id AS VARCHAR(20)) LIKE ? OR CAST(t.order_id AS VARCHAR(20)) LIKE ?)");
            String kw = "%" + q.keyword.trim() + "%";
            params.add(kw); params.add(kw);
        }

        String sort = (q.sort == null || q.sort.isBlank()) ? "t.created_at DESC" : q.sort;
        sql.append(" ORDER BY ").append(sort);

        int size = Math.max(q.size, 1);
        int offset = (Math.max(q.page, 1) - 1) * size;
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset); params.add(size);

        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) st.setObject(i + 1, params.get(i));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setTransactionId(rs.getInt("transaction_id"));
                    t.setOrderId(rs.getInt("order_id"));
                    t.setAmount(rs.getDouble("amount"));
                    t.setType(rs.getString("type"));
                    t.setPaymentStatus(rs.getString("payment_status"));
                    t.setCreatedAt(rs.getTimestamp("created_at"));
                    // Nếu có join, thêm view-only fields:
                    // t.setOrderCode(rs.getString("order_code"));
                    // t.setCustomerName(rs.getString("full_name"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            logger.error("Error find transactions", e);
            throw new RuntimeException("Failed to query transactions", e);
        }
        return list;
    }

    /** Đếm tổng bản ghi cho trang */
    public int count(TxnQuery q) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Transactions t WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (q.status != null && !q.status.isBlank()) { sql.append(" AND t.payment_status = ?"); params.add(q.status); }
        if (q.type != null && !q.type.isBlank())     { sql.append(" AND t.type = ?");           params.add(q.type); }
        if (q.from != null)                          { sql.append(" AND CAST(t.created_at AS date) >= ?"); params.add(q.from); }
        if (q.to != null)                            { sql.append(" AND CAST(t.created_at AS date) <= ?"); params.add(q.to); }
        if (q.keyword != null && !q.keyword.isBlank()){
            sql.append(" AND (CAST(t.transaction_id AS VARCHAR(20)) LIKE ? OR CAST(t.order_id AS VARCHAR(20)) LIKE ?)");
            String kw = "%" + q.keyword.trim() + "%";
            params.add(kw); params.add(kw);
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) st.setObject(i + 1, params.get(i));
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error count transactions", e);
        }
        return 0;
    }

}