package controller.staff;

import dao.CarDAO;
import exception.DatabaseException;
import model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@WebServlet("/staff/car-detail")
public class CarDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarDetailServlet.class);
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get car ID from parameter
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.warn("Car ID parameter is missing");
                request.getSession().setAttribute("error", "Không tìm thấy thông tin xe!");
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            int carId = Integer.parseInt(idParam);
            logger.info("Loading car detail for car ID: {}", carId);

            // Get car details
            Car car = carDAO.getCarById(carId);

            if (car == null) {
                logger.warn("Car not found with ID: {}", carId);
                request.getSession().setAttribute("error", "Xe không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            // Set attributes
            request.setAttribute("car", car);

            // Forward to car detail JSP
            request.getRequestDispatcher("/WEB-INF/views/Staff/car-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid car ID format", e);
            request.getSession().setAttribute("error", "ID xe không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/cars");

        } catch (DatabaseException e) {
            logger.error("Database error loading car detail", e);
            request.setAttribute("error", "Không thể tải thông tin xe!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading car detail", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
