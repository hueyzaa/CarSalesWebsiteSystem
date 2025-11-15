package controller.admin;

import dao.BrandDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ValidationUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/Admin/update-brand")
public class UpdateBrandServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdateBrandServlet.class);
    private BrandDAO brandDAO;

    @Override
    public void init() {
        brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy brand hiện tại theo id
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("id"), "Mã hãng");
            Brand brand = brandDAO.getBrandById(brandId);

            if (brand == null) {
                request.getSession().setAttribute("error", "Không tìm thấy hãng cần cập nhật!");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
                return;
            }

            // Lấy tất cả brand để hiển thị trong select
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
            request.setAttribute("brand", brand);

            request.getRequestDispatcher("/WEB-INF/views/Admin/update-brand.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Lỗi khi tải form cập nhật hãng", e);
            request.setAttribute("error", "Không thể tải thông tin hãng. Vui lòng thử lại.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("brandId"), "Mã hãng");
            String brandName = request.getParameter("brandName");

            if (brandName == null || brandName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên hãng không được để trống!");
            }

            boolean updated = brandDAO.updateBrand(brandId, brandName);

            if (updated) {
                request.getSession().setAttribute("success", "Cập nhật hãng thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
            } else {
                throw new Exception("Không thể cập nhật hãng, vui lòng thử lại.");
            }

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật hãng", e);
            handleError(request, response, e.getMessage());
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);

        try {
            // Nạp lại tất cả brand để hiển thị trong form nếu có lỗi
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
        } catch (Exception e) {
            logger.warn("Không thể nạp lại danh sách hãng khi xảy ra lỗi", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/Admin/update-brand.jsp").forward(request, response);
    }
}
