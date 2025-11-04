<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Car Showroom</title>
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

        .navbar {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 15px 0;
            box-shadow: 0 4px 20px rgba(0,0,0,0.5);
            border-bottom: 1px solid #333;
        }

        .navbar-brand {
            font-size: 1.5rem;
            font-weight: bold;
            color: #f8f9fa !important;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .navbar-brand i {
            color: #ffd700;
        }

        .nav-link {
            color: #e0e0e0 !important;
            font-weight: 500;
            transition: all 0.3s;
        }

        .nav-link:hover {
            color: #ffd700 !important;
        }

        .btn-nav-register {
            background: linear-gradient(135deg, #c9a944 0%, #b89532 100%);
            color: #fff;
            padding: 8px 20px;
            border-radius: 25px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-nav-register:hover {
            background: linear-gradient(135deg, #d4b555 0%, #c9a944 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(201, 169, 68, 0.4);
            color: #fff;
        }

        .login-container {
            position: relative;
            z-index: 1;
            min-height: calc(100vh - 80px);
            display: flex;
            align-items: center;
            padding: 40px 0;
        }

        .login-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            overflow: hidden;
            position: relative;
            width: 100%;
        }

        .login-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .login-header {
            text-align: center;
            padding: 40px 40px 30px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .login-header i {
            font-size: 3.5rem;
            color: #ffd700;
            margin-bottom: 20px;
        }

        .login-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
            font-size: 1.8rem;
        }

        .login-header p {
            color: #888;
            margin: 0;
            font-size: 1rem;
        }

        .login-body {
            padding: 45px;
        }

        @media (max-width: 576px) {
            .login-body {
                padding: 30px 25px;
            }
            .login-header {
                padding: 30px 25px 25px;
            }
        }

        .form-label {
            color: #e0e0e0;
            font-weight: 600;
            margin-bottom: 10px;
            font-size: 0.95rem;
        }

        .form-control {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #fff;
            padding: 14px 15px;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1rem;
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

        .input-group {
            position: relative;
        }

        .input-group i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #888;
            z-index: 10;
            font-size: 1.1rem;
        }

        .input-group .form-control {
            padding-left: 48px;
        }

        .btn-google {
            background: #fff;
            border: 2px solid #333;
            color: #3c4043;
            padding: 14px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.05rem;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
            text-decoration: none;
            margin-bottom: 10px;
        }

        .btn-google:hover {
            background: #f8f9fa;
            border-color: #555;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 255, 255, 0.15);
            color: #3c4043;
        }

        .btn-google i.fab.fa-google {
            font-size: 1.3rem;
            background: linear-gradient(135deg, #4285f4 0%, #34a853 25%, #fbbc05 50%, #ea4335 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .btn-google.loading {
            pointer-events: none;
            opacity: 0.7;
        }

        .oauth-info {
            text-align: center;
            margin-bottom: 20px;
        }

        .oauth-info small {
            color: #888;
            font-size: 0.88rem;
        }

        .oauth-info i {
            font-size: 0.85rem;
            margin-right: 5px;
        }

        .btn-login {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 14px;
            font-weight: 700;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-login:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .alert-danger {
            background: rgba(220, 53, 69, 0.1);
            border: 1px solid rgba(220, 53, 69, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
            padding: 14px 16px;
        }

        .alert-success {
            background: rgba(40, 167, 69, 0.1);
            border: 1px solid rgba(40, 167, 69, 0.3);
            color: #51cf66;
            border-radius: 10px;
            padding: 14px 16px;
        }

        .divider {
            text-align: center;
            margin: 28px 0;
            position: relative;
        }

        .divider::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            width: 100%;
            height: 1px;
            background: #333;
        }

        .divider span {
            background: #1a1a1a;
            color: #888;
            padding: 0 18px;
            position: relative;
            z-index: 1;
            font-size: 0.92rem;
        }

        .register-link {
            text-align: center;
            margin-top: 25px;
            padding-top: 25px;
            border-top: 1px solid #333;
        }

        .register-link p {
            color: #888;
            margin: 0;
            font-size: 0.98rem;
        }

        .register-link a {
            color: #ffd700;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
        }

        .register-link a:hover {
            color: #ffed4e;
            text-decoration: underline;
        }

        .back-home {
            color: #888;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
            transition: all 0.3s;
            font-size: 0.95rem;
        }

        .back-home:hover {
            color: #ffd700;
        }

        .back-home i {
            transition: transform 0.3s;
        }

        .back-home:hover i {
            transform: translateX(-5px);
        }

        .forgot-password-link {
            text-align: right;
            margin-top: -8px;
            margin-bottom: 22px;
        }

        .forgot-password-link a {
            color: #888;
            text-decoration: none;
            font-size: 0.92rem;
            transition: all 0.3s;
        }

        .forgot-password-link a:hover {
            color: #ffd700;
            text-decoration: underline;
        }

        .mb-3 {
            margin-bottom: 1.2rem !important;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <i class="fas fa-car"></i> Car Showroom
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">
                        <i class="fas fa-home"></i> Trang Chủ
                    </a>
                </li>
                <li class="nav-item ms-2">
                    <a class="nav-link btn-nav-register" href="${pageContext.request.contextPath}/register">
                        <i class="fas fa-user-plus"></i> Đăng Ký
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Login Form -->
<div class="login-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <a href="${pageContext.request.contextPath}/" class="back-home">
                    <i class="fas fa-arrow-left"></i>
                    <span>Quay lại trang chủ</span>
                </a>

                <div class="login-card">
                    <div class="login-header">
                        <i class="fas fa-user-circle"></i>
                        <h2>Đăng Nhập</h2>
                        <p>Chào mừng bạn quay trở lại!</p>
                    </div>

                    <div class="login-body">
                        <!-- Error Message -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-circle"></i> ${error}
                            </div>
                        </c:if>

                        <!-- Success Message -->
                        <c:if test="${not empty success}">
                            <div class="alert alert-success" role="alert">
                                <i class="fas fa-check-circle"></i> ${success}
                            </div>
                        </c:if>

                        <!-- Google Login Button -->
                        <a href="${pageContext.request.contextPath}/oauth2/google"
                           class="btn btn-google w-100"
                           id="googleLoginBtn">
                            <i class="fab fa-google"></i>
                            <span>Đăng nhập với Google</span>
                        </a>

                        <!-- Info Text -->
                        <div class="oauth-info">
                            <small>
                                <i class="fas fa-info-circle"></i>
                                Chỉ dành cho tài khoản đăng ký bằng Google
                            </small>
                        </div>

                        <!-- Divider -->
                        <div class="divider">
                            <span>hoặc đăng nhập với email</span>
                        </div>

                        <!-- Email/Password Login Form -->
                        <form method="post" action="${pageContext.request.contextPath}/login">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">

                            <div class="mb-3">
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
                                           autocomplete="email"
                                           value="${email}">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label">
                                    <i class="fas fa-lock"></i> Mật khẩu
                                </label>
                                <div class="input-group">
                                    <i class="fas fa-lock"></i>
                                    <input type="password"
                                           class="form-control"
                                           id="password"
                                           name="password"
                                           required
                                           placeholder="Nhập mật khẩu của bạn"
                                           autocomplete="current-password">
                                </div>
                            </div>

                            <div class="forgot-password-link">
                                <a href="${pageContext.request.contextPath}/forgot-password">
                                    <i class="fas fa-key"></i> Quên mật khẩu?
                                </a>
                            </div>

                            <button type="submit" class="btn btn-login w-100">
                                <i class="fas fa-sign-in-alt"></i> Đăng Nhập
                            </button>
                        </form>

                        <!-- Register Link -->
                        <div class="register-link">
                            <p>
                                Chưa có tài khoản?
                                <a href="${pageContext.request.contextPath}/register">
                                    Đăng ký ngay <i class="fas fa-arrow-right"></i>
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Google Login Button with loading state
    document.getElementById('googleLoginBtn').addEventListener('click', function(e) {
        this.classList.add('loading');
        const span = this.querySelector('span');
        const originalText = span.textContent;
        span.textContent = 'Đang chuyển hướng...';

        setTimeout(() => {
            this.classList.remove('loading');
            span.textContent = originalText;
        }, 5000);
    });

    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
</script>
</body>
</html>