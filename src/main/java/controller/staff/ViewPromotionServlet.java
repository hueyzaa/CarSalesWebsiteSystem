package controller.staff;

import dao.PromotionDAO;
import model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionUtils;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff/promotions")
public class ViewPromotionServlet extends HttpServlet{
    private static final Logger logger = LoggerFactory.getLogger(controller.staff.ViewPromotionServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        promotionDAO = new PromotionDAO();
        logger.info("PromotionServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            Integer userId = SessionUtils.getUserId(session);

            // Get promotions (personalized if logged in)
            List<Promotion> promotions = promotionDAO.getAllActivePromotionsWithUserStatus(userId);

            logger.info("Retrieved {} promotions for {} user{}",
                    promotions.size(),
                    userId != null ? "logged-in" : "guest",
                    userId != null ? " ID: " + userId : "");

            // Get unused promotion count for customers
            if (SessionUtils.isStaff(session)) {
                setUnusedPromotionCount(request, userId);
            }

            // Set attributes
            setRequestAttributes(request, session, promotions);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/Staff/promotion.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error loading promotions", e);
            handleError(request, response, "Không thể tải thông tin khuyến mãi: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error loading promotions", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    /**
     * Set unused promotion count for customer notification
     */
    private void setUnusedPromotionCount(HttpServletRequest request, Integer userId) {
        try {
            List<Promotion> userPromotions = promotionDAO.getUserClaimedPromotions(userId);
            long unusedCount = userPromotions.stream()
                    .filter(p -> !p.isUsedByUser())
                    .count();
            request.setAttribute("unusedCount", unusedCount);

            logger.debug("User {} has {} unused promotions", userId, unusedCount);
        } catch (Exception e) {
            logger.error("Error getting unused promotion count for user {}", userId, e);
        }
    }

    /**
     * Set all request attributes for JSP
     */
    private void setRequestAttributes(HttpServletRequest request, HttpSession session,
                                      List<Promotion> promotions) {
        // Promotion data
        request.setAttribute("promotions", promotions);
        request.setAttribute("totalPromotions", promotions.size());

        // User info
        request.setAttribute("isLoggedIn", SessionUtils.isLoggedIn(session));
        request.setAttribute("isCustomer", SessionUtils.isCustomer(session));
        request.setAttribute("isStaff", SessionUtils.isStaff(session));
        request.setAttribute("isAdmin", SessionUtils.isAdmin(session));

        if (SessionUtils.isLoggedIn(session)) {
            request.setAttribute("userId", SessionUtils.getUserId(session));
            request.setAttribute("userRole", SessionUtils.getUserRole(session));
            request.setAttribute("userEmail", SessionUtils.getUserEmail(session));
        }
    }

    /**
     * Handle error and forward to error page
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("PromotionServlet destroyed");
    }
}