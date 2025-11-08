package dto;

/**
 * CartItemDTO - Data Transfer Object for Cart Item with pre-calculated subtotal
 * Used to pass cart item data with calculated values to JSP views
 * All business logic calculations are done before creating this DTO
 */
public class CartItemDTO {
    // Cart item information
    private int id;           // Cart item ID
    private int cartId;       // Cart ID (not user ID)
    private int carId;
    private int quantity;

    // Car information (simplified)
    private CarWithDiscountDTO car;

    // Pre-calculated values
    private double subtotal;  // price * quantity (pre-calculated)

    // Constructors
    public CartItemDTO() {
    }

    public CartItemDTO(int id, int cartId, int carId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.carId = carId;
        this.quantity = quantity;
    }

    // Getters and Setters
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

    public CarWithDiscountDTO getCar() {
        return car;
    }

    public void setCar(CarWithDiscountDTO car) {
        this.car = car;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", carId=" + carId +
                ", quantity=" + quantity +
                ", car=" + (car != null ? car.getName() : "null") +
                ", subtotal=" + subtotal +
                '}';
    }
}