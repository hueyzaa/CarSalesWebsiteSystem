<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 03/11/2025
  Time: 1:40 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực email - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
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

        .verification-container {
            position: relative;
            z-index: 1;
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .verification-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 600px;
            width: 100%;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .verification-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .verification-icon {
            font-size: 5rem;
            color: #ffd700;
            margin: 40px 0 20px;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }

        .verification-card h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .verification-card p {
            color: #888;
            font-size: 1.1rem;
            margin-bottom: 20px;
        }

        .email-display {
            background: #0f0f0f;
            border: 1px solid #333;
            border-radius: 10px;
            padding: 15px;
            margin: 20px 40px;
            color: #ffd700;
            font-weight: 600;
            font-size: 1.1rem;
        }

        .steps-container {
            text-align: left;
            padding: 30px 40px;
            border-top: 1px solid #333;
            border-bottom: 1px solid #333;
            margin: 20px 0;
        }

        .step {
            display: flex;
            align-items: flex-start;
            margin-bottom: 20px;
        }

        .step:last-child {
            margin-bottom: 0;
        }

        .step-number {
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #1a1a1a;
            font-weight: 700;
            font-size: 1.2rem;
            flex-shrink: 0;
        }

        .step-content {
            margin-left: 15px;
        }

        .step-content h5 {
            color: #f8f9fa;
            margin-bottom: 5px;
            font-weight: 600;
        }

        .step-content p {
            color: #888;
            margin: 0;
            font-size: 0.95rem;
        }

        .alert-info {
            background: rgba(13, 202, 240, 0.1);
            border: 1px solid rgba(13, 202, 240, 0.3);
            color: #0dcaf0;
            border-radius: 10px;
            margin: 20px 40px;
        }

        .btn-resend {
            background: transparent;
            border: 2px solid #ffd700;
            color: #ffd700;
            padding: 12px 30px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s;
            margin: 20px 0;
        }

        .btn-resend:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
        }

        .btn-back {
            color: #888;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 10px 20px;
            transition: all 0.3s;
            margin-top: 20px;
        }

        .btn-back:hover {
            color: #ffd700;
        }
    </style>
</head>
<body>
<div class="verification-container">
    <div class="verification-card">
        <div class="verification-icon">
            <i class="fas fa-envelope-open-text"></i>
        </div>

        <h2>Kiểm tra email của bạn</h2>
        <p>Chúng tôi đã gửi link xác thực đến địa chỉ email:</p>

        <div class="email-display">
            <i class="fas fa-envelope"></i>
            <%= session.getAttribute("registeredEmail") %>
        </div>

        <div class="steps-container">
            <div class="step">
                <div class="step-number">1</div>
                <div class="step-content">
                    <h5>Mở email</h5>
                    <p>Kiểm tra hộp thư đến (và cả thư rác nếu không thấy)</p>
                </div>
            </div>

            <div class="step">
                <div class="step-number">2</div>
                <div class="step-content">
                    <h5>Nhấn vào link xác thực</h5>
                    <p>Click vào nút "Xác thực Email" trong email</p>
                </div>
            </div>

            <div class="step">
                <div class="step-number">3</div>
                <div class="step-content">
                    <h5>Hoàn tất</h5>
                    <p>Đăng nhập và bắt đầu trải nghiệm dịch vụ</p>
                </div>
            </div>
        </div>

        <div class="alert alert-info">
            <i class="fas fa-clock"></i>
            Link xác thực có hiệu lực trong <strong>24 giờ</strong>
        </div>

        <div style="padding: 20px 40px 40px;">
            <p style="color: #888; margin-bottom: 15px;">
                Không nhận được email?
            </p>
            <button class="btn btn-resend" onclick="resendEmail()">
                <i class="fas fa-paper-plane"></i> Gửi lại email xác thực
            </button>

            <div>
                <a href="${pageContext.request.contextPath}/login" class="btn-back">
                    <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
                </a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function resendEmail() {
        const email = '<%= session.getAttribute("registeredEmail") %>';
        window.location.href = '${pageContext.request.contextPath}/resend-verification?email=' + encodeURIComponent(email);
    }
</script>
</body>
</html>
