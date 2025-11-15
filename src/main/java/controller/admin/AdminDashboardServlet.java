package controller.admin;

import dao.AdminDashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        // Kiểm tra nếu là AJAX request lấy thống kê JSON
        String ajax = request.getParameter("ajax");
        if ("stats".equals(ajax)) {
            // Lấy số liệu từ DAO (giả sử bạn có AdminDashboardDAO)
            // Lấy số liệu từ DAO
            AdminDashboardDAO dao = new AdminDashboardDAO();
            int carCount = dao.getCarCount();
            int brandCount = dao.getBrandCount();
            int staffCount = dao.getStaffCount();
            int orderCount = dao.getOrderCount();

            // Trả JSON
            response.setContentType("application/json;charset=UTF-8");
            String json = String.format(
                    "{ \"carCount\": %d, \"brandCount\": %d, \"staffCount\": %d, \"orderCount\": %d }",
                    carCount, brandCount, staffCount, orderCount
            );
            response.getWriter().print(json);

            return; // Kết thúc xử lý AJAX
        }

        // Nếu không phải AJAX, forward bình thường tới JSP
        String section = request.getParameter("section");
        if (section == null) section = "overview"; // default section

        request.setAttribute("activeSection", section);
        request.getRequestDispatcher("/WEB-INF/views/Admin/dashboard.jsp")
                .forward(request, response);
    }
}

