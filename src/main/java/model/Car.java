package model;

import java.io.Serializable;
import java.util.List;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    // Basic car properties
    private int id;
    private int brandId;
    private String model;
    private double price;
    private String status;
    private String description;
    private Integer year;
    private String color;
    private int stock;

    // Related data
    private String brandName;
    private String imageUrl;
    private List<CarImage> images;

    // Promotion discount properties (từ CarPromotion)
    private double discountPercentage = 0;
    private double discountAmount = 0;

    // =============================================
    // CONSTRUCTORS
    // =============================================

    public Car() {
    }

    public Car(int id, String model, double price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    // =============================================
    // BASIC GETTERS AND SETTERS
    // =============================================

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

    // Alias methods for getName/setName (backward compatibility)
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

    // =============================================
    // DISCOUNT GETTERS/SETTERS
    // =============================================

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    // =============================================
    // DISCOUNT CALCULATION METHODS
    // =============================================

    /**
     * Check if this car has any discount applied
     * @return true if car has percentage or amount discount
     */
    public boolean hasDiscount() {
        return discountPercentage > 0 || discountAmount > 0;
    }

    /**
     * Calculate the final price after discount
     * Priority: percentage discount > amount discount
     * @return discounted price (never negative)
     */
    public double getDiscountedPrice() {
        if (discountPercentage > 0) {
            return price * (1 - discountPercentage / 100);
        } else if (discountAmount > 0) {
            return Math.max(0, price - discountAmount);
        }
        return price;
    }

    /**
     * Calculate the discount value (amount saved)
     * @return amount of money saved with discount
     */
    public double getDiscountValue() {
        if (discountPercentage > 0) {
            return price * (discountPercentage / 100);
        } else if (discountAmount > 0) {
            return Math.min(discountAmount, price); // Don't exceed original price
        }
        return 0;
    }

    /**
     * Get discount percentage for display (even if using amount discount)
     * @return discount as percentage
     */
    public double getEffectiveDiscountPercentage() {
        if (discountPercentage > 0) {
            return discountPercentage;
        } else if (discountAmount > 0 && price > 0) {
            return (discountAmount / price) * 100;
        }
        return 0;
    }

    /**
     * Get human-readable discount display text
     * @return formatted discount string (e.g., "Giảm 10%" or "Giảm 500,000₫")
     */
    public String getDiscountDisplay() {
        if (discountPercentage > 0) {
            return String.format("Giảm %.0f%%", discountPercentage);
        } else if (discountAmount > 0) {
            return String.format("Giảm %,.0f₫", discountAmount);
        }
        return "";
    }

    /**
     * Check if discount is percentage-based
     * @return true if using percentage discount
     */
    public boolean isPercentageDiscount() {
        return discountPercentage > 0;
    }

    /**
     * Check if discount is amount-based
     * @return true if using fixed amount discount
     */
    public boolean isAmountDiscount() {
        return discountAmount > 0 && discountPercentage == 0;
    }

    /**
     * Calculate savings amount in currency
     * @return the amount of money saved
     */
    public double getSavings() {
        return getDiscountValue();
    }

    // =============================================
    // CONVENIENCE METHODS
    // =============================================

    /**
     * Check if car is available for purchase
     */
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status) && stock > 0;
    }

    /**
     * Check if car is unavailable
     */
    public boolean isUnavailable() {
        return "UNAVAILABLE".equalsIgnoreCase(status) || stock <= 0;
    }

    /**
     * Check if car has low stock (≤ 5 items)
     */
    public boolean hasLowStock() {
        return stock > 0 && stock <= 5;
    }

    /**
     * Check if car is out of stock
     */
    public boolean isOutOfStock() {
        return stock <= 0;
    }

    /**
     * Get formatted price string
     */
    public String getFormattedPrice() {
        return String.format("%,.0f ₫", price);
    }

    /**
     * Get formatted discounted price string
     */
    public String getFormattedDiscountedPrice() {
        return String.format("%,.0f ₫", getDiscountedPrice());
    }

    /**
     * Get formatted savings string
     */
    public String getFormattedSavings() {
        return String.format("%,.0f ₫", getSavings());
    }

    /**
     * Get full name (brand + model)
     */
    public String getFullName() {
        return brandName != null ? brandName + " " + model : model;
    }

    /**
     * Get year or "N/A" if null
     */
    public String getYearDisplay() {
        return year != null ? String.valueOf(year) : "N/A";
    }

    /**
     * Get color or "N/A" if null
     */
    public String getColorDisplay() {
        return color != null && !color.isEmpty() ? color : "N/A";
    }

    /**
     * Get primary image URL or placeholder
     */
    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            for (CarImage img : images) {
                if (img.isMainImage()) {
                    return img.getImageURL();
                }
            }
            return images.get(0).getImageURL(); // Return first if no primary
        }
        return imageUrl != null ? imageUrl : "https://via.placeholder.com/400x300?text=No+Image";
    }

    /**
     * Get stock status message
     */
    public String getStockStatusMessage() {
        if (stock <= 0) {
            return "Hết hàng";
        } else if (stock <= 5) {
            return "Chỉ còn " + stock + " xe";
        } else {
            return "Còn " + stock + " xe";
        }
    }

    /**
     * Get stock status CSS class
     */
    public String getStockStatusClass() {
        if (stock <= 0) {
            return "out-of-stock";
        } else if (stock <= 5) {
            return "low-stock";
        } else {
            return "in-stock";
        }
    }

    // =============================================
    // COMPARISON METHODS
    // =============================================

    /**
     * Compare discount value with another car
     */
    public boolean hasHigherDiscountThan(Car other) {
        if (other == null) return this.hasDiscount();
        return this.getDiscountValue() > other.getDiscountValue();
    }

    /**
     * Compare final price with another car
     */
    public boolean isCheaperThan(Car other) {
        if (other == null) return false;
        return this.getDiscountedPrice() < other.getDiscountedPrice();
    }

    // =============================================
    // OBJECT METHODS
    // =============================================

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", discountedPrice=" + getDiscountedPrice() +
                ", status='" + status + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", stock=" + stock +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", hasDiscount=" + hasDiscount() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // =============================================
    // BUILDER PATTERN (Optional - for easier object creation)
    // =============================================

    public static class Builder {
        private Car car;

        public Builder() {
            car = new Car();
        }

        public Builder id(int id) {
            car.id = id;
            return this;
        }

        public Builder brandId(int brandId) {
            car.brandId = brandId;
            return this;
        }

        public Builder brandName(String brandName) {
            car.brandName = brandName;
            return this;
        }

        public Builder model(String model) {
            car.model = model;
            return this;
        }

        public Builder price(double price) {
            car.price = price;
            return this;
        }

        public Builder status(String status) {
            car.status = status;
            return this;
        }

        public Builder description(String description) {
            car.description = description;
            return this;
        }

        public Builder year(Integer year) {
            car.year = year;
            return this;
        }

        public Builder color(String color) {
            car.color = color;
            return this;
        }

        public Builder stock(int stock) {
            car.stock = stock;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            car.imageUrl = imageUrl;
            return this;
        }

        public Builder discountPercentage(double discountPercentage) {
            car.discountPercentage = discountPercentage;
            return this;
        }

        public Builder discountAmount(double discountAmount) {
            car.discountAmount = discountAmount;
            return this;
        }

        public Car build() {
            return car;
        }
    }
}