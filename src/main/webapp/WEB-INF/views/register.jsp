<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - Car Showroom</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .register-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 450px;
        }

        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .register-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .register-header p {
            color: #666;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group label .required {
            color: #e74c3c;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .error-message {
            background: #fee;
            color: #c33;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            border-left: 4px solid #c33;
        }

        .success-message {
            background: #efe;
            color: #3c3;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            border-left: 4px solid #3c3;
        }

        .password-strength {
            margin-top: 5px;
            font-size: 12px;
        }

        .password-strength .weak {
            color: #e74c3c;
        }

        .password-strength .medium {
            color: #f39c12;
        }

        .password-strength .strong {
            color: #27ae60;
        }

        .btn-register {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-register:active {
            transform: translateY(0);
        }

        .register-footer {
            text-align: center;
            margin-top: 20px;
            color: #666;
            font-size: 14px;
        }

        .register-footer a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .register-footer a:hover {
            text-decoration: underline;
        }

        .divider {
            text-align: center;
            margin: 20px 0;
            position: relative;
        }

        .divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #ddd;
        }

        .divider span {
            background: white;
            padding: 0 10px;
            position: relative;
            color: #999;
            font-size: 12px;
        }

        .input-icon {
            position: relative;
        }

        .input-icon input {
            padding-left: 40px;
        }

        .input-icon::before {
            content: attr(data-icon);
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
        }
    </style>
</head>
<body>
<div class="register-container">
    <div class="register-header">
        <h1>🚗 Car Showroom</h1>
        <p>Tạo tài khoản mới</p>
    </div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error-message">
        ❌ <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success-message">
        ✅ <%= request.getAttribute("success") %>
    </div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/register" id="registerForm">
        <div class="form-group">
            <label for="name">
                Họ và Tên <span class="required">*</span>
            </label>
            <div class="input-icon" data-icon="👤">
                <input type="text"
                       id="name"
                       name="name"
                       required
                       value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
                       placeholder="Nguyễn Văn A">
            </div>
        </div>

        <div class="form-group">
            <label for="email">
                Email <span class="required">*</span>
            </label>
            <div class="input-icon" data-icon="📧">
                <input type="email"
                       id="email"
                       name="email"
                       required
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                       placeholder="example@email.com">
            </div>
        </div>

        <div class="form-group">
            <label for="password">
                Mật khẩu <span class="required">*</span>
            </label>
            <div class="input-icon" data-icon="🔒">
                <input type="password"
                       id="password"
                       name="password"
                       required
                       minlength="6"
                       placeholder="Ít nhất 6 ký tự">
            </div>
            <div class="password-strength" id="passwordStrength"></div>
        </div>

        <div class="form-group">
            <label for="confirmPassword">
                Xác nhận mật khẩu <span class="required">*</span>
            </label>
            <div class="input-icon" data-icon="🔒">
                <input type="password"
                       id="confirmPassword"
                       name="confirmPassword"
                       required
                       minlength="6"
                       placeholder="Nhập lại mật khẩu">
            </div>
        </div>

        <button type="submit" class="btn-register">Đăng Ký</button>
    </form>

    <div class="divider">
        <span>hoặc</span>
    </div>

    <div class="register-footer">
        <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a></p>
    </div>
</div>

<script>
    // Password strength indicator
    const password = document.getElementById('password');
    const strengthDiv = document.getElementById('passwordStrength');

    password.addEventListener('input', function() {
        const val = this.value;
        let strength = '';

        if (val.length === 0) {
            strengthDiv.innerHTML = '';
        } else if (val.length < 6) {
            strengthDiv.innerHTML = '<span class="weak">⚠️ Yếu - Cần ít nhất 6 ký tự</span>';
        } else if (val.length < 10) {
            strengthDiv.innerHTML = '<span class="medium">⚡ Trung bình</span>';
        } else {
            strengthDiv.innerHTML = '<span class="strong">✅ Mạnh</span>';
        }
    });

    // Confirm password validation
    const confirmPassword = document.getElementById('confirmPassword');
    const form = document.getElementById('registerForm');

    form.addEventListener('submit', function(e) {
        if (password.value !== confirmPassword.value) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp!');
            confirmPassword.focus();
        }
    });
</script>
</body>
</html>