<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Cá Nhân - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
        }

        /* Profile Card */
        .profile-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.5);
            overflow: hidden;
            position: relative;
        }

        .profile-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);
        }

        .profile-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            padding: 40px;
            text-align: center;
            border-bottom: 1px solid #333;
        }

        .profile-avatar {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            border: 4px solid #333;
        }

        .profile-avatar i {
            font-size: 3rem;
            color: #1a1a1a;
        }

        .profile-header h2 {
            color: #ffd700;
            font-weight: 700;
            margin: 0;
        }

        .profile-body {
            padding: 40px;
        }

        .form-label {
            color: #e0e0e0;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .form-label i {
            color: #ffd700;
            margin-right: 8px;
        }

        .form-control, textarea.form-control {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #fff;
            padding: 12px 15px;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .form-control:focus, textarea.form-control:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
            color: #fff;
        }

        .form-control::placeholder {
            color: #666;
        }

        .form-control:disabled {
            background: #0a0a0a;
            color: #666;
            cursor: not-allowed;
        }

        .alert-success {
            background: rgba(46, 204, 113, 0.1);
            border: 1px solid rgba(46, 204, 113, 0.3);
            color: #2ecc71;
            border-radius: 10px;
        }

        .alert-danger {
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid rgba(231, 76, 60, 0.3);
            color: #ff6b6b;
            border-radius: 10px;
        }

        .btn-update {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.05rem;
        }

        .btn-update:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-back {
            background: transparent;
            border: 2px solid #ffd700;
            color: #ffd700;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.05rem;
        }

        .btn-back:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
        }

        .info-badge {
            background: #0f0f0f;
            border: 1px solid #333;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 30px;
        }

        .info-badge p {
            color: #888;
            margin: 0;
            font-size: 0.9rem;
        }

        .info-badge strong {
            color: #ffd700;
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-7">
            <div class="profile-card">
                <!-- Profile Header -->
                <div class="profile-header">
                    <div class="profile-avatar">
                        <i class="fas fa-user"></i>
                    </div>
                    <h2><i class="fas fa-id-card"></i> Hồ Sơ Cá Nhân</h2>
                </div>

                <!-- Profile Body -->
                <div class="profile-body">
                    <!-- Success/Error Messages -->
                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle"></i> ${sessionScope.success}
                        </div>
                        <c:remove var="success" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
                        </div>
                        <c:remove var="error" scope="session"/>
                    </c:if>

                    <!-- Info Badge -->
                    <div class="info-badge">
                        <p>
                            <i class="fas fa-info-circle"></i>
                            Cập nhật thông tin cá nhân của bạn.
                            <strong>Email</strong> là thông tin bắt buộc và không thể thay đổi.
                        </p>
                    </div>

                    <!-- Profile Form -->
                    <form method="post" action="${pageContext.request.contextPath}/profile">
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="fas fa-user"></i> Họ và Tên <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control"
                                   id="name"
                                   name="name"
                                   value="${user.name}"
                                   required
                                   maxlength="100"
                                   placeholder="Nguyễn Văn A">
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="fas fa-envelope"></i> Email <span class="text-danger">*</span>
                            </label>
                            <input type="email"
                                   class="form-control"
                                   id="email"
                                   name="email"
                                   value="${user.email}"
                                   disabled
                                   title="Email không thể thay đổi">
                            <small class="text-muted">
                                <i class="fas fa-lock"></i> Email không thể thay đổi
                            </small>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">
                                <i class="fas fa-phone"></i> Số Điện Thoại
                            </label>
                            <input type="tel"
                                   class="form-control"
                                   id="phone"
                                   name="phone"
                                   value="${user.phone}"
                                   pattern="[0-9]{10,11}"
                                   maxlength="20"
                                   placeholder="0123456789">
                            <small class="text-muted">10-11 chữ số (không bắt buộc)</small>
                        </div>

                        <div class="mb-4">
                            <label for="address" class="form-label">
                                <i class="fas fa-map-marker-alt"></i> Địa Chỉ
                            </label>
                            <textarea class="form-control"
                                      id="address"
                                      name="address"
                                      rows="3"
                                      maxlength="255"
                                      placeholder="Nhập địa chỉ của bạn...">${user.address}</textarea>
                            <small class="text-muted">Tối đa 255 ký tự (không bắt buộc)</small>
                        </div>

                        <div class="d-grid gap-3">
                            <button type="submit" class="btn btn-update">
                                <i class="fas fa-save"></i> Cập Nhật Thông Tin
                            </button>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-back">
                                <i class="fas fa-arrow-left"></i> Quay Lại Trang Chủ
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Phone number validation - only allow numbers
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }
</script>
</body>
</html>