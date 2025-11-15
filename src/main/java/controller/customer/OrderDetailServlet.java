package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.TransactionDAO;
import dao.CustomerDAO;
import dto.OrderDTO;
import model.Order;
import model.Customer;
import service.OrderService;
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

/**
 * OrderDetailServlet - Display detailed order information for customers
 * Customers can only view their own orders
 * Uses OrderDTO with pre-calculated business logic
 */
@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);

    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;
    private OrderService orderService;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
        customerDAO = new CustomerDAO();
        orderService = new OrderService();
        logger.info("OrderDetailServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validate user authentication
        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            // Get and validate order ID parameter
            Integer orderId = getOrderId(request);
            if (orderId == null) {
                logger.warn("Order ID parameter is missing");
                redirectWithError(request, response, "/orders", "Order not found!");
                return;
            }

            logger.info("Loading order {} for user {}", orderId, userId);

            // Load order from database
            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                redirectWithError(request, response, "/orders", "Order does not exist!");
                return;
            }

            // Verify customer owns this order
            if (!isOrderOwner(userId, order)) {
                logger.warn("User {} attempted to access order {} (owner: {})",
                        userId, orderId, order.getUserId());
                redirectWithError(request, response, "/orders",
                        "You do not have permission to view this order!");
                return;
            }

            // Enrich order with complete details
            enrichOrderWithDetails(order);

            // Convert Model to DTO with pre-calculated business logic
            OrderDTO orderDTO = orderService.toOrderDTO(order);

            // Load customer information
            Customer customer = customerDAO.getCustomerById(order.getUserId());

            // Log order summary for debugging
            logOrderSummary(orderId, orderDTO, customer);

            // Set attributes and forward to JSP
            request.setAttribute("order", orderDTO);
            request.setAttribute("customer", customer);
            request.setAttribute("userId", userId);

            forward(request, response, "/WEB-INF/views/Customer/order-detail.jsp");

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            redirectWithError(request, response, "/orders", "Invalid order ID!");
        } catch (RuntimeException e) {
            logger.error("Database error loading order", e);
            handleError(request, response, "Cannot load order information.");
        } catch (Exception e) {
            logger.error("Unexpected error loading order", e);
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
            logger.warn("Unauthenticated access attempt to order detail");
            redirect(response, request.getContextPath() + "/login");
            return null;
        }

        return SessionUtils.getUserId(session);
    }

    /**
     * Get and parse order ID from request parameter
     * @return orderId if valid, null if missing or invalid
     */
    private Integer getOrderId(HttpServletRequest request) {
        String orderIdParam = request.getParameter("id");

        if (isEmpty(orderIdParam)) {
            return null;
        }

        try {
            return Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            logger.error("Cannot parse order ID: {}", orderIdParam, e);
            return null;
        }
    }

    /**
     * Check if user is the owner of the order
     */
    private boolean isOrderOwner(Integer userId, Order order) {
        return order.getUserId() == userId;
    }

    /**
     * Enrich order with order details, transactions, and calculated amounts
     */
    private void enrichOrderWithDetails(Order order) {
        // Load order details (items with car info, images)
        order.setOrderDetails(
                orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId())
        );

        // Load transactions (payment history)
        order.setTransactions(
                transactionDAO.getTransactionsByOrderId(order.getOrderId())
        );

        // Calculate total and paid amounts
        double totalAmount = orderDetailDAO.calculateOrderTotal(order.getOrderId());
        double paidAmount = transactionDAO.getTotalPaidAmount(order.getOrderId());

        order.setTotalAmount(totalAmount);
        order.setPaidAmount(paidAmount);

        logger.debug("Order {} enriched: {} items, {} transactions, total: {}, paid: {}",
                order.getOrderId(),
                order.getOrderDetails().size(),
                order.getTransactions().size(),
                totalAmount,
                paidAmount);
    }

    /**
     * Log order summary for debugging and monitoring
     */
    private void logOrderSummary(int orderId, OrderDTO orderDTO, Customer customer) {
        logger.info("Order {} details loaded successfully:", orderId);
        logger.info("  Customer: {} (ID: {})",
                customer != null ? customer.getName() : "Unknown",
                orderDTO.getUserId());
        logger.info("  Payment: {} | Status: {}",
                orderDTO.getPaymentType(), orderDTO.getStatus());
        logger.info("  Total: {}₫ | Paid: {}₫ | Remaining: {}₫",
                orderDTO.getTotalAmount(),
                orderDTO.getPaidAmount(),
                orderDTO.getRemainingAmount());
        logger.info("  Items: {} | Transactions: {}",
                orderDTO.getOrderDetails().size(),
                orderDTO.getTransactions().size());
        logger.info("  Fully Paid: {} | Can Cancel: {}",
                orderDTO.isFullyPaid(),
                orderDTO.isCanBeCancelled());
    }

    // ============ UTILITY METHODS ============

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response,
                                   String path, String errorMessage) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("error", errorMessage);
        redirect(response, request.getContextPath() + path);
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