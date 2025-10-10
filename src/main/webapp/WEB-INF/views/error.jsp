<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lỗi - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background: linear-gradient(135deg, #0f0f0f 0%, #1a1a1a 100%);
            position: relative;
            overflow-x: hidden;
        }

        /* Background Pattern */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image:
                    radial-gradient(circle at 20% 50%, rgba(231, 76, 60, 0.05) 0%, transparent 50%),
                    radial-gradient(circle at 80% 80%, rgba(231, 76, 60, 0.05) 0%, transparent 50%);
            pointer-events: none;
        }

        .error-container {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
            position: relative;
            z-index: 1;
        }

        .error-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 650px;
            width: 100%;
            position: relative;
            overflow: hidden;
        }

        .error-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #e74c3c 0%, #c0392b 50%, #e74c3c 100%);
        }

        .error-card .card-body {
            padding: 60px 40px;
        }

        .error-icon {
            font-size: 5rem;
            color: #e74c3c;
            margin-bottom: 20px;
            animation: shake 0.5s ease-in-out;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        .error-card h1 {
            color: #e74c3c;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .error-card h3 {
            color: #e74c3c;
            font-weight: 600;
            margin-bottom: 30px;
        }

        .alert-danger {
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid rgba(231, 76, 60, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
        }

        .alert-danger strong {
            color: #e74c3c;
        }

        .btn-back {
            background: transparent;
            border: 2px solid #e74c3c;
            color: #e74c3c;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background: #e74c3c;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(231, 76, 60, 0.4);
        }

        .btn-home {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
        }

        .btn-home:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .help-text {
            color: #888;
            margin-top: 30px;
        }

        .help-text a {
            color: #ffd700;
            text-decoration: none;
            transition: all 0.3s;
        }

        .help-text a:hover {
            color: #ffed4e;
            text-decoration: underline;
        }

        footer {
            background: #1a1a1a;
            border-top: 1px solid #333;
            color: #888;
            text-align: center;
            padding: 20px;
            position: relative;
            z-index: 1;
        }

        footer p {
            margin: 0;
        }

        /* Animated background */
        .error-bg-shape {
            position: fixed;
            border-radius: 50%;
            opacity: 0.1;
            animation: float 20s infinite;
        }

        .shape-1 {
            width: 300px;
            height: 300px;
            background: #e74c3c;
            top: 10%;
            left: -100px;
            animation-delay: 0s;
        }

        .shape-2 {
            width: 200px;
            height: 200px;
            background: #e74c3c;
            bottom: 20%;
            right: -50px;
            animation-delay: 5s;
        }

        @keyframes float {
            0%, 100% { transform: translate(0, 0) scale(1); }
            25% { transform: translate(50px, 50px) scale(1.1); }
            50% { transform: translate(-30px, 80px) scale(0.9); }
            75% { transform: translate(70px, -40px) scale(1.05); }
        }
    </style>
</head>
<body>
<!-- Animated background shapes -->
<div class="error-bg-shape shape-1"></div>
<div class="error-bg-shape shape-2"></div>

<div class="error-container">
    <div class="error-card">
        <div class="card-body text-center">
            <i class="fas fa-exclamation-triangle error-icon"></i>

            <h1 class="display-4">Oops!</h1>

            <h3>Đã Xảy Ra Lỗi</h3>

            <div class="alert alert-danger mb-4" role="alert">
                <i class="fas fa-info-circle"></i>
                <strong>Chi tiết lỗi:</strong><br>
                <c:choose>
                    <c:when test="${not empty error}">
                        ${error}
                    </c:when>
                    <c:otherwise>
                        Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="d-flex gap-3 justify-content-center flex-wrap">
                <button onclick="window.history.back()" class="btn btn-back">
                    <i class="fas fa-arrow-left"></i> Quay Lại
                </button>
                <a href="${pageContext.request.contextPath}/" class="btn btn-home">
                    <i class="fas fa-home"></i> Trang Chủ
                </a>
            </div>

            <div class="help-text">
                <small>
                    <i class="fas fa-question-circle"></i>
                    Nếu vấn đề vẫn tiếp tục, vui lòng
                    <a href="${pageContext.request.contextPath}/contact">
                        liên hệ hỗ trợ
                    </a>
                </small>
            </div>
        </div>
    </div>
</div>

<footer>
    <p>© 2025 Car Showroom. Bảo lưu mọi quyền.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>