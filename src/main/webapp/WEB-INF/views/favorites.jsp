<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 09/10/2025
  Time: 1:58 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xe Yêu Thích - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<div class="container my-5">
    <div class="text-center mb-5">
        <h1><i class="fas fa-heart text-danger"></i> Xe Yêu Thích</h1>
        <p class="lead">Danh sách các mẫu xe bạn yêu thích</p>
    </div>

    <c:if test="${not empty message}">
        <div class="alert alert-info text-center" role="alert">
            <i class="fas fa-info-circle"></i> ${message}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty favoriteCars}">
            <div class="text-center py-5">
                <i class="fas fa-heart-broken" style="font-size: 5rem; color: #ccc;"></i>
                <h3 class="mt-3">Chưa có xe yêu thích nào</h3>
                <p class="text-muted">Hãy khám phá và thêm những mẫu xe bạn yêu thích!</p>
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary mt-3">
                    <i class="fas fa-search"></i> Khám Phá Xe
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <c:forEach var="car" items="${favoriteCars}">
                    <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                        <div class="card h-100 shadow-sm">
                            <img src="${car.imageUrl}" class="card-img-top" alt="${car.model}" style="height: 200px; object-fit: cover;">
                            <div class="card-body">
                                <h5 class="card-title">${car.brandName} ${car.model}</h5>
                                <p class="card-text text-primary fw-bold">
                                    <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/>
                                </p>
                                <div class="d-grid gap-2">
                                    <a href="${pageContext.request.contextPath}/car-detail?id=${car.carId}"
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="fas fa-eye"></i> Xem Chi Tiết
                                    </a>
                                    <button class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-heart-broken"></i> Bỏ Yêu Thích
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>