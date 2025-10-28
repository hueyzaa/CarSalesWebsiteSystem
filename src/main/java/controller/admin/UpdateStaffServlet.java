package controller.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/update-staff")
public class UpdateStaffServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            User staff = userDAO.getUserById(userId);

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

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");


            User staff = userDAO.getUserById(userId);
            if (staff == null) {
                request.setAttribute("error", "Không tìm thấy nhân viên để cập nhật!");
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);
                return;
            }


            staff.setName(name);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setAddress(address);

            boolean success = userDAO.updateUser(staff);

            if (success) {
                request.setAttribute("success", "Cập nhật thông tin nhân viên thành công!");
            } else {
                request.setAttribute("error", "Cập nhật thất bại, vui lòng thử lại!");
            }


            request.setAttribute("staff", userDAO.getUserById(userId));
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-staff.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID nhân viên không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/staff-list.jsp").forward(request, response);
        }
    }
}
