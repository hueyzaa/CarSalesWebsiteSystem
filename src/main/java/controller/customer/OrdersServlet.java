package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import model.Order;
import model.OrderDetail;
import util.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * OrdersServlet - Display customer's orders
 * Customers can view their own orders with full order details
 */
@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrdersServlet.class);

    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        logger.info("OrdersServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validate user authentication
        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            String statusFilter = request.getParameter("status");

            logger.info("Loading orders for user ID: {}", userId);

            // Load customer's orders
            List<Order> orders = loadOrders(userId, statusFilter);

            // Populate each order with details and calculations
            enrichOrdersWithDetails(orders);

            logger.info("Retrieved {} orders with details for user {}", orders.size(), userId);

            // Set attributes and forward to JSP
            request.setAttribute("orders", orders);
            request.setAttribute("userId", userId);

            if (isNotEmpty(statusFilter)) {
                request.setAttribute("statusFilter", statusFilter);
            }

            forward(request, response, "/WEB-INF/views/Customer/orders.jsp");

        } catch (RuntimeException e) {
            logger.error("Database error loading orders for user {}", userId, e);
            handleError(request, response, "Cannot load orders list.");
        } catch (Exception e) {
            logger.error("Unexpected error loading orders for user {}", userId, e);
            handleError(request, response, "An unexpected error occurred.");
        }
    }

    // ============ PRIVATE METHODS ============

    /**
     * Validate user authentication
     * @return userId if authenticated, null otherwise
     */
    private Integer validateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated access attempt to orders page");
            redirect(response, request.getContextPath() + "/login");
            return null;
        }

        return SessionUtils.getUserId(session);
    }

    /**
     * Load orders for customer with optional status filter
     */
    private List<Order> loadOrders(Integer userId, String statusFilter) {
        if (isNotEmpty(statusFilter)) {
            logger.debug("Filtering orders by status: {}", statusFilter);

            // Get all orders with status, then filter by userId
            List<Order> orders = ordersDAO.getOrdersByStatus(statusFilter);
            orders.removeIf(order -> order.getUserId() != userId);

            logger.debug("Found {} orders with status '{}' for user {}",
                    orders.size(), statusFilter, userId);

            return orders;
        }

        return ordersDAO.getOrdersByUserId(userId);
    }

    /**
     * Enrich orders with order details and calculated values
     */
    private void enrichOrdersWithDetails(List<Order> orders) {
        for (Order order : orders) {
            // Load order details (includes car info and images)
            List<OrderDetail> orderDetails =
                    orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId());
            order.setOrderDetails(orderDetails);

            // Calculate total amount
            double totalAmount = ordersDAO.getOrderTotal(order.getOrderId());
            order.setTotalAmount(totalAmount);

            // Calculate paid amount
            double paidAmount = calculatePaidAmount(order, totalAmount);
            order.setPaidAmount(paidAmount);

            logger.debug("Order {}: {} items, total: {}, paid: {}, remaining: {}",
                    order.getOrderId(),
                    orderDetails.size(),
                    totalAmount,
                    paidAmount,
                    order.getRemainingAmount());
        }
    }

    /**
     * Calculate paid amount for order
     * paidAmount = totalAmount - remainingAmount
     */
    private double calculatePaidAmount(Order order, double totalAmount) {
        if (order.getRemainingAmount() != null) {
            return totalAmount - order.getRemainingAmount();
        }
        // If no remaining amount, assume fully paid
        return totalAmount;
    }

    // ============ UTILITY METHODS ============

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        forward(request, response, "/WEB-INF/views/Customer/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}