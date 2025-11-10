
package controller.staff;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        String role = (String) session.getAttribute("userRole");


        if (role == null || !role.equalsIgnoreCase("STAFF")) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/Staff/dashboard.jsp").forward(request, response);
    }
}
