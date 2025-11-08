package model;

import java.io.Serializable;
import java.util.Date;

public class CarImage implements Serializable {
    private static final long serialVersionUID = 1L;

    private int imageId;
    private int carId;
    private String imageURL;
    private boolean mainImage;
    private Date createdAt;

    public CarImage() {}

    public CarImage(int imageId, int carId, String imageURL, boolean mainImage) {
        this.imageId = imageId;
        this.carId = carId;
        this.imageURL = imageURL;
        this.mainImage = mainImage;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isMainImage() {
        return mainImage;
    }

    public void setMainImage(boolean mainImage) {
        this.mainImage = mainImage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}