package controller.customer;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test Cases for CartServlet
 * Testing: handleAddToCart, handleUpdateQuantity, handleRemoveItem
 * Coverage: Black-box (EP + BVA) and White-box (Decision + Statement Coverage)
 */
public class CartServletTest {

    private CartServlet cartServlet;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private CarDAO carDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        cartServlet = new CartServlet();

        // Inject mocked DAOs using reflection
        java.lang.reflect.Field cartDAOField = CartServlet.class.getDeclaredField("cartDAO");
        cartDAOField.setAccessible(true);
        cartDAOField.set(cartServlet, cartDAO);

        java.lang.reflect.Field carDAOField = CartServlet.class.getDeclaredField("carDAO");
        carDAOField.setAccessible(true);
        carDAOField.set(cartServlet, carDAO);
    }

    // ============ TEST CASES FOR handleAddToCart ============

    @Test
    @DisplayName("UTC01_AddToCart_Normal_ValidQuantity_Success")
    void testAddToCart_NormalCase_ValidQuantity() throws Exception {
        // Condition: carId = P001, quantity = 5, stock = 100
        // Expected: Return = true, Message = "Product added successfully"

        int userId = 1;
        int carId = 1;
        int quantity = 5;

        Car car = new Car();
        car.setId(carId);
        car.setStock(100);
        car.setPrice(50000);

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");
        verify(response).sendRedirect("/cart");
    }

    @Test
    @DisplayName("UTC02_AddToCart_Boundary_MinQuantity_Success")
    void testAddToCart_BoundaryCase_MinQuantity() throws Exception {
        // Condition: carId = P001, quantity = 1, stock = 100
        // Expected: Return = true, Message = "Product added successfully"

        int userId = 1;
        int carId = 1;
        int quantity = 1;

        Car car = new Car();
        car.setId(carId);
        car.setStock(100);

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");
    }

    @Test
    @DisplayName("UTC03_AddToCart_Boundary_MaxQuantityEqualStock_Success")
    void testAddToCart_BoundaryCase_MaxQuantityEqualStock() throws Exception {
        // Condition: carId = P001, quantity = 100, stock = 100
        // Expected: Return = true, Message = "Product added successfully"

        int userId = 1;
        int carId = 1;
        int quantity = 100;

        Car car = new Car();
        car.setId(carId);
        car.setStock(100);

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");
    }

    @Test
    @DisplayName("UTC04_AddToCart_Abnormal_ZeroQuantity_Failed")
    void testAddToCart_AbnormalCase_ZeroQuantity() throws Exception {
        // Condition: carId = P001, quantity = 0, stock = 100
        // Expected: Exception = InvalidQuantityException, Message error

        int userId = 1;
        int carId = 1;
        int quantity = 0;

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
    }

    @Test
    @DisplayName("UTC05_AddToCart_Abnormal_NegativeQuantity_Failed")
    void testAddToCart_AbnormalCase_NegativeQuantity() throws Exception {
        // Condition: carId = P001, quantity = -5, stock = 100
        // Expected: Exception = InvalidQuantityException

        int userId = 1;
        int carId = 1;
        int quantity = -5;

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
    }

    @Test
    @DisplayName("UTC06_AddToCart_Abnormal_ExceedStock_Failed")
    void testAddToCart_AbnormalCase_QuantityExceedStock() throws Exception {
        // Condition: carId = P001, quantity = 101, stock = 100
        // Expected: Exception = InvalidQuantityException

        int userId = 1;
        int carId = 1;
        int quantity = 101;

        Car car = new Car();
        car.setId(carId);
        car.setStock(100);

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(car);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute(eq("error"), contains("Số lượng xe không đủ"));
    }

    @Test
    @DisplayName("UTC07_AddToCart_Abnormal_NullProduct_Failed")
    void testAddToCart_AbnormalCase_ProductNotFound() throws Exception {
        // Condition: carId = null, quantity = 5
        // Expected: Exception = InvalidArgumentException

        int userId = 1;
        int carId = 999;
        int quantity = 5;

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(null);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Xe không tồn tại!");
    }

    @Test
    @DisplayName("UTC08_AddToCart_Abnormal_EmptyProductID_Failed")
    void testAddToCart_AbnormalCase_EmptyProductID() throws Exception {
        // Condition: carId = empty, quantity = 5
        // Expected: Exception = InvalidArgumentException

        int userId = 1;

        when(request.getParameter("carId")).thenReturn("");
        when(request.getParameter("quantity")).thenReturn("5");
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response, userId);
        });
    }

    @Test
    @DisplayName("UTC09_AddToCart_Boundary_QuantityJustBelowStock_Success")
    void testAddToCart_BoundaryCase_QuantityJustBelowStock() throws Exception {
        // Condition: carId = P001, quantity = 99, stock = 100
        // Expected: Return = true

        int userId = 1;
        int carId = 1;
        int quantity = 99;

        Car car = new Car();
        car.setId(carId);
        car.setStock(100);

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");
    }

    @Test
    @DisplayName("UTC10_AddToCart_Abnormal_UserNotLogin_Failed")
    void testAddToCart_AbnormalCase_UserNotLogin() throws Exception {
        // Condition: User is not logged in (userId = empty)
        // Expected: Exception = InvalidArgumentException

        Integer userId = null;
        int carId = 1;
        int quantity = 5;

        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleAddToCart",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);

        // Will throw NullPointerException when userId is null
        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response, userId);
        });
    }

    // ============ TEST CASES FOR handleUpdateQuantity ============

    @Test
    @DisplayName("UTC01_UpdateQuantity_Normal_ValidUpdate_Success")
    void testUpdateQuantity_NormalCase_ValidUpdate() throws Exception {
        // Condition: cartItemId = 1, quantity = 5, stock = 100
        // Expected: Return = true, Message = "Quantity updated successfully"

        int userId = 1;
        int cartItemId = 1;
        int quantity = 5;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);
        cartItem.setQuantity(3);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã cập nhật số lượng!");
    }

    @Test
    @DisplayName("UTC02_UpdateQuantity_Boundary_MinQuantity_Success")
    void testUpdateQuantity_BoundaryCase_MinQuantity() throws Exception {
        // Condition: cartItemId = 1, quantity = 1, stock = 100
        // Expected: Return = true

        int userId = 1;
        int cartItemId = 1;
        int quantity = 1;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã cập nhật số lượng!");
    }

    @Test
    @DisplayName("UTC03_UpdateQuantity_Boundary_MaxQuantityEqualStock_Success")
    void testUpdateQuantity_BoundaryCase_MaxQuantity() throws Exception {
        // Condition: cartItemId = 1, quantity = 100, stock = 100
        // Expected: Return = true

        int userId = 1;
        int cartItemId = 1;
        int quantity = 100;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã cập nhật số lượng!");
    }

    @Test
    @DisplayName("UTC04_UpdateQuantity_Abnormal_ZeroQuantity_Failed")
    void testUpdateQuantity_AbnormalCase_ZeroQuantity() throws Exception {
        // Condition: cartItemId = 1, quantity = 0
        // Expected: Exception = InvalidQuantityException

        int userId = 1;
        int cartItemId = 1;
        int quantity = 0;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
    }

    @Test
    @DisplayName("UTC05_UpdateQuantity_Abnormal_NegativeQuantity_Failed")
    void testUpdateQuantity_AbnormalCase_NegativeQuantity() throws Exception {
        // Condition: cartItemId = 1, quantity = -5
        // Expected: Exception = InvalidQuantityException

        int userId = 1;
        int cartItemId = 1;
        int quantity = -5;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
    }

    @Test
    @DisplayName("UTC06_UpdateQuantity_Abnormal_ExceedStock_Failed")
    void testUpdateQuantity_AbnormalCase_ExceedStock() throws Exception {
        // Condition: cartItemId = 1, quantity = 101, stock = 100
        // Expected: Exception = InvalidQuantityException

        int userId = 1;
        int cartItemId = 1;
        int quantity = 101;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute(eq("error"), contains("Số lượng không đủ"));
    }

    @Test
    @DisplayName("UTC07_UpdateQuantity_Abnormal_CartItemNotFound_Failed")
    void testUpdateQuantity_AbnormalCase_CartItemNotFound() throws Exception {
        // Condition: cartItemId = 999 (not exist)
        // Expected: Exception = InvalidArgumentException

        int userId = 1;
        int cartItemId = 999;
        int quantity = 5;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Collections.emptyList());

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");
    }

    @Test
    @DisplayName("UTC08_UpdateQuantity_Abnormal_EmptyCartItemID_Failed")
    void testUpdateQuantity_AbnormalCase_EmptyCartItemID() throws Exception {
        // Condition: cartItemId = empty
        // Expected: Exception = InvalidArgumentException

        int userId = 1;

        when(request.getParameter("cartItemId")).thenReturn("");
        when(request.getParameter("quantity")).thenReturn("5");
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response, userId);
        });
    }

    @Test
    @DisplayName("UTC09_UpdateQuantity_Boundary_QuantityJustBelowStock_Success")
    void testUpdateQuantity_BoundaryCase_JustBelowStock() throws Exception {
        // Condition: cartItemId = 1, quantity = 99, stock = 100
        // Expected: Return = true

        int userId = 1;
        int cartItemId = 1;
        int quantity = 99;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("success", "Đã cập nhật số lượng!");
    }

    @Test
    @DisplayName("UTC10_UpdateQuantity_Abnormal_UpdateFailed_Failed")
    void testUpdateQuantity_AbnormalCase_DatabaseUpdateFailed() throws Exception {
        // Condition: Database update returns false
        // Expected: Return = false, error message

        int userId = 1;
        int cartItemId = 1;
        int quantity = 5;

        Car car = new Car();
        car.setStock(100);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setCar(car);

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(java.util.Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleUpdateQuantity",
                HttpServletRequest.class, HttpServletResponse.class, Integer.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response, userId);

        verify(session).setAttribute("error", "Không thể cập nhật số lượng!");
    }

    // ============ TEST CASES FOR handleRemoveItem ============

    @Test
    @DisplayName("UTC01_RemoveItem_Normal_ValidCartItem_Success")
    void testRemoveItem_NormalCase_ValidCartItem() throws Exception {
        // Condition: cartItemId = 1 (exists)
        // Expected: Return = true, Message = "Item removed successfully"

        int cartItemId = 1;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
        verify(response).sendRedirect("/cart");
    }

    @Test
    @DisplayName("UTC02_RemoveItem_Normal_AnotherValidCartItem_Success")
    void testRemoveItem_NormalCase_AnotherValidItem() throws Exception {
        // Condition: cartItemId = 5 (exists)
        // Expected: Return = true, Message = "Item removed successfully"

        int cartItemId = 5;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(true);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
    }

    @Test
    @DisplayName("UTC03_RemoveItem_Abnormal_CartItemNotFound_Failed")
    void testRemoveItem_AbnormalCase_ItemNotFound() throws Exception {
        // Condition: cartItemId = 999 (not exists)
        // Expected: Return = false, Message = "Item not found"

        int cartItemId = 999;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
    }

    @Test
    @DisplayName("UTC04_RemoveItem_Abnormal_NegativeCartItemID_Failed")
    void testRemoveItem_AbnormalCase_NegativeID() throws Exception {
        // Condition: cartItemId = -1
        // Expected: Return = false

        int cartItemId = -1;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
    }

    @Test
    @DisplayName("UTC05_RemoveItem_Abnormal_ZeroCartItemID_Failed")
    void testRemoveItem_AbnormalCase_ZeroID() throws Exception {
        // Condition: cartItemId = 0
        // Expected: Return = false

        int cartItemId = 0;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
    }

    @Test
    @DisplayName("UTC06_RemoveItem_Abnormal_EmptyCartItemID_Exception")
    void testRemoveItem_AbnormalCase_EmptyID() throws Exception {
        // Condition: cartItemId = empty
        // Expected: Exception = NumberFormatException

        when(request.getParameter("cartItemId")).thenReturn("");
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response);
        });
    }

    @Test
    @DisplayName("UTC07_RemoveItem_Abnormal_NullCartItemID_Exception")
    void testRemoveItem_AbnormalCase_NullID() throws Exception {
        // Condition: cartItemId = null
        // Expected: Exception = NullPointerException

        when(request.getParameter("cartItemId")).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response);
        });
    }

    @Test
    @DisplayName("UTC08_RemoveItem_Abnormal_InvalidFormat_Exception")
    void testRemoveItem_AbnormalCase_InvalidFormat() throws Exception {
        // Condition: cartItemId = "abc" (invalid format)
        // Expected: Exception = NumberFormatException

        when(request.getParameter("cartItemId")).thenReturn("abc");
        when(request.getSession()).thenReturn(session);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () -> {
            method.invoke(cartServlet, request, response);
        });
    }

    @Test
    @DisplayName("UTC09_RemoveItem_Boundary_MaxIntValue_Failed")
    void testRemoveItem_BoundaryCase_MaxIntValue() throws Exception {
        // Condition: cartItemId = Integer.MAX_VALUE
        // Expected: Return = false (item not found)

        int cartItemId = Integer.MAX_VALUE;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
    }

    @Test
    @DisplayName("UTC10_RemoveItem_Abnormal_DatabaseError_Failed")
    void testRemoveItem_AbnormalCase_DatabaseError() throws Exception {
        // Condition: Database operation fails
        // Expected: Return = false, error message

        int cartItemId = 1;

        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(session.getServletContext()).thenReturn(mock(jakarta.servlet.ServletContext.class));
        when(session.getServletContext().getContextPath()).thenReturn("");
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        Method method = CartServlet.class.getDeclaredMethod("handleRemoveItem",
                HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);
        method.invoke(cartServlet, request, response);

        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
        verify(response).sendRedirect("/cart");
    }
}