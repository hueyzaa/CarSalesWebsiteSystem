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
            background-color: #0f0f0f;
        }

        /* Hero Section */
        .hero-section {
            position: relative;
            height: 600px;
            background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)),
            url('https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=1920') center/cover;
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

        .btn-explore {
            padding: 15px 40px;
            font-size: 1.1rem;
            border-radius: 50px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            border: none;
            transition: all 0.3s;
            font-weight: 600;
        }

        .btn-explore:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        /* Car Cards */
        .car-card {
            transition: transform 0.3s, box-shadow 0.3s;
            height: 100%;
            border: none;
            border-radius: 15px;
            overflow: hidden;
            background: #1a1a1a;
            border: 1px solid #333;
        }

        .car-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.3);
            border-color: #ffd700;
        }

        .car-card img {
            height: 220px;
            object-fit: cover;
            transition: transform 0.3s;
        }

        .car-card:hover img {
            transform: scale(1.1);
        }

        .car-card .card-body {
            padding: 20px;
            background: #1a1a1a;
        }

        .car-card .card-title {
            color: #f8f9fa;
        }

        .car-card .text-primary {
            color: #ffd700 !important;
        }

        .car-card .small {
            color: #ffd700 !important;
        }

        .car-card .btn-primary {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
        }

        .car-card .btn-primary:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
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

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Hero Section -->
<div class="hero-section">
    <div class="hero-content">
        <h1>Khám phá thế giới xe hơi đẳng cấp</h1>
        <p>Tận hưởng trải nghiệm lái xe tuyệt vời với những mẫu xe mới nhất.</p>
        <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary btn-explore">
            Khám Phá Ngay
        </a>
    </div>
</div>

<!-- Main Content -->
<div class="container">
    <!-- Featured Cars Section -->
    <div class="mb-5">
        <div class="section-title">
            <h2>
                <i class="fas fa-star text-warning"></i> Xe Nổi Bật
            </h2>
            <p>Khám phá những mẫu xe được yêu thích nhất</p>
        </div>

        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty cars}">
                    <c:forEach var="car" items="${cars}" begin="0" end="7">
                        <div class="col-lg-3 col-md-4 col-sm-6">
                            <div class="card car-card shadow-sm">
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
                        <h3 style="color: #888;">Hiện chưa có xe nào để hiển thị</h3>
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
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>