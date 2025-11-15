package controller.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Admin/update-user")
public class UpdateUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Admin/user-list");
            return;
        }

        try {
            int userId = Integer.parseInt(idStr);
            User user = userDAO.getUserById(userId);
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/Admin/user-list?error=notfound");
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
        String userIdStr = request.getParameter("userId");
        try {
            int userId = Integer.parseInt(userIdStr);
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String role = request.getParameter("role");
            boolean active = "ACTIVE".equalsIgnoreCase(request.getParameter("status"));

            boolean success = userDAO.updateUserInfo(userId, name, phone, address, role, active);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?updateSuccess=true");
            } else {
                request.setAttribute("error", "Cập nhật thất bại");
                request.setAttribute("user", userDAO.getUserById(userId));
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-user.jsp").forward(request, response);
            }


        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Admin/user-list");
        }
    }
}


