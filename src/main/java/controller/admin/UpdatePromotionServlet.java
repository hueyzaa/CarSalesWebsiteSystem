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

@WebServlet("/Admin/update-promotion")
public class UpdatePromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdatePromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Promotion promotion = promotionDAO.getPromotionById(id);

            if (promotion == null) {
                request.setAttribute("error", "Không tìm thấy khuyến mãi!");
                request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp")
                        .forward(request, response);
                return;
            }

            request.setAttribute("promotion", promotion);
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID format", e);
            request.setAttribute("error", "ID khuyến mãi không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Lỗi khi tải form cập nhật khuyến mãi", e);
            request.setAttribute("error", "Không thể tải thông tin khuyến mãi.");
            request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get parameters
            int id = Integer.parseInt(request.getParameter("promotionId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));

            // Validate
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
            promotion.setPromotionId(id);
            promotion.setTitle(title.trim());
            promotion.setDescription(description != null ? description.trim() : "");
            promotion.setDiscountPercentage(discountPercentage);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            // Update promotion
            boolean success = promotionDAO.updatePromotion(promotion);

            if (success) {
                logger.info("Updated promotion {} successfully", id);
                request.getSession().setAttribute("success", "Cập nhật khuyến mãi thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/dashboard");
            } else {
                logger.warn("Failed to update promotion {}", id);
                request.setAttribute("error", "Không thể cập nhật khuyến mãi.");
                request.setAttribute("promotion", promotion);
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in promotion update", e);
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ. Vui lòng kiểm tra lại!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp")
                    .forward(request, response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật khuyến mãi", e);
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình cập nhật: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp")
                    .forward(request, response);
        }
    }
}