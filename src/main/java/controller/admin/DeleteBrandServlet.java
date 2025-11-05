package controller.admin;

import dao.BrandDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Brand;

import java.io.IOException;

@WebServlet("/Admin/delete-brand")
public class DeleteBrandServlet extends HttpServlet {
    private final BrandDAO brandDAO = new BrandDAO();
    private Brand BrandId;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/cars?error=missingId");
            return;
        }
        try {
            int brandId = Integer.parseInt(idParam);

            boolean success = brandDAO.deleteBrand(BrandId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/cars?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/cars?error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/brands?error=invalidId");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/brans?error=exception");
        }
    }
}
