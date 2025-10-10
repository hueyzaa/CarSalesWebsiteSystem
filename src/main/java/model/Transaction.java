package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int orderId;
    private double amount;
    private String type;  // FULL, DEPOSIT, or SHOWROOM
    private String paymentStatus;  // PENDING, PAID, CANCELLED
    private Timestamp createdAt;

    // Enums for type safety
    public enum TransactionType {
        FULL, DEPOSIT, SHOWROOM
    }

    public enum PaymentStatus {
        PENDING, PAID, CANCELLED
    }

    // Constructors
    public Transaction() {
        this.paymentStatus = PaymentStatus.PENDING.name();
    }

    public Transaction(int orderId, double amount, String type) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.paymentStatus = PaymentStatus.PENDING.name();
    }

    public Transaction(int orderId, double amount, String type, String paymentStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Convenience methods for type checking
    public boolean isFullPayment() {
        return "FULL".equalsIgnoreCase(type);
    }

    public boolean isDeposit() {
        return "DEPOSIT".equalsIgnoreCase(type);
    }

    public boolean isShowroom() {
        return "SHOWROOM".equalsIgnoreCase(type);
    }

    // Payment status checking
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(paymentStatus);
    }

    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(paymentStatus);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(paymentStatus);
    }

    // Display methods
    public String getFormattedAmount() {
        return String.format("%,.0f ₫", amount);
    }

    public String getTypeDisplay() {
        if (isFullPayment()) {
            return "Thanh toán toàn bộ";
        } else if (isDeposit()) {
            return "Đặt cọc";
        } else if (isShowroom()) {
            return "Thanh toán tại Showroom";
        }
        return type;
    }

    public String getPaymentStatusDisplay() {
        if (isPending()) {
            return "Chờ thanh toán";
        } else if (isPaid()) {
            return "Đã thanh toán";
        } else if (isCancelled()) {
            return "Đã hủy";
        }
        return paymentStatus;
    }

    public String getPaymentStatusColor() {
        if (isPending()) {
            return "warning";
        } else if (isPaid()) {
            return "success";
        } else if (isCancelled()) {
            return "danger";
        }
        return "secondary";
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}