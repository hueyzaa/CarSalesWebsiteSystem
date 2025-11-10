package controller.admin;

import dao.BrandDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/Admin/delete-brand")
public class DeleteBrandServlet extends HttpServlet {
    private final BrandDAO brandDAO = new BrandDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/brand?error=missingId");
            return;
        }

        try {
            int brandId = Integer.parseInt(idParam);

            boolean success = brandDAO.deleteBrand(brandId);

            if (success) {

                response.sendRedirect(request.getContextPath() + "/admin/brand?success=deleted");
            } else {

                response.sendRedirect(request.getContextPath() + "/admin/brand?error=notfound");
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(request.getContextPath() + "/admin/brand?error=invalidId");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/brand?error=exception");
        }
    }
}

