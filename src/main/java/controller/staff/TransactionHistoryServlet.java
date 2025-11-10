package controller.staff;

import dao.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import model.Transaction;

@WebServlet("/staff/transactions")
public class TransactionHistoryServlet extends HttpServlet {
    private final TransactionDAO dao = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("active", "transactions"); // dùng để highlight menu nếu cần

        TransactionDAO.TxnQuery q = new TransactionDAO.TxnQuery();
        q.status  = p(req, "status");
        q.type    = p(req, "type");
        q.keyword = p(req, "q");
        q.sort    = p(req, "sort");
        q.from    = toDate(p(req,"from"));
        q.to      = toDate(p(req,"to"));
        q.page    = toInt(p(req,"page"), 1);
        q.size    = toInt(p(req,"size"), 10);

        List<Transaction> txns = dao.find(q);
        int total = dao.count(q);
        int totalPages = (int) Math.ceil((double) total / Math.max(q.size, 1));

        req.setAttribute("txns", txns);
        req.setAttribute("total", total);
        req.setAttribute("page", q.page);
        req.setAttribute("size", q.size);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("q", q);
        req.setAttribute("pageTitle", "Transaction History");
        // Nếu bạn dùng layout staff mới mình đã gửi:
        req.setAttribute("view", "/WEB-INF/views/Staff/transactions.jsp");
        req.getRequestDispatcher("/WEB-INF/views/Staff/transactions.jsp").forward(req, resp);
    }

    private static String p(HttpServletRequest r, String k){ String v=r.getParameter(k); return (v==null||v.isBlank())?null:v.trim(); }
    private static Date toDate(String s){ try{ return (s==null||s.isBlank())?null:Date.valueOf(s);}catch(Exception e){return null;} }
    private static int toInt(String s, int d){ try{ return (s==null||s.isBlank())?d:Integer.parseInt(s);}catch(Exception e){return d;} }
}