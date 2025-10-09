package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private int carId;
    private int brandId;
    private String brandName;
    private String model;
    private BigDecimal price;
    private String status;
    private String description;
    private String imageUrl;

    // For JSP compatibility
    private int id;
    private String name;
    private List<CarImage> images = new ArrayList<>();

    public Car() {}

    public Car(int carId, int brandId, String model, BigDecimal price, String status, String description) {
        this.carId = carId;
        this.id = carId;
        this.brandId = brandId;
        this.model = model;
        this.name = model;
        this.price = price;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public int getCarId() { return carId; }
    public void setCarId(int carId) {
        this.carId = carId;
        this.id = carId;
    }

    public int getId() { return carId; }
    public void setId(int id) {
        this.id = id;
        this.carId = id;
    }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getModel() { return model; }
    public void setModel(String model) {
        this.model = model;
        this.name = model;
    }

    public String getName() { return model; }
    public void setName(String name) {
        this.name = name;
        this.model = name;
    }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<CarImage> getImages() { return images; }
    public void setImages(List<CarImage> images) { this.images = images; }

    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }

    public String getFormattedPrice() {
        return String.format("%,d VNĐ", price.longValue());
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", brandName='" + brandName + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}