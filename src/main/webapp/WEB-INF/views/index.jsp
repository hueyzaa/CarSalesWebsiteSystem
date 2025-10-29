<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Car Showroom - Trang Chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
        }

        /* Navbar */
        .navbar {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 15px 0;
            box-shadow: 0 4px 20px rgba(0,0,0,0.5);
            border-bottom: 1px solid #333;
        }

        .navbar-brand {
            font-size: 1.5rem;
            font-weight: bold;
            color: #f8f9fa !important;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .navbar-brand i {
            color: #ffd700;
        }

        /* Search Box */
        .search-navbar {
            position: relative;
            max-width: 600px;
            width: 100%;
        }

        .search-navbar input {
            padding: 10px 20px;
            border: 2px solid #555;
            border-radius: 25px;
            width: 100%;
            background: #3a3a3a;
            color: #f0f0f0;
            font-size: 0.95rem;
            font-weight: 400;
            transition: all 0.3s;
        }

        .search-navbar input::placeholder {
            color: #bbb;
            font-weight: 300;
        }

        .search-navbar input:focus {
            outline: none;
            border-color: #0d6efd;
            background: #2a2a2a;
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.25);
            color: #ffffff;
        }

        .cart-btn-nav {
            position: relative;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            border: none;
            border-radius: 25px;
            padding: 8px 20px;
            font-weight: 600;
            margin-left: 15px;
            transition: all 0.3s;
        }

        .cart-btn-nav:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
            color: #1a1a1a;
            text-decoration: none;
        }

        .cart-badge-nav {
            position: absolute;
            top: -8px;
            right: -8px;
            background: #dc3545;
            color: white;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 11px;
            font-weight: bold;
        }

        .nav-link {
            color: #e0e0e0 !important;
            margin: 0 10px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .nav-link:hover {
            color: #ffd700 !important;
            text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }

        .dropdown-menu {
            background: #2a2a2a;
            border: 1px solid #444;
        }

        .dropdown-item {
            color: #e0e0e0;
            transition: all 0.3s;
        }

        .dropdown-item:hover {
            background: #333;
            color: #ffd700;
        }

        .dropdown-divider {
            border-color: #444;
        }

        /* Hero Section */
        .hero-section {
            position: relative;
            height: 650px;
            background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)),
            url('https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?auto=format&fit=crop&w=1500&q=80') center/cover;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-align: center;
            margin-bottom: 50px;
            box-shadow: inset 0 0 100px rgba(0,0,0,0.5);
        }

        .hero-content h1 {
            font-size: 3.5rem;
            font-weight: 700;
            margin-bottom: 20px;
            text-shadow: 3px 3px 6px rgba(0,0,0,0.8);
            background: linear-gradient(135deg, #fff 0%, #ffd700 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .hero-content p {
            font-size: 1.3rem;
            margin-bottom: 30px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.8);
            color: #e0e0e0;
        }

        .btn-hero-primary {
            padding: 15px 40px;
            font-size: 1.1rem;
            border-radius: 50px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            border: none;
            transition: all 0.3s;
            font-weight: 600;
            margin: 0 10px;
        }

        .btn-hero-primary:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-hero-outline {
            padding: 15px 40px;
            font-size: 1.1rem;
            border-radius: 50px;
            background: transparent;
            color: #fff;
            border: 2px solid #fff;
            transition: all 0.3s;
            font-weight: 600;
            margin: 0 10px;
        }

        .btn-hero-outline:hover {
            background: rgba(255, 255, 255, 0.1);
            border-color: #ffd700;
            color: #ffd700;
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.2);
        }

        /* Car Cards - Updated to match reference image */
        .car-card {
            background: linear-gradient(145deg, #1f1f1f 0%, #1a1a1a 100%);
            border: 1px solid #2a2a2a;
            border-radius: 20px;
            overflow: hidden;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            height: 100%;
        }

        .car-card:hover {
            transform: translateY(-8px) scale(1.02);
            box-shadow: 0 20px 50px rgba(255, 215, 0, 0.25);
            border-color: rgba(255, 215, 0, 0.5);
        }

        .car-card img {
            height: 200px;
            object-fit: cover;
            transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
            background: linear-gradient(180deg, #2a2a2a 0%, #1a1a1a 100%);
        }

        .car-card:hover img {
            transform: scale(1.15);
        }

        .car-card .card-body {
            padding: 1.5rem 1.25rem;
            background: transparent;
        }

        .car-card .card-title {
            color: #ffffff;
            font-size: 1.15rem;
            font-weight: 700;
            margin-bottom: 0.75rem;
            line-height: 1.3;
        }

        .car-card .text-primary {
            color: #ffd700 !important;
        }

        .car-card .small {
            color: #ffd700 !important;
            font-size: 0.75rem !important;
            font-weight: 700;
            letter-spacing: 1.5px;
            margin-bottom: 0.5rem;
        }

        .car-card .btn-primary {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            border-radius: 10px;
            color: #000000;
            font-weight: 700;
            padding: 0.85rem;
            transition: all 0.3s ease;
        }

        .car-card .btn-primary:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.5);
            color: #000000;
            transform: translateY(-2px);
        }

        .car-card .card-text {
            font-size: 1.25rem !important;
            margin-bottom: 1rem !important;
        }

        .section-title {
            text-align: center;
            margin-bottom: 40px;
        }

        .section-title h2 {
            font-size: 2.5rem;
            font-weight: 700;
            color: #f8f9fa;
            margin-bottom: 10px;
        }

        .section-title p {
            color: #888;
            font-size: 1.1rem;
        }

        .section-title .fa-star {
            color: #ffd700;
        }

        .btn-outline-primary {
            border-color: #ffd700;
            color: #ffd700;
            font-weight: 600;
        }

        .btn-outline-primary:hover {
            background: #ffd700;
            border-color: #ffd700;
            color: #1a1a1a;
            box-shadow: 0 5px 20px rgba(255, 215, 0, 0.4);
        }

        /* Features Section */
        .features-section {
            background: #1a1a1a;
            padding: 60px 0;
            border-top: 1px solid #333;
            border-bottom: 1px solid #333;
        }

        .feature-card {
            background: #0f0f0f;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 30px;
            height: 100%;
            transition: all 0.3s;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            border-color: #ffd700;
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.2);
        }

        .feature-card i {
            font-size: 3rem;
            margin-bottom: 20px;
        }

        .feature-card h4 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .feature-card p {
            color: #888;
            margin: 0;
        }

        .feature-card.quality i {
            color: #ffd700;
        }

        .feature-card.support i {
            color: #4caf50;
        }

        .feature-card.price i {
            color: #ff9800;
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Navbar with Search & Cart -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid px-4">
        <!-- Brand - Sát bên trái -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <i class="fas fa-car"></i> Car Showroom
        </a>

        <!-- Menu items cạnh brand -->
        <ul class="navbar-nav me-auto mb-0 d-none d-lg-flex">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                    <i class="fas fa-car"></i> Xem Xe
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/promotions">
                    <i class="fas fa-gift"></i> Khuyến Mãi
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/blog">
                    <i class="fas fa-newspaper"></i> Tin Tức
                </a>
            </li>
        </ul>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Mobile Menu -->
            <ul class="navbar-nav d-lg-none mb-3">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                        <i class="fas fa-car"></i> Xem Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/promotions">
                        <i class="fas fa-gift"></i> Khuyến Mãi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/blog">
                        <i class="fas fa-newspaper"></i> Tin Tức
                    </a>
                </li>
            </ul>

            <!-- Search Box - Giữa -->
            <form action="${pageContext.request.contextPath}/cars" method="get" class="d-flex search-navbar mx-auto">
                <input type="text"
                       name="search"
                       class="form-control"
                       placeholder="Tìm kiếm xe...">
            </form>

            <!-- Right side - Cart & User -->
            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <!-- Cart Button -->
                <a href="${pageContext.request.contextPath}/cart" class="cart-btn-nav">
                    <i class="fas fa-shopping-cart"></i> Giỏ Hàng
                    <span class="cart-badge-nav" id="cartBadge">0</span>
                </a>

                <!-- User Menu - Sát bên phải -->
                <ul class="navbar-nav ms-3 mb-0">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.name}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/home">
                                        <i class="fas fa-home"></i> Trang Chủ
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="fas fa-user-circle"></i> Hồ Sơ
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                        <i class="fas fa-receipt"></i> Đơn Hàng
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/favorites">
                                        <i class="fas fa-heart"></i> Yêu Thích
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                        <i class="fas fa-sign-out-alt"></i> Đăng Xuất
                                    </a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-sign-in-alt"></i> Đăng Nhập
                                </a>
                            </li>
                            <li class="nav-item ms-2">
                                <a class="nav-link" href="${pageContext.request.contextPath}/register"
                                   style="background: linear-gradient(135deg, #c9a944 0%, #b89532 100%);
                                          color: #fff; padding: 8px 20px; border-radius: 25px; font-weight: 600;">
                                    <i class="fas fa-user-plus"></i> Đăng Ký
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</nav>

