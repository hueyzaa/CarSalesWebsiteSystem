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

        logger.info("========================================");
        logger.info("PAYMENT RESULT PAGE ACCESSED");
        logger.info("Success param: {}", request.getParameter("success"));
        logger.info("Order ID param: {}", request.getParameter("orderId"));
        logger.info("========================================");

        // Forward to JSP page
        request.getRequestDispatcher("/WEB-INF/views/payment-result.jsp").forward(request, response);
    }
}