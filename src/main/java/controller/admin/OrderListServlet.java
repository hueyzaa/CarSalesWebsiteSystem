package controller.admin;

import dao.OrdersDAO;
import model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/Admin/order-list")
public class OrderListServlet extends HttpServlet {
    private final OrdersDAO ordersDAO = new OrdersDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = ordersDAO.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/Admin/order-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        try {
            boolean success = false;

            switch (action) {
                case "approve":
                    success = ordersDAO.approveOrder(orderId);
                    request.getSession().setAttribute("success",
                            success ? "Đã duyệt đơn hàng #" + orderId : "Không thể duyệt đơn hàng.");
                    break;

                case "cancel":
                    success = ordersDAO.cancelOrder(orderId);
                    request.getSession().setAttribute("success",
                            success ? "Đã hủy đơn hàng #" + orderId : "Không thể hủy đơn hàng.");
                    break;

                case "complete":
                    success = ordersDAO.completeOrder(orderId);
                    request.getSession().setAttribute("success",
                            success ? "Đã hoàn tất đơn hàng #" + orderId : "Không thể hoàn tất đơn hàng.");
                    break;

                case "update":
                    String status = request.getParameter("status");
                    success = ordersDAO.updateOrderStatus(orderId, status);
                    request.getSession().setAttribute("success",
                            success ? "Đã cập nhật trạng thái đơn hàng #" + orderId : "Cập nhật thất bại.");
                    break;

                default:
                    request.getSession().setAttribute("error", "Hành động không hợp lệ.");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("error", "Lỗi khi xử lý đơn hàng: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/Admin/order-list");
    }
}
