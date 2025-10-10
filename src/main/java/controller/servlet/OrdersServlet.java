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
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrdersServlet.class);
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
            int userId = user.getId();

            // Get filter parameter
            String statusFilter = request.getParameter("status");

            // Get orders
            List<Order> orders;
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                orders = ordersDAO.getOrdersByStatus(statusFilter);
                // Filter by user if not admin
                if (!user.isAdmin()) {
                    orders.removeIf(order -> order.getUserId() != userId);
                }
                request.setAttribute("statusFilter", statusFilter);
            } else {
                if (user.isAdmin()) {
                    orders = ordersDAO.getAllOrders();
                } else {
                    orders = ordersDAO.getOrdersByUserId(userId);
                }
            }

            // Enrich orders with details and transaction info
            for (Order order : orders) {
                order.setOrderDetails(orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId()));
                order.setTransactions(transactionDAO.getTransactionsByOrderId(order.getOrderId()));

                double total = orderDetailDAO.calculateOrderTotal(order.getOrderId());
                double paid = transactionDAO.getTotalPaidAmount(order.getOrderId());

                order.setTotalAmount(total);
                order.setPaidAmount(paid);
                order.setRemainingAmount(total - paid);
            }

            // Set attributes
            request.setAttribute("orders", orders);
            request.setAttribute("isAdmin", user.isAdmin());

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);

        } catch (DatabaseException e) {
            logger.error("Database error in OrdersServlet", e);
            request.setAttribute("error", "Không thể tải danh sách đơn hàng.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error in OrdersServlet", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}