<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khuyến Mãi Của Tôi - Car Showroom</title>
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

        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 60px 0;
            margin-bottom: 40px;
            border-bottom: 3px solid #ffd700;
        }

        .page-header h1 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 10px;
            font-size: 2.5rem;
        }

        .page-header p {
            color: #b0b0b0;
            font-size: 1.1rem;
            margin: 0;
        }

        .stats-card {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
            padding: 25px;
            border-radius: 15px;
            text-align: center;
            margin-bottom: 30px;
            transition: transform 0.3s;
            cursor: default;
        }

        .stats-card:hover {
            transform: translateY(-5px);
        }

        .stats-card h3 {
            font-size: 2.5rem;
            font-weight: 700;
            margin: 0;
        }

        .stats-card p {
            margin: 0;
            font-weight: 600;
            font-size: 1rem;
        }

        .stats-card.unused {
            background: linear-gradient(135deg, #28a745, #20c997);
        }

        .stats-card.used {
            background: linear-gradient(135deg, #6c757d, #5a6268);
        }

        .stats-card.unused h3,
        .stats-card.unused p,
        .stats-card.used h3,
        .stats-card.used p {
            color: white;
        }

        .promotion-card {
            background: #1a1a1a;
            border: 2px solid #333;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 25px;
            transition: all 0.3s;
            position: relative;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
            display: block;
        }

        .promotion-card.unused {
            border-color: #28a745;
        }

        .promotion-card.used {
            border-color: #6c757d;
            opacity: 0.7;
        }

        .promotion-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.3);
            border-color: #ffd700;
        }

        .promotion-card.used:hover {
            border-color: #888;
        }

        .status-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 700;
            font-size: 0.85rem;
            z-index: 1;
        }

        .status-badge.unused {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
        }

        .status-badge.used {
            background: #6c757d;
            color: white;
        }

        .promotion-title {
            color: #ffd700;
            font-size: 1.6rem;
            font-weight: 700;
            margin-bottom: 12px;
            padding-right: 140px;
        }

        .promotion-description {
            color: #b0b0b0;
            line-height: 1.7;
            margin-bottom: 20px;
            font-size: 1rem;
        }

        .promotion-meta {
            display: flex;
            gap: 25px;
            flex-wrap: wrap;
            color: #888;
            font-size: 0.95rem;
            padding-bottom: 15px;
            border-bottom: 1px solid #333;
            margin-bottom: 15px;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .meta-item i {
            color: #ffd700;
            font-size: 1.1rem;
        }

        .discount-info {
            background: linear-gradient(135deg, #ff6b6b, #ee5a6f);
            color: white;
            padding: 12px 20px;
            border-radius: 10px;
            display: inline-block;
            font-weight: 600;
            font-size: 1rem;
        }

        .discount-info i {
            margin-right: 5px;
        }

        .use-promotion-hint {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            background: rgba(255, 215, 0, 0.1);
            border: 1px solid #ffd700;
            border-radius: 10px;
            color: #ffd700;
            font-size: 0.9rem;
            font-weight: 600;
        }

        .use-promotion-hint i {
            margin-right: 8px;
        }

        .no-promotions {
            text-align: center;
            padding: 80px 20px;
            background: #1a1a1a;
            border-radius: 15px;
            border: 2px dashed #333;
        }

        .no-promotions i {
            font-size: 5rem;
            color: #444;
            margin-bottom: 20px;
        }

        .no-promotions h3 {
            color: #888;
            margin-bottom: 15px;
            font-size: 1.8rem;
        }

        .no-promotions p {
            color: #666;
            font-size: 1.1rem;
        }

        .btn-browse-promotions {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 12px 30px;
            font-size: 1rem;
            border-radius: 25px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .btn-browse-promotions:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        footer {
            margin-top: auto;
        }

        @media (max-width: 768px) {
            .page-header h1 {
                font-size: 2rem;
            }

            .promotion-title {
                font-size: 1.3rem;
                padding-right: 0;
            }

            .status-badge {
                position: static;
                display: inline-block;
                margin-bottom: 15px;
            }

            .stats-card h3 {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-gift"></i> Khuyến Mãi Của Tôi</h1>
        <p>Quản lý các chương trình khuyến mãi bạn đã nhận</p>
    </div>
</div>

<div class="container my-5">
    <!-- Statistics Cards -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="stats-card">
                <h3>${claimedPromotions.size()}</h3>
                <p><i class="fas fa-ticket-alt"></i> Tổng Khuyến Mãi</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card unused">
                <h3>${unusedCount}</h3>
                <p><i class="fas fa-check-circle"></i> Chưa Sử Dụng</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card used">
                <h3>${usedCount}</h3>
                <p><i class="fas fa-check-double"></i> Đã Sử Dụng</p>
            </div>
        </div>
    </div>

    <!-- Promotions List -->
    <c:choose>
        <c:when test="${not empty claimedPromotions}">
            <div class="row">
                <c:forEach var="promotion" items="${claimedPromotions}">
                    <div class="col-12">
                        <a href="${pageContext.request.contextPath}/checkout"
                           class="promotion-card ${promotion.usedByUser ? 'used' : 'unused'}"
                           onclick="${promotion.usedByUser || !promotion.active ? 'return false;' : ''}"
                           style="${promotion.usedByUser || !promotion.active ? 'cursor: not-allowed;' : ''}">

                            <span class="status-badge ${promotion.usedByUser ? 'used' : 'unused'}">
                                <c:choose>
                                    <c:when test="${promotion.usedByUser}">
                                        <i class="fas fa-check-double"></i> Đã Sử Dụng
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-check"></i> Có Thể Dùng
                                    </c:otherwise>
                                </c:choose>
                            </span>

                            <h3 class="promotion-title">${promotion.title}</h3>
                            <p class="promotion-description">${promotion.description}</p>

                            <div class="promotion-meta">
                                <div class="meta-item">
                                    <i class="far fa-calendar-alt"></i>
                                    <span>Từ <fmt:formatDate value="${promotion.startDate}" pattern="dd/MM/yyyy"/></span>
                                </div>
                                <div class="meta-item">
                                    <i class="far fa-calendar-check"></i>
                                    <span>Đến <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy"/></span>
                                </div>
                                <div class="meta-item">
                                    <i class="far fa-clock"></i>
                                    <c:choose>
                                        <c:when test="${promotion.expired}">
                                            <span style="color: #dc3545;">Đã hết hạn</span>
                                        </c:when>
                                        <c:when test="${promotion.active}">
                                            <span style="color: #28a745;">Đang hiệu lực</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #ffc107;">Sắp diễn ra</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <c:if test="${promotion.discountPercentage > 0 || promotion.discountAmount > 0}">
                                <div class="discount-info">
                                    <i class="fas fa-tag"></i>
                                    <c:choose>
                                        <c:when test="${promotion.discountPercentage > 0}">
                                            Giảm giá ${promotion.discountPercentage}%
                                        </c:when>
                                        <c:otherwise>
                                            Giảm <fmt:formatNumber value="${promotion.discountAmount}" type="currency" currencySymbol="₫"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>

                            <!-- Hint for unused promotions -->
                            <c:if test="${!promotion.usedByUser && promotion.active}">
                                <div class="use-promotion-hint">
                                    <i class="fas fa-hand-pointer"></i>
                                    Click để sử dụng khuyến mãi này khi thanh toán
                                </div>
                            </c:if>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-promotions">
                <i class="fas fa-ticket-alt"></i>
                <h3>Bạn Chưa Nhận Khuyến Mãi Nào</h3>
                <p>Hãy khám phá các chương trình khuyến mãi hấp dẫn của chúng tôi!</p>
                <a href="${pageContext.request.contextPath}/promotions" class="btn-browse-promotions">
                    <i class="fas fa-search"></i> Xem Khuyến Mãi
                </a>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Back to Promotions Button -->
    <c:if test="${not empty claimedPromotions}">
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/promotions" class="btn-browse-promotions">
                <i class="fas fa-arrow-left"></i> Quay Lại Khuyến Mãi
            </a>
        </div>
    </c:if>
</div>

<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>