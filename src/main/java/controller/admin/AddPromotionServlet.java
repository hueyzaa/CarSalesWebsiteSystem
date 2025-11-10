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

@WebServlet("/Admin/add-promotion")
public class AddPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddPromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));
            double discountAmount = Double.parseDouble(request.getParameter("discountAmount"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));

            Promotion promotion = new Promotion();
            promotion.setTitle(title);
            promotion.setDescription(description);
            promotion.setDiscountPercentage(discountPercentage);
            promotion.setDiscountAmount(discountAmount);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            int promotionId = promotionDAO.createPromotion(promotion);

            if (promotionId > 0) {
                request.getSession().setAttribute("success", "Thêm khuyến mãi mới thành công!");
                response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");
            } else {
                request.setAttribute("error", "Không thể thêm khuyến mãi mới.");
                request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.error("Lỗi khi thêm khuyến mãi mới", e);
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình thêm khuyến mãi!");
            request.getRequestDispatcher("/WEB-INF/views/Admin/add-promotion.jsp").forward(request, response);
        }
    }
}
