package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer model - Simplified version
 * Removed: loyalty_points
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    // From User (AppUsers table)
    private int customerId;
    private String email;
    private boolean isActive;
    private Date createdAt;
    private Date lastLogin;

    // Customer-specific fields
    private String name;
    private String phone;
    private String address;
    private String oauthProvider;

    // Additional fields from view
    private int totalOrders;
    private double totalSpent;

    // Constructors
    public Customer() {}

    public Customer(int customerId, String email, String name) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
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