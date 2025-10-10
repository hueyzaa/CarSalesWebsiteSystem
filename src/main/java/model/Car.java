package model;

import java.io.Serializable;
import java.util.List;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;  // car_id
    private int brandId;
    private String model;  // model name from database
    private double price;
    private String status;
    private String description;
    private Integer year;
    private String color;
    private int stock;

    // Additional fields for display
    private String brandName;
    private String imageUrl;
    private List<CarImage> images;

    // Constructors
    public Car() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // ✅ CRITICAL: Alias methods for getName/setName to work with DAO code
    public String getName() {
        return model;
    }

    public void setName(String name) {
        this.model = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    // Convenience methods
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status) && stock > 0;
    }

    public boolean isUnavailable() {
        return "UNAVAILABLE".equalsIgnoreCase(status) || stock <= 0;
    }

    public String getFormattedPrice() {
        return String.format("%,.0f ₫", price);
    }

    public String getFullName() {
        return brandName != null ? brandName + " " + model : model;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", stock=" + stock +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}