package controller.customer;

import dao.CarDAO;
import model.Car;
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
 * HomeServlet - Home page for logged-in users
 * Accessible by: Customer/Staff/Admin
 * Shows personalized dashboard with featured cars
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        carDAO = new CarDAO();
        logger.info("HomeServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isLoggedIn(session)) {
            logger.warn("Unauthenticated access to home page");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Load featured cars
            List<Car> cars = carDAO.getAvailableCars();

            // Limit to featured cars
            if (cars != null && cars.size() > 8) {
                cars = cars.subList(0, 8);
            }

            request.setAttribute("cars", cars);

            // Set user info for personalization
            request.setAttribute("userName", SessionUtils.getUserName(session));
            request.setAttribute("userRole", SessionUtils.getUserRole(session));
            request.setAttribute("isCustomer", SessionUtils.isCustomer(session));

            logger.debug("User {} (role: {}) accessing home with {} featured cars",
                    SessionUtils.getUserId(session),
                    SessionUtils.getUserRole(session),
                    cars != null ? cars.size() : 0);

            request.getRequestDispatcher("/WEB-INF/views/home.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error loading home page", e);
            handleError(request, response, "Không thể tải trang chủ.");
        } catch (Exception e) {
            logger.error("Unexpected error loading home page", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
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
        logger.info("HomeServlet destroyed");
    }
}