<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chờ Xác Thực Email - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            background: linear-gradient(135deg, #0f0f0f 0%, #1a1a1a 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .pending-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 600px;
            width: 100%;
            overflow: hidden;
            position: relative;
        }

        .pending-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .pending-header {
            text-align: center;
            padding: 40px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .pending-header i {
            font-size: 5rem;
            color: #4CAF50;
            margin-bottom: 20px;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.5; }
        }

        .pending-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .pending-header p {
            color: #888;
            margin: 0;
        }

        .pending-body {
            padding: 40px;
        }

        .success-box {
            background: rgba(76, 175, 80, 0.1);
            border: 1px solid rgba(76, 175, 80, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
            text-align: center;
        }

        .success-box p {
            color: #e0e0e0;
            margin: 0;
            line-height: 1.6;
        }

        .email-display {
            background: #0f0f0f;
            border: 1px solid #333;
            border-radius: 10px;
            padding: 15px;
            text-align: center;
            margin-bottom: 20px;
        }

        .email-display i {
            color: #ffd700;
            margin-right: 10px;
        }

        .email-display strong {
            color: #ffd700;
            font-size: 1.1rem;
        }

        .warning-box {
            background: rgba(255, 165, 0, 0.1);
            border: 1px solid rgba(255, 165, 0, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .warning-box i {
            color: #ffa500;
            margin-right: 10px;
        }

        .warning-box p {
            color: #e0e0e0;
            margin: 0;
            line-height: 1.6;
        }

        .steps {
            background: rgba(255, 215, 0, 0.05);
            border: 1px solid rgba(255, 215, 0, 0.2);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .steps h5 {
            color: #ffd700;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .steps ol {
            color: #e0e0e0;
            margin: 0;
            padding-left: 20px;
        }

        .steps li {
            margin-bottom: 10px;
            line-height: 1.6;
        }

        .back-link {
            text-align: center;
            padding-top: 20px;
            border-top: 1px solid #333;
        }

        .back-link p {
            color: #888;
            margin-bottom: 10px;
            font-size: 0.95rem;
        }

        .back-link a {
            color: #888;
            text-decoration: none;
            transition: all 0.3s;
        }

        .back-link a:hover {
            color: #ffd700;
        }
    </style>
</head>
<body>
<div class="pending-card">
    <div class="pending-header">
        <i class="fas fa-envelope-circle-check"></i>
        <h2>Kiểm Tra Email Của Bạn</h2>
        <p>Email xác thực đã được gửi</p>
    </div>

    <div class="pending-body">
        <div class="success-box">
            <p>
                <strong>Email xác thực đã được gửi!</strong><br>
                Vui lòng kiểm tra hộp thư đến và click vào link để hoàn tất đăng ký.
            </p>
        </div>

        <!-- Email Display -->
        <c:if test="${not empty registeredEmail}">
            <div class="email-display">
                <i class="fas fa-paper-plane"></i>
                Email đã gửi đến: <strong>${registeredEmail}</strong>
            </div>
        </c:if>

        <!-- Warning Box -->
        <div class="warning-box">
            <p>
                <i class="fas fa-clock"></i>
                <strong>Lưu ý quan trọng:</strong> Link xác thực có hiệu lực trong <strong>24 giờ</strong>.
                Nếu không xác thực trong thời gian này, bạn sẽ cần đăng ký lại.
            </p>
        </div>

        <!-- Steps -->
        <div class="steps">
            <h5><i class="fas fa-list-check"></i> Các bước tiếp theo:</h5>
            <ol>
                <li>Mở email từ <strong>Car Showroom</strong></li>
                <li>Click vào link xác thực trong email</li>
                <li>Tài khoản sẽ được tạo và bạn có thể đăng nhập</li>
            </ol>
        </div>

        <!-- Back Link -->
        <div class="back-link">
            <p>
                <i class="fas fa-info-circle"></i>
                Không nhận được email? Kiểm tra thư mục spam
            </p>
            <a href="${pageContext.request.contextPath}/login">
                <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>