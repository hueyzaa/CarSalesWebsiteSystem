<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Xe - Car Showroom</title>
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
        .main-image {
            width: 100%;
            height: 400px;
            object-fit: cover;
            border-radius: 15px;
        }
        .thumbnail {
            width: 100%;
            height: 100px;
            object-fit: cover;
            cursor: pointer;
            border-radius: 8px;
            transition: all 0.3s;
        }
        .thumbnail:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }
        .thumbnail.active {
            border: 3px solid #667eea;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container my-5">
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty car}">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang Chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cars">Xe Hơi</a></li>
                <li class="breadcrumb-item active">${car.model}</li>
            </ol>
        </nav>

        <div class="row g-4">
            <!-- Images Column -->
            <div class="col-lg-7">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <!-- Main Image -->
                        <img id="mainImage"
                             src="${not empty images[0] ? images[0] : 'https://via.placeholder.com/800x400?text=No+Image'}"
                             class="main-image mb-3" alt="${car.model}">

                        <!-- Thumbnail Gallery -->
                        <div class="row g-2">
                            <c:forEach var="image" items="${images}" varStatus="status">
                                <div class="col-3">
                                    <img src="${image}"
                                         class="thumbnail ${status.index == 0 ? 'active' : ''}"
                                         alt="Image ${status.index + 1}"
                                         onclick="changeImage('${image}', this)">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Details Column -->
            <div class="col-lg-5">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <!-- Brand Badge -->
                        <h6 class="text-primary text-uppercase mb-3">
                            <i class="fas fa-flag"></i> ${car.brandName}
                        </h6>

                        <!-- Car Name -->
                        <h2 class="fw-bold mb-3">${car.model}</h2>

                        <!-- Price -->
                        <div class="bg-light p-3 rounded mb-4">
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="text-muted">Giá bán:</span>
                                <h3 class="text-primary fw-bold mb-0">${car.formattedPrice}</h3>
                            </div>
                        </div>

                        <!-- Status -->
                        <div class="mb-4">
                            <span class="fw-semibold">Trạng thái: </span>
                            <c:choose>
                                <c:when test="${car.isAvailable}">
                                    <span class="badge bg-success fs-6">
                                        <i class="fas fa-check-circle"></i> Còn Hàng
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger fs-6">
                                        <i class="fas fa-times-circle"></i> Hết Hàng
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Description -->
                        <div class="mb-4">
                            <h5 class="fw-bold mb-3">
                                <i class="fas fa-info-circle"></i> Mô Tả
                            </h5>
                            <p class="text-muted">
                                <c:choose>
                                    <c:when test="${not empty car.description}">
                                        ${car.description}
                                    </c:when>
                                    <c:otherwise>
                                        Thông tin chi tiết về xe sẽ được cập nhật sớm.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <!-- Features -->
                        <div class="mb-4">
                            <h5 class="fw-bold mb-3">
                                <i class="fas fa-star"></i> Tính Năng Nổi Bật
                            </h5>
                            <ul class="list-unstyled">
                                <li class="mb-2">
                                    <i class="fas fa-check text-success"></i> Động cơ mạnh mẽ
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-check text-success"></i> Tiết kiệm nhiên liệu
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-check text-success"></i> Thiết kế hiện đại
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-check text-success"></i> An toàn tối ưu
                                </li>
                            </ul>
                        </div>

                        <!-- Actions -->
                        <div class="d-grid gap-2">
                            <c:if test="${car.isAvailable}">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="carId" value="${car.carId}">
                                            <div class="input-group mb-3">
                                                <span class="input-group-text">Số lượng:</span>
                                                <input type="number" name="quantity" class="form-control"
                                                       value="1" min="1" max="10">
                                            </div>
                                            <button type="submit" class="btn btn-success btn-lg w-100 mb-2">
                                                <i class="fas fa-shopping-cart"></i> Thêm Vào Giỏ Hàng
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/login" class="btn btn-success btn-lg mb-2">
                                            <i class="fas fa-sign-in-alt"></i> Đăng Nhập Để Mua Hàng
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>

                            <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-secondary">
                                <i class="fas fa-arrow-left"></i> Quay Lại Danh Sách
                            </a>

                            <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-primary">
                                <i class="fas fa-phone"></i> Liên Hệ Tư Vấn
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Additional Information -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-file-alt"></i> Thông Tin Chi Tiết
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <table class="table table-borderless">
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-flag text-primary"></i> Hãng xe:</td>
                                        <td>${car.brandName}</td>
                                    </tr>
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-car text-primary"></i> Mẫu xe:</td>
                                        <td>${car.model}</td>
                                    </tr>
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-dollar-sign text-primary"></i> Giá:</td>
                                        <td class="text-primary fw-bold">${car.formattedPrice}</td>
                                    </tr>
                                </table>
                            </div>
                            <div class="col-md-6">
                                <table class="table table-borderless">
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-info-circle text-primary"></i> Trạng thái:</td>
                                        <td>
                                            <span class="badge ${car.isAvailable ? 'bg-success' : 'bg-danger'}">
                                                    ${car.isAvailable ? 'Còn hàng' : 'Hết hàng'}
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-shield-alt text-primary"></i> Bảo hành:</td>
                                        <td>3 năm hoặc 100,000 km</td>
                                    </tr>
                                    <tr>
                                        <td class="fw-semibold"><i class="fas fa-truck text-primary"></i> Giao xe:</td>
                                        <td>Miễn phí toàn quốc</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function changeImage(imageSrc, element) {
        // Update main image
        document.getElementById('mainImage').src = imageSrc;

        // Remove active class from all thumbnails
        document.querySelectorAll('.thumbnail').forEach(thumb => {
            thumb.classList.remove('active');
        });

        // Add active class to clicked thumbnail
        element.classList.add('active');
    }
</script>
</body>
</html>