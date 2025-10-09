package dao;

import model.CartItem;
import util.DBContext;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);

    /**
     * Add item to cart (with transaction)
     */
    public boolean addToCart(int userId, int carId, int quantity) {
        String sqlCart = "INSERT INTO Cart (user_id) VALUES (?)";
        String sqlCartItem = "INSERT INTO CartItem (cart_id, car_id, quantity) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            // Get or create cart
            int cartId = getCartIdByUserId(conn, userId);
            if (cartId == -1) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlCart, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        cartId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Add or update cart item
            if (cartItemExists(conn, cartId, carId)) {
                updateCartItemQuantity(conn, cartId, carId, quantity);
            } else {
                try (PreparedStatement stmt = conn.prepareStatement(sqlCartItem)) {
                    stmt.setInt(1, cartId);
                    stmt.setInt(2, carId);
                    stmt.setInt(3, quantity);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            logger.info("Added to cart - userId: {}, carId: {}, quantity: {}", userId, carId, quantity);
            return true;

        } catch (SQLException e) {
            rollback(conn);
            logger.error("Error adding to cart for userId: {}, carId: {}", userId, carId, e);
            throw new DatabaseException("Failed to add item to cart", e);

        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Get cart items by user ID
     */
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.cart_item_id, ci.car_id, ci.quantity, c.model, c.price, " +
                "ci_img.image_url, b.brand_name " +
                "FROM CartItem ci " +
                "JOIN Cart ct ON ci.cart_id = ct.cart_id " +
                "JOIN Car c ON ci.car_id = c.car_id " +
                "JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci_img ON c.car_id = ci_img.car_id AND ci_img.is_primary = 1 " +
                "WHERE ct.user_id = ? " +
                "ORDER BY ci.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartItemId(rs.getInt("cart_item_id"));
                    item.setCarId(rs.getInt("car_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setCarModel(rs.getString("model"));
                    item.setCarPrice(rs.getBigDecimal("price"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setBrandName(rs.getString("brand_name"));
                    cartItems.add(item);
                }
            }

            logger.debug("Retrieved {} cart items for userId: {}", cartItems.size(), userId);
            return cartItems;

        } catch (SQLException e) {
            logger.error("Error getting cart items for userId: {}", userId, e);
            throw new DatabaseException("Failed to retrieve cart items", e);
        }
    }

    /**
     * Remove item from cart
     */
    public boolean removeFromCart(int cartItemId) {
        String sql = "DELETE FROM CartItem WHERE cart_item_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Removed cart item: {}", cartItemId);
            } else {
                logger.warn("No cart item removed for ID: {}", cartItemId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error removing from cart: {}", cartItemId, e);
            throw new DatabaseException("Failed to remove item from cart", e);
        }
    }

    /**
     * Update cart item quantity
     */
    public boolean updateQuantity(int cartItemId, int quantity) {
        String sql = "UPDATE CartItem SET quantity = ? WHERE cart_item_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, cartItemId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated cart item {} quantity to {}", cartItemId, quantity);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating cart item quantity: {}", cartItemId, e);
            throw new DatabaseException("Failed to update cart item quantity", e);
        }
    }

    /**
     * Clear all items from user's cart
     */
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM CartItem WHERE cart_id IN " +
                "(SELECT cart_id FROM Cart WHERE user_id = ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Cleared cart for userId: {}", userId);
            }

            return true; // Return true even if no items (idempotent)

        } catch (SQLException e) {
            logger.error("Error clearing cart for userId: {}", userId, e);
            throw new DatabaseException("Failed to clear cart", e);
        }
    }

    /**
     * Get cart ID by user ID
     */
    private int getCartIdByUserId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT cart_id FROM Cart WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }
        }
        return -1;
    }

    /**
     * Check if cart item exists
     */
    private boolean cartItemExists(Connection conn, int cartId, int carId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CartItem WHERE cart_id = ? AND car_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Update quantity if item already exists
     */
    private void updateCartItemQuantity(Connection conn, int cartId, int carId,
                                        int quantityToAdd) throws SQLException {
        String sql = "UPDATE CartItem SET quantity = quantity + ? " +
                "WHERE cart_id = ? AND car_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityToAdd);
            stmt.setInt(2, cartId);
            stmt.setInt(3, carId);
            stmt.executeUpdate();
        }
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
}