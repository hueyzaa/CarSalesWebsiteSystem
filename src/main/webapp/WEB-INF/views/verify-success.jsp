<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 03/11/2025
  Time: 1:41 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực thành công - Car Showroom</title>
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

        .success-card {
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

        .success-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #2ecc71 0%, #27ae60 100%);
        }

        .success-icon {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #2ecc71 0%, #27ae60 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            animation: scaleIn 0.5s ease-out;
        }

        .success-icon i {
            font-size: 3rem;
            color: #fff;
        }

        @keyframes scaleIn {
            0% {
                transform: scale(0);
                opacity: 0;
            }
            100% {
                transform: scale(1);
                opacity: 1;
            }
        }

        .success-card h2 {
            color: #2ecc71;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .success-card p {
            color: #888;
            font-size: 1.1rem;
            margin-bottom: 30px;
        }

        .btn-login {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 15px 40px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.1rem;
            text-decoration: none;
            display: inline-block;
        }

        .btn-login:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .confetti {
            position: fixed;
            width: 10px;
            height: 10px;
            background: #ffd700;
            position: absolute;
            animation: confetti-fall 3s linear infinite;
        }

        @keyframes confetti-fall {
            to {
                transform: translateY(100vh) rotate(360deg);
                opacity: 0;
            }
        }
    </style>
</head>
<body>
<div class="success-card">
    <div class="success-icon">
        <i class="fas fa-check"></i>
    </div>

    <h2>Xác thực thành công!</h2>
    <p>
        Email của bạn đã được xác thực thành công.<br>
        Bây giờ bạn có thể đăng nhập và sử dụng dịch vụ của chúng tôi.
    </p>

    <a href="${pageContext.request.contextPath}/login" class="btn-login">
        <i class="fas fa-sign-in-alt"></i> Đăng nhập ngay
    </a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto redirect after 3 seconds
    setTimeout(function() {
        window.location.href = '${pageContext.request.contextPath}/login';
    }, 3000);
</script>
</body>
</html>
