package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
import model.Order;
import model.OrderDetail;
import util.SessionUtils;
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
    public void init() {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        carDAO = new CarDAO();
        logger.info("OrderCancelServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated cancel attempt");
            redirect(response, request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            boolean isAdmin = SessionUtils.isAdmin(session);

            String orderIdParam = request.getParameter("orderId");
            if (isEmpty(orderIdParam)) {
                logger.warn("Order ID parameter missing");
                redirectWithError(session, response, "/orders", "Không tìm thấy đơn hàng!");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            logger.info("Cancel request for order {} by user {} (isAdmin: {})",
                    orderId, userId, isAdmin);

            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                redirectWithError(session, response, "/orders", "Đơn hàng không tồn tại!");
                return;
            }

            if (!hasPermission(userId, isAdmin, order)) {
                logger.warn("User {} attempted to cancel order {} (owner: {})",
                        userId, orderId, order.getUserId());
                redirectWithError(session, response, "/orders",
                        "Bạn không có quyền hủy đơn hàng này!");
                return;
            }

            if (!validateCancellation(request, order, session, response)) {
                return;
            }

            restoreStock(orderId);

            if (ordersDAO.cancelOrder(orderId)) {
                logger.info("Order {} cancelled successfully", orderId);
                session.setAttribute("success", "Đơn hàng #" + orderId + " đã được hủy thành công!");
            } else {
                logger.error("Failed to cancel order: {}", orderId);
                session.setAttribute("error", "Không thể hủy đơn hàng. Vui lòng thử lại!");
            }

            String redirectTo = "detail".equals(request.getParameter("redirectTo"))
                    ? "/order-detail?id=" + orderId
                    : "/orders";
            redirect(response, request.getContextPath() + redirectTo);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            redirectWithError(session, response, "/orders", "ID đơn hàng không hợp lệ!");
        } catch (RuntimeException e) {
            logger.error("Database error in OrderCancelServlet", e);
            redirectWithError(session, response, "/orders",
                    "Không thể hủy đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in OrderCancelServlet", e);
            redirectWithError(session, response, "/orders", "Đã xảy ra lỗi không mong muốn!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        logger.warn("GET request to cancel order, redirecting");
        redirect(response, request.getContextPath() + "/orders");
    }

    // ============ VALIDATION ============

    private boolean hasPermission(Integer userId, boolean isAdmin, Order order) {
        return isAdmin || order.getUserId() == userId;
    }

    private boolean validateCancellation(HttpServletRequest request, Order order,
                                         HttpSession session, HttpServletResponse response)
            throws IOException {
        String redirectUrl = request.getContextPath() + "/order-detail?id=" + order.getOrderId();

        if (!order.canBeCancelled()) {
            logger.warn("Order {} cannot be cancelled. Status: {}",
                    order.getOrderId(), order.getStatus());
            session.setAttribute("error",
                    "Không thể hủy đơn hàng với trạng thái: " + order.getStatusDisplay());
            redirect(response, redirectUrl);
            return false;
        }

        if (order.getPaidAmount() > 0) {
            logger.warn("Order {} has payment: {}, cannot cancel",
                    order.getOrderId(), order.getPaidAmount());
            session.setAttribute("error",
                    "Đơn hàng đã có giao dịch thanh toán. Vui lòng liên hệ quản trị viên để hoàn tiền!");
            redirect(response, redirectUrl);
            return false;
        }

        return true;
    }

    // ============ STOCK MANAGEMENT ============

    private void restoreStock(int orderId) {
        List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

        if (orderDetails == null || orderDetails.isEmpty()) {
            logger.warn("No order details found for order {}", orderId);
            return;
        }

        logger.info("Restoring stock for {} items in order {}", orderDetails.size(), orderId);

        for (OrderDetail detail : orderDetails) {
            if (carDAO.increaseStock(detail.getCarId(), detail.getQuantity())) {
                logger.debug("Stock restored for car {} (+{})",
                        detail.getCarId(), detail.getQuantity());
            } else {
                logger.warn("Failed to restore stock for car {} in order {}",
                        detail.getCarId(), orderId);
            }
        }
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
}