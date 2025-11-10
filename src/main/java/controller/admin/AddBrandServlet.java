package controller.admin;

import dao.BrandDAO;
import model.Brand;
import util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

@WebServlet("/Admin/add-brand")
public class AddBrandServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddBrandServlet.class);
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error loading add brand page", e);
            request.setAttribute("error", "Không thể tải thêm hãng xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("brandId"), "Hãng xe");
            String brandName = ValidationUtil.validateBrand(request.getParameter("brandName"));
            Brand br = new Brand();
            br.setBrandId(brandId);
            br.setBrandName(brandName);
            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=cars");
        } catch (Exception e) {
            handleError(request, response, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException  {

        request.setAttribute("error", errorMessage);
        request.setAttribute("brandId", request.getParameter("brandId"));
        request.setAttribute("brandName", request.getParameter("brandName"));


        try {
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
        } catch (Exception e) {
            logger.error("Tải trang thất bại bởi lỗi ", e);
        }
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-brand.jsp").forward(request, response);
    }
}

