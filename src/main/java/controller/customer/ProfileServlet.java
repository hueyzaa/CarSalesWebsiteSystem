package controller.customer;

import dao.CustomerDAO;
import model.Customer;
import model.User;
import util.SessionUtils;
import util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ProfileServlet - Handle customer profile view and update
 * ONLY FOR CUSTOMERS - Staff/Admin have separate profile management
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProfileServlet.class);
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        logger.info("ProfileServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check authentication
        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated access to profile");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if customer (staff/admin have separate profile pages)
        if (!SessionUtils.isCustomer(session)) {
            logger.warn("Non-customer user attempted to access customer profile");
            session.setAttribute("error", "Trang này chỉ dành cho khách hàng!");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            Integer customerId = SessionUtils.getUserId(session);
            logger.info("Loading profile for customer ID: {}", customerId);

            // Load customer data
            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer == null) {
                logger.warn("Customer not found: ID {}", customerId);
                session.setAttribute("error", "Không tìm thấy thông tin người dùng!");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // Set attributes
            request.setAttribute("customer", customer);
            User user = SessionUtils.getUser(session);
            request.setAttribute("user", user);

            request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.error("Error loading profile", e);
            session.setAttribute("error", "Không thể tải thông tin người dùng!");
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        // Check authentication
        if (!SessionUtils.isCustomer(session)) {
            logger.warn("Unauthenticated or non-customer POST to profile");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            Integer customerId = SessionUtils.getUserId(session);

            // Validate inputs
            String name = ValidationUtil.validateString(request.getParameter("name"), "Tên", 100);

            String phone = validateOptional(request.getParameter("phone"), ValidationUtil::validatePhone);
            String address = validateOptional(request.getParameter("address"),
                    s -> ValidationUtil.validateString(s, "Địa chỉ", 255));

            logger.info("Updating profile for customer ID: {}", customerId);

            // Update customer profile
            boolean success = customerDAO.updateCustomer(customerId, name, phone, address);

            if (success) {
                Customer updatedCustomer = customerDAO.getCustomerById(customerId);

                if (updatedCustomer != null) {
                    User currentUser = SessionUtils.getUser(session);

                    // Update user info in session
                    currentUser.setName(updatedCustomer.getName());
                    currentUser.setPhone(updatedCustomer.getPhone());
                    currentUser.setAddress(updatedCustomer.getAddress());


                    SessionUtils.updateUser(session, currentUser);
                    logger.info("Customer profile updated successfully: {}", customerId);
                }

                session.setAttribute("success", "Cập nhật thông tin thành công!");
            } else {
                session.setAttribute("error", "Cập nhật thông tin thất bại!");
            }

        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            session.setAttribute("error", "Đã xảy ra lỗi khi cập nhật thông tin!");
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }

    /**
     * Validate optional field
     */
    @FunctionalInterface
    private interface Validator {
        String validate(String input);
    }

    private String validateOptional(String value, Validator validator) {
        return (value == null || value.trim().isEmpty()) ? null : validator.validate(value);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("ProfileServlet destroyed");
    }
}