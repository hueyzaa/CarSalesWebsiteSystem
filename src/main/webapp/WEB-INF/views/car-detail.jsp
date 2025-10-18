<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${car.name} - Chi Tiết Xe</title>
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

        .car-detail-container {
            background: #1a1a1a;
            border-radius: 20px;
            padding: 40px;
            margin: 40px 0;
            border: 1px solid #333;
            box-shadow: 0 10px 40px rgba(0,0,0,0.5);
        }

        .breadcrumb {
            background: transparent;
            padding: 20px 0;
        }

        .breadcrumb-item a {
            color: #ffd700;
            text-decoration: none;
        }

        .breadcrumb-item.active {
            color: #888;
        }

        /* Image Gallery */
        .main-image-container {
            position: relative;
            border-radius: 15px;
            overflow: hidden;
            background: #0f0f0f;
            border: 2px solid #333;
            margin-bottom: 20px;
        }

        .main-image {
            width: 100%;
            height: 500px;
            object-fit: cover;
            transition: transform 0.3s;
        }

        .main-image:hover {
            transform: scale(1.05);
        }

        .status-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
            color: white;
            padding: 10px 20px;
            border-radius: 50px;
            font-weight: 600;
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.4);
        }

        .status-badge.unavailable {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
        }

        .thumbnail-gallery {
            display: flex;
            gap: 15px;
            overflow-x: auto;
            padding: 10px 0;
        }

        .thumbnail-gallery::-webkit-scrollbar {
            height: 8px;
        }

        .thumbnail-gallery::-webkit-scrollbar-track {
            background: #2a2a2a;
            border-radius: 10px;
        }

        .thumbnail-gallery::-webkit-scrollbar-thumb {
            background: #ffd700;
            border-radius: 10px;
        }

        .thumbnail {
            min-width: 120px;
            height: 90px;
            border-radius: 10px;
            overflow: hidden;
            cursor: pointer;
            border: 3px solid transparent;
            transition: all 0.3s;
        }

        .thumbnail:hover {
            border-color: #ffd700;
            transform: translateY(-3px);
        }

        .thumbnail.active {
            border-color: #ffd700;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }

        .thumbnail img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        /* Car Info */
        .car-title {
            color: #f8f9fa;
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .brand-name {
            color: #ffd700;
            font-size: 1.2rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 20px;
        }

        .price-section {
            background: linear-gradient(135deg, #2a2a2a 0%, #1a1a1a 100%);
            padding: 25px;
            border-radius: 15px;
            margin: 20px 0;
            border: 1px solid #333;
        }

        .price {
            color: #ffd700;
            font-size: 2rem;
            font-weight: 700;
            margin: 0;
        }

        .price-label {
            color: #888;
            font-size: 0.95rem;
            margin-bottom: 8px;
        }

        /* Specs */
        .specs-section {
            background: #0f0f0f;
            padding: 25px;
            border-radius: 15px;
            margin: 20px 0;
            border: 1px solid #333;
        }

        .specs-title {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .spec-item {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #333;
        }

        .spec-item:last-child {
            border-bottom: none;
        }

        .spec-label {
            color: #888;
            font-weight: 600;
            font-size: 0.95rem;
        }

        .spec-value {
            color: #f8f9fa;
            font-weight: 600;
        }

        /* Add to Cart Form */
        .add-to-cart-section {
            background: linear-gradient(135deg, #2a2a2a 0%, #1a1a1a 100%);
            padding: 25px;
            border-radius: 15px;
            border: 2px solid #ffd700;
            margin: 20px 0;
        }

        .quantity-selector {
            display: flex;
            align-items: center;
            gap: 15px;
            margin: 15px 0;
        }

        .quantity-selector label {
            color: #f8f9fa;
            font-weight: 600;
            font-size: 1rem;
        }

        .quantity-input {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .quantity-btn {
            width: 40px;
            height: 40px;
            border: none;
            border-radius: 8px;
            background: #333;
            color: #ffd700;
            font-size: 1.2rem;
            cursor: pointer;
            transition: all 0.3s;
        }

        .quantity-btn:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: scale(1.1);
        }

        .quantity-value {
            width: 60px;
            height: 40px;
            text-align: center;
            border: 2px solid #333;
            border-radius: 8px;
            background: #0f0f0f;
            color: #f8f9fa;
            font-size: 1.1rem;
            font-weight: 600;
        }

        .quantity-value::-webkit-outer-spin-button,
        .quantity-value::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        .quantity-value[type=number] {
            -moz-appearance: textfield;
        }

        .btn-add-cart {
            width: 100%;
            padding: 15px;
            font-size: 1.1rem;
            font-weight: 700;
            border: none;
            border-radius: 50px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            transition: all 0.3s;
            margin-top: 15px;
        }

        .btn-add-cart:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
        }

        .btn-add-cart:disabled {
            background: #555;
            color: #888;
            cursor: not-allowed;
            transform: none;
        }

        .stock-info {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 12px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-top: 15px;
            font-size: 0.95rem;
        }

        .stock-info i {
            color: #4caf50;
        }

        .stock-info.low-stock i {
            color: #ff9800;
        }

        .stock-info.out-of-stock i {
            color: #dc3545;
        }

        /* Promotion Section */
        .promotion-section {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 25px;
            border-radius: 15px;
            margin-top: 20px;
            border: 2px solid #ffd700;
        }

        .promotion-section h3 {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .best-discount-badge {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 20px;
            box-shadow: 0 5px 20px rgba(220, 53, 69, 0.4);
        }

        .promotion-card {
            background: #0f0f0f;
            padding: 20px;
            border-radius: 12px;
            border: 1px solid #333;
            transition: all 0.3s;
            margin-bottom: 15px;
        }

        .promotion-card:hover {
            border-color: #ffd700;
            transform: translateX(5px);
        }

        .description-section {
            margin: 30px 0;
            background: #0f0f0f;
            padding: 30px;
            border-radius: 15px;
            border: 1px solid #333;
        }

        .description-title {
            color: #ffd700;
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .description-text {
            color: #b0b0b0;
            line-height: 1.8;
            font-size: 1.05rem;
        }

        footer {
            margin-top: auto;
        }

        .alert {
            border-radius: 15px;
            border: none;
        }

        .alert-success {
            background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
            color: white;
        }

        .alert-danger {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
        }

        /* Responsive */
        @media (max-width: 992px) {
            .main-image {
                height: 400px;
            }

            .car-title {
                font-size: 2rem;
            }

            .price {
                font-size: 1.8rem;
            }
        }

        @media (max-width: 768px) {
            .car-detail-container {
                padding: 20px;
            }

            .main-image {
                height: 300px;
            }

            .car-title {
                font-size: 1.5rem;
            }

            .price {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang Chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cars">Xem Xe</a></li>
            <li class="breadcrumb-item active">${car.name}</li>
        </ol>
    </nav>

    <!-- Success/Error Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Car Detail Container -->
    <div class="car-detail-container">
        <div class="row">
            <!-- Left Column - Images -->
            <div class="col-lg-7 mb-4">
                <!-- Main Image -->
                <div class="main-image-container">
                    <c:choose>
                        <c:when test="${not empty car.images}">
                            <c:set var="mainImage" value=""/>
                            <c:forEach var="img" items="${car.images}">
                                <c:if test="${img.mainImage}">
                                    <c:set var="mainImage" value="${img.imageURL}"/>
                                </c:if>
                            </c:forEach>
                            <c:if test="${empty mainImage and not empty car.images}">
                                <c:set var="mainImage" value="${car.images[0].imageURL}"/>
                            </c:if>
                            <img src="${mainImage}" alt="${car.name}" class="main-image" id="mainImage">
                        </c:when>
                        <c:when test="${not empty car.imageUrl}">
                            <img src="${car.imageUrl}" alt="${car.name}" class="main-image" id="mainImage">
                        </c:when>
                        <c:otherwise>
                            <img src="https://via.placeholder.com/800x500?text=No+Image" alt="${car.name}" class="main-image" id="mainImage">
                        </c:otherwise>
                    </c:choose>

                    <!-- Status Badge -->
                    <span class="status-badge ${car.status == 'AVAILABLE' ? '' : 'unavailable'}">
                        <c:choose>
                            <c:when test="${car.status == 'AVAILABLE'}">
                                <i class="fas fa-check-circle"></i> Còn Hàng
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-times-circle"></i> Hết Hàng
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <!-- Thumbnail Gallery -->
                <c:if test="${not empty car.images and car.images.size() > 1}">
                    <div class="thumbnail-gallery">
                        <c:forEach var="img" items="${car.images}" varStatus="status">
                            <div class="thumbnail ${status.first ? 'active' : ''}" onclick="changeImage('${img.imageURL}', this)">
                                <img src="${img.imageURL}" alt="${car.name}">
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Description Section (Mobile: below images) -->
                <c:if test="${not empty car.description}">
                    <div class="description-section d-lg-none mt-4">
                        <h3 class="description-title">
                            <i class="fas fa-file-alt"></i> Mô Tả Chi Tiết
                        </h3>
                        <p class="description-text">${car.description}</p>
                    </div>
                </c:if>
            </div>

            <!-- Right Column - Info & Purchase -->
            <div class="col-lg-5">
                <!-- Brand & Title -->
                <div class="brand-name">
                    <i class="fas fa-award"></i> ${car.brandName}
                </div>
                <h1 class="car-title">${car.name}</h1>

                <!-- Price Section -->
                <div class="price-section">
                    <div class="price-label">Giá Xe</div>
                    <p class="price">
                        <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/>
                    </p>
                </div>

                <!-- Specs Section -->
                <div class="specs-section">
                    <h3 class="specs-title">
                        <i class="fas fa-info-circle"></i> Thông Số Kỹ Thuật
                    </h3>
                    <c:if test="${not empty car.year}">
                        <div class="spec-item">
                            <span class="spec-label"><i class="fas fa-calendar"></i> Năm Sản Xuất</span>
                            <span class="spec-value">${car.year}</span>
                        </div>
                    </c:if>
                    <c:if test="${not empty car.color}">
                        <div class="spec-item">
                            <span class="spec-label"><i class="fas fa-palette"></i> Màu Sắc</span>
                            <span class="spec-value">${car.color}</span>
                        </div>
                    </c:if>
                    <div class="spec-item">
                        <span class="spec-label"><i class="fas fa-box"></i> Tình Trạng</span>
                        <span class="spec-value">${car.status == 'AVAILABLE' ? 'Còn Hàng' : 'Hết Hàng'}</span>
                    </div>
                    <c:if test="${car.stock > 0}">
                        <div class="spec-item">
                            <span class="spec-label"><i class="fas fa-warehouse"></i> Số Lượng Có Sẵn</span>
                            <span class="spec-value">${car.stock} xe</span>
                        </div>
                    </c:if>
                </div>

                <!-- Add to Cart Section -->
                <div class="add-to-cart-section">
                    <form action="${pageContext.request.contextPath}/cart" method="post" id="addToCartForm">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="carId" value="${car.id}">

                        <!-- Quantity Selector -->
                        <div class="quantity-selector">
                            <label for="quantity">Số Lượng:</label>
                            <div class="quantity-input">
                                <button type="button" class="quantity-btn" onclick="decreaseQuantity()">-</button>
                                <input type="number"
                                       id="quantity"
                                       name="quantity"
                                       value="1"
                                       min="1"
                                       max="${car.stock > 0 ? car.stock : 1}"
                                       class="quantity-value"
                                       readonly>
                                <button type="button" class="quantity-btn" onclick="increaseQuantity()">+</button>
                            </div>
                        </div>

                        <!-- Stock Info -->
                        <c:choose>
                            <c:when test="${car.stock > 10}">
                                <div class="stock-info">
                                    <i class="fas fa-check-circle"></i>
                                    <span>Còn ${car.stock} xe trong kho</span>
                                </div>
                            </c:when>
                            <c:when test="${car.stock > 0 and car.stock <= 10}">
                                <div class="stock-info low-stock">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    <span>Chỉ còn ${car.stock} xe - Đặt hàng ngay!</span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="stock-info out-of-stock">
                                    <i class="fas fa-times-circle"></i>
                                    <span>Hiện tại đã hết hàng</span>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Add to Cart Button -->
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <button type="submit"
                                        class="btn-add-cart"
                                    ${car.stock <= 0 || car.status != 'AVAILABLE' ? 'disabled' : ''}>
                                    <i class="fas fa-shopping-cart"></i>
                                        ${car.stock > 0 && car.status == 'AVAILABLE' ? 'Thêm Vào Giỏ Hàng' : 'Hết Hàng'}
                                </button>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/login" class="btn-add-cart" style="display: block; text-align: center; text-decoration: none;">
                                    <i class="fas fa-sign-in-alt"></i> Đăng Nhập Để Mua Hàng
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </div>

                <!-- Promotion Section -->
                <c:if test="${not empty activePromotions}">
                    <div class="promotion-section">
                        <h3>
                            <i class="fas fa-gift"></i> Khuyến Mãi Đặc Biệt
                        </h3>

                        <!-- Best Discount Badge -->
                        <c:if test="${not empty bestPromotion}">
                            <div class="best-discount-badge">
                                <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 15px;">
                                    <div>
                                        <div style="font-size: 0.9rem; opacity: 0.9; margin-bottom: 5px;">
                                            🔥 Giảm giá tốt nhất
                                        </div>
                                        <div style="font-size: 1.8rem; font-weight: 700;">
                                            <c:choose>
                                                <c:when test="${bestDiscountPercentage > 0}">
                                                    -${bestDiscountPercentage}%
                                                </c:when>
                                                <c:otherwise>
                                                    -<fmt:formatNumber value="${bestDiscountAmount}" type="number" maxFractionDigits="0"/>₫
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <c:if test="${not empty discountedPrice}">
                                        <div style="text-align: right;">
                                            <div style="font-size: 0.9rem; opacity: 0.9; margin-bottom: 5px;">
                                                Giá sau giảm
                                            </div>
                                            <div style="font-size: 1.5rem; font-weight: 700;">
                                                <fmt:formatNumber value="${discountedPrice}" type="number" maxFractionDigits="0"/>₫
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:if>

                        <!-- Promotions List -->
                        <div style="max-height: 400px; overflow-y: auto; padding-right: 5px;">
                            <c:forEach var="promo" items="${activePromotions}">
                                <div class="promotion-card">
                                    <h4 style="color: #ffd700; font-size: 1.1rem; font-weight: 600; margin-bottom: 10px;">
                                            ${promo.title}
                                    </h4>
                                    <p style="color: #b0b0b0; margin-bottom: 12px; font-size: 0.9rem;">
                                            ${promo.description}
                                    </p>
                                    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
                                        <div style="color: #888; font-size: 0.85rem;">
                                            <i class="far fa-calendar-alt" style="color: #ffd700;"></i>
                                            <fmt:formatDate value="${promo.startDate}" pattern="dd/MM/yyyy"/> -
                                            <fmt:formatDate value="${promo.endDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty sessionScope.user}">
                                                <a href="${pageContext.request.contextPath}/login?redirect=car-detail?id=${car.id}"
                                                   style="background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
                                                          color: white; padding: 8px 16px; border-radius: 20px;
                                                          text-decoration: none; font-weight: 600; font-size: 0.85rem;">
                                                    <i class="fas fa-sign-in-alt"></i> Đăng nhập
                                                </a>
                                            </c:when>
                                            <c:when test="${promo.claimedByUser}">
                                                <button disabled style="background: #6c757d; color: white; padding: 8px 16px;
                                                               border-radius: 20px; border: none; font-size: 0.85rem; opacity: 0.7;">
                                                    <i class="fas fa-check"></i> Đã nhận
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/promotions/claim" method="post" style="display: inline;">
                                                    <input type="hidden" name="promotionId" value="${promo.promotionId}">
                                                    <input type="hidden" name="redirectUrl" value="car-detail?id=${car.id}">
                                                    <button type="submit" style="background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
                                                                   color: white; padding: 8px 16px; border-radius: 20px;
                                                                   border: none; font-size: 0.85rem; cursor: pointer;">
                                                        <i class="fas fa-gift"></i> Nhận ngay
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- View All Link -->
                        <div style="text-align: center; margin-top: 15px;">
                            <a href="${pageContext.request.contextPath}/promotions"
                               style="color: #ffd700; text-decoration: none; font-weight: 600; font-size: 0.95rem;">
                                <i class="fas fa-arrow-right"></i> Xem tất cả khuyến mãi
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Description Section (Desktop: full width below) -->
        <c:if test="${not empty car.description}">
            <div class="row mt-4 d-none d-lg-block">
                <div class="col-12">
                    <div class="description-section">
                        <h3 class="description-title">
                            <i class="fas fa-file-alt"></i> Mô Tả Chi Tiết
                        </h3>
                        <p class="description-text">${car.description}</p>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Change main image when clicking thumbnail
    function changeImage(imageUrl, thumbnail) {
        document.getElementById('mainImage').src = imageUrl;
        document.querySelectorAll('.thumbnail').forEach(function(t) {
            t.classList.remove('active');
        });
        thumbnail.classList.add('active');
    }

    // Quantity controls
    function increaseQuantity() {
        const input = document.getElementById('quantity');
        const max = parseInt(input.max);
        const current = parseInt(input.value);

        if (current < max) {
            input.value = current + 1;
        }
    }

    function decreaseQuantity() {
        const input = document.getElementById('quantity');
        const min = parseInt(input.min);
        const current = parseInt(input.value);

        if (current > min) {
            input.value = current - 1;
        }
    }

    // Form validation
    const form = document.getElementById('addToCartForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            const quantity = parseInt(document.getElementById('quantity').value);
            const maxStock = parseInt(document.getElementById('quantity').max);

            if (quantity < 1) {
                e.preventDefault();
                alert('Số lượng phải lớn hơn 0!');
                return false;
            }

            if (quantity > maxStock) {
                e.preventDefault();
                alert('Số lượng vượt quá hàng có sẵn!');
                return false;
            }
        });
    }
</script>
</body>
</html>