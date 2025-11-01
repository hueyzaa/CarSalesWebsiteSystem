package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Admin model - Represents admin users in the system
 * Admin doesn't have separate table, just AppUsers with role='ADMIN'
 * This class provides type safety and admin-specific convenience methods
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


    /**
     * Default constructor
     */
    public Admin() {}

    /**
     * Constructor with basic fields
     *
     * @param adminId Admin ID (User ID)
     * @param email Admin email
     */
    public Admin(int adminId, String email) {
        this.adminId = adminId;
        this.email = email;
    }


    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * Alias for getAdminId() for compatibility
     *
     * @return Admin ID
     */
    public int getUserId() {
        return adminId;
    }

    /**
     * Alias for setAdminId() for compatibility
     *
     * @param adminId Admin ID
     */
    public void setUserId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * Alias for getAdminId() for compatibility
     *
     * @return Admin ID
     */
    public int getId() {
        return adminId;
    }

    /**
     * Alias for setAdminId() for compatibility
     *
     * @param adminId Admin ID
     */
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

    /**
     * Alias for getName() for compatibility
     *
     * @return Admin name
     */
    public String getFullname() {
        return name;
    }

    /**
     * Alias for setName() for compatibility
     *
     * @param name Admin name
     */
    public void setFullname(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * Gets display name (name if available, otherwise email)
     *
     * @return Display name
     */
    public String getDisplayName() {
        return name != null ? name : email;
    }

    /**
     * Gets admin initials for avatar display
     *
     * @return Admin initials (1-2 characters)
     */
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


    /**
     * Gets user role (always "ADMIN")
     *
     * @return "ADMIN"
     */
    public String getRole() {
        return "ADMIN";
    }

    /**
     * Checks if user is an admin (always true for this class)
     *
     * @return true
     */
    public boolean isAdmin() {
        return true;
    }

    /**
     * Checks if user is a staff member (always false for admin)
     *
     * @return false
     */
    public boolean isStaff() {
        return false;
    }

    /**
     * Checks if user is a customer (always false for admin)
     *
     * @return false
     */
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