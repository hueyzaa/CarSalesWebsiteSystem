package service;

import dto.OrderDTO;
import dto.OrderDetailDTO;
import model.Order;
import model.OrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderService - Business logic for orders
 * Handles Order to OrderDTO conversion with pre-calculated values
 */
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0");

    /**
     * Convert Order to OrderDTO with pre-calculated values
     */
    public OrderDTO toOrderDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();

        // Basic information
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaidAmount(order.getPaidAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentType(order.getPaymentType());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());

        // Convert List<OrderDetail> to List<OrderDetailDTO>
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            List<OrderDetailDTO> detailDTOs = order.getOrderDetails().stream()
                    .map(this::toOrderDetailDTO)
                    .collect(Collectors.toList());
            dto.setOrderDetails(detailDTOs);

            logger.debug("Converted {} order details to DTOs for order {}",
                    detailDTOs.size(), order.getOrderId());
        }

        // Transactions remain as Model
        dto.setTransactions(order.getTransactions());

        // Pre-calculate display values
        dto.setStatusDisplay(getOrderStatusDisplay(order.getStatus()));
        dto.setStatusColor(getStatusColor(order.getStatus()));
        dto.setPaymentTypeDisplay(getPaymentTypeDisplay(order.getPaymentType()));
        dto.setFormattedTotal(formatCurrency(order.getTotalAmount()));

        // Pre-calculate amounts
        if ("DEPOSIT".equals(order.getPaymentType())) {
            double depositAmount = order.getTotalAmount() * 0.10;
            dto.setDepositAmount(depositAmount);
        }

        double remaining = order.getTotalAmount() - order.getPaidAmount();
        dto.setRemainingAmount(remaining > 0 ? remaining : 0);

        // Pre-calculate flags
        dto.setFullyPaid(isOrderFullyPaid(order));
        dto.setCanBeCancelled(canOrderBeCancelled(order));

        // Compute total items
        int totalItems = 0;
        if (order.getOrderDetails() != null) {
            totalItems = order.getOrderDetails().stream()
                    .mapToInt(OrderDetail::getQuantity)
                    .sum();
        }
        dto.setTotalItems(totalItems);

        logger.debug("Converted Order {} to DTO - fullyPaid: {}, canCancel: {}, details: {}",
                order.getOrderId(), dto.isFullyPaid(), dto.isCanBeCancelled(),
                dto.getOrderDetails() != null ? dto.getOrderDetails().size() : 0);

        return dto;
    }

    /**
     * Convert OrderDetail (Model) to OrderDetailDTO
     */
    private OrderDetailDTO toOrderDetailDTO(OrderDetail detail) {
        if (detail == null) {
            return null;
        }

        OrderDetailDTO dto = new OrderDetailDTO();

        dto.setOrderDetailId(detail.getOrderDetailId());
        dto.setOrderId(detail.getOrderId());
        dto.setCarId(detail.getCarId());
        dto.setPrice(detail.getPrice());
        dto.setQuantity(detail.getQuantity());
        dto.setCar(detail.getCar());

        // Calculate subtotal
        double subtotal = detail.getPrice() * detail.getQuantity();
        dto.setSubtotal(subtotal);

        logger.trace("Converted OrderDetail {} - price: {}, qty: {}, subtotal: {}",
                detail.getOrderDetailId(), detail.getPrice(), detail.getQuantity(), subtotal);

        return dto;
    }

    // ============ BUSINESS LOGIC ============

    /**
     * Check if order is fully paid
     */
    private boolean isOrderFullyPaid(Order order) {
        return order.getPaidAmount() >= order.getTotalAmount();
    }

    private boolean canOrderBeCancelled(Order order) {
        if (order == null || order.getStatus() == null) {
            return false;
        }
        String status = order.getStatus().toUpperCase();

        // Allow cancellation for PENDING or APPROVED orders
        boolean isValidStatus = "PENDING".equals(status) || "APPROVED".equals(status);

        // TEMPORARY: Allow cancel even if has payment
        // TODO: Should check hasNoPayment and implement refund flow

        logger.trace("canOrderBeCancelled - order: {}, status: {}, validStatus: {}, paidAmount: {}, result: {}",
                order.getOrderId(), status, isValidStatus, order.getPaidAmount(), isValidStatus);

        return isValidStatus;
    }

    /**
     * Get display text for order status
     */
    private String getOrderStatusDisplay(String status) {
        if (status == null) return "Không xác định";

        switch (status) {
            case "PENDING":
                return "Chờ xử lý";
            case "APPROVED":
                return "Đã duyệt";
            case "COMPLETED":
                return "Hoàn thành";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return status;
        }
    }

    /**
     * Get CSS class for status badge color
     */
    private String getStatusColor(String status) {
        if (status == null) return "pending";

        switch (status) {
            case "PENDING":
                return "pending";
            case "APPROVED":
                return "approved";
            case "COMPLETED":
                return "completed";
            case "CANCELLED":
                return "cancelled";
            default:
                return "pending";
        }
    }

    /**
     * Get display text for payment type
     */
    private String getPaymentTypeDisplay(String paymentType) {
        if (paymentType == null) return "Không xác định";

        switch (paymentType) {
            case "DEPOSIT":
                return "Đặt cọc 10%";
            case "SHOWROOM":
                return "Thanh toán tại showroom";
            default:
                return paymentType;
        }
    }

    /**
     * Format currency value
     */
    private String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount) + " ₫";
    }
}