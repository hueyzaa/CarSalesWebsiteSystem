package service;

import dao.CartDAO;
import dto.CheckoutSummaryDTO;
import model.CartItem;
import model.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service xử lý business logic cho checkout
 * Tách biệt hoàn toàn business logic khỏi Controller
 */
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);
    private static final double DEPOSIT_PERCENTAGE = 10.0;

    private final PromotionService promotionService;
    private final CartDAO cartDAO;

    public CheckoutService() {
        this.promotionService = new PromotionService();
        this.cartDAO = new CartDAO();
    }

    // Constructor for dependency injection (for testing)
    public CheckoutService(PromotionService promotionService, CartDAO cartDAO) {
        this.promotionService = promotionService;
        this.cartDAO = cartDAO;
    }

    // ============ PUBLIC METHODS - Business Logic ============

    /**
     * Tính toán tất cả các kịch bản checkout có thể xảy ra
     * Controller chỉ gọi method này và gửi kết quả đến View
     */
    public CheckoutCalculationResult calculateAllScenarios(Integer userId) {
        CheckoutCalculationResult result = new CheckoutCalculationResult();

        try {
            // 1. Lấy cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                result.setError("Giỏ hàng trống");
                return result;
            }

            // 2. Validate stock
            String stockError = validateStock(cartItems);
            if (stockError != null) {
                result.setError(stockError);
                return result;
            }

            // 3. Tính tổng giá gốc
            double originalTotal = calculateCartTotal(cartItems);
            result.setOriginalTotal(originalTotal);
            result.setCartItems(cartItems);

            // 4. Lấy promotions khả dụng
            List<Promotion> availablePromotions = promotionService
                    .getAvailablePromotionsForCart(userId, cartItems);
            result.setAvailablePromotions(availablePromotions);

            // 5. Tính toán kịch bản không dùng promotion
            result.setNoPromotionDeposit(
                    calculateDepositScenario(originalTotal, null, 0, 0)
            );
            result.setNoPromotionShowroom(
                    calculateShowroomScenario(originalTotal, null, 0, 0)
            );

            // 6. Tính toán kịch bản cho từng promotion
            Map<Integer, CheckoutSummaryDTO> promotionPreviews =
                    calculatePromotionPreviews(userId, cartItems, availablePromotions, originalTotal);
            result.setPromotionPreviews(promotionPreviews);

            result.setSuccess(true);
            logger.info("Calculated all checkout scenarios for user {} - {} items, {} promotions",
                    userId, cartItems.size(), availablePromotions.size());

        } catch (Exception e) {
            logger.error("Error calculating checkout scenarios for user {}", userId, e);
            result.setError("Không thể tính toán thông tin checkout: " + e.getMessage());
        }

        return result;
    }

    /**
     * Tính toán checkout cuối cùng khi user submit
     * Đảm bảo logic tính toán nhất quán với preview
     */
    public CheckoutSubmitResult calculateFinalCheckout(
            Integer userId,
            String paymentType,
            Integer promotionId) {

        CheckoutSubmitResult result = new CheckoutSubmitResult();

        try {
            // 1. Validate payment type
            if (!isValidPaymentType(paymentType)) {
                result.setError("Hình thức thanh toán không hợp lệ");
                return result;
            }

            // 2. Validate promotion cho showroom
            if ("SHOWROOM".equals(paymentType) && promotionId != null) {
                result.setError("Khuyến mãi chỉ áp dụng cho hình thức Đặt Cọc Online!");
                return result;
            }

            // 3. Lấy cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                result.setError("Giỏ hàng trống");
                return result;
            }

            // 4. Validate stock
            String stockError = validateStock(cartItems);
            if (stockError != null) {
                result.setError(stockError);
                return result;
            }

            // 5. Tính tổng giá gốc
            double orderTotal = calculateCartTotal(cartItems);

            // 6. Tính discount (nếu có)
            double discount = 0;
            if (promotionId != null && "DEPOSIT".equals(paymentType)) {
                String promoError = promotionService.validatePromotionForCart(
                        userId, promotionId, cartItems);

                if (promoError != null) {
                    result.setError(promoError);
                    return result;
                }

                discount = promotionService.calculateTotalDiscount(promotionId, cartItems);
            }

            // 7. Tính final total
            double finalTotal = Math.max(0, orderTotal - discount);

            // 8. Tạo checkout data dựa theo payment type
            if ("SHOWROOM".equals(paymentType)) {
                populateShowroomCheckoutResult(result, finalTotal);
            } else {
                populateDepositCheckoutResult(result, finalTotal, discount);
            }

            result.setSuccess(true);
            result.setCartItems(cartItems);
            result.setPromotionId(promotionId);

            logger.info("Final checkout calculated for user {}: total={}, discount={}, paymentType={}",
                    userId, finalTotal, discount, paymentType);

        } catch (Exception e) {
            logger.error("Error calculating final checkout for user {}", userId, e);
            result.setError("Không thể tính toán thông tin đơn hàng: " + e.getMessage());
        }

        return result;
    }

    /**
     * Validate stock cho tất cả items trong cart
     */
    public String validateStock(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return "Giỏ hàng trống";
        }

        StringBuilder errors = new StringBuilder();

        for (CartItem item : cartItems) {
            if (item.getCar() == null) {
                continue;
            }

            int stock = item.getCar().getStock();
            int quantity = item.getQuantity();

            if (stock < quantity) {
                if (!errors.isEmpty()) {
                    errors.append(" ");
                }
                errors.append(item.getCar().getModel())
                        .append(" chỉ còn ")
                        .append(stock)
                        .append(" xe.");
            }
        }

        return !errors.isEmpty() ? "Số lượng không đủ: " + errors : null;
    }

    /**
     * Tính tổng giá trị cart
     */
    public double calculateCartTotal(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0.0;
        }

        return cartItems.stream()
                .mapToDouble(this::calculateCartItemSubtotal)
                .sum();
    }

    // ============ PRIVATE HELPER METHODS ============

    /**
     * Tính subtotal cho một cart item
     */
    private double calculateCartItemSubtotal(CartItem item) {
        if (item == null || item.getCar() == null) {
            return 0.0;
        }
        return item.getCar().getPrice() * item.getQuantity();
    }

    /**
     * Tính toán preview cho tất cả promotions
     */
    private Map<Integer, CheckoutSummaryDTO> calculatePromotionPreviews(
            Integer userId,
            List<CartItem> cartItems,
            List<Promotion> promotions,
            double originalTotal) {

        Map<Integer, CheckoutSummaryDTO> previews = new HashMap<>();

        if (promotions == null || promotions.isEmpty()) {
            return previews;
        }

        for (Promotion promo : promotions) {
            try {
                // Validate promotion
                String error = promotionService.validatePromotionForCart(
                        userId, promo.getPromotionId(), cartItems);

                if (error != null) {
                    logger.debug("Promotion {} not applicable: {}",
                            promo.getPromotionId(), error);
                    continue;
                }

                // Tính discount
                double discountAmount = promotionService.calculateTotalDiscount(
                        promo.getPromotionId(), cartItems);

                if (discountAmount > 0) {
                    // Tạo summary cho DEPOSIT với promotion
                    CheckoutSummaryDTO summary = calculateDepositScenario(
                            originalTotal,
                            discountAmount,
                            (int) promo.getDiscountPercentage(),
                            promo.getPromotionId()
                    );

                    previews.put(promo.getPromotionId(), summary);

                    logger.debug("Promotion {} preview calculated: discount={}, finalTotal={}, deposit={}",
                            promo.getPromotionId(),
                            discountAmount,
                            summary.getFinalTotal(),
                            summary.getDepositAmount());
                }

            } catch (Exception e) {
                logger.error("Error calculating preview for promotion {}",
                        promo.getPromotionId(), e);
            }
        }

        return previews;
    }

    /**
     * Tính toán kịch bản đặt cọc
     */
    private CheckoutSummaryDTO calculateDepositScenario(
            double originalTotal,
            Double discountAmount,
            int discountPercentage,
            int promotionId) {

        return new CheckoutSummaryDTO(
                originalTotal,
                discountAmount,
                discountPercentage,
                promotionId,
                DEPOSIT_PERCENTAGE
        );
    }

    /**
     * Tính toán kịch bản thanh toán tại showroom
     */
    private CheckoutSummaryDTO calculateShowroomScenario(
            double originalTotal,
            Double discountAmount,
            int discountPercentage,
            int promotionId) {

        return new CheckoutSummaryDTO(
                originalTotal,
                discountAmount,
                discountPercentage,
                promotionId
        );
    }

    /**
     * Populate result cho showroom checkout
     */
    private void populateShowroomCheckoutResult(CheckoutSubmitResult result, double finalTotal) {
        result.setFinalTotal(finalTotal);
        result.setPaymentAmount(0.0); // Không cần thanh toán online
        result.setDepositAmount(null);
        result.setNotes(String.format(
                "Khách hàng sẽ thanh toán toàn bộ %,.0f₫ tại showroom. " +
                        "Vui lòng liên hệ khách hàng để xác nhận và hẹn lịch đến showroom.",
                finalTotal
        ));
    }

    /**
     * Populate result cho deposit checkout
     */
    private void populateDepositCheckoutResult(
            CheckoutSubmitResult result,
            double finalTotal,
            double discount) {

        double depositAmount = finalTotal * (DEPOSIT_PERCENTAGE / 100.0);
        double remainingAmount = finalTotal - depositAmount;

        result.setFinalTotal(finalTotal);
        result.setDiscount(discount);
        result.setDepositAmount(depositAmount);
        result.setPaymentAmount(depositAmount);
        result.setRemainingAmount(remainingAmount);

        // Tạo notes
        if (discount > 0) {
            result.setNotes(String.format(
                    "Chờ thanh toán đặt cọc %,.0f₫ (%.0f%%). " +
                            "Đã áp dụng khuyến mãi giảm %,.0f₫. " +
                            "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ " +
                            "tại showroom khi nhận xe.",
                    depositAmount, DEPOSIT_PERCENTAGE, discount, remainingAmount
            ));
            result.setSuccessMessage(String.format("Bạn đã tiết kiệm %,.0f₫!", discount));
        } else {
            result.setNotes(String.format(
                    "Chờ thanh toán đặt cọc %,.0f₫ (%.0f%%). " +
                            "Sau khi đặt cọc thành công, khách hàng cần thanh toán %,.0f₫ " +
                            "tại showroom khi nhận xe.",
                    depositAmount, DEPOSIT_PERCENTAGE, remainingAmount
            ));
        }
    }

    /**
     * Validate payment type
     */
    private boolean isValidPaymentType(String paymentType) {
        return "DEPOSIT".equals(paymentType) || "SHOWROOM".equals(paymentType);
    }

    // ============ RESULT CLASSES ============

    /**
     * Kết quả tính toán tất cả các kịch bản checkout
     */
    public static class CheckoutCalculationResult {
        private boolean success;
        private String error;

        private double originalTotal;
        private List<CartItem> cartItems;
        private List<Promotion> availablePromotions;

        private CheckoutSummaryDTO noPromotionDeposit;
        private CheckoutSummaryDTO noPromotionShowroom;
        private Map<Integer, CheckoutSummaryDTO> promotionPreviews;

        public CheckoutCalculationResult() {
            this.success = false;
        }

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
            this.success = false;
        }

        public double getOriginalTotal() {
            return originalTotal;
        }

        public void setOriginalTotal(double originalTotal) {
            this.originalTotal = originalTotal;
        }

        public List<CartItem> getCartItems() {
            return cartItems;
        }

        public void setCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        public List<Promotion> getAvailablePromotions() {
            return availablePromotions;
        }

        public void setAvailablePromotions(List<Promotion> availablePromotions) {
            this.availablePromotions = availablePromotions;
        }

        public CheckoutSummaryDTO getNoPromotionDeposit() {
            return noPromotionDeposit;
        }

        public void setNoPromotionDeposit(CheckoutSummaryDTO noPromotionDeposit) {
            this.noPromotionDeposit = noPromotionDeposit;
        }

        public CheckoutSummaryDTO getNoPromotionShowroom() {
            return noPromotionShowroom;
        }

        public void setNoPromotionShowroom(CheckoutSummaryDTO noPromotionShowroom) {
            this.noPromotionShowroom = noPromotionShowroom;
        }

        public Map<Integer, CheckoutSummaryDTO> getPromotionPreviews() {
            return promotionPreviews;
        }

        public void setPromotionPreviews(Map<Integer, CheckoutSummaryDTO> promotionPreviews) {
            this.promotionPreviews = promotionPreviews;
        }
    }

    /**
     * Kết quả tính toán checkout khi submit
     */
    public static class CheckoutSubmitResult {
        private boolean success;
        private String error;

        private List<CartItem> cartItems;
        private Integer promotionId;

        private double finalTotal;
        private double discount;
        private double paymentAmount;
        private Double depositAmount;
        private Double remainingAmount;

        private String notes;
        private String successMessage;

        public CheckoutSubmitResult() {
            this.success = false;
        }

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
            this.success = false;
        }

        public List<CartItem> getCartItems() {
            return cartItems;
        }

        public void setCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        public Integer getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(Integer promotionId) {
            this.promotionId = promotionId;
        }

        public double getFinalTotal() {
            return finalTotal;
        }

        public void setFinalTotal(double finalTotal) {
            this.finalTotal = finalTotal;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(double paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public Double getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(Double depositAmount) {
            this.depositAmount = depositAmount;
        }

        public Double getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(Double remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }
    }
}