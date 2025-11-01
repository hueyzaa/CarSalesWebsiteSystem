package controller.customer;

import dao.PromotionDAO;
import model.Customer;
import util.SessionUtils;
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
    public void init() {
        promotionDAO = new PromotionDAO();
        logger.info("ClaimPromotionServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.info("Guest trying to claim promotion, redirecting to login");
            saveRedirectAndLogin(request, response);
            return;
        }

        if (!SessionUtils.isCustomer(session)) {
            logger.warn("Non-customer (role: {}) attempted to claim promotion",
                    SessionUtils.getUserRole(session));
            redirectWithError(session, response, "/promotions",
                    "Chỉ khách hàng mới có thể nhận khuyến mãi!");
            return;
        }

        try {
            Customer customer = SessionUtils.getCustomer(session);

            String promotionIdParam = request.getParameter("promotionId");
            if (isEmpty(promotionIdParam)) {
                logger.warn("Promotion ID missing in claim request");
                redirectWithError(session, response, "/promotions",
                        "Không tìm thấy thông tin khuyến mãi!");
                return;
            }

            int promotionId = Integer.parseInt(promotionIdParam);
            int customerId = customer.getCustomerId();

            logger.info("Customer {} (ID: {}) claiming promotion {}",
                    customer.getEmail(), customerId, promotionId);

            if (promotionDAO.claimPromotion(customerId, promotionId)) {
                setSuccessMessage(session, "Nhận khuyến mãi thành công! Bạn có thể sử dụng khi thanh toán.");
                logger.info("Customer {} successfully claimed promotion {}",
                        customer.getEmail(), promotionId);
            } else {
                setErrorMessage(session, "Không thể nhận khuyến mãi. Vui lòng thử lại!");
                logger.warn("Failed to claim promotion {} for customer {}",
                        promotionId, customer.getEmail());
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid promotion ID format", e);
            setErrorMessage(session, "ID khuyến mãi không hợp lệ!");
        } catch (RuntimeException e) {
            logger.error("Database error claiming promotion", e);
            setErrorMessage(session, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error claiming promotion", e);
            setErrorMessage(session, "Đã xảy ra lỗi không mong muốn!");
        }

        redirectToSource(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        logger.warn("GET request to claim promotion, redirecting");
        redirect(response, request.getContextPath() + "/promotions");
    }

    // ============ HELPER METHODS ============

    private void saveRedirectAndLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(true);

        String redirectUrl = request.getParameter("redirectUrl");
        String savedUrl = isNotEmpty(redirectUrl)
                ? request.getContextPath() + "/" + redirectUrl
                : request.getContextPath() + "/promotions";

        session.setAttribute("redirectAfterLogin", savedUrl);
        session.setAttribute("loginMessage", "Vui lòng đăng nhập để nhận khuyến mãi");
        redirect(response, request.getContextPath() + "/login");
    }

    private void redirectToSource(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUrl = request.getParameter("redirectUrl");

        if (isNotEmpty(redirectUrl)) {
            logger.debug("Redirecting to: {}", redirectUrl);
            redirect(response, request.getContextPath() + "/" + redirectUrl);
        } else {
            redirect(response, request.getContextPath() + "/promotions");
        }
    }

    // ============ UTILITY METHODS ============

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    @SuppressWarnings("SameParameterValue")
    private void setSuccessMessage(HttpSession session, String message) {
        session.setAttribute("successMessage", message);
    }

    @SuppressWarnings("SameParameterValue")
    private void setErrorMessage(HttpSession session, String message) {
        session.setAttribute("errorMessage", message);
    }

    private void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithError(HttpSession session, HttpServletResponse response,
                                   String path, String errorMessage) throws IOException {
        setErrorMessage(session, errorMessage);
        redirect(response, session.getServletContext().getContextPath() + path);
    }
}