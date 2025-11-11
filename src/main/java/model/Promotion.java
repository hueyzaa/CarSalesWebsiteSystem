package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Nguyen Gia Huy
 * @version 2.0 - Fixed to match database
 */
public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int promotionId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private double discountPercentage;


    private boolean claimedByUser;
    private boolean usedByUser;
    private List<Car> applicableCars;

    // ============ CONSTRUCTORS ============

    public Promotion() {
    }

    public Promotion(int promotionId, String title, String description,
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

    public List<Car> getApplicableCars() {
        return applicableCars;
    }

    public void setApplicableCars(List<Car> applicableCars) {
        this.applicableCars = applicableCars;
    }
}