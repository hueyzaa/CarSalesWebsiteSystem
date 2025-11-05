package controller.customer;

import dao.CarDAO;
import dao.CartDAO;
import model.Car;
import model.CartItem;
import util.SessionUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Unit Test for CartServlet - 3 Functions
 *
 * Functions Tested:
 * 1. handleUpdateQuantity() - UTCID01-UTCID24
 * 2. handleRemoveItem() - UTCID25-UTCID40
 * 3. handleClearCart() - UTCID41-UTCID52
 *
 * Test Coverage: Black Box (EP, BVA) + White Box (Statement, Decision)
 */
class CartServletThreeFunctionsTest {

    private CartServlet cartServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private CarDAO carDAO;

    private static final Integer VALID_USER_ID = 1;
    private static final Integer VALID_CART_ITEM_ID = 10;
    private static final Integer VALID_CAR_ID = 100;
    private static final String CONTEXT_PATH = "/app";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartServlet = new CartServlet();

        // Inject mocked DAOs
        try {
            java.lang.reflect.Field cartDAOField = CartServlet.class.getDeclaredField("cartDAO");
            cartDAOField.setAccessible(true);
            cartDAOField.set(cartServlet, cartDAO);

            java.lang.reflect.Field carDAOField = CartServlet.class.getDeclaredField("carDAO");
            carDAOField.setAccessible(true);
            carDAOField.set(cartServlet, carDAO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mocked DAOs", e);
        }

        // Common mocking setup
        when(request.getContextPath()).thenReturn(CONTEXT_PATH);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(session.getServletContext()).thenReturn(servletContext);
        when(servletContext.getContextPath()).thenReturn(CONTEXT_PATH);
    }

    // =====================================================
    // TEST SUITE 1: handleUpdateQuantity()
    // UTCID01 - UTCID24
    // =====================================================

    @Nested
    @DisplayName("Function 1: handleUpdateQuantity() Tests")
    class HandleUpdateQuantityTests {

        // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

        @Test
        @DisplayName("UTCID01: Valid update with quantity within stock")
        void testUTCID01() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
                verify(session).setAttribute("success", "Đã cập nhật số lượng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID02: Invalid quantity - zero")
        void testUTCID02() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("0");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID03: Invalid quantity - negative")
        void testUTCID03() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("-5");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID04: Cart item not found")
        void testUTCID04() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("999");
                when(request.getParameter("quantity")).thenReturn("5");

                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(new ArrayList<>());

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID05: Quantity exceeds available stock")
        void testUTCID05() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("20");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute(eq("error"), contains("Số lượng không đủ"));
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID06: Update fails in DAO")
        void testUTCID06() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
                verify(session).setAttribute("error", "Không thể cập nhật số lượng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID07: Invalid cartItemId format")
        void testUTCID07() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("invalid");
                when(request.getParameter("quantity")).thenReturn("5");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID08: Invalid quantity format")
        void testUTCID08() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("invalid");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        // ============= BLACK BOX - BOUNDARY VALUE ANALYSIS =============

