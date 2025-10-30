package controller.customer;

import dao.PromotionDAO;
import model.Promotion;
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
 */
@WebServlet("/my-promotions")
public class MyPromotionsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MyPromotionsServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        promotionDAO = new PromotionDAO();
        logger.info("MyPromotionsServlet initialized");
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
            redirectWithError(request, session, response, "/promotions",
                    "Chức năng này chỉ dành cho khách hàng!");
            return;
        }

        try {
            Integer userId = SessionUtils.getUserId(session);
            logger.info("Loading promotions for user {} ({})",
                    userId, SessionUtils.getUserEmail(session));

            List<Promotion> claimedPromotions = promotionDAO.getUserClaimedPromotions(userId);

            long unusedCount = claimedPromotions.stream()
                    .filter(p -> !p.isUsedByUser())
                    .count();

            long usedCount = claimedPromotions.stream()
                    .filter(Promotion::isUsedByUser)
                    .count();

            logger.info("User {} has {} promotions ({} unused, {} used)",
                    userId, claimedPromotions.size(), unusedCount, usedCount);

            request.setAttribute("claimedPromotions", claimedPromotions);
            request.setAttribute("unusedCount", unusedCount);
            request.setAttribute("usedCount", usedCount);
            request.setAttribute("totalPromotions", claimedPromotions.size());
            request.setAttribute("isCustomer", true);

            request.getRequestDispatcher("/WEB-INF/views/my-promotions.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error loading promotions", e);
            redirectWithError(request, session, response, "/promotions",
                    "Không thể tải danh sách khuyến mãi: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error loading promotions", e);
            redirectWithError(request, session, response, "/promotions",
                    "Đã xảy ra lỗi không mong muốn.");
        }
    }

    private void saveRedirectAndLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute("redirectAfterLogin", request.getContextPath() + "/my-promotions");
        session.setAttribute("loginMessage", "Vui lòng đăng nhập để xem khuyến mãi của bạn");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void redirectWithError(HttpServletRequest request, HttpSession session,
                                   HttpServletResponse response, String path,
                                   String errorMessage) throws IOException {
        session.setAttribute("error", errorMessage);
        response.sendRedirect(request.getContextPath() + path);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("MyPromotionsServlet destroyed");
    }
}