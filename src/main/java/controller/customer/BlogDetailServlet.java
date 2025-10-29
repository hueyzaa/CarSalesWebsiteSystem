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

@WebServlet("/blog-detail")
public class BlogDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BlogDetailServlet.class);
    private BlogDAO blogDAO;

    @Override
    public void init() throws ServletException {
        blogDAO = new BlogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get blog ID from parameter
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.warn("Blog ID parameter is missing");
                response.sendRedirect(request.getContextPath() + "/blog");
                return;
            }

            int blogId = Integer.parseInt(idParam);
            logger.info("Loading blog detail for ID: {}", blogId);

            // Get blog by ID
            Blog blog = blogDAO.getBlogById(blogId);

            if (blog == null) {
                logger.warn("Blog not found: {}", blogId);
                request.setAttribute("error", "Không tìm thấy bài viết.");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            // Get related blogs (same author or recent)
            List<Blog> relatedBlogs = blogDAO.getRecentBlogs(4);
            // Remove current blog from related list
            relatedBlogs.removeIf(b -> b.getBlogId() == blogId);
            if (relatedBlogs.size() > 3) {
                relatedBlogs = relatedBlogs.subList(0, 3);
            }

            request.setAttribute("blog", blog);
            request.setAttribute("relatedBlogs", relatedBlogs);
            request.getRequestDispatcher("/WEB-INF/views/blog-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid blog ID format", e);
            response.sendRedirect(request.getContextPath() + "/blog");

        } catch (RuntimeException e) {
            logger.error("Database error loading blog detail", e);
            request.setAttribute("error", "Không thể tải bài viết.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading blog detail", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}