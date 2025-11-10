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

        /* Orders Table */
        .orders-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
            margin-bottom: 30px;
        }

        .table {
            color: #e0e0e0;
            margin: 0;
        }

        .table thead {
            background: #0f0f0f;
            border-bottom: 2px solid #ffd700;
        }

        .table thead th {
            color: #ffd700;
            font-weight: 600;
            padding: 15px;
            border: none;
            font-size: 0.95rem;
        }

        .table tbody tr {
            border-bottom: 1px solid #2a2a2a;
            transition: all 0.3s;
        }

        .table tbody tr:hover {
            background: #222;
        }

        .table tbody td {
            padding: 15px;
            border: none;
            vertical-align: middle;
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

        /* Action Button */
        .btn-detail {
            background: transparent;
            border: 1px solid #ffd700;
            color: #ffd700;
            padding: 6px 15px;
            border-radius: 8px;
            transition: all 0.3s;
            font-size: 0.9rem;
            text-decoration: none;
            display: inline-block;
        }

        .btn-detail:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-2px);
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

            .table thead th {
                font-size: 0.85rem;
                padding: 12px 8px;
            }

            .table tbody td {
                padding: 12px 8px;
                font-size: 0.9rem;
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
        <div class="orders-card">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>Mã đơn hàng</th>
                        <th>Trạng thái</th>
                        <th>Ngày đặt</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td class="fw-bold">#${order.orderId}</td>
                            <td>
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
                            </td>
                            <td>
                                <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/order-detail?id=${order.orderId}"
                                   class="btn-detail">
                                    <i class="fas fa-eye"></i> Chi tiết
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
