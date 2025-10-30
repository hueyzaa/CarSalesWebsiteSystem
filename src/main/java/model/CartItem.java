package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int cartId;
    private int carId;
    private int quantity;
    private Car car;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public CartItem() {
    }

    public CartItem(int id, int cartId, int carId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.carId = carId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Convenience methods
    public double getSubtotal() {
        if (car != null) {
            return car.getPrice() * quantity;
        }
        return 0.0;
    }

    // Legacy setters for backward compatibility
    public void setCartItemId(int id) {
        this.id = id;
    }

    public void setCarModel(String model) {
        if (this.car == null) {
            this.car = new Car();
        }
        this.car.setName(model);
    }

    public void setCarPrice(java.math.BigDecimal price) {
        if (this.car == null) {
            this.car = new Car();
        }
        this.car.setPrice(price.doubleValue());
    }

    public void setImageUrl(String imageUrl) {
        if (this.car == null) {
            this.car = new Car();
        }
        this.car.setImageUrl(imageUrl);
    }

    public void setBrandName(String brandName) {
        if (this.car == null) {
            this.car = new Car();
        }
        this.car.setBrandName(brandName);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", carId=" + carId +
                ", quantity=" + quantity +
                ", car=" + (car != null ? car.getName() : "null") +
                '}';
    }
}