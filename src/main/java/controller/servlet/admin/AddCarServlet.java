package controller.servlet.admin;

import dao.BrandDAO;
import dao.CarDAO;
import model.Car;
import model.Brand;
import util.ValidationUtil;
import exception.ValidationException;
import exception.DatabaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Admin/add-car")
public class AddCarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddCarServlet.class);
    private CarDAO carDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-car.jsp").forward(request, response);
        } catch (DatabaseException e) {
            logger.error("Error loading add car page", e);
            request.setAttribute("error", "Không thể tải danh sách hãng xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // === Validate dữ liệu ===
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("brandId"), "Hãng xe");
            String model = ValidationUtil.validateString(request.getParameter("model"), "Tên mẫu xe", 100);
            double price = ValidationUtil.validatePrice(request.getParameter("price")).doubleValue();
            String status = ValidationUtil.validateStatus(request.getParameter("status"));
            String description = request.getParameter("description");
            if (description != null && !description.isBlank()) {
                description = ValidationUtil.validateString(description, "Mô tả", 1000);
            }

            Integer year = parseOptionalInt(request.getParameter("year"), "Năm sản xuất");
            Integer stock = parseOptionalInt(request.getParameter("stock"), "Số lượng tồn kho");
            String color = request.getParameter("color");
            if (color != null && !color.trim().isEmpty()) {
                color = ValidationUtil.validateString(color, "Màu sắc", 50);
            }

            String[] imageUrls = request.getParameterValues("imageUrls");
            List<String> validImageUrls = new ArrayList<>();
            if (imageUrls != null) {
                for (String url : imageUrls) {
                    if (url != null && !url.trim().isEmpty()) {
                        String validUrl = ValidationUtil.validateUrl(url);
                        if (validUrl != null && validUrl.length() <= 255) {
                            validImageUrls.add(validUrl);
                        }
                    }
                }
            }


            Car car = new Car();
            car.setBrandId(brandId);
            car.setName(model);
            car.setPrice(price);
            car.setStatus(status);
            car.setDescription(description);
            car.setYear(year != null ? year : 0);
            car.setColor(color);
            car.setStock(stock != null ? stock : 0);

            int carId = carDAO.addCar(car);
            if (carId <= 0)
                throw new DatabaseException("Không thể thêm xe vào cơ sở dữ liệu");


            if (!validImageUrls.isEmpty()) {
                int primaryIndex = 0;
                try {
                    String primaryParam = request.getParameter("primaryImage");
                    if (primaryParam != null) {
                        int index = Integer.parseInt(primaryParam);
                        if (index >= 0 && index < validImageUrls.size()) {
                            primaryIndex = index;
                        }
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid primary image index, defaulting to 0");
                }
                boolean imageAdded = carDAO.addCarImages(carId, validImageUrls, primaryIndex);
                if (!imageAdded)
                    logger.warn("Ảnh xe không được lưu thành công cho carId = {}", carId);
            }

            logger.info("Xe mới được thêm thành công: ID = {}", carId);


            response.sendRedirect(request.getContextPath() + "/Admin/dashboard?section=cars");

        } catch (ValidationException e) {
            logger.warn("Validation failed: {}", e.getMessage());
            handleError(request, response, e.getMessage());
        } catch (DatabaseException e) {
            logger.error("Database error when adding car", e);
            handleError(request, response, "Lỗi cơ sở dữ liệu. Vui lòng thử lại.");
        } catch (Exception e) {
            logger.error("Unexpected error in add car", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.");
        }
    }

    private Integer parseOptionalInt(String param, String fieldName) throws ValidationException {
        if (param == null || param.trim().isEmpty()) return null;
        return ValidationUtil.validatePositiveInt(param, fieldName);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {

        request.setAttribute("error", errorMessage);
        request.setAttribute("brandId", request.getParameter("brandId"));
        request.setAttribute("model", request.getParameter("model"));
        request.setAttribute("price", request.getParameter("price"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("description", request.getParameter("description"));
        request.setAttribute("year", request.getParameter("year"));
        request.setAttribute("color", request.getParameter("color"));
        request.setAttribute("stock", request.getParameter("stock"));

        try {
            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
        } catch (DatabaseException e) {
            logger.error("Failed to reload brand list after error", e);
        }


        request.getRequestDispatcher("/WEB-INF/views/Admin/add-car.jsp").forward(request, response);
    }
}
