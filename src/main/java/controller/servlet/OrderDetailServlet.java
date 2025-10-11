package controller.servlet;

import dao.OrdersDAO;
import dao.OrderDetailDAO;
import dao.TransactionDAO;
import model.Order;
import model.User;
import exception.DatabaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);
    private OrdersDAO ordersDAO;
    private OrderDetailDAO orderDetailDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        ordersDAO = new OrdersDAO();
        orderDetailDAO = new OrderDetailDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            String orderIdParam = request.getParameter("id");

            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                logger.warn("Order ID parameter is missing");
                session.setAttribute("error", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);
            logger.info("Loading order detail for order ID: {}", orderId);

            // Get order
            Order order = ordersDAO.getOrderById(orderId);

            if (order == null) {
                logger.warn("Order not found: {}", orderId);
                session.setAttribute("error", "Đơn hàng không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            // Check permission
            if (!user.isAdmin() && order.getUserId() != user.getId()) {
                logger.warn("User {} attempted to access order {} belonging to user {}",
                        user.getId(), orderId, order.getUserId());
                session.setAttribute("error", "Bạn không có quyền xem đơn hàng này!");
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }

            // Load order details and transactions
            order.setOrderDetails(orderDetailDAO.getOrderDetailsByOrderId(orderId));
            order.setTransactions(transactionDAO.getTransactionsByOrderId(orderId));

            // Calculate totals for display
            double total = orderDetailDAO.calculateOrderTotal(orderId);
            double paid = transactionDAO.getTotalPaidAmount(orderId);

            // Set amounts for display
            order.setTotalAmount(total);
            order.setPaidAmount(paid);

            // ✅ remainingAmount is already calculated by trigger in database
            // No need to recalculate or override!

            logger.info("Order {} details (with trigger-calculated remaining):", orderId);
            logger.info("  - Payment Type: {}", order.getPaymentType());
            logger.info("  - Total: {}", total);
            logger.info("  - Paid: {}", paid);
            logger.info("  - Remaining (from trigger): {}", order.getRemainingAmount());
            logger.info("  - Status: {}", order.getStatus());
            logger.info("  - Fully Paid: {}", order.isFullyPaid());

            // Set attributes
            request.setAttribute("order", order);
            request.setAttribute("isAdmin", user.isAdmin());

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID format", e);
            session.setAttribute("error", "ID đơn hàng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (DatabaseException e) {
            logger.error("Database error in OrderDetailServlet", e);
            request.setAttribute("error", "Không thể tải thông tin đơn hàng.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error in OrderDetailServlet", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}