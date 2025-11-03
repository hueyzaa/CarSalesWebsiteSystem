package controller.admin;

import dao.AdminDAO;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/Admin/add-user")
public class AddUserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddUserServlet.class);
    private final UserDAO userDAO = new UserDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
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
        String role = request.getParameter("role"); // "STAFF" hoặc "CUSTOMER"

        try {

            Admin admin = (Admin) request.getSession().getAttribute("adminAccount");
            if (admin == null) {
                logger.warn("Unauthorized access to AddUserServlet - no admin in session");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int adminId = admin.getAdminId();
            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                saveFormData(request, name, email, phone, address, role);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
                return;
            }

            boolean success = false;

            if ("STAFF".equalsIgnoreCase(role)) {
                success = adminDAO.createStaff(adminId, email, password, name, phone, address);
            }
            else if ("CUSTOMER".equalsIgnoreCase(role)) {
                success = userDAO.registerCustomer(name, email, password, phone, address, null);
            }
            else {
                request.setAttribute("error", "Vai trò không hợp lệ!");
                saveFormData(request, name, email, phone, address, role);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
                return;
            }


            if (success) {
                request.getSession().setAttribute("message", "Thêm người dùng mới thành công!");
                logger.info("Admin {} created new user ({}) with role {}", adminId, email, role);
                response.sendRedirect(request.getContextPath() + "/Admin/user-list");
            } else {
                request.setAttribute("error", "Không thể thêm người dùng, vui lòng thử lại!");
                saveFormData(request, name, email, phone, address, role);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi thêm người dùng", e);
            request.setAttribute("error", "⚠ Lỗi hệ thống khi thêm người dùng!");
            saveFormData(request, name, email, phone, address, role);
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
        }
    }

    private void saveFormData(HttpServletRequest request, String name, String email,
                              String phone, String address, String role) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.setAttribute("role", role);
    }
}
