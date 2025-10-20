<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/18/2025
  Time: 3:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật nhân viên</title>
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

        .form-control {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #444;
        }

        .form-control:focus {
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
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-user-edit"></i> Cập nhật thông tin nhân viên</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <c:if test="${not empty success}">
        <div class="alert alert-success text-center">${success}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/update-staff">
        <input type="hidden" name="userId" value="${staff.userId}">

        <div class="mb-3">
            <label class="form-label">Tên nhân viên</label>
            <input type="text" name="name" value="${staff.name}" class="form-control" required maxlength="100">
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" value="${staff.email}" class="form-control" readonly>
        </div>

        <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input type="text" name="phone" value="${staff.phone}" class="form-control">
        </div>

        <div class="mb-3">
            <label class="form-label">Địa chỉ</label>
            <input type="text" name="address" value="${staff.address}" class="form-control">
        </div>

        <div class="text-center">
            <button type="submit" id ="btnBackToDashboard" class="btn btn-primary px-4">
                <i class="fas fa-save"></i> Cập nhật
            </button>
        </div>
    </form>
</div>
<script>

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

