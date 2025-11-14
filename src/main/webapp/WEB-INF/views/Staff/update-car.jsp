<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 12:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật xe</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        :root{ --bg:#0f0f0f; --panel:#151515; --line:#2a2a2a; --gold:#ffd700; }
        body{background:var(--bg); color:#f4f4f4; padding:32px}
        .cardx{max-width:960px; margin:auto; background:linear-gradient(180deg,#141414,#121212); border:1px solid var(--line); border-radius:16px; padding:22px}
        .title{color:var(--gold); font-weight:800}
        .form-control,.form-select{background:#0f0f0f; color:#fff; border:1px solid #3a3a3a}
        .form-control:focus,.form-select:focus{border-color:var(--gold); box-shadow:0 0 0 2px rgba(255,215,0,.25)}
        .image-input-row{display:flex; align-items:center; gap:10px; margin-top:8px}
        .btn-gold{background:var(--gold); color:#111; font-weight:800; border:none}
        .btn-gold:hover{background:#e8c300}
        .btn-dim{background:#2a2a2a; color:#fff; border:none}
        .btn-dim:hover{background:#3a3a3a}
    </style>
</head>
<body>
<div class="cardx">
    <h3 class="title mb-3"><i class="fa-solid fa-pen-to-square"></i> Cập nhật xe</h3>
    <form action="${pageContext.request.contextPath}/staff/update-car" method="post">
        <input type="hidden" name="id" value="${car.id}">
        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Tên xe</label>
                <input type="text" name="name" value="${car.name}" class="form-control" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Hãng xe</label>
                <select name="brandId" class="form-select" required>
                    <c:forEach var="brand" items="${brandList}">
                        <option value="${brand.brandId}" ${brand.brandId == car.brandId ? 'selected' : ''}>${brand.brandName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Năm sản xuất</label>
                <input type="number" name="year" value="${car.year}" class="form-control">
            </div>
            <div class="col-md-4">
                <label class="form-label">Tồn kho</label>
                <input type="number" name="stock" value="${car.stock}" class="form-control">
            </div>

            <div class="col-md-6">
                <label class="form-label">Màu sắc</label>
                <input type="text" name="color" value="${car.color}" class="form-control">
            </div>
            <div class="col-md-6">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="AVAILABLE" ${car.status == 'AVAILABLE' ? 'selected' : ''}>Còn hàng</option>
                    <option value="OUT_OF_STOCK" ${car.status == 'OUT_OF_STOCK' ? 'selected' : ''}>Hết hàng</option>
                </select>
            </div>

            <div class="col-12">
                <label class="form-label">Mô tả</label>
                <textarea name="description" rows="3" class="form-control">${car.description}</textarea>
            </div>

            <div class="col-12">
                <label class="form-label">Hình ảnh xe (URL)</label>
                <div id="imageContainer">
                    <div class="image-input-row">
                        <input type="text" name="imageUrls" class="form-control" placeholder="Nhập link ảnh…">
                        <label class="text-warning"><input type="radio" name="primaryImage" value="0" checked> Ảnh chính</label>
                    </div>
                </div>
                <button type="button" class="btn btn-dim btn-sm mt-2" id="addImageBtn">
                    <i class="fas fa-plus"></i> Thêm ảnh
                </button>
            </div>
        </div>

        <div class="text-center mt-4">
            <button type="submit" class="btn btn-gold px-4">Lưu thay đổi</button>
            <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn btn-dim px-3">Hủy</a>
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
        <input type="text" name="imageUrls" class="form-control" placeholder="Nhập link ảnh…">
        <label class="text-warning"><input type="radio" name="primaryImage" value="${index}"> Ảnh chính</label>
      `;
        container.appendChild(div);
    });
</script>
</body>
</html>
