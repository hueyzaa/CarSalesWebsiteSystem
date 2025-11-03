package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Google OAuth 2.0 Configuration
 */
public class GoogleOAuthConfig {
    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuthConfig.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = GoogleOAuthConfig.class
                .getClassLoader()
                .getResourceAsStream("google-oauth.properties")) {

            if (input == null) {
                throw new IOException("google-oauth.properties not found");
            }

            properties.load(input);
            logger.info("Google OAuth config loaded");

        } catch (IOException e) {
            logger.error("Failed to load Google OAuth config", e);
        }
    }

    public static String getClientId() {
        return properties.getProperty("google.client.id");
    }

    public static String getClientSecret() {
        return properties.getProperty("google.client.secret");
    }

    public static String getRedirectUri() {
        return properties.getProperty("google.redirect.uri");
    }

    public static String getAuthUri() {
        return properties.getProperty("google.auth.uri");
    }

    public static String getTokenUri() {
        return properties.getProperty("google.token.uri");
    }

    public static String getUserInfoUri() {
        return properties.getProperty("google.user.info.uri");
    }

    public static String getScopes() {
        return properties.getProperty("google.scopes");
    }
}