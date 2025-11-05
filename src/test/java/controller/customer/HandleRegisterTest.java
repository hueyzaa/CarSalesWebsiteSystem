package controller.customer;

import dao.AuthDAO;
import service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for handleRegister function
 * Coverage: Black Box (EP, BVA) + White Box (Statement, Decision)
 * UPDATED: All passwords changed to 8+ characters to match new validation
 */
public class HandleRegisterTest {

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

    @Mock
    private EmailService emailService;

    private AuthServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new AuthServlet();

        // Inject mocked dependencies
        try {
            java.lang.reflect.Field authDAOField = AuthServlet.class.getDeclaredField("authDAO");
            authDAOField.setAccessible(true);
            authDAOField.set(servlet, authDAO);

            java.lang.reflect.Field emailServiceField = AuthServlet.class.getDeclaredField("emailService");
            emailServiceField.setAccessible(true);
            emailServiceField.set(servlet, emailService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setup common mocks
        setupCommonMocks();
    }

    /**
     * HELPER METHOD: Setup common mocks for all tests
     */
    private void setupCommonMocks() throws IOException {
        // Mock session
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

        // Mock RequestDispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    // ============= BLACK BOX - EQUIVALENCE PARTITIONING =============

    @Test
    @DisplayName("UTCID01 - Valid registration")
    public void testValidRegistration() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van A");
        when(request.getParameter("email")).thenReturn("newuser@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("newuser@test.com")).thenReturn(false);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(authDAO).emailExists("newuser@test.com");
        verify(session).setAttribute(eq("pendingRegistration"), any(Map.class));
        verify(emailService).sendVerificationEmail(eq(request), eq("newuser@test.com"), eq("Nguyen Van A"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID02 - Email already exists")
    public void testEmailAlreadyExists() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van B");
        when(request.getParameter("email")).thenReturn("existing@test.com");
        when(request.getParameter("phone")).thenReturn("0987654321");
        when(request.getParameter("address")).thenReturn("HCM");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("existing@test.com")).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Email đã được sử dụng"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID03 - Invalid email format")
    public void testInvalidEmailFormat() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van C");
        when(request.getParameter("email")).thenReturn("invalid-email");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Da Nang");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID04 - Empty name")
    public void testEmptyName() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Họ và tên"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID05 - Null name")
    public void testNullName() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID06 - Name too long (>100 chars)")
    public void testNameTooLong() throws ServletException, IOException {
        // Arrange
        String longName = "A".repeat(101);

        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn(longName);
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID07 - Password too short")
    public void testPasswordTooShort() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van D");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Mật khẩu"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID08 - Password mismatch")
    public void testPasswordMismatch() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van E");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass5678");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("không khớp"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID09 - Invalid CSRF token")
    public void testInvalidCsrfToken() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van F");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("invalid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("CSRF"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID10 - Email send failure")
    public void testEmailSendFailure() throws Exception {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Nguyen Van G");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("user@test.com")).thenReturn(false);

        // Mock email service to throw exception
        doThrow(new RuntimeException("Email service error"))
                .when(emailService).sendVerificationEmail(any(), anyString(), anyString(), anyString());

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("email"));
        verify(dispatcher).forward(request, response);
    }

    // ============= BLACK BOX - BOUNDARY VALUE ANALYSIS =============

    @Test
    @DisplayName("UTCID11 - Name exactly 1 char (boundary)")
    public void testNameExactly1Char() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("A");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("user@test.com")).thenReturn(false);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID12 - Name exactly 100 chars (boundary)")
    public void testNameExactly100Chars() throws ServletException, IOException {
        // Arrange
        String name100 = "A".repeat(100);

        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn(name100);
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("user@test.com")).thenReturn(false);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID15 - Password exactly 8 chars (boundary) - SHOULD PASS")
    public void testPasswordExactly8Chars() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass123A");
        when(request.getParameter("confirmPassword")).thenReturn("Pass123A");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("user@test.com")).thenReturn(false);

        // Act
        servlet.doPost(request, response);

        // Assert - Should SUCCESS
        verify(session).setAttribute(eq("pendingRegistration"), any(Map.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID16 - Password exactly 7 chars (boundary) - SHOULD FAIL")
    public void testPasswordExactly7Chars() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("Test User");
        when(request.getParameter("email")).thenReturn("user@test.com");
        when(request.getParameter("phone")).thenReturn("0123456789");
        when(request.getParameter("address")).thenReturn("Ha Noi");
        when(request.getParameter("password")).thenReturn("Pass12A");
        when(request.getParameter("confirmPassword")).thenReturn("Pass12A");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        // Act
        servlet.doPost(request, response);

        // Assert - Should FAIL with error message
        verify(request).setAttribute(eq("error"), contains("Mật khẩu phải có ít nhất 8 ký tự"));
        verify(dispatcher).forward(request, response);
    }

    // ============= WHITE BOX - DECISION COVERAGE =============

    @Test
    @DisplayName("UTCID27 - Decision: authDAO.emailExists() returns true")
    public void testDecisionEmailExistsTrue() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("User");
        when(request.getParameter("email")).thenReturn("existing@test.com");
        when(request.getParameter("phone")).thenReturn("0123");
        when(request.getParameter("address")).thenReturn("HN");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("existing@test.com")).thenReturn(true);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), contains("Email đã được sử dụng"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("UTCID28 - Decision: authDAO.emailExists() returns false")
    public void testDecisionEmailExistsFalse() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/register");
        when(request.getParameter("name")).thenReturn("User");
        when(request.getParameter("email")).thenReturn("new@test.com");
        when(request.getParameter("phone")).thenReturn("0123");
        when(request.getParameter("address")).thenReturn("HN");
        when(request.getParameter("password")).thenReturn("Pass1234");
        when(request.getParameter("confirmPassword")).thenReturn("Pass1234");
        when(request.getParameter("csrfToken")).thenReturn("valid_token");
        when(session.getAttribute("csrfToken")).thenReturn("valid_token");

        when(authDAO.emailExists("new@test.com")).thenReturn(false);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(session).setAttribute(eq("pendingRegistration"), any(Map.class));
        verify(dispatcher).forward(request, response);
    }
}