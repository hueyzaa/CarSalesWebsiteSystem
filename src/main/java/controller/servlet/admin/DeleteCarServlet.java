package controller.servlet.admin;

import dao.CarDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/delete-car")
public class DeleteCarServlet extends HttpServlet {
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/cars?error=missingId");
            return;
        }

        try {
            int carId = Integer.parseInt(idParam);

            boolean success = carDAO.deleteCar(carId);

            if (success) {

                response.sendRedirect(request.getContextPath() + "/admin/cars?success=deleted");
            } else {

                response.sendRedirect(request.getContextPath() + "/admin/cars?error=notfound");
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(request.getContextPath() + "/admin/cars?error=invalidId");

        } catch (Exception e) {

            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/cars?error=exception");
        }
    }
}
