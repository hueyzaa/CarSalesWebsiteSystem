package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import util.GoogleOAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Google OAuth 2.0 Service
 */
public class GoogleOAuthService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuthService.class);
    private static final Gson gson = new Gson();

    /**
     * Generate Google OAuth authorization URL
     */
    public String getAuthorizationUrl(String state) {
        try {
            String authUrl = GoogleOAuthConfig.getAuthUri();
            String clientId = GoogleOAuthConfig.getClientId();
            String redirectUri = GoogleOAuthConfig.getRedirectUri();
            String scopes = GoogleOAuthConfig.getScopes();

            String url = authUrl + "?" +
                    "client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode(scopes, "UTF-8") +
                    "&state=" + URLEncoder.encode(state, "UTF-8") +
                    "&access_type=offline" +
                    "&prompt=consent";

            logger.info("Generated Google OAuth URL");
            return url;

        } catch (Exception e) {
            logger.error("Error generating OAuth URL", e);
            return null;
        }
    }

    /**
     * Exchange authorization code for access token
     */
    public String getAccessToken(String code) {
        try {
            String tokenUri = GoogleOAuthConfig.getTokenUri();
            String clientId = GoogleOAuthConfig.getClientId();
            String clientSecret = GoogleOAuthConfig.getClientSecret();
            String redirectUri = GoogleOAuthConfig.getRedirectUri();

            URL url = new URL(tokenUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String params = "code=" + URLEncoder.encode(code, "UTF-8") +
                    "&client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                    "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                    "&grant_type=authorization_code";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                String accessToken = jsonResponse.get("access_token").getAsString();

                logger.info("Access token obtained");
                return accessToken;
            } else {
                logger.error("Failed to get access token: {}", responseCode);
                return null;
            }

        } catch (Exception e) {
            logger.error("Error getting access token", e);
            return null;
        }
    }

    /**
     * Get user info from Google using access token
     */
    public Map<String, String> getUserInfo(String accessToken) {
        try {
            String userInfoUri = GoogleOAuthConfig.getUserInfoUri();
            URL url = new URL(userInfoUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("email", jsonResponse.get("email").getAsString());
                userInfo.put("name", jsonResponse.get("name").getAsString());
                userInfo.put("googleId", jsonResponse.get("id").getAsString());

                if (jsonResponse.has("picture")) {
                    userInfo.put("picture", jsonResponse.get("picture").getAsString());
                }

                logger.info("User info retrieved: {}", userInfo.get("email"));
                return userInfo;
            } else {
                logger.error("Failed to get user info: {}", responseCode);
                return null;
            }

        } catch (Exception e) {
            logger.error("Error getting user info", e);
            return null;
        }
    }
}