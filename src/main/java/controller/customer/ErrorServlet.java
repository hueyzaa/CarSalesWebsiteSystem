package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
        String message = exception != null ? exception.getMessage() : "Đã xảy ra lỗi không xác định!";
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/Customer/error.jsp").forward(request, response);
    }
}