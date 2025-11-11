package dto;

import java.util.Date;
import java.util.List;

/**
 * PromotionDTO - Data Transfer Object for Promotion
 * Pre-calculated values from PromotionService
 *
 * @author Nguyen Gia Huy
 * @version 2.0 - Fixed: Only percentage discount
 */
public class PromotionDTO {
    // Basic promotion information
    private int promotionId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discountPercentage;

    // User-specific flags (pre-calculated)
    private boolean claimedByUser;
    private boolean usedByUser;

    // Status flags (pre-calculated)
    private boolean active;
    private boolean expired;

    // Applicable cars with pre-calculated discounts
    private List<CarWithDiscountDTO> applicableCars;

    // ============ CONSTRUCTORS ============

    public PromotionDTO() {
    }

    public PromotionDTO(int promotionId, String title, String description,
                        Date startDate, Date endDate) {
        this.promotionId = promotionId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // ============ GETTERS & SETTERS ============

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isClaimedByUser() {
        return claimedByUser;
    }

    public void setClaimedByUser(boolean claimedByUser) {
        this.claimedByUser = claimedByUser;
    }

    public boolean isUsedByUser() {
        return usedByUser;
    }

    public void setUsedByUser(boolean usedByUser) {
        this.usedByUser = usedByUser;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public List<CarWithDiscountDTO> getApplicableCars() {
        return applicableCars;
    }

    public void setApplicableCars(List<CarWithDiscountDTO> applicableCars) {
        this.applicableCars = applicableCars;
    }

    @Override
    public String toString() {
        return "PromotionDTO{" +
                "promotionId=" + promotionId +
                ", title='" + title + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", active=" + active +
                ", claimedByUser=" + claimedByUser +
                ", applicableCars=" + (applicableCars != null ? applicableCars.size() + " cars" : "null") +
                '}';
    }
}