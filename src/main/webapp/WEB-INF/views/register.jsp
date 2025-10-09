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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .register-container {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }
        .register-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            max-width: 600px;
            width: 100%;
        }
        .password-strength {
            height: 5px;
            border-radius: 3px;
            margin-top: 5px;
            transition: all 0.3s;
        }
        .strength-weak { background: #e74c3c; width: 33%; }
        .strength-medium { background: #f39c12; width: 66%; }
        .strength-strong { background: #2ecc71; width: 100%; }
    </style>
</head>
<body>
<div class="register-container">
    <div class="register-card">
        <div class="card-body p-5">
            <!-- Logo/Brand -->
            <div class="text-center mb-4">
                <i class="fas fa-car fa-3x text-primary mb-3"></i>
                <h2 class="fw-bold">Đăng Ký Tài Khoản</h2>
                <p class="text-muted">Tạo tài khoản mới để bắt đầu</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Register Form -->
            <form method="post" action="${pageContext.request.contextPath}/register" id="registerForm">
                <input type="hidden" name="csrfToken" value="${csrfToken}">

                <!-- Name -->
                <div class="mb-3">
                    <label for="name" class="form-label fw-semibold">
                        <i class="fas fa-user"></i> Họ và Tên <span class="text-danger">*</span>
                    </label>
                    <input type="text" class="form-control" id="name" name="name"
                           required value="${name}" maxlength="100"
                           placeholder="Nguyễn Văn A">
                </div>

                <!-- Email -->
                <div class="mb-3">
                    <label for="email" class="form-label fw-semibold">
                        <i class="fas fa-envelope"></i> Email <span class="text-danger">*</span>
                    </label>
                    <input type="email" class="form-control" id="email" name="email"
                           required value="${email}" maxlength="255"
                           placeholder="example@email.com">
                </div>

                <!-- Phone -->
                <div class="mb-3">
                    <label for="phone" class="form-label fw-semibold">
                        <i class="fas fa-phone"></i> Số Điện Thoại
                    </label>
                    <input type="tel" class="form-control" id="phone" name="phone"
                           value="${phone}" maxlength="20"
                           pattern="[0-9]{10,11}"
                           placeholder="0123456789">
                    <div class="form-text">10-11 chữ số (không bắt buộc)</div>
                </div>

                <!-- Address -->
                <div class="mb-3">
                    <label for="address" class="form-label fw-semibold">
                        <i class="fas fa-map-marker-alt"></i> Địa Chỉ
                    </label>
                    <textarea class="form-control" id="address" name="address"
                              rows="2" maxlength="255"
                              placeholder="Số nhà, đường, quận/huyện, tỉnh/thành phố">${address}</textarea>
                    <div class="form-text">Tối đa 255 ký tự (không bắt buộc)</div>
                </div>

                <!-- Password -->
                <div class="mb-3">
                    <label for="password" class="form-label fw-semibold">
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
                    <label for="confirmPassword" class="form-label fw-semibold">
                        <i class="fas fa-lock"></i> Xác Nhận Mật Khẩu <span class="text-danger">*</span>
                    </label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                           required minlength="6" maxlength="100"
                           placeholder="Nhập lại mật khẩu">
                    <div id="confirmPasswordHelp" class="form-text"></div>
                </div>

                <!-- Terms & Conditions -->
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="terms" required>
                    <label class="form-check-label" for="terms">
                        Tôi đồng ý với <a href="#" class="text-decoration-none">Điều khoản sử dụng</a>
                    </label>
                </div>

                <!-- Submit Button -->
                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="fas fa-user-plus"></i> Đăng Ký
                    </button>
                </div>

                <!-- Login Link -->
                <div class="text-center">
                    <p class="text-muted mb-0">
                        Đã có tài khoản?
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold">
                            Đăng nhập ngay
                        </a>
                    </p>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="text-center text-white py-3">
    <p class="mb-0">© 2025 Car Showroom. Thiết kế bởi Nhóm PRN212.</p>
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
            strengthText.textContent = '⚠️ Mật khẩu yếu';
            strengthText.className = 'form-text text-danger';
        } else if (strength <= 3) {
            strengthBar.className = 'password-strength strength-medium';
            strengthText.textContent = '⚡ Mật khẩu trung bình';
            strengthText.className = 'form-text text-warning';
        } else {
            strengthBar.className = 'password-strength strength-strong';
            strengthText.textContent = '✅ Mật khẩu mạnh';
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
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp!');
            confirmPasswordInput.focus();
            return false;
        }

        // Check password requirements
        const hasUpper = /[A-Z]/.test(password);
        const hasLower = /[a-z]/.test(password);
        const hasDigit = /\d/.test(password);

        if (!hasUpper || !hasLower || !hasDigit) {
            e.preventDefault();
            alert('Mật khẩu phải chứa chữ hoa, chữ thường và số!');
            passwordInput.focus();
            return false;
        }

        return true;
    });

    // Phone Validation
    const phoneInput = document.getElementById('phone');
    phoneInput.addEventListener('input', function() {
        // Remove non-numeric characters
        this.value = this.value.replace(/[^0-9]/g, '');
    });
</script>
</body>
</html>