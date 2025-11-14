package controller.staff;

import ch.qos.logback.classic.Logger;
import dao.TransactionDAO;
import dto.TransactionCustomerHistory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff/transactions")
public class TransactionHistoryServlet extends HttpServlet {
    private final TransactionDAO transactionDAO = new TransactionDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<TransactionCustomerHistory> list = transactionDAO.getTransactionCustomerHistory();
            request.setAttribute("transactions", list);
            request.getRequestDispatcher("/WEB-INF/views/Staff/transaction.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Không thể tải danh sách khuyến mãi. Vui lòng thử lại!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}