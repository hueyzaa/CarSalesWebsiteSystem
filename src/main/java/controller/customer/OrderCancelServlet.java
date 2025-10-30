package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
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
 * OrderCancelServlet - Handle order cancellation
 * Customers: Cancel their own orders (if eligible)
 * Admins: Cancel any order
 */
@WebServlet("/order-cancel")
public class OrderCancelServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelServlet.class);
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        carDAO = new CarDAO();
        logger.info("OrderCancelServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated cancel attempt");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            boolean isAdmin = SessionUtils.isAdmin(session);

            // Validate order ID
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                logger.warn("Order ID parameter missing");
                redirectWithError(request, session, response, "/orders", "Không tìm thấy đơn hàng!");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            logger.info("Cancel request for order {} by user {} (isAdmin: {})",
                    orderId, userId, isAdmin);

            // Get order
            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                redirectWithError(request, session, response, "/orders", "Đơn hàng không tồn tại!");
                return;
            }

            // Check permission
            if (!isAdmin && order.getUserId() != userId) {
                logger.warn("User {} attempted to cancel order {} (owner: {})",
                        userId, orderId, order.getUserId());
                redirectWithError(request, session, response, "/orders",
                        "Bạn không có quyền hủy đơn hàng này!");
                return;
            }

            // Validate cancellation eligibility
            if (!validateCancellation(request, order, session, response)) {
                return;
            }

            // Restore stock
            restoreStock(orderId);

            // Cancel order
            boolean cancelled = ordersDAO.cancelOrder(orderId);

            if (cancelled) {
                logger.info("Order {} cancelled successfully", orderId);
                session.setAttribute("success", "Đơn hàng #" + orderId + " đã được hủy thành công!");
            } else {
                logger.error("Failed to cancel order: {}", orderId);
                session.setAttribute("error", "Không thể hủy đơn hàng. Vui lòng thử lại!");
            }

            // Redirect
            String redirectTo = "detail".equals(request.getParameter("redirectTo"))
                    ? "/order-detail?id=" + orderId
                    : "/orders";
            response.sendRedirect(request.getContextPath() + redirectTo);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            redirectWithError(request, session, response, "/orders", "ID đơn hàng không hợp lệ!");
        } catch (RuntimeException e) {
            logger.error("Database error in OrderCancelServlet", e);
            redirectWithError(request, session, response, "/orders",
                    "Không thể hủy đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in OrderCancelServlet", e);
            redirectWithError(request, session, response, "/orders", "Đã xảy ra lỗi không mong muốn!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.warn("GET request to cancel order, redirecting");
        response.sendRedirect(request.getContextPath() + "/orders");
    }

    /**
     * Validate if order can be cancelled
     */
    private boolean validateCancellation(HttpServletRequest request, Order order,
                                         HttpSession session, HttpServletResponse response)
            throws IOException {
        String redirectUrl = request.getContextPath() + "/order-detail?id=" + order.getOrderId();

        if (!order.canBeCancelled()) {
            logger.warn("Order {} cannot be cancelled. Status: {}",
                    order.getOrderId(), order.getStatus());
            session.setAttribute("error",
                    "Không thể hủy đơn hàng với trạng thái: " + order.getStatusDisplay());
            response.sendRedirect(redirectUrl);
            return false;
        }

        if (order.getPaidAmount() > 0) {
            logger.warn("Order {} has payment: {}, cannot cancel",
                    order.getOrderId(), order.getPaidAmount());
            session.setAttribute("error",
                    "Đơn hàng đã có giao dịch thanh toán. Vui lòng liên hệ quản trị viên để hoàn tiền!");
            response.sendRedirect(redirectUrl);
            return false;
        }

        return true;
    }

    /**
     * Restore stock for cancelled order items
     */
    private void restoreStock(int orderId) {
        List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

        if (orderDetails == null || orderDetails.isEmpty()) {
            logger.warn("No order details found for order {}", orderId);
            return;
        }

        logger.info("Restoring stock for {} items in order {}", orderDetails.size(), orderId);

        for (OrderDetail detail : orderDetails) {
            boolean restored = carDAO.increaseStock(detail.getCarId(), detail.getQuantity());

            if (restored) {
                logger.debug("Stock restored for car {} (+{})",
                        detail.getCarId(), detail.getQuantity());
            } else {
                logger.warn("Failed to restore stock for car {} in order {}",
                        detail.getCarId(), orderId);
            }
        }
    }

    /**
     * Redirect with error message
     */
    private void redirectWithError(HttpServletRequest request, HttpSession session,
                                   HttpServletResponse response, String path,
                                   String errorMessage) throws IOException {
        session.setAttribute("error", errorMessage);
        response.sendRedirect(request.getContextPath() + path);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("OrderCancelServlet destroyed");
    }
}