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
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        footer {
            margin-top: auto;
            background-color: #2f3542;
            color: white;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container my-5">
    <!-- Page Header -->
    <div class="text-center mb-5">
        <h1 class="display-4 fw-bold">
            <i class="fas fa-receipt text-primary"></i> Đơn Hàng Của Tôi
        </h1>
        <p class="lead text-muted">Xem và quản lý các đơn hàng của bạn</p>
    </div>

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
        <div class="text-center py-5">
            <i class="fas fa-inbox fa-5x text-muted mb-4"></i>
            <h3>Chưa Có Đơn Hàng Nào</h3>
            <p class="text-muted mb-4">Bạn chưa đặt hàng sản phẩm nào</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary btn-lg">
                <i class="fas fa-search"></i> Khám Phá Xe
            </a>
        </div>
    </c:if>

    <!-- Orders List -->
    <c:if test="${not empty orders}">
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
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
                                                <span class="badge bg-warning text-dark">
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
                                    <a href="${pageContext.request.contextPath}/orders/detail?id=${order.orderId}"
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="fas fa-eye"></i> Chi Tiết
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Order Summary -->
        <div class="row mt-4">
            <div class="col-md-4">
                <div class="card text-center border-warning">
                    <div class="card-body">
                        <i class="fas fa-clock fa-2x text-warning mb-2"></i>
                        <h5 class="card-title">Chờ Xử Lý</h5>
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
            </div>
            <div class="col-md-4">
                <div class="card text-center border-success">
                    <div class="card-body">
                        <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
                        <h5 class="card-title">Hoàn Thành</h5>
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
            </div>
            <div class="col-md-4">
                <div class="card text-center border-danger">
                    <div class="card-body">
                        <i class="fas fa-times-circle fa-2x text-danger mb-2"></i>
                        <h5 class="card-title">Đã Hủy</h5>
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
        </div>
    </c:if>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>