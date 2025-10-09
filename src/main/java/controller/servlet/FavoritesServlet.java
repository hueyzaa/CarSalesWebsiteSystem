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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/favorites")
public class FavoritesServlet extends HttpServlet {
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // For now, just show available cars as favorites
        // In a real application, you would have a separate Favorites table
        List<Car> favoriteCars = new ArrayList<>();

        request.setAttribute("favoriteCars", favoriteCars);
        request.setAttribute("message", "Tính năng xe yêu thích đang được phát triển!");

        request.getRequestDispatcher("/WEB-INF/views/favorites.jsp").forward(request, response);
    }
}