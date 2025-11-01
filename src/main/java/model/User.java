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
    private Date createdAt;
    private Date lastLogin;

    // Additional fields from vw_AllUsers (from Customers/Staff tables)
    private String name;
    private String phone;
    private String address;


    /**
     * Default constructor
     */
    public User() {}

    /**
     * Constructor with basic fields
     *
     * @param userId User ID
     * @param email User email
     * @param role User role (ADMIN, STAFF, CUSTOMER)
     */
    public User(int userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    /**
     * Constructor with name field
     *
     * @param userId User ID
     * @param email User email
     * @param name User name
     * @param role User role (ADMIN, STAFF, CUSTOMER)
     */
    public User(int userId, String email, String name, String role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }


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

    /**
     * Gets created date (defensive copy)
     *
     * @return Copy of creation date
     */
    public Date getCreatedAt() {
        return createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    /**
     * Sets created date (defensive copy)
     *
     * @param createdAt Creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? new Date(createdAt.getTime()) : null;
    }

    /**
     * Gets last login date (defensive copy)
     *
     * @return Copy of last login date
     */
    public Date getLastLogin() {
        return lastLogin != null ? new Date(lastLogin.getTime()) : null;
    }

    /**
     * Sets last login date (defensive copy)
     *
     * @param lastLogin Last login date
     */
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
     * Gets display name (name if available, otherwise email)
     *
     * @return Display name
     */
    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        return email != null ? email : "Unknown";
    }

    /**
     * Gets user initials for avatar display
     *
     * @return User initials (1-2 characters)
     */
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


    /**
     * Checks if user is an admin
     *
     * @return true if user role is ADMIN
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Checks if user is a staff member
     *
     * @return true if user role is STAFF
     */
    public boolean isStaff() {
        return "STAFF".equalsIgnoreCase(role);
    }

    /**
     * Checks if user is a customer
     *
     * @return true if user role is CUSTOMER
     */
    public boolean isCustomer() {
        return "CUSTOMER".equalsIgnoreCase(role);
    }


    /**
     * Gets Bootstrap badge class for role
     *
     * @return CSS class for role badge
     */
    public String getRoleBadge() {
        if (isAdmin()) return "badge-danger";
        if (isStaff()) return "badge-warning";
        if (isCustomer()) return "badge-info";
        return "badge-secondary";
    }

    /**
     * Gets Vietnamese display text for role
     *
     * @return Role display text in Vietnamese
     */
    public String getRoleDisplay() {
        if (isAdmin()) return "Quản trị viên";
        if (isStaff()) return "Nhân viên";
        if (isCustomer()) return "Khách hàng";
        return role;
    }

    /**
     * Gets Bootstrap icon class for role
     *
     * @return Icon class for role
     */
    public String getRoleIcon() {
        if (isAdmin()) return "bi-shield-fill";
        if (isStaff()) return "bi-person-badge";
        if (isCustomer()) return "bi-person";
        return "bi-question-circle";
    }

    /**
     * Gets Bootstrap badge class for active status
     *
     * @return CSS class for status badge
     */
    public String getStatusBadge() {
        return isActive ? "badge-success" : "badge-secondary";
    }

    /**
     * Gets Vietnamese display text for active status
     *
     * @return Status display text in Vietnamese
     */
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