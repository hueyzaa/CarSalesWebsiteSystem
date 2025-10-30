package controller.customer;

import dao.PromotionDAO;
import model.Customer;
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

/**
 * ClaimPromotionServlet - Handle promotion claiming
 * Only accessible by customers
 */
@WebServlet("/promotions/claim")
public class ClaimPromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ClaimPromotionServlet.class);
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        promotionDAO = new PromotionDAO();
        logger.info("ClaimPromotionServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.info("Guest trying to claim promotion, redirecting to login");
            saveRedirectAndLogin(request, response);
            return;
        }

        if (!SessionUtils.isCustomer(session)) {
            logger.warn("Non-customer (role: {}) attempted to claim promotion",
                    SessionUtils.getUserRole(session));
            redirectWithError(request, session, response, "/promotions",
                    "Chỉ khách hàng mới có thể nhận khuyến mãi!");
            return;
        }

        try {
            Customer customer = SessionUtils.getCustomer(session);

            String promotionIdParam = request.getParameter("promotionId");
            if (promotionIdParam == null || promotionIdParam.trim().isEmpty()) {
                logger.warn("Promotion ID missing in claim request");
                redirectWithError(request, session, response, "/promotions",
                        "Không tìm thấy thông tin khuyến mãi!");
                return;
            }

            int promotionId = Integer.parseInt(promotionIdParam);
            int customerId = customer.getCustomerId();

            logger.info("Customer {} (ID: {}) claiming promotion {}",
                    customer.getEmail(), customerId, promotionId);

            boolean success = promotionDAO.claimPromotion(customerId, promotionId);

            if (success) {
                session.setAttribute("successMessage",
                        "Nhận khuyến mãi thành công! Bạn có thể sử dụng khi thanh toán.");
                logger.info("Customer {} successfully claimed promotion {}",
                        customer.getEmail(), promotionId);
            } else {
                session.setAttribute("errorMessage",
                        "Không thể nhận khuyến mãi. Vui lòng thử lại!");
                logger.warn("Failed to claim promotion {} for customer {}",
                        promotionId, customer.getEmail());
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

        redirectToSource(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.warn("GET request to claim promotion, redirecting");
        response.sendRedirect(request.getContextPath() + "/promotions");
    }

    /**
     * Save redirect URL and send to login
     */
    private void saveRedirectAndLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(true);

        String redirectUrl = request.getParameter("redirectUrl");
        String savedUrl = (redirectUrl != null && !redirectUrl.trim().isEmpty())
                ? request.getContextPath() + "/" + redirectUrl
                : request.getContextPath() + "/promotions";

        session.setAttribute("redirectAfterLogin", savedUrl);
        session.setAttribute("loginMessage", "Vui lòng đăng nhập để nhận khuyến mãi");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    /**
     * Redirect back to source page or default to promotions
     */
    private void redirectToSource(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUrl = request.getParameter("redirectUrl");

        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            logger.debug("Redirecting to: {}", redirectUrl);
            response.sendRedirect(request.getContextPath() + "/" + redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/promotions");
        }
    }

    /**
     * Redirect with error message
     */
    private void redirectWithError(HttpServletRequest request, HttpSession session,
                                   HttpServletResponse response, String path,
                                   String errorMessage) throws IOException {
        session.setAttribute("errorMessage", errorMessage);
        response.sendRedirect(request.getContextPath() + path);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("ClaimPromotionServlet destroyed");
    }
}