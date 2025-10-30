package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.TransactionDAO;
import model.Order;
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
 * OrdersServlet - Display orders for customers and admins
 * Customers: View their own orders
 * Admins: View all orders with filtering
 */
@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrdersServlet.class);
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
        logger.info("OrdersServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated access to orders");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            boolean isAdmin = SessionUtils.isAdmin(session);
            String statusFilter = request.getParameter("status");

            logger.info("Loading orders for user ID: {} (isAdmin: {})", userId, isAdmin);

            // Get orders
            List<Order> orders = getOrders(userId, isAdmin, statusFilter);

            logger.info("Retrieved {} orders", orders.size());

            // Enrich orders with details
            enrichOrders(orders);

            // Set attributes
            request.setAttribute("orders", orders);
            request.setAttribute("isAdmin", isAdmin);
            request.setAttribute("userRole", SessionUtils.getUserRole(session));
            request.setAttribute("userId", userId);
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                request.setAttribute("statusFilter", statusFilter);
            }

            request.getRequestDispatcher("/WEB-INF/views/orders.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error in OrdersServlet", e);
            handleError(request, response, "Không thể tải danh sách đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in OrdersServlet", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    /**
     * Get orders based on user role and filter
     */
    private List<Order> getOrders(Integer userId, boolean isAdmin, String statusFilter) {
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            logger.debug("Filtering orders by status: {}", statusFilter);
            List<Order> orders = ordersDAO.getOrdersByStatus(statusFilter);

            if (!isAdmin) {
                final int currentUserId = userId;
                orders.removeIf(order -> order.getUserId() != currentUserId);
                logger.debug("Filtered {} orders for user {}", orders.size(), userId);
            }

            return orders;
        }

        return isAdmin ? ordersDAO.getAllOrders() : ordersDAO.getOrdersByUserId(userId);
    }

    /**
     * Enrich orders with details and transactions
     */
    private void enrichOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return;
        }

        for (Order order : orders) {
            order.setOrderDetails(orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId()));
            order.setTransactions(transactionDAO.getTransactionsByOrderId(order.getOrderId()));

            double total = orderDetailDAO.calculateOrderTotal(order.getOrderId());
            double paid = transactionDAO.getTotalPaidAmount(order.getOrderId());

            order.setTotalAmount(total);
            order.setPaidAmount(paid);
        }

        logger.debug("Enriched {} orders with details and transactions", orders.size());
    }

    /**
     * Handle error and forward to error page
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("OrdersServlet destroyed");
    }
}