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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .error-container {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .error-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            max-width: 600px;
            width: 100%;
        }
        .error-icon {
            font-size: 5rem;
            color: #e74c3c;
            margin-bottom: 20px;
        }
        footer {
            background-color: rgba(0,0,0,0.2);
            color: white;
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-card">
        <div class="card-body text-center p-5">
            <i class="fas fa-exclamation-triangle error-icon"></i>

            <h1 class="display-4 fw-bold mb-3">Oops!</h1>

            <h3 class="text-danger mb-4">Đã Xảy Ra Lỗi</h3>

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

            <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                <button onclick="window.history.back()" class="btn btn-outline-primary btn-lg">
                    <i class="fas fa-arrow-left"></i> Quay Lại
                </button>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                    <i class="fas fa-home"></i> Trang Chủ
                </a>
            </div>

            <div class="mt-4 text-muted">
                <small>
                    <i class="fas fa-question-circle"></i>
                    Nếu vấn đề vẫn tiếp tục, vui lòng
                    <a href="${pageContext.request.contextPath}/contact" class="text-decoration-none">
                        liên hệ hỗ trợ
                    </a>
                </small>
            </div>
        </div>
    </div>
</div>

<footer>
    <p class="mb-0">© 2025 Car Showroom. Thiết kế bởi Nhóm PRN212.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>