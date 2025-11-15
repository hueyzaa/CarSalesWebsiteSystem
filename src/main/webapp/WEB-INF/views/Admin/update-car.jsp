<%--
  Created by IntelliJ IDEA.
  User: HungNB
  Date: 10/15/2025
  Time: 10:55 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật xe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #0f0f0f;
            color: #f8f9fa;
            padding: 40px;
        }

        .card {
            background-color: #1c1c1c;
            border: 1px solid #333;
            border-radius: 14px;
            padding: 30px;
            max-width: 900px;
            margin: auto;
        }

        .card h3 {
            color: #ffd700;
            text-align: center;
            margin-bottom: 25px;
        }


        label, .form-label, .form-control, .form-select, textarea {
            color: #fff;
        }

        .form-control, .form-select {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #444;
        }

        .form-control:focus, .form-select:focus {
            border-color: #ffd700;
            box-shadow: 0 0 6px #ffd700;
        }


        .btn-primary {
            background-color: #ffd700;
            color: #000;
            font-weight: bold;
            border: none;
        }

        .btn-primary:hover {
            background-color: #e5c100;
            color: #000;
        }

        .btn-secondary {
            background-color: #444;
            border: none;
            color: #fff;
        }

        .btn-secondary:hover {
            background-color: #555;
            color: #fff;
        }

        .alert {
            color: #2a2a2a;
        }
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-edit"></i> Cập Nhật Xe</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/update-car">
        <input type="hidden" name="id" value="${car.id}"/>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Tên xe</label>
                <input type="text" name="model" value="${car.name}" class="form-control" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Hãng xe</label>
                <select name="brandId" class="form-select" required>
                    <c:forEach var="brand" items="${brandList}">
                        <option value="${brand.brandId}" ${brand.brandId == car.brandId ? 'selected' : ''}>${brand.brandName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <label class="form-label">Giá (₫)</label>
                <input type="number" step="0.01" name="price" value="${car.price}" class="form-control" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Năm sản xuất</label>
                <input type="number" name="year" value="${car.year}" class="form-control">
            </div>
            <div class="col-md-4">
                <label class="form-label">Tồn kho</label>
                <input type="number" name="stock" value="${car.stock}" class="form-control">
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Màu sắc</label>
                <input type="text" name="color" value="${car.color}" class="form-control">
            </div>
            <div class="col-md-6">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="AVAILABLE" ${car.status == 'AVAILABLE' ? 'selected' : ''}>Còn hàng</option>
                    <option value="UNAVAILABLE" ${car.status == 'UNAVAILABLE' ? 'selected' : ''}>Hết hàng</option>
                </select>
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label">Mô tả</label>
            <textarea name="description" rows="3" class="form-control">${car.description}</textarea>
        </div>
        <div class="mb-4">
            <label class="form-label">Hình ảnh xe (URL)</label>
            <div id="imageContainer">
                <div class="image-input-row">
                    <input type="text" name="imageUrls" class="form-control" placeholder="Nhập link ảnh...">
                    <label><input type="radio" name="primaryImage" value="0" checked> Ảnh chính</label>
                </div>
            </div>
            <button type="button" class="btn btn-secondary btn-sm mt-2" id="addImageBtn">
                <i class="fas fa-plus"></i> Thêm ảnh
            </button>
        </div>
        <div class="text-center mt-4">
            <button type="submit" id="btnBackToDashboard" class="btn btn-primary px-4">
                <i class="fas fa-save"></i> Lưu thay đổi
            </button>
        </div>
    </form>
</div>
<script>

    document.getElementById("addImageBtn").addEventListener("click", function () {
        const container = document.getElementById("imageContainer");
        const index = container.querySelectorAll(".image-input-row").length;
        const div = document.createElement("div");
        div.className = "image-input-row";
        div.innerHTML = `
            <input type="text" name="imageUrls" class="form-control" placeholder="Nhập link ảnh...">
            <label><input type="radio" name="primaryImage" value="${index}"> Ảnh chính</label>
        `;
        container.appendChild(div);
    });
    document.getElementById("btnBackToDashboard").addEventListener("click",function () {
        if(window.parent && window.parent.document.getElementById("adminDynamicContent")) {
            const overview = window.parent.document.getElementById("overviewSection");
            dynamicContent.style.display = "none";
            overview.style.display = "block";
        } else {
            window.location.href = "${pageContext.request.contextPath}/Admin/dashboard";
        }
    });
</script>
</body>
</html>
