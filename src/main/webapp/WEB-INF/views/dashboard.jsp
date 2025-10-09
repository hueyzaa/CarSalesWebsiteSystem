<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng Điều Khiển Admin - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .action-card {
            transition: transform 0.3s, box-shadow 0.3s;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            display: block;
            height: 100%;
        }
        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(102,126,234,0.4);
            color: white;
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
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
            <i class="fas fa-car"></i> Car Showroom
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                        <i class="fas fa-car"></i> Xe Hơi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/add-car">
                        <i class="fas fa-plus-circle"></i> Thêm Xe
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user-shield"></i> ${sessionScope.userName}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><span class="dropdown-item-text"><small>Role: ADMIN</small></span></li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt"></i> Đăng Xuất
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="container my-5">
    <!-- Page Header -->
    <div class="text-center mb-5">
        <h1 class="display-4 fw-bold">
            <i class="fas fa-tachometer-alt text-primary"></i> Bảng Điều Khiển Admin
        </h1>
        <p class="lead text-muted">Quản lý xe, đơn hàng và người dùng</p>
    </div>

    <!-- Success Message -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <!-- Error Message -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Admin Actions -->
    <div class="row g-4">
        <div class="col-md-6 col-lg-3">
            <a href="${pageContext.request.contextPath}/admin/add-car" class="action-card rounded-3 p-4">
                <div class="text-center">
                    <i class="fas fa-plus-circle fa-3x mb-3"></i>
                    <h4 class="fw-bold">Thêm Xe Mới</h4>
                    <p class="mb-0 opacity-75">Thêm một mẫu xe mới vào hệ thống</p>
                </div>
            </a>
        </div>

        <div class="col-md-6 col-lg-3">
            <a href="${pageContext.request.contextPath}/cars" class="action-card rounded-3 p-4">
                <div class="text-center">
                    <i class="fas fa-car fa-3x mb-3"></i>
                    <h4 class="fw-bold">Quản Lý Xe</h4>
                    <p class="mb-0 opacity-75">Xem, chỉnh sửa hoặc xóa các mẫu xe</p>
                </div>
            </a>
        </div>

        <div class="col-md-6 col-lg-3">
            <a href="${pageContext.request.contextPath}/orders" class="action-card rounded-3 p-4">
                <div class="text-center">
                    <i class="fas fa-shopping-cart fa-3x mb-3"></i>
                    <h4 class="fw-bold">Quản Lý Đơn Hàng</h4>
                    <p class="mb-0 opacity-75">Xem và xử lý các đơn hàng của khách hàng</p>
                </div>
            </a>
        </div>

        <div class="col-md-6 col-lg-3">
            <a href="${pageContext.request.contextPath}/users" class="action-card rounded-3 p-4">
                <div class="text-center">
                    <i class="fas fa-users fa-3x mb-3"></i>
                    <h4 class="fw-bold">Quản Lý Người Dùng</h4>
                    <p class="mb-0 opacity-75">Quản lý tài khoản người dùng và vai trò</p>
                </div>
            </a>
        </div>
    </div>

    <!-- Statistics Cards (Optional) -->
    <div class="row g-4 mt-4">
        <div class="col-md-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-car fa-2x text-primary mb-3"></i>
                    <h5 class="card-title">Tổng Xe</h5>
                    <h2 class="text-primary">--</h2>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-shopping-cart fa-2x text-success mb-3"></i>
                    <h5 class="card-title">Đơn Hàng</h5>
                    <h2 class="text-success">--</h2>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-users fa-2x text-info mb-3"></i>
                    <h5 class="card-title">Người Dùng</h5>
                    <h2 class="text-info">--</h2>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-dollar-sign fa-2x text-warning mb-3"></i>
                    <h5 class="card-title">Doanh Thu</h5>
                    <h2 class="text-warning">--</h2>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="py-4 text-center">
    <div class="container">
        <p class="mb-0">© 2025 Car Showroom. Thiết kế bởi Nhóm PRN212. |
            <a href="${pageContext.request.contextPath}/about" class="text-decoration-none text-white">Về chúng tôi</a>
        </p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>