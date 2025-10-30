package dao;

import model.Promotion;
import model.Car;
import util.DBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    private static final Logger logger = LoggerFactory.getLogger(PromotionDAO.class);

    /**
     * Get all promotions from database
     */
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT promotion_id, title, description, start_date, end_date, " +
                "discount_percentage, discount_amount " +
                "FROM Promotion " +
                "ORDER BY start_date DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = mapResultSetToPromotion(rs);
                promotions.add(promotion);
            }

            logger.info("Retrieved {} promotions from database", promotions.size());
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving all promotions", e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi", e);
        }
    }

    /**
     * Get only active promotions (current date between start_date and end_date)
     */
    public List<Promotion> getAllActivePromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT promotion_id, title, description, start_date, end_date, " +
                "discount_percentage, discount_amount " +
                "FROM Promotion " +
                "WHERE GETDATE() BETWEEN start_date AND end_date " +
                "ORDER BY start_date DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = mapResultSetToPromotion(rs);
                promotions.add(promotion);
            }

            logger.info("Retrieved {} active promotions from database", promotions.size());
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving active promotions", e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi đang hoạt động", e);
        }
    }

    /**
     * Get all active promotions with user claim status
     * @param userId User ID to check claim status, null for guest users
     */
    public List<Promotion> getAllActivePromotionsWithUserStatus(Integer userId) {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT p.promotion_id, p.title, p.description, p.start_date, p.end_date, " +
                "p.discount_percentage, p.discount_amount" +
                (userId != null ?
                        ", CASE WHEN up.user_promotion_id IS NOT NULL THEN 1 ELSE 0 END as is_claimed, " +
                                "CASE WHEN up.is_used = 1 THEN 1 ELSE 0 END as is_used " :
                        ", 0 as is_claimed, 0 as is_used ") +
                "FROM Promotion p " +
                (userId != null ?
                        "LEFT JOIN UserPromotion up ON p.promotion_id = up.promotion_id AND up.user_id = ? " : "") +
                "WHERE GETDATE() BETWEEN p.start_date AND p.end_date " +
                "ORDER BY p.start_date DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (userId != null) {
                stmt.setInt(1, userId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = mapResultSetToPromotion(rs);
                    promotion.setClaimedByUser(rs.getBoolean("is_claimed"));
                    promotion.setUsedByUser(rs.getBoolean("is_used"));

                    // Load applicable cars for this promotion
                    promotion.setApplicableCars(getCarsInPromotion(promotion.getPromotionId()));

                    promotions.add(promotion);
                }
            }

            logger.info("Retrieved {} active promotions with user status", promotions.size());
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving active promotions with user status", e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi", e);
        }
    }

    /**
     * Get promotion by ID
     */
    public Promotion getPromotionById(int promotionId) {
        String sql = "SELECT promotion_id, title, description, start_date, end_date, " +
                "discount_percentage, discount_amount " +
                "FROM Promotion " +
                "WHERE promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Promotion promotion = mapResultSetToPromotion(rs);
                    logger.info("Retrieved promotion: {}", promotionId);
                    return promotion;
                }
            }

            logger.warn("Promotion not found: {}", promotionId);
            return null;

        } catch (SQLException e) {
            logger.error("Error retrieving promotion by ID: {}", promotionId, e);
            throw new RuntimeException("Không thể lấy thông tin khuyến mãi", e);
        }
    }

    /**
     * Get promotions for a specific car
     */
    public List<Promotion> getPromotionsByCar(int carId){
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT p.promotion_id, p.title, p.description, p.start_date, p.end_date, " +
                "p.discount_percentage, p.discount_amount " +
                "FROM Promotion p " +
                "INNER JOIN CarPromotion cp ON p.promotion_id = cp.promotion_id " +
                "WHERE cp.car_id = ? " +
                "AND GETDATE() BETWEEN p.start_date AND p.end_date " +
                "ORDER BY p.start_date DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = mapResultSetToPromotion(rs);
                    promotions.add(promotion);
                }
            }

            logger.info("Retrieved {} promotions for car: {}", promotions.size(), carId);
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving promotions for car: {}", carId, e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi cho xe", e);
        }
    }

    /**
     * Create new promotion
     */
    public int createPromotion(Promotion promotion){
        String sql = "INSERT INTO Promotion (title, description, start_date, end_date, " +
                "discount_percentage, discount_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, promotion.getTitle());
            stmt.setString(2, promotion.getDescription());
            stmt.setDate(3, new java.sql.Date(promotion.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(promotion.getEndDate().getTime()));
            stmt.setDouble(5, promotion.getDiscountPercentage());
            stmt.setDouble(6, promotion.getDiscountAmount());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Tạo khuyến mãi thất bại, không có dòng nào bị ảnh hưởng");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int promotionId = generatedKeys.getInt(1);
                    logger.info("Created promotion with ID: {}", promotionId);
                    return promotionId;
                } else {
                    throw new RuntimeException("Tạo khuyến mãi thất bại, không lấy được ID");
                }
            }

        } catch (SQLException e) {
            logger.error("Error creating promotion", e);
            throw new RuntimeException("Không thể tạo khuyến mãi", e);
        }
    }

    /**
     * Update promotion
     */
    public boolean updatePromotion(Promotion promotion) {
        String sql = "UPDATE Promotion " +
                "SET title = ?, description = ?, start_date = ?, end_date = ?, " +
                "discount_percentage = ?, discount_amount = ? " +
                "WHERE promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, promotion.getTitle());
            stmt.setString(2, promotion.getDescription());
            stmt.setDate(3, new java.sql.Date(promotion.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(promotion.getEndDate().getTime()));
            stmt.setDouble(5, promotion.getDiscountPercentage());
            stmt.setDouble(6, promotion.getDiscountAmount());
            stmt.setInt(7, promotion.getPromotionId());

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Updated promotion {}: {}", promotion.getPromotionId(), success);
            return success;

        } catch (SQLException e) {
            logger.error("Error updating promotion: {}", promotion.getPromotionId(), e);
            throw new RuntimeException("Không thể cập nhật khuyến mãi", e);
        }
    }

    /**
     * Delete promotion
     */
    public boolean deletePromotion(int promotionId) {
        String sql = "DELETE FROM Promotion WHERE promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Deleted promotion {}: {}", promotionId, success);
            return success;

        } catch (SQLException e) {
            logger.error("Error deleting promotion: {}", promotionId, e);
            throw new RuntimeException("Không thể xóa khuyến mãi", e);
        }
    }

    /**
     * Add car to promotion with specific discount
     */
    public boolean addCarToPromotion(int carId, int promotionId, double discountPercentage, double discountAmount) {
        String sql = "INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            stmt.setInt(2, promotionId);
            stmt.setDouble(3, discountPercentage);
            stmt.setDouble(4, discountAmount);

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Added car {} to promotion {} with discount: {}%, {} amount: {}",
                    carId, promotionId, discountPercentage, discountAmount, success);
            return success;

        } catch (SQLException e) {
            logger.error("Error adding car {} to promotion {}", carId, promotionId, e);
            throw new RuntimeException("Không thể thêm xe vào khuyến mãi", e);
        }
    }

    /**
     * Add car to promotion (using default promotion discount)
     */
    public boolean addCarToPromotion(int carId, int promotionId) {
        return addCarToPromotion(carId, promotionId, 0, 0);
    }

    /**
     * Remove car from promotion
     */
    public boolean removeCarFromPromotion(int carId, int promotionId) {
        String sql = "DELETE FROM CarPromotion WHERE car_id = ? AND promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            stmt.setInt(2, promotionId);

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Removed car {} from promotion {}: {}", carId, promotionId, success);
            return success;

        } catch (SQLException e) {
            logger.error("Error removing car {} from promotion {}", carId, promotionId, e);
            throw new RuntimeException("Không thể xóa xe khỏi khuyến mãi", e);
        }
    }

    /**
     * Get all cars in a promotion (returns full Car objects with individual discounts)
     */
    public List<Car> getCarsInPromotion(int promotionId)  {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.brand_id, c.model, c.price, c.status, " +
                "c.description, c.year, c.color, c.stock, b.brand_name, " +
                "cp.discount_percentage, cp.discount_amount, " +
                "(SELECT TOP 1 image_url FROM CarImage WHERE car_id = c.car_id AND is_primary = 1) as image_url " +
                "FROM Car c " +
                "INNER JOIN CarPromotion cp ON c.car_id = cp.car_id " +
                "INNER JOIN Brand b ON c.brand_id = b.brand_id " +
                "WHERE cp.promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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
                    car.setDiscountPercentage(rs.getDouble("discount_percentage"));
                    car.setDiscountAmount(rs.getDouble("discount_amount"));

                    cars.add(car);
                }
            }

            logger.info("Retrieved {} cars in promotion: {}", cars.size(), promotionId);
            return cars;

        } catch (SQLException e) {
            logger.error("Error retrieving cars in promotion: {}", promotionId, e);
            throw new RuntimeException("Không thể lấy danh sách xe trong khuyến mãi", e);
        }
    }

    /**
     * Get car IDs in a promotion
     */
    public List<Integer> getCarIdsInPromotion(int promotionId) {
        List<Integer> carIds = new ArrayList<>();
        String sql = "SELECT car_id FROM CarPromotion WHERE promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    carIds.add(rs.getInt("car_id"));
                }
            }

            logger.info("Retrieved {} car IDs in promotion: {}", carIds.size(), promotionId);
            return carIds;

        } catch (SQLException e) {
            logger.error("Error retrieving car IDs in promotion: {}", promotionId, e);
            throw new RuntimeException("Không thể lấy danh sách xe trong khuyến mãi", e);
        }
    }

    // =============================================
    // USER PROMOTION METHODS
    // =============================================

    /**
     * Claim promotion for user
     */
    public boolean claimPromotion(int userId, int promotionId) {
        String checkSql = "SELECT COUNT(*) FROM Promotion " +
                "WHERE promotion_id = ? AND GETDATE() BETWEEN start_date AND end_date";

        String insertSql = "INSERT INTO UserPromotion (user_id, promotion_id) VALUES (?, ?)";

        try (Connection conn = DBContext.getConnection()) {

            // Check if promotion is active
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, promotionId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new RuntimeException("Khuyến mãi không còn hiệu lực");
                    }
                }
            }

            // Insert claim
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, promotionId);

                int affectedRows = stmt.executeUpdate();
                boolean success = affectedRows > 0;

                logger.info("User {} claimed promotion {}: {}", userId, promotionId, success);
                return success;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("duplicate") ||
                    e.getMessage().contains("UQ_UserPromotion")) {
                logger.warn("User {} already claimed promotion {}", userId, promotionId);
                throw new RuntimeException("Bạn đã nhận khuyến mãi này rồi!");
            }

            logger.error("Error claiming promotion", e);
            throw new RuntimeException("Không thể nhận khuyến mãi", e);
        }
    }

    /**
     * Check if user has claimed a promotion
     */
    public boolean hasUserClaimedPromotion(int userId, int promotionId)  {
        String sql = "SELECT COUNT(*) FROM UserPromotion WHERE user_id = ? AND promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

            return false;

        } catch (SQLException e) {
            logger.error("Error checking user promotion claim", e);
            throw new RuntimeException("Không thể kiểm tra trạng thái khuyến mãi", e);
        }
    }

    /**
     * Get user's claimed promotions
     */
    public List<Promotion> getUserClaimedPromotions(int userId)  {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT p.promotion_id, p.title, p.description, p.start_date, p.end_date, " +
                "p.discount_percentage, p.discount_amount, " +
                "up.claimed_at, up.is_used, up.used_at " +
                "FROM Promotion p " +
                "INNER JOIN UserPromotion up ON p.promotion_id = up.promotion_id " +
                "WHERE up.user_id = ? " +
                "ORDER BY up.claimed_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = mapResultSetToPromotion(rs);
                    promotion.setClaimedByUser(true);
                    promotion.setUsedByUser(rs.getBoolean("is_used"));

                    promotions.add(promotion);
                }
            }

            logger.info("Retrieved {} claimed promotions for user {}", promotions.size(), userId);
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving user claimed promotions", e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi đã nhận", e);
        }
    }

    /**
     * Get user's available (claimed but not used) promotions for a specific car
     */
    public List<Promotion> getUserAvailablePromotionsForCar(int userId, int carId)  {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT p.promotion_id, p.title, p.description, p.start_date, p.end_date, " +
                "p.discount_percentage, p.discount_amount, " +
                "cp.discount_percentage as car_discount_percentage, " +
                "cp.discount_amount as car_discount_amount " +
                "FROM Promotion p " +
                "INNER JOIN UserPromotion up ON p.promotion_id = up.promotion_id " +
                "INNER JOIN CarPromotion cp ON p.promotion_id = cp.promotion_id " +
                "WHERE up.user_id = ? " +
                "AND cp.car_id = ? " +
                "AND up.is_used = 0 " +
                "AND GETDATE() BETWEEN p.start_date AND p.end_date " +
                "ORDER BY cp.discount_percentage DESC, cp.discount_amount DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = mapResultSetToPromotion(rs);
                    promotion.setClaimedByUser(true);
                    promotion.setUsedByUser(false);

                    promotions.add(promotion);
                }
            }

            logger.info("Retrieved {} available promotions for user {} and car {}",
                    promotions.size(), userId, carId);
            return promotions;

        } catch (SQLException e) {
            logger.error("Error retrieving user available promotions for car", e);
            throw new RuntimeException("Không thể lấy danh sách khuyến mãi khả dụng", e);
        }
    }

    /**
     * Mark promotion as used
     */
    public boolean markPromotionAsUsed(int userId, int promotionId, int orderId) {
        String sql = "UPDATE UserPromotion " +
                "SET is_used = 1, used_at = GETDATE(), order_id = ? " +
                "WHERE user_id = ? AND promotion_id = ? AND is_used = 0";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, userId);
            stmt.setInt(3, promotionId);

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Marked promotion {} as used for user {}: {}", promotionId, userId, success);
            return success;

        } catch (SQLException e) {
            logger.error("Error marking promotion as used", e);
            throw new RuntimeException("Không thể cập nhật trạng thái khuyến mãi", e);
        }
    }

    /**
     * Get promotion statistics for admin
     */
    public int getTotalClaimsForPromotion(int promotionId)  {
        String sql = "SELECT COUNT(*) FROM UserPromotion WHERE promotion_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Error getting total claims for promotion", e);
            throw new RuntimeException("Không thể lấy thống kê khuyến mãi", e);
        }
    }

    /**
     * Get used claims count for promotion
     */
    public int getUsedClaimsForPromotion(int promotionId)  {
        String sql = "SELECT COUNT(*) FROM UserPromotion WHERE promotion_id = ? AND is_used = 1";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, promotionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Error getting used claims for promotion", e);
            throw new RuntimeException("Không thể lấy thống kê khuyến mãi", e);
        }
    }

    /**
     * Map ResultSet to Promotion object
     */
    private Promotion mapResultSetToPromotion(ResultSet rs) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(rs.getInt("promotion_id"));
        promotion.setTitle(rs.getString("title"));
        promotion.setDescription(rs.getString("description"));
        promotion.setStartDate(rs.getDate("start_date"));
        promotion.setEndDate(rs.getDate("end_date"));

        try {
            promotion.setDiscountPercentage(rs.getDouble("discount_percentage"));
            promotion.setDiscountAmount(rs.getDouble("discount_amount"));
        } catch (SQLException e) {
            promotion.setDiscountPercentage(0);
            promotion.setDiscountAmount(0);
        }

        return promotion;
    }
}