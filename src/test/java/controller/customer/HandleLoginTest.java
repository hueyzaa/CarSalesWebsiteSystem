package controller.customer;

import dao.AuthDAO;
import model.User;
import util.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for handleLogin function
 * FIXED: Added setupCommonMocks() to prevent NullPointerException
 */
public class HandleLoginTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AuthDAO authDAO;

    private AuthServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new AuthServlet();

        // Inject mocked AuthDAO
        try {
            java.lang.reflect.Field authDAOField = AuthServlet.class.getDeclaredField("authDAO");
            authDAOField.setAccessible(true);
            authDAOField.set(servlet, authDAO);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setupCommonMocks();
    }

    /**
     * HELPER METHOD: Setup common mocks for all tests
     */
    private void setupCommonMocks() throws IOException {
        // Mock session - QUAN TRỌNG!
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);

        // Mock request URL info
        when(request.getScheme()).thenReturn("http");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);
        when(request.getContextPath()).thenReturn("/app");

        // Mock request metadata
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");

        // Mock RequestDispatcher - QUAN TRỌNG!
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

    @Test
    @DisplayName("UTCID01 - Valid customer login")
    public void testValidCustomerLogin() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("user@test.com");
        mockUser.setRole("CUSTOMER");
        mockUser.setActive(true);

        when(authDAO.login("user@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(authDAO).login("user@test.com", "Pass1234");
        verify(session).setAttribute(eq("user"), any(User.class));
        verify(response).sendRedirect(contains("/home"));
    }

    @Test
    @DisplayName("UTCID02 - Valid staff login")
    public void testValidStaffLogin() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("staff@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setEmail("staff@test.com");
        mockUser.setRole("STAFF");
        mockUser.setActive(true);

        when(authDAO.login("staff@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(authDAO).login("staff@test.com", "Pass1234");
        verify(response).sendRedirect(contains("/staff/dashboard"));
    }

    @Test
    @DisplayName("UTCID03 - Valid admin login")
    public void testValidAdminLogin() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("admin@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(3);
        mockUser.setEmail("admin@test.com");
        mockUser.setRole("ADMIN");
        mockUser.setActive(true);

        when(authDAO.login("admin@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(authDAO).login("admin@test.com", "Pass1234");
        verify(response).sendRedirect(contains("/admin/dashboard"));
    }

    @Test
    @DisplayName("UTCID04 - Invalid email format")
    public void testInvalidEmailFormat() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("invalid-email");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID05 - Empty password")
    public void testEmptyPassword() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID06 - Null password")
    public void testNullPassword() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID07 - Wrong password")
    public void testWrongPassword() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("WrongPass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.login("user@test.com", "WrongPass1234")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Email hoặc mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID08 - User not found")
    public void testUserNotFound() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("nonexistent@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.login("nonexistent@test.com", "Pass1234")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Email hoặc mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID09 - Invalid CSRF token")
    public void testInvalidCsrfToken() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("invalid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("CSRF"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID10 - Null CSRF token")
    public void testNullCsrfToken() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn(null);
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("CSRF"));
        verify(dispatcher).forward(request, response);
    }

    // ============= WHITE BOX - DECISION COVERAGE =============

    @Test
    @DisplayName("UTCID20 - Decision: password == null")
    public void testDecisionPasswordNull() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID21 - Decision: password.isEmpty() == true")
    public void testDecisionPasswordEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID22 - Decision: user == null")
    public void testDecisionUserNull() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.login("user@test.com", "Pass1234")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Email hoặc mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID23 - Decision: user.isAdmin() == true")
    public void testDecisionUserIsAdmin() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("admin@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("admin@test.com");
        mockUser.setRole("ADMIN");

        when(authDAO.login("admin@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect(contains("/admin/dashboard"));
    }

    @Test
    @DisplayName("UTCID24 - Decision: user.isStaff() == true")
    public void testDecisionUserIsStaff() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("staff@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setEmail("staff@test.com");
        mockUser.setRole("STAFF");

        when(authDAO.login("staff@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect(contains("/staff/dashboard"));
    }

    @Test
    @DisplayName("UTCID25 - Decision: user is customer (else branch)")
    public void testDecisionUserIsCustomer() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("email")).thenReturn("customer@test.com");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        User mockUser = new User();
        mockUser.setUserId(3);
        mockUser.setEmail("customer@test.com");
        mockUser.setRole("CUSTOMER");

        when(authDAO.login("customer@test.com", "Pass1234")).thenReturn(mockUser);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).sendRedirect(contains("/home"));
    }
}