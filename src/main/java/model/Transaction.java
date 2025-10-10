package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int orderId;
    private double amount;
    private String type;  // FULL or DEPOSIT
    private Timestamp createdAt;

    // Constructors
    public Transaction() {
    }

    public Transaction(int orderId, double amount, String type) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Convenience methods
    public boolean isFullPayment() {
        return "FULL".equalsIgnoreCase(type);
    }

    public boolean isDeposit() {
        return "DEPOSIT".equalsIgnoreCase(type);
    }

    public String getFormattedAmount() {
        return String.format("%,.0f ₫", amount);
    }

    public String getTypeDisplay() {
        if (isFullPayment()) {
            return "Thanh toán toàn bộ";
        } else if (isDeposit()) {
            return "Đặt cọc";
        }
        return type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}