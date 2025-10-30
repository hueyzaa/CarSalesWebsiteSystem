package model;

import java.util.Date;

public class Blog {
    private int blogId;
    private String title;
    private String content;
    private int authorId;
    private Date createdAt;
    private String imageUrl;  // NEW: Thêm trường hình ảnh

    // Additional fields for display
    private String authorName;

    // Constructors
    public Blog() {
    }

    public Blog(int blogId, String title, String content, int authorId, Date createdAt) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
    }

    public Blog(int blogId, String title, String content, int authorId, Date createdAt, String imageUrl) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    // Utility methods
    public String getExcerpt(int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }

    public String getShortTitle(int maxLength) {
        if (title == null || title.length() <= maxLength) {
            return title;
        }
        return title.substring(0, maxLength) + "...";
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}