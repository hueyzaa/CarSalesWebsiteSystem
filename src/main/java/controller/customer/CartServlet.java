package controller.customer;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import util.SessionUtils;
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

/**
 * CartServlet - Handle shopping cart operations
 * Accessible by logged-in users (Customer/Staff/Admin)
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);
    private CartDAO cartDAO;
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        cartDAO = new CartDAO();
        carDAO = new CarDAO();
        logger.info("CartServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);

            double total = calculateTotal(cartItems);

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("total", total);
            session.setAttribute("cartCount", cartItems != null ? cartItems.size() : 0);

            logger.debug("Cart loaded for user {} with {} items", userId,
                    cartItems != null ? cartItems.size() : 0);

            request.getRequestDispatcher("/WEB-INF/views/cart.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.error("Error loading cart", e);
            request.setAttribute("error", "Lỗi khi tải giỏ hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            String action = request.getParameter("action");

            switch (action != null ? action : "") {
                case "add":
                    handleAddToCart(request, response, session, userId);
                    break;
                case "update":
                    handleUpdateQuantity(request, response, session, userId);
                    break;
                case "remove":
                    handleRemoveItem(request, response, session);
                    break;
                case "clear":
                    handleClearCart(request, response, session, userId);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/cart");
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in cart operation", e);
            session.setAttribute("error", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (Exception e) {
            logger.error("Error in cart operation", e);
            session.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    /**
     * Calculate cart total
     */
    private double calculateTotal(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0;
        }
        return cartItems.stream()
                .mapToDouble(item -> item.getCar().getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Handle add to cart with stock validation
     */
    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response,
                                 HttpSession session, Integer userId) throws IOException {
        int carId = Integer.parseInt(request.getParameter("carId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            logger.warn("Attempted to add non-existent car {} to cart", carId);
            session.setAttribute("error", "Xe không tồn tại!");
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        if (quantity <= 0) {
            session.setAttribute("error", "Số lượng phải lớn hơn 0!");
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId);
            return;
        }

        if (car.getStock() < quantity) {
            logger.warn("Insufficient stock for car {}: requested {}, available {}",
                    carId, quantity, car.getStock());
            session.setAttribute("error",
                    String.format("Số lượng xe không đủ! Chỉ còn %d xe trong kho.", car.getStock()));
            response.sendRedirect(request.getContextPath() + "/car-detail?id=" + carId);
            return;
        }

        boolean success = cartDAO.addToCart(userId, carId, quantity);

        if (success) {
            logger.info("User {} added car {} (qty: {}) to cart", userId, carId, quantity);
            session.setAttribute("success", "Đã thêm vào giỏ hàng!");
        } else {
            logger.warn("Failed to add car {} to cart for user {}", carId, userId);
            session.setAttribute("error", "Không thể thêm vào giỏ hàng! Vui lòng kiểm tra lại số lượng.");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    /**
     * Handle update quantity with stock validation
     */
    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response,
                                      HttpSession session, Integer userId) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            session.setAttribute("error", "Số lượng phải lớn hơn 0!");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
        CartItem targetItem = cartItems.stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElse(null);

        if (targetItem == null) {
            logger.warn("Cart item {} not found for user {}", cartItemId, userId);
            session.setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        int availableStock = targetItem.getCar().getStock();
        if (quantity > availableStock) {
            logger.warn("Insufficient stock for cart update: requested {}, available {}",
                    quantity, availableStock);
            session.setAttribute("error",
                    String.format("Số lượng không đủ! Chỉ còn %d xe trong kho.", availableStock));
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        boolean success = cartDAO.updateCartItem(cartItemId, quantity);

        if (success) {
            logger.info("User {} updated cart item {} to quantity {}", userId, cartItemId, quantity);
            session.setAttribute("success", "Đã cập nhật số lượng!");
        } else {
            logger.warn("Failed to update cart item {} for user {}", cartItemId, userId);
            session.setAttribute("error", "Không thể cập nhật số lượng!");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    /**
     * Handle remove item from cart
     */
    private void handleRemoveItem(HttpServletRequest request, HttpServletResponse response,
                                  HttpSession session) throws IOException {
        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        boolean success = cartDAO.removeCartItem(cartItemId);

        if (success) {
            logger.info("Removed cart item {}", cartItemId);
            session.setAttribute("success", "Đã xóa khỏi giỏ hàng!");
        } else {
            logger.warn("Failed to remove cart item {}", cartItemId);
            session.setAttribute("error", "Không thể xóa sản phẩm!");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    /**
     * Handle clear all cart
     */
    private void handleClearCart(HttpServletRequest request, HttpServletResponse response,
                                 HttpSession session, Integer userId) throws IOException {
        boolean success = cartDAO.clearCart(userId);

        if (success) {
            logger.info("Cleared cart for user {}", userId);
            session.setAttribute("success", "Đã xóa tất cả sản phẩm!");
        } else {
            logger.warn("Failed to clear cart for user {}", userId);
            session.setAttribute("error", "Không thể xóa giỏ hàng!");
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("CartServlet destroyed");
    }
}