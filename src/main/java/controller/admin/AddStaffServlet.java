package controller.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/Admin/add-staff")
public class AddStaffServlet extends HttpServlet {
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

            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
                return;
            }


            boolean success = userDAO.register(name, email, password, phone, address);

            if (success) {

                User newUser = userDAO.login(email, password);
                if (newUser != null) {
                    userDAO.updateUserRole(newUser.getUserId(), "STAFF");
                }


                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=staff");
                return;
            } else {
                request.setAttribute("error", "Không thể thêm nhân viên. Vui lòng thử lại!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình thêm nhân viên!");
        }

        // ❌ Nếu lỗi → quay lại form
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-staff.jsp").forward(request, response);
    }
}
