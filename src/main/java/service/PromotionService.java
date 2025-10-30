package service;

import dao.PromotionDAO;
import dao.CarDAO;
import model.Promotion;
import model.Car;
import model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Service layer for promotion business logic
 * Handles promotion validation, calculation, and application
 */
public class PromotionService {
    private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);
    private final PromotionDAO promotionDAO;
    private final CarDAO carDAO;

    public PromotionService() {
        this.promotionDAO = new PromotionDAO();
        this.carDAO = new CarDAO();
    }

    // For testing with mock DAOs
    public PromotionService(PromotionDAO promotionDAO, CarDAO carDAO) {
        this.promotionDAO = promotionDAO;
        this.carDAO = carDAO;
    }

    /**
     * Get available promotions that user can apply to their cart
     * Returns promotions that:
     * 1. User has claimed
     * 2. Not yet used
     * 3. Currently active
     * 4. Applicable to at least one car in the cart
     */
    public List<Promotion> getAvailablePromotionsForCart(int userId, List<CartItem> cartItems) {

        logger.info("Getting available promotions for user {} with {} cart items",
                userId, cartItems != null ? cartItems.size() : 0);

        if (cartItems == null || cartItems.isEmpty()) {
            logger.warn("Cart is empty, no promotions available");
            return List.of();
        }

        // Get all user's unused promotions
        List<Promotion> userPromotions = promotionDAO.getUserClaimedPromotions(userId);

        // Filter: only unused and active promotions
        List<Promotion> availablePromotions = userPromotions.stream()
                .filter(p -> !p.isUsedByUser() && p.isActive())
                .collect(Collectors.toList());

        logger.debug("Found {} unused active promotions", availablePromotions.size());

        // For each promotion, check if it applies to any car in cart
        availablePromotions = availablePromotions.stream()
                .filter(promotion -> {
                    try {
                        List<Integer> applicableCarIds = promotionDAO.getCarIdsInPromotion(
                                promotion.getPromotionId());

                        // Check if any cart item matches
                        boolean hasMatch = cartItems.stream()
                                .anyMatch(item -> applicableCarIds.contains(item.getCar().getId()));

                        if (!hasMatch) {
                            logger.debug("Promotion {} does not apply to any cart item",
                                    promotion.getPromotionId());
                        }

                        return hasMatch;
                    } catch (Exception e) {
                        logger.error("Error checking promotion applicability", e);
                        return false;
                    }
                })
                .collect(Collectors.toList());

        logger.info("Found {} available promotions for cart", availablePromotions.size());
        return availablePromotions;
    }

    /**
     * Calculate discount for cart items when applying a promotion
     * Returns map of carId -> discountAmount
     */
    public Map<Integer, Double> calculatePromotionDiscount(
            int promotionId, List<CartItem> cartItems) {

        Map<Integer, Double> discounts = new HashMap<>();

        if (cartItems == null || cartItems.isEmpty()) {
            logger.warn("Cart is empty, no discount to calculate");
            return discounts;
        }

        // Get promotion details
        Promotion promotion = promotionDAO.getPromotionById(promotionId);
        if (promotion == null) {
            logger.warn("Promotion {} not found", promotionId);
            return discounts;
        }

        if (!promotion.isActive()) {
            logger.warn("Promotion {} is not active", promotionId);
            return discounts;
        }

        // Get cars in promotion with their specific discounts
        List<Car> applicableCars = promotionDAO.getCarsInPromotion(promotionId);
        Map<Integer, Car> carDiscountMap = new HashMap<>();
        for (Car car : applicableCars) {
            carDiscountMap.put(car.getId(), car);
        }

        logger.debug("Found {} cars applicable for promotion {}",
                carDiscountMap.size(), promotionId);

        // Calculate discount for each cart item
        for (CartItem item : cartItems) {
            int carId = item.getCar().getId();

            if (carDiscountMap.containsKey(carId)) {
                Car carWithDiscount = carDiscountMap.get(carId);
                double originalPrice = item.getCar().getPrice();
                double discountAmount = 0;

                // Priority: Car-specific discount > Promotion default discount
                if (carWithDiscount.getDiscountPercentage() > 0) {
                    discountAmount = originalPrice * (carWithDiscount.getDiscountPercentage() / 100);
                    logger.debug("Applying car-specific percentage discount: {}%",
                            carWithDiscount.getDiscountPercentage());
                } else if (carWithDiscount.getDiscountAmount() > 0) {
                    discountAmount = carWithDiscount.getDiscountAmount();
                    logger.debug("Applying car-specific amount discount: {}₫", discountAmount);
                } else if (promotion.getDiscountPercentage() > 0) {
                    discountAmount = originalPrice * (promotion.getDiscountPercentage() / 100);
                    logger.debug("Applying promotion percentage discount: {}%",
                            promotion.getDiscountPercentage());
                } else if (promotion.getDiscountAmount() > 0) {
                    discountAmount = promotion.getDiscountAmount();
                    logger.debug("Applying promotion amount discount: {}₫", discountAmount);
                }

                // Apply discount to all quantity
                discountAmount *= item.getQuantity();
                discounts.put(carId, discountAmount);

                logger.debug("Total discount for car {} (qty {}): {}₫",
                        carId, item.getQuantity(), discountAmount);
            }
        }

        return discounts;
    }

    /**
     * Calculate total discount for entire cart
     */
    public double calculateTotalDiscount(int promotionId, List<CartItem> cartItems) {

        Map<Integer, Double> discounts = calculatePromotionDiscount(promotionId, cartItems);
        double totalDiscount = discounts.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        logger.info("Total discount for promotion {}: {}₫", promotionId, totalDiscount);
        return totalDiscount;
    }

    /**
     * Validate if promotion can be applied to cart
     * Returns null if valid, error message if invalid
     */
    public String validatePromotionForCart(int userId, int promotionId, List<CartItem> cartItems) {

        if (cartItems == null || cartItems.isEmpty()) {
            return "Giỏ hàng trống!";
        }

        // Check if user has claimed this promotion
        if (!promotionDAO.hasUserClaimedPromotion(userId, promotionId)) {
            logger.warn("User {} has not claimed promotion {}", userId, promotionId);
            return "Bạn chưa nhận khuyến mãi này!";
        }

        // Check if promotion exists and is active
        Promotion promotion = promotionDAO.getPromotionById(promotionId);
        if (promotion == null) {
            logger.warn("Promotion {} not found", promotionId);
            return "Khuyến mãi không tồn tại!";
        }

        if (!promotion.isActive()) {
            logger.warn("Promotion {} is not active", promotionId);
            return "Khuyến mãi đã hết hạn hoặc chưa có hiệu lực!";
        }

        // Check if promotion applies to any cart item
        List<Integer> applicableCarIds = promotionDAO.getCarIdsInPromotion(promotionId);
        boolean hasApplicableCar = cartItems.stream()
                .anyMatch(item -> applicableCarIds.contains(item.getCar().getId()));

        if (!hasApplicableCar) {
            logger.warn("Promotion {} does not apply to any cart items", promotionId);
            return "Khuyến mãi không áp dụng cho các xe trong giỏ hàng!";
        }

        logger.info("Promotion {} is valid for user {}'s cart", promotionId, userId);
        return null; // Valid
    }

    /**
     * Get active promotions for a specific car (for car detail page)
     */
    public List<Promotion> getActivePromotionsForCar(int carId, Integer userId) {

        logger.info("Getting active promotions for car {}", carId);

        List<Promotion> promotions = promotionDAO.getPromotionsByCar(carId);

        // If user is logged in, check claim status for each promotion
        if (userId != null) {
            for (Promotion promotion : promotions) {
                boolean claimed = promotionDAO.hasUserClaimedPromotion(
                        userId, promotion.getPromotionId());
                promotion.setClaimedByUser(claimed);

                logger.debug("Promotion {}: claimed={}",
                        promotion.getPromotionId(), claimed);
            }
        }

        logger.info("Found {} active promotions for car {}", promotions.size(), carId);
        return promotions;
    }

    /**
     * Get discount info for a car with a specific promotion
     */
    public Car getCarWithPromotionInfo(int carId, int promotionId) {
        logger.debug("Getting car {} with promotion {} info", carId, promotionId);

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            logger.warn("Car {} not found", carId);
            return null;
        }

        // Get promotion details and car-specific discount
        List<Car> carsInPromotion = promotionDAO.getCarsInPromotion(promotionId);

        for (Car promotionCar : carsInPromotion) {
            if (promotionCar.getId() == carId) {
                // Copy discount info
                car.setDiscountPercentage(promotionCar.getDiscountPercentage());
                car.setDiscountAmount(promotionCar.getDiscountAmount());

                logger.debug("Car {} has discount: {}% or {}₫",
                        carId,
                        promotionCar.getDiscountPercentage(),
                        promotionCar.getDiscountAmount());
                break;
            }
        }

        return car;
    }

    /**
     * Calculate best discount among multiple promotions for display
     */
    public Promotion getBestPromotionForCar(int carId, List<Promotion> promotions) {

        if (promotions == null || promotions.isEmpty()) {
            return null;
        }

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            return null;
        }

        Promotion bestPromotion = null;
        double maxDiscount = 0;

        for (Promotion promo : promotions) {
            Car carWithDiscount = getCarWithPromotionInfo(carId, promo.getPromotionId());
            if (carWithDiscount == null) {
                continue;
            }

            double discountValue = carWithDiscount.getDiscountValue();

            if (discountValue > maxDiscount) {
                maxDiscount = discountValue;
                bestPromotion = promo;
            }
        }

        if (bestPromotion != null) {
            logger.info("Best promotion for car {}: {} with {}₫ discount",
                    carId, bestPromotion.getPromotionId(), maxDiscount);
        }

        return bestPromotion;
    }
}