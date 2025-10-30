package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Admin model - minimal, just for type safety
 * Admin doesn't have separate table, just AppUsers with role='ADMIN'
 */
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    // From User (AppUsers table)
    private int adminId; // FK to AppUsers.user_id
    private String email;
    private boolean isActive;
    private Date createdAt;
    private Date lastLogin;

    // Optional fields (can be null for Admin)
    private String name;
    private String phone;

    // Constructors
    public Admin() {}

    public Admin(int adminId, String email) {
        this.adminId = adminId;
        this.email = email;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getUserId() {
        return adminId;
    }

    public void setUserId(int adminId) {
        this.adminId = adminId;
    }

    public int getId() {
        return adminId;
    }

    public void setId(int adminId) {
        this.adminId = adminId;
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
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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

    // Convenience methods
    public String getDisplayName() {
        return name != null ? name : email;
    }

    public String getInitials() {
        if (name == null || name.trim().isEmpty()) {
            return email != null && !email.isEmpty() ? email.substring(0, 1).toUpperCase() : "A";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    public String getRole() {
        return "ADMIN";
    }

    public boolean isAdmin() {
        return true;
    }

    public boolean isStaff() {
        return false;
    }

    public boolean isCustomer() {
        return false;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return adminId == admin.adminId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(adminId);
    }
}