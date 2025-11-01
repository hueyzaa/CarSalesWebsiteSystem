package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Transaction model - Represents a payment transaction for an order
 * Tracks payment type, status, and transaction details
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private int orderId;
    private double amount;
    private String type;  // FULL, DEPOSIT, or SHOWROOM
    private String paymentStatus;  // PENDING, PAID, CANCELLED
    private Timestamp createdAt;


    /**
     * Transaction type enumeration
     */
    public enum TransactionType {
        /** Full payment transaction */
        FULL,
        /** Deposit payment transaction */
        DEPOSIT,
        /** Showroom payment transaction */
        SHOWROOM
    }

    /**
     * Payment status enumeration
     */
    public enum PaymentStatus {
        /** Payment is pending */
        PENDING,
        /** Payment is completed */
        PAID,
        /** Payment is cancelled */
        CANCELLED
    }


    /**
     * Default constructor - sets status to PENDING
     */
    public Transaction() {
        this.paymentStatus = PaymentStatus.PENDING.name();
    }

    /**
     * Constructor with basic fields - sets status to PENDING
     *
     * @param orderId Order ID
     * @param amount Transaction amount
     * @param type Transaction type (FULL, DEPOSIT, SHOWROOM)
     */
    public Transaction(int orderId, double amount, String type) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.paymentStatus = PaymentStatus.PENDING.name();
    }

    /**
     * Constructor with all fields
     *
     * @param orderId Order ID
     * @param amount Transaction amount
     * @param type Transaction type (FULL, DEPOSIT, SHOWROOM)
     * @param paymentStatus Payment status (PENDING, PAID, CANCELLED)
     */
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


    /**
     * Check if transaction is full payment type
     *
     * @return true if type is FULL
     */
    public boolean isFullPayment() {
        return "FULL".equalsIgnoreCase(type);
    }

    /**
     * Check if transaction is deposit type
     *
     * @return true if type is DEPOSIT
     */
    public boolean isDeposit() {
        return "DEPOSIT".equalsIgnoreCase(type);
    }

    /**
     * Check if transaction is showroom payment type
     *
     * @return true if type is SHOWROOM
     */
    public boolean isShowroom() {
        return "SHOWROOM".equalsIgnoreCase(type);
    }


    /**
     * Check if payment is pending
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(paymentStatus);
    }

    /**
     * Check if payment is completed
     *
     * @return true if status is PAID
     */
    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(paymentStatus);
    }

    /**
     * Check if payment is cancelled
     *
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(paymentStatus);
    }


    /**
     * Get formatted amount string
     *
     * @return Amount formatted as "X,XXX ₫"
     */
    public String getFormattedAmount() {
        return String.format("%,.0f ₫", amount);
    }

    /**
     * Get Vietnamese display text for transaction type
     *
     * @return Transaction type in Vietnamese
     */
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

    /**
     * Get Vietnamese display text for payment status
     *
     * @return Payment status in Vietnamese
     */
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

    /**
     * Get Bootstrap color class for payment status
     * Used for badge styling
     *
     * @return CSS color class (warning, success, danger)
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(transactionId);
    }
}