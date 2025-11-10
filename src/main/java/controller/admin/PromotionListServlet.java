package controller.admin;

import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;


@WebServlet("/Admin/promotion-list")
public class PromotionListServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PromotionListServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Promotion> promotions = promotionDAO.getAllPromotions();
            request.setAttribute("promotions", promotions);
            request.getRequestDispatcher("/WEB-INF/views/Admin/promotion-list.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Lỗi khi tải danh sách khuyến mãi", e);
            request.setAttribute("error", "Không thể tải danh sách khuyến mãi. Vui lòng thử lại!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
