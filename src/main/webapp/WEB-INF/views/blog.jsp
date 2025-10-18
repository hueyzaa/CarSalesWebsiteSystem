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
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
            color: #e0e0e0;
        }

        /* Hero Section */
        .hero-blog {
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
            url('https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?auto=format&fit=crop&w=1500&q=80') center/cover;
            padding: 100px 0;
            margin-bottom: 50px;
            text-align: center;
            color: white;
            box-shadow: inset 0 0 100px rgba(0,0,0,0.5);
        }

        .hero-blog h1 {
            font-size: 3rem;
            font-weight: 700;
            margin-bottom: 20px;
            color: #ffd700;
            text-shadow: 3px 3px 6px rgba(0,0,0,0.8);
        }

        .hero-blog p {
            font-size: 1.3rem;
            color: #e0e0e0;
        }

        /* Featured Post */
        .featured-post {
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border: 2px solid #ffd700;
            border-radius: 20px;
            overflow: hidden;
            margin-bottom: 50px;
            box-shadow: 0 10px 40px rgba(255, 215, 0, 0.2);
        }

        .featured-post-content {
            display: flex;
            gap: 30px;
            align-items: center;
        }

        .featured-image {
            flex: 0 0 450px;
            height: 350px;
            background: linear-gradient(135deg, #2a2a2a 0%, #1a1a1a 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .featured-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .featured-image i {
            font-size: 8rem;
            color: #333;
        }

        .featured-content {
            flex: 1;
            padding: 40px;
        }

        .featured-badge {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 8px 20px;
            border-radius: 25px;
            font-weight: 700;
            font-size: 0.85rem;
            display: inline-block;
            margin-bottom: 15px;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.4);
        }

        .featured-content h2 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
            font-size: 2rem;
        }

        .featured-content p {
            color: #b0b0b0;
            line-height: 1.8;
            margin-bottom: 20px;
            font-size: 1.05rem;
        }

        .blog-meta {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #333;
        }
        .meta-item {
            display: flex;
            align-items: center;
            gap: 8px;
            color: #888;
            font-size: 0.9rem;
        }

        .meta-item i {
            color: #ffd700;
        }

        /* Blog Cards */
        .blog-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
            transition: all 0.3s;
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .blog-card:hover {
            transform: translateY(-10px);
            border-color: #ffd700;
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.3);
        }

        .blog-image {
            width: 100%;
            height: 250px;
            background: linear-gradient(135deg, #2a2a2a 0%, #1a1a1a 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #555;
            font-size: 4rem;
            overflow: hidden;
        }

        .blog-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s;
        }

        .blog-card:hover .blog-image img {
            transform: scale(1.1);
        }

        .blog-body {
            padding: 25px;
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .blog-title {
            color: #f8f9fa;
            font-size: 1.4rem;
            font-weight: 700;
            margin-bottom: 15px;
            line-height: 1.4;
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
            line-height: 1.7;
            margin-bottom: 20px;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
            flex: 1;
        }

        .btn-read-more {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 10px 20px;
            font-size: 0.9rem;
            border-radius: 25px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            text-align: center;
            margin-top: auto;
            white-space: nowrap;
        }

        .btn-read-more:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        /* No Blogs Message */
        .no-blogs {
            text-align: center;
            padding: 100px 20px;
            background: #1a1a1a;
            border-radius: 20px;
            border: 1px solid #333;
        }

        .no-blogs i {
            font-size: 6rem;
            color: #444;
            margin-bottom: 30px;
        }

        .no-blogs h3 {
            color: #888;
            margin-bottom: 15px;
            font-size: 1.8rem;
        }

        .no-blogs p {
            color: #666;
            font-size: 1.1rem;
            margin-bottom: 30px;
        }

        /* Category Tags */
        .category-tag {
            background: rgba(255, 215, 0, 0.1);
            border: 1px solid rgba(255, 215, 0, 0.3);
            color: #ffd700;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            display: inline-block;
            margin-bottom: 10px;
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
                flex: 1;
                width: 100%;
                height: 300px;
            }

            .hero-blog h1 {
                font-size: 2.2rem;
            }
        }

        @media (max-width: 576px) {
            .hero-blog h1 {
                font-size: 1.8rem;
            }

            .featured-content h2 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<!-- Hero Section -->
<div class="hero-blog">
    <div class="container">
        <h1><i class="fas fa-newspaper"></i> Tin Tức & Blog</h1>
        <p>Cập nhật thông tin mới nhất về xe hơi và công nghệ ô tô</p>
    </div>
</div>

<div class="container my-5">
    <!-- Featured Post (First Blog) -->
    <c:if test="${not empty blogs && blogs.size() > 0}">
        <div class="featured-post">
            <div class="featured-post-content">
                <div class="featured-image">
                    <i class="fas fa-newspaper"></i>
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
                        <span class="meta-item">
                            <i class="far fa-clock"></i>
                            5 phút đọc
                        </span>
                    </div>
                    <p>
                        <c:choose>
                            <c:when test="${blogs[0].content.length() > 250}">
                                ${blogs[0].content.substring(0, 250)}...
                            </c:when>
                            <c:otherwise>
                                ${blogs[0].content}
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <a href="${pageContext.request.contextPath}/blog-detail?id=${blogs[0].blogId}" class="btn-read-more">
                        <i class="fas fa-arrow-right"></i> Đọc Tiếp
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
                                <i class="fas fa-file-alt"></i>
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
                                        <c:when test="${blog.content.length() > 150}">
                                            ${blog.content.substring(0, 150)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${blog.content}
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <a href="${pageContext.request.contextPath}/blog-detail?id=${blog.blogId}" class="btn-read-more">
                                    <i class="fas fa-arrow-right"></i> Đọc Tiếp
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