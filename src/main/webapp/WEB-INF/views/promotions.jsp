<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khuyến Mãi - Car Showroom</title>
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

        .hero-promotion {
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
            url('https://images.unsplash.com/photo-1558618666-fcd25c85cd64?auto=format&fit=crop&w=1500&q=80') center/cover;
            padding: 100px 0;
            margin-bottom: 50px;
            text-align: center;
            color: white;
            box-shadow: inset 0 0 100px rgba(0,0,0,0.5);
        }

        .hero-promotion h1 {
            font-size: 3rem;
            font-weight: 700;
            margin-bottom: 20px;
            color: #ffd700;
            text-shadow: 3px 3px 6px rgba(0,0,0,0.8);
        }

        .hero-promotion p {
            font-size: 1.3rem;
            color: #e0e0e0;
        }

        .notification-banner {
            background: linear-gradient(135deg, rgba(255, 215, 0, 0.15), rgba(255, 237, 78, 0.15));
            border: 2px solid #ffd700;
            border-radius: 15px;
            padding: 15px 20px;
            margin-top: -20px;
            margin-bottom: 30px;
        }

        .notification-banner .notification-content {
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
        }

        .notification-banner .notification-text {
            color: #ffd700;
            font-weight: 600;
            font-size: 1rem;
        }

        .notification-banner .notification-text i {
            font-size: 1.2rem;
            margin-right: 10px;
        }

        .notification-banner .btn-view-my-promotions {
            background: linear-gradient(135deg, #ffd700, #ffed4e);
            color: #1a1a1a;
            padding: 8px 20px;
            border-radius: 20px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .notification-banner .btn-view-my-promotions:hover {
            background: linear-gradient(135deg, #ffed4e, #ffd700);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }

        .promotion-card {
            background: #1a1a1a;
            border: 2px solid #ffd700;
            border-radius: 20px;
            padding: 35px;
            margin-bottom: 30px;
            transition: all 0.3s;
            position: relative;
            overflow: hidden;
        }

        .promotion-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 5px;
            background: linear-gradient(90deg, #ffd700, #ffed4e, #ffd700);
        }

        .promotion-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.4);
        }

        .promotion-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 8px 20px;
            border-radius: 25px;
            font-weight: 700;
            font-size: 0.9rem;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.4);
        }

        .promotion-title {
            color: #ffd700;
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 15px;
            padding-right: 120px;
        }

        .promotion-description {
            color: #b0b0b0;
            line-height: 1.8;
            margin-bottom: 25px;
            font-size: 1.05rem;
        }

        .promotion-meta {
            display: flex;
            gap: 30px;
            margin-bottom: 25px;
            padding-bottom: 20px;
            border-bottom: 1px solid #333;
            flex-wrap: wrap;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 10px;
            color: #888;
        }

        .meta-item i {
            color: #ffd700;
            font-size: 1.2rem;
        }

        .cars-section {
            margin-top: 30px;
        }

        .cars-section h5 {
            color: #f8f9fa;
            font-weight: 600;
            margin-bottom: 20px;
            font-size: 1.2rem;
        }

        .car-item {
            background: #0f0f0f;
            border: 1px solid #333;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 15px;
            display: flex;
            align-items: flex-start;
            gap: 15px;
            transition: all 0.3s;
        }

        .car-item:hover {
            border-color: #ffd700;
            transform: translateX(5px);
        }

        .car-item i.fa-car-side {
            color: #ffd700;
            font-size: 1.5rem;
            margin-top: 5px;
            min-width: 25px;
        }

        .car-info {
            flex: 1;
        }

        .car-name {
            color: #f8f9fa;
            font-weight: 600;
            margin: 0 0 5px 0;
            font-size: 1.1rem;
        }

        .car-details {
            color: #888;
            font-size: 0.9rem;
            margin-bottom: 8px;
        }

        /* ✅ Car discount badge */
        .car-discount-badge {
            display: inline-block;
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 0.85rem;
            font-weight: 600;
            margin-top: 5px;
        }

        .car-discount-badge i {
            font-size: 0.8rem;
            margin-right: 3px;
        }

        .car-price-section {
            text-align: right;
            min-width: 180px;
        }

        .car-price-original {
            color: #888;
            text-decoration: line-through;
            font-size: 0.9rem;
            margin-bottom: 2px;
        }

        .car-price-discounted {
            color: #dc3545;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .car-price-normal {
            color: #28a745;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .car-savings {
            color: #28a745;
            font-size: 0.85rem;
            margin-top: 3px;
        }

        .no-promotions {
            text-align: center;
            padding: 80px 20px;
            background: #1a1a1a;
            border-radius: 15px;
            border: 1px solid #333;
        }

        .no-promotions i {
            font-size: 5rem;
            color: #444;
            margin-bottom: 20px;
        }

        .no-promotions h3 {
            color: #888;
            margin-bottom: 15px;
        }

        .no-promotions p {
            color: #666;
        }

        .action-buttons {
            margin-top: 25px;
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }

        .btn-view-cars {
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
            white-space: nowrap;
        }

        .btn-view-cars:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-claim-promotion {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 10px 20px;
            font-size: 0.9rem;
            border-radius: 25px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
        }

        .btn-claim-promotion:hover {
            background: linear-gradient(135deg, #20c997 0%, #28a745 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(40, 167, 69, 0.4);
            color: white;
        }

        .btn-claimed {
            background: #6c757d;
            border: none;
            color: white;
            font-weight: 600;
            padding: 10px 20px;
            font-size: 0.9rem;
            border-radius: 25px;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: not-allowed;
            opacity: 0.7;
        }

        .btn-claimed-used {
            background: #17a2b8;
            border: none;
            color: white;
            font-weight: 600;
            padding: 10px 20px;
            font-size: 0.9rem;
            border-radius: 25px;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            cursor: not-allowed;
            opacity: 0.7;
        }

        .btn-login {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 10px 20px;
            font-size: 0.9rem;
            border-radius: 25px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .btn-login:hover {
            background: linear-gradient(135deg, #0056b3 0%, #007bff 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(0, 123, 255, 0.4);
            color: white;
        }

        .discount-badge {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
            padding: 10px 20px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 0.9rem;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .alert {
            border-radius: 15px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 20px;
        }

        .alert-success {
            background: linear-gradient(135deg, rgba(40, 167, 69, 0.2), rgba(32, 201, 151, 0.2));
            border-left: 4px solid #28a745;
            color: #28a745;
        }

        .alert-danger {
            background: linear-gradient(135deg, rgba(220, 53, 69, 0.2), rgba(200, 35, 51, 0.2));
            border-left: 4px solid #dc3545;
            color: #dc3545;
        }

        .alert-info {
            background: linear-gradient(135deg, rgba(23, 162, 184, 0.2), rgba(13, 202, 240, 0.2));
            border-left: 4px solid #17a2b8;
            color: #17a2b8;
        }

        footer {
            margin-top: auto;
        }

        @media (max-width: 768px) {
            .hero-promotion h1 {
                font-size: 2rem;
            }

            .hero-promotion p {
                font-size: 1rem;
            }

            .promotion-title {
                font-size: 1.4rem;
                padding-right: 0;
            }

            .promotion-badge {
                position: static;
                display: inline-block;
                margin-bottom: 15px;
            }

            .promotion-meta {
                flex-direction: column;
                gap: 10px;
            }

            .notification-banner .notification-content {
                flex-direction: column;
                text-align: center;
            }

            .notification-banner .btn-view-my-promotions {
                width: 100%;
                justify-content: center;
            }

            .car-item {
                flex-direction: column;
            }

            .car-price-section {
                text-align: left;
                width: 100%;
                margin-top: 10px;
            }

            .action-buttons {
                flex-direction: column;
                width: 100%;
            }

            .action-buttons a,
            .action-buttons button,
            .action-buttons form {
                width: 100%;
            }

            .btn-view-cars,
            .btn-claim-promotion,
            .btn-login,
            .btn-claimed,
            .btn-claimed-used {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<!-- Hero Section -->
<div class="hero-promotion">
    <div class="container">
        <h1><i class="fas fa-gift"></i> Khuyến Mãi Đặc Biệt</h1>
        <p>Ưu đãi hấp dẫn cho các dòng xe cao cấp</p>
    </div>
</div>

<!-- Notification Banner for Logged-in Users -->
<c:if test="${isLoggedIn}">
    <div class="container">
        <div class="notification-banner">
            <div class="notification-content">
                <div class="notification-text">
                    <i class="fas fa-info-circle"></i>
                    Bạn đang có <strong>${unusedCount > 0 ? unusedCount : 0}</strong> khuyến mãi chưa sử dụng
                </div>
                <a href="${pageContext.request.contextPath}/my-promotions" class="btn-view-my-promotions">
                    Xem khuyến mãi của tôi <i class="fas fa-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</c:if>

<div class="container my-5">
    <!-- Success Message -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <!-- Error Message -->
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <!-- Info Message -->
    <c:if test="${not empty sessionScope.infoMessage}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <i class="fas fa-info-circle"></i> ${sessionScope.infoMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="infoMessage" scope="session"/>
    </c:if>

    <!-- Promotions List -->
    <c:choose>
        <c:when test="${not empty promotions}">
            <c:forEach var="promotion" items="${promotions}">
                <div class="promotion-card">
                    <!-- HOT Badge -->
                    <span class="promotion-badge">
                        <i class="fas fa-fire"></i> HOT
                    </span>

                    <!-- Promotion Title -->
                    <h2 class="promotion-title">${promotion.title}</h2>

                    <!-- Promotion Description -->
                    <p class="promotion-description">${promotion.description}</p>

                    <!-- Promotion Meta Info -->
                    <div class="promotion-meta">
                        <div class="meta-item">
                            <i class="far fa-calendar-alt"></i>
                            <span>Từ <fmt:formatDate value="${promotion.startDate}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <div class="meta-item">
                            <i class="far fa-calendar-check"></i>
                            <span>Đến <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <c:if test="${promotion.discountPercentage > 0 || promotion.discountAmount > 0}">
                            <div class="meta-item">
                                <i class="fas fa-tags"></i>
                                <span>
                                    <c:choose>
                                        <c:when test="${promotion.discountPercentage > 0}">
                                            Giảm ${promotion.discountPercentage}%
                                        </c:when>
                                        <c:otherwise>
                                            Giảm <fmt:formatNumber value="${promotion.discountAmount}" type="currency" currencySymbol="₫"/>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </c:if>
                    </div>

                    <!-- ✅ Cars Section with Individual Discounts -->
                    <c:if test="${not empty promotion.applicableCars}">
                        <div class="cars-section">
                            <h5><i class="fas fa-car"></i> Xe Áp Dụng Khuyến Mãi (${promotion.applicableCars.size()} xe):</h5>
                            <div class="row">
                                <c:forEach var="car" items="${promotion.applicableCars}">
                                    <div class="col-md-6">
                                        <div class="car-item">
                                            <i class="fas fa-car-side"></i>
                                            <div class="car-info">
                                                <p class="car-name">${car.brandName} ${car.name}</p>
                                                <div class="car-details">
                                                    <i class="far fa-calendar"></i> ${car.year}
                                                    <span style="margin: 0 8px;">•</span>
                                                    <i class="fas fa-palette"></i> ${car.color}
                                                </div>

                                                <!-- ✅ Display discount badge for this specific car -->
                                                <c:if test="${car.hasDiscount()}">
                                                    <div class="car-discount-badge">
                                                        <i class="fas fa-tag"></i>
                                                        <c:choose>
                                                            <c:when test="${car.discountPercentage > 0}">
                                                                Giảm ${car.discountPercentage}%
                                                            </c:when>
                                                            <c:otherwise>
                                                                Giảm <fmt:formatNumber value="${car.discountAmount}" type="number" maxFractionDigits="0"/>₫
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <!-- ✅ Price Section with Original and Discounted Price -->
                                            <div class="car-price-section">
                                                <c:choose>
                                                    <c:when test="${car.hasDiscount()}">
                                                        <!-- Original Price (strikethrough) -->
                                                        <div class="car-price-original">
                                                            <fmt:formatNumber value="${car.price}" type="number" maxFractionDigits="0"/>₫
                                                        </div>
                                                        <!-- Discounted Price (red, bold) -->
                                                        <div class="car-price-discounted">
                                                            <fmt:formatNumber value="${car.discountedPrice}" type="number" maxFractionDigits="0"/>₫
                                                        </div>
                                                        <!-- Savings -->
                                                        <div class="car-savings">
                                                            Tiết kiệm: <fmt:formatNumber value="${car.discountValue}" type="number" maxFractionDigits="0"/>₫
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!-- Normal Price (green) -->
                                                        <div class="car-price-normal">
                                                            <fmt:formatNumber value="${car.price}" type="number" maxFractionDigits="0"/>₫
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <!-- View Cars Button -->
                        <a href="${pageContext.request.contextPath}/cars" class="btn-view-cars">
                            <i class="fas fa-eye"></i> Xem Tất Cả Xe
                        </a>

                        <!-- Claim Promotion Button -->
                        <c:choose>
                            <%-- User not logged in --%>
                            <c:when test="${not isLoggedIn}">
                                <a href="${pageContext.request.contextPath}/login?redirect=promotions"
                                   class="btn-login">
                                    <i class="fas fa-sign-in-alt"></i> Đăng Nhập Để Nhận
                                </a>
                            </c:when>

                            <%-- User already claimed and used --%>
                            <c:when test="${promotion.usedByUser}">
                                <button class="btn-claimed-used" disabled>
                                    <i class="fas fa-check-double"></i> Đã Sử Dụng
                                </button>
                            </c:when>

                            <%-- User already claimed but not used --%>
                            <c:when test="${promotion.claimedByUser}">
                                <button class="btn-claimed" disabled>
                                    <i class="fas fa-check"></i> Đã Nhận
                                </button>
                            </c:when>

                            <%-- User can claim --%>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/promotions/claim"
                                      method="post" style="display: inline;">
                                    <input type="hidden" name="promotionId" value="${promotion.promotionId}">
                                    <button type="submit" class="btn-claim-promotion">
                                        <i class="fas fa-gift"></i> Nhận Khuyến Mãi
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </c:when>

        <%-- No promotions available --%>
        <c:otherwise>
            <div class="no-promotions">
                <i class="fas fa-gift"></i>
                <h3>Hiện Chưa Có Khuyến Mãi Nào</h3>
                <p>Các chương trình khuyến mãi mới sẽ được cập nhật sớm. Vui lòng quay lại sau!</p>
                <a href="${pageContext.request.contextPath}/cars" class="btn-view-cars mt-3">
                    <i class="fas fa-car"></i> Xem Danh Sách Xe
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto dismiss alerts after 5 seconds
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, 5000);
        });
    });

    // Smooth scroll for internal links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
</script>
</body>
</html>