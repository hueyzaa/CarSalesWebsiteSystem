package dao;

import model.Order;
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
        String sql = "SELECT order_id, user_id, status, created_at FROM Orders " +
                "WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));
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
        String sql = "SELECT order_id, user_id, status, created_at FROM Orders " +
                "WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at"));

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
     * Create order from cart items (with transaction)
     */
    public int createOrder(int userId, List<model.CartItem> cartItems) {
        String sqlOrder = "INSERT INTO Orders (user_id, status) VALUES (?, 'PENDING')";
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
                for (model.CartItem item : cartItems) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, item.getCarId());
                    stmt.setBigDecimal(3, item.getCarPrice());
                    stmt.setInt(4, item.getQuantity());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            logger.info("Created order {} for userId: {} with {} items",
                    orderId, userId, cartItems.size());
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
     * Get all orders (admin)
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, user_id, status, created_at FROM Orders " +
                "ORDER BY created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
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
     * Get order statistics
     */
    public OrderStats getOrderStats() {
        String sql = "SELECT " +
                "COUNT(*) as total_orders, " +
                "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
                "SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pending, " +
                "SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled " +
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

                logger.debug("Retrieved order statistics");
                return stats;
            }

        } catch (SQLException e) {
            logger.error("Error getting order statistics", e);
        }

        return new OrderStats();
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

        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int total) { this.totalOrders = total; }

        public int getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(int completed) { this.completedOrders = completed; }

        public int getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(int pending) { this.pendingOrders = pending; }

        public int getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(int cancelled) { this.cancelledOrders = cancelled; }
    }
}