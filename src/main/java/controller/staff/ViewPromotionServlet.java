package controller.staff;

import dao.PromotionDAO;
import dto.PromotionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff/promotions")
public class ViewPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(controller.staff.ViewPromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<PromotionDTO> promotions = promotionDAO.getAllPromotionsWithCars();
            request.setAttribute("promotions", promotions);
            request.getRequestDispatcher("/WEB-INF/views/Staff/promotion.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Lỗi khi tải danh sách khuyến mãi", e);
            request.setAttribute("error", "Không thể tải danh sách khuyến mãi. Vui lòng thử lại!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}