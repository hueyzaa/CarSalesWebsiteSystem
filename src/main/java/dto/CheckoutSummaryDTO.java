package dto;

/**
 * DTO chứa tất cả thông tin đã được tính toán cho màn hình checkout
 * View (JSP) chỉ cần hiển thị, không cần tính toán gì thêm
 */
public class CheckoutSummaryDTO {

    // Giá trị gốc
    private double originalTotal;

    // Thông tin giảm giá
    private Double discountAmount;        // Số tiền giảm (null nếu không có)
    private Integer discountPercentage;   // % giảm (null nếu không có)
    private Integer appliedPromotionId;   // ID promotion đang áp dụng

    // Tổng tiền sau giảm giá
    private double finalTotal;

    // Số tiền cần thanh toán (đặt cọc hoặc full)
    private double paymentAmount;

    // Thông tin đặt cọc
    private double depositPercentage = 10.0;
    private double depositAmount;
    private double remainingAmount;

    // Trạng thái
    private boolean hasPromotion;
    private String paymentType;  // "DEPOSIT" hoặc "SHOWROOM"

    // Constructors
    public CheckoutSummaryDTO() {
    }

    /**
     * Constructor cho thanh toán tại showroom (không đặt cọc)
     */
    public CheckoutSummaryDTO(double originalTotal, Double discountAmount,
                              int discountPercentage, int appliedPromotionId) {
        this.originalTotal = originalTotal;
        this.discountAmount = discountAmount;
        this.discountPercentage = (discountPercentage > 0 || discountAmount != null) ? discountPercentage : null;
        this.appliedPromotionId = (appliedPromotionId > 0 || discountAmount != null) ? appliedPromotionId : null;
        this.hasPromotion = (discountAmount != null && discountAmount > 0);

        // Tính tổng sau giảm
        this.finalTotal = originalTotal - (discountAmount != null ? discountAmount : 0);

        // Showroom: không cần đặt cọc
        this.paymentAmount = this.finalTotal;
        this.paymentType = "SHOWROOM";
        this.depositAmount = 0;
        this.remainingAmount = 0;
    }

    /**
     * Constructor cho đặt cọc online
     */
    public CheckoutSummaryDTO(double originalTotal, Double discountAmount,
                              int discountPercentage, int appliedPromotionId,
                              double depositPercentage) {
        this.originalTotal = originalTotal;
        this.discountAmount = discountAmount;
        this.discountPercentage = (discountPercentage > 0 || discountAmount != null) ? discountPercentage : null;
        this.appliedPromotionId = (appliedPromotionId > 0 || discountAmount != null) ? appliedPromotionId : null;
        this.hasPromotion = (discountAmount != null && discountAmount > 0);

        // Tính tổng sau giảm
        this.finalTotal = originalTotal - (discountAmount != null ? discountAmount : 0);

        // Deposit: tính tiền cọc và còn lại
        this.depositPercentage = depositPercentage;
        this.depositAmount = this.finalTotal * (depositPercentage / 100.0);
        this.remainingAmount = this.finalTotal - this.depositAmount;
        this.paymentAmount = this.depositAmount;
        this.paymentType = "DEPOSIT";
    }

    // Getters and Setters
    public double getOriginalTotal() {
        return originalTotal;
    }

    public void setOriginalTotal(double originalTotal) {
        this.originalTotal = originalTotal;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getAppliedPromotionId() {
        return appliedPromotionId;
    }

    public void setAppliedPromotionId(Integer appliedPromotionId) {
        this.appliedPromotionId = appliedPromotionId;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public double getDepositPercentage() {
        return depositPercentage;
    }

    public void setDepositPercentage(double depositPercentage) {
        this.depositPercentage = depositPercentage;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean isHasPromotion() {
        return hasPromotion;
    }

    public void setHasPromotion(boolean hasPromotion) {
        this.hasPromotion = hasPromotion;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}