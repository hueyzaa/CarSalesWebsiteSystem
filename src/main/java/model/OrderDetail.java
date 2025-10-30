package model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderDetailId;
    private int orderId;
    private int carId;
    private double price;
    private int quantity;
    private Car car;

    public OrderDetail() {
    }

    public OrderDetail(int orderId, int carId, double price, int quantity) {
        this.orderId = orderId;
        this.carId = carId;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    // Convenience methods
    public double getSubtotal() {
        return price * quantity;
    }

    public String getFormattedPrice() {
        return String.format("%,.0f ₫", price);
    }

    public String getFormattedSubtotal() {
        return String.format("%,.0f ₫", getSubtotal());
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", carId=" + carId +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}