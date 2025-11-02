package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer model - Represents customer users in the system
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    // From User (AppUsers table)
    private int customerId;
    private String email;
    private boolean isActive;
    private boolean emailVerified;
    private Date createdAt;
    private Date lastLogin;

    // Customer-specific fields
    private String name;
    private String phone;
    private String address;
    private String oauthProvider;
    private String oauthId;

    // Additional fields from view
    private int totalOrders;
    private double totalSpent;

    /**
     * Default constructor
     */
    public Customer() {}

    /**
     * Constructor with basic fields
     */
    public Customer(int customerId, String email, String name) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
    }


    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    // ... (giữ nguyên các getter/setter cũ) ...

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return customerId;
    }

    public void setUserId(int customerId) {
        this.customerId = customerId;
    }

    public int getId() {
        return customerId;
    }

    public void setId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    public Date getLastLogin() {
        return lastLogin != null ? new Date(lastLogin.getTime()) : null;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin != null ? new Date(lastLogin.getTime()) : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return name;
    }

    public void setFullname(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }


    /**
     * Gets display text for email verification status
     */
    public String getEmailVerifiedDisplay() {
        return emailVerified ? "Đã xác thực" : "Chưa xác thực";
    }

    /**
     * Gets Bootstrap badge class for email verification status
     */
    public String getEmailVerifiedBadge() {
        return emailVerified ? "badge-success" : "badge-warning";
    }

    /**
     * Gets Bootstrap icon for email verification status
     */
    public String getEmailVerifiedIcon() {
        return emailVerified ? "bi-check-circle-fill" : "bi-exclamation-circle-fill";
    }

    /**
     * Gets OAuth provider display name
     */
    public String getOauthProviderDisplay() {
        if (oauthProvider == null || oauthProvider.trim().isEmpty()) {
            return "Email/Password";
        }
        switch (oauthProvider.toUpperCase()) {
            case "GOOGLE":
                return "Google";
            case "FACEBOOK":
                return "Facebook";
            default:
                return oauthProvider;
        }
    }

    /**
     * Gets OAuth provider icon
     */
    public String getOauthProviderIcon() {
        if (oauthProvider == null || oauthProvider.trim().isEmpty()) {
            return "bi-envelope";
        }
        switch (oauthProvider.toUpperCase()) {
            case "GOOGLE":
                return "bi-google";
            case "FACEBOOK":
                return "bi-facebook";
            default:
                return "bi-shield-lock";
        }
    }

    // ... (giữ nguyên các method cũ) ...

    public String getDisplayName() {
        return name != null ? name : email;
    }

    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return email != null && !email.isEmpty() ? email.substring(0, 1).toUpperCase() : "?";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public String getRole() {
        return "CUSTOMER";
    }

    public boolean isAdmin() {
        return false;
    }

    public boolean isStaff() {
        return false;
    }

    public boolean isCustomer() {
        return true;
    }

    public boolean isOAuthUser() {
        return oauthProvider != null && !oauthProvider.trim().isEmpty();
    }

    public String getStatusBadge() {
        return isActive ? "badge-success" : "badge-secondary";
    }

    public String getStatusDisplay() {
        return isActive ? "Hoạt động" : "Vô hiệu hóa";
    }

    public String getFormattedTotalSpent() {
        return String.format("%,.0f ₫", totalSpent);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", oauthProvider='" + oauthProvider + '\'' +
                ", oauthId='" + oauthId + '\'' +
                ", emailVerified=" + emailVerified +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(customerId);
    }
}