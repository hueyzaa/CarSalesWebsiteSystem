<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 11/3/2025
  Time: 9:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm hãng xe mới</title>
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
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.15);
        }

        .card h3 {
            color: #ffd700;
            margin-bottom: 25px;
            text-align: center;
            font-weight: 700;
        }

        .form-label {
            color: #ffd700;
            font-size: 18px;
            font-weight: 600;
        }

        .form-control {
            background-color: #2a2a2a;
            color: #fff;
            border: 2px solid #444;
            padding: 14px 18px;
            font-size: 18px;
            border-radius: 10px;
            transition: all 0.2s ease-in-out;
        }

        .form-control:focus {
            border-color: #ffd700;
            box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }

        .btn-primary {
            background-color: #ffd700;
            border: none;
            color: #000;
            font-weight: bold;
            padding: 10px 30px;
            border-radius: 10px;
            font-size: 18px;
        }

        .btn-primary:hover {
            background-color: #e5c100;
        }

        .alert {
            font-size: 17px;
            font-weight: bold;
        }
    </style>
</head>

<body>

<div class="card">
    <h3><i class="fas fa-plus-circle"></i> Thêm Hãng Xe Mới</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/add-brand">

        <div class="mb-4">
            <label class="form-label">Tên hãng xe</label>
            <input type="text"
                   name="brandName"
                   class="form-control"
                   placeholder="Ví dụ: Honda, Yamaha, Suzuki..."
                   value="${brandName}"
                   required>
        </div>

        <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Thêm Hãng Xe
            </button>
        </div>

    </form>
</div>
</body>
</html>


