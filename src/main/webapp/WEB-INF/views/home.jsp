<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Car Showroom - Khám phá thế giới xe hơi đẳng cấp">
    <title>Trang Chủ - Car Showroom</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous">

    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
            color: #fff;
        }

        .hero-section {
            position: relative;
            height: 650px;
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
            url('${pageContext.request.contextPath}/images/BG.png') center/cover;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-align: center;
            margin-bottom: 50px;
            box-shadow: inset 0 0 100px rgba(0, 0, 0, 0.5);
        }

        .hero-content h1 {
            font-size: 3.5rem;
            font-weight: 700;
            margin-bottom: 20px;
            text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.8);
            background: linear-gradient(135deg, #fff 0%, #ffd700 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .hero-content p {
            font-size: 1.3rem;
            margin-bottom: 30px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
            color: #e0e0e0;
        }

        .btn-hero-primary {
            padding: 15px 40px;
            font-size: 1.1rem;
            border-radius: 50px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            border: none;
            transition: all 0.3s ease;
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
            transition: all 0.3s ease;
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

        .car-card-img {
            height: 200px;
            object-fit: cover;
            transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
            background: linear-gradient(180deg, #2a2a2a 0%, #1a1a1a 100%);
        }

        .car-card:hover .car-card-img {
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

        .car-card .brand-label {
            color: #ffd700 !important;
            font-size: 0.75rem !important;
            font-weight: 700;
            letter-spacing: 1.5px;
            margin-bottom: 0.5rem;
        }

        .car-card .card-text {
            font-size: 1.25rem !important;
            margin-bottom: 1rem !important;
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

        .btn-outline-primary {
            border-color: #ffd700;
            color: #ffd700;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-outline-primary:hover {
            background: #ffd700;
            border-color: #ffd700;
            color: #1a1a1a;
            box-shadow: 0 5px 20px rgba(255, 215, 0, 0.4);
        }

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
            transition: all 0.3s ease;
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

        .empty-state {
            color: #888;
            padding: 5rem 0;
        }

        .empty-state i {
            color: #555;
        }

        footer {
            margin-top: auto;
        }

        @media (max-width: 768px) {
            .hero-content h1 {
                font-size: 2.5rem;
            }

            .hero-content p {
                font-size: 1.1rem;
            }

            .section-title h2 {
                font-size: 2rem;
            }

            .btn-hero-primary,
            .btn-hero-outline {
                padding: 12px 30px;
                font-size: 1rem;
                margin: 5px;
            }
        }

        @media (max-width: 576px) {
            .hero-section {
                height: 500px;
            }

            .hero-content h1 {
                font-size: 2rem;
            }

            .hero-content p {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<section class="hero-section" aria-label="Hero banner">
    <div class="hero-content">
        <h1>Khám Phá Thế Giới Xe Hơi Đẳng Cấp</h1>
        <p>Tận hưởng trải nghiệm lái xe tuyệt vời với những mẫu xe mới nhất</p>
        <div class="d-flex gap-3 justify-content-center flex-wrap">
            <a href="${pageContext.request.contextPath}/cars"
               class="btn btn-hero-primary"
               title="Khám phá tất cả xe"
               aria-label="Khám phá tất cả xe có sẵn">
                <i class="fas fa-search" aria-hidden="true"></i> Khám Phá Ngay
            </a>
            <a href="${pageContext.request.contextPath}/promotions"
               class="btn btn-hero-outline"
               title="Xem khuyến mãi"
               aria-label="Xem các chương trình khuyến mãi">
                <i class="fas fa-gift" aria-hidden="true"></i> Khuyến Mãi
            </a>
        </div>
    </div>
</section>

<main class="container my-5 py-5">
    <!-- Featured Cars Section -->
    <section class="mb-5" aria-labelledby="featured-cars-heading">
        <!-- Section Title -->
        <div class="section-title">
            <h2 id="featured-cars-heading">
                <i class="fas fa-star text-warning" aria-hidden="true"></i> Xe Nổi Bật
            </h2>
            <p>Những mẫu xe được yêu thích nhất</p>
        </div>

        <!-- Car Grid -->
        <div class="row g-4" role="list">
            <c:choose>
                <%-- Display cars if available --%>
                <c:when test="${not empty cars}">
                    <c:forEach var="car" items="${cars}" begin="0" end="7">
                        <div class="col-lg-3 col-md-4 col-sm-6" role="listitem">
                            <article class="card car-card shadow-sm">
                                    <%-- Find main image --%>
                                <c:set var="mainImage" value="" />
                                <c:forEach var="img" items="${car.images}">
                                    <c:if test="${img.mainImage}">
                                        <c:set var="mainImage" value="${img.imageURL}" />
                                    </c:if>
                                </c:forEach>

                                    <%-- Display image with fallback --%>
                                <c:choose>
                                    <c:when test="${not empty mainImage}">
                                        <img src="${mainImage}"
                                             class="card-img-top car-card-img"
                                             alt="Hình ảnh xe ${car.name}"
                                             loading="lazy">
                                    </c:when>
                                    <c:when test="${not empty car.imageUrl}">
                                        <img src="${car.imageUrl}"
                                             class="card-img-top car-card-img"
                                             alt="Hình ảnh xe ${car.name}"
                                             loading="lazy">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/300x200?text=No+Image"
                                             class="card-img-top car-card-img"
                                             alt="Không có hình ảnh cho ${car.name}"
                                             loading="lazy">
                                    </c:otherwise>
                                </c:choose>

                                    <%-- Card Body --%>
                                <div class="card-body">
                                        <%-- Brand Name --%>
                                    <h6 class="text-primary text-uppercase brand-label">
                                            ${car.brandName}
                                    </h6>

                                        <%-- Car Name --%>
                                    <h5 class="card-title">${car.name}</h5>

                                        <%-- Price --%>
                                    <p class="card-text text-primary fw-bold fs-5">
                                        <fmt:formatNumber value="${car.price}" pattern="#,##0" /> ₫
                                    </p>

                                        <%-- View Details Button --%>
                                    <a href="${pageContext.request.contextPath}/car-detail?id=${car.id}"
                                       class="btn btn-primary w-100"
                                       title="Xem chi tiết xe ${car.name}"
                                       aria-label="Xem chi tiết xe ${car.name}">
                                        <i class="fas fa-eye" aria-hidden="true"></i> Xem Chi Tiết
                                    </a>
                                </div>
                            </article>
                        </div>
                    </c:forEach>
                </c:when>

                <%-- Empty state --%>
                <c:otherwise>
                    <div class="col-12">
                        <div class="text-center empty-state">
                            <i class="fas fa-car-crash fa-5x mb-3" aria-hidden="true"></i>
                            <h3>Hiện chưa có xe nào để hiển thị</h3>
                            <p>Vui lòng quay lại sau</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- View All Button --%>
        <div class="text-center mt-5">
            <a href="${pageContext.request.contextPath}/cars"
               class="btn btn-outline-primary btn-lg px-5"
               title="Xem tất cả các xe có sẵn"
               aria-label="Xem tất cả các xe có sẵn">
                <i class="fas fa-th me-2" aria-hidden="true"></i> Xem Tất Cả Xe
            </a>
        </div>
    </section>
</main>

<section class="features-section">
    <div class="container">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="feature-card quality text-center">
                    <i class="fas fa-award" aria-hidden="true"></i>
                    <h4>Chất Lượng Đảm Bảo</h4>
                    <p>Tất cả xe đều được kiểm tra kỹ lưỡng và có chế độ bảo hành uy tín</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card support text-center">
                    <i class="fas fa-headset" aria-hidden="true"></i>
                    <h4>Hỗ Trợ 24/7</h4>
                    <p>Đội ngũ chuyên nghiệp luôn sẵn sàng tư vấn và hỗ trợ mọi lúc mọi nơi</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card price text-center">
                    <i class="fas fa-dollar-sign" aria-hidden="true"></i>
                    <h4>Giá Tốt Nhất</h4>
                    <p>Cam kết giá cạnh tranh nhất thị trường với nhiều ưu đãi hấp dẫn</p>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>

</body>
</html>
