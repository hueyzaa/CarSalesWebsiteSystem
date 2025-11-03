<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - Car Showroom</title>
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
                    radial-gradient(circle at 20% 50%, rgba(255, 215, 0, 0.05) 0%, transparent 50%),
                    radial-gradient(circle at 80% 80%, rgba(255, 215, 0, 0.05) 0%, transparent 50%);
            pointer-events: none;
        }

        /* Navbar */
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

        .btn-nav-login {
            background: transparent;
            border: 1px solid #ffd700;
            color: #ffd700;
            padding: 8px 20px;
            border-radius: 25px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-nav-login:hover {
            background: #ffd700;
            color: #1a1a1a;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }

        /* Register Container */
        .register-container {
            position: relative;
            z-index: 1;
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .register-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 650px;
            width: 100%;
            position: relative;
            overflow: hidden;
        }

        .register-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .register-header {
            text-align: center;
            padding: 40px 40px 20px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .register-header i {
            font-size: 3rem;
            color: #ffd700;
            margin-bottom: 15px;
        }

        .register-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .register-header p {
            color: #888;
            margin: 0;
        }

        .register-body {
            padding: 40px;
        }

        .form-label {
            color: #e0e0e0;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .form-control, .form-select {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #fff;
            padding: 12px 15px;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .form-control:focus, .form-select:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
            color: #fff;
        }

        .form-control::placeholder {
            color: #666;
        }

        .input-group-text {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #888;
        }

        .btn-outline-secondary {
            background: #0f0f0f;
            border-color: #333;
            color: #888;
        }

        .btn-outline-secondary:hover {
            background: #ffd700;
            border-color: #ffd700;
            color: #1a1a1a;
        }

        .form-text {
            color: #666;
            font-size: 0.85rem;
        }

        .password-strength {
            height: 5px;
            border-radius: 3px;
            margin-top: 8px;
            transition: all 0.3s;
            background: #333;
        }

        .strength-weak {
            background: #e74c3c;
            width: 33%;
        }

        .strength-medium {
            background: #f39c12;
            width: 66%;
        }

        .strength-strong {
            background: #2ecc71;
            width: 100%;
        }

        .form-check-input {
            background-color: #0f0f0f;
            border-color: #333;
        }

        .form-check-input:checked {
            background-color: #ffd700;
            border-color: #ffd700;
        }

        .form-check-label {
            color: #e0e0e0;
        }

        .form-check-label a {
            color: #ffd700;
            text-decoration: none;
        }

        .form-check-label a:hover {
            color: #ffed4e;
            text-decoration: underline;
        }

        .btn-register {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-register:hover {
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
        }

        .login-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #333;
        }

        .login-link p {
            color: #888;
            margin: 0;
        }

        .login-link a {
            color: #ffd700;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
        }

        .login-link a:hover {
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

        footer {
            background: #1a1a1a;
            border-top: 1px solid #333;
            padding: 20px 0;
            margin-top: auto;
        }

        footer p {
            color: #888;
            margin: 0;
        }

        .is-valid {
            border-color: #2ecc71 !important;
        }

        .is-invalid {
            border-color: #e74c3c !important;
        }

        .text-success {
            color: #2ecc71 !important;
        }

        .text-danger {
            color: #e74c3c !important;
        }

        .text-warning {
            color: #f39c12 !important;
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
                    <a class="nav-link btn-nav-login" href="${pageContext.request.contextPath}/login">
                        <i class="fas fa-sign-in-alt"></i> Đăng Nhập
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="register-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-7">
                <a href="${pageContext.request.contextPath}/" class="back-home">
                    <i class="fas fa-arrow-left"></i>
                    <span>Quay lại trang chủ</span>
                </a>

                <div class="register-card">
                    <div class="register-header">
                        <i class="fas fa-user-plus"></i>
                        <h2>Đăng Ký Tài Khoản</h2>
                        <p>Tạo tài khoản mới để bắt đầu</p>
                    </div>

                    <div class="register-body">
                        <!-- Error Message -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle"></i> ${error}
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <!-- Register Form -->
                        <form method="post" action="${pageContext.request.contextPath}/register" id="registerForm">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">

                            <div class="row">
                                <!-- Name -->
                                <div class="col-md-6 mb-3">
                                    <label for="name" class="form-label">
                                        <i class="fas fa-user"></i> Họ và Tên <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="name" name="name"
                                           required value="${name}" maxlength="100"
                                           placeholder="Nguyễn Văn A">
                                </div>

                                <!-- Email -->
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">
                                        <i class="fas fa-envelope"></i> Email <span class="text-danger">*</span>
                                    </label>
                                    <input type="email" class="form-control" id="email" name="email"
                                           required value="${email}" maxlength="255"
                                           placeholder="example@email.com">
                                </div>
                            </div>

                            <div class="row">
                                <!-- Phone -->
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">
                                        <i class="fas fa-phone"></i> Số Điện Thoại
                                    </label>
                                    <input type="tel" class="form-control" id="phone" name="phone"
                                           value="${phone}" maxlength="20"
                                           pattern="[0-9]{10,11}"
                                           placeholder="0123456789">
                                    <div class="form-text">10-11 chữ số (không bắt buộc)</div>
                                </div>

                                <!-- Address -->
                                <div class="col-md-6 mb-3">
                                    <label for="address" class="form-label">
                                        <i class="fas fa-map-marker-alt"></i> Địa Chỉ
                                    </label>
                                    <input type="text" class="form-control" id="address" name="address"
                                           value="${address}" maxlength="255"
                                           placeholder="Cần Thơ, Việt Nam">
                                    <div class="form-text">Tối đa 255 ký tự (không bắt buộc)</div>
                                </div>
                            </div>

                            <!-- Password -->
                            <div class="mb-3">
                                <label for="password" class="form-label">
                                    <i class="fas fa-lock"></i> Mật Khẩu <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="password" name="password"
                                           required minlength="6" maxlength="100"
                                           placeholder="Ít nhất 6 ký tự">
                                    <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                        <i class="fas fa-eye" id="eyeIcon"></i>
                                    </button>
                                </div>
                                <div id="passwordStrength" class="password-strength"></div>
                                <div id="passwordHelp" class="form-text"></div>
                            </div>

                            <!-- Confirm Password -->
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">
                                    <i class="fas fa-lock"></i> Xác Nhận Mật Khẩu <span class="text-danger">*</span>
                                </label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                       required minlength="6" maxlength="100"
                                       placeholder="Nhập lại mật khẩu">
                                <div id="confirmPasswordHelp" class="form-text"></div>
                            </div>

                            <!-- Terms & Conditions -->
                            <div class="mb-4 form-check">
                                <input type="checkbox" class="form-check-input" id="terms" required>
                                <label class="form-check-label" for="terms">
                                    Tôi đồng ý với <a href="#">Điều khoản sử dụng</a>
                                </label>
                            </div>

                            <!-- Submit Button -->
                            <div class="d-grid mb-3">
                                <button type="submit" class="btn btn-register">
                                    <i class="fas fa-user-plus"></i> Đăng Ký
                                </button>
                            </div>
                        </form>

                        <!-- Login Link -->
                        <div class="login-link">
                            <p>
                                Đã có tài khoản?
                                <a href="${pageContext.request.contextPath}/login">
                                    Đăng nhập ngay <i class="fas fa-arrow-right"></i>
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="text-center">
    <p>© 2025 Car Showroom. Bảo lưu mọi quyền.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Toggle Password Visibility
    document.getElementById('togglePassword').addEventListener('click', function() {
        const passwordInput = document.getElementById('password');
        const eyeIcon = document.getElementById('eyeIcon');

        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            eyeIcon.classList.remove('fa-eye');
            eyeIcon.classList.add('fa-eye-slash');
        } else {
            passwordInput.type = 'password';
            eyeIcon.classList.remove('fa-eye-slash');
            eyeIcon.classList.add('fa-eye');
        }
    });

    // Password Strength Indicator
    const passwordInput = document.getElementById('password');
    const strengthBar = document.getElementById('passwordStrength');
    const strengthText = document.getElementById('passwordHelp');

    passwordInput.addEventListener('input', function() {
        const password = this.value;

        if (password.length === 0) {
            strengthBar.className = 'password-strength';
            strengthText.textContent = '';
            return;
        }

        // Calculate strength
        let strength = 0;
        if (password.length >= 6) strength++;
        if (password.length >= 10) strength++;
        if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++;
        if (/\d/.test(password)) strength++;
        if (/[^a-zA-Z0-9]/.test(password)) strength++;

        // Update UI
        if (strength <= 2) {
            strengthBar.className = 'password-strength strength-weak';
            strengthText.textContent = 'Mật khẩu yếu';
            strengthText.className = 'form-text text-danger';
        } else if (strength <= 3) {
            strengthBar.className = 'password-strength strength-medium';
            strengthText.textContent = '⚡ Mật khẩu trung bình';
            strengthText.className = 'form-text text-warning';
        } else {
            strengthBar.className = 'password-strength strength-strong';
            strengthText.textContent = 'Mật khẩu mạnh';
            strengthText.className = 'form-text text-success';
        }
    });

    // Confirm Password Match
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const confirmHelp = document.getElementById('confirmPasswordHelp');

    confirmPasswordInput.addEventListener('input', function() {
        const password = passwordInput.value;
        const confirmPassword = this.value;

        if (confirmPassword.length === 0) {
            confirmHelp.textContent = '';
            this.classList.remove('is-valid', 'is-invalid');
            return;
        }

        if (password === confirmPassword) {
            confirmHelp.textContent = 'Mật khẩu khớp';
            confirmHelp.className = 'form-text text-success';
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            confirmHelp.textContent = 'Mật khẩu không khớp';
            confirmHelp.className = 'form-text text-danger';
            this.classList.remove('is-valid');
            this.classList.add('is-invalid');
        }
    });

    // Form Validation
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp!');
            confirmPasswordInput.focus();
            return false;
        }

        return true;
    });

    // Phone Validation
    const phoneInput = document.getElementById('phone');
    phoneInput.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
</script>
</body>
</html>