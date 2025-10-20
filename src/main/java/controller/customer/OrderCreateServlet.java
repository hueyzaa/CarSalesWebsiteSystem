package controller.customer;

import dao.CartDAO;
import dao.OrdersDAO;
import model.CartItem;
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

@WebServlet("/orders/create")
public class OrderCreateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreateServlet.class);
    private OrdersDAO ordersDAO;
    private CartDAO cartDAO;

    @Override
    public void init() throws ServletException {
        ordersDAO = new OrdersDAO();
        cartDAO = new CartDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            if (cartItems.isEmpty()) {
                session.setAttribute("error", "Giỏ hàng trống!");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Create order
            int orderId = ordersDAO.createOrder(userId, cartItems);

            if (orderId > 0) {
                // Clear cart after successful order
                cartDAO.clearCart(userId);
                session.setAttribute("success", "Đặt hàng thành công! Mã đơn hàng: " + orderId);
                response.sendRedirect(request.getContextPath() + "/orders");
            } else {
                session.setAttribute("error", "Không thể tạo đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/cart");
            }

        } catch (Exception e) {
            logger.error("Error creating order", e);
            session.setAttribute("error", "Đã xảy ra lỗi khi tạo đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}