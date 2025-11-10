package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/payment-result")
public class PaymentResultServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PaymentResultServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean success = "true".equals(request.getParameter("success"));
        String orderId = request.getParameter("orderId");

        logger.info("Payment result: success={}, orderId={}", success, orderId);

        request.getRequestDispatcher("/WEB-INF/views/Customer/payment-result.jsp")
                .forward(request, response);
    }
}