        @Test
        @DisplayName("UTCID09: Quantity = 1 (minimum valid)")
        void testUTCID09() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("1");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 1)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 1);
                verify(session).setAttribute("success", "Đã cập nhật số lượng!");
            }
        }

        @Test
        @DisplayName("UTCID10: Quantity = 0 (below minimum)")
        void testUTCID10() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("0");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
            }
        }

        @Test
        @DisplayName("UTCID11: Quantity = -1 (negative boundary)")
        void testUTCID11() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("-1");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
            }
        }

        @Test
        @DisplayName("UTCID12: Quantity = availableStock (at maximum boundary)")
        void testUTCID12() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("10");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 10)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 10);
                verify(session).setAttribute("success", "Đã cập nhật số lượng!");
            }
        }

        @Test
        @DisplayName("UTCID13: Quantity = availableStock + 1 (above maximum boundary)")
        void testUTCID13() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("11");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute(eq("error"), contains("Số lượng không đủ"));
            }
        }

        // ============= WHITE BOX - DECISION COVERAGE =============

        @Test
        @DisplayName("UTCID14: Decision - quantity <= 0 (true)")
        void testUTCID14() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("0");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Số lượng phải lớn hơn 0!");
            }
        }

        @Test
        @DisplayName("UTCID15: Decision - quantity <= 0 (false)")
        void testUTCID15() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
            }
        }

        @Test
        @DisplayName("UTCID16: Decision - targetItem == null (true)")
        void testUTCID16() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("999");
                when(request.getParameter("quantity")).thenReturn("5");

                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(new ArrayList<>());

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");
            }
        }

        @Test
        @DisplayName("UTCID17: Decision - targetItem == null (false)")
        void testUTCID17() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
            }
        }

        @Test
        @DisplayName("UTCID18: Decision - quantity > availableStock (true)")
        void testUTCID18() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("20");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute(eq("error"), contains("Số lượng không đủ"));
            }
        }

        @Test
        @DisplayName("UTCID19: Decision - quantity > availableStock (false)")
        void testUTCID19() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
            }
        }

        @Test
        @DisplayName("UTCID20: Decision - cartDAO.updateCartItem returns true")
        void testUTCID20() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("success", "Đã cập nhật số lượng!");
            }
        }

        @Test
        @DisplayName("UTCID21: Decision - cartDAO.updateCartItem returns false")
        void testUTCID21() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Không thể cập nhật số lượng!");
            }
        }

        // ============= WHITE BOX - STATEMENT COVERAGE =============

        @Test
        @DisplayName("UTCID22: All statements - success path")
        void testUTCID22() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(request.getParameter("quantity")).thenReturn("5");

                CartItem cartItem = createMockCartItem(VALID_CART_ITEM_ID, VALID_CAR_ID, 10, 50000.0);
                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(Arrays.asList(cartItem));
                when(cartDAO.updateCartItem(VALID_CART_ITEM_ID, 5)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers all statements in success path
                verify(cartDAO).getCartItemsByUserId(VALID_USER_ID);
                verify(cartDAO).updateCartItem(VALID_CART_ITEM_ID, 5);
                verify(session).setAttribute("success", "Đã cập nhật số lượng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID23: All statements - findCartItem returns null")
        void testUTCID23() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("999");
                when(request.getParameter("quantity")).thenReturn("5");

                when(cartDAO.getCartItemsByUserId(VALID_USER_ID)).thenReturn(new ArrayList<>());

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers findCartItem null branch
                verify(cartDAO).getCartItemsByUserId(VALID_USER_ID);
                verify(cartDAO, never()).updateCartItem(anyInt(), anyInt());
                verify(session).setAttribute("error", "Không tìm thấy sản phẩm trong giỏ hàng!");
            }
        }

        @Test
        @DisplayName("UTCID24: All statements - exception handling")
        void testUTCID24() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("update");
                when(request.getParameter("cartItemId")).thenReturn("invalid");
                when(request.getParameter("quantity")).thenReturn("5");

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers exception handling
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
            }
        }
    }

    // =====================================================
    // TEST SUITE 2: handleRemoveItem()
    // UTCID25 - UTCID40
    // =====================================================

    @Nested
    @DisplayName("Function 2: handleRemoveItem() Tests")
    class HandleRemoveItemTests {

        // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

        @Test
        @DisplayName("UTCID25: Valid remove operation")
        void testUTCID25() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(VALID_CART_ITEM_ID);
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID26: Remove fails in DAO")
        void testUTCID26() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(VALID_CART_ITEM_ID);
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID27: Invalid cartItemId format")
        void testUTCID27() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("invalid");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).removeCartItem(anyInt());
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID28: Null cartItemId")
        void testUTCID28() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn(null);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).removeCartItem(anyInt());
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
            }
        }

        @Test
        @DisplayName("UTCID29: Non-existent cartItemId")
        void testUTCID29() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("999");
                when(cartDAO.removeCartItem(999)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(999);
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
            }
        }

        // ============= BLACK BOX - BOUNDARY VALUE ANALYSIS =============

        @Test
        @DisplayName("UTCID30: CartItemId = 1 (minimum valid ID)")
        void testUTCID30() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("1");
                when(cartDAO.removeCartItem(1)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(1);
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
            }
        }

        @Test
        @DisplayName("UTCID31: CartItemId = 0 (invalid boundary)")
        void testUTCID31() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("0");
                when(cartDAO.removeCartItem(0)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(0);
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
            }
        }

        @Test
        @DisplayName("UTCID32: CartItemId = -1 (negative boundary)")
        void testUTCID32() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("-1");
                when(cartDAO.removeCartItem(-1)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(-1);
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
            }
        }

        @Test
        @DisplayName("UTCID33: CartItemId = Integer.MAX_VALUE (maximum boundary)")
        void testUTCID33() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn(String.valueOf(Integer.MAX_VALUE));
                when(cartDAO.removeCartItem(Integer.MAX_VALUE)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).removeCartItem(Integer.MAX_VALUE);
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
            }
        }

        // ============= WHITE BOX - DECISION COVERAGE =============

        @Test
        @DisplayName("UTCID34: Decision - cartDAO.removeCartItem returns true")
        void testUTCID34() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID35: Decision - cartDAO.removeCartItem returns false")
        void testUTCID35() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        // ============= WHITE BOX - STATEMENT COVERAGE =============

        @Test
        @DisplayName("UTCID36: All statements - success path")
        void testUTCID36() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers all statements in success path
                verify(cartDAO).removeCartItem(VALID_CART_ITEM_ID);
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID37: All statements - failure path")
        void testUTCID37() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers all statements in failure path
                verify(cartDAO).removeCartItem(VALID_CART_ITEM_ID);
                verify(session).setAttribute("error", "Không thể xóa sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID38: Exception handling - NumberFormatException")
        void testUTCID38() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("abc123");

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers exception handling branch
                verify(cartDAO, never()).removeCartItem(anyInt());
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
            }
        }

        @Test
        @DisplayName("UTCID39: All statements - logging and session management")
        void testUTCID39() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("10");
                when(cartDAO.removeCartItem(VALID_CART_ITEM_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert - verify session interaction
                verify(request).getSession();
                verify(session).setAttribute("success", "Đã xóa khỏi giỏ hàng!");
            }
        }

        @Test
        @DisplayName("UTCID40: Edge case - empty string cartItemId")
        void testUTCID40() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("remove");
                when(request.getParameter("cartItemId")).thenReturn("");

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO, never()).removeCartItem(anyInt());
                verify(session).setAttribute("error", "Dữ liệu không hợp lệ!");
            }
        }
    }

    // =====================================================
    // TEST SUITE 3: handleClearCart()
    // UTCID41 - UTCID52
    // =====================================================

    @Nested
    @DisplayName("Function 3: handleClearCart() Tests")
    class HandleClearCartTests {

        // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

        @Test
        @DisplayName("UTCID41: Valid clear cart operation")
        void testUTCID41() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID42: Clear cart fails in DAO")
        void testUTCID42() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("error", "Không thể xóa giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID43: Clear empty cart")
        void testUTCID43() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }

        @Test
        @DisplayName("UTCID44: Clear cart with multiple items")
        void testUTCID44() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }

        @Test
        @DisplayName("UTCID45: Clear cart for different user IDs")
        void testUTCID45() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                mockedSessionUtils.when(() -> SessionUtils.isLoggedIn(session)).thenReturn(true);
                mockedSessionUtils.when(() -> SessionUtils.getUserId(session)).thenReturn(2);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(2)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(2);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }

        // ============= BLACK BOX - BOUNDARY VALUE ANALYSIS =============

        @Test
        @DisplayName("UTCID46: UserId = 1 (minimum valid)")
        void testUTCID46() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                mockedSessionUtils.when(() -> SessionUtils.isLoggedIn(session)).thenReturn(true);
                mockedSessionUtils.when(() -> SessionUtils.getUserId(session)).thenReturn(1);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(1)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(1);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }

        @Test
        @DisplayName("UTCID47: UserId = Integer.MAX_VALUE (maximum boundary)")
        void testUTCID47() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                mockedSessionUtils.when(() -> SessionUtils.isLoggedIn(session)).thenReturn(true);
                mockedSessionUtils.when(() -> SessionUtils.getUserId(session)).thenReturn(Integer.MAX_VALUE);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(Integer.MAX_VALUE)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(cartDAO).clearCart(Integer.MAX_VALUE);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }

        // ============= WHITE BOX - DECISION COVERAGE =============

        @Test
        @DisplayName("UTCID48: Decision - cartDAO.clearCart returns true")
        void testUTCID48() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID49: Decision - cartDAO.clearCart returns false")
        void testUTCID49() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert
                verify(session).setAttribute("error", "Không thể xóa giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        // ============= WHITE BOX - STATEMENT COVERAGE =============

        @Test
        @DisplayName("UTCID50: All statements - success path")
        void testUTCID50() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers all statements in success path
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID51: All statements - failure path")
        void testUTCID51() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(false);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers all statements in failure path
                verify(cartDAO).clearCart(VALID_USER_ID);
                verify(session).setAttribute("error", "Không thể xóa giỏ hàng!");
                verify(response).sendRedirect(CONTEXT_PATH + "/cart");
            }
        }

        @Test
        @DisplayName("UTCID52: Integration with session management")
        void testUTCID52() throws Exception {
            // Arrange
            try (MockedStatic<SessionUtils> mockedSessionUtils = mockStatic(SessionUtils.class)) {
                setupLoggedInUser(mockedSessionUtils);

                when(request.getParameter("action")).thenReturn("clear");
                when(cartDAO.clearCart(VALID_USER_ID)).thenReturn(true);

                // Act
                cartServlet.doPost(request, response);

                // Assert - covers session attribute setting
                verify(request).getSession();
                verify(session).setAttribute("success", "Đã xóa tất cả sản phẩm!");
            }
        }
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private void setupLoggedInUser(MockedStatic<SessionUtils> mockedSessionUtils) {
        mockedSessionUtils.when(() -> SessionUtils.isLoggedIn(session)).thenReturn(true);
        mockedSessionUtils.when(() -> SessionUtils.getUserId(session)).thenReturn(VALID_USER_ID);
    }

    private Car createMockCar(int id, int stock, double price) {
        Car car = mock(Car.class);
        when(car.getId()).thenReturn(id);
        when(car.getStock()).thenReturn(stock);
        when(car.getPrice()).thenReturn(price);
        when(car.getName()).thenReturn("Test Car " + id);
        return car;
    }

    private CartItem createMockCartItem(int id, int carId, int stock, double price) {
        CartItem cartItem = mock(CartItem.class);
        Car car = createMockCar(carId, stock, price);
        when(cartItem.getId()).thenReturn(id);
        when(cartItem.getCar()).thenReturn(car);
        when(cartItem.getQuantity()).thenReturn(2);
        return cartItem;
    }
}