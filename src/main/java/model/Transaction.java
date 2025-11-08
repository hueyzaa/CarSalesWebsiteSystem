package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int orderId;
    private double amount;
    private String type;
    private String paymentStatus;
    private Timestamp createdAt;

    public Transaction() {}

    public Transaction(int orderId, double amount, String type) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(int orderId, double amount, String type, String paymentStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.paymentStatus = paymentStatus;
    }

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
}