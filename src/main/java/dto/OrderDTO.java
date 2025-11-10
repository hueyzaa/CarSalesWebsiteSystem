package dto;

import model.Transaction;
import java.util.Date;
import java.util.List;

/**
 * OrderDTO - Data Transfer Object for Order with pre-calculated values
 */
public class OrderDTO {
    // Basic order information
    private int orderId;
    private int userId;
    private double totalAmount;
    private double paidAmount;
    private String status;
    private String paymentType;
    private String notes;
    private Date createdAt;  // Only createdAt, no updatedAt


    private List<OrderDetailDTO> orderDetails;

    // Transactions remain as Model (no calculations needed)
    private List<Transaction> transactions;

    // Pre-calculated display values
    private String statusDisplay;
    private String statusColor;
    private String paymentTypeDisplay;
    private String formattedTotal;

    // Pre-calculated amounts
    private Double depositAmount;
    private Double remainingAmount;

    // Pre-calculated flags
    private boolean fullyPaid;
    private boolean canBeCancelled;  // Pre-calculated business logic

    // Computed values
    private int totalItems;

    // Constructors
    public OrderDTO() {
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getPaymentTypeDisplay() {
        return paymentTypeDisplay;
    }

    public void setPaymentTypeDisplay(String paymentTypeDisplay) {
        this.paymentTypeDisplay = paymentTypeDisplay;
    }

    public String getFormattedTotal() {
        return formattedTotal;
    }

    public void setFormattedTotal(String formattedTotal) {
        this.formattedTotal = formattedTotal;
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

    public boolean isFullyPaid() {
        return fullyPaid;
    }

    public void setFullyPaid(boolean fullyPaid) {
        this.fullyPaid = fullyPaid;
    }

    public boolean isCanBeCancelled() {
        return canBeCancelled;
    }

    public void setCanBeCancelled(boolean canBeCancelled) {
        this.canBeCancelled = canBeCancelled;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", paidAmount=" + paidAmount +
                ", status='" + status + '\'' +
                ", fullyPaid=" + fullyPaid +
                ", canBeCancelled=" + canBeCancelled +
                ", orderDetails=" + (orderDetails != null ? orderDetails.size() + " items" : "null") +
                '}';
    }
}