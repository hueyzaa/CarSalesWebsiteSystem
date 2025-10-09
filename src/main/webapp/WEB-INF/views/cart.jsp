<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ Hàng - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        footer {
            margin-top: auto;
            background-color: #2f3542;
            color: white;
        }
        .cart-item-img {
            width: 100px;
            height: 75px;
            object-fit: cover;
            border-radius: 8px;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container my-5">
    <!-- Page Header -->
    <div class="text-center mb-5">
        <h1 class="display-4 fw-bold">
            <i class="fas fa-shopping-cart text-primary"></i> Giỏ Hàng
        </h1>
        <p class="lead text-muted">Quản lý các xe bạn đã chọn</p>
    </div>

    <!-- Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Empty Cart -->
    <c:if test="${empty cartItems}">
        <div class="text-center py-5">
            <i class="fas fa-shopping-cart fa-5x text-muted mb-4"></i>
            <h3>Giỏ Hàng Trống</h3>
            <p class="text-muted mb-4">Bạn chưa thêm sản phẩm nào vào giỏ hàng</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary btn-lg">
                <i class="fas fa-search"></i> Khám Phá Xe
            </a>
        </div>
    </c:if>

    <!-- Cart Items -->
    <c:if test="${not empty cartItems}">
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>Hình Ảnh</th>
                            <th>Hãng Xe</th>
                            <th>Mẫu Xe</th>
                            <th>Số Lượng</th>
                            <th class="text-end">Đơn Giá</th>
                            <th class="text-end">Thành Tiền</th>
                            <th class="text-center">Thao Tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:set var="total" value="0" />
                        <c:forEach var="item" items="${cartItems}">
                            <tr>
                                <td>
                                    <img src="${item.imageUrl != null ? item.imageUrl : 'https://via.placeholder.com/100x75?text=No+Image'}"
                                         alt="${item.carModel}" class="cart-item-img">
                                </td>
                                <td>
                                    <span class="badge bg-primary">${item.brandName}</span>
                                </td>
                                <td class="fw-semibold">${item.carModel}</td>
                                <td>
                                    <span class="badge bg-secondary">${item.quantity}</span>
                                </td>
                                <td class="text-end">
                                    <fmt:formatNumber value="${item.carPrice}" type="currency" currencySymbol="₫" />
                                </td>
                                <td class="text-end fw-bold text-primary">
                                    <fmt:formatNumber value="${item.carPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                    <c:set var="total" value="${total + (item.carPrice * item.quantity)}" />
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/cart/remove?cartItemId=${item.cartItemId}"
                                       class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                        <i class="fas fa-trash"></i> Xóa
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot class="table-light">
                        <tr>
                            <th colspan="5" class="text-end">Tổng Cộng:</th>
                            <th class="text-end fs-4 text-primary">
                                <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫" />
                            </th>
                            <th></th>
                        </tr>
                        </tfoot>
                    </table>
                </div>

                <!-- Action Buttons -->
                <div class="d-flex justify-content-between mt-4">
                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left"></i> Tiếp Tục Mua Hàng
                    </a>
                    <a href="${pageContext.request.contextPath}/orders/create" class="btn btn-success btn-lg">
                        <i class="fas fa-check-circle"></i> Thanh Toán
                    </a>
                </div>
            </div>
        </div>
    </c:if>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>