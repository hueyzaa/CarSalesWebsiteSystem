package controller.admin;

import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/Admin/delete-promotion")
public class DeletePromotionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeletePromotionServlet.class);
    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = promotionDAO.deletePromotion(id);

            if (deleted) {
                request.getSession().setAttribute("success", "Xóa khuyến mãi thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể xóa khuyến mãi (có thể không tồn tại).");
            }

            response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");

        } catch (Exception e) {
            logger.error("Lỗi khi xóa khuyến mãi", e);
            request.getSession().setAttribute("error", "Đã xảy ra lỗi khi xóa khuyến mãi!");
            response.sendRedirect(request.getContextPath() + "/Admin/promotion-list");
        }
    }
}

