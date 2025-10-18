package controller.servlet.admin;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/Admin/staff-list")
public class StaffListServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }


        List<User> allUsers = userDAO.getAllUsers();
        List<User> staffList = allUsers.stream()
                .filter(u -> "STAFF".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());

        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/WEB-INF/views/Admin/staff-list.jsp").forward(request, response);
    }
}
