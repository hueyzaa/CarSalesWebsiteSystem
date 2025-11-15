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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #0e0e0e;
            color: #f8f9fa;
            font-family: "Inter", sans-serif;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 15px;
        }
        .card {
            background: linear-gradient(145deg, #1b1b1b, #151515);
            border-radius: 18px;
            padding: 35px;
            max-width: 650px;
            width: 100%;
        }
        .card h3 {
            color: #ffd700;
            text-align: center;
            margin-bottom: 25px;
        }
        .form-label { color: #ccc; font-weight: 500; margin-bottom: 6px; }
        .form-control, .form-select {
            background-color: #2a2a2a; color: #fff; border: 1px solid #444;
        }
        .btn-primary { background-color: #ffd700; color: #000; }
        .btn-secondary { background-color: #2c2c2c; color: #fff; }
        .alert { text-align: center; border-radius: 10px; }
        .form-section { margin-bottom: 20px; }
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-user-edit"></i> Cập nhật người dùng</h3>

    <!-- Thông báo lỗi / thành công -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success py-2">${success}</div>
    </c:if>

    <c:if test="${not empty user}">
        <form method="post" action="${pageContext.request.contextPath}/Admin/update-user">
            <input type="hidden" name="userId" value="${user.userId}">

            <div class="form-section">
                <label class="form-label">Tên người dùng</label>
                <input type="text" name="name" value="${user.name}" class="form-control" required maxlength="100">
            </div>

            <div class="form-section">
                <label class="form-label">Số điện thoại</label>
                <input type="text" name="phone" value="${user.phone}" class="form-control" maxlength="20">
            </div>

            <div class="form-section">
                <label class="form-label">Địa chỉ</label>
                <input type="text" name="address" value="${user.address}" class="form-control" maxlength="255">
            </div>

            <div class="form-section">
                <label class="form-label">Vai trò</label>
                <select name="role" class="form-select">
                    <option value="STAFF" <c:if test="${user.role eq 'STAFF'}">selected</c:if>>Nhân viên</option>
                    <option value="CUSTOMER" <c:if test="${user.role eq 'CUSTOMER'}">selected</c:if>>Khách hàng</option>
                </select>
            </div>

            <div class="form-section">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="ACTIVE" <c:if test="${user.active}">selected</c:if>>Hoạt động</option>
                    <option value="INACTIVE" <c:if test="${not user.active}">selected</c:if>>Vô hiệu hóa</option>
                </select>
            </div>

            <div class="text-center mt-4">
                <button type="submit" class="btn btn-primary me-2"><i class="fas fa-save"></i> Lưu thay đổi</button>
                <a href="${pageContext.request.contextPath}/Admin/user-list" class="btn btn-secondary"><i class="fas fa-times"></i> Hủy</a>
            </div>
        </form>
    </c:if>
</div>

</body>
</html>

