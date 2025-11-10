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
                request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp").forward(request, response);
                return;
            }

            request.setAttribute("promotion", promotion);
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Lỗi khi tải form cập nhật khuyến mãi", e);
            request.setAttribute("error", "Không thể tải thông tin khuyến mãi.");
            request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("promotionId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));
            double discountAmount = Double.parseDouble(request.getParameter("discountAmount"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));

            Promotion promotion = new Promotion();
            promotion.setPromotionId(id);
            promotion.setTitle(title);
            promotion.setDescription(description);
            promotion.setDiscountPercentage(discountPercentage);
            promotion.setDiscountAmount(discountAmount);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            boolean success = promotionDAO.updatePromotion(promotion);

            if (success) {
                request.getSession().setAttribute("success", "Cập nhật khuyến mãi thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");
            } else {
                request.setAttribute("error", "Không thể cập nhật khuyến mãi.");
                request.setAttribute("promotion", promotion);
                request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật khuyến mãi", e);
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình cập nhật!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/update-promotion.jsp").forward(request, response);
        }
    }
}

