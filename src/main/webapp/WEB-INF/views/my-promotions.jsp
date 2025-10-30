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

        /* Page Header */
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

        /* Promotion Cards */
        .promotion-card {
            background: #1a1a1a;
            border: 2px solid #333;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
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
            border-color: #ffd700;
        }

        .promotion-card.used:hover {
            border-color: #888;
        }

        .status-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 6px 14px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.85rem;
        }

        .status-badge.unused {
            background: #28a745;
            color: white;
        }

        .status-badge.used {
            background: #6c757d;
            color: white;
        }

        .promotion-title {
            color: #ffd700;
            font-size: 1.4rem;
            font-weight: 600;
            margin-bottom: 12px;
            padding-right: 120px;
        }

        .promotion-description {
            color: #b0b0b0;
            line-height: 1.6;
            margin-bottom: 18px;
            font-size: 0.95rem;
        }

        .promotion-meta {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
            color: #888;
            font-size: 0.9rem;
            padding-bottom: 15px;
            border-bottom: 1px solid #2a2a2a;
            margin-bottom: 15px;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .meta-item i {
            color: #ffd700;
        }

        .discount-info {
            background: linear-gradient(135deg, #ff6b6b, #ee5a6f);
            color: white;
            padding: 10px 18px;
            border-radius: 10px;
            display: inline-block;
            font-weight: 600;
            font-size: 0.95rem;
        }

        .use-promotion-hint {
            display: inline-block;
            margin-top: 12px;
            padding: 8px 16px;
            background: rgba(255, 215, 0, 0.1);
            border: 1px solid #ffd700;
            border-radius: 10px;
            color: #ffd700;
            font-size: 0.85rem;
            font-weight: 500;
        }

        /* Empty State */
        .no-promotions {
            text-align: center;
            padding: 60px 20px;
            background: #1a1a1a;
            border-radius: 15px;
            border: 1px solid #333;
        }

        .no-promotions i {
            font-size: 4rem;
            color: #444;
            margin-bottom: 20px;
        }

        .no-promotions h3 {
            color: #f8f9fa;
            margin-bottom: 12px;
            font-size: 1.5rem;
        }

        .no-promotions p {
            color: #888;
            margin-bottom: 25px;
        }

        .btn-browse-promotions {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 12px 28px;
            font-size: 0.95rem;
            border-radius: 25px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
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

        /* Responsive */
        @media (max-width: 768px) {
            .page-header h1 {
                font-size: 1.5rem;
            }

            .page-header .subtitle {
                font-size: 0.9rem;
            }

            .promotion-title {
                font-size: 1.2rem;
                padding-right: 0;
            }

            .status-badge {
                position: static;
                display: inline-block;
                margin-bottom: 12px;
            }

            .promotion-meta {
                flex-direction: column;
                gap: 10px;
            }

            .no-promotions {
                padding: 40px 20px;
            }

            .no-promotions h3 {
                font-size: 1.3rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-gift"></i> Khuyến Mãi Của Tôi</h1>
        <p class="subtitle">Quản lý các chương trình khuyến mãi bạn đã nhận</p>
    </div>
</div>

<div class="container my-4">
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
                                        <i class="fas fa-check-double"></i> Đã sử dụng
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-check"></i> Có thể dùng
                                    </c:otherwise>
                                </c:choose>
                            </span>

                            <h3 class="promotion-title">${promotion.title}</h3>
                            <p class="promotion-description">${promotion.description}</p>

                            <div class="promotion-meta">
                                <div class="meta-item">
                                    <i class="far fa-calendar"></i>
                                    <span><fmt:formatDate value="${promotion.startDate}" pattern="dd/MM/yyyy"/> - <fmt:formatDate value="${promotion.endDate}" pattern="dd/MM/yyyy"/></span>
                                </div>
                                <div class="meta-item">
                                    <i class="far fa-clock"></i>
                                    <c:choose>
                                        <c:when test="${promotion.expired}">
                                            <span style="color: #e74c3c;">Đã hết hạn</span>
                                        </c:when>
                                        <c:when test="${promotion.active}">
                                            <span style="color: #2ecc71;">Đang hiệu lực</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #f39c12;">Sắp diễn ra</span>
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

            <!-- Back Button -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/promotions" class="btn-browse-promotions">
                    <i class="fas fa-arrow-left"></i> Quay lại khuyến mãi
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-promotions">
                <i class="fas fa-ticket-alt"></i>
                <h3>Bạn chưa nhận khuyến mãi nào</h3>
                <p>Hãy khám phá các chương trình khuyến mãi hấp dẫn của chúng tôi!</p>
                <a href="${pageContext.request.contextPath}/promotions" class="btn-browse-promotions">
                    <i class="fas fa-search"></i> Xem khuyến mãi
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
