package controller.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UsersServlet - Manage all users (Admin, Staff, Customer)
 * Uses vw_AllUsers database view for unified user listing
 */
@WebServlet("/admin/users")
public class UsersServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UsersServlet.class);
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        logger.info("UsersServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameters
            String roleFilter = request.getParameter("role");
            String searchKeyword = request.getParameter("search");
            String statusFilter = request.getParameter("status");

            List<User> userList;

            // Apply filters
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                // Search users by keyword
                userList = userDAO.searchUsers(searchKeyword);
                request.setAttribute("searchKeyword", searchKeyword);
                logger.debug("Searching users with keyword: {}", searchKeyword);

            } else if (roleFilter != null && !roleFilter.isEmpty() && !"ALL".equals(roleFilter)) {
                // Filter by role
                userList = userDAO.getUsersByRole(roleFilter);
                request.setAttribute("roleFilter", roleFilter);
                logger.debug("Filtering users by role: {}", roleFilter);

            } else {
                // Get all users
                userList = userDAO.getAllUsers();
                logger.debug("Loading all users");
            }

            // Filter by status (active/inactive)
            if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                boolean isActive = "ACTIVE".equals(statusFilter);
                userList = userList.stream()
                        .filter(u -> u.isActive() == isActive)
                        .collect(Collectors.toList());
                request.setAttribute("statusFilter", statusFilter);
                logger.debug("Filtering users by status: {}", statusFilter);
            }

            // Get statistics
            UserDAO.UserStats stats = userDAO.getUserStats();

            // Set attributes for JSP
            request.setAttribute("userList", userList);
            request.setAttribute("userStats", stats);
            request.setAttribute("totalUsers", userList.size());
            request.setAttribute("pageTitle", "Quản lý người dùng");

            // Check for success messages
            if (request.getParameter("statusUpdated") != null) {
                request.setAttribute("successMessage", "Cập nhật trạng thái thành công!");
            }
            if (request.getParameter("deleted") != null) {
                request.setAttribute("successMessage", "Vô hiệu hóa người dùng thành công!");
            }

            logger.info("Loaded {} users (role: {}, search: {}, status: {})",
                    userList.size(), roleFilter, searchKeyword, statusFilter);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.error("Error loading users list", e);
            request.setAttribute("errorMessage", "Không thể tải danh sách người dùng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {
                case "toggleStatus":
                    handleToggleStatus(request, response);
                    break;

                case "activate":
                    handleActivate(request, response);
                    break;

                case "deactivate":
                    handleDeactivate(request, response);
                    break;

                default:
                    logger.warn("Invalid action: {}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (Exception e) {
            logger.error("Error handling POST action: {}", action, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error: " + e.getMessage());
        }
    }

    /**
     * Toggle user active status
     */
    private void handleToggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        boolean newStatus = Boolean.parseBoolean(request.getParameter("status"));

        boolean success = userDAO.toggleUserStatus(userId, newStatus);

        if (success) {
            logger.info("User {} status toggled to: {}", userId, newStatus);
            response.sendRedirect(request.getContextPath() + "/admin/users?statusUpdated=true");
        } else {
            logger.error("Failed to toggle status for user: {}", userId);
            response.sendRedirect(request.getContextPath() + "/admin/users?error=true");
        }
    }

    /**
     * Activate user
     */
    private void handleActivate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));

        boolean success = userDAO.activateUser(userId);

        if (success) {
            logger.info("User {} activated", userId);
            response.sendRedirect(request.getContextPath() + "/admin/users?statusUpdated=true");
        } else {
            logger.error("Failed to activate user: {}", userId);
            response.sendRedirect(request.getContextPath() + "/admin/users?error=true");
        }
    }

    /**
     * Deactivate user (soft delete)
     */
    private void handleDeactivate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));

        boolean success = userDAO.deactivateUser(userId);

        if (success) {
            logger.info("User {} deactivated", userId);
            response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
        } else {
            logger.error("Failed to deactivate user: {}", userId);
            response.sendRedirect(request.getContextPath() + "/admin/users?error=true");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("UsersServlet destroyed");
    }
}