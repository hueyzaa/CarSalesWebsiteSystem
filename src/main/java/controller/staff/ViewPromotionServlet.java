package controller.staff;

import dao.PromotionDAO;
import dto.PromotionDTO;
import model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.PromotionService;
import util.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff/promotions")
public class ViewPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(controller.staff.ViewPromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();
    private PromotionService promotionService;
    // ============ BUSINESS LOGIC (moved from Model) ============

    /**
     * Check if promotion is used by user
     */
    private boolean isPromotionUsedByUser(Promotion promotion) {
        return promotion != null && promotion.isUsedByUser();
    }

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
        try {
            HttpSession session = request.getSession(false);
            Integer userId = SessionUtils.getUserId(session);

            // Get promotions from DAO (returns Model objects)
            List<Promotion> promotions = promotionDAO.getAllActivePromotionsWithUserStatus(userId);

            logger.info("Retrieved {} promotions for {} user{}",
                    promotions.size(),
                    userId != null ? "logged-in" : "guest",
                    userId != null ? " ID: " + userId : "");

            // Convert Models to DTOs with pre-calculated values
            List<PromotionDTO> promotionDTOs = promotionService.toPromotionDTOs(promotions);

            logger.debug("Converted {} promotions to DTOs", promotionDTOs.size());

            // Get unused promotion count for customers
            if (SessionUtils.isStaff(session)) {
                setUnusedPromotionCount(request, userId);
            }

            // Set attributes - IMPORTANT: Use DTOs for view
            setRequestAttributes(request, session, promotionDTOs);

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
                    .filter(p -> !isPromotionUsedByUser(p))
                    .count();
            request.setAttribute("unusedCount", unusedCount);

            logger.debug("User {} has {} unused promotions", userId, unusedCount);
        } catch (Exception e) {
            logger.error("Error getting unused promotion count for user {}", userId, e);
        }
    }

    /**
     * Set all request attributes for JSP
     * IMPORTANT: Pass DTOs to view, not Models
     */
    private void setRequestAttributes(HttpServletRequest request, HttpSession session,
                                      List<PromotionDTO> promotionDTOs) {
        // Promotion data - Use DTOs
        request.setAttribute("promotions", promotionDTOs);
        request.setAttribute("totalPromotions", promotionDTOs.size());

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