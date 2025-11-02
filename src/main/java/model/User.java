package model;

import java.io.Serializable;
import java.util.Date;

/**
 * User model - Base user entity from AppUsers table
 * Represents all users in the system (Admin, Staff, Customer)
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Core fields from AppUsers table
    private int userId;
    private String email;
    private String passwordHash;
    private String role;
    private boolean isActive;
    private boolean emailVerified;
    private Date createdAt;
    private Date lastLogin;

    // Additional fields from vw_AllUsers (from Customers/Staff tables)
    private String name;
    private String phone;
    private String address;
    private String oauthProvider;

    /**
     * Default constructor
     */
    public User() {}

    /**
     * Constructor with basic fields
     */
    public User(int userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    /**
     * Constructor with name field
     */
    public User(int userId, String email, String name, String role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }


    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    // ... (giữ nguyên các getter/setter cũ) ...

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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


    /**
     * Checks if user uses OAuth authentication
     */
    public boolean isOAuthUser() {
        return oauthProvider != null && !oauthProvider.trim().isEmpty();
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

    // ... (giữ nguyên các method cũ: getDisplayName, getInitials, isAdmin, etc.) ...

    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        return email != null ? email : "Unknown";
    }

    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return email != null && !email.isEmpty() ?
                    email.substring(0, 1).toUpperCase() : "?";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isStaff() {
        return "STAFF".equalsIgnoreCase(role);
    }

    public boolean isCustomer() {
        return "CUSTOMER".equalsIgnoreCase(role);
    }

    public String getRoleBadge() {
        if (isAdmin()) return "badge-danger";
        if (isStaff()) return "badge-warning";
        if (isCustomer()) return "badge-info";
        return "badge-secondary";
    }

    public String getRoleDisplay() {
        if (isAdmin()) return "Quản trị viên";
        if (isStaff()) return "Nhân viên";
        if (isCustomer()) return "Khách hàng";
        return role;
    }

    public String getRoleIcon() {
        if (isAdmin()) return "bi-shield-fill";
        if (isStaff()) return "bi-person-badge";
        if (isCustomer()) return "bi-person";
        return "bi-question-circle";
    }

    public String getStatusBadge() {
        return isActive ? "badge-success" : "badge-secondary";
    }

    public String getStatusDisplay() {
        return isActive ? "Hoạt động" : "Vô hiệu hóa";
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", emailVerified=" + emailVerified +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}