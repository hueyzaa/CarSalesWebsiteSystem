package controller.admin;

import dao.AdminDAO;
import model.Admin;
import model.Staff;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/update-staff")
public class UpdateStaffServlet extends HttpServlet {
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Admin admin = (Admin) session.getAttribute("adminAccount");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
            return;
        }

        try {
            int staffId = Integer.parseInt(idParam);
            Staff staff = adminDAO.getStaffById(staffId);

            if (staff == null) {
                request.setAttribute("error", "Không tìm thấy nhân viên!");
                request.getRequestDispatcher("/WEB-INF/views/Admin/staff-list.jsp").forward(request, response);
                return;
            }

            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Admin admin = (Admin) session.getAttribute("adminAccount");

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int staffId = Integer.parseInt(request.getParameter("staffId"));
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String status = request.getParameter("status");


            boolean updated = adminDAO.updateStaff(admin.getAdminId(), staffId, name, phone, address);


            if ("INACTIVE".equalsIgnoreCase(status)) {
                adminDAO.toggleStaffStatus(admin.getAdminId(), staffId, false);
            } else if ("ACTIVE".equalsIgnoreCase(status)) {
                adminDAO.toggleStaffStatus(admin.getAdminId(), staffId, true);
            }

            if (updated) {
                request.setAttribute("success", "Cập nhật thông tin nhân viên thành công!");
            } else {
                request.setAttribute("error", "Không thể cập nhật thông tin nhân viên. Vui lòng thử lại!");
            }


            request.setAttribute("staff", adminDAO.getStaffById(staffId));
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID nhân viên không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/staff-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật nhân viên!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);
        }
    }
}
