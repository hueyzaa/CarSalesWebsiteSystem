package controller.staff;

import dao.CustomerDAO;
import dao.UserDAO;
import model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff/customers")
public class UserListServlet extends HttpServlet {
    private final UserDAO  userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String role = (session == null) ? null : (String) session.getAttribute("userRole");

        if (role == null || !role.equalsIgnoreCase("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        List<User> userList = userDAO.getUsersByRole("CUSTOMER");

        request.setAttribute("customerList", userList);

        request.getRequestDispatcher("/WEB-INF/views/Staff/user-list.jsp").forward(request, response);
    }
}