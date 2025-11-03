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
    <title>Quên mật khẩu - Car Showroom</title>
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

        .forgot-container {
            position: relative;
            z-index: 1;
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .forgot-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 500px;
            width: 100%;
            position: relative;
            overflow: hidden;
        }

        .forgot-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .forgot-header {
            text-align: center;
            padding: 40px 40px 20px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .forgot-header i {
            font-size: 3rem;
            color: #ffd700;
            margin-bottom: 15px;
        }

        .forgot-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .forgot-header p {
            color: #888;
            margin: 0;
        }

        .forgot-body {
            padding: 40px;
        }

        .form-label {
            color: #e0e0e0;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .form-control {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #fff;
            padding: 12px 15px;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .form-control:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
            color: #fff;
        }

        .form-control::placeholder {
            color: #666;
        }

        .input-group i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #888;
            z-index: 10;
        }

        .input-group .form-control {
            padding-left: 45px;
        }

        .btn-reset {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-reset:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .alert-success {
            background: rgba(46, 204, 113, 0.1);
            border: 1px solid rgba(46, 204, 113, 0.3);
            color: #2ecc71;
            border-radius: 10px;
        }

        .alert-danger {
            background: rgba(220, 53, 69, 0.1);
            border: 1px solid rgba(220, 53, 69, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
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
<div class="forgot-container">
    <div class="forgot-card">
        <div class="forgot-header">
            <i class="fas fa-key"></i>
            <h2>Quên mật khẩu?</h2>
            <p>Nhập email của bạn để nhận link đặt lại mật khẩu</p>
        </div>

        <div class="forgot-body">
            <c:if test="${not empty success}">
                <div class="alert alert-success" role="alert">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/forgot-password">
                <input type="hidden" name="csrfToken" value="${csrfToken}">

                <div class="mb-4">
                    <label for="email" class="form-label">
                        <i class="fas fa-envelope"></i> Email
                    </label>
                    <div class="input-group">
                        <i class="fas fa-envelope"></i>
                        <input type="email"
                               class="form-control"
                               id="email"
                               name="email"
                               required
                               placeholder="example@email.com"
                               autocomplete="email">
                    </div>
                </div>

                <button type="submit" class="btn btn-reset w-100">
                    <i class="fas fa-paper-plane"></i> Gửi link đặt lại mật khẩu
                </button>
            </form>

            <div class="back-link">
                <a href="${pageContext.request.contextPath}/login">
                    <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
                </a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
