package controller.admin;

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

@WebServlet("/Admin/delete-user")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteStaffServlet.class);
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                logger.warn("Thiếu ID người dùng trong yêu cầu xóa");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff&error=missingId");
                return;
            }
            int userId = Integer.parseInt(idParam);
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                logger.info("Đã xóa người dùng có ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&success=deleted");
            } else {
                logger.warn("Không tìm thấy hoặc không thể xóa ngươid dùng ID = {}", userId);
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=notFound");
            }

        } catch (NumberFormatException e) {
            logger.error("ID không hợp lệ khi người dùng", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=invalidId");

        } catch (DatabaseException e) {
            logger.error("Lỗi cơ sở dữ liệu khi xóa người dùng", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=dbError");

        } catch (Exception e) {
            logger.error("Lỗi không mong muốn khi xóa người dùng", e);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=user&error=unexpected");
        }
    }
}

