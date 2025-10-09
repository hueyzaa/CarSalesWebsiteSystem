<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 09/10/2025
  Time: 1:57 CH
  To change this template use File | Settings | File Templates.
--%>
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
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow">
                <div class="card-body">
                    <h2 class="card-title text-center mb-4">
                        <i class="fas fa-user-circle"></i> Hồ Sơ Cá Nhân
                    </h2>

                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success" role="alert">
                                ${sessionScope.success}
                        </div>
                        <c:remove var="success" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger" role="alert">
                                ${sessionScope.error}
                        </div>
                        <c:remove var="error" scope="session"/>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/profile">
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="fas fa-user"></i> Họ và Tên
                            </label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="${user.name}" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="fas fa-envelope"></i> Email
                            </label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="${user.email}" required>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">
                                <i class="fas fa-phone"></i> Số Điện Thoại
                            </label>
                            <input type="text" class="form-control" id="phone" name="phone"
                                   value="${user.phone}" placeholder="Nhập số điện thoại">
                        </div>

                        <div class="mb-3">
                            <label for="address" class="form-label">
                                <i class="fas fa-map-marker-alt"></i> Địa Chỉ
                            </label>
                            <textarea class="form-control" id="address" name="address"
                                      rows="3" placeholder="Nhập địa chỉ">${user.address}</textarea>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Cập Nhật Thông Tin
                            </button>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Quay Lại
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
</body>
</html>
