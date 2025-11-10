<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/9/2025
  Time: 2:44 AM
  To change this template use File | Settings | File Templates.
--%>
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

        .main-container {
            margin-bottom: 3rem;
        }

        /* Content Area */
        .content-area {
            width: 100%;
        }

        /* Notification Banner */
        .notification-banner {
            background: #1a1a1a;
            border: 2px solid #ffd700;
            border-radius: 12px;
            padding: 1.25rem;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .notification-text {
            color: #e0e0e0;
            font-weight: 500;
            font-size: 0.95rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .notification-text i {
            color: #ffd700;
            font-size: 1.2rem;
        }

        .notification-text strong {
            color: #ffd700;
            font-size: 1.1rem;
        }

        .btn-view-my-promotions {
            background: linear-gradient(135deg, #ffd700, #ffed4e);
            color: #1a1a1a;
            padding: 0.6rem 1.25rem;
            border-radius: 20px;
            text-decoration: none;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            white-space: nowrap;
        }

        .btn-view-my-promotions:hover {
            background: linear-gradient(135deg, #ffed4e, #ffd700);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        /* Results Header */
        .results-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding: 1rem 1.25rem;
            background: #1a1a1a;
            border-radius: 10px;
            border: 1px solid #333;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .results-count {
            color: #ffd700;
            font-size: 1rem;
            font-weight: 600;
        }

        .results-count strong {
            font-size: 1.2rem;
        }

        /* Alerts */
        .alert {
            border-radius: 10px;
            border: none;
            padding: 1rem 1.25rem;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert i {
            font-size: 1.2rem;
        }

        .alert-success {
            background: #1a4d2e;
            border-left: 4px solid #28a745;
            color: #5cb85c;
        }

        .alert-danger {
            background: #4d1a1a;
            border-left: 4px solid #dc3545;
            color: #dc3545;
        }

        .alert-info {
            background: #1a3a4d;
            border-left: 4px solid #17a2b8;
            color: #5bc0de;
        }

        /* Promotion Card */
        .promotion-card {
            background: #1a1a1a;
            border: 1px solid #2a2a2a;
            border-radius: 12px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            transition: all 0.3s;
        }

        .promotion-card:hover {
            border-color: #ffd700;
            box-shadow: 0 8px 25px rgba(255, 215, 0, 0.15);
        }

        .promotion-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 1rem;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .promotion-title {
            color: #ffd700;
            font-size: 1.4rem;
            font-weight: 700;
            margin: 0;
            flex: 1;
        }

        .promotion-badge {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 0.4rem 0.9rem;
            border-radius: 15px;
            font-weight: 600;
            font-size: 0.8rem;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .promotion-description {
            color: #b0b0b0;
            line-height: 1.6;
            margin-bottom: 1rem;
            font-size: 0.95rem;
        }

        .promotion-meta {
            display: flex;
            gap: 1.5rem;
            margin-bottom: 1.25rem;
            padding: 1rem;
            background: #0a0a0a;
            border-radius: 8px;
            flex-wrap: wrap;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 6px;
            color: #b0b0b0;
            font-size: 0.9rem;
        }

        .meta-item i {
            color: #ffd700;
            font-size: 1rem;
        }

        /* Cars Section */
        .cars-section {
            margin-top: 1.25rem;
            padding: 1.25rem;
            background: #0a0a0a;
            border-radius: 10px;
        }

        .cars-section-title {
            color: #f8f9fa;
            font-weight: 600;
            margin-bottom: 1rem;
            font-size: 1rem;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .cars-section-title i {
            color: #ffd700;
        }

        .car-item {
            background: #1a1a1a;
            border: 1px solid #2a2a2a;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
            transition: all 0.3s;
        }

        .car-item:hover {
            border-color: #ffd700;
            box-shadow: 0 4px 15px rgba(255, 215, 0, 0.1);
        }

        .car-item-content {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 1rem;
        }

        .car-info {
            flex: 1;
        }

        .car-name {
            color: #f8f9fa;
            font-weight: 600;
            margin: 0 0 0.5rem 0;
            font-size: 1rem;
        }

        .car-details {
            color: #888;
            font-size: 0.85rem;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .car-details span {
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }

        .car-discount-badge {
            display: inline-block;
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 0.3rem 0.7rem;
            border-radius: 12px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .car-price-section {
            text-align: right;
            min-width: 140px;
        }

        .car-price-original {
            color: #666;
            text-decoration: line-through;
            font-size: 0.85rem;
            margin-bottom: 0.3rem;
        }

        .car-price-discounted {
            color: #dc3545;
            font-weight: 700;
            font-size: 1.1rem;
            margin-bottom: 0.3rem;
        }

        .car-price-normal {
            color: #28a745;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .car-savings {
            color: #28a745;
            font-size: 0.8rem;
            font-weight: 500;
        }

        /* Action Buttons */
        .action-buttons {
            margin-top: 1.25rem;
            display: flex;
            gap: 0.75rem;
            flex-wrap: wrap;
        }

        .btn-view-cars,
        .btn-claim-promotion,
        .btn-login,
        .btn-claimed,
        .btn-claimed-used {
            padding: 0.65rem 1.25rem;
            font-size: 0.9rem;
            border-radius: 20px;
            font-weight: 600;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            border: none;
            cursor: pointer;
        }

        .btn-view-cars {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
        }

        .btn-view-cars:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-claim-promotion {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
        }

        .btn-claim-promotion:hover {
            background: linear-gradient(135deg, #20c997 0%, #28a745 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4);
            color: white;
        }

        .btn-login {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            color: white;
        }

        .btn-login:hover {
            background: linear-gradient(135deg, #0056b3 0%, #007bff 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 123, 255, 0.4);
            color: white;
        }

        .btn-claimed,
        .btn-claimed-used {
            background: #6c757d;
            color: white;
            cursor: not-allowed;
            opacity: 0.7;
        }

        /* Empty State */
        .no-promotions {
            text-align: center;
            padding: 4rem 2rem;
            background: #1a1a1a;
            border-radius: 12px;
            border: 1px solid #2a2a2a;
        }

        .no-promotions i {
            font-size: 4rem;
            color: #444;
            margin-bottom: 1.5rem;
        }

        .no-promotions h3 {
            color: #888;
            margin-bottom: 1rem;
            font-size: 1.4rem;
        }

        .no-promotions p {
            color: #666;
            margin-bottom: 1.5rem;
        }

        footer {
            margin-top: auto;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .page-header h1 {
                font-size: 1.5rem;
            }

            .page-header .subtitle {
                font-size: 0.9rem;
            }

            .notification-banner {
                flex-direction: column;
                text-align: center;
            }

            .btn-view-my-promotions {
                width: 100%;
                justify-content: center;
            }

            .promotion-header {
                flex-direction: column;
            }

            .promotion-title {
                font-size: 1.2rem;
            }

            .promotion-meta {
                flex-direction: column;
                gap: 0.75rem;
            }

            .car-item-content {
                flex-direction: column;
            }

            .car-price-section {
                text-align: left;
                width: 100%;
            }

            .action-buttons {
                flex-direction: column;
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

        @media (max-width: 576px) {
            .main-container {
                gap: 1rem;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="main-container">
        <!-- Content Area -->
        <div class="content-area">

            <!-- Alerts -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i>
                    <span>${sessionScope.successMessage}</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${sessionScope.errorMessage}</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.infoMessage}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <i class="fas fa-info-circle"></i>
                    <span>${sessionScope.infoMessage}</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="infoMessage" scope="session"/>
            </c:if>

            <!-- Promotions List -->
            <c:choose>
                <c:when test="${not empty promotions}">
                    <c:forEach var="promotion" items="${promotions}">
                        <div class="promotion-card">
                            <!-- Header -->
                            <div class="promotion-header">
                                <h2 class="promotion-title">${promotion.title}</h2>
                            </div>

                            <!-- Description -->
                            <p class="promotion-description">${promotion.description}</p>

                            <!-- Meta Info -->
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

                            <!-- Cars Section -->
                            <c:if test="${not empty promotion.applicableCars}">
                                <div class="cars-section">
                                    <div class="cars-section-title">
                                        <i class="fas fa-car"></i>
                                        <span>Xe Áp Dụng Khuyến Mãi (${promotion.applicableCars.size()} xe)</span>
                                    </div>

                                    <c:forEach var="car" items="${promotion.applicableCars}">
                                        <div class="car-item">
                                            <div class="car-item-content">
                                                <div class="car-info">
                                                    <h5 class="car-name">${car.brandName} ${car.name}</h5>
                                                    <div class="car-details">
                                                        <span><i class="far fa-calendar"></i> ${car.year}</span>
                                                        <span><i class="fas fa-palette"></i> ${car.color}</span>
                                                    </div>
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

                                                <div class="car-price-section">
                                                    <c:choose>
                                                        <c:when test="${car.hasDiscount()}">
                                                            <div class="car-price-original">
                                                                <fmt:formatNumber value="${car.price}" type="number" maxFractionDigits="0"/>₫
                                                            </div>
                                                            <div class="car-price-discounted">
                                                                <fmt:formatNumber value="${car.discountedPrice}" type="number" maxFractionDigits="0"/>₫
                                                            </div>
                                                            <div class="car-savings">
                                                                Tiết kiệm: <fmt:formatNumber value="${car.discountValue}" type="number" maxFractionDigits="0"/>₫
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
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
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <div class="no-promotions">
                        <i class="fas fa-gift"></i>
                        <h3>Không Tìm Thấy Khuyến Mãi Nào</h3>
                        <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn-view-cars">Quay Lại
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto dismiss alerts
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, 5000);
        });
    });
</script>
</body>
</html>
