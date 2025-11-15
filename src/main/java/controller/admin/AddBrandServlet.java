package controller.admin;

import dao.BrandDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@WebServlet("/Admin/add-brand")
public class AddBrandServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddBrandServlet.class);
    private BrandDAO brandDAO;

    @Override
    public void init() {
        brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String brandName = request.getParameter("brandName");

            if (brandName == null || brandName.trim().isEmpty()) {
                request.setAttribute("error", "Tên hãng xe không được để trống.");
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
                return;
            }

            // Kiểm tra trùng
            if (brandDAO.brandExists(brandName)) {
                request.setAttribute("error", "Hãng xe này đã tồn tại.");
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
                return;
            }

            // Thêm brand
            brandDAO.addBrand(brandName);

            // Quay về dashboard
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=brands");

        } catch (Exception e) {
            logger.error("Lỗi thêm brand", e);
            request.setAttribute("error", "Đã xảy ra lỗi.");
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
        }
    }
}
