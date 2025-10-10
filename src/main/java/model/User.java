package model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private String oauthProvider;
    private Date createdAt;

    // Additional fields for JSP display
    private String phone;
    private String address;

    // Constructors
    public User() {}

    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public User(String name, String email, String passwordHash, String role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Primary Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Alias methods for consistency with other models
    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Alias for fullname used in JSPs
    public String getFullname() {
        return name;
    }

    public void setFullname(String name) {
        this.name = name;
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

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    // Convenience methods
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isStaff() {
        return "STAFF".equalsIgnoreCase(role);
    }

    public boolean isCustomer() {
        return "CUSTOMER".equalsIgnoreCase(role);
    }

    public boolean isGuest() {
        return "GUEST".equalsIgnoreCase(role);
    }

    public boolean hasRole(String checkRole) {
        return role != null && role.equalsIgnoreCase(checkRole);
    }

    public boolean isOAuthUser() {
        return oauthProvider != null && !oauthProvider.trim().isEmpty();
    }

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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", oauthProvider='" + oauthProvider + '\'' +
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