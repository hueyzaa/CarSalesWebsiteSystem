package controller.admin;

import dao.AdminDAO;
import dao.UserDAO;
import model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionUtils;

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


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("Unauthorized attempt to add staff — no session");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy user từ session (AppUsers)
        User currentUser = SessionUtils.getUser(session);
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Unauthorized attempt to add staff — no admin user in session");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Đây chính là AppUsers.user_id
        int adminId = currentUser.getUserId();

        // Lấy dữ liệu form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // (Tuỳ bạn) có thể validate sơ:
        if (email == null || email.isBlank() ||
                password == null || password.isBlank() ||
                name == null || name.isBlank()) {

            session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ Email, Mật khẩu và Tên");
            response.sendRedirect(request.getContextPath() + "/Admin/staff-list");
            return;
        }

        boolean success = adminDAO.createStaff(adminId, email, password, name, phone, address);

        if (success) {
            session.setAttribute("successMessage", "Tạo tài khoản staff thành công");
        } else {
            session.setAttribute("errorMessage", "Tạo staff thất bại. Vui lòng kiểm tra lại email hoặc thử lại sau.");
        }

        response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
    }
    private void saveFormData(HttpServletRequest request, String name, String email,
                              String phone, String address) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
    }
}
