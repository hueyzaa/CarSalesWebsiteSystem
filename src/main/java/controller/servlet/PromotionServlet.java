package controller.servlet;

import dao.PromotionDAO;
import model.Promotion;
import model.User;
import exception.DatabaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/promotions")
public class PromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PromotionServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        promotionDAO = new PromotionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            logger.info("Loading promotions page");

            // Check if user is logged in
            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

            // Get promotions with user status if logged in
            List<Promotion> promotions;
            if (currentUser != null) {
                promotions = promotionDAO.getAllActivePromotionsWithUserStatus(currentUser.getUserId());
                logger.info("Retrieved {} promotions for logged-in user {}",
                        promotions.size(), currentUser.getEmail());

                // Get unused promotion count for notification
                try {
                    List<Promotion> userPromotions = promotionDAO.getUserClaimedPromotions(currentUser.getUserId());
                    long unusedCount = userPromotions.stream()
                            .filter(p -> !p.isUsedByUser())
                            .count();
                    request.setAttribute("unusedCount", unusedCount);
                } catch (DatabaseException e) {
                    logger.error("Error getting unused promotion count", e);
                }
            } else {
                promotions = promotionDAO.getAllActivePromotionsWithUserStatus(null);
                logger.info("Retrieved {} promotions for guest user", promotions.size());
            }

            request.setAttribute("promotions", promotions);
            request.setAttribute("isLoggedIn", currentUser != null);
            request.getRequestDispatcher("/WEB-INF/views/promotions.jsp").forward(request, response);

        } catch (DatabaseException e) {
            logger.error("Database error loading promotions", e);
            request.setAttribute("error", "Không thể tải thông tin khuyến mãi.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading promotions", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}