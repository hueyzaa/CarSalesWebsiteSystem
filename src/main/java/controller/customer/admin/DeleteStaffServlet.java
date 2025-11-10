package controller.servlet.admin;

import dao.UserDAO;
import exception.DatabaseException;
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
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                logger.warn("Thiếu ID nhân viên trong yêu cầu xóa");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=missingId");
                return;
            }
            int userId = Integer.parseInt(idParam);
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                logger.info("Đã xóa nhân viên có ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&success=deleted");
            } else {
                logger.warn("Không tìm thấy hoặc không thể xóa nhân viên ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=notFound");
            }

        } catch (NumberFormatException e) {
            logger.error("ID không hợp lệ khi xóa nhân viên", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=invalidId");

        } catch (DatabaseException e) {
            logger.error("Lỗi cơ sở dữ liệu khi xóa nhân viên", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=dbError");

        } catch (Exception e) {
            logger.error("Lỗi không mong muốn khi xóa nhân viên", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=unexpected");
        }
    }
}

