package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int userId;
    private String status;  // PENDING, APPROVED, CANCELLED, COMPLETED
    private Timestamp createdAt;

    // Additional fields for display
    private List<OrderDetail> orderDetails;
    private List<Transaction> transactions;
    private double totalAmount;
    private double paidAmount;
    private double remainingAmount;
    private User user;  // For admin view

    // Constructors
    public Order() {
    }

    public Order(int userId, String status) {
        this.userId = userId;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
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

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Convenience methods
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }

    public boolean canBeCancelled() {
        return isPending() || isApproved();
    }

    public boolean canBeCompleted() {
        return isApproved();
    }

    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }

    public String getStatusDisplay() {
        switch (status != null ? status.toUpperCase() : "") {
            case "PENDING":
                return "Chờ xử lý";
            case "APPROVED":
                return "Đã duyệt";
            case "CANCELLED":
                return "Đã hủy";
            case "COMPLETED":
                return "Hoàn thành";
            default:
                return status;
        }
    }

    public String getStatusColor() {
        switch (status != null ? status.toUpperCase() : "") {
            case "PENDING":
                return "warning";
            case "APPROVED":
                return "info";
            case "CANCELLED":
                return "danger";
            case "COMPLETED":
                return "success";
            default:
                return "secondary";
        }
    }

    public String getFormattedTotal() {
        return String.format("%,.0f ₫", totalAmount);
    }

    public String getFormattedPaid() {
        return String.format("%,.0f ₫", paidAmount);
    }

    public String getFormattedRemaining() {
        return String.format("%,.0f ₫", remainingAmount);
    }

    public int getTotalItems() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return 0;
        }
        return orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}