<script>
    // Load cart count from session
    document.addEventListener('DOMContentLoaded', function() {
        const cartCount = ${sessionScope.cartCount != null ? sessionScope.cartCount : 0};
        const badge = document.getElementById('cartBadge');
        if (badge) {
            if (cartCount > 0) {
                badge.textContent = cartCount;
            } else {
                badge.style.display = 'none';
            }
        }
    });
</script>

<!-- Hero Section -->
<div class="hero-section">
    <div class="hero-content">
        <h1>Khám Phá Thế Giới Xe Hơi Đẳng Cấp</h1>
        <p>Tận hưởng trải nghiệm lái xe tuyệt vời với những mẫu xe mới nhất</p>
        <div class="d-flex gap-3 justify-content-center flex-wrap">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-hero-primary">
                <i class="fas fa-search"></i> Khám Phá Ngay
            </a>
            <a href="${pageContext.request.contextPath}/promotions" class="btn btn-hero-outline">
                <i class="fas fa-gift"></i> Khuyến Mãi
            </a>
        </div>
    </div>
</div>

<!-- Featured Cars Section -->
<div class="container my-5 py-5">
    <div class="section-title">
        <h2>
            <i class="fas fa-star"></i> Xe Nổi Bật
        </h2>
        <p>Những mẫu xe được yêu thích nhất</p>
    </div>

    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty cars}">
                <c:forEach var="car" items="${cars}" begin="0" end="7">
                    <div class="col-lg-3 col-md-4 col-sm-6">
                        <div class="card car-card">
                            <c:set var="mainImage" value=""/>
                            <c:forEach var="img" items="${car.images}">
                                <c:if test="${img.mainImage}">
                                    <c:set var="mainImage" value="${img.imageURL}"/>
                                </c:if>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${not empty mainImage}">
                                    <img src="${mainImage}" class="card-img-top" alt="${car.name}">
                                </c:when>
                                <c:when test="${not empty car.imageUrl}">
                                    <img src="${car.imageUrl}" class="card-img-top" alt="${car.name}">
                                </c:when>
                                <c:otherwise>
                                    <img src="https://via.placeholder.com/300x200?text=No+Image" class="card-img-top" alt="${car.name}">
                                </c:otherwise>
                            </c:choose>

                            <div class="card-body">
                                <h6 class="text-primary text-uppercase small mb-2">
                                        ${car.brandName}
                                </h6>
                                <h5 class="card-title fw-bold">${car.name}</h5>
                                <p class="card-text text-primary fw-bold fs-5">
                                    <fmt:formatNumber value="${car.price}" pattern="#,##0" /> ₫
                                </p>
                                <a href="${pageContext.request.contextPath}/car-detail?id=${car.id}"
                                   class="btn btn-primary w-100">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12 text-center py-5">
                    <i class="fas fa-car-crash fa-5x text-muted mb-3"></i>
                    <h3 style="color: #888;">Hiện chưa có xe nào để hiển thị</h3>
                    <p style="color: #666;">Vui lòng quay lại sau</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="text-center mt-5">
        <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-primary btn-lg px-5">
            <i class="fas fa-th me-2"></i> Xem Tất Cả Xe
        </a>
    </div>
</div>

<!-- Features Section -->
<div class="features-section">
    <div class="container">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="feature-card quality text-center">
                    <i class="fas fa-award"></i>
                    <h4>Chất Lượng Đảm Bảo</h4>
                    <p>Tất cả xe đều được kiểm tra kỹ lưỡng và có chế độ bảo hành uy tín</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card support text-center">
                    <i class="fas fa-headset"></i>
                    <h4>Hỗ Trợ 24/7</h4>
                    <p>Đội ngũ chuyên nghiệp luôn sẵn sàng tư vấn và hỗ trợ mọi lúc mọi nơi</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card price text-center">
                    <i class="fas fa-dollar-sign"></i>
                    <h4>Giá Tốt Nhất</h4>
                    <p>Cam kết giá cạnh tranh nhất thị trường với nhiều ưu đãi hấp dẫn</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
