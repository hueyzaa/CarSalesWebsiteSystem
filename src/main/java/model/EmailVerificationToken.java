package model;

import java.io.Serializable;
import java.util.Date;

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

    public EmailVerificationToken() {}

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
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Date usedAt) {
        this.usedAt = usedAt;
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
}