package controller.servlet;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import model.User;
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

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get user from session
            User user = (User) session.getAttribute("user");
            int userId = user.getId(); // Lấy userId từ User object

            // Get cart items
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            // Calculate total
            double total = 0;
            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    total += item.getCar().getPrice() * item.getQuantity();
                }
            }

            // Set attributes
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);

            // Update cart count in session
            session.setAttribute("cartCount", cartItems != null ? cartItems.size() : 0);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải giỏ hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            int userId = user.getId();

            String action = request.getParameter("action");

            if ("add".equals(action)) {
                // Add to cart
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // Validate car
                Car car = carDAO.getCarById(carId);
                if (car == null) {
                    session.setAttribute("error", "Xe không tồn tại!");
                    response.sendRedirect(request.getContextPath() + "/cars");
                    return;
                }

                // Check stock
                if (car.getStock() < quantity) {
                    session.setAttribute("error", "Số lượng xe không đủ!");
                    response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId);
                    return;
                }

                // Add to cart
                boolean success = cartDAO.addToCart(userId, carId, quantity);

                if (success) {
                    session.setAttribute("success", "Đã thêm vào giỏ hàng!");
                } else {
                    session.setAttribute("error", "Không thể thêm vào giỏ hàng!");
                }

                response.sendRedirect(request.getContextPath() + "/cart");

            } else if ("update".equals(action)) {
                // Update quantity
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                if (quantity <= 0) {
                    session.setAttribute("error", "Số lượng phải lớn hơn 0!");
                } else {
                    boolean success = cartDAO.updateCartItem(cartItemId, quantity);
                    if (success) {
                        session.setAttribute("success", "Đã cập nhật số lượng!");
                    } else {
                        session.setAttribute("error", "Không thể cập nhật!");
                    }
                }

                response.sendRedirect(request.getContextPath() + "/cart");

            } else if ("remove".equals(action)) {
                // Remove item
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                boolean success = cartDAO.removeCartItem(cartItemId);

                if (success) {
                    session.setAttribute("success", "Đã xóa khỏi giỏ hàng!");
                } else {
                    session.setAttribute("error", "Không thể xóa!");
                }

                response.sendRedirect(request.getContextPath() + "/cart");

            } else if ("clear".equals(action)) {
                // Clear all cart
                boolean success = cartDAO.clearCart(userId);

                if (success) {
                    session.setAttribute("success", "Đã xóa tất cả sản phẩm!");
                } else {
                    session.setAttribute("error", "Không thể xóa giỏ hàng!");
                }

                response.sendRedirect(request.getContextPath() + "/cart");

            } else {
                response.sendRedirect(request.getContextPath() + "/cart");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("error", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}