package model;

import java.math.BigDecimal;

public class CartItem {
    private int cartItemId;
    private int carId;
    private int quantity;
    private String carModel;
    private BigDecimal carPrice;
    private String imageUrl;
    private String brandName;

    // Getters and Setters
    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    public BigDecimal getCarPrice() { return carPrice; }
    public void setCarPrice(BigDecimal carPrice) { this.carPrice = carPrice; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
}