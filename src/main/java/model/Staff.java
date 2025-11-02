package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Staff model - Represents staff users in the system
 * Simplified version without position, department, hiredDate, salary, createdBy, notes
 */
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;

    // From User (AppUsers table)
    private int staffId;
    private String email;
    private boolean isActive;
    private boolean emailVerified;
    private Date createdAt;
    private Date lastLogin;

    // Staff-specific fields (simplified)
    private String name;
    private String phone;
    private String address;

    // Additional fields from view
    private int totalOrders;
    private int totalBlogs;


    /**
     * Default constructor
     */
    public Staff() {}

    /**
     * Constructor with basic fields
     *
     * @param staffId Staff ID (User ID)
     * @param email Staff email
     * @param name Staff name
     */
    public Staff(int staffId, String email, String name) {
        this.staffId = staffId;
        this.email = email;
        this.name = name;
    }


    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    /**
     * Alias for getStaffId() for compatibility
     *
     * @return Staff ID
     */
    public int getUserId() {
        return staffId;
    }

    /**
     * Alias for setStaffId() for compatibility
     *
     * @param staffId Staff ID
     */
    public void setUserId(int staffId) {
        this.staffId = staffId;
    }

    /**
     * Alias for getStaffId() for compatibility
     *
     * @return Staff ID
     */
    public int getId() {
        return staffId;
    }

    /**
     * Alias for setStaffId() for compatibility
     *
     * @param staffId Staff ID
     */
    public void setId(int staffId) {
        this.staffId = staffId;
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
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
     * @return Staff name
     */
    public String getFullname() {
        return name;
    }

    /**
     * Alias for setName() for compatibility
     *
     * @param name Staff name
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalBlogs() {
        return totalBlogs;
    }

    public void setTotalBlogs(int totalBlogs) {
        this.totalBlogs = totalBlogs;
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
     * Gets staff initials for avatar display
     *
     * @return Staff initials (1-2 characters)
     */
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


    /**
     * Gets user role (always "STAFF")
     *
     * @return "STAFF"
     */
    public String getRole() {
        return "STAFF";
    }

    /**
     * Checks if user is an admin (always false for staff)
     *
     * @return false
     */
    public boolean isAdmin() {
        return false;
    }

    /**
     * Checks if user is a staff member (always true for this class)
     *
     * @return true
     */
    public boolean isStaff() {
        return true;
    }

    /**
     * Checks if user is a customer (always false for staff)
     *
     * @return false
     */
    public boolean isCustomer() {
        return false;
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
        return "Staff{" +
                "staffId=" + staffId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return staffId == staff.staffId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(staffId);
    }
}