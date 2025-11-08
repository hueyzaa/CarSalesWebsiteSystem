package dto;

/**
 * CarWithDiscountDTO - Data Transfer Object for Car with pre-calculated discount values
 * Used to pass car data with discount information to JSP views
 * All business logic calculations are done before creating this DTO
 */
public class CarWithDiscountDTO {
    // Basic car information
    private int carId;
    private String name;
    private String brandName;
    private int year;
    private String color;
    private double price;
    private String status;
    private int quantity;
    private String imageUrl;  // ADD THIS

    // Discount information (pre-calculated)
    private boolean hasDiscount;
    private double discountPercentage;
    private double discountAmount;
    private double discountedPrice;
    private double discountValue;

    // Constructors
    public CarWithDiscountDTO() {
    }

    public CarWithDiscountDTO(int carId, String name, String brandName, int year,
                              String color, double price) {
        this.carId = carId;
        this.name = name;
        this.brandName = brandName;
        this.year = year;
        this.color = color;
        this.price = price;
    }

    // Getters and Setters
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
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

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public String toString() {
        return "CarWithDiscountDTO{" +
                "carId=" + carId +
                ", name='" + name + '\'' +
                ", brandName='" + brandName + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", hasDiscount=" + hasDiscount +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", discountedPrice=" + discountedPrice +
                ", discountValue=" + discountValue +
                '}';
    }
}