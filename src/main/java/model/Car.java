package model;

import java.io.Serializable;
import java.util.List;

/**
 * Car Model - PURE DATA ONLY
 * Mapped to database Car table
 * @author Nguyen Gia Huy
 * @version 2.0 - Fixed to match database
 */
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int brandId;
    private String model;
    private double price;
    private String status;
    private String description;
    private Integer year;
    private String color;
    private int stock;
    private String brandName;
    private String imageUrl;
    private List<CarImage> images;


    // ============ CONSTRUCTORS ============

    public Car() {
    }

    public Car(int id, String model, double price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    // ============ GETTERS & SETTERS ============

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
}