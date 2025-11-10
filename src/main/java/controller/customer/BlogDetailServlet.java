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
 * BlogDetailServlet - Display detailed blog post
 * Accessible by everyone (Guest/Customer/Staff/Admin)
 */
@WebServlet("/blog-detail")
public class BlogDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BlogDetailServlet.class);
    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        blogDAO = new BlogDAO();
        logger.info("BlogDetailServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.warn("Blog ID parameter missing");
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            int blogId = Integer.parseInt(idParam);
            logger.info("Loading blog detail for ID: {}", blogId);

            Blog blog = blogDAO.getBlogById(blogId);

            if (blog == null) {
                logger.warn("Blog not found: {}", blogId);
                handleError(request, response, "Không tìm thấy bài viết.");
                return;
            }

            // Get related blogs
            List<Blog> relatedBlogs = getRelatedBlogs(blogId);

            request.setAttribute("blog", blog);
            request.setAttribute("relatedBlogs", relatedBlogs);
            request.getRequestDispatcher("/WEB-INF/views/Customer/blog-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid blog ID format", e);
            response.sendRedirect(request.getContextPath() + "/blog");
        } catch (RuntimeException e) {
            logger.error("Database error loading blog detail", e);
            handleError(request, response, "Không thể tải bài viết.");
        } catch (Exception e) {
            logger.error("Unexpected error loading blog detail", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    /**
     * Get related blogs (excluding current blog)
     */
    private List<Blog> getRelatedBlogs(int currentBlogId) {
        List<Blog> relatedBlogs = blogDAO.getRecentBlogs(4);
        relatedBlogs.removeIf(b -> b.getBlogId() == currentBlogId);

        if (relatedBlogs.size() > 3) {
            relatedBlogs = relatedBlogs.subList(0, 3);
        }

        logger.debug("Loaded {} related blogs", relatedBlogs.size());
        return relatedBlogs;
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
        logger.info("BlogDetailServlet destroyed");
    }
}