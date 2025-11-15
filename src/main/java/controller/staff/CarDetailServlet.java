package controller.staff;

import dao.CarDAO;
import service.PromotionService;
import model.Car;
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
import java.util.Collections;
import java.util.List;

@WebServlet("/staff/car-detail")
public class CarDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarDetailServlet.class);

    private CarDAO carDAO;

    @Override
    public void init() {
        carDAO = new CarDAO();
        logger.info("CarDetailServlet initialized (simplified version)");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer carId = getCarId(request);
            if (carId == null) {
                redirectWithError(request, response, "/staff/dashboard",
                        "Không tìm thấy thông tin xe!");
                return;
            }

            Car car = carDAO.getCarById(carId);
            if (car == null) {
                redirectWithError(request, response, "/staff/dashboard",
                        "Xe không tồn tại!");
                return;
            }

            // Gắn vào request cho JSP dùng: ${car...}
            request.setAttribute("car", car);

            forward(request, response, "/WEB-INF/views/Staff/car-detail.jsp");

        } catch (Exception e) {
            logger.error("Error loading car detail", e);
            forwardToError(request, response,
                    "Không thể tải thông tin xe. Vui lòng thử lại sau!");
        }
    }

    private Integer getCarId(HttpServletRequest request) {
        String param = request.getParameter("id");
        if (param == null || param.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid car ID: {}", param);
            return null;
        }
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response,
                                   String path, String message) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("error", message);
        logger.warn("Redirecting with error: {}", message);
        response.sendRedirect(request.getContextPath() + path);
    }

    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}