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
 * OrderDetailServlet - Display detailed order information
 * UPDATED: Uses OrderDTO with pre-calculated business logic
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
    private OrderService orderService;

    @Override
    public void init() {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
        customerDAO = new CustomerDAO();
        orderService = new OrderService();
        logger.info("OrderDetailServlet initialized with OrderService");
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

            // Get Order Model from DAO
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

            // Enrich order with details
            enrichOrder(order);

            // Convert Model to DTO with pre-calculated values
            OrderDTO orderDTO = orderService.toOrderDTO(order);

            logger.debug("Converted order {} to DTO - canCancel: {}, fullyPaid: {}",
                    orderId, orderDTO.isCanBeCancelled(), orderDTO.isFullyPaid());

            Customer customer = customerDAO.getCustomerById(order.getUserId());

            logOrderSummary(orderId, orderDTO, customer);

            // Pass DTO to view (not Model)
            setOrderAttributes(request, session, orderDTO, customer, isAdmin, currentUserId);
            forward(request, response, "/WEB-INF/views/Customer/order-detail.jsp");

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

    // ============ BUSINESS LOGIC (moved from Model) ============

    /**
     * Check if order is fully paid
     * NOTE: This is now pre-calculated in OrderDTO
     */
    private boolean isOrderFullyPaid(Order order) {
        return order.getPaidAmount() >= order.getTotalAmount();
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
                                    OrderDTO orderDTO, Customer customer,
                                    boolean isAdmin, Integer currentUserId) {
        request.setAttribute("order", orderDTO);  // Pass DTO
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

    private void logOrderSummary(int orderId, OrderDTO orderDTO, Customer customer) {
        logger.info("Order {} details:", orderId);
        logger.info("  Customer: {} (ID: {})",
                customer != null ? customer.getName() : "Unknown", orderDTO.getUserId());
        logger.info("  Payment Type: {}, Status: {}",
                orderDTO.getPaymentType(), orderDTO.getStatus());
        logger.info("  Total: {}, Paid: {}, Remaining: {}",
                orderDTO.getTotalAmount(), orderDTO.getPaidAmount(), orderDTO.getRemainingAmount());
        logger.info("  Items: {}, Transactions: {}, Fully Paid: {}",
                orderDTO.getOrderDetails().size(), orderDTO.getTransactions().size(),
                orderDTO.isFullyPaid());
        logger.info("  Can be cancelled: {}", orderDTO.isCanBeCancelled());
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
        forward(request, response, "/WEB-INF/views/Customer/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}