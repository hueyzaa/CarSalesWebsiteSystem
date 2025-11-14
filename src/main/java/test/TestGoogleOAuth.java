package test;

import util.GoogleOAuthConfig;
import service.GoogleOAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGoogleOAuth {
    private static final Logger logger = LoggerFactory.getLogger(TestGoogleOAuth.class);

    public static void main(String[] args) {
        logger.info("Testing Google OAuth Configuration");

        // Test 1: Check config loaded
        String clientId = GoogleOAuthConfig.getClientId();
        String clientSecret = GoogleOAuthConfig.getClientSecret();

        logger.info("Client ID: {}", clientId != null ? "Loaded" : "Missing");
        logger.info("Client Secret: {}", clientSecret != null ? "Loaded" : "Missing");

        // Test 2: Generate auth URL
        GoogleOAuthService service = new GoogleOAuthService();
        String authUrl = service.getAuthorizationUrl("test-state-123");

        if (authUrl != null) {
            logger.info("Auth URL generated successfully");
            logger.info("URL: {}", authUrl);
        } else {
            logger.error("Failed to generate auth URL");
        }
    }
}