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

@WebServlet("/my-promotions")
public class MyPromotionsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MyPromotionsServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        promotionDAO = new PromotionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        // Redirect to login if not logged in
        if (currentUser == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getContextPath() + "/my-promotions");
            session.setAttribute("loginMessage", "Vui lòng đăng nhập để xem khuyến mãi của bạn");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            logger.info("Loading my promotions page for user: {}", currentUser.getEmail());

            // Get user's claimed promotions
            List<Promotion> claimedPromotions = promotionDAO.getUserClaimedPromotions(currentUser.getUserId());

            // Separate into used and unused
            long unusedCount = claimedPromotions.stream()
                    .filter(p -> !p.isUsedByUser())
                    .count();

            long usedCount = claimedPromotions.stream()
                    .filter(Promotion::isUsedByUser)
                    .count();

            logger.info("User {} has {} claimed promotions ({} unused, {} used)",
                    currentUser.getEmail(), claimedPromotions.size(), unusedCount, usedCount);

            request.setAttribute("claimedPromotions", claimedPromotions);
            request.setAttribute("unusedCount", unusedCount);
            request.setAttribute("usedCount", usedCount);
            request.getRequestDispatcher("/WEB-INF/views/my-promotions.jsp").forward(request, response);

        } catch (DatabaseException e) {
            logger.error("Database error loading my promotions", e);
            request.setAttribute("error", "Không thể tải danh sách khuyến mãi của bạn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading my promotions", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}