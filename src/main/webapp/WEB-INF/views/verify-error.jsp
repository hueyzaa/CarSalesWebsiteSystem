<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác Thực Thất Bại - Car Showroom</title>
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

        .error-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 500px;
            width: 100%;
            overflow: hidden;
            position: relative;
        }

        .error-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #dc3545 0%, #ff6b6b 50%, #dc3545 100%);
        }

        .error-header {
            text-align: center;
            padding: 40px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .error-header i {
            font-size: 5rem;
            color: #dc3545;
            margin-bottom: 20px;
        }

        .error-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .error-body {
            padding: 40px;
        }

        .error-message {
            background: rgba(220, 53, 69, 0.1);
            border: 1px solid rgba(220, 53, 69, 0.3);
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            margin-bottom: 30px;
        }

        .error-message p {
            color: #ff6b6b;
            margin: 0;
            font-size: 1.1rem;
        }

        .solution-box {
            background: rgba(255, 165, 0, 0.1);
            border: 1px solid rgba(255, 165, 0, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .solution-box h5 {
            color: #ffa500;
            margin-bottom: 15px;
        }

        .solution-box p {
            color: #e0e0e0;
            margin: 0;
            line-height: 1.6;
        }

        .btn-register {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
            width: 100%;
            text-align: center;
        }

        .btn-register:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .back-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #333;
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
<div class="error-card">
    <div class="error-header">
        <i class="fas fa-circle-xmark"></i>
        <h2>Xác Thực Thất Bại</h2>
    </div>

    <div class="error-body">
        <!-- Error Message -->
        <div class="error-message">
            <p>
                <i class="fas fa-exclamation-triangle"></i>
                <c:choose>
                    <c:when test="${not empty error}">
                        ${error}
                    </c:when>
                    <c:otherwise>
                        Link xác thực không hợp lệ hoặc đã hết hạn
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <!-- Solution Box -->
        <div class="solution-box">
            <h5><i class="fas fa-lightbulb"></i> Giải pháp</h5>
            <p>
                Link xác thực có thể đã hết hạn (24 giờ) hoặc phiên làm việc đã kết thúc.
                <strong>Vui lòng đăng ký lại để nhận email xác thực mới.</strong>
            </p>
        </div>

        <!-- Register Button -->
        <a href="${pageContext.request.contextPath}/register" class="btn-register">
            <i class="fas fa-user-plus"></i> Đăng Ký Lại
        </a>

        <!-- Back Link -->
        <div class="back-link">
            <a href="${pageContext.request.contextPath}/login">
                <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>