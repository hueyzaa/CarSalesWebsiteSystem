package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Staff model - Simplified version
 * Removed: position, department, hiredDate, salary, createdBy, notes
 */
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;

    // From User (AppUsers table)
    private int staffId;
    private String email;
    private boolean isActive;
    private Date createdAt;
    private Date lastLogin;

    // Staff-specific fields (simplified)
    private String name;
    private String phone;
    private String address;

    // Additional fields from view
    private int totalOrders;
    private int totalBlogs;

    // Constructors
    public Staff() {}

    public Staff(int staffId, String email, String name) {
        this.staffId = staffId;
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getUserId() {
        return staffId;
    }

    public void setUserId(int staffId) {
        this.staffId = staffId;
    }

    public int getId() {
        return staffId;
    }

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

    // Convenience methods
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
        return "STAFF";
    }

    public boolean isAdmin() {
        return false;
    }

    public boolean isStaff() {
        return true;
    }

    public boolean isCustomer() {
        return false;
    }

    public String getStatusBadge() {
        return isActive ? "badge-success" : "badge-secondary";
    }

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