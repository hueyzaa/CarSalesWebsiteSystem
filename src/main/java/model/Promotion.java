package model;

import java.util.Date;
import java.util.List;

public class Promotion {
    private int promotionId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discountPercentage;  // Discount mặc định
    private double discountAmount;      // Discount mặc định

    private boolean claimedByUser;
    private boolean usedByUser;

    private List<Car> applicableCars;

    // Constructors
    public Promotion() {
    }

    public Promotion(int promotionId, String title, String description, Date startDate, Date endDate) {
        this.promotionId = promotionId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
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

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
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

    public List<Car> getApplicableCars() {
        return applicableCars;
    }

    public void setApplicableCars(List<Car> applicableCars) {
        this.applicableCars = applicableCars;
    }

    // Utility methods
    public boolean isActive() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    public boolean isExpired() {
        Date now = new Date();
        return now.after(endDate);
    }

    public boolean isUpcoming() {
        Date now = new Date();
        return now.before(startDate);
    }

    /**
     * Calculate discount for a given price (default promotion discount)
     */
    public double calculateDiscount(double originalPrice) {
        if (discountPercentage > 0) {
            return originalPrice * (discountPercentage / 100);
        }
        return discountAmount;
    }

    public double calculateFinalPrice(double originalPrice) {
        return originalPrice - calculateDiscount(originalPrice);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId=" + promotionId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", claimedByUser=" + claimedByUser +
                ", usedByUser=" + usedByUser +
                '}';
    }
}