package controller.customer;

import dao.BlogDAO;
import model.Blog;
import exception.DatabaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BlogServlet.class);
    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        blogDAO = new BlogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            logger.info("Loading blog page");

            // Get all blogs
            List<Blog> blogs = blogDAO.getAllBlogs();

            logger.info("Retrieved {} blogs", blogs != null ? blogs.size() : 0);

            request.setAttribute("blogs", blogs);
            request.getRequestDispatcher("/WEB-INF/views/blog.jsp").forward(request, response);

        } catch (DatabaseException e) {
            logger.error("Database error loading blogs", e);
            request.setAttribute("error", "Không thể tải tin tức.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading blogs", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}