package controller.admin;

import dao.BrandDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Brand;

@WebServlet("/Admin/brand-list")
public class BrandListServlet extends HttpServlet {
    private final BrandDAO brandDAO = new BrandDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Brand> brans = brandDAO.getAllBrands();
        request.setAttribute("brans", brans);
        request.getRequestDispatcher("/WEB-INF/views/Admin/brand-list.jsp").forward(request, response);
    }
}