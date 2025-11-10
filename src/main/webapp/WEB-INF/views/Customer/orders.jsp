<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn Hàng - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
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

        /* Alerts */
        .alert-success {
            background: rgba(46, 204, 113, 0.1);
            border: 1px solid rgba(46, 204, 113, 0.3);
            color: #2ecc71;
            border-radius: 10px;
        }

        .alert-danger {
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid rgba(231, 76, 60, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
        }

        .btn-close {
            filter: invert(1);
        }

        /* Empty State */
        .empty-state {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 60px 40px;
            text-align: center;
        }

        .empty-state i {
            color: #444;
            margin-bottom: 25px;
        }

        .empty-state h3 {
            color: #f8f9fa;
            margin-bottom: 12px;
            font-size: 1.5rem;
        }

        .empty-state p {
            color: #888;
            margin-bottom: 25px;
        }

        .btn-explore {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 25px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-explore:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        /* Order Card */
        .order-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
            margin-bottom: 20px;
            transition: all 0.3s;
        }

        .order-card:hover {
            border-color: #ffd700;
            box-shadow: 0 5px 20px rgba(255, 215, 0, 0.15);
        }

        /* Order Header */
        .order-header {
            background: linear-gradient(135deg, #2d2d2d 0%, #1a1a1a 100%);
            padding: 15px 20px;
            border-bottom: 1px solid #333;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .order-id {
            color: #ffd700;
            font-weight: 700;
            font-size: 1.1rem;
            margin: 0;
        }

        .order-meta {
            display: flex;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        .order-date {
            color: #888;
            font-size: 0.9rem;
        }

        .order-date i {
            margin-right: 5px;
        }

        /* Status Badges */
        .badge {
            padding: 6px 12px;
            font-size: 0.85rem;
            font-weight: 500;
            border-radius: 20px;
        }

        .badge.bg-warning {
            background: #f39c12 !important;
            color: #1a1a1a !important;
        }

        .badge.bg-info {
            background: #3498db !important;
        }

        .badge.bg-success {
            background: #2ecc71 !important;
        }

        .badge.bg-danger {
            background: #e74c3c !important;
        }

        /* Order Body */
        .order-body {
            padding: 20px;
        }

        /* Car Items */
        .car-item {
            display: flex;
            gap: 15px;
            padding: 15px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-bottom: 12px;
            transition: all 0.3s;
        }

        .car-item:last-child {
            margin-bottom: 0;
        }

        .car-item:hover {
            background: #1a1a1a;
        }

        .car-image {
            width: 100px;
            height: 70px;
            object-fit: cover;
            border-radius: 8px;
            border: 1px solid #333;
            flex-shrink: 0;
        }

        .car-image-placeholder {
            width: 100px;
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #2a2a2a;
            border-radius: 8px;
            border: 1px solid #333;
            color: #555;
            font-size: 2rem;
            flex-shrink: 0;
        }

        .car-info {
            flex: 1;
            min-width: 0;
        }

        .car-name {
            color: #f8f9fa;
            font-weight: 600;
            font-size: 1rem;
            margin-bottom: 5px;
            display: -webkit-box;
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .car-details {
            color: #888;
            font-size: 0.85rem;
            margin-bottom: 8px;
        }

        .car-details span {
            margin-right: 12px;
            white-space: nowrap;
        }

        .car-details i {
            margin-right: 4px;
            color: #ffd700;
        }

        .car-price-quantity {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
        }

        .car-price {
            color: #ffd700;
            font-weight: 600;
            font-size: 1rem;
        }

        .car-quantity {
            color: #888;
            font-size: 0.9rem;
        }

        /* More Items Badge */
        .more-items-badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 15px;
            background: #2a2a2a;
            border-radius: 8px;
            color: #888;
            font-size: 0.9rem;
        }

        .more-items-badge i {
            color: #ffd700;
        }

        /* Order Footer */
        .order-footer {
            background: #0f0f0f;
            padding: 15px 20px;
            border-top: 1px solid #333;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        .order-total {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .total-label {
            color: #888;
            font-size: 0.85rem;
        }

        .total-amount {
            color: #ffd700;
            font-weight: 700;
            font-size: 1.3rem;
        }

        .btn-detail {
            background: transparent;
            border: 1px solid #ffd700;
            color: #ffd700;
            padding: 8px 20px;
            border-radius: 8px;
            transition: all 0.3s;
            font-size: 0.9rem;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-weight: 500;
        }

        .btn-detail:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.3);
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

            .order-header {
                padding: 12px 15px;
            }

            .order-id {
                font-size: 1rem;
            }

            .order-meta {
                width: 100%;
                justify-content: space-between;
            }

            .car-item {
                flex-direction: column;
                gap: 12px;
            }

            .car-image,
            .car-image-placeholder {
                width: 100%;
                height: 150px;
            }

            .car-price-quantity {
                flex-direction: column;
                align-items: flex-start;
            }

            .order-footer {
                flex-direction: column;
                align-items: stretch;
            }

            .btn-detail {
                width: 100%;
                justify-content: center;
            }

            .empty-state {
                padding: 40px 20px;
            }

            .empty-state h3 {
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
        <h1><i class="fas fa-receipt"></i> Đơn Hàng Của Tôi</h1>
        <p class="subtitle">Xem và quản lý các đơn hàng của bạn</p>
    </div>
</div>

<!-- Main Content -->
<div class="container my-4">
    <!-- Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- No Orders -->
    <c:if test="${empty orders}">
        <div class="empty-state">
            <i class="fas fa-inbox fa-4x"></i>
            <h3>Chưa có đơn hàng nào</h3>
            <p>Bạn chưa đặt hàng sản phẩm nào</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn-explore">Khám phá xe</a>
        </div>
    </c:if>

    <!-- Orders List -->
    <c:if test="${not empty orders}">
        <c:forEach var="order" items="${orders}">
            <div class="order-card">
                <!-- Order Header -->
                <div class="order-header">
                    <h3 class="order-id">#${order.orderId}</h3>
                    <div class="order-meta">
                        <span class="order-date">
                            <i class="far fa-calendar-alt"></i>
                            <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                        </span>
                        <c:choose>
                            <c:when test="${order.status == 'PENDING'}">
                                <span class="badge bg-warning">
                                    <i class="fas fa-clock"></i> Chờ xử lý
                                </span>
                            </c:when>
                            <c:when test="${order.status == 'APPROVED'}">
                                <span class="badge bg-info">
                                    <i class="fas fa-check"></i> Đã duyệt
                                </span>
                            </c:when>
                            <c:when test="${order.status == 'COMPLETED'}">
                                <span class="badge bg-success">
                                    <i class="fas fa-check-double"></i> Hoàn thành
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger">
                                    <i class="fas fa-times"></i> Đã hủy
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Order Body -->
                <div class="order-body">
                    <c:choose>
                        <c:when test="${not empty order.orderDetails}">
                            <!-- Show first 2 cars -->
                            <c:forEach var="detail" items="${order.orderDetails}" begin="0" end="1">
                                <div class="car-item">
                                    <!-- Car Image -->
                                    <c:choose>
                                        <c:when test="${not empty detail.car.imageUrl}">
                                            <c:set var="imagePath" value="${detail.car.imageUrl}" />
                                            <c:choose>
                                                <%-- External URL (http/https) - Direct use --%>
                                                <c:when test="${imagePath.startsWith('http://') or imagePath.startsWith('https://')}">
                                                    <img src="${imagePath}"
                                                         alt="${detail.car.brandName} ${detail.car.name}"
                                                         class="car-image"
                                                         loading="lazy"
                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                    <div class="car-image-placeholder" style="display: none;">
                                                        <i class="fas fa-car"></i>
                                                    </div>
                                                </c:when>
                                                <%-- Local path with leading slash --%>
                                                <c:when test="${imagePath.startsWith('/')}">
                                                    <img src="${pageContext.request.contextPath}${imagePath}"
                                                         alt="${detail.car.brandName} ${detail.car.name}"
                                                         class="car-image"
                                                         loading="lazy"
                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                    <div class="car-image-placeholder" style="display: none;">
                                                        <i class="fas fa-car"></i>
                                                    </div>
                                                </c:when>
                                                <%-- Local path without leading slash --%>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/${imagePath}"
                                                         alt="${detail.car.brandName} ${detail.car.name}"
                                                         class="car-image"
                                                         loading="lazy"
                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                                    <div class="car-image-placeholder" style="display: none;">
                                                        <i class="fas fa-car"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="car-image-placeholder">
                                                <i class="fas fa-car"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Car Info -->
                                    <div class="car-info">
                                        <div class="car-name">
                                                ${detail.car.brandName} ${detail.car.name}
                                        </div>
                                        <div class="car-details">
                                            <c:if test="${not empty detail.car.year}">
                                                <span><i class="fas fa-calendar"></i> ${detail.car.year}</span>
                                            </c:if>
                                            <c:if test="${not empty detail.car.color}">
                                                <span><i class="fas fa-palette"></i> ${detail.car.color}</span>
                                            </c:if>
                                        </div>
                                        <div class="car-price-quantity">
                                            <div class="car-price">
                                                <fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true"/>₫
                                            </div>
                                            <div class="car-quantity">
                                                <i class="fas fa-box"></i> Số lượng: ${detail.quantity}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                            <!-- Show "and X more" badge if there are more than 2 items -->
                            <c:if test="${order.orderDetails.size() > 2}">
                                <div class="more-items-badge">
                                    <i class="fas fa-ellipsis-h"></i>
                                    <span>và ${order.orderDetails.size() - 2} xe khác</span>
                                </div>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center text-muted py-3">
                                <i class="fas fa-box-open"></i> Không có sản phẩm
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Order Footer -->
                <div class="order-footer">
                    <div class="order-total">
                        <div class="total-label">Tổng thanh toán</div>
                        <div class="total-amount">
                            <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/>₫
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/order-detail?id=${order.orderId}"
                       class="btn-detail">
                        <i class="fas fa-eye"></i>
                        <span>Xem chi tiết</span>
                    </a>
                </div>
            </div>
        </c:forEach>
    </c:if>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
