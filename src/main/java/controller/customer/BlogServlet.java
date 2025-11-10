package controller.customer;

import dao.BlogDAO;
import model.Blog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * BlogServlet - Display blog/news listings
 * Accessible by everyone (Guest/Customer/Staff/Admin)
 */
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BlogServlet.class);
    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        blogDAO = new BlogDAO();
        logger.info("BlogServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            logger.info("Loading blog page");

            List<Blog> blogs = blogDAO.getAllBlogs();

            logger.info("Retrieved {} blogs", blogs != null ? blogs.size() : 0);

            request.setAttribute("blogs", blogs);
            request.getRequestDispatcher("/WEB-INF/views/Customer/blog.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error loading blogs", e);
            handleError(request, response, "Không thể tải tin tức.");
        } catch (Exception e) {
            logger.error("Unexpected error loading blogs", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    /**
     * Handle error and forward to error page
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/Customer/error.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("BlogServlet destroyed");
    }
}