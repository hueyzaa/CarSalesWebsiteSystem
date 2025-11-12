package controller.customer;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import model.User;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test Cases for CartServlet
 * 30 Test Cases: 10 per function (Add, Update, Remove)
 * Naming Convention: UTC## (Unit Test Case)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartServletTestFinal {

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

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private User mockUser;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        cartServlet = new CartServlet();

        // Inject mocked DAOs
        injectMock("cartDAO", cartDAO);
        injectMock("carDAO", carDAO);

        // Create mock user
        mockUser = createMockUser(1, "test@example.com", "Test User", "customer");

        // Setup common mocks
        setupCommonMocks();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    private void injectMock(String fieldName, Object mock) throws Exception {
        java.lang.reflect.Field field = CartServlet.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(cartServlet, mock);
    }

    private void setupCommonMocks() {
        when(servletContext.getContextPath()).thenReturn("/myapp");
        when(session.getServletContext()).thenReturn(servletContext);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getContextPath()).thenReturn("/myapp");
    }

    private User createMockUser(int userId, String email, String name, String role) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        user.setActive(true);
        return user;
    }

    // ========================================================================
    // FUNCTION 1: ADD TO CART - 10 TEST CASES (UTC01-UTC10)
    // ========================================================================

    @Test
    @Order(1)
    @DisplayName("UTC01_N: Add valid product with normal quantity (5) - Success")
    void UTC01_AddToCart_NormalCase_ValidQuantity() throws IOException {
        // Arrange
        int userId = 1;
        int carId = 100;
        int quantity = 5;
        int stock = 100;

        Car car = createCar(carId, "Tesla Model 3", 50000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(carDAO).getCarById(carId);
        verify(cartDAO).addToCart(userId, carId, quantity);
        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC01 Passed: Normal case - Valid quantity");
    }

    @Test
    @Order(2)
    @DisplayName("UTC02_B: Add product with minimum quantity (1) - Success")
    void UTC02_AddToCart_BoundaryCase_MinimumQuantity() throws IOException {
        // Arrange
        int userId = 1;
        int carId = 100;
        int quantity = 1; // Boundary: Minimum
        int stock = 50;

        Car car = createCar(carId, "Honda Civic", 30000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).addToCart(userId, carId, quantity);
        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");

        System.out.println("✅ UTC02 Passed: Boundary case - Minimum quantity (1)");
    }

    @Test
    @Order(3)
    @DisplayName("UTC03_B: Add product with quantity equal to stock - Success")
    void UTC03_AddToCart_BoundaryCase_QuantityEqualsStock() throws IOException {
        // Arrange
        int userId = 1;
        int carId = 100;
        int quantity = 50; // Boundary: Equal to stock
        int stock = 50;

        Car car = createCar(carId, "BMW X5", 70000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).addToCart(userId, carId, quantity);
        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");

        System.out.println("✅ UTC03 Passed: Boundary case - Quantity equals stock");
    }

    @Test
    @Order(4)
    @DisplayName("UTC04_B: Add product with quantity just below stock (99) - Success")
    void UTC04_AddToCart_BoundaryCase_QuantityBelowStock() throws IOException {
        // Arrange
        int userId = 1;
        int carId = 100;
        int stock = 100;
        int quantity = 99; // Boundary: Just below stock

        Car car = createCar(carId, "Audi A4", 45000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);
        when(cartDAO.addToCart(userId, carId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).addToCart(userId, carId, quantity);
        verify(session).setAttribute("success", "Đã thêm vào giỏ hàng!");

        System.out.println("✅ UTC04 Passed: Boundary case - Quantity just below stock");
    }

    @Test
    @Order(5)
    @DisplayName("UTC05_A: Add product with zero quantity - Failed")
    void UTC05_AddToCart_AbnormalCase_ZeroQuantity() throws IOException {
        // Arrange
        int carId = 100;
        int quantity = 0; // Abnormal: Zero
        int stock = 50;

        Car car = createCar(carId, "Toyota Camry", 35000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(carDAO).getCarById(carId);
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(session).setAttribute(eq("error"), argThat(msg ->
                msg != null && msg.toString().contains("Số lượng phải lớn hơn 0")
        ));

        System.out.println("✅ UTC05 Passed: Abnormal case - Zero quantity rejected");
    }

    @Test
    @Order(6)
    @DisplayName("UTC06_A: Add product with negative quantity - Failed")
    void UTC06_AddToCart_AbnormalCase_NegativeQuantity() throws IOException {
        // Arrange
        int carId = 100;
        int quantity = -5; // Abnormal: Negative
        int stock = 50;

        Car car = createCar(carId, "Ford Mustang", 55000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(carDAO).getCarById(carId);
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(session).setAttribute(eq("error"), argThat(msg ->
                msg != null && msg.toString().contains("Số lượng phải lớn hơn 0")
        ));

        System.out.println("✅ UTC06 Passed: Abnormal case - Negative quantity rejected");
    }

    @Test
    @Order(7)
    @DisplayName("UTC07_A: Add product with quantity exceeding stock - Failed")
    void UTC07_AddToCart_AbnormalCase_QuantityExceedsStock() throws IOException {
        // Arrange
        int carId = 100;
        int stock = 10;
        int quantity = 11; // Abnormal: Exceeds stock

        Car car = createCar(carId, "Mercedes C-Class", 60000.0, stock);

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(car);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(carDAO).getCarById(carId);
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(session).setAttribute(eq("error"), argThat(msg -> {
            String message = msg != null ? msg.toString() : "";
            return message.contains("Số lượng xe không đủ") &&
                    message.contains(String.valueOf(stock));
        }));

        System.out.println("✅ UTC07 Passed: Abnormal case - Quantity exceeds stock");
    }

    @Test
    @Order(8)
    @DisplayName("UTC08_A: Add non-existent product - Failed")
    void UTC08_AddToCart_AbnormalCase_ProductNotFound() throws IOException {
        // Arrange
        int carId = 999; // Abnormal: Non-existent
        int quantity = 5;

        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn(String.valueOf(carId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(carDAO.getCarById(carId)).thenReturn(null);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(carDAO).getCarById(carId);
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(session).setAttribute("error", "Xe không tồn tại!");
        verify(response).sendRedirect("/myapp/cars");

        System.out.println("✅ UTC08 Passed: Abnormal case - Product not found");
    }

    @Test
    @Order(9)
    @DisplayName("UTC09_A: Add product with invalid carId format - Failed")
    void UTC09_AddToCart_AbnormalCase_InvalidCarIdFormat() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("carId")).thenReturn("invalid"); // Abnormal: Invalid format
        when(request.getParameter("quantity")).thenReturn("5");

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(session).setAttribute(eq("error"), eq("Dữ liệu không hợp lệ!"));
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC09 Passed: Abnormal case - Invalid carId format");
    }

    @Test
    @Order(10)
    @DisplayName("UTC10_A: Add product when user not logged in - Failed")
    void UTC10_AddToCart_AbnormalCase_UserNotLoggedIn() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("add");
        when(request.getSession(false)).thenReturn(null); // Abnormal: Not logged in

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).addToCart(anyInt(), anyInt(), anyInt());
        verify(response).sendRedirect("/myapp/login");

        System.out.println("✅ UTC10 Passed: Abnormal case - User not logged in");
    }

    // ========================================================================
    // FUNCTION 2: UPDATE QUANTITY - 10 TEST CASES (UTC11-UTC20)
    // ========================================================================

    @Test
    @Order(11)
    @DisplayName("UTC11_N: Update cart item with valid quantity (7) - Success")
    void UTC11_UpdateQuantity_NormalCase_ValidUpdate() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int quantity = 7;
        int stock = 50;

        Car car = createCar(100, "Nissan Altima", 32000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 3);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).getCartItemsByUserId(userId);
        verify(cartDAO).updateCartItem(cartItemId, quantity);
        verify(session).setAttribute("success", "Đã cập nhật số lượng!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC11 Passed: Normal case - Valid update");
    }

    @Test
    @Order(12)
    @DisplayName("UTC12_B: Update quantity to minimum (1) - Success")
    void UTC12_UpdateQuantity_BoundaryCase_MinimumQuantity() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int quantity = 1; // Boundary: Minimum
        int stock = 50;

        Car car = createCar(100, "Chevrolet Malibu", 31000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 5);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).updateCartItem(cartItemId, quantity);
        verify(session).setAttribute("success", "Đã cập nhật số lượng!");

        System.out.println("✅ UTC12 Passed: Boundary case - Minimum quantity (1)");
    }

    @Test
    @Order(13)
    @DisplayName("UTC13_B: Update quantity to equal stock - Success")
    void UTC13_UpdateQuantity_BoundaryCase_QuantityEqualsStock() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int stock = 20;
        int quantity = 20; // Boundary: Equal to stock

        Car car = createCar(100, "Hyundai Sonata", 29000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 10);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).updateCartItem(cartItemId, quantity);
        verify(session).setAttribute("success", "Đã cập nhật số lượng!");

        System.out.println("✅ UTC13 Passed: Boundary case - Quantity equals stock");
    }

    @Test
    @Order(14)
    @DisplayName("UTC14_B: Update quantity just below stock (19) - Success")
    void UTC14_UpdateQuantity_BoundaryCase_QuantityBelowStock() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int stock = 20;
        int quantity = 19; // Boundary: Just below stock

        Car car = createCar(100, "Kia Optima", 28000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 5);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).updateCartItem(cartItemId, quantity);
        verify(session).setAttribute("success", "Đã cập nhật số lượng!");

        System.out.println("✅ UTC14 Passed: Boundary case - Quantity just below stock");
    }

    @Test
    @Order(15)
    @DisplayName("UTC15_A: Update quantity to zero - Failed")
    void UTC15_UpdateQuantity_AbnormalCase_ZeroQuantity() throws IOException {
        // Arrange
        int cartItemId = 10;
        int quantity = 0; // Abnormal: Zero

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC15 Passed: Abnormal case - Zero quantity rejected");
    }

    @Test
    @Order(16)
    @DisplayName("UTC16_A: Update quantity to negative value - Failed")
    void UTC16_UpdateQuantity_AbnormalCase_NegativeQuantity() throws IOException {
        // Arrange
        int cartItemId = 10;
        int quantity = -3; // Abnormal: Negative

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
        verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");

        System.out.println("✅ UTC16 Passed: Abnormal case - Negative quantity rejected");
    }

    @Test
    @Order(17)
    @DisplayName("UTC17_A: Update quantity exceeding stock - Failed")
    void UTC17_UpdateQuantity_AbnormalCase_QuantityExceedsStock() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int stock = 15;
        int quantity = 16; // Abnormal: Exceeds stock

        Car car = createCar(100, "Mazda 6", 33000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 5);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).getCartItemsByUserId(userId);
        verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
        verify(session).setAttribute(eq("error"), argThat(msg -> {
            String message = msg != null ? msg.toString() : "";
            return message.contains("Số lượng không đủ") &&
                    message.contains(String.valueOf(stock));
        }));

        System.out.println("✅ UTC17 Passed: Abnormal case - Quantity exceeds stock");
    }

    @Test
    @Order(18)
    @DisplayName("UTC18_A: Update non-existent cart item - Failed")
    void UTC18_UpdateQuantity_AbnormalCase_CartItemNotFound() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 999; // Abnormal: Non-existent
        int quantity = 5;

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).getCartItemsByUserId(userId);
        verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
        verify(session).setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");

        System.out.println("✅ UTC18 Passed: Abnormal case - Cart item not found");
    }

    @Test
    @Order(19)
    @DisplayName("UTC19_A: Update with invalid cartItemId format - Failed")
    void UTC19_UpdateQuantity_AbnormalCase_InvalidFormat() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn("invalid"); // Abnormal: Invalid format
        when(request.getParameter("quantity")).thenReturn("5");

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
        verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");

        System.out.println("✅ UTC19 Passed: Abnormal case - Invalid format");
    }

    @Test
    @Order(20)
    @DisplayName("UTC20_A: Update when database operation fails - Failed")
    void UTC20_UpdateQuantity_AbnormalCase_DatabaseFailure() throws IOException {
        // Arrange
        int userId = 1;
        int cartItemId = 10;
        int quantity = 5;
        int stock = 50;

        Car car = createCar(100, "Subaru Legacy", 34000.0, stock);
        CartItem cartItem = createCartItem(cartItemId, car, 3);

        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));
        when(cartDAO.getCartItemsByUserId(userId)).thenReturn(Arrays.asList(cartItem));
        when(cartDAO.updateCartItem(cartItemId, quantity)).thenReturn(false); // Abnormal: DB fails

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).updateCartItem(cartItemId, quantity);
        verify(session).setAttribute("error", "Không thể cập nhật số lượng!");

        System.out.println("✅ UTC20 Passed: Abnormal case - Database failure");
    }

    // ========================================================================
    // FUNCTION 3: REMOVE ITEM - 10 TEST CASES (UTC21-UTC30)
    // ========================================================================

    @Test
    @Order(21)
    @DisplayName("UTC21_N: Remove valid cart item (ID: 10) - Success")
    void UTC21_RemoveItem_NormalCase_ValidItem() throws IOException {
        // Arrange
        int cartItemId = 10;

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC21 Passed: Normal case - Valid item removed");
    }

    @Test
    @Order(22)
    @DisplayName("UTC22_N: Remove another valid cart item (ID: 5) - Success")
    void UTC22_RemoveItem_NormalCase_AnotherValidItem() throws IOException {
        // Arrange
        int cartItemId = 5;

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");

        System.out.println("✅ UTC22 Passed: Normal case - Another valid item removed");
    }

    @Test
    @Order(23)
    @DisplayName("UTC23_B: Remove item with minimum valid ID (1) - Success")
    void UTC23_RemoveItem_BoundaryCase_MinimumId() throws IOException {
        // Arrange
        int cartItemId = 1; // Boundary: Minimum valid ID

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(true);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");

        System.out.println("✅ UTC23 Passed: Boundary case - Minimum ID");
    }

    @Test
    @Order(24)
    @DisplayName("UTC24_A: Remove non-existent cart item - Failed")
    void UTC24_RemoveItem_AbnormalCase_ItemNotFound() throws IOException {
        // Arrange
        int cartItemId = 999; // Abnormal: Non-existent

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC24 Passed: Abnormal case - Item not found");
    }

    @Test
    @Order(25)
    @DisplayName("UTC25_A: Remove item with negative ID - Failed")
    void UTC25_RemoveItem_AbnormalCase_NegativeId() throws IOException {
        // Arrange
        int cartItemId = -1; // Abnormal: Negative ID

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");

        System.out.println("✅ UTC25 Passed: Abnormal case - Negative ID");
    }

    @Test
    @Order(26)
    @DisplayName("UTC26_A: Remove item with zero ID - Failed")
    void UTC26_RemoveItem_AbnormalCase_ZeroId() throws IOException {
        // Arrange
        int cartItemId = 0; // Abnormal: Zero ID

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false);

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");

        System.out.println("✅ UTC26 Passed: Abnormal case - Zero ID");
    }

    @Test
    @Order(27)
    @DisplayName("UTC27_A: Remove item with invalid format ID - Failed")
    void UTC27_RemoveItem_AbnormalCase_InvalidFormat() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn("invalid"); // Abnormal: Invalid format

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).removeCartItem(anyInt());
        verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
        verify(response).sendRedirect("/myapp/cart");

        System.out.println("✅ UTC27 Passed: Abnormal case - Invalid format");
    }

    @Test
    @Order(28)
    @DisplayName("UTC28_A: Remove item with null ID - Failed")
    void UTC28_RemoveItem_AbnormalCase_NullId() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(null); // Abnormal: Null

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).removeCartItem(anyInt());
        verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");

        System.out.println("✅ UTC28 Passed: Abnormal case - Null ID");
    }

    @Test
    @Order(29)
    @DisplayName("UTC29_A: Remove item when database operation fails - Failed")
    void UTC29_RemoveItem_AbnormalCase_DatabaseFailure() throws IOException {
        // Arrange
        int cartItemId = 10;

        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("cartItemId")).thenReturn(String.valueOf(cartItemId));
        when(cartDAO.removeCartItem(cartItemId)).thenReturn(false); // Abnormal: DB fails

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO).removeCartItem(cartItemId);
        verify(session).setAttribute("error", "Không thể xóa sản phẩm!");

        System.out.println("✅ UTC29 Passed: Abnormal case - Database failure");
    }

    @Test
    @Order(30)
    @DisplayName("UTC30_A: Remove item when user not logged in - Failed")
    void UTC30_RemoveItem_AbnormalCase_UserNotLoggedIn() throws IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getSession(false)).thenReturn(null); // Abnormal: Not logged in

        // Act
        cartServlet.doPost(request, response);

        // Assert
        verify(cartDAO, never()).removeCartItem(anyInt());
        verify(response).sendRedirect("/myapp/login");

        System.out.println("✅ UTC30 Passed: Abnormal case - User not logged in");
    }

    // ============ HELPER METHODS ============

    private Car createCar(int id, String name, double price, int stock) {
        Car car = new Car();
        car.setId(id);
        car.setName(name);
        car.setPrice(price);
        car.setStock(stock);
        return car;
    }

    private CartItem createCartItem(int id, Car car, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setCar(car);
        item.setQuantity(quantity);
        return item;
    }
}