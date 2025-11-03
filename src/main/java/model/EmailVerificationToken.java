package model;

import java.io.Serializable;
import java.util.Date;

/**
 * EmailVerificationToken model
 * Represents tokens for email verification and password reset
 */
public class EmailVerificationToken implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tokenId;
    private int userId;
    private String token;
    private String tokenType;
    private Date expiryDate;
    private boolean isUsed;
    private Date createdAt;
    private Date usedAt;
    private String ipAddress;
    private String userAgent;

    /**
     * Default constructor
     */
    public EmailVerificationToken() {}

    /**
     * Constructor for creating new token
     */
    public EmailVerificationToken(int userId, String token, String tokenType, Date expiryDate) {
        this.userId = userId;
        this.token = token;
        this.tokenType = tokenType;
        this.expiryDate = expiryDate;
        this.isUsed = false;
        this.createdAt = new Date();
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpiryDate() {
        return expiryDate != null ? new Date(expiryDate.getTime()) : null;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate != null ? new Date(expiryDate.getTime()) : null;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Date getCreatedAt() {
        return createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    public Date getUsedAt() {
        return usedAt != null ? new Date(usedAt.getTime()) : null;
    }

    public void setUsedAt(Date usedAt) {
        this.usedAt = usedAt != null ? new Date(usedAt.getTime()) : null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    /**
     * Checks if token is expired
     */
    public boolean isExpired() {
        return expiryDate != null && new Date().after(expiryDate);
    }

    /**
     * Checks if token is valid (not used and not expired)
     */
    public boolean isValid() {
        return !isUsed && !isExpired();
    }

    /**
     * Gets token status
     */
    public String getStatus() {
        if (isUsed) return "USED";
        if (isExpired()) return "EXPIRED";
        return "ACTIVE";
    }

    /**
     * Gets token status display in Vietnamese
     */
    public String getStatusDisplay() {
        switch (getStatus()) {
            case "USED":
                return "Đã sử dụng";
            case "EXPIRED":
                return "Đã hết hạn";
            case "ACTIVE":
                return "Đang hoạt động";
            default:
                return "Không xác định";
        }
    }

    /**
     * Gets Bootstrap badge class for status
     */
    public String getStatusBadge() {
        switch (getStatus()) {
            case "USED":
                return "badge-secondary";
            case "EXPIRED":
                return "badge-danger";
            case "ACTIVE":
                return "badge-success";
            default:
                return "badge-dark";
        }
    }

    /**
     * Gets token type display in Vietnamese
     */
    public String getTokenTypeDisplay() {
        if ("EMAIL_VERIFICATION".equals(tokenType)) {
            return "Xác thực email";
        } else if ("PASSWORD_RESET".equals(tokenType)) {
            return "Đặt lại mật khẩu";
        }
        return tokenType;
    }

    /**
     * Gets Bootstrap icon for token type
     */
    public String getTokenTypeIcon() {
        if ("EMAIL_VERIFICATION".equals(tokenType)) {
            return "bi-envelope-check";
        } else if ("PASSWORD_RESET".equals(tokenType)) {
            return "bi-key";
        }
        return "bi-shield-lock";
    }

    /**
     * Gets hours until expiry
     */
    public long getHoursUntilExpiry() {
        if (expiryDate == null) return 0;
        long diff = expiryDate.getTime() - new Date().getTime();
        return diff / (1000 * 60 * 60);
    }

    /**
     * Gets minutes until expiry
     */
    public long getMinutesUntilExpiry() {
        if (expiryDate == null) return 0;
        long diff = expiryDate.getTime() - new Date().getTime();
        return diff / (1000 * 60);
    }

    /**
     * Check if token will expire soon (within 1 hour)
     */
    public boolean isExpiringSoon() {
        return !isExpired() && getHoursUntilExpiry() < 1;
    }

    @Override
    public String toString() {
        return "EmailVerificationToken{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", tokenType='" + tokenType + '\'' +
                ", status='" + getStatus() + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailVerificationToken that = (EmailVerificationToken) o;
        return tokenId == that.tokenId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(tokenId);
    }
}