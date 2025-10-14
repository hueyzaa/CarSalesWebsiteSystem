package controller.servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        String role = (String) session.getAttribute("userRole");


        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/Admin/dashboard.jsp").forward(request, response);
    }
}
