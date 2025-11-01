package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer model - Represents customer users in the system
 * Simplified version without loyalty_points
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


    /**
     * Default constructor
     */
    public Customer() {}

    /**
     * Constructor with basic fields
     *
     * @param customerId Customer ID (User ID)
     * @param email Customer email
     * @param name Customer name
     */
    public Customer(int customerId, String email, String name) {
        this.customerId = customerId;
        this.email = email;
        this.name = name;
    }


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Alias for getCustomerId() for compatibility
     *
     * @return Customer ID
     */
    public int getUserId() {
        return customerId;
    }

    /**
     * Alias for setCustomerId() for compatibility
     *
     * @param customerId Customer ID
     */
    public void setUserId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Alias for getCustomerId() for compatibility
     *
     * @return Customer ID
     */
    public int getId() {
        return customerId;
    }

    /**
     * Alias for setCustomerId() for compatibility
     *
     * @param customerId Customer ID
     */
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
     * @return Customer name
     */
    public String getFullname() {
        return name;
    }

    /**
     * Alias for setName() for compatibility
     *
     * @param name Customer name
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
     * Gets display name (name if available, otherwise email)
     *
     * @return Display name
     */
    public String getDisplayName() {
        return name != null ? name : email;
    }

    /**
     * Gets customer initials for avatar display
     *
     * @return Customer initials (1-2 characters)
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
     * Gets user role (always "CUSTOMER")
     *
     * @return "CUSTOMER"
     */
    public String getRole() {
        return "CUSTOMER";
    }

    /**
     * Checks if user is an admin (always false for customer)
     *
     * @return false
     */
    public boolean isAdmin() {
        return false;
    }

    /**
     * Checks if user is a staff member (always false for customer)
     *
     * @return false
     */
    public boolean isStaff() {
        return false;
    }

    /**
     * Checks if user is a customer (always true for this class)
     *
     * @return true
     */
    public boolean isCustomer() {
        return true;
    }

    /**
     * Checks if customer uses OAuth authentication
     *
     * @return true if customer logged in via OAuth (Google, Facebook, etc.)
     */
    public boolean isOAuthUser() {
        return oauthProvider != null && !oauthProvider.trim().isEmpty();
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

    /**
     * Gets formatted total spent amount
     *
     * @return Formatted currency string
     */
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