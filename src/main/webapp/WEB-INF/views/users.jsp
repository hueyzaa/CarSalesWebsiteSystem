<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Người Dùng - Car Showroom</title>
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
            <i class="fas fa-users text-primary"></i> Quản Lý Người Dùng
        </h1>
        <p class="lead text-muted">Quản lý tài khoản người dùng và vai trò</p>
    </div>

    <!-- Messages -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Users Table -->
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                <i class="fas fa-list"></i> Danh Sách Người Dùng
            </h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                    <tr>
                        <th><i class="fas fa-hashtag"></i> ID</th>
                        <th><i class="fas fa-user"></i> Tên</th>
                        <th><i class="fas fa-envelope"></i> Email</th>
                        <th><i class="fas fa-user-tag"></i> Vai Trò</th>
                        <th><i class="fas fa-calendar"></i> Ngày Tạo</th>
                        <th class="text-center"><i class="fas fa-cog"></i> Thao Tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td class="fw-bold">${user.userId}</td>
                            <td>
                                <i class="fas fa-user-circle text-primary"></i>
                                    ${user.name}
                            </td>
                            <td>${user.email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.role == 'ADMIN'}">
                                            <span class="badge bg-danger">
                                                <i class="fas fa-user-shield"></i> ADMIN
                                            </span>
                                    </c:when>
                                    <c:when test="${user.role == 'STAFF'}">
                                            <span class="badge bg-info">
                                                <i class="fas fa-user-tie"></i> STAFF
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                            <span class="badge bg-success">
                                                <i class="fas fa-user"></i> CUSTOMER
                                            </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <i class="far fa-calendar-alt text-muted"></i>
                                <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy" />
                            </td>
                            <td class="text-center">
                                <div class="btn-group btn-group-sm" role="group">
                                    <a href="${pageContext.request.contextPath}/users/edit?id=${user.userId}"
                                       class="btn btn-outline-primary" title="Chỉnh sửa">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <c:if test="${user.userId != sessionScope.userId}">
                                        <a href="${pageContext.request.contextPath}/users/delete?id=${user.userId}"
                                           class="btn btn-outline-danger" title="Xóa"
                                           onclick="return confirm('Bạn có chắc muốn xóa người dùng này?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Statistics -->
    <div class="row mt-4">
        <div class="col-md-4">
            <div class="card text-center border-danger">
                <div class="card-body">
                    <i class="fas fa-user-shield fa-2x text-danger mb-2"></i>
                    <h5 class="card-title">Admin</h5>
                    <h2 class="text-danger">
                        <c:set var="adminCount" value="0" />
                        <c:forEach var="user" items="${userList}">
                            <c:if test="${user.role == 'ADMIN'}">
                                <c:set var="adminCount" value="${adminCount + 1}" />
                            </c:if>
                        </c:forEach>
                        ${adminCount}
                    </h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center border-info">
                <div class="card-body">
                    <i class="fas fa-user-tie fa-2x text-info mb-2"></i>
                    <h5 class="card-title">Staff</h5>
                    <h2 class="text-info">
                        <c:set var="staffCount" value="0" />
                        <c:forEach var="user" items="${userList}">
                            <c:if test="${user.role == 'STAFF'}">
                                <c:set var="staffCount" value="${staffCount + 1}" />
                            </c:if>
                        </c:forEach>
                        ${staffCount}
                    </h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center border-success">
                <div class="card-body">
                    <i class="fas fa-users fa-2x text-success mb-2"></i>
                    <h5 class="card-title">Customer</h5>
                    <h2 class="text-success">
                        <c:set var="customerCount" value="0" />
                        <c:forEach var="user" items="${userList}">
                            <c:if test="${user.role == 'CUSTOMER'}">
                                <c:set var="customerCount" value="${customerCount + 1}" />
                            </c:if>
                        </c:forEach>
                        ${customerCount}
                    </h2>
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