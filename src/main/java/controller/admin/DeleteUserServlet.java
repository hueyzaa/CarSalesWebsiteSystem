package controller.admin;

import dao.AdminDAO;
import dao.UserDAO;
import model.Admin;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/Admin/delete-user")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteUserServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Admin admin = (Admin) request.getSession().getAttribute("adminAccount");
            if (admin == null) {
                logger.warn("Unauthorized attempt to delete user — no admin session found");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                logger.warn("Thiếu ID người dùng trong yêu cầu xóa");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=missingId");
                return;
            }

            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);

            if (user == null) {
                logger.warn("Không tìm thấy người dùng ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=notFound");
                return;
            }

            boolean success = false;
            String role = user.getRole();
            int adminId = admin.getAdminId();


            if ("STAFF".equalsIgnoreCase(role)) {
                success = adminDAO.toggleStaffStatus(adminId, userId, false);
            } else {

                success = userDAO.deactivateUser(userId);
            }

            if (success) {
                logger.info("Admin {} đã vô hiệu hóa người dùng ID = {} (role = {})", adminId, userId, role);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&success=deleted");
            } else {
                logger.warn("Không thể xóa hoặc vô hiệu hóa người dùng ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=failed");
            }

        } catch (NumberFormatException e) {
            logger.error("ID không hợp lệ khi xóa người dùng", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=invalidId");

        } catch (Exception e) {
            logger.error("Lỗi không mong muốn khi xóa người dùng", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=unexpected");
        }
    }
}
