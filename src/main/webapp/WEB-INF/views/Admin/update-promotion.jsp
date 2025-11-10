<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 11/9/2025
  Time: 8:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật khuyến mãi</title>
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
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.1);
        }

        .card h3 {
            color: #ffd700;
            margin-bottom: 25px;
            text-align: center;
        }

        .form-label {
            color: #ddd;
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
        }

        .btn-secondary:hover {
            background-color: #555;
        }

        .alert {
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-edit"></i> Cập Nhật Khuyến Mãi</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/update-promotion">
        <input type="hidden" name="promotionId" value="${promotion.promotionId}">

        <div class="mb-3">
            <label class="form-label">Tên khuyến mãi</label>
            <input type="text" name="title" value="${promotion.title}" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Mô tả</label>
            <textarea name="description" class="form-control" rows="3" required>${promotion.description}</textarea>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">Ngày bắt đầu</label>
                <input type="date" name="startDate"
                       value="<fmt:formatDate value='${promotion.startDate}' pattern='yyyy-MM-dd'/>"
                       class="form-control" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Ngày kết thúc</label>
                <input type="date" name="endDate"
                       value="<fmt:formatDate value='${promotion.endDate}' pattern='yyyy-MM-dd'/>"
                       class="form-control" required>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-6">
                <label class="form-label">Giảm (%)</label>
                <input type="number" step="0.01" name="discountPercentage" value="${promotion.discountPercentage}"
                       class="form-control">
            </div>
        </div>
        <div class="text-center">
            <button type="submit" class="btn btn-primary px-4 me-2">
                <i class="fas fa-save"></i> Lưu thay đổi
            </button>
            <a href="${pageContext.request.contextPath}/Admin/promotion-list" class="btn btn-secondary px-4">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>
    </form>
</div>
</body>
</html>


