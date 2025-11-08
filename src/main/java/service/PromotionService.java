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
 * Service layer for promotion business logic
 * Handles promotion validation, calculation, and application
 * UPDATED: Added DTO conversion methods for view layer
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
     * Check if promotion is currently active
     */
    private boolean isPromotionActive(Promotion promotion) {
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
    private boolean isPromotionExpired(Promotion promotion) {
        if (promotion == null || promotion.getEndDate() == null) {
            return false;
        }
        Date now = new Date();
        return now.after(promotion.getEndDate());
    }

    /**
     * Calculate discount value for a car
     */
    private double calculateCarDiscountValue(Car car) {
        if (car == null) {
            return 0;
        }

        double price = car.getPrice();

        if (car.getDiscountPercentage() > 0) {
            return price * (car.getDiscountPercentage() / 100);
        } else if (car.getDiscountAmount() > 0) {
            return Math.min(car.getDiscountAmount(), price);
        }
        return 0;
    }

    // ============ DTO CONVERSION METHODS (NEW) ============

    /**
     * Convert Promotion to PromotionDTO with pre-calculated values
     * This ensures JSP views don't need to call business logic methods
     */
    public PromotionDTO toPromotionDTO(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        PromotionDTO dto = new PromotionDTO();

        // Basic information
        dto.setPromotionId(promotion.getPromotionId());
        dto.setTitle(promotion.getTitle());
        dto.setDescription(promotion.getDescription());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setDiscountPercentage(promotion.getDiscountPercentage());
        dto.setDiscountAmount(promotion.getDiscountAmount());

        // User-specific flags (already available in model)
        dto.setClaimedByUser(promotion.isClaimedByUser());
        dto.setUsedByUser(promotion.isUsedByUser());

        // Status flags (pre-calculated)
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

        // Basic car information - FIXED: Use correct Car model method names
        dto.setCarId(car.getId());              // getId() not getCarId()
        dto.setName(car.getName());             // getName() (alias for getModel())
        dto.setBrandName(car.getBrandName());
        dto.setYear(car.getYear());
        dto.setColor(car.getColor());
        dto.setPrice(car.getPrice());
        dto.setStatus(car.getStatus());
        dto.setQuantity(car.getStock());        // getStock() not getQuantity()
        dto.setImageUrl(car.getImageUrl());     // ADD THIS - imageUrl

        // Pre-calculate discount information
        boolean hasDiscount = car.getDiscountPercentage() > 0 || car.getDiscountAmount() > 0;
        dto.setHasDiscount(hasDiscount);
        dto.setDiscountPercentage(car.getDiscountPercentage());
        dto.setDiscountAmount(car.getDiscountAmount());

        if (hasDiscount) {
            // Calculate discount value using service method
            double discountValue = calculateCarDiscountValue(car);
            double discountedPrice = car.getPrice() - discountValue;

            dto.setDiscountValue(discountValue);
            dto.setDiscountedPrice(discountedPrice);

            logger.debug("Car {} discount: {} -> {} (saved: {})",
                    car.getName(), car.getPrice(), discountedPrice, discountValue);
        } else {
            // No discount
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
        dto.setId(cartItem.getId());              // getId() not getCartItemId()
        dto.setCartId(cartItem.getCartId());      // getCartId() not getUserId()
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

    // ============ EXISTING BUSINESS LOGIC METHODS ============

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

            double discountValue = calculateCarDiscountValue(carWithDiscount);

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