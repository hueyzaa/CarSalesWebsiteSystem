package controller.customer;

import dao.PromotionDAO;
import dto.PromotionDTO;
import model.Promotion;
import service.PromotionService;
import util.SessionUtils;
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

/**
 * MyPromotionsServlet - Display user's claimed promotions
 * Only accessible by customers
 * UPDATED: Uses DTOs with pre-calculated values for view layer
 */
@WebServlet("/my-promotions")
public class MyPromotionsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MyPromotionsServlet.class);
    private PromotionDAO promotionDAO;
    private PromotionService promotionService;

    @Override
    public void init() {
        promotionDAO = new PromotionDAO();
        promotionService = new PromotionService();
        logger.info("MyPromotionsServlet initialized");
    }

    /**
     * Check if promotion is used by user
     */
    private boolean isPromotionUsedByUser(Promotion promotion) {
        return promotion != null && promotion.isUsedByUser();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            saveRedirectAndLogin(request, response);
            return;
        }

        if (!SessionUtils.isCustomer(session)) {
            logger.warn("Non-customer (role: {}) attempted to access my-promotions",
                    SessionUtils.getUserRole(session));
            redirectWithError(session, response, "/promotions",
                    "Chức năng này chỉ dành cho khách hàng!");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            logger.info("Loading promotions for user {} ({})",
                    userId, SessionUtils.getUserEmail(session));

            // Get promotions from DAO (returns Model objects)
            List<Promotion> claimedPromotions = promotionDAO.getUserClaimedPromotions(userId);

            // Convert Models to DTOs with pre-calculated values
            List<PromotionDTO> promotionDTOs = promotionService.toPromotionDTOs(claimedPromotions);

            logger.debug("Converted {} promotions to DTOs", promotionDTOs.size());

            long unusedCount = countUnused(claimedPromotions);
            long usedCount = countUsed(claimedPromotions);

            logger.info("User {} has {} promotions ({} unused, {} used)",
                    userId, promotionDTOs.size(), unusedCount, usedCount);

            // Set attributes - IMPORTANT: Use DTOs for view
            setPromotionAttributes(request, promotionDTOs, unusedCount, usedCount);
            forward(request, response, "/WEB-INF/views/Customer/my-promotions.jsp");

        } catch (RuntimeException e) {
            logger.error("Database error loading promotions", e);
            redirectWithError(session, response, "/promotions",
                    "Không thể tải danh sách khuyến mãi: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error loading promotions", e);
            redirectWithError(session, response, "/promotions",
                    "Đã xảy ra lỗi không mong muốn.");
        }
    }

    // ============ HELPER METHODS ============

    private long countUnused(List<Promotion> promotions) {
        return promotions.stream()
                .filter(p -> !isPromotionUsedByUser(p))
                .count();
    }

    private long countUsed(List<Promotion> promotions) {
        return promotions.stream()
                .filter(this::isPromotionUsedByUser)
                .count();
    }

    /**
     * Set all request attributes for JSP
     * IMPORTANT: Pass DTOs to view, not Models
     */
    private void setPromotionAttributes(HttpServletRequest request,
                                        List<PromotionDTO> promotionDTOs,
                                        long unusedCount, long usedCount) {
        request.setAttribute("claimedPromotions", promotionDTOs);
        request.setAttribute("unusedCount", unusedCount);
        request.setAttribute("usedCount", usedCount);
        request.setAttribute("totalPromotions", promotionDTOs.size());
        request.setAttribute("isCustomer", true);
    }

    private void saveRedirectAndLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute("redirectAfterLogin", request.getContextPath() + "/my-promotions");
        session.setAttribute("loginMessage", "Vui lòng đăng nhập để xem khuyến mãi của bạn");
        redirect(response, request.getContextPath() + "/login");
    }

    // ============ UTILITY METHODS ============

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithError(HttpSession session, HttpServletResponse response,
                                   String path, String errorMessage) throws IOException {
        session.setAttribute("error", errorMessage);
        redirect(response, session.getServletContext().getContextPath() + path);
    }

    @SuppressWarnings("SameParameterValue")
    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}