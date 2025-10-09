package controller.servlet;

import dao.CarDAO;
import model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/car-detail")
public class CarDetailServlet extends HttpServlet {
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String carIdStr = request.getParameter("id");
        try {
            int carId = Integer.parseInt(carIdStr);
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                request.setAttribute("error", "Xe không tồn tại!");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }
            List<String> images = carDAO.getCarImages(carId);
            request.setAttribute("car", car);
            request.setAttribute("images", images);
            request.getRequestDispatcher("/WEB-INF/views/car-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID xe không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}