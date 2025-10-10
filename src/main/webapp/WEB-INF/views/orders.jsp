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
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            padding: 60px 0;
            margin-bottom: 40px;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .page-header p {
            color: #888;
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
            border-radius: 20px;
            padding: 80px 40px;
            text-align: center;
        }

        .empty-state i {
            color: #333;
            margin-bottom: 30px;
        }

        .empty-state h3 {
            color: #f8f9fa;
            margin-bottom: 15px;
        }

        .empty-state p {
            color: #666;
            margin-bottom: 30px;
        }

        .btn-explore {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 40px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
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
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
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
            font-weight: 700;
            padding: 15px;
            border: none;
        }

        .table tbody tr {
            border-bottom: 1px solid #333;
            transition: all 0.3s;
        }

        .table tbody tr:hover {
            background: #252525;
        }

        .table tbody td {
            padding: 15px;
            border: none;
        }

        /* Status Badges */
        .badge {
            padding: 8px 12px;
            font-size: 0.85rem;
            font-weight: 600;
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
        }

        .btn-detail:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.3);
        }

        /* Summary Cards */
        .summary-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
            transition: all 0.3s;
            height: 100%;
        }

        .summary-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
        }

        .summary-card.pending {
            border-color: #f39c12;
        }

        .summary-card.pending:hover {
            box-shadow: 0 10px 30px rgba(243, 156, 18, 0.3);
        }

        .summary-card.completed {
            border-color: #2ecc71;
        }

        .summary-card.completed:hover {
            box-shadow: 0 10px 30px rgba(46, 204, 113, 0.3);
        }

        .summary-card.cancelled {
            border-color: #e74c3c;
        }

        .summary-card.cancelled:hover {
            box-shadow: 0 10px 30px rgba(231, 76, 60, 0.3);
        }

        .summary-card i {
            margin-bottom: 15px;
        }

        .summary-card h5 {
            color: #e0e0e0;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .summary-card h2 {
            font-weight: 700;
            margin: 0;
        }

        .text-warning {
            color: #f39c12 !important;
        }

        .text-success {
            color: #2ecc71 !important;
        }

        .text-danger {
            color: #e74c3c !important;
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container text-center">
        <h1>
            <i class="fas fa-receipt"></i> Đơn Hàng Của Tôi
        </h1>
        <p>Xem và quản lý các đơn hàng của bạn</p>
    </div>
</div>

<!-- Main Content -->
<div class="container my-5">
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
            <i class="fas fa-inbox fa-5x"></i>
            <h3>Chưa Có Đơn Hàng Nào</h3>
            <p>Bạn chưa đặt hàng sản phẩm nào</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-explore">
                <i class="fas fa-search"></i> Khám Phá Xe
            </a>
        </div>
    </c:if>

    <!-- Orders List -->
    <c:if test="${not empty orders}">
        <div class="orders-card mb-4">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th><i class="fas fa-hashtag"></i> Mã Đơn Hàng</th>
                        <th><i class="fas fa-info-circle"></i> Trạng Thái</th>
                        <th><i class="fas fa-calendar"></i> Ngày Đặt</th>
                        <th class="text-center"><i class="fas fa-cog"></i> Thao Tác</th>
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
                                            <i class="fas fa-clock"></i> Chờ Xử Lý
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'APPROVED'}">
                                        <span class="badge bg-info">
                                            <i class="fas fa-check"></i> Đã Duyệt
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-double"></i> Hoàn Thành
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">
                                            <i class="fas fa-times"></i> Đã Hủy
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <i class="far fa-calendar-alt text-muted"></i>
                                <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/order-detail?id=${order.orderId}"
                                   class="btn btn-detail">
                                    <i class="fas fa-eye"></i> Chi Tiết
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Order Summary -->
        <div class="row g-4">
            <div class="col-md-4">
                <div class="summary-card pending">
                    <i class="fas fa-clock fa-3x text-warning"></i>
                    <h5>Chờ Xử Lý</h5>
                    <h2 class="text-warning">
                        <c:set var="pending" value="0" />
                        <c:forEach var="order" items="${orders}">
                            <c:if test="${order.status == 'PENDING'}">
                                <c:set var="pending" value="${pending + 1}" />
                            </c:if>
                        </c:forEach>
                            ${pending}
                    </h2>
                </div>
            </div>
            <div class="col-md-4">
                <div class="summary-card completed">
                    <i class="fas fa-check-circle fa-3x text-success"></i>
                    <h5>Hoàn Thành</h5>
                    <h2 class="text-success">
                        <c:set var="completed" value="0" />
                        <c:forEach var="order" items="${orders}">
                            <c:if test="${order.status == 'COMPLETED'}">
                                <c:set var="completed" value="${completed + 1}" />
                            </c:if>
                        </c:forEach>
                            ${completed}
                    </h2>
                </div>
            </div>
            <div class="col-md-4">
                <div class="summary-card cancelled">
                    <i class="fas fa-times-circle fa-3x text-danger"></i>
                    <h5>Đã Hủy</h5>
                    <h2 class="text-danger">
                        <c:set var="cancelled" value="0" />
                        <c:forEach var="order" items="${orders}">
                            <c:if test="${order.status == 'CANCELLED'}">
                                <c:set var="cancelled" value="${cancelled + 1}" />
                            </c:if>
                        </c:forEach>
                            ${cancelled}
                    </h2>
                </div>
            </div>
        </div>
    </c:if>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>