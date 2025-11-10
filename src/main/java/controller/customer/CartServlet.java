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

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);

    private CartDAO cartDAO;
    private CarDAO carDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        carDAO = new CarDAO();
        logger.info("CartServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
            double total = calculateTotal(cartItems);

            setCartAttributes(request, request.getSession(), cartItems, total);
            forward(request, response, "/WEB-INF/views/Customer/cart.jsp");

        } catch (Exception e) {
            logger.error("Error loading cart", e);
            forwardToError(request, response, "Lỗi khi tải giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Integer userId = validateUser(request, response);
        if (userId == null) return;

        try {
            String action = request.getParameter("action");
            handleAction(action, request, response, userId);

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in cart operation", e);
            redirectWithError(response, request.getSession(), "/cart", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            logger.error("Error in cart operation", e);
            redirectWithError(response, request.getSession(), "/cart",
                    "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // ============ USER VALIDATION ============

    private Integer validateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            redirect(response, request.getContextPath() + "/login");
            return null;
        }

        return SessionUtils.getUserId(session);
    }

    // ============ ACTION HANDLERS ============

    private void handleAction(String action, HttpServletRequest request,
                              HttpServletResponse response, Integer userId) throws IOException {

        switch (action != null ? action : "") {
            case "add":
                handleAddToCart(request, response, userId);
                break;
            case "update":
                handleUpdateQuantity(request, response, userId);
                break;
            case "remove":
                handleRemoveItem(request, response);
                break;
            case "clear":
                handleClearCart(request, response, userId);
                break;
            default:
                redirect(response, request.getContextPath() + "/cart");
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response,
                                 Integer userId) throws IOException {
        HttpSession session = request.getSession();

        int carId = Integer.parseInt(request.getParameter("carId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            logger.warn("Attempted to add non-existent car {}", carId);
            redirectWithError(response, session, "/cars", "Xe không tồn tại!");
            return;
        }

        if (quantity <= 0) {
            redirectWithError(response, session, "/car-detail?id=" + carId,
                    "Số lượng phải lớn hơn 0!");
            return;
        }

        if (car.getStock() < quantity) {
            logger.warn("Insufficient stock for car {}: requested {}, available {}",
                    carId, quantity, car.getStock());
            redirectWithError(response, session, "/car-detail?id=" + carId,
                    String.format("Số lượng xe không đủ! Chỉ còn %d xe trong kho.", car.getStock()));
            return;
        }

        if (cartDAO.addToCart(userId, carId, quantity)) {
            logger.info("User {} added car {} (qty: {}) to cart", userId, carId, quantity);
            redirectWithSuccess(response, session, "/cart", "Đã thêm vào giỏ hàng!");
        } else {
            logger.warn("Failed to add car {} to cart for user {}", carId, userId);
            redirectWithError(response, session, "/cart",
                    "Không thể thêm vào giỏ hàng! Vui lòng kiểm tra lại số lượng.");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response,
                                      Integer userId) throws IOException {
        HttpSession session = request.getSession();

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        if (quantity <= 0) {
            redirectWithError(response, session, "/cart", "Số lượng phải lớn hơn 0!");
            return;
        }

        CartItem targetItem = findCartItem(userId, cartItemId);
        if (targetItem == null) {
            logger.warn("Cart item {} not found for user {}", cartItemId, userId);
            redirectWithError(response, session, "/cart", "Không tìm thấy sản phẩm trong giỏ hàng!");
            return;
        }

        int availableStock = targetItem.getCar().getStock();
        if (quantity > availableStock) {
            logger.warn("Insufficient stock for cart update: requested {}, available {}",
                    quantity, availableStock);
            redirectWithError(response, session, "/cart",
                    String.format("Số lượng không đủ! Chỉ còn %d xe trong kho.", availableStock));
            return;
        }

        if (cartDAO.updateCartItem(cartItemId, quantity)) {
            logger.info("User {} updated cart item {} to quantity {}", userId, cartItemId, quantity);
            redirectWithSuccess(response, session, "/cart", "Đã cập nhật số lượng!");
        } else {
            logger.warn("Failed to update cart item {} for user {}", cartItemId, userId);
            redirectWithError(response, session, "/cart", "Không thể cập nhật số lượng!");
        }
    }

    private void handleRemoveItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();

        int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));

        if (cartDAO.removeCartItem(cartItemId)) {
            logger.info("Removed cart item {}", cartItemId);
            redirectWithSuccess(response, session, "/cart", "Đã xóa khỏi giỏ hàng!");
        } else {
            logger.warn("Failed to remove cart item {}", cartItemId);
            redirectWithError(response, session, "/cart", "Không thể xóa sản phẩm!");
        }
    }

    private void handleClearCart(HttpServletRequest request, HttpServletResponse response,
                                 Integer userId) throws IOException {
        HttpSession session = request.getSession();

        if (cartDAO.clearCart(userId)) {
            logger.info("Cleared cart for user {}", userId);
            redirectWithSuccess(response, session, "/cart", "Đã xóa tất cả sản phẩm!");
        } else {
            logger.warn("Failed to clear cart for user {}", userId);
            redirectWithError(response, session, "/cart", "Không thể xóa giỏ hàng!");
        }
    }

    // ============ HELPER METHODS ============

    private CartItem findCartItem(Integer userId, int cartItemId) {
        List<CartItem> cartItems = cartDAO.getCartItemsByUserId(userId);
        return cartItems.stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst()
                .orElse(null);
    }

    private double calculateTotal(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return 0;
        }
        return cartItems.stream()
                .mapToDouble(item -> item.getCar().getPrice() * item.getQuantity())
                .sum();
    }

    private void setCartAttributes(HttpServletRequest request, HttpSession session,
                                   List<CartItem> cartItems, double total) {
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("total", total);

        int cartCount = cartItems != null ? cartItems.size() : 0;
        session.setAttribute("cartCount", cartCount);

        logger.debug("Cart loaded with {} items", cartCount);
    }

    // ============ UTILITY METHODS ============

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithSuccess(HttpServletResponse response, HttpSession session,
                                     String path, String message) throws IOException {
        session.setAttribute("success", message);
        redirect(response, session.getServletContext().getContextPath() + path);
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithError(HttpServletResponse response, HttpSession session,
                                   String path, String message) throws IOException {
        session.setAttribute("error", message);
        redirect(response, session.getServletContext().getContextPath() + path);
    }

    @SuppressWarnings("SameParameterValue")
    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/Customer/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}