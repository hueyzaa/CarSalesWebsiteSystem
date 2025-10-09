<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Xe - Car Showroom</title>
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
        .car-card {
            transition: transform 0.3s, box-shadow 0.3s;
            height: 100%;
        }
        .car-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .car-card img {
            height: 220px;
            object-fit: cover;
        }
        .badge-available {
            position: absolute;
            top: 10px;
            right: 10px;
            z-index: 1;
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="fas fa-home"></i> Trang Chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/cars">
                        <i class="fas fa-car"></i> Xe Hơi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cart">
                        <i class="fas fa-shopping-cart"></i> Giỏ Hàng
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/orders">
                        <i class="fas fa-receipt"></i> Đơn Hàng
                    </a>
                </li>
                <c:if test="${sessionScope.userRole == 'ADMIN'}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                            <i class="fas fa-tachometer-alt"></i> Admin
                        </a>
                    </li>
                </c:if>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-user"></i> ${sessionScope.userName}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><span class="dropdown-item-text"><small>${sessionScope.userRole}</small></span></li>
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
            <i class="fas fa-car text-primary"></i> Danh Sách Xe
        </h1>
        <p class="lead text-muted">Khám phá các mẫu xe nổi bật và phù hợp với nhu cầu của bạn</p>
    </div>

    <!-- Success/Error Messages -->
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

    <!-- Filters -->
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/cars" method="get">
                <div class="row g-3">
                    <!-- Brand Filter -->
                    <div class="col-md-3">
                        <label for="brand" class="form-label fw-semibold">
                            <i class="fas fa-flag"></i> Hãng Xe
                        </label>
                        <select name="brand" id="brand" class="form-select">
                            <option value="">Tất cả</option>
                            <c:forEach var="b" items="${brandList}">
                                <option value="${b.brandId}" ${param.brand == b.brandId ? 'selected' : ''}>
                                        ${b.brandName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Min Price -->
                    <div class="col-md-3">
                        <label for="minPrice" class="form-label fw-semibold">
                            <i class="fas fa-dollar-sign"></i> Giá Tối Thiểu (VNĐ)
                        </label>
                        <input type="number" name="minPrice" id="minPrice"
                               class="form-control" value="${param.minPrice}"
                               placeholder="Từ...">
                    </div>

                    <!-- Max Price -->
                    <div class="col-md-3">
                        <label for="maxPrice" class="form-label fw-semibold">
                            <i class="fas fa-dollar-sign"></i> Giá Tối Đa (VNĐ)
                        </label>
                        <input type="number" name="maxPrice" id="maxPrice"
                               class="form-control" value="${param.maxPrice}"
                               placeholder="Đến...">
                    </div>

                    <!-- Search -->
                    <div class="col-md-3">
                        <label for="keyword" class="form-label fw-semibold">
                            <i class="fas fa-search"></i> Tìm Kiếm
                        </label>
                        <div class="input-group">
                            <input type="text" name="keyword" id="keyword"
                                   class="form-control" value="${param.keyword}"
                                   placeholder="Tìm theo tên xe...">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </form>

            <!-- Add Car Button for Admin -->
            <c:if test="${isAdmin}">
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/admin/add-car" class="btn btn-success">
                        <i class="fas fa-plus-circle"></i> Thêm Xe Mới
                    </a>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Results Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="mb-0">Kết Quả Tìm Kiếm</h3>
        <p class="text-muted mb-0">
            Tìm thấy <strong class="text-primary">${carList.size()}</strong> xe
        </p>
    </div>

    <!-- Cars Grid -->
    <div class="row g-4">
        <c:forEach var="car" items="${carList}">
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card car-card border-0 shadow-sm">
                    <div class="position-relative">
                        <img src="${car.imageUrl != null ? car.imageUrl : 'https://via.placeholder.com/300x200?text=No+Image'}"
                             class="card-img-top" alt="${car.model}">
                        <span class="badge ${car.isAvailable ? 'bg-success' : 'bg-danger'} badge-available">
                                ${car.isAvailable ? 'Còn hàng' : 'Hết hàng'}
                        </span>
                    </div>
                    <div class="card-body">
                        <h6 class="text-primary text-uppercase small mb-2">${car.brandName}</h6>
                        <h5 class="card-title fw-bold">${car.model}</h5>
                        <p class="card-text text-muted small">${car.description}</p>
                        <h4 class="text-primary fw-bold mb-3">${car.formattedPrice}</h4>
                        <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}"
                           class="btn btn-primary w-100">
                            <i class="fas fa-eye"></i> Xem Chi Tiết
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Empty State -->
    <c:if test="${empty carList}">
        <div class="text-center py-5">
            <i class="fas fa-car-crash fa-4x text-muted mb-3"></i>
            <h3>Không tìm thấy xe nào</h3>
            <p class="text-muted">Vui lòng thử tìm kiếm với điều kiện khác</p>
        </div>
    </c:if>
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