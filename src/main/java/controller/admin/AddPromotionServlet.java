package controller.admin;

import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AddPromotionServlet - Admin create new promotion
 * * @author
 * @version
 */
@WebServlet("/Admin/add-promotion")
public class AddPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddPromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // Get parameters
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));

            // Validate input
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề không được để trống");
            }

            if (discountPercentage < 0 || discountPercentage > 100) {
                throw new IllegalArgumentException("Phần trăm giảm giá phải từ 0-100");
            }

            if (endDate.before(startDate)) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
            }

            // Create promotion object
            Promotion promotion = new Promotion();
            promotion.setTitle(title.trim());
            promotion.setDescription(description != null ? description.trim() : "");
            promotion.setDiscountPercentage(discountPercentage);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            // Save to database
            int promotionId = promotionDAO.createPromotion(promotion);

            if (promotionId > 0) {
                logger.info("Created promotion {} successfully", promotionId);
                request.getSession().setAttribute("success",
                        "Thêm khuyến mãi mới thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");
            } else {
                logger.warn("Failed to create promotion");
                request.setAttribute("error", "Không thể thêm khuyến mãi mới.");
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in promotion data", e);
            request.setAttribute("error",
                    "Dữ liệu nhập không hợp lệ. Vui lòng kiểm tra lại!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                    .forward(request, response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm khuyến mãi mới", e);
            request.setAttribute("error",
                    "Đã xảy ra lỗi trong quá trình thêm khuyến mãi: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp")
                    .forward(request, response);
        }
    }
}