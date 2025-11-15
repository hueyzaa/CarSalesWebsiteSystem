package controller.admin;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Admin/delete-user")
public class DeleteUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            boolean success = userDAO.deleteUser(userId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?deleteSuccess=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard?deleteFailed=true");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?deleteError=true");
        }
    }
}

