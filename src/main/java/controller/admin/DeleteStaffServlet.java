package controller.admin;

import dao.AdminDAO;
import model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/Admin/delete-staff")
public class DeleteStaffServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteStaffServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Admin admin = (Admin) request.getSession().getAttribute("adminAccount");
            if (admin == null) {
                logger.warn("Unauthorized attempt to delete staff — no admin session found");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                logger.warn("Thiếu ID nhân viên trong yêu cầu xóa");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=missingId");
                return;
            }

            int staffId = Integer.parseInt(idParam);
            int adminId = admin.getAdminId();


            boolean success = adminDAO.toggleStaffStatus(adminId, staffId, false);

            if (success) {
                logger.info("Admin {} đã vô hiệu hóa nhân viên ID = {}", adminId, staffId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&success=deleted");
            } else {
                logger.warn("Không thể vô hiệu hóa nhân viên ID = {}", staffId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=notFound");
            }

        } catch (NumberFormatException e) {
            logger.error("ID nhân viên không hợp lệ khi xóa", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=invalidId");

        } catch (Exception e) {
            logger.error("Lỗi không mong muốn khi vô hiệu hóa nhân viên", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=unexpected");
        }
    }
}
