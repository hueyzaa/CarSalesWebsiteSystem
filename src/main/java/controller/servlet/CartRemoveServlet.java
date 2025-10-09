package controller.servlet;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/cart/remove")
public class CartRemoveServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartRemoveServlet.class);
    private CartDAO cartDAO;

    @Override
    public void init() throws ServletException {
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

        try {
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            boolean success = cartDAO.removeCartItem(cartItemId);  // ✅ Fixed method name

            if (success) {
                session.setAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng!");
            } else {
                session.setAttribute("error", "Không thể xóa sản phẩm!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid cart item ID", e);
            session.setAttribute("error", "ID không hợp lệ!");
        } catch (Exception e) {
            logger.error("Error removing cart item", e);
            session.setAttribute("error", "Đã xảy ra lỗi khi xóa sản phẩm!");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Support POST method as well
        doGet(request, response);
    }
}