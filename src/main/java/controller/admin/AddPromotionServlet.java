package controller.admin;

import dao.PromotionDAO;
import dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Promotion;
import model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * AddPromotionServlet - Admin create new promotion with car selection
 * Version: 2.0 - Added car selection feature
 * Only shows AVAILABLE cars, requires at least 1 car selection
 */
@WebServlet("/Admin/add-promotion")
public class AddPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddPromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get all AVAILABLE cars for selection
            List<Car> allCars = carDAO.getAvailableCars();
            request.setAttribute("allCars", allCars);

            logger.info("Loaded {} available cars for promotion selection", allCars.size());

            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Error loading cars for promotion", e);
            request.setAttribute("error", "Không thể tải danh sách xe: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // Get promotion parameters
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));

            String[] selectedCarIds = request.getParameterValues("carIds");

            // Validate basic input
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề không được để trống");
            }

            if (discountPercentage < 1 || discountPercentage > 100) {
                throw new IllegalArgumentException("Phần trăm giảm giá phải từ 1-100");
            }

            if (endDate.before(startDate)) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
            }

            if (selectedCarIds == null || selectedCarIds.length == 0) {
                throw new IllegalArgumentException("Vui lòng chọn ít nhất một xe để áp dụng khuyến mãi");
            }

            // Convert selected car IDs to List<Integer>
            List<Integer> carIds = new ArrayList<>();
            for (String carIdStr : selectedCarIds) {
                try {
                    carIds.add(Integer.parseInt(carIdStr));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid car ID: {}", carIdStr);
                }
            }

            // Validate converted car IDs
            if (carIds.isEmpty()) {
                throw new IllegalArgumentException("Không có xe hợp lệ được chọn");
            }

            // Create promotion object
            Promotion promotion = new Promotion();
            promotion.setTitle(title.trim());
            promotion.setDescription(description != null ? description.trim() : "");
            promotion.setDiscountPercentage(discountPercentage);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            int promotionId = promotionDAO.createPromotionWithCars(promotion, carIds);

            if (promotionId > 0) {
                logger.info("Created promotion {} with {} cars successfully",
                        promotionId, carIds.size());
                request.getSession().setAttribute("success",
                        "Thêm khuyến mãi mới thành công! Đã áp dụng cho " + carIds.size() + " xe.");
                response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");
            } else {
                logger.warn("Failed to create promotion");
                throw new RuntimeException("Không thể thêm khuyến mãi mới");
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in promotion data", e);
            handleError(request, response, "Dữ liệu nhập không hợp lệ. Vui lòng kiểm tra lại!");

        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            handleError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Error creating promotion", e);
            handleError(request, response,
                    "Đã xảy ra lỗi trong quá trình thêm khuyến mãi: " + e.getMessage());
        }
    }

    /**
     * Handle error - reload cars and forward back to form
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);

        // Reload cars for re-display
        try {
            List<Car> allCars = carDAO.getAvailableCars();
            request.setAttribute("allCars", allCars);
        } catch (Exception ex) {
            logger.error("Error reloading cars after error", ex);
        }

        request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                .forward(request, response);
    }
}