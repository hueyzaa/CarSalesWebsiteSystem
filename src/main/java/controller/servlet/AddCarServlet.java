package controller.servlet;

import dao.BrandDAO;
import dao.CarDAO;
import model.Car;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/add-car")
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
            // Get brand list for dropdown
            List<model.Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
            request.getRequestDispatcher("/WEB-INF/views/admin/add-car.jsp").forward(request, response);

        } catch (DatabaseException e) {
            logger.error("Error loading add car page", e);
            request.setAttribute("error", "Không thể tải trang. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validate and extract parameters
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("brandId"), "Hãng xe");
            String model = ValidationUtil.validateString(request.getParameter("model"), "Tên mẫu xe", 100);
            BigDecimal price = ValidationUtil.validatePrice(request.getParameter("price"));
            String status = ValidationUtil.validateStatus(request.getParameter("status"));
            String description = request.getParameter("description");

            // Validate description
            if (description != null && !description.trim().isEmpty()) {
                description = ValidationUtil.validateString(description, "Mô tả", 1000);
            } else {
                description = null;
            }

            // Validate image URLs
            String[] imageUrls = request.getParameterValues("imageUrls");
            String primaryImageIndex = request.getParameter("primaryImage");

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

            // Create car object
            Car car = new Car();
            car.setBrandId(brandId);
            car.setModel(model);
            car.setPrice(price);
            car.setStatus(status);
            car.setDescription(description);

            // Add car to database
            int carId = carDAO.addCar(car);

            if (carId == -1) {
                throw new DatabaseException("Không thể thêm xe vào cơ sở dữ liệu");
            }

            // Add images if available
            if (!validImageUrls.isEmpty()) {
                int primaryIndex = 0;
                if (primaryImageIndex != null) {
                    try {
                        primaryIndex = Integer.parseInt(primaryImageIndex);
                        if (primaryIndex < 0 || primaryIndex >= validImageUrls.size()) {
                            primaryIndex = 0;
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid primary image index, using default", e);
                    }
                }

                carDAO.addCarImages(carId, validImageUrls, primaryIndex);
            }

            // Success
            logger.info("Car added successfully with ID: {}", carId);
            request.getSession().setAttribute("success", "Thêm xe thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");

        } catch (ValidationException e) {
            logger.warn("Validation error in add car: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (DatabaseException e) {
            logger.error("Database error in add car", e);
            handleError(request, response, "Lỗi cơ sở dữ liệu. Vui lòng thử lại.");

        } catch (Exception e) {
            logger.error("Unexpected error in add car", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.");
        }
    }

    /**
     * Handle error and forward back to form
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);

        // Preserve form data
        request.setAttribute("model", request.getParameter("model"));
        request.setAttribute("price", request.getParameter("price"));
        request.setAttribute("status", request.getParameter("status"));
        request.setAttribute("description", request.getParameter("description"));
        request.setAttribute("brandId", request.getParameter("brandId"));

        // Reload brand list
        try {
            List<model.Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("brandList", brandList);
        } catch (DatabaseException e) {
            logger.error("Error loading brands for error page", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/add-car.jsp").forward(request, response);
    }
}