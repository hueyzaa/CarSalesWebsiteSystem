package model;

import java.io.Serializable;
import java.util.List;

/**
 * Car model - Represents a car in the showroom inventory
 * Includes pricing, discount calculations, stock management, and display utilities
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

    private double discountPercentage = 0;
    private double discountAmount = 0;


    /**
     * Default constructor
     */
    public Car() {
    }

    /**
     * Constructor with basic fields
     *
     * @param id Car ID
     * @param model Car model name
     * @param price Car price
     */
    public Car(int id, String model, double price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }


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

    /**
     * Alias for getModel() - backward compatibility
     *
     * @return Car model name
     */
    public String getName() {
        return model;
    }

    /**
     * Alias for setModel() - backward compatibility
     *
     * @param name Car model name
     */
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


    /**
     * Check if this car has any discount applied
     *
     * @return true if car has percentage or amount discount
     */
    public boolean hasDiscount() {
        return discountPercentage > 0 || discountAmount > 0;
    }

    /**
     * Calculate the final price after discount
     * Priority: percentage discount > amount discount
     *
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
     *
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
     *
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
     *
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
     *
     * @return true if using percentage discount
     */
    public boolean isPercentageDiscount() {
        return discountPercentage > 0;
    }

    /**
     * Check if discount is amount-based
     *
     * @return true if using fixed amount discount
     */
    public boolean isAmountDiscount() {
        return discountAmount > 0 && discountPercentage == 0;
    }

    /**
     * Calculate savings amount in currency (alias for getDiscountValue)
     *
     * @return the amount of money saved
     */
    public double getSavings() {
        return getDiscountValue();
    }


    /**
     * Check if car is available for purchase
     *
     * @return true if status is AVAILABLE and stock > 0
     */
    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status) && stock > 0;
    }

    /**
     * Check if car is unavailable
     *
     * @return true if status is UNAVAILABLE or stock is 0
     */
    public boolean isUnavailable() {
        return "UNAVAILABLE".equalsIgnoreCase(status) || stock <= 0;
    }

    /**
     * Check if car has low stock (≤ 5 items)
     *
     * @return true if stock is between 1 and 5
     */
    public boolean hasLowStock() {
        return stock > 0 && stock <= 5;
    }

    /**
     * Check if car is out of stock
     *
     * @return true if stock is 0 or less
     */
    public boolean isOutOfStock() {
        return stock <= 0;
    }


    /**
     * Get formatted price string
     *
     * @return Price formatted as "X,XXX ₫"
     */
    public String getFormattedPrice() {
        return String.format("%,.0f ₫", price);
    }

    /**
     * Get formatted discounted price string
     *
     * @return Discounted price formatted as "X,XXX ₫"
     */
    public String getFormattedDiscountedPrice() {
        return String.format("%,.0f ₫", getDiscountedPrice());
    }

    /**
     * Get formatted savings string
     *
     * @return Savings formatted as "X,XXX ₫"
     */
    public String getFormattedSavings() {
        return String.format("%,.0f ₫", getSavings());
    }

    /**
     * Get full name (brand + model)
     *
     * @return Full car name, e.g. "Toyota Camry"
     */
    public String getFullName() {
        return brandName != null ? brandName + " " + model : model;
    }

    /**
     * Get year or "N/A" if null
     *
     * @return Year as string or "N/A"
     */
    public String getYearDisplay() {
        return year != null ? String.valueOf(year) : "N/A";
    }

    /**
     * Get color or "N/A" if null/empty
     *
     * @return Color name or "N/A"
     */
    public String getColorDisplay() {
        return color != null && !color.isEmpty() ? color : "N/A";
    }

    /**
     * Get primary image URL or placeholder
     * Prioritizes main image, then first image, then imageUrl, then placeholder
     *
     * @return Image URL
     */
    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            // Try to find main image
            for (CarImage img : images) {
                if (img.isMainImage()) {
                    return img.getImageURL();
                }
            }
            // Return first image if no main image
            return images.get(0).getImageURL();
        }
        // Fallback to imageUrl or placeholder
        return imageUrl != null ? imageUrl : "https://via.placeholder.com/400x300?text=No+Image";
    }

    /**
     * Get stock status message in Vietnamese
     *
     * @return Stock status message
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
     * Get CSS class for stock status
     *
     * @return CSS class name
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


    /**
     * Compare discount value with another car
     *
     * @param other Car to compare with
     * @return true if this car has higher discount
     */
    public boolean hasHigherDiscountThan(Car other) {
        if (other == null) return this.hasDiscount();
        return this.getDiscountValue() > other.getDiscountValue();
    }

    /**
     * Compare final price with another car
     *
     * @param other Car to compare with
     * @return true if this car is cheaper
     */
    public boolean isCheaperThan(Car other) {
        if (other == null) return false;
        return this.getDiscountedPrice() < other.getDiscountedPrice();
    }


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


    /**
     * Builder class for creating Car instances
     * Provides fluent API for car construction
     */
    public static class Builder {
        private final Car car;

        /**
         * Create a new Builder
         */
        public Builder() {
            car = new Car();
        }

        /**
         * Set car ID
         *
         * @param id Car ID
         * @return Builder instance
         */
        public Builder id(int id) {
            car.id = id;
            return this;
        }

        /**
         * Set brand ID
         *
         * @param brandId Brand ID
         * @return Builder instance
         */
        public Builder brandId(int brandId) {
            car.brandId = brandId;
            return this;
        }

        /**
         * Set brand name
         *
         * @param brandName Brand name
         * @return Builder instance
         */
        public Builder brandName(String brandName) {
            car.brandName = brandName;
            return this;
        }

        /**
         * Set car model
         *
         * @param model Car model
         * @return Builder instance
         */
        public Builder model(String model) {
            car.model = model;
            return this;
        }

        /**
         * Set price
         *
         * @param price Car price
         * @return Builder instance
         */
        public Builder price(double price) {
            car.price = price;
            return this;
        }

        /**
         * Set status
         *
         * @param status Car status (AVAILABLE, UNAVAILABLE)
         * @return Builder instance
         */
        public Builder status(String status) {
            car.status = status;
            return this;
        }

        /**
         * Set description
         *
         * @param description Car description
         * @return Builder instance
         */
        public Builder description(String description) {
            car.description = description;
            return this;
        }

        /**
         * Set year
         *
         * @param year Manufacturing year
         * @return Builder instance
         */
        public Builder year(Integer year) {
            car.year = year;
            return this;
        }

        /**
         * Set color
         *
         * @param color Car color
         * @return Builder instance
         */
        public Builder color(String color) {
            car.color = color;
            return this;
        }

        /**
         * Set stock quantity
         *
         * @param stock Stock quantity
         * @return Builder instance
         */
        public Builder stock(int stock) {
            car.stock = stock;
            return this;
        }

        /**
         * Set image URL
         *
         * @param imageUrl Main image URL
         * @return Builder instance
         */
        public Builder imageUrl(String imageUrl) {
            car.imageUrl = imageUrl;
            return this;
        }

        /**
         * Set discount percentage
         *
         * @param discountPercentage Discount percentage (0-100)
         * @return Builder instance
         */
        public Builder discountPercentage(double discountPercentage) {
            car.discountPercentage = discountPercentage;
            return this;
        }

        /**
         * Set discount amount
         *
         * @param discountAmount Fixed discount amount
         * @return Builder instance
         */
        public Builder discountAmount(double discountAmount) {
            car.discountAmount = discountAmount;
            return this;
        }

        /**
         * Build and return the Car instance
         *
         * @return Constructed Car object
         */
        public Car build() {
            return car;
        }
    }
}