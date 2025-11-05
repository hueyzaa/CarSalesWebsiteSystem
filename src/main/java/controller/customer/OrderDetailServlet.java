package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.TransactionDAO;
import dao.CustomerDAO;
import model.Order;
import model.Customer;
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
 * OrderDetailServlet - Display detailed order information
 * Customers: View their own orders
 * Admins: View any order
 */
@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);

    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
        customerDAO = new CustomerDAO();
        logger.info("OrderDetailServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirect(response, request.getContextPath() + "/login");
            return;
        }

        try {
            Integer currentUserId = SessionUtils.getUserId(session);
            boolean isAdmin = SessionUtils.isAdmin(session);

            String orderIdParam = request.getParameter("id");
            if (isEmpty(orderIdParam)) {
                logger.warn("Order ID parameter is missing");
                redirectWithError(session, response, "/orders", "Không tìm thấy đơn hàng!");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            logger.info("Loading order {} for user {} (isAdmin: {})",
                    orderId, currentUserId, isAdmin);

            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                redirectWithError(session, response, "/orders", "Đơn hàng không tồn tại!");
                return;
            }

            if (!hasPermission(currentUserId, isAdmin, order)) {
                logger.warn("User {} attempted to access order {} (owner: {})",
                        currentUserId, orderId, order.getUserId());
                redirectWithError(session, response, "/orders",
                        "Bạn không có quyền xem đơn hàng này!");
                return;
            }

            enrichOrder(order);

            Customer customer = customerDAO.getCustomerById(order.getUserId());

            logOrderSummary(orderId, order, customer);

            setOrderAttributes(request, session, order, customer, isAdmin, currentUserId);
            forward(request, response, "/WEB-INF/views/order-detail.jsp");

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            redirectWithError(session, response, "/orders", "ID đơn hàng không hợp lệ!");
        } catch (RuntimeException e) {
            logger.error("Database error in OrderDetailServlet", e);
            handleError(request, response, "Không thể tải thông tin đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in OrderDetailServlet", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    // ============ ORDER PROCESSING ============

    private void enrichOrder(Order order) {
        order.setOrderDetails(orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId()));
        order.setTransactions(transactionDAO.getTransactionsByOrderId(order.getOrderId()));

        double total = orderDetailDAO.calculateOrderTotal(order.getOrderId());
        double paid = transactionDAO.getTotalPaidAmount(order.getOrderId());

        order.setTotalAmount(total);
        order.setPaidAmount(paid);
    }

    private void setOrderAttributes(HttpServletRequest request, HttpSession session,
                                    Order order, Customer customer,
                                    boolean isAdmin, Integer currentUserId) {
        request.setAttribute("order", order);
        request.setAttribute("customer", customer);
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("currentUserId", currentUserId);
        request.setAttribute("userRole", SessionUtils.getUserRole(session));
    }

    // ============ VALIDATION ============

    private boolean hasPermission(Integer userId, boolean isAdmin, Order order) {
        return isAdmin || order.getUserId() == userId;
    }

    // ============ LOGGING ============

    private void logOrderSummary(int orderId, Order order, Customer customer) {
        logger.info("Order {} details:", orderId);
        logger.info("  Customer: {} (ID: {})",
                customer != null ? customer.getName() : "Unknown", order.getUserId());
        logger.info("  Payment Type: {}, Status: {}", order.getPaymentType(), order.getStatus());
        logger.info("  Total: {}, Paid: {}, Remaining: {}",
                order.getTotalAmount(), order.getPaidAmount(), order.getRemainingAmount());
        logger.info("  Items: {}, Transactions: {}, Fully Paid: {}",
                order.getOrderDetails().size(), order.getTransactions().size(), order.isFullyPaid());
    }

    // ============ UTILITY METHODS ============

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithError(HttpSession session, HttpServletResponse response,
                                   String path, String errorMessage) throws IOException {
        session.setAttribute("error", errorMessage);
        redirect(response, session.getServletContext().getContextPath() + path);
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