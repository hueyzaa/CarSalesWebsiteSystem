package controller.servlet;

import dao.OrdersDAO;
import model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/orders/detail")
public class OrderDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);
    private OrdersDAO ordersDAO;

    @Override
    public void init() throws ServletException {
        ordersDAO = new OrdersDAO();
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
            int orderId = Integer.parseInt(request.getParameter("id"));
            int userId = (int) session.getAttribute("userId");

            Order order = ordersDAO.getOrderById(orderId);

            if (order == null) {
                request.setAttribute("error", "Đơn hàng không tồn tại!");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            // Check if user owns this order (or is admin)
            String role = (String) session.getAttribute("userRole");
            if (order.getUserId() != userId && !"ADMIN".equals(role)) {
                request.setAttribute("error", "Bạn không có quyền xem đơn hàng này!");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/order-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid order ID", e);
            request.setAttribute("error", "ID đơn hàng không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}