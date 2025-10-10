package dao;

import model.Order;
import model.CartItem;
import util.DBContext;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrdersDAO.class);

    /**
     * Get orders by user ID
     */
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = extractOrderFromResultSet(rs);
                    orders.add(order);
                }
            }

            logger.debug("Retrieved {} orders for userId: {}", orders.size(), userId);
            return orders;

        } catch (SQLException e) {
            logger.error("Error getting orders for userId: {}", userId, e);
            throw new DatabaseException("Failed to retrieve orders", e);
        }
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = extractOrderFromResultSet(rs);
                    logger.debug("Retrieved order: {}", orderId);
                    return order;
                }
            }

            logger.debug("Order not found: {}", orderId);
            return null;

        } catch (SQLException e) {
            logger.error("Error getting order: {}", orderId, e);
            throw new DatabaseException("Failed to retrieve order", e);
        }
    }

    /**
     * Create order from cart items with payment information
     */
    public int createOrder(int userId, List<CartItem> cartItems, String paymentType,
                           Double depositAmount, String notes) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart items cannot be null or empty");
        }

        // Calculate total and remaining amount
        double totalAmount = cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        Double remainingAmount = null;
        if ("DEPOSIT".equals(paymentType) && depositAmount != null) {
            remainingAmount = totalAmount - depositAmount;
        } else if ("SHOWROOM".equals(paymentType)) {
            remainingAmount = totalAmount;
        }

        String sqlOrder = "INSERT INTO Orders (user_id, status, payment_type, deposit_amount, remaining_amount, notes) " +
                "VALUES (?, 'PENDING', ?, ?, ?, ?)";
        String sqlOrderDetail = "INSERT INTO OrderDetail (order_id, car_id, price, quantity) " +
                "VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            // Create order
            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setString(2, paymentType);

                if (depositAmount != null) {
                    stmt.setDouble(3, depositAmount);
                } else {
                    stmt.setNull(3, Types.DECIMAL);
                }

                if (remainingAmount != null) {
                    stmt.setDouble(4, remainingAmount);
                } else {
                    stmt.setNull(4, Types.DECIMAL);
                }

                stmt.setString(5, notes);

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated order ID");
                }
            }

            // Add order details (batch)
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrderDetail)) {
                for (CartItem item : cartItems) {
                    if (item.getCar() == null) {
                        throw new IllegalStateException("CartItem must have Car object populated");
                    }

                    stmt.setInt(1, orderId);
                    stmt.setInt(2, item.getCarId());
                    stmt.setDouble(3, item.getCar().getPrice());
                    stmt.setInt(4, item.getQuantity());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            logger.info("Created order {} for userId: {} with {} items - Payment Type: {}",
                    orderId, userId, cartItems.size(), paymentType);
            return orderId;

        } catch (SQLException e) {
            rollback(conn);
            logger.error("Error creating order for userId: {}", userId, e);
            throw new DatabaseException("Failed to create order", e);

        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Create order from cart items (backward compatibility - default to FULL payment)
     */
    public int createOrder(int userId, List<CartItem> cartItems) {
        return createOrder(userId, cartItems, "FULL", null, null);
    }

    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated order {} status to {}", orderId, status);
            } else {
                logger.warn("No order updated with ID: {}", orderId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating order status for orderId: {}", orderId, e);
            throw new DatabaseException("Failed to update order status", e);
        }
    }

    /**
     * Update order payment information
     */
    public boolean updateOrderPaymentInfo(int orderId, Double depositAmount, Double remainingAmount) {
        String sql = "UPDATE Orders SET deposit_amount = ?, remaining_amount = ? WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (depositAmount != null) {
                stmt.setDouble(1, depositAmount);
            } else {
                stmt.setNull(1, Types.DECIMAL);
            }

            if (remainingAmount != null) {
                stmt.setDouble(2, remainingAmount);
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }

            stmt.setInt(3, orderId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated order {} payment info", orderId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating order payment info for orderId: {}", orderId, e);
            throw new DatabaseException("Failed to update order payment info", e);
        }
    }

    /**
     * Update order notes
     */
    public boolean updateOrderNotes(int orderId, String notes) {
        String sql = "UPDATE Orders SET notes = ? WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, notes);
            stmt.setInt(2, orderId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error updating order notes for orderId: {}", orderId, e);
            throw new DatabaseException("Failed to update order notes", e);
        }
    }

    /**
     * Cancel order
     */
    public boolean cancelOrder(int orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }

    /**
     * Complete order
     */
    public boolean completeOrder(int orderId) {
        return updateOrderStatus(orderId, "COMPLETED");
    }

    /**
     * Approve order
     */
    public boolean approveOrder(int orderId) {
        return updateOrderStatus(orderId, "APPROVED");
    }

    /**
     * Get all orders (admin)
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                orders.add(order);
            }

            logger.debug("Retrieved {} total orders", orders.size());
            return orders;

        } catch (SQLException e) {
            logger.error("Error getting all orders", e);
            throw new DatabaseException("Failed to retrieve all orders", e);
        }
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders WHERE status = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = extractOrderFromResultSet(rs);
                    orders.add(order);
                }
            }

            logger.debug("Retrieved {} orders with status: {}", orders.size(), status);
            return orders;

        } catch (SQLException e) {
            logger.error("Error getting orders by status: {}", status, e);
            throw new DatabaseException("Failed to retrieve orders by status", e);
        }
    }

    /**
     * Get orders by payment type
     */
    public List<Order> getOrdersByPaymentType(String paymentType) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders WHERE payment_type = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paymentType);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = extractOrderFromResultSet(rs);
                    orders.add(order);
                }
            }

            logger.debug("Retrieved {} orders with payment type: {}", orders.size(), paymentType);
            return orders;

        } catch (SQLException e) {
            logger.error("Error getting orders by payment type: {}", paymentType, e);
            throw new DatabaseException("Failed to retrieve orders by payment type", e);
        }
    }

    /**
     * Get pending showroom payment orders
     */
    public List<Order> getPendingShowroomOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at, payment_type, " +
                "deposit_amount, remaining_amount, notes " +
                "FROM Orders WHERE payment_type = 'SHOWROOM' AND status = 'PENDING' " +
                "ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                orders.add(order);
            }

            logger.debug("Retrieved {} pending showroom orders", orders.size());
            return orders;

        } catch (SQLException e) {
            logger.error("Error getting pending showroom orders", e);
            throw new DatabaseException("Failed to retrieve pending showroom orders", e);
        }
    }

    /**
     * Calculate order total
     */
    public double getOrderTotal(int orderId) {
        String sql = "SELECT SUM(price * quantity) as total FROM OrderDetail " +
                "WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }

            return 0.0;

        } catch (SQLException e) {
            logger.error("Error calculating order total for orderId: {}", orderId, e);
            return 0.0;
        }
    }

    /**
     * Get order statistics
     */
    public OrderStats getOrderStats() {
        String sql = "SELECT " +
                "COUNT(*) as total_orders, " +
                "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
                "SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pending, " +
                "SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled, " +
                "SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) as approved " +
                "FROM Orders";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                OrderStats stats = new OrderStats();
                stats.setTotalOrders(rs.getInt("total_orders"));
                stats.setCompletedOrders(rs.getInt("completed"));
                stats.setPendingOrders(rs.getInt("pending"));
                stats.setCancelledOrders(rs.getInt("cancelled"));
                stats.setApprovedOrders(rs.getInt("approved"));

                logger.debug("Retrieved order statistics");
                return stats;
            }

        } catch (SQLException e) {
            logger.error("Error getting order statistics", e);
        }

        return new OrderStats();
    }

    /**
     * Extract Order object from ResultSet
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setPaymentType(rs.getString("payment_type"));

        double depositAmount = rs.getDouble("deposit_amount");
        if (!rs.wasNull()) {
            order.setDepositAmount(depositAmount);
        }

        double remainingAmount = rs.getDouble("remaining_amount");
        if (!rs.wasNull()) {
            order.setRemainingAmount(remainingAmount);
        }

        order.setNotes(rs.getString("notes"));

        return order;
    }

    /**
     * Rollback transaction
     */
    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                logger.debug("Transaction rolled back");
            } catch (SQLException e) {
                logger.error("Failed to rollback transaction", e);
            }
        }
    }

    /**
     * Close connection
     */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection", e);
            }
        }
    }

    /**
     * Inner class for order statistics
     */
    public static class OrderStats {
        private int totalOrders;
        private int completedOrders;
        private int pendingOrders;
        private int cancelledOrders;
        private int approvedOrders;

        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int total) { this.totalOrders = total; }

        public int getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(int completed) { this.completedOrders = completed; }

        public int getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(int pending) { this.pendingOrders = pending; }

        public int getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(int cancelled) { this.cancelledOrders = cancelled; }

        public int getApprovedOrders() { return approvedOrders; }
        public void setApprovedOrders(int approved) { this.approvedOrders = approved; }

        @Override
        public String toString() {
            return "OrderStats{" +
                    "total=" + totalOrders +
                    ", completed=" + completedOrders +
                    ", pending=" + pendingOrders +
                    ", approved=" + approvedOrders +
                    ", cancelled=" + cancelledOrders +
                    '}';
        }
    }
}