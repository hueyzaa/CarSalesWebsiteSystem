<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/28/2025
  Time: 7:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật người dùng</title>

    <!-- Bootstrap & Font Awesome -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #0e0e0e;
            color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 15px;
            font-family: "Inter", sans-serif;
        }

        .card {
            background: linear-gradient(145deg, #1b1b1b, #151515);
            border: 1px solid #333;
            border-radius: 18px;
            padding: 35px;
            width: 100%;
            max-width: 650px;
            box-shadow: 0 0 30px rgba(255, 215, 0, 0.05);
        }

        .card h3 {
            color: #ffd700;
            text-align: center;
            font-weight: 700;
            margin-bottom: 25px;
        }

        label.form-label {
            color: #ccc;
            font-weight: 500;
            margin-bottom: 6px;
        }

        .form-control, .form-select {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #444;
            border-radius: 10px;
            padding: 10px 12px;
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: #ffd700;
            box-shadow: 0 0 6px rgba(255, 215, 0, 0.4);
            outline: none;
        }

        .btn-primary {
            background-color: #ffd700;
            color: #000;
            font-weight: 600;
            border: none;
            padding: 10px 24px;
            border-radius: 10px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #e5c100;
            transform: scale(1.02);
        }

        .btn-secondary {
            background-color: #2c2c2c;
            color: #fff;
            border: none;
            padding: 10px 24px;
            border-radius: 10px;
            transition: all 0.3s ease;
        }

        .btn-secondary:hover {
            background-color: #3a3a3a;
            color: #ffd700;
        }

        .alert {
            border-radius: 10px;
            text-align: center;
            font-weight: 500;
        }

        .form-section {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-user-edit"></i> Cập nhật thông tin người dùng</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2">${error}</div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="alert alert-success py-2">${success}</div>

        <script>
            setTimeout(() => {
                window.location.href = "${pageContext.request.contextPath}/Admin/dashboard";
            }, 2000);
        </script>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/update-user">
        <input type="hidden" name="userId" value="${user.userId}">

        <div class="form-section">
            <label class="form-label">Tên người dùng</label>
            <input type="text" name="name" value="${user.name}" class="form-control" required maxlength="100">
        </div>

        <div class="form-section">
            <label class="form-label">Email</label>
            <input type="email" name="email" value="${user.email}" class="form-control" readonly>
        </div>

        <div class="form-section">
            <label class="form-label">Số điện thoại</label>
            <input type="text" name="phone" value="${user.phone}" class="form-control">
        </div>

        <div class="form-section">
            <label class="form-label">Địa chỉ</label>
            <input type="text" name="address" value="${user.address}" class="form-control">
        </div>

        <div class="form-section">
            <label class="form-label">Vai trò</label>
            <select name="role" class="form-select">
                <option value="ADMIN" <c:if test="${user.role eq 'ADMIN'}">selected</c:if>>Quản trị viên</option>
                <option value="STAFF" <c:if test="${user.role eq 'STAFF'}">selected</c:if>>Nhân viên</option>
                <option value="CUSTOMER" <c:if test="${user.role eq 'CUSTOMER'}">selected</c:if>>Khách hàng</option>
            </select>
        </div>

        <div class="form-section">
            <label class="form-label">Trạng thái</label>
            <select name="status" class="form-select">
                <option value="ACTIVE" <c:if test="${user.status eq 'ACTIVE'}">selected</c:if>>Hoạt động</option>
                <option value="INACTIVE" <c:if test="${user.status eq 'INACTIVE'}">selected</c:if>>Không hoạt động</option>
            </select>
        </div>

        <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary me-2">
                <i class="fas fa-save"></i> Lưu thay đổi
            </button>
            <a href="${pageContext.request.contextPath}/Admin/dashboard" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại Dashboard
            </a>
        </div>
    </form>
</div>

</body>
</html>


