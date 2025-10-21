package controller.customer;

import dao.PromotionDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/promotions/claim")
public class ClaimPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ClaimPromotionServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        promotionDAO = new PromotionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        // If not logged in, redirect to login
        if (currentUser == null) {
            logger.info("Guest trying to claim promotion, redirecting to login");
            session = request.getSession(true);

            // Save redirect URL to return after login
            String redirectUrl = request.getParameter("redirectUrl");
            if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
                session.setAttribute("redirectAfterLogin", request.getContextPath() + "/" + redirectUrl);
            } else {
                session.setAttribute("redirectAfterLogin", request.getContextPath() + "/promotions");
            }

            session.setAttribute("loginMessage", "Vui lòng đăng nhập để nhận khuyến mãi");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Validate promotion ID parameter
            String promotionIdParam = request.getParameter("promotionId");
            if (promotionIdParam == null || promotionIdParam.trim().isEmpty()) {
                logger.warn("Promotion ID missing in claim request");
                session.setAttribute("errorMessage", "Không tìm thấy thông tin khuyến mãi!");
                response.sendRedirect(request.getContextPath() + "/promotions");
                return;
            }

            int promotionId = Integer.parseInt(promotionIdParam);
            int userId = currentUser.getUserId();

            logger.info("User {} (ID: {}) attempting to claim promotion {}",
                    currentUser.getEmail(), userId, promotionId);

            // Claim promotion
            boolean success = promotionDAO.claimPromotion(userId, promotionId);

            if (success) {
                session.setAttribute("successMessage",
                        "Nhận khuyến mãi thành công! Bạn có thể sử dụng khi thanh toán.");
                logger.info("User {} successfully claimed promotion {}",
                        currentUser.getEmail(), promotionId);
            } else {
                session.setAttribute("errorMessage",
                        "Không thể nhận khuyến mãi. Vui lòng thử lại!");
                logger.warn("Failed to claim promotion {} for user {}",
                        promotionId, currentUser.getEmail());
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID format", e);
            session.setAttribute("errorMessage", "ID khuyến mãi không hợp lệ!");

        } catch (RuntimeException e) {
            logger.error("Database error claiming promotion", e);
            session.setAttribute("errorMessage", e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error claiming promotion", e);
            session.setAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn!");
        }

        // Smart redirect - return to where user came from
        String redirectUrl = request.getParameter("redirectUrl");

        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            logger.info("Redirecting back to: {}", redirectUrl);
            response.sendRedirect(request.getContextPath() + "/" + redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/promotions");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.warn("GET request to claim promotion endpoint, redirecting to promotions page");
        response.sendRedirect(request.getContextPath() + "/promotions");
    }
}