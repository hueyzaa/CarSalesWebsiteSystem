package controller.customer;

import dao.OrdersDAO;
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

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
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

            logger.info("Retrieved {} orders", orders.size());

            setOrderAttributes(request, session, orders, isAdmin, userId, statusFilter);
            forward(request, response, "/WEB-INF/views/orders.jsp");

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
        forward(request, response, "/WEB-INF/views/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}