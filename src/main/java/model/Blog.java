package model;

import java.io.Serializable;
import java.util.Date;

public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;

    private int blogId;
    private String title;
    private String content;
    private int authorId;
    private Date createdAt;
    private String imageUrl;
    private String authorName;

    public Blog() {}

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
}