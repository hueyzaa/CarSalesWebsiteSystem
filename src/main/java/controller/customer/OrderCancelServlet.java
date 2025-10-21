package controller.customer;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.CarDAO;
import model.Order;
import model.OrderDetail;
import model.User;
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

@WebServlet("/order-cancel")
public class OrderCancelServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelServlet.class);
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        carDAO = new CarDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            String orderIdParam = request.getParameter("orderId");

            // Validate order ID
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                logger.warn("Order ID parameter is missing");
                session.setAttribute("error", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            logger.info("Processing cancel request for order ID: {} by user ID: {}", orderId, user.getId());

            // Get order
            Order order = ordersDAO.getOrderById(orderId);

            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                session.setAttribute("error", "Đơn hàng không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            // Check permission: user can only cancel their own orders (unless admin)
            if (!user.isAdmin() && order.getUserId() != user.getId()) {
                logger.warn("User {} attempted to cancel order {} belonging to user {}",
                        user.getId(), orderId, order.getUserId());
                session.setAttribute("error", "Bạn không có quyền hủy đơn hàng này!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            // Check if order can be cancelled
            if (!order.canBeCancelled()) {
                logger.warn("Order {} cannot be cancelled. Current status: {}", orderId, order.getStatus());
                session.setAttribute("error", "Không thể hủy đơn hàng với trạng thái: " + order.getStatusDisplay());
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
                return;
            }

            // Get order details to restore stock
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);

            if (orderDetails != null && !orderDetails.isEmpty()) {
                logger.info("Restoring stock for {} items in order {}", orderDetails.size(), orderId);

                // Restore stock for each item
                for (OrderDetail detail : orderDetails) {
                    boolean stockRestored = carDAO.increaseStock(detail.getCarId(), detail.getQuantity());

                    if (stockRestored) {
                        logger.debug("Stock restored for car ID: {} by {} units",
                                detail.getCarId(), detail.getQuantity());
                    } else {
                        logger.warn("Failed to restore stock for car ID: {} in cancelled order {}",
                                detail.getCarId(), orderId);
                    }
                }
            }

            // Cancel the order
            boolean cancelled = ordersDAO.cancelOrder(orderId);

            if (cancelled) {
                logger.info("Order {} cancelled successfully by user {}", orderId, user.getId());
                session.setAttribute("success", "Đơn hàng #" + orderId + " đã được hủy thành công!");
            } else {
                logger.error("Failed to cancel order: {}", orderId);
                session.setAttribute("error", "Không thể hủy đơn hàng. Vui lòng thử lại!");
            }

            // Redirect back to order detail or orders list
            String redirectTo = request.getParameter("redirectTo");
            if ("detail".equals(redirectTo)) {
                response.sendRedirect(request.getContextPath() + "/order-detail?id=" + orderId);
            } else {
                response.sendRedirect(request.getContextPath() + "/orders");
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            session.setAttribute("error", "ID đơn hàng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (RuntimeException e) {
            logger.error("Database error in OrderCancelServlet", e);
            session.setAttribute("error", "Không thể hủy đơn hàng. Vui lòng thử lại!");
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (Exception e) {
            logger.error("Unexpected error in OrderCancelServlet", e);
            session.setAttribute("error", "Đã xảy ra lỗi không mong muốn!");
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to POST
        logger.warn("GET request to cancel order, redirecting to orders");
        response.sendRedirect(request.getContextPath() + "/orders");
    }
}