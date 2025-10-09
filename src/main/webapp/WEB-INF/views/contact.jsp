<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 09/10/2025
  Time: 1:59 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liên Hệ - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<div class="container my-5">
    <div class="row">
        <div class="col-lg-6 mb-4">
            <h1 class="mb-4"><i class="fas fa-envelope"></i> Liên Hệ Với Chúng Tôi</h1>

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success" role="alert">
                    <i class="fas fa-check-circle"></i> ${sessionScope.success}
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/contact">
                <div class="mb-3">
                    <label for="name" class="form-label">
                        <i class="fas fa-user"></i> Họ và Tên
                    </label>
                    <input type="text" class="form-control" id="name" name="name"
                           required placeholder="Nhập họ và tên của bạn">
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">
                        <i class="fas fa-envelope"></i> Email
                    </label>
                    <input type="email" class="form-control" id="email" name="email"
                           required placeholder="Nhập email của bạn">
                </div>

                <div class="mb-3">
                    <label for="subject" class="form-label">
                        <i class="fas fa-tag"></i> Chủ Đề
                    </label>
                    <input type="text" class="form-control" id="subject" name="subject"
                           required placeholder="Nhập chủ đề">
                </div>

                <div class="mb-3">
                    <label for="message" class="form-label">
                        <i class="fas fa-comment"></i> Tin Nhắn
                    </label>
                    <textarea class="form-control" id="message" name="message"
                              rows="5" required placeholder="Nhập tin nhắn của bạn"></textarea>
                </div>

                <button type="submit" class="btn btn-primary w-100">
                    <i class="fas fa-paper-plane"></i> Gửi Tin Nhắn
                </button>
            </form>
        </div>

        <div class="col-lg-6">
            <h2 class="mb-4">Thông Tin Liên Hệ</h2>

            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="fas fa-map-marker-alt text-primary"></i> Địa Chỉ
                    </h5>
                    <p class="card-text">
                        Cần Thơ, Việt Nam
                    </p>
                </div>
            </div>

            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="fas fa-phone text-primary"></i> Điện Thoại
                    </h5>
                    <p class="card-text">
                        0123 456 789<br>
                        0987 654 321
                    </p>
                </div>
            </div>

            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="fas fa-envelope text-primary"></i> Email
                    </h5>
                    <p class="card-text">
                        info@carshowroom.com<br>
                        support@carshowroom.com
                    </p>
                </div>
            </div>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="fas fa-clock text-primary"></i> Giờ Làm Việc
                    </h5>
                    <p class="card-text">
                        Thứ 2 - Thứ 6: 8:00 - 17:00<br>
                        Thứ 7: 8:00 - 12:00<br>
                        Chủ Nhật: Nghỉ
                    </p>
                </div>
            </div>

            <div class="mt-4">
                <h5>Theo Dõi Chúng Tôi</h5>
                <div class="d-flex gap-3">
                    <a href="#" class="btn btn-outline-primary">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" class="btn btn-outline-info">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#" class="btn btn-outline-danger">
                        <i class="fab fa-instagram"></i>
                    </a>
                    <a href="#" class="btn btn-outline-danger">
                        <i class="fab fa-youtube"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>