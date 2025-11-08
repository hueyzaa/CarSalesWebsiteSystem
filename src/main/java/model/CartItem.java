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

    public CartItem() {}

    public CartItem(int id, int cartId, int carId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.carId = carId;
        this.quantity = quantity;
    }

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
}