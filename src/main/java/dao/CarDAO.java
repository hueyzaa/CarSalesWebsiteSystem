package dao;

import dto.CarWithDiscountDTO;
import model.Car;
import model.CarImage;
import util.DBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarDAO {
    private static final Logger logger = LoggerFactory.getLogger(CarDAO.class);

    /**
     * Get all cars with primary image and images list
     */
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "ORDER BY c.car_id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Car car = extractCarFromResultSet(rs);
                car.setImages(getCarImageObjects(car.getId()));
                cars.add(car);
            }

            logger.debug("Retrieved {} cars", cars.size());
            return cars;

        } catch (SQLException e) {
            logger.error("Error getting all cars", e);
            throw new RuntimeException("Failed to retrieve cars", e);
        }
    }

    /**
     * Get available cars only with images
     */
    public List<Car> getAvailableCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE c.status = 'AVAILABLE' " +
                "ORDER BY c.car_id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Car car = extractCarFromResultSet(rs);
                car.setImages(getCarImageObjects(car.getId()));
                cars.add(car);
            }

            logger.debug("Retrieved {} available cars", cars.size());
            return cars;

        } catch (SQLException e) {
            logger.error("Error getting available cars", e);
            throw new RuntimeException("Failed to retrieve available cars", e);
        }
    }

    /**
     * Get car by ID with images
     */
    public Car getCarById(int carId) {
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE c.car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    car.setImages(getCarImageObjects(carId));
                    logger.debug("Retrieved car with ID: {}", carId);
                    return car;
                }
            }

            logger.debug("Car not found with ID: {}", carId);
            return null;

        } catch (SQLException e) {
            logger.error("Error getting car by ID: {}", carId, e);
            throw new RuntimeException("Failed to retrieve car", e);
        }
    }

    public List<CarImage> getImagesByCarId(int carId) {
        List<CarImage> list = new ArrayList<>();
        String sql = "SELECT image_id, car_id, image_url FROM CarImages WHERE car_id = ? ORDER BY image_id";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarImage img = new CarImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setCarId(rs.getInt("car_id"));
                    img.setImageURL(rs.getString("image_url")); // khớp getter JSP
                    list.add(img);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * Search cars by keyword
     */
    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        keyword = keyword.trim().replaceAll("[';\"\\\\%_]", "");

        if (keyword.isEmpty() || keyword.length() > 100) {
            return Collections.emptyList();
        }

        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE c.model LIKE ? OR b.brand_name LIKE ? " +
                "ORDER BY c.car_id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    car.setImages(getCarImageObjects(car.getId()));
                    cars.add(car);
                }
            }

            logger.debug("Search for '{}' returned {} results", keyword, cars.size());
            return cars;

        } catch (SQLException e) {
            logger.error("Error searching cars with keyword: {}", keyword, e);
            throw new RuntimeException("Failed to search cars", e);
        }
    }

    /**
     * Get cars by brand
     */
    public List<Car> getCarsByBrand(int brandId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE c.brand_id = ? " +
                "ORDER BY c.car_id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, brandId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    car.setImages(getCarImageObjects(car.getId()));
                    cars.add(car);
                }
            }

            logger.debug("Retrieved {} cars for brand ID: {}", cars.size(), brandId);
            return cars;

        } catch (SQLException e) {
            logger.error("Error getting cars by brand: {}", brandId, e);
            throw new RuntimeException("Failed to retrieve cars by brand", e);
        }
    }

    /**
     * Get cars by price range
     */
    public List<Car> getCarsByPriceRange(double minPrice, double maxPrice) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, b.brand_name, c.model, c.price, " +
                "c.status, c.description, c.year, c.color, c.stock, ci.image_url " +
                "FROM Car c " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1 " +
                "WHERE c.price BETWEEN ? AND ? AND c.status = 'AVAILABLE' " +
                "ORDER BY c.price ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    car.setImages(getCarImageObjects(car.getId()));
                    cars.add(car);
                }
            }

            logger.debug("Retrieved {} cars in price range {}-{}", cars.size(), minPrice, maxPrice);
            return cars;

        } catch (SQLException e) {
            logger.error("Error getting cars by price range: {}-{}", minPrice, maxPrice, e);
            throw new RuntimeException("Failed to retrieve cars by price range", e);
        }
    }

    /**
     * Add a new car
     */
    public int addCar(Car car) {
        String sql = "INSERT INTO Car (brand_id, model, price, status, description, year, color, stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, car.getBrandId());
            stmt.setString(2, car.getName());
            stmt.setDouble(3, car.getPrice());
            stmt.setString(4, car.getStatus());
            stmt.setString(5, car.getDescription());
            stmt.setInt(6, car.getYear());
            stmt.setString(7, car.getColor());
            stmt.setInt(8, car.getStock());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int carId = rs.getInt(1);
                        logger.info("Added new car with ID: {}", carId);
                        return carId;
                    }
                }
            }

            logger.warn("Failed to add car, no rows affected");
            return -1;

        } catch (SQLException e) {
            logger.error("Error adding car: {}", car, e);
            throw new RuntimeException("Failed to add car", e);
        }
    }

    /**
     * Get all images for a car as string list
     */
    public List<String> getCarImages(int carId) {
        List<String> images = new ArrayList<>();
        String sql = "SELECT image_url FROM CarImage WHERE car_id = ? " +
                "ORDER BY is_primary DESC, created_at ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    images.add(rs.getString("image_url"));
                }
            }

            logger.debug("Retrieved {} images for car ID: {}", images.size(), carId);
            return images;

        } catch (SQLException e) {
            logger.error("Error getting car images for carId: {}", carId, e);
            throw new RuntimeException("Failed to retrieve car images", e);
        }
    }

    /**
     * Get all images for a car as CarImage objects
     */
    public List<CarImage> getCarImageObjects(int carId) {
        List<CarImage> images = new ArrayList<>();
        String sql = "SELECT image_id, car_id, image_url, is_primary, created_at " +
                "FROM CarImage WHERE car_id = ? " +
                "ORDER BY is_primary DESC, created_at ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CarImage img = new CarImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setCarId(rs.getInt("car_id"));
                    img.setImageURL(rs.getString("image_url"));
                    img.setMainImage(rs.getBoolean("is_primary"));
                    img.setCreatedAt(rs.getTimestamp("created_at"));
                    images.add(img);
                }
            }

            return images;

        } catch (SQLException e) {
            logger.error("Error getting car image objects for carId: {}", carId, e);
            return new ArrayList<>();
        }
    }

    // dao/CarDAO.java
    public List<CarWithDiscountDTO> getCarsWithDiscountByPromotionId(int promotionId) {
        String sql =
                "SELECT c.car_id, c.model, c.year, c.color, c.price, c.status, c.stock, " +
                        "       b.brand_name, " +
                        "       cp.discount_percentage, cp.discount_amount " +
                        "FROM CarPromotion cp " +
                        "JOIN Car c        ON c.car_id = cp.car_id " +
                        "LEFT JOIN Brand b ON b.brand_id = c.brand_id " +
                        "WHERE cp.promotion_id = ?";

        List<CarWithDiscountDTO> list = new ArrayList<>();

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, promotionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarWithDiscountDTO dto = new CarWithDiscountDTO();

                    int carId = rs.getInt("car_id");
                    String brandName = rs.getString("brand_name");
                    String model = rs.getString("model");
                    int year = rs.getObject("year") != null ? rs.getInt("year") : 0;
                    String color = rs.getString("color");
                    double price = rs.getDouble("price");
                    String status = rs.getString("status");
                    int quantity = rs.getObject("stock") != null ? rs.getInt("stock") : 0;

                    // Lấy discount từ bảng CarPromotion
                    double percent = rs.getDouble("discount_percentage");
                    boolean hasPercent = !rs.wasNull();
                    double amount = rs.getDouble("discount_amount");
                    boolean hasAmount = !rs.wasNull();

                    // Tính toán giá trị giảm
                    double discountValue = 0;
                    if (hasPercent && percent > 0) {
                        discountValue = price * percent / 100.0;
                    } else if (hasAmount && amount > 0) {
                        discountValue = amount;
                    }
                    double discountedPrice = Math.max(0, price - discountValue);

                    // Gán vào DTO theo đúng getter/setter bạn có
                    dto.setCarId(carId);
                    dto.setBrandName(brandName);
                    dto.setName(model);                 // JSP đang dùng ${car.name}
                    dto.setYear(year);
                    dto.setColor(color);
                    dto.setPrice(price);
                    dto.setStatus(status);
                    dto.setQuantity(quantity);

                    dto.setHasDiscount(discountValue > 0);
                    dto.setDiscountPercentage(hasPercent ? percent : 0);
                    dto.setDiscountAmount(hasAmount ? amount : 0);
                    dto.setDiscountValue(discountValue);
                    dto.setDiscountedPrice(discountedPrice);

                    // Nếu có cột/ảnh chính thì setImageUrl(...) ở đây

                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load cars for promotion " + promotionId, e);
        }
        return list;
    }



    /**
     * Add car images
     */
    public boolean addCarImages(int carId, List<String> imageUrls, int primaryIndex) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return true;
        }

        String sql = "INSERT INTO CarImage (car_id, image_url, is_primary) VALUES (?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < imageUrls.size(); i++) {
                    String url = imageUrls.get(i);
                    if (url != null && !url.trim().isEmpty()) {
                        stmt.setInt(1, carId);
                        stmt.setString(2, url.trim());
                        stmt.setBoolean(3, i == primaryIndex);
                        stmt.addBatch();
                    }
                }

                stmt.executeBatch();
                conn.commit();

                logger.info("Added {} images for car ID: {}", imageUrls.size(), carId);
                return true;
            }

        } catch (SQLException e) {
            rollback(conn);
            logger.error("Error adding car images for carId: {}", carId, e);
            throw new RuntimeException("Failed to add car images", e);

        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Update car information
     */
    public boolean updateCar(Car car) {
        String sql = "UPDATE Car SET brand_id = ?, model = ?, price = ?, " +
                "status = ?, description = ?, year = ?, color = ?, stock = ? WHERE car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, car.getBrandId());
            stmt.setString(2, car.getName());
            stmt.setDouble(3, car.getPrice());
            stmt.setString(4, car.getStatus());
            stmt.setString(5, car.getDescription());
            stmt.setInt(6, car.getYear());
            stmt.setString(7, car.getColor());
            stmt.setInt(8, car.getStock());
            stmt.setInt(9, car.getId());

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated car with ID: {}", car.getId());
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating car: {}", car, e);
            throw new RuntimeException("Failed to update car", e);
        }
    }

    /**
     * Delete car
     */
    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM Car WHERE car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Deleted car with ID: {}", carId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error deleting car with ID: {}", carId, e);
            throw new RuntimeException("Failed to delete car", e);
        }
    }

    /**
     * Update car status
     */
    public boolean updateCarStatus(int carId, String status) {
        String sql = "UPDATE Car SET status = ? WHERE car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, carId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated status for car ID {} to {}", carId, status);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating car status for ID: {}", carId, e);
            throw new RuntimeException("Failed to update car status", e);
        }
    }

    /**
     * Update car stock
     */
    public boolean updateCarStock(int carId, int stock) {
        String sql = "UPDATE Car SET stock = ? WHERE car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, stock);
            stmt.setInt(2, carId);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Updated stock for car ID {} to {}", carId, stock);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error updating car stock for ID: {}", carId, e);
            throw new RuntimeException("Failed to update car stock", e);
        }
    }
    /**
     * Increase car stock (for cancelled orders)
     */
    public boolean increaseStock(int carId, int quantity) {
        String sql = "UPDATE Car SET stock = stock + ? WHERE car_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, carId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Increased stock for car ID {} by {} units", carId, quantity);
                return true;
            } else {
                logger.warn("No car found with ID: {}", carId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error increasing stock for car ID: {}", carId, e);
            throw new RuntimeException("Failed to increase car stock", e);
        }
    }

    /**
     * Decrease car stock (for orders)
     */
    public boolean decreaseStock(int carId, int quantity) {
        String sql = "UPDATE Car SET stock = stock - ? WHERE car_id = ? AND stock >= ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, carId);
            stmt.setInt(3, quantity);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                logger.info("Decreased stock for car ID {} by {}", carId, quantity);
            } else {
                logger.warn("Failed to decrease stock for car ID {} - insufficient stock", carId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error decreasing stock for car ID: {}", carId, e);
            throw new RuntimeException("Failed to decrease car stock", e);
        }
    }

    /**
     * Extract Car object from ResultSet
     */
    private Car extractCarFromResultSet(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt("car_id"));
        car.setBrandId(rs.getInt("brand_id"));
        car.setBrandName(rs.getString("brand_name"));
        car.setName(rs.getString("model"));
        car.setPrice(rs.getDouble("price"));
        car.setStatus(rs.getString("status"));
        car.setDescription(rs.getString("description"));
        car.setYear(rs.getInt("year"));
        car.setColor(rs.getString("color"));
        car.setStock(rs.getInt("stock"));
        car.setImageUrl(rs.getString("image_url"));
        return car;
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.error("Failed to rollback transaction", e);
            }
        }
    }

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