package dao;

import model.Blog;
import util.DBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDAO {
    private static final Logger logger = LoggerFactory.getLogger(BlogDAO.class);

    /**
     * Get all blogs from database
     */
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = new ArrayList<>();

        String sql = "SELECT b.blog_id, b.title, b.content, b.author_id, b.created_at, b.image_url, " +
                "u.email as author_email " +
                "FROM Blog b " +
                "LEFT JOIN AppUsers u ON b.author_id = u.user_id " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Blog blog = new Blog();
                blog.setBlogId(rs.getInt("blog_id"));
                blog.setTitle(rs.getString("title"));
                blog.setContent(rs.getString("content"));
                blog.setAuthorId(rs.getInt("author_id"));
                blog.setCreatedAt(rs.getTimestamp("created_at"));
                blog.setImageUrl(rs.getString("image_url"));
                blog.setAuthorName(rs.getString("author_email"));

                blogs.add(blog);
            }

            logger.info("Retrieved {} blogs from database", blogs.size());
            return blogs;

        } catch (SQLException e) {
            logger.error("Error retrieving all blogs", e);
            throw new RuntimeException("Không thể lấy danh sách blog", e);
        }
    }

    /**
     * Get blog by ID
     */
    public Blog getBlogById(int blogId) {
        String sql = "SELECT b.blog_id, b.title, b.content, b.author_id, b.created_at, b.image_url, " +
                "u.email as author_email " +
                "FROM Blog b " +
                "LEFT JOIN AppUsers u ON b.author_id = u.user_id " +
                "WHERE b.blog_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, blogId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Blog blog = new Blog();
                    blog.setBlogId(rs.getInt("blog_id"));
                    blog.setTitle(rs.getString("title"));
                    blog.setContent(rs.getString("content"));
                    blog.setAuthorId(rs.getInt("author_id"));
                    blog.setCreatedAt(rs.getTimestamp("created_at"));
                    blog.setImageUrl(rs.getString("image_url"));
                    blog.setAuthorName(rs.getString("author_email"));

                    logger.info("Retrieved blog: {}", blogId);
                    return blog;
                }
            }

            logger.warn("Blog not found: {}", blogId);
            return null;

        } catch (SQLException e) {
            logger.error("Error retrieving blog by ID: {}", blogId, e);
            throw new RuntimeException("Không thể lấy thông tin blog", e);
        }
    }

    /**
     * Get recent blogs (limit)
     */
    public List<Blog> getRecentBlogs(int limit) {
        List<Blog> blogs = new ArrayList<>();

        String sql = "SELECT TOP (?) b.blog_id, b.title, b.content, b.author_id, b.created_at, b.image_url, " +
                "u.email as author_email " +
                "FROM Blog b " +
                "LEFT JOIN AppUsers u ON b.author_id = u.user_id " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = new Blog();
                    blog.setBlogId(rs.getInt("blog_id"));
                    blog.setTitle(rs.getString("title"));
                    blog.setContent(rs.getString("content"));
                    blog.setAuthorId(rs.getInt("author_id"));
                    blog.setCreatedAt(rs.getTimestamp("created_at"));
                    blog.setImageUrl(rs.getString("image_url"));
                    blog.setAuthorName(rs.getString("author_email"));

                    blogs.add(blog);
                }
            }

            logger.info("Retrieved {} recent blogs", blogs.size());
            return blogs;

        } catch (SQLException e) {
            logger.error("Error retrieving recent blogs", e);
            throw new RuntimeException("Không thể lấy danh sách blog mới nhất", e);
        }
    }

    /**
     * Get blogs by author
     */
    public List<Blog> getBlogsByAuthor(int authorId)  {
        List<Blog> blogs = new ArrayList<>();

        String sql = "SELECT b.blog_id, b.title, b.content, b.author_id, b.created_at, b.image_url, " +
                "u.email as author_email " +
                "FROM Blog b " +
                "LEFT JOIN AppUsers u ON b.author_id = u.user_id " +
                "WHERE b.author_id = ? " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, authorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = new Blog();
                    blog.setBlogId(rs.getInt("blog_id"));
                    blog.setTitle(rs.getString("title"));
                    blog.setContent(rs.getString("content"));
                    blog.setAuthorId(rs.getInt("author_id"));
                    blog.setCreatedAt(rs.getTimestamp("created_at"));
                    blog.setImageUrl(rs.getString("image_url"));
                    blog.setAuthorName(rs.getString("author_email"));

                    blogs.add(blog);
                }
            }

            logger.info("Retrieved {} blogs for author: {}", blogs.size(), authorId);
            return blogs;

        } catch (SQLException e) {
            logger.error("Error retrieving blogs by author: {}", authorId, e);
            throw new RuntimeException("Không thể lấy danh sách blog của tác giả", e);
        }
    }

    /**
     * Search blogs by title or content
     */
    public List<Blog> searchBlogs(String keyword){
        List<Blog> blogs = new ArrayList<>();

        String sql = "SELECT b.blog_id, b.title, b.content, b.author_id, b.created_at, b.image_url, " +
                "u.email as author_email " +
                "FROM Blog b " +
                "LEFT JOIN AppUsers u ON b.author_id = u.user_id " +
                "WHERE b.title LIKE ? OR b.content LIKE ? " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = new Blog();
                    blog.setBlogId(rs.getInt("blog_id"));
                    blog.setTitle(rs.getString("title"));
                    blog.setContent(rs.getString("content"));
                    blog.setAuthorId(rs.getInt("author_id"));
                    blog.setCreatedAt(rs.getTimestamp("created_at"));
                    blog.setImageUrl(rs.getString("image_url"));
                    blog.setAuthorName(rs.getString("author_email"));

                    blogs.add(blog);
                }
            }

            logger.info("Found {} blogs matching keyword: {}", blogs.size(), keyword);
            return blogs;

        } catch (SQLException e) {
            logger.error("Error searching blogs with keyword: {}", keyword, e);
            throw new RuntimeException("Không thể tìm kiếm blog", e);
        }
    }


    public int createBlog(Blog blog) {
        String sql = "INSERT INTO Blog (title, content, author_id, image_url) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setInt(3, blog.getAuthorId());
            stmt.setString(4, blog.getImageUrl());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Tạo blog thất bại");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int blogId = generatedKeys.getInt(1);
                    logger.info("Created blog with ID: {}", blogId);
                    return blogId;
                } else {
                    throw new RuntimeException("Tạo blog thất bại, không lấy được ID");
                }
            }

        } catch (SQLException e) {
            logger.error("Error creating blog", e);
            throw new RuntimeException("Không thể tạo blog", e);
        }
    }

    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE Blog SET title = ?, content = ?, image_url = ? WHERE blog_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setString(3, blog.getImageUrl());
            stmt.setInt(4, blog.getBlogId());

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Updated blog {}: {}", blog.getBlogId(), success);
            return success;

        } catch (SQLException e) {
            logger.error("Error updating blog: {}", blog.getBlogId(), e);
            throw new RuntimeException("Không thể cập nhật blog", e);
        }
    }

    public boolean deleteBlog(int blogId) {
        String sql = "DELETE FROM Blog WHERE blog_id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, blogId);

            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;

            logger.info("Deleted blog {}: {}", blogId, success);
            return success;

        } catch (SQLException e) {
            logger.error("Error deleting blog: {}", blogId, e);
            throw new RuntimeException("Không thể xóa blog", e);
        }
    }

    public int getBlogCount() {
        String sql = "SELECT COUNT(*) as total FROM Blog";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("total");
                logger.info("Total blogs count: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Error getting blog count", e);
            throw new RuntimeException("Không thể đếm số lượng blog", e);
        }
    }
}