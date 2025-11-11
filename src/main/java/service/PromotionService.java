package service;

import dao.PromotionDAO;
import dao.CarDAO;
import dto.CarWithDiscountDTO;
import dto.PromotionDTO;
import model.Promotion;
import model.Car;
import model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * PromotionService - ALL Business Logic for Promotions
 *
 * @author Nguyen Gia Huy
 * @version 2.0 - Fixed to match database
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

    // ============ BUSINESS LOGIC - PROMOTION STATUS ============

    /**
     * Check if promotion is currently active
     */
    public boolean isPromotionActive(Promotion promotion) {
        if (promotion == null) {
            return false;
        }
        Date now = new Date();
        return promotion.getStartDate() != null
                && promotion.getEndDate() != null
                && now.after(promotion.getStartDate())
                && now.before(promotion.getEndDate());
    }

    /**
     * Check if promotion is expired
     */
    public boolean isPromotionExpired(Promotion promotion) {
        if (promotion == null || promotion.getEndDate() == null) {
            return false;
        }
        Date now = new Date();
        return now.after(promotion.getEndDate());
    }

    /**
     * Check if promotion is upcoming
     */
    public boolean isPromotionUpcoming(Promotion promotion) {
        if (promotion == null || promotion.getStartDate() == null) {
            return false;
        }
        Date now = new Date();
        return now.before(promotion.getStartDate());
    }

    // ============ BUSINESS LOGIC - DISCOUNT CALCULATION ============

    /**
     * Calculate discount value from percentage
     */
    public double calculateDiscountValue(double price, double percentage) {
        if (price <= 0 || percentage <= 0) {
            return 0;
        }
        return price * (percentage / 100.0);
    }

    /**
     * Calculate final price after discount
     */
    public double calculateFinalPrice(double price, double percentage) {
        double discountValue = calculateDiscountValue(price, percentage);
        return price - discountValue;
    }

    // ============ DTO CONVERSION METHODS ============

    /**
     * Convert Promotion to PromotionDTO with pre-calculated values
     * This ensures JSP views don't need to call business logic methods
     */
    public PromotionDTO toPromotionDTO(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        PromotionDTO dto = new PromotionDTO();

        // Copy basic information
        dto.setPromotionId(promotion.getPromotionId());
        dto.setTitle(promotion.getTitle());
        dto.setDescription(promotion.getDescription());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setDiscountPercentage(promotion.getDiscountPercentage());
        // User-specific flags (from model)
        dto.setClaimedByUser(promotion.isClaimedByUser());
        dto.setUsedByUser(promotion.isUsedByUser());

        // Pre-calculate status flags
        dto.setActive(isPromotionActive(promotion));
        dto.setExpired(isPromotionExpired(promotion));

        // Convert applicable cars with pre-calculated discounts
        if (promotion.getApplicableCars() != null && !promotion.getApplicableCars().isEmpty()) {
            List<CarWithDiscountDTO> carDTOs = promotion.getApplicableCars().stream()
                    .map(car -> toCarWithDiscountDTO(car, promotion))
                    .collect(Collectors.toList());
            dto.setApplicableCars(carDTOs);

            logger.debug("Converted {} cars for promotion {}", carDTOs.size(), promotion.getPromotionId());
        }

        return dto;
    }

    /**
     * Convert list of Promotions to list of PromotionDTOs
     */
    public List<PromotionDTO> toPromotionDTOs(List<Promotion> promotions) {
        if (promotions == null) {
            return null;
        }

        return promotions.stream()
                .map(this::toPromotionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Car to CarWithDiscountDTO with pre-calculated discount values
     * All discount calculations are done here, so JSP only displays values
     */
    public CarWithDiscountDTO toCarWithDiscountDTO(Car car, Promotion promotion) {
        if (car == null) {
            return null;
        }

        CarWithDiscountDTO dto = new CarWithDiscountDTO();

        // Copy basic car information
        dto.setCarId(car.getId());
        dto.setName(car.getName());
        dto.setBrandName(car.getBrandName());
        dto.setYear(car.getYear());
        dto.setColor(car.getColor());
        dto.setPrice(car.getPrice());
        dto.setStatus(car.getStatus());
        dto.setQuantity(car.getStock());
        dto.setImageUrl(car.getImageUrl());

        if (promotion != null && promotion.getDiscountPercentage() > 0) {
            double percentage = promotion.getDiscountPercentage();

            dto.setHasDiscount(true);
            dto.setDiscountPercentage(percentage);

            // Pre-calculate discount using percentage
            double discountValue = calculateDiscountValue(car.getPrice(), percentage);
            double discountedPrice = car.getPrice() - discountValue;

            dto.setDiscountValue(discountValue);
            dto.setDiscountedPrice(discountedPrice);

            logger.debug("Car {} discount: {}% = {}₫ -> final: {}₫",
                    car.getName(), percentage, discountValue, discountedPrice);
        } else {
            // No discount
            dto.setHasDiscount(false);
            dto.setDiscountPercentage(0);
            dto.setDiscountValue(0.0);
            dto.setDiscountedPrice(car.getPrice());
        }

        return dto;
    }

    /**
     * Convert Car to CarWithDiscountDTO without promotion context
     */
    public CarWithDiscountDTO toCarWithDiscountDTO(Car car) {
        return toCarWithDiscountDTO(car, null);
    }

    /**
     * Convert list of Cars to list of CarWithDiscountDTOs
     */
    public List<CarWithDiscountDTO> toCarWithDiscountDTOs(List<Car> cars) {
        if (cars == null) {
            return null;
        }

        return cars.stream()
                .map(this::toCarWithDiscountDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert CartItem to CartItemDTO with pre-calculated subtotal
     */
    public dto.CartItemDTO toCartItemDTO(model.CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        dto.CartItemDTO dto = new dto.CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCartId());
        dto.setCarId(cartItem.getCarId());
        dto.setQuantity(cartItem.getQuantity());

        // Convert car to DTO
        if (cartItem.getCar() != null) {
            CarWithDiscountDTO carDTO = toCarWithDiscountDTO(cartItem.getCar());
            dto.setCar(carDTO);

            // Pre-calculate subtotal
            double subtotal = cartItem.getCar().getPrice() * cartItem.getQuantity();
            dto.setSubtotal(subtotal);

            logger.debug("Cart item subtotal: {} x {} = {}",
                    cartItem.getCar().getPrice(), cartItem.getQuantity(), subtotal);
        }

        return dto;
    }

    /**
     * Convert list of CartItems to list of CartItemDTOs
     */
    public List<dto.CartItemDTO> toCartItemDTOs(List<model.CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }

        return cartItems.stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());
    }

    // ============ CART & PROMOTION LOGIC ============

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
                .filter(p -> !p.isUsedByUser() && isPromotionActive(p))
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

        if (!isPromotionActive(promotion)) {
            logger.warn("Promotion {} is not active", promotionId);
            return discounts;
        }

        double promotionPercentage = promotion.getDiscountPercentage();
        if (promotionPercentage <= 0) {
            logger.warn("Promotion {} has no discount", promotionId);
            return discounts;
        }

        // Get applicable car IDs
        List<Integer> applicableCarIds = promotionDAO.getCarIdsInPromotion(promotionId);

        logger.debug("Found {} cars applicable for promotion {} with {}% discount",
                applicableCarIds.size(), promotionId, promotionPercentage);

        // Calculate discount for each cart item
        for (CartItem item : cartItems) {
            int carId = item.getCar().getId();

            if (applicableCarIds.contains(carId)) {
                double originalPrice = item.getCar().getPrice();
                double discountPerItem = calculateDiscountValue(originalPrice, promotionPercentage);
                double totalDiscount = discountPerItem * item.getQuantity();

                discounts.put(carId, totalDiscount);

                logger.debug("Car {} (qty {}): {}% of {} = {}₫ per item, total: {}₫",
                        carId, item.getQuantity(), promotionPercentage,
                        originalPrice, discountPerItem, totalDiscount);
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

        if (!isPromotionActive(promotion)) {
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

        // Filter only active promotions
        promotions = promotions.stream()
                .filter(this::isPromotionActive)
                .collect(Collectors.toList());

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
     * Get best promotion for a car (highest discount percentage)
     */
    public Promotion getBestPromotionForCar(int carId, List<Promotion> promotions) {

        if (promotions == null || promotions.isEmpty()) {
            return null;
        }

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            return null;
        }

        Promotion bestPromotion = promotions.stream()
                .filter(p -> isPromotionActive(p) && p.getDiscountPercentage() > 0)
                .max((p1, p2) -> Double.compare(p1.getDiscountPercentage(), p2.getDiscountPercentage()))
                .orElse(null);

        if (bestPromotion != null) {
            double discountValue = calculateDiscountValue(
                    car.getPrice(), bestPromotion.getDiscountPercentage());

            logger.info("Best promotion for car {}: {} with {}% discount ({}₫)",
                    carId, bestPromotion.getPromotionId(),
                    bestPromotion.getDiscountPercentage(), discountValue);
        }

        return bestPromotion;
    }

    /**
     * Calculate discount value for a car with a specific promotion
     */
    public double calculateDiscountForCar(int carId, int promotionId) {
        Car car = carDAO.getCarById(carId);
        if (car == null) {
            return 0;
        }

        Promotion promotion = promotionDAO.getPromotionById(promotionId);
        if (promotion == null || !isPromotionActive(promotion)) {
            return 0;
        }

        // Check if promotion applies to this car
        List<Integer> applicableCarIds = promotionDAO.getCarIdsInPromotion(promotionId);
        if (!applicableCarIds.contains(carId)) {
            return 0;
        }

        return calculateDiscountValue(car.getPrice(), promotion.getDiscountPercentage());
    }
}