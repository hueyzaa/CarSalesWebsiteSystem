package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order model - Represents a customer order in the system
 * Manages order status, payment tracking, and order details
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;


    private int orderId;
    private int userId;
    private String status;
    private Timestamp createdAt;

    private String paymentType;
    private Double depositAmount;
    private Double remainingAmount;
    private String notes;

    private List<OrderDetail> orderDetails;
    private List<Transaction> transactions;
    private double totalAmount;
    private double paidAmount;
    private User user;


    /**
     * Default constructor
     */
    public Order() {
    }

    /**
     * Constructor with basic fields
     *
     * @param userId User ID who placed the order
     * @param status Order status
     */
    public Order(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    /**
     * Constructor with payment type
     *
     * @param userId User ID who placed the order
     * @param status Order status
     * @param paymentType Payment type (FULL, DEPOSIT, SHOWROOM)
     */
    public Order(int userId, String status, String paymentType) {
        this.userId = userId;
        this.status = status;
        this.paymentType = paymentType;
    }


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


    /**
     * Calculate remaining amount based on total and paid
     *
     * @return Remaining amount (never negative)
     */
    public double getCalculatedRemainingAmount() {
        return Math.max(0, totalAmount - paidAmount);
    }


    /**
     * Check if order is pending
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    /**
     * Check if order is approved
     *
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(status);
    }

    /**
     * Check if order is cancelled
     *
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }

    /**
     * Check if order is completed
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }

    /**
     * Check if order can be cancelled
     * Only PENDING or APPROVED orders can be cancelled
     *
     * @return true if order can be cancelled
     */
    public boolean canBeCancelled() {
        return isPending() || isApproved();
    }

    /**
     * Check if order can be completed
     * Only APPROVED orders can be completed
     *
     * @return true if order can be completed
     */
    public boolean canBeCompleted() {
        return isApproved();
    }


    /**
     * Check if payment type is FULL
     *
     * @return true if payment type is FULL
     */
    public boolean isFullPayment() {
        return "FULL".equalsIgnoreCase(paymentType);
    }

    /**
     * Check if payment type is DEPOSIT
     *
     * @return true if payment type is DEPOSIT
     */
    public boolean isDepositPayment() {
        return "DEPOSIT".equalsIgnoreCase(paymentType);
    }

    /**
     * Check if payment type is SHOWROOM
     *
     * @return true if payment type is SHOWROOM
     */
    public boolean isShowroomPayment() {
        return "SHOWROOM".equalsIgnoreCase(paymentType);
    }

    /**
     * Check if order is fully paid
     *
     * @return true if paid amount >= total amount
     */
    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }

    /**
     * Check if order requires payment
     *
     * @return true if order is not fully paid
     */
    public boolean requiresPayment() {
        return !isFullyPaid();
    }


    /**
     * Get Vietnamese display text for order status
     *
     * @return Order status in Vietnamese
     */
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

    /**
     * Get Bootstrap color class for order status
     *
     * @return CSS color class
     */
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

    /**
     * Get Vietnamese display text for payment type
     *
     * @return Payment type in Vietnamese
     */
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
     * Get payment status display text based on payment type and amounts
     *
     * @return Payment status in Vietnamese
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
     * Get Bootstrap color class for payment status
     *
     * @return CSS color class
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

    /**
     * Get formatted total amount
     *
     * @return Total formatted as "X,XXX ₫"
     */
    public String getFormattedTotal() {
        return String.format("%,.0f ₫", totalAmount);
    }

    /**
     * Get formatted paid amount
     *
     * @return Paid amount formatted as "X,XXX ₫"
     */
    public String getFormattedPaid() {
        return String.format("%,.0f ₫", paidAmount);
    }

    /**
     * Get formatted remaining amount
     *
     * @return Remaining amount formatted as "X,XXX ₫"
     */
    public String getFormattedRemaining() {
        double remaining = remainingAmount != null && remainingAmount > 0
                ? remainingAmount
                : getCalculatedRemainingAmount();
        return String.format("%,.0f ₫", remaining);
    }

    /**
     * Get formatted deposit amount
     *
     * @return Deposit amount formatted as "X,XXX ₫" or "N/A"
     */
    public String getFormattedDepositAmount() {
        return depositAmount != null ? String.format("%,.0f ₫", depositAmount) : "N/A";
    }

    /**
     * Get total number of items in order
     *
     * @return Sum of quantities in order details
     */
    public int getTotalItems() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return 0;
        }
        return orderDetails.stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}