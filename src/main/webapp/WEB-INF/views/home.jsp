<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 60px 0;
            border-radius: 15px;
            margin: 30px 0;
            box-shadow: 0 8px 20px rgba(102,126,234,0.3);
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
        .action-btn {
            padding: 25px;
            border-radius: 15px;
            text-decoration: none;
            color: white;
            transition: all 0.3s;
            display: block;
            text-align: center;
        }
        .action-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
            color: white;
        }
        .action-cars {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .action-profile {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        .action-contact {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container my-5">
    <!-- Welcome Section -->
    <div class="welcome-section">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="display-4 fw-bold mb-3">
                        <i class="fas fa-hand-sparkles"></i>
                        Chào mừng <c:out value="${sessionScope.user.name}" default="bạn"/>!
                    </h1>
                    <p class="lead">Khám phá bộ sưu tập xe hơi đẳng cấp của chúng tôi ngay hôm nay</p>
                </div>
                <div class="col-md-4 text-center">
                    <i class="fas fa-car fa-5x opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <!-- User Info and Quick Actions -->
    <div class="row g-4 mb-5">
        <!-- User Information Card -->
        <div class="col-lg-6">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-user-circle"></i> Thông Tin Cá Nhân
                    </h5>
                </div>
                <div class="card-body">
                    <div class="mb-3 pb-3 border-bottom">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="text-muted">
                                <i class="fas fa-user"></i> Tên đầy đủ:
                            </span>
                            <span class="fw-bold">
                                <c:out value="${sessionScope.user.name}" default="N/A"/>
                            </span>
                        </div>
                    </div>
                    <div class="mb-3 pb-3 border-bottom">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="text-muted">
                                <i class="fas fa-envelope"></i> Email:
                            </span>
                            <span class="fw-bold">
                                <c:out value="${sessionScope.user.email}" default="N/A"/>
                            </span>
                        </div>
                    </div>
                    <div class="mb-3 pb-3 border-bottom">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="text-muted">
                                <i class="fas fa-phone"></i> Số điện thoại:
                            </span>
                            <span class="fw-bold">
                                <c:out value="${sessionScope.user.phone}" default="Chưa cập nhật"/>
                            </span>
                        </div>
                    </div>
                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="text-muted">
                                <i class="fas fa-map-marker-alt"></i> Địa chỉ:
                            </span>
                            <span class="fw-bold">
                                <c:out value="${sessionScope.user.address}" default="Chưa cập nhật"/>
                            </span>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary w-100 mt-3">
                        <i class="fas fa-edit"></i> Chỉnh Sửa Thông Tin
                    </a>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="col-lg-6">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-bolt"></i> Hành Động Nhanh
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <a href="${pageContext.request.contextPath}/cars" class="action-btn action-cars">
                                <i class="fas fa-search fa-2x mb-2"></i>
                                <h6 class="fw-bold mb-0">Tìm Xe</h6>
                            </a>
                        </div>
                        <div class="col-md-4">
                            <a href="${pageContext.request.contextPath}/favorites" class="action-btn action-profile">
                                <i class="fas fa-heart fa-2x mb-2"></i>
                                <h6 class="fw-bold mb-0">Yêu Thích</h6>
                            </a>
                        </div>
                        <div class="col-md-4">
                            <a href="${pageContext.request.contextPath}/contact" class="action-btn action-contact">
                                <i class="fas fa-envelope fa-2x mb-2"></i>
                                <h6 class="fw-bold mb-0">Liên Hệ</h6>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-primary w-100 py-3">
                                <i class="fas fa-shopping-cart fa-lg"></i><br>
                                <small>Giỏ Hàng</small>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-success w-100 py-3">
                                <i class="fas fa-receipt fa-lg"></i><br>
                                <small>Đơn Hàng</small>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Featured Cars -->
    <div class="mb-5">
        <div class="text-center mb-4">
            <h2 class="display-6 fw-bold">
                <i class="fas fa-star text-warning"></i> Xe Nổi Bật
            </h2>
            <p class="text-muted">Khám phá những mẫu xe được yêu thích nhất</p>
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
                        <i class="fas fa-car-crash fa-4x text-muted mb-3"></i>
                        <h3>Hiện chưa có xe nào để hiển thị</h3>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-primary btn-lg">
                <i class="fas fa-th"></i> Xem Tất Cả Xe
            </a>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>