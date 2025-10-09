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
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .hero-section {
            background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
            url('https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?auto=format&fit=crop&w=1500&q=80') no-repeat center center/cover;
            color: white;
            padding: 150px 0;
            text-align: center;
        }
        .car-card {
            transition: transform 0.3s, box-shadow 0.3s;
            height: 100%;
        }
        .car-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .car-card img {
            height: 220px;
            object-fit: cover;
        }
        footer {
            margin-top: auto;
            background-color: #2f3542;
            color: white;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
            <i class="fas fa-car"></i> Car Showroom
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                        <i class="fas fa-search"></i> Xem Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/about">
                        <i class="fas fa-info-circle"></i> Giới Thiệu
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/contact">
                        <i class="fas fa-envelope"></i> Liên Hệ
                    </a>
                </li>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="fas fa-sign-in-alt"></i> Đăng Nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link btn btn-light text-primary ms-2" href="${pageContext.request.contextPath}/register">
                                <i class="fas fa-user-plus"></i> Đăng Ký
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link btn btn-light text-primary ms-2" href="${pageContext.request.contextPath}/home">
                                <i class="fas fa-home"></i> Vào Trang Chủ
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<div class="hero-section">
    <div class="container">
        <h1 class="display-3 fw-bold mb-4">Khám Phá Thế Giới Xe Hơi Đẳng Cấp</h1>
        <p class="lead fs-4 mb-5">Tận hưởng trải nghiệm lái xe tuyệt vời với những mẫu xe mới nhất</p>
        <div class="d-flex gap-3 justify-content-center">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary btn-lg px-5 py-3">
                <i class="fas fa-search"></i> Khám Phá Ngay
            </a>
            <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-light btn-lg px-5 py-3">
                <i class="fas fa-phone"></i> Liên Hệ
            </a>
        </div>
    </div>
</div>

<!-- Featured Cars Section -->
<div class="container my-5 py-5">
    <div class="text-center mb-5">
        <h2 class="display-5 fw-bold">
            <i class="fas fa-star text-warning"></i> Xe Nổi Bật
        </h2>
        <p class="lead text-muted">Những mẫu xe được yêu thích nhất</p>
    </div>

    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty cars}">
                <c:forEach var="car" items="${cars}" begin="0" end="7">
                    <div class="col-lg-3 col-md-4 col-sm-6">
                        <div class="card car-card border-0 shadow-sm">
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
                                    <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/>
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
                    <h3>Hiện chưa có xe nào để hiển thị</h3>
                    <p class="text-muted">Vui lòng quay lại sau</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="text-center mt-5">
        <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-primary btn-lg">
            <i class="fas fa-th"></i> Xem Tất Cả Xe
        </a>
    </div>
</div>

<!-- Features Section -->
<div class="bg-light py-5">
    <div class="container">
        <div class="row text-center">
            <div class="col-md-4 mb-4">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-4">
                        <i class="fas fa-award fa-3x text-primary mb-3"></i>
                        <h4 class="fw-bold">Chất Lượng Đảm Bảo</h4>
                        <p class="text-muted">Tất cả xe đều được kiểm tra kỹ lưỡng và có chế độ bảo hành uy tín</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-4">
                        <i class="fas fa-headset fa-3x text-success mb-3"></i>
                        <h4 class="fw-bold">Hỗ Trợ 24/7</h4>
                        <p class="text-muted">Đội ngũ chuyên nghiệp luôn sẵn sàng tư vấn và hỗ trợ mọi lúc mọi nơi</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-4">
                        <i class="fas fa-dollar-sign fa-3x text-warning mb-3"></i>
                        <h4 class="fw-bold">Giá Tốt Nhất</h4>
                        <p class="text-muted">Cam kết giá cạnh tranh nhất thị trường với nhiều ưu đãi hấp dẫn</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="py-4 text-center">
    <div class="container">
        <div class="row mb-4">
            <div class="col-md-4 mb-3">
                <h5 class="fw-bold"><i class="fas fa-car"></i> Car Showroom</h5>
                <p>Đối tác tin cậy cho hành trình của bạn</p>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="fw-bold">Liên Kết</h5>
                <div class="d-flex flex-column gap-2">
                    <a href="${pageContext.request.contextPath}/about" class="text-white text-decoration-none">Giới Thiệu</a>
                    <a href="${pageContext.request.contextPath}/cars" class="text-white text-decoration-none">Xe Hơi</a>
                    <a href="${pageContext.request.contextPath}/contact" class="text-white text-decoration-none">Liên Hệ</a>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <h5 class="fw-bold">Liên Hệ</h5>
                <p class="mb-1"><i class="fas fa-phone"></i> 0123 456 789</p>
                <p class="mb-1"><i class="fas fa-envelope"></i> info@carshowroom.com</p>
                <p class="mb-1"><i class="fas fa-map-marker-alt"></i> Cần Thơ, Việt Nam</p>
            </div>
        </div>
        <hr class="bg-light">
        <p class="mb-0">© 2025 Car Showroom. Thiết kế bởi Nhóm PRN212.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>