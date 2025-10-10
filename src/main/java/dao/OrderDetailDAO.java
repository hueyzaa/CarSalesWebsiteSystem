package dao;

import model.Car;
import model.OrderDetail;
import util.DBContext;
import exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailDAO.class);

    /**
     * Get order details by order ID
     */
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT od.order_detail_id, od.order_id, od.car_id, od.price, od.quantity, " +
                "c.model, c.year, c.color, b.brand_name, ci.image_url " +
                "FROM OrderDetail od " +
                "JOIN Car c ON od.car_id = c.car_id " +
                "JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE od.order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderDetailId(rs.getInt("order_detail_id"));
                    detail.setOrderId(rs.getInt("order_id"));
                    detail.setCarId(rs.getInt("car_id"));
                    detail.setPrice(rs.getDouble("price"));
                    detail.setQuantity(rs.getInt("quantity"));

                    // Create Car object with basic info
                    Car car = new Car();
                    car.setId(rs.getInt("car_id"));
                    car.setName(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setBrandName(rs.getString("brand_name"));
                    car.setImageUrl(rs.getString("image_url"));

                    detail.setCar(car);
                    details.add(detail);
                }
            }

            logger.debug("Retrieved {} order details for order: {}", details.size(), orderId);
            return details;

        } catch (SQLException e) {
            logger.error("Error getting order details for order: {}", orderId, e);
            throw new DatabaseException("Failed to retrieve order details", e);
        }
    }

    /**
     * Get single order detail by ID
     */
    public OrderDetail getOrderDetailById(int orderDetailId) {
        String sql = "SELECT od.order_detail_id, od.order_id, od.car_id, od.price, od.quantity, " +
                "c.model, c.year, c.color, b.brand_name, ci.image_url " +
                "FROM OrderDetail od " +
                "JOIN Car c ON od.car_id = c.car_id " +
                "JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE od.order_detail_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderDetailId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderDetailId(rs.getInt("order_detail_id"));
                    detail.setOrderId(rs.getInt("order_id"));
                    detail.setCarId(rs.getInt("car_id"));
                    detail.setPrice(rs.getDouble("price"));
                    detail.setQuantity(rs.getInt("quantity"));

                    Car car = new Car();
                    car.setId(rs.getInt("car_id"));
                    car.setName(rs.getString("model"));
                    car.setYear(rs.getInt("year"));
                    car.setColor(rs.getString("color"));
                    car.setBrandName(rs.getString("brand_name"));
                    car.setImageUrl(rs.getString("image_url"));

                    detail.setCar(car);
                    return detail;
                }
            }

            return null;

        } catch (SQLException e) {
            logger.error("Error getting order detail: {}", orderDetailId, e);
            throw new DatabaseException("Failed to retrieve order detail", e);
        }
    }

    /**
     * Calculate total amount for an order
     */
    public double calculateOrderTotal(int orderId) {
        String sql = "SELECT SUM(price * quantity) as total FROM OrderDetail WHERE order_id = ?";

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
            logger.error("Error calculating order total for order: {}", orderId, e);
            return 0.0;
        }
    }

    /**
     * Get total items count in an order
     */
    public int getOrderItemCount(int orderId) {
        String sql = "SELECT SUM(quantity) as total_items FROM OrderDetail WHERE order_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_items");
                }
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Error getting order item count for order: {}", orderId, e);
            return 0;
        }
    }
}