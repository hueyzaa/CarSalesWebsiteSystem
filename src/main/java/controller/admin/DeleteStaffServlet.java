package controller.admin;

import dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionUtils;

import java.io.IOException;

@WebServlet("/Admin/delete-staff")
public class DeleteStaffServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DeleteStaffServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = SessionUtils.getUser(session);
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            session.setAttribute("errorMessage", "Không xác định nhân viên cần xóa");
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
            return;
        }

        try {
            int staffId = Integer.parseInt(idParam);
            boolean success = adminDAO.deleteStaff(staffId);

            if (success) {
                session.setAttribute("successMessage", "Đã xóa nhân viên thành công");
                logger.info("Admin {} đã xóa staff {}", currentUser.getUserId(), staffId);
            } else {
                session.setAttribute("errorMessage", "Xóa nhân viên thất bại");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID nhân viên không hợp lệ");
        }

        response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
    }
}
