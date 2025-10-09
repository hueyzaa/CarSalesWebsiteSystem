package controller.servlet;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private CartDAO cartDAO;
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        carDAO = new CarDAO();
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
        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int carId = Integer.parseInt(request.getParameter("carId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Car car = carDAO.getCarById(carId);
        if (car == null || !"AVAILABLE".equals(car.getStatus())) {
            request.setAttribute("error", "Xe không tồn tại hoặc không còn hàng!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        cartDAO.addToCart(userId, carId, quantity);
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}