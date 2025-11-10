<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/28/2025
  Time: 7:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Người Dùng Mới</title>
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
            max-width: 700px;
            margin: auto;
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.1);
        }

        .card h3 {
            color: #ffd700;
            text-align: center;
            margin-bottom: 25px;
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
            border: none;
            color: #000;
            font-weight: bold;
        }

        .btn-primary:hover {
            background-color: #e5c100;
        }

        .btn-secondary {
            background-color: #444;
            border: none;
            color: #fff;
        }

        .btn-secondary:hover {
            background-color: #555;
        }

        .alert {
            border-radius: 10px;
        }
        .form-label{
            color: #fff;
        }

    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-user-plus"></i> Thêm Người Dùng Mới</h3>


    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="alert alert-success text-center">${success}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/add-user">

        <div class="mb-3">
            <label class="form-label">Tên người dùng</label>
            <input type="text" name="name" value="${name}" class="form-control" required maxlength="100">
        </div>


        <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <select name="role" class="form-select" required>
                <option value="">-- Chọn vai trò --</option>
                <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>Quản trị viên</option>
                <option value="STAFF" ${role == 'STAFF' ? 'selected' : ''}>Nhân viên</option>
                <option value="CUSTOMER" ${role == 'CUSTOMER' ? 'selected' : ''}>Khách hàng</option>
            </select>
        </div>


        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" value="${email}" class="form-control" required>
        </div>


        <div class="mb-3">
            <label class="form-label">Mật khẩu</label>
            <input type="password" name="password" class="form-control" required minlength="6">
        </div>


        <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input type="text" name="phone" value="${phone}" class="form-control">
        </div>


        <div class="mb-3">
            <label class="form-label">Địa chỉ</label>
            <input type="text" name="address" value="${address}" class="form-control">
        </div>
        <div class="text-center">
            <button type="submit" class="btn btn-primary px-4">
                <i class="fas fa-save"></i> Thêm người dùng
            </button>
            <a href="${pageContext.request.contextPath}/Admin/user-list" class="btn btn-secondary px-4 ms-2">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>
    </form>
</div>

<script>

    document.querySelector("form").addEventListener("submit", function() {
        if (window.parent && window.parent.document.getElementById("adminDynamicContent")) {
            const dynamicContent = window.parent.document.getElementById("adminDynamicContent");
            const overview = window.parent.document.getElementById("overviewSection");
            dynamicContent.style.display = "none";
            overview.style.display = "block";
        }
    });
</script>
</body>
</html>
