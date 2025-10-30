<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tin Tức & Blog - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0a0a0a;
            color: #e0e0e0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        /* Page Header - Simple & Clean */
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

        .content-section {
            flex: 1;
            padding-bottom: 3rem;
        }

        /* Featured Post */
        .featured-post {
            background: #1a1a1a;
            border: 2px solid #ffd700;
            border-radius: 12px;
            overflow: hidden;
            margin-bottom: 2rem;
            box-shadow: 0 8px 25px rgba(255, 215, 0, 0.15);
        }

        .featured-post-content {
            display: flex;
            gap: 0;
            align-items: stretch;
        }

        .featured-image {
            flex: 0 0 45%;
            min-height: 400px;
            background: #0a0a0a;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            position: relative;
        }

        .featured-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            position: absolute;
            top: 0;
            left: 0;
        }

        .featured-image i {
            font-size: 5rem;
            color: #333;
            z-index: 1;
        }

        .featured-content {
            flex: 1;
            padding: 2.5rem;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .featured-badge {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 0.5rem 1.25rem;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.85rem;
            display: inline-block;
            margin-bottom: 1.25rem;
            width: fit-content;
        }

        .featured-content h2 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 1.25rem;
            font-size: 1.6rem;
            line-height: 1.3;
        }

        .featured-content p {
            color: #b0b0b0;
            line-height: 1.6;
            margin-bottom: 1.5rem;
            font-size: 0.95rem;
        }

        .blog-meta {
            display: flex;
            flex-wrap: wrap;
            gap: 1.25rem;
            margin-bottom: 1.25rem;
            padding-bottom: 1.25rem;
            border-bottom: 1px solid #2a2a2a;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 6px;
            color: #888;
            font-size: 0.9rem;
            white-space: nowrap;
        }

        .meta-item i {
            color: #ffd700;
            flex-shrink: 0;
        }

        /* Blog Cards */
        .blog-card {
            background: #1a1a1a;
            border: 1px solid #2a2a2a;
            border-radius: 12px;
            overflow: hidden;
            transition: all 0.3s;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .blog-card:hover {
            transform: translateY(-8px);
            border-color: #ffd700;
            box-shadow: 0 12px 30px rgba(255, 215, 0, 0.2);
        }

        .blog-image {
            width: 100%;
            height: 200px;
            background: #0a0a0a;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #444;
            font-size: 3rem;
            overflow: hidden;
            position: relative;
        }

        .blog-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.4s;
            position: absolute;
            top: 0;
            left: 0;
        }

        .blog-image i {
            z-index: 1;
        }

        .blog-card:hover .blog-image img {
            transform: scale(1.1);
        }

        .blog-body {
            padding: 1.5rem;
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .category-tag {
            background: #2a2a2a;
            color: #ffd700;
            padding: 0.4rem 0.9rem;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 600;
            display: inline-block;
            margin-bottom: 1rem;
            border: 1px solid #333;
        }

        .blog-title {
            color: #f8f9fa;
            font-size: 1.2rem;
            font-weight: 700;
            margin-bottom: 1rem;
            line-height: 1.4;
            min-height: 60px;
        }

        .blog-title a {
            color: #f8f9fa;
            text-decoration: none;
            transition: all 0.3s;
        }

        .blog-title a:hover {
            color: #ffd700;
        }

        .blog-excerpt {
            color: #b0b0b0;
            line-height: 1.6;
            margin-bottom: 1.25rem;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
            flex: 1;
            font-size: 0.9rem;
        }

        .btn-read-more {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 0.65rem 1.5rem;
            border-radius: 20px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            transition: all 0.3s;
            align-self: flex-start;
            font-size: 0.9rem;
        }

        .btn-read-more:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-read-more i {
            transition: transform 0.3s;
        }

        .btn-read-more:hover i {
            transform: translateX(3px);
        }

        /* Empty State */
        .no-blogs {
            text-align: center;
            padding: 5rem 2rem;
            background: #1a1a1a;
            border-radius: 12px;
            border: 2px solid #2a2a2a;
        }

        .no-blogs i {
            font-size: 4rem;
            color: #444;
            margin-bottom: 1.5rem;
        }

        .no-blogs h3 {
            color: #888;
            margin-bottom: 1rem;
            font-size: 1.5rem;
        }

        .no-blogs p {
            color: #666;
            margin-bottom: 1.5rem;
        }

        footer {
            margin-top: auto;
        }

        /* Responsive */
        @media (max-width: 992px) {
            .featured-post-content {
                flex-direction: column;
            }

            .featured-image {
                min-height: 300px;
                flex: none;
            }
        }

        @media (max-width: 768px) {
            .page-header h1 {
                font-size: 1.5rem;
            }

            .page-header .subtitle {
                font-size: 0.9rem;
            }

            .featured-content h2 {
                font-size: 1.3rem;
            }

            .featured-content {
                padding: 1.5rem;
            }

            .blog-meta {
                gap: 1rem;
            }

            .meta-item {
                font-size: 0.85rem;
            }

            .blog-title {
                font-size: 1.1rem;
                min-height: auto;
            }

            .blog-image {
                height: 180px;
            }
        }

        @media (max-width: 576px) {
            .featured-image {
                min-height: 250px;
            }

            .featured-content h2 {
                font-size: 1.2rem;
            }

            .featured-content {
                padding: 1.25rem;
            }

            .blog-body {
                padding: 1.25rem;
            }

            .blog-meta {
                flex-direction: column;
                gap: 0.75rem;
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

<div class="container content-section">
    <!-- Featured Post (First Blog) -->
    <c:if test="${not empty blogs && blogs.size() > 0}">
        <div class="featured-post">
            <div class="featured-post-content">
                <div class="featured-image">
                    <c:choose>
                        <c:when test="${not empty blogs[0].imageUrl}">
                            <img src="${blogs[0].imageUrl}"
                                 alt="${blogs[0].title}"
                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                            <i class="fas fa-newspaper" style="display:none;"></i>
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-newspaper"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="featured-content">
                    <span class="featured-badge">
                        <i class="fas fa-star"></i> BÀI VIẾT NỔI BẬT
                    </span>
                    <h2>${blogs[0].title}</h2>
                    <div class="blog-meta">
                        <span class="meta-item">
                            <i class="far fa-calendar"></i>
                            <fmt:formatDate value="${blogs[0].createdAt}" pattern="dd/MM/yyyy"/>
                        </span>
                        <span class="meta-item">
                            <i class="far fa-user"></i>
                            <c:choose>
                                <c:when test="${not empty blogs[0].authorName}">
                                    ${blogs[0].authorName}
                                </c:when>
                                <c:otherwise>
                                    Admin
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <p>
                        <c:choose>
                            <c:when test="${blogs[0].content.length() > 200}">
                                ${blogs[0].content.substring(0, 200)}...
                            </c:when>
                            <c:otherwise>
                                ${blogs[0].content}
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <a href="${pageContext.request.contextPath}/blog-detail?id=${blogs[0].blogId}" class="btn-read-more">
                        Đọc Tiếp <i class="fas fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Blog Grid -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty blogs && blogs.size() > 1}">
                <c:forEach var="blog" items="${blogs}" begin="1">
                    <div class="col-lg-4 col-md-6">
                        <div class="blog-card">
                            <div class="blog-image">
                                <c:choose>
                                    <c:when test="${not empty blog.imageUrl}">
                                        <img src="${blog.imageUrl}"
                                             alt="${blog.title}"
                                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        <i class="fas fa-file-alt" style="display:none;"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-file-alt"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="blog-body">
                                <span class="category-tag">
                                    <i class="fas fa-tag"></i> Tin Tức
                                </span>

                                <div class="blog-meta mb-3">
                                    <span class="meta-item">
                                        <i class="far fa-calendar"></i>
                                        <fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy"/>
                                    </span>
                                    <span class="meta-item">
                                        <i class="far fa-user"></i>
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

                                <h3 class="blog-title">
                                    <a href="${pageContext.request.contextPath}/blog-detail?id=${blog.blogId}">
                                            ${blog.title}
                                    </a>
                                </h3>

                                <p class="blog-excerpt">
                                    <c:choose>
                                        <c:when test="${blog.content.length() > 120}">
                                            ${blog.content.substring(0, 120)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${blog.content}
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <a href="${pageContext.request.contextPath}/blog-detail?id=${blog.blogId}" class="btn-read-more">
                                    Đọc Tiếp <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${empty blogs}">
                <div class="col-12">
                    <div class="no-blogs">
                        <i class="fas fa-newspaper"></i>
                        <h3>Chưa Có Bài Viết Nào</h3>
                        <p>Các bài viết mới sẽ được cập nhật sớm. Vui lòng quay lại sau!</p>
                        <a href="${pageContext.request.contextPath}/cars" class="btn-read-more">
                            <i class="fas fa-car"></i> Xem Danh Sách Xe
                        </a>
                    </div>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>

<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
