<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Xe - Car Showroom</title>
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
        .image-url-input {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="fas fa-car"></i> Car Showroom - Admin
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
                        <i class="fas fa-list"></i> Danh Sách Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt"></i> Đăng Xuất
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <!-- Page Header -->
            <div class="text-center mb-4">
                <h1 class="display-5 fw-bold">
                    <i class="fas fa-plus-circle text-primary"></i> Thêm Xe Mới
                </h1>
                <p class="text-muted">Điền thông tin chi tiết về xe mới</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Add Car Form -->
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-car"></i> Thông Tin Xe
                    </h5>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/admin/add-car">
                        <!-- Brand Selection -->
                        <div class="mb-3">
                            <label for="brandId" class="form-label fw-semibold">
                                <i class="fas fa-flag"></i> Hãng Xe <span class="text-danger">*</span>
                            </label>
                            <select class="form-select" id="brandId" name="brandId" required>
                                <option value="">-- Chọn hãng xe --</option>
                                <c:forEach var="brand" items="${brandList}">
                                    <option value="${brand.brandId}" ${brandId == brand.brandId ? 'selected' : ''}>
                                            ${brand.brandName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Model -->
                        <div class="mb-3">
                            <label for="model" class="form-label fw-semibold">
                                <i class="fas fa-car"></i> Tên Mẫu Xe <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="model" name="model"
                                   value="${model}" required maxlength="100"
                                   placeholder="Ví dụ: Camry 2024, Civic RS...">
                            <div class="form-text">Tối đa 100 ký tự</div>
                        </div>

                        <!-- Price -->
                        <div class="mb-3">
                            <label for="price" class="form-label fw-semibold">
                                <i class="fas fa-dollar-sign"></i> Giá (VNĐ) <span class="text-danger">*</span>
                            </label>
                            <input type="number" class="form-control" id="price" name="price"
                                   value="${price}" required min="1"
                                   placeholder="Ví dụ: 1250000000">
                            <div class="form-text">Nhập giá bán của xe</div>
                        </div>

                        <!-- Status -->
                        <div class="mb-3">
                            <label for="status" class="form-label fw-semibold">
                                <i class="fas fa-info-circle"></i> Trạng Thái <span class="text-danger">*</span>
                            </label>
                            <select class="form-select" id="status" name="status" required>
                                <option value="AVAILABLE" ${status == 'AVAILABLE' ? 'selected' : ''}>
                                    Còn hàng
                                </option>
                                <option value="UNAVAILABLE" ${status == 'UNAVAILABLE' ? 'selected' : ''}>
                                    Hết hàng
                                </option>
                            </select>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label for="description" class="form-label fw-semibold">
                                <i class="fas fa-align-left"></i> Mô Tả
                            </label>
                            <textarea class="form-control" id="description" name="description"
                                      rows="4" maxlength="1000"
                                      placeholder="Nhập mô tả chi tiết về xe...">${description}</textarea>
                            <div class="form-text">Tối đa 1000 ký tự</div>
                        </div>

                        <!-- Image URLs -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fas fa-images"></i> URL Hình Ảnh
                            </label>
                            <div id="imageUrlsContainer">
                                <div class="input-group image-url-input">
                                    <input type="url" class="form-control" name="imageUrls"
                                           placeholder="https://example.com/image1.jpg">
                                    <button type="button" class="btn btn-outline-danger" onclick="removeImageUrl(this)">
                                        <i class="fas fa-times"></i>
                                    </button>
                                </div>
                            </div>
                            <button type="button" class="btn btn-sm btn-outline-primary mt-2" onclick="addImageUrl()">
                                <i class="fas fa-plus"></i> Thêm Ảnh
                            </button>
                            <div class="form-text">Thêm URL của các hình ảnh xe (tối đa 255 ký tự mỗi URL)</div>
                        </div>

                        <!-- Primary Image -->
                        <div class="mb-4">
                            <label for="primaryImage" class="form-label fw-semibold">
                                <i class="fas fa-image"></i> Ảnh Chính
                            </label>
                            <select class="form-select" id="primaryImage" name="primaryImage">
                                <option value="0">Ảnh đầu tiên</option>
                            </select>
                            <div class="form-text">Chọn ảnh nào sẽ hiển thị đầu tiên</div>
                        </div>

                        <!-- Form Actions -->
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-save"></i> Thêm Xe
                            </button>
                            <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-secondary">
                                <i class="fas fa-times"></i> Hủy
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Help Card -->
            <div class="card mt-4 border-info">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">
                        <i class="fas fa-info-circle"></i> Hướng Dẫn
                    </h6>
                </div>
                <div class="card-body">
                    <ul class="mb-0">
                        <li>Các trường có dấu <span class="text-danger">*</span> là bắt buộc</li>
                        <li>Giá xe phải là số dương</li>
                        <li>URL ảnh phải bắt đầu bằng http:// hoặc https://</li>
                        <li>Bạn có thể thêm nhiều ảnh cho mỗi xe</li>
                        <li>Ảnh chính sẽ được hiển thị đầu tiên trong danh sách</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="py-4 text-center">
    <div class="container">
        <p class="mb-0">© 2025 Car Showroom. Thiết kế bởi Nhóm PRN212.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function addImageUrl() {
        const container = document.getElementById('imageUrlsContainer');
        const div = document.createElement('div');
        div.className = 'input-group image-url-input';
        div.innerHTML = `
        <input type="url" class="form-control" name="imageUrls"
               placeholder="https://example.com/image.jpg">
        <button type="button" class="btn btn-outline-danger" onclick="removeImageUrl(this)">
            <i class="fas fa-times"></i>
        </button>
    `;
        container.appendChild(div);
        updatePrimaryImageOptions();
    }

    function removeImageUrl(button) {
        const container = document.getElementById('imageUrlsContainer');
        if (container.children.length > 1) {
            button.parentElement.remove();
            updatePrimaryImageOptions();
        } else {
            alert('Phải có ít nhất một ảnh!');
        }
    }

    function updatePrimaryImageOptions() {
        const container = document.getElementById('imageUrlsContainer');
        const select = document.getElementById('primaryImage');
        const count = container.children.length;

        select.innerHTML = '';
        for (let i = 0; i < count; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `Ảnh ${i + 1}`;
            select.appendChild(option);
        }
    }
</script>
</body>
</html>