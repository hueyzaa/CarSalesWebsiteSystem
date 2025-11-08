package dto;

import model.Car;

/**
 * OrderDetailDTO - Data Transfer Object for Order Detail with pre-calculated values
 * Used to pass order detail data with computed subtotal to JSP views
 * All business logic calculations are done before creating this DTO
 *
 * Key difference from Model:
 * - Model (OrderDetail): Raw data from database, no calculations
 * - DTO (OrderDetailDTO): Includes pre-calculated values like subtotal
 */
public class OrderDetailDTO {
    // Basic order detail information (from Model)
    private int orderDetailId;
    private int orderId;
    private int carId;
    private double price;
    private int quantity;

    // Car information (can be Car object or CarDTO)
    private Car car;  // Keep as Car for simplicity, can change to CarDTO if needed


    private double subtotal;  // = price * quantity (calculated in Service)

    // Constructors
    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int orderDetailId, int orderId, int carId,
                          double price, int quantity) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.carId = carId;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
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
        return "OrderDetailDTO{" +
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", carId=" + carId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", car=" + (car != null ? car.getName() : "null") +
                '}';
    }
}