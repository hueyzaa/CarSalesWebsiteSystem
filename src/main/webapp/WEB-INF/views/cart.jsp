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
            background-color: #0f0f0f;
        }

        /* Page Header */
        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            padding: 60px 0;
            margin-bottom: 40px;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .page-header p {
            color: #888;
            margin: 0;
        }

        /* Alerts */
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

        .btn-close {
            filter: invert(1);
        }

        /* Empty Cart */
        .empty-cart {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 20px;
            padding: 80px 40px;
            text-align: center;
        }

        .empty-cart i {
            color: #333;
            margin-bottom: 30px;
        }

        .empty-cart h3 {
            color: #f8f9fa;
            margin-bottom: 15px;
        }

        .empty-cart p {
            color: #666;
            margin-bottom: 30px;
        }

        .btn-explore {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px 40px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
        }

        .btn-explore:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        /* Cart Items */
        .cart-items {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
        }

        .cart-item {
            background: #0f0f0f;
            border-bottom: 1px solid #333;
            padding: 20px;
            display: flex;
            align-items: center;
            gap: 20px;
            transition: all 0.3s;
        }

        .cart-item:last-child {
            border-bottom: none;
        }

        .cart-item:hover {
            background: #1a1a1a;
        }

        .cart-item-image {
            width: 150px;
            height: 100px;
            border-radius: 10px;
            overflow: hidden;
            flex-shrink: 0;
        }

        .cart-item-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .cart-item-details {
            flex: 1;
        }

        .cart-item-details h5 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .cart-item-details p {
            color: #888;
            margin: 0;
            font-size: 0.9rem;
        }

        .cart-item-price {
            color: #f8f9fa;
            font-size: 1.3rem;
            font-weight: 700;
        }

        .cart-item-quantity {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .quantity-input {
            width: 70px;
            text-align: center;
            background: #1a1a1a;
            border: 1px solid #333;
            color: #fff;
            padding: 8px;
            border-radius: 8px;
        }

        .quantity-input:focus {
            outline: none;
            border-color: #ffd700;
        }

        .btn-quantity {
            background: #1a1a1a;
            border: 1px solid #333;
            color: #ffd700;
            width: 35px;
            height: 35px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;
        }

        .btn-quantity:hover {
            background: #ffd700;
            color: #1a1a1a;
        }

        .btn-remove {
            background: rgba(231, 76, 60, 0.1);
            border: 1px solid #e74c3c;
            color: #e74c3c;
            padding: 8px 15px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-remove:hover {
            background: #e74c3c;
            color: white;
        }

        /* Cart Summary */
        .cart-summary {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 30px;
            position: sticky;
            top: 20px;
        }

        .cart-summary h4 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 1px solid #333;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            color: #888;
        }

        .summary-row.total {
            font-size: 1.3rem;
            font-weight: 700;
            color: #f8f9fa;
            padding-top: 15px;
            border-top: 2px solid #ffd700;
        }

        .summary-row.total .amount {
            color: #ffd700;
        }

        .btn-checkout {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 15px;
            font-weight: 700;
            border-radius: 10px;
            width: 100%;
            font-size: 1.1rem;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .btn-checkout:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-continue {
            background: transparent;
            border: 2px solid #ffd700;
            color: #ffd700;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            width: 100%;
            margin-top: 10px;
            transition: all 0.3s;
        }

        .btn-continue:hover {
            background: #ffd700;
            color: #1a1a1a;
        }

        .btn-clear {
            background: transparent;
            border: 1px solid #e74c3c;
            color: #e74c3c;
            padding: 8px 20px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-clear:hover {
            background: #e74c3c;
            color: white;
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container text-center">
        <h1>
            <i class="fas fa-shopping-cart"></i> Giỏ Hàng
        </h1>
        <p>Quản lý sản phẩm trong giỏ hàng của bạn</p>
    </div>
</div>

<!-- Main Content -->
<div class="container my-5">
    <!-- Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Empty Cart -->
    <c:if test="${empty cartItems}">
        <div class="empty-cart">
            <i class="fas fa-shopping-cart fa-5x"></i>
            <h3>Giỏ Hàng Trống</h3>
            <p>Bạn chưa có sản phẩm nào trong giỏ hàng</p>
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-explore">
                <i class="fas fa-search"></i> Khám Phá Xe
            </a>
        </div>
    </c:if>

    <!-- Cart Items -->
    <c:if test="${not empty cartItems}">
        <div class="row">
            <div class="col-lg-8">
                <div class="cart-items">
                    <div class="p-3 d-flex justify-content-between align-items-center" style="background: #0f0f0f; border-bottom: 1px solid #333;">
                        <h5 style="color: #ffd700; margin: 0;">
                            <i class="fas fa-list"></i> Sản Phẩm Trong Giỏ (${cartItems.size()})
                        </h5>
                        <form method="post" action="${pageContext.request.contextPath}/cart" style="margin: 0;">
                            <input type="hidden" name="action" value="clear">
                            <button type="submit" class="btn btn-clear" onclick="return confirm('Bạn có chắc muốn xóa tất cả?')">
                                <i class="fas fa-trash"></i> Xóa Tất Cả
                            </button>
                        </form>
                    </div>

                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-item">
                            <div class="cart-item-image">
                                <c:choose>
                                    <c:when test="${not empty item.car.imageUrl}">
                                        <img src="${item.car.imageUrl}" alt="${item.car.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/150x100?text=No+Image" alt="No Image">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="cart-item-details">
                                <h5>${item.car.name}</h5>
                                <p>
                                    <i class="fas fa-tag"></i> ${item.car.brandName}<br>
                                    <i class="fas fa-calendar"></i> Năm: ${item.car.year}
                                </p>
                            </div>

                            <div class="cart-item-quantity">
                                <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex gap-2">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="cartItemId" value="${item.id}">
                                    <button type="button" class="btn-quantity" onclick="decreaseQuantity(this)">
                                        <i class="fas fa-minus"></i>
                                    </button>
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="10" class="quantity-input" readonly>
                                    <button type="button" class="btn-quantity" onclick="increaseQuantity(this)">
                                        <i class="fas fa-plus"></i>
                                    </button>
                                    <button type="submit" class="btn btn-quantity" style="width: auto; padding: 0 15px;">
                                        <i class="fas fa-check"></i>
                                    </button>
                                </form>
                            </div>

                            <div class="cart-item-price">
                                <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="₫"/>
                            </div>

                            <div>
                                <form method="post" action="${pageContext.request.contextPath}/cart" style="margin: 0;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="cartItemId" value="${item.id}">
                                    <button type="submit" class="btn btn-remove" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="cart-summary">
                    <h4><i class="fas fa-calculator"></i> Tóm Tắt Đơn Hàng</h4>

                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span class="amount">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <div class="summary-row">
                        <span>Phí vận chuyển:</span>
                        <span class="amount">Miễn phí</span>
                    </div>

                    <div class="summary-row total">
                        <span>Tổng cộng:</span>
                        <span class="amount">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-checkout">
                        <i class="fas fa-credit-card"></i> Đặt Hàng
                    </a>

                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-continue">
                        <i class="fas fa-arrow-left"></i> Tiếp Tục Mua Sắm
                    </a>
                </div>
            </div>
        </div>
    </c:if>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function increaseQuantity(btn) {
        const input = btn.previousElementSibling;
        const currentValue = parseInt(input.value);
        const maxValue = parseInt(input.max);
        if (currentValue < maxValue) {
            input.value = currentValue + 1;
        }
    }

    function decreaseQuantity(btn) {
        const input = btn.nextElementSibling;
        const currentValue = parseInt(input.value);
        const minValue = parseInt(input.min);
        if (currentValue > minValue) {
            input.value = currentValue - 1;
        }
    }
</script>
</body>
</html>