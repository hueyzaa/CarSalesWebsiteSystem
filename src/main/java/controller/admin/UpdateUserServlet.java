package controller.admin;

import dao.AdminDAO;
import dao.UserDAO;
import model.Admin;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/Admin/update-user")
public class UpdateUserServlet extends HttpServlet {
    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();

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
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Admin/user-list");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            User user = userDAO.getUserById(userId);

            if (user == null) {
                request.setAttribute("error", "Không tìm thấy người dùng!");
                request.getRequestDispatcher("/WEB-INF/views/Admin/user-list.jsp").forward(request, response);
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Admin/user-list");
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
            int userId = Integer.parseInt(request.getParameter("userId"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String role = request.getParameter("role");
            String status = request.getParameter("status");

            boolean updated = false;


            if ("STAFF".equalsIgnoreCase(role)) {
                updated = adminDAO.updateStaff(admin.getAdminId(), userId, name, phone, address);


                if ("INACTIVE".equalsIgnoreCase(status)) {
                    adminDAO.toggleStaffStatus(admin.getAdminId(), userId, false);
                } else if ("ACTIVE".equalsIgnoreCase(status)) {
                    adminDAO.toggleStaffStatus(admin.getAdminId(), userId, true);
                }

            } else if ("CUSTOMER".equalsIgnoreCase(role)) {

                updated = userDAO.updateCustomer(userId, name, phone, address);


                if ("INACTIVE".equalsIgnoreCase(status)) {
                    userDAO.deactivateUser(userId);
                } else if ("ACTIVE".equalsIgnoreCase(status)) {
                    userDAO.activateUser(userId);
                }
            }

            if (updated) {
                response.sendRedirect(request.getContextPath() + "/Admin/user-list?success=1");
            } else {
                request.setAttribute("error", "Không thể cập nhật người dùng. Vui lòng thử lại!");
                request.setAttribute("user", userDAO.getUserById(userId));
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID người dùng không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/user-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);
        }
    }
}
