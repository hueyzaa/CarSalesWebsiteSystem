package controller.admin;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/add-user")
public class AddUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

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
        String role = request.getParameter("role");

        try {
            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("address", address);
                request.setAttribute("role", role);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
                return;
            }

            boolean success = userDAO.registerWithRole(name, email, password, phone, address, role);

            if (success) {
                request.getSession().setAttribute("message", "Thêm người dùng thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/user-list");
            } else {
                request.setAttribute("error", "Không thể thêm người dùng, vui lòng thử lại!");
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống khi thêm người dùng!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-user.jsp").forward(request, response);
        }
    }
}
