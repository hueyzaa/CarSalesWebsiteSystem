package controller.admin;

import dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Staff;
import model.User;
import util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/Admin/update-staff")
public class UpdateStaffServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStaffServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = SessionUtils.getUser(session);
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
            return;
        }

        int staffId = Integer.parseInt(idParam);
        Staff staff = adminDAO.getStaffById(staffId);
        if (staff == null) {
            session.setAttribute("errorMessage", "Nhân viên không tồn tại");
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
            return;
        }

        request.setAttribute("staff", staff);
        request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        User currentUser = SessionUtils.getUser(session);
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int adminId = currentUser.getUserId();
        int staffId = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        boolean success = adminDAO.updateStaff(adminId, staffId, name, phone, address);

        if (success) {
            session.setAttribute("successMessage", "Cập nhật nhân viên thành công");
        } else {
            session.setAttribute("errorMessage", "Cập nhật nhân viên thất bại");
        }

        response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
    }
}
