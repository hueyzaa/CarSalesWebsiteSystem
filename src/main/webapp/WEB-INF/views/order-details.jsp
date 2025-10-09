<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Đơn Hàng - Car Showroom</title>
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
        .status-timeline {
            position: relative;
            padding: 20px 0;
        }
        .status-step {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            position: relative;
        }
        .status-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            z-index: 1;
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
        <h1 class="display-5 fw-bold">
            <i class="fas fa-file-invoice text-primary"></i> Chi Tiết Đơn Hàng
        </h1>
        <p class="text-muted">Thông tin chi tiết về đơn hàng #${order.orderId}</p>
    </div>

    <div class="row g-4">
        <!-- Order Information -->
        <div class="col-lg-8">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-info-circle"></i> Thông Tin Đơn Hàng
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <div class="d-flex justify-content-between border-bottom pb-2">
                                <span class="text-muted">
                                    <i class="fas fa-hashtag"></i> Mã đơn hàng:
                                </span>
                                <span class="fw-bold">#${order.orderId}</span>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="d-flex justify-content-between border-bottom pb-2">
                                <span class="text-muted">
                                    <i class="fas fa-calendar"></i> Ngày đặt:
                                </span>
                                <span class="fw-bold">
                                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="d-flex justify-content-between border-bottom pb-2">
                                <span class="text-muted">
                                    <i class="fas fa-user"></i> Khách hàng:
                                </span>
                                <span class="fw-bold">${sessionScope.userName}</span>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="d-flex justify-content-between border-bottom pb-2">
                                <span class="text-muted">
                                    <i class="fas fa-info-circle"></i> Trạng thái:
                                </span>
                                <span>
                                    <c:choose>
                                        <c:when test="${order.status == 'PENDING'}">
                                            <span class="badge bg-warning text-dark fs-6">
                                                <i class="fas fa-clock"></i> Chờ Xử Lý
                                            </span>
                                        </c:when>
                                        <c:when test="${order.status == 'APPROVED'}">
                                            <span class="badge bg-info fs-6">
                                                <i class="fas fa-check"></i> Đã Duyệt
                                            </span>
                                        </c:when>
                                        <c:when test="${order.status == 'COMPLETED'}">
                                            <span class="badge bg-success fs-6">
                                                <i class="fas fa-check-double"></i> Hoàn Thành
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger fs-6">
                                                <i class="fas fa-times"></i> Đã Hủy
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Items (Placeholder - would need OrderDetail items) -->
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-shopping-cart"></i> Sản Phẩm Trong Đơn
                    </h5>
                </div>
                <div class="card-body">
                    <div class="alert alert-info mb-0">
                        <i class="fas fa-info-circle"></i>
                        Chi tiết sản phẩm sẽ được hiển thị khi có dữ liệu OrderDetail
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Status Timeline -->
        <div class="col-lg-4">
            <div class="card shadow-sm">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-history"></i> Trạng Thái Đơn Hàng
                    </h5>
                </div>
                <div class="card-body">
                    <div class="status-timeline">
                        <!-- Pending -->
                        <div class="status-step">
                            <div class="status-icon ${order.status == 'PENDING' || order.status == 'APPROVED' || order.status == 'COMPLETED' ? 'bg-warning' : 'bg-secondary'} text-white">
                                <i class="fas fa-clock"></i>
                            </div>
                            <div class="ms-3">
                                <h6 class="mb-0">Chờ Xử Lý</h6>
                                <small class="text-muted">Đơn hàng đang chờ được xác nhận</small>
                            </div>
                        </div>

                        <!-- Approved -->
                        <div class="status-step">
                            <div class="status-icon ${order.status == 'APPROVED' || order.status == 'COMPLETED' ? 'bg-info' : 'bg-secondary'} text-white">
                                <i class="fas fa-check"></i>
                            </div>
                            <div class="ms-3">
                                <h6 class="mb-0">Đã Duyệt</h6>
                                <small class="text-muted">Đơn hàng đã được xác nhận</small>
                            </div>
                        </div>

                        <!-- Completed -->
                        <div class="status-step">
                            <div class="status-icon ${order.status == 'COMPLETED' ? 'bg-success' : 'bg-secondary'} text-white">
                                <i class="fas fa-check-double"></i>
                            </div>
                            <div class="ms-3">
                                <h6 class="mb-0">Hoàn Thành</h6>
                                <small class="text-muted">Đơn hàng đã hoàn tất</small>
                            </div>
                        </div>

                        <!-- Cancelled (if applicable) -->
                        <c:if test="${order.status == 'CANCELLED'}">
                            <div class="status-step">
                                <div class="status-icon bg-danger text-white">
                                    <i class="fas fa-times"></i>
                                </div>
                                <div class="ms-3">
                                    <h6 class="mb-0">Đã Hủy</h6>
                                    <small class="text-muted">Đơn hàng đã bị hủy</small>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Actions -->
            <div class="card shadow-sm mt-4">
                <div class="card-header bg-secondary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-cog"></i> Hành Động
                    </h5>
                </div>
                <div class="card-body">
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
                            <i class="fas fa-arrow-left"></i> Quay Lại Danh Sách
                        </a>

                        <c:if test="${order.status == 'PENDING'}">
                            <button class="btn btn-danger" onclick="confirmCancel()">
                                <i class="fas fa-times-circle"></i> Hủy Đơn Hàng
                            </button>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-info">
                            <i class="fas fa-headset"></i> Liên Hệ Hỗ Trợ
                        </a>
                    </div>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="card shadow-sm mt-4 border-success">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-calculator"></i> Tóm Tắt Đơn Hàng
                    </h5>
                </div>
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tạm tính:</span>
                        <span class="fw-bold">--</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Phí vận chuyển:</span>
                        <span class="text-success">Miễn phí</span>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between">
                        <span class="fw-bold fs-5">Tổng cộng:</span>
                        <span class="fw-bold fs-5 text-success">--</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function confirmCancel() {
        if (confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')) {
            // In real implementation, would send request to cancel order
            alert('Tính năng hủy đơn hàng đang được phát triển!');
        }
    }
</script>
</body>
</html>