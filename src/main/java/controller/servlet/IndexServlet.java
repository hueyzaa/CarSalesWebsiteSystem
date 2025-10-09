package controller.servlet;

import dao.CarDAO;
import model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet({"/", "/index"})
public class IndexServlet extends HttpServlet {
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Load available cars for display
        List<Car> cars = carDAO.getAvailableCars();
        request.setAttribute("cars", cars);

        // If logged in, forward to home page
        if (session != null && session.getAttribute("user") != null) {
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
        } else {
            // If not logged in, show index page
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }
}