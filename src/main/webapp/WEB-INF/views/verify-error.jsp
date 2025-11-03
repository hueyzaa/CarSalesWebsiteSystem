<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 03/11/2025
  Time: 1:41 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực thất bại - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #0f0f0f 0%, #1a1a1a 100%);
            position: relative;
        }

        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image:
                    radial-gradient(circle at 20% 50%, rgba(255, 215, 0, 0.05) 0%, transparent 50%),
                    radial-gradient(circle at 80% 80%, rgba(255, 215, 0, 0.05) 0%, transparent 50%);
            pointer-events: none;
        }

        .error-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 550px;
            width: 100%;
            text-align: center;
            position: relative;
            overflow: hidden;
            padding: 60px 40px;
            z-index: 1;
        }

        .error-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #e74c3c 0%, #c0392b 100%);
        }

        .error-icon {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
        }

        .error-icon i {
            font-size: 3rem;
            color: #fff;
        }

        .error-card h2 {
            color: #e74c3c;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .error-card p {
            color: #888;
            font-size: 1.1rem;
            margin-bottom: 10px;
        }

        .error-message {
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid rgba(231, 76, 60, 0.3);
            border-radius: 10px;
            padding: 15px;
            color: #ff6b6b;
            margin: 20px 0 30px;
        }

        .btn-action {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1rem;
            text-decoration: none;
            display: inline-block;
            margin: 0 5px;
        }

        .btn-action:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-secondary-action {
            background: transparent;
            border: 2px solid #ffd700;
            color: #ffd700;
            padding: 10px 30px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1rem;
            text-decoration: none;
            display: inline-block;
            margin: 0 5px;
        }

        .btn-secondary-action:hover {
            background: #ffd700;
            color: #1a1a1a;
        }
    </style>
</head>
<body>
<div class="error-card">
    <div class="error-icon">
        <i class="fas fa-times"></i>
    </div>

    <h2>Xác thực thất bại</h2>
    <p>Rất tiếc, chúng tôi không thể xác thực email của bạn.</p>

    <div class="error-message">
        <i class="fas fa-exclamation-circle"></i>
        ${error != null ? error : 'Link xác thực không hợp lệ hoặc đã hết hạn'}
    </div>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/register" class="btn-action">
            <i class="fas fa-user-plus"></i> Đăng ký lại
        </a>
        <a href="${pageContext.request.contextPath}/login" class="btn-secondary-action">
            <i class="fas fa-sign-in-alt"></i> Đăng nhập
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
