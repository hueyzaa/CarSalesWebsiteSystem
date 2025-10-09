package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    // Basic fields
    private int id;
    private int brandId;
    private String name;          // model in database
    private double price;
    private int year;
    private String color;
    private String description;
    private int stock;
    private String status;

    // Additional fields (from joins)
    private String brandName;
    private String imageUrl;      // Main/primary image
    private List<CarImage> images; // All images

    // Timestamps
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Car() {
    }

    public Car(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Alias method for backward compatibility
    public int getCarId() {
        return id;
    }

    public void setCarId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Alias for model field
    public String getModel() {
        return name;
    }

    public void setModel(String model) {
        this.name = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CarImage> getImages() {
        return images;
    }

    public void setImages(List<CarImage> images) {
        this.images = images;
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
    public boolean isAvailable() {
        return stock > 0 && "AVAILABLE".equals(status);
    }

    public String getFormattedPrice() {
        return String.format("%,.0f ₫", price);
    }

    public boolean hasStock() {
        return stock > 0;
    }

    public boolean hasEnoughStock(int quantity) {
        return stock >= quantity;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brandName='" + brandName + '\'' +
                ", price=" + price +
                ", year=" + year +
                ", stock=" + stock +
                ", status='" + status + '\'' +
                '}';
    }
}