package controller.admin;

import dao.AdminDAO;
import dao.UserDAO;
import model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Admin/add-staff")
public class AddStaffServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddStaffServlet.class);
    private final AdminDAO adminDAO = new AdminDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
    }
     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        try {
            Admin admin = (Admin) request.getSession().getAttribute("adminAccount");
            if (admin == null) {
                logger.warn("Unauthorized attempt to add staff — no admin session found");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            int adminId = admin.getAdminId();
            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                saveFormData(request, name, email, phone, address);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
                return;
            }
            boolean success = adminDAO.createStaff(adminId, email, password, name, phone, address);
            if (success) {
                logger.info("Admin {} created new staff: {}", adminId, email);
                request.getSession().setAttribute("message", "Thêm nhân viên thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff");
            } else {
                logger.warn("Admin {} failed to create staff {}", adminId, email);
                request.setAttribute("error", "Không thể thêm nhân viên. Vui lòng thử lại!");
                saveFormData(request, name, email, phone, address);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Lỗi khi thêm nhân viên mới", e);
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống khi thêm nhân viên!");
            saveFormData(request, name, email, phone, address);
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
        }
    }

    private void saveFormData(HttpServletRequest request, String name, String email,
                              String phone, String address) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
    }
}
