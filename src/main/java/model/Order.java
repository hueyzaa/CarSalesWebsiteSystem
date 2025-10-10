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

    // Payment-related fields
    private String paymentType;  // FULL, DEPOSIT, SHOWROOM
    private Double depositAmount;
    private Double remainingAmount;
    private String notes;

    // Additional fields for display
    private List<OrderDetail> orderDetails;
    private List<Transaction> transactions;
    private double totalAmount;
    private double paidAmount;
    private User user;  // For admin view

    // Constructors
    public Order() {
    }

    public Order(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public Order(int userId, String status, String paymentType) {
        this.userId = userId;
        this.status = status;
        this.paymentType = paymentType;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Calculated remaining amount based on total and paid
    public double getCalculatedRemainingAmount() {
        return Math.max(0, totalAmount - paidAmount);
    }

    // Status convenience methods
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

    // Payment type convenience methods
    public boolean isFullPayment() {
        return "FULL".equalsIgnoreCase(paymentType);
    }

    public boolean isDepositPayment() {
        return "DEPOSIT".equalsIgnoreCase(paymentType);
    }

    public boolean isShowroomPayment() {
        return "SHOWROOM".equalsIgnoreCase(paymentType);
    }

    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }

    public boolean requiresPayment() {
        return !isFullyPaid();
    }

    // Display methods
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
                return "pending";
            case "APPROVED":
                return "approved";
            case "CANCELLED":
                return "cancelled";
            case "COMPLETED":
                return "completed";
            default:
                return "secondary";
        }
    }

    public String getPaymentTypeDisplay() {
        if (isFullPayment()) {
            return "Thanh toán toàn bộ";
        } else if (isDepositPayment()) {
            return "Đặt cọc";
        } else if (isShowroomPayment()) {
            return "Thanh toán tại Showroom";
        }
        return paymentType != null ? paymentType : "N/A";
    }

    /**
     * Get payment status display text
     */
    public String getPaymentStatusDisplay() {
        if (isFullPayment()) {
            // FULL payment
            if (paidAmount >= totalAmount) {
                return "Đã thanh toán toàn bộ";
            } else {
                return "Chờ thanh toán toàn bộ";
            }
        } else if (isDepositPayment()) {
            // DEPOSIT payment
            if (paidAmount >= totalAmount) {
                return "Đã thanh toán toàn bộ";
            } else if (paidAmount > 0) {
                return "Đã đặt cọc - Còn nợ";
            } else {
                return "Chờ thanh toán đặt cọc";
            }
        } else if (isShowroomPayment()) {
            // SHOWROOM payment
            if (paidAmount > 0) {
                return "Đã thanh toán";
            } else {
                return "Chưa thanh toán - Thanh toán tại showroom";
            }
        }
        return "Chưa xác định";
    }

    /**
     * Get payment status color
     */
    public String getPaymentStatusColor() {
        if (isFullyPaid()) {
            return "success";  // Green
        } else if (paidAmount > 0) {
            return "warning";  // Orange/Yellow
        } else {
            return "danger";   // Red
        }
    }

    public String getFormattedTotal() {
        return String.format("%,.0f ₫", totalAmount);
    }

    public String getFormattedPaid() {
        return String.format("%,.0f ₫", paidAmount);
    }

    public String getFormattedRemaining() {
        double remaining = remainingAmount != null && remainingAmount > 0 ? remainingAmount : getCalculatedRemainingAmount();
        return String.format("%,.0f ₫", remaining);
    }

    public String getFormattedDepositAmount() {
        return depositAmount != null ? String.format("%,.0f ₫", depositAmount) : "N/A";
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
                ", paymentType='" + paymentType + '\'' +
                ", totalAmount=" + totalAmount +
                ", paidAmount=" + paidAmount +
                ", remainingAmount=" + remainingAmount +
                ", depositAmount=" + depositAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}