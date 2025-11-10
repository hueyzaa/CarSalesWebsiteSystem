<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${blog.title} - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #0f0f0f;
            color: #e0e0e0;
            min-height: 100vh;
        }

        /* Page Header */
        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 2rem 0;
            margin-bottom: 2rem;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #f8f9fa;
            font-weight: 700;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .page-header .subtitle {
            color: #888;
            font-size: 1rem;
            margin: 0;
        }

        /* Article Container */
        .article-container {
            max-width: 800px;
            margin: 0 auto 40px;
            padding: 0 20px;
        }

        /* Back Button */
        .back-button {
            background: transparent;
            color: #ffd700;
            padding: 10px 0;
            text-decoration: none;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 25px;
            transition: all 0.3s;
        }

        .back-button:hover {
            color: #ffed4e;
            gap: 12px;
        }

        /* Article Header */
        .article-title {
            color: #ffd700;
            font-size: 2.2rem;
            font-weight: 700;
            margin-bottom: 20px;
            line-height: 1.3;
        }

        .article-meta {
            display: flex;
            gap: 20px;
            color: #888;
            font-size: 0.9rem;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #333;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .meta-item i {
            color: #ffd700;
            font-size: 0.85rem;
        }

        /* Featured Image */
        .featured-image {
            margin-bottom: 40px;
            border-radius: 10px;
            overflow: hidden;
            background: #1a1a1a;
        }

        .featured-image img {
            width: 100%;
            height: auto;
            display: block;
        }

        /* Article Content */
        .article-content {
            color: #d0d0d0;
            font-size: 1.05rem;
            line-height: 1.8;
            margin-bottom: 40px;
        }

        .article-content p {
            margin-bottom: 20px;
        }

        .article-content h2,
        .article-content h3 {
            color: #ffd700;
            margin-top: 35px;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .article-content h2 {
            font-size: 1.6rem;
        }

        .article-content h3 {
            font-size: 1.3rem;
        }

        .article-content ul,
        .article-content ol {
            margin-bottom: 20px;
            padding-left: 25px;
        }

        .article-content li {
            margin-bottom: 8px;
        }

        /* Related Posts */
        .related-section {
            margin-top: 60px;
            padding-top: 40px;
            border-top: 2px solid #333;
        }

        .section-title {
            color: #ffd700;
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 30px;
        }

        .related-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 10px;
            overflow: hidden;
            transition: all 0.3s;
            height: 100%;
        }

        .related-card:hover {
            transform: translateY(-3px);
            border-color: #ffd700;
        }

        .related-image {
            width: 100%;
            height: 180px;
            background: #252525;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .related-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .related-image i {
            font-size: 2.5rem;
            color: #444;
        }

        .related-body {
            padding: 20px;
        }

        .related-title {
            font-size: 1rem;
            margin-bottom: 10px;
        }

        .related-title a {
            color: #f8f9fa;
            text-decoration: none;
            font-weight: 500;
        }

        .related-title a:hover {
            color: #ffd700;
        }

        .related-date {
            color: #888;
            font-size: 0.85rem;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .page-header h1 {
                font-size: 1.5rem;
            }

            .page-header .subtitle {
                font-size: 0.9rem;
            }

            .article-title {
                font-size: 1.6rem;
            }

            .article-meta {
                flex-direction: column;
                gap: 10px;
            }

            .article-content {
                font-size: 1rem;
            }

            .related-image {
                height: 150px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-newspaper"></i> Tin Tức & Blog</h1>
        <p class="subtitle">Cập nhật thông tin mới nhất về xe hơi và công nghệ ô tô</p>
    </div>
</div>

<div class="article-container">
    <!-- Back Button -->
    <a href="${pageContext.request.contextPath}/blog" class="back-button">
        <i class="fas fa-arrow-left"></i> Quay lại danh sách
    </a>

    <!-- Article Header -->
    <h1 class="article-title">${blog.title}</h1>

    <div class="article-meta">
        <div class="meta-item">
            <i class="far fa-calendar"></i>
            <span><fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy"/></span>
        </div>
        <div class="meta-item">
            <i class="far fa-user"></i>
            <span>
                <c:choose>
                    <c:when test="${not empty blog.authorName}">
                        ${blog.authorName}
                    </c:when>
                    <c:otherwise>
                        Admin
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <!-- Featured Image -->
    <c:if test="${not empty blog.imageUrl}">
        <div class="featured-image">
            <img src="${blog.imageUrl}" alt="${blog.title}">
        </div>
    </c:if>

    <!-- Article Content -->
    <div class="article-content">
        ${blog.content}
    </div>

    <!-- Related Posts -->
    <c:if test="${not empty relatedBlogs}">
        <div class="related-section">
            <h2 class="section-title">Bài viết liên quan</h2>
            <div class="row g-4">
                <c:forEach var="relatedBlog" items="${relatedBlogs}">
                    <div class="col-lg-4 col-md-6">
                        <div class="related-card">
                            <div class="related-image">
                                <c:choose>
                                    <c:when test="${not empty relatedBlog.imageUrl}">
                                        <img src="${relatedBlog.imageUrl}" alt="${relatedBlog.title}">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-file-alt"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="related-body">
                                <h3 class="related-title">
                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${relatedBlog.blogId}">
                                            ${relatedBlog.title}
                                    </a>
                                </h3>
                                <div class="related-date">
                                    <fmt:formatDate value="${relatedBlog.createdAt}" pattern="dd/MM/yyyy"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
