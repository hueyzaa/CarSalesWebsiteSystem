package controller.admin;

import dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/delete-car")
public class DeleteCarServlet extends HttpServlet {
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?error=missingId");
            return;
        }

        int carId;
        try {
            carId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?error=invalidId");
            return;
        }

        boolean success = carDAO.deleteCar(carId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?error=notfound");
        }
    }
}
