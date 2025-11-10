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
 * OrdersServlet - Display orders for customers and admins
 * Customers: View their own orders with order details
 * Admins: View all orders with filtering
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

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated access to orders");
            redirect(response, request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            boolean isAdmin = SessionUtils.isAdmin(session);
            String statusFilter = request.getParameter("status");

            logger.info("Loading orders for user ID: {} (isAdmin: {})", userId, isAdmin);

            List<Order> orders = getOrders(userId, isAdmin, statusFilter);

            // Load order details and calculate totals for each order
            for (Order order : orders) {
                // Load order details (with car info, images)
                List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId());
                order.setOrderDetails(orderDetails);

                // Calculate total amount
                double totalAmount = ordersDAO.getOrderTotal(order.getOrderId());
                order.setTotalAmount(totalAmount);

                // Calculate paid amount (totalAmount - remainingAmount)
                double paidAmount = 0.0;
                if (order.getRemainingAmount() != null) {
                    paidAmount = totalAmount - order.getRemainingAmount();
                } else {
                    // If no remaining amount, assume fully paid
                    paidAmount = totalAmount;
                }
                order.setPaidAmount(paidAmount);

                logger.debug("Order {}: {} items, total: {}, paid: {}, remaining: {}",
                        order.getOrderId(),
                        orderDetails.size(),
                        totalAmount,
                        paidAmount,
                        order.getRemainingAmount());
            }

            logger.info("Retrieved {} orders with details", orders.size());

            setOrderAttributes(request, session, orders, isAdmin, userId, statusFilter);
            forward(request, response, "/WEB-INF/views/Customer/orders.jsp");

        } catch (RuntimeException e) {
            logger.error("Database error in OrdersServlet", e);
            handleError(request, response, "Không thể tải danh sách đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in OrdersServlet", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    // ============ ORDER LOADING ============

    private List<Order> getOrders(Integer userId, boolean isAdmin, String statusFilter) {
        if (isNotEmpty(statusFilter)) {
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

    private void setOrderAttributes(HttpServletRequest request, HttpSession session,
                                    List<Order> orders, boolean isAdmin,
                                    Integer userId, String statusFilter) {
        request.setAttribute("orders", orders);
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("userRole", SessionUtils.getUserRole(session));
        request.setAttribute("userId", userId);

        if (isNotEmpty(statusFilter)) {
            request.setAttribute("statusFilter", statusFilter);
        }
    }

    // ============ UTILITY METHODS ============

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
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