package controller.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/update-user")
public class UpdateUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
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

        int userId = Integer.parseInt(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);
        user.setStatus(status);

        try {
            boolean updated = userDAO.updateUser(user);
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/Admin/user-list?success=1");
            } else {
                request.setAttribute("error", "Không thể cập nhật người dùng. Vui lòng thử lại!");
                request.setAttribute("user", userDAO.getUserById(userId));
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật!");
            request.setAttribute("user", userDAO.getUserById(userId));
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);
        }
    }
}
