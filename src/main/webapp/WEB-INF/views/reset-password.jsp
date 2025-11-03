<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 03/11/2025
  Time: 1:42 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - Car Showroom</title>
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

        .reset-container {
            position: relative;
            z-index: 1;
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .reset-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            max-width: 500px;
            width: 100%;
            position: relative;
            overflow: hidden;
        }

        .reset-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .reset-header {
            text-align: center;
            padding: 40px 40px 20px;
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border-bottom: 1px solid #333;
        }

        .reset-header i {
            font-size: 3rem;
            color: #ffd700;
            margin-bottom: 15px;
        }

        .reset-header h2 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .reset-header p {
            color: #888;
            margin: 0;
        }

        .reset-body {
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

        .alert-danger {
            background: rgba(220, 53, 69, 0.1);
            border: 1px solid rgba(220, 53, 69, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
        }

        .form-text {
            color: #666;
            font-size: 0.85rem;
        }

        .text-success {
            color: #2ecc71 !important;
        }

        .text-danger {
            color: #e74c3c !important;
        }

        .is-valid {
            border-color: #2ecc71 !important;
        }

        .is-invalid {
            border-color: #e74c3c !important;
        }
    </style>
</head>
<body>
<div class="reset-container">
    <div class="reset-card">
        <div class="reset-header">
            <i class="fas fa-lock"></i>
            <h2>Đặt lại mật khẩu</h2>
            <p>Nhập mật khẩu mới cho tài khoản của bạn</p>
        </div>

        <div class="reset-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/reset-password" id="resetForm">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="token" value="${token}">

                <!-- New Password -->
                <div class="mb-3">
                    <label for="password" class="form-label">
                        <i class="fas fa-lock"></i> Mật khẩu mới <span class="text-danger">*</span>
                    </label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="password" name="password"
                               required minlength="6" maxlength="100"
                               placeholder="Ít nhất 6 ký tự">
                        <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                            <i class="fas fa-eye" id="eyeIcon"></i>
                        </button>
                    </div>
                    <div id="passwordHelp" class="form-text"></div>
                </div>

                <!-- Confirm Password -->
                <div class="mb-4">
                    <label for="confirmPassword" class="form-label">
                        <i class="fas fa-lock"></i> Xác nhận mật khẩu <span class="text-danger">*</span>
                    </label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                           required minlength="6" maxlength="100"
                           placeholder="Nhập lại mật khẩu">
                    <div id="confirmPasswordHelp" class="form-text"></div>
                </div>

                <button type="submit" class="btn btn-reset w-100">
                    <i class="fas fa-check-circle"></i> Đặt lại mật khẩu
                </button>
            </form>
        </div>
    </div>
</div>

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

    // Confirm Password Match
    const passwordInput = document.getElementById('password');
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
            confirmHelp.textContent = '✅ Mật khẩu khớp';
            confirmHelp.className = 'form-text text-success';
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
        } else {
            confirmHelp.textContent = '❌ Mật khẩu không khớp';
            confirmHelp.className = 'form-text text-danger';
            this.classList.remove('is-valid');
            this.classList.add('is-invalid');
        }
    });

    // Form Validation
    document.getElementById('resetForm').addEventListener('submit', function(e) {
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
</script>
</body>
</html>