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
 * IndexServlet - Landing page
 * Shows different view based on login status:
 * - Guest: index.jsp (landing page)
 * - Logged-in user: home.jsp (dashboard)
 */
@WebServlet({"/", "/index"})
public class IndexServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(IndexServlet.class);
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        carDAO = new CarDAO();
        logger.info("IndexServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            boolean isLoggedIn = SessionUtils.isLoggedIn(session);

            // Load featured cars for display
            List<Car> cars = carDAO.getAvailableCars();

            // Limit to featured cars (e.g., 6-8 cars)
            if (cars != null && cars.size() > 8) {
                cars = cars.subList(0, 8);
            }

            request.setAttribute("cars", cars);
            logger.debug("Loaded {} featured cars", cars != null ? cars.size() : 0);

            // Route based on login status
            if (isLoggedIn) {
                logger.debug("Logged-in user accessing index, showing home page");
                request.getRequestDispatcher("/WEB-INF/views/Customer/home.jsp")
                        .forward(request, response);
            } else {
                logger.debug("Guest accessing index, showing landing page");
                request.getRequestDispatcher("/WEB-INF/views/Customer/index.jsp")
                        .forward(request, response);
            }

        } catch (RuntimeException e) {
            logger.error("Database error loading index page", e);
            handleError(request, response, "Không thể tải trang chủ.");
        } catch (Exception e) {
            logger.error("Unexpected error loading index page", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    /**
     * Handle error and forward to error page
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/Customer/error.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("IndexServlet destroyed");
    }
}