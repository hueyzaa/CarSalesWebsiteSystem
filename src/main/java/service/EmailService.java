package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * EmailService - Loads config from .env file (separate from db.properties)
 */
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // Email configuration
    private static String SMTP_HOST;
    private static String SMTP_PORT;
    private static String EMAIL_USERNAME;
    private static String EMAIL_PASSWORD;
    private static String EMAIL_FROM;
    private static String EMAIL_FROM_NAME;
    private static String APP_NAME;
    private static String APP_URL;
    private static int TOKEN_EXPIRY_HOURS;
    private static int PASSWORD_RESET_EXPIRY_HOURS;

    static {
        loadConfig();
    }

    /**
     * Load configuration from .env file (NOT db.properties)
     */
    private static void loadConfig() {
        Properties env = new Properties();
        try {
            // Try loading from resources
            InputStream is = EmailService.class.getClassLoader().getResourceAsStream(".env");
            if (is != null) {
                // Read .env file line by line
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    parseLine(line, env);
                }
                reader.close();
                logger.info("Loaded .env from resources");
            } else {
                // Try loading from project root
                BufferedReader reader = new BufferedReader(new FileReader(".env"));
                String line;
                while ((line = reader.readLine()) != null) {
                    parseLine(line, env);
                }
                reader.close();
                logger.info("Loaded .env from project root");
            }

            // Load email configuration
            SMTP_HOST = env.getProperty("EMAIL_SMTP_HOST", "smtp.gmail.com");
            SMTP_PORT = env.getProperty("EMAIL_SMTP_PORT", "587");
            EMAIL_USERNAME = env.getProperty("EMAIL_USERNAME", "");
            EMAIL_PASSWORD = env.getProperty("EMAIL_PASSWORD", "");
            EMAIL_FROM = env.getProperty("EMAIL_FROM", "noreply@carshowroom.com");
            EMAIL_FROM_NAME = env.getProperty("EMAIL_FROM_NAME", "Car Showroom");

            // Load application configuration
            APP_NAME = env.getProperty("APP_NAME", "Car Showroom");
            APP_URL = env.getProperty("APP_URL", "http://localhost:8080");

            // Load security configuration
            TOKEN_EXPIRY_HOURS = Integer.parseInt(env.getProperty("TOKEN_EXPIRY_HOURS", "24"));
            PASSWORD_RESET_EXPIRY_HOURS = Integer.parseInt(env.getProperty("PASSWORD_RESET_EXPIRY_HOURS", "1"));

            logger.info("Email config loaded - Host: {}, Port: {}, From: {}",
                    SMTP_HOST, SMTP_PORT, EMAIL_FROM);

        } catch (Exception e) {
            logger.error("Failed to load .env file: {}", e.getMessage());
            // Set defaults
            SMTP_HOST = "smtp.gmail.com";
            SMTP_PORT = "587";
            EMAIL_FROM = "noreply@carshowroom.com";
            EMAIL_FROM_NAME = "Car Showroom";
            APP_NAME = "Car Showroom";
            APP_URL = "http://localhost:8080";
            TOKEN_EXPIRY_HOURS = 24;
            PASSWORD_RESET_EXPIRY_HOURS = 1;
        }
    }

    /**
     * Parse single line from .env file
     */
    private static void parseLine(String line, Properties props) {
        line = line.trim();

        // Skip empty lines and comments
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }

        // Parse KEY=VALUE
        int equalIndex = line.indexOf('=');
        if (equalIndex > 0) {
            String key = line.substring(0, equalIndex).trim();
            String value = line.substring(equalIndex + 1).trim();

            // Remove quotes if present
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            props.setProperty(key, value);
        }
    }

    /**
     * Send HTML email
     */
    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, EMAIL_FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            logger.info("Email sent successfully to: {}", to);

        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    /**
     * Send verification email using JSP template
     */
    public void sendVerificationEmail(HttpServletRequest request, String email,
                                      String name, String verificationUrl) {
        try {
            // Set attributes for JSP
            request.setAttribute("userName", name);
            request.setAttribute("verificationUrl", verificationUrl);
            request.setAttribute("appName", APP_NAME);
            request.setAttribute("tokenExpiryHours", TOKEN_EXPIRY_HOURS);

            // Render JSP to HTML
            String htmlContent = renderJSP(request, "/WEB-INF/email-templates/verification-email.jsp");

            // Send email
            String subject = "Xác thực tài khoản - " + APP_NAME;
            sendEmail(email, subject, htmlContent);

            logger.info("Verification email sent to: {}", email);

        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", email, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    /**
     * Send password reset email using JSP template
     */
    public void sendPasswordResetEmail(HttpServletRequest request, String email,
                                       String name, String resetUrl) {
        try {
            // Set attributes for JSP
            request.setAttribute("userName", name);
            request.setAttribute("resetUrl", resetUrl);
            request.setAttribute("appName", APP_NAME);
            request.setAttribute("resetExpiryHours", PASSWORD_RESET_EXPIRY_HOURS);

            // Render JSP to HTML
            String htmlContent = renderJSP(request, "/WEB-INF/email-templates/password-reset-email.jsp");

            // Send email
            String subject = "Đặt lại mật khẩu - " + APP_NAME;
            sendEmail(email, subject, htmlContent);

            logger.info("Password reset email sent to: {}", email);

        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    /**
     * Render JSP template to HTML string
     */
    private String renderJSP(HttpServletRequest request, String jspPath) {
        try {
            StringWriter writer = new StringWriter();
            HttpServletResponse dummyResponse = new ResponseWrapper(writer);
            request.getRequestDispatcher(jspPath).include(request, dummyResponse);
            return writer.toString();
        } catch (Exception e) {
            logger.error("Failed to render JSP: {}", jspPath, e);
            throw new RuntimeException("Failed to render email template: " + jspPath, e);
        }
    }

    /**
     * Response wrapper to capture JSP output
     */
    private static class ResponseWrapper implements HttpServletResponse {
        private final StringWriter writer;
        private final PrintWriter printWriter;

        public ResponseWrapper(StringWriter writer) {
            this.writer = writer;
            this.printWriter = new PrintWriter(writer);
        }

        @Override
        public PrintWriter getWriter() {
            return printWriter;
        }

        @Override
        public String toString() {
            return writer.toString();
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // Not needed
                }

                @Override
                public void write(int b) throws IOException {
                    writer.write(b);
                }
            };
        }

        // Required implementations
        @Override public void setContentType(String type) {}
        @Override public void setCharacterEncoding(String charset) {}
        @Override public String getCharacterEncoding() { return "UTF-8"; }
        @Override public String getContentType() { return "text/html"; }
        @Override public void setContentLength(int len) {}
        @Override public void setContentLengthLong(long len) {}
        @Override public void setBufferSize(int size) {}
        @Override public int getBufferSize() { return 0; }
        @Override public void flushBuffer() {}
        @Override public void resetBuffer() {}
        @Override public boolean isCommitted() { return false; }
        @Override public void reset() {}
        @Override public void setLocale(java.util.Locale loc) {}
        @Override public java.util.Locale getLocale() { return java.util.Locale.getDefault(); }
        @Override public void addCookie(jakarta.servlet.http.Cookie cookie) {}
        @Override public boolean containsHeader(String name) { return false; }
        @Override public String encodeURL(String url) { return url; }
        @Override public String encodeRedirectURL(String url) { return url; }
        @Override public void sendError(int sc, String msg) {}
        @Override public void sendError(int sc) {}
        @Override public void sendRedirect(String location) {}

        @Override
        public void sendRedirect(String location, int sc, boolean clearBuffer) throws IOException {
        }

        @Override public void setDateHeader(String name, long date) {}
        @Override public void addDateHeader(String name, long date) {}
        @Override public void setHeader(String name, String value) {}
        @Override public void addHeader(String name, String value) {}
        @Override public void setIntHeader(String name, int value) {}
        @Override public void addIntHeader(String name, int value) {}
        @Override public void setStatus(int sc) {}
        @Override public int getStatus() { return 200; }
        @Override public String getHeader(String name) { return null; }
        @Override public java.util.Collection<String> getHeaders(String name) { return null; }
        @Override public java.util.Collection<String> getHeaderNames() { return null; }
    }

    // Getters
    public static String getAppName() { return APP_NAME; }
    public static String getAppUrl() { return APP_URL; }
    public static int getTokenExpiryHours() { return TOKEN_EXPIRY_HOURS; }
    public static int getPasswordResetExpiryHours() { return PASSWORD_RESET_EXPIRY_HOURS; }
}