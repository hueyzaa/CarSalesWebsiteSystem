<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 10/10/2025
  Time: 12:16 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh Toán - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
            color: #e0e0e0;
        }

        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 40px 0;
            margin-bottom: 30px;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #f8f9fa;
            font-weight: 700;
        }

        .checkout-container {
            background: #1a1a1a;
            border-radius: 15px;
            padding: 30px;
            border: 1px solid #333;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
        }

        .section-title {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #333;
        }

        /* User Info */
        .user-info-card {
            background: #0f0f0f;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #333;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #888;
            font-weight: 600;
        }

        .info-value {
            color: #f8f9fa;
        }

        /* Cart Items */
        .cart-item {
            display: flex;
            gap: 15px;
            padding: 15px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-bottom: 15px;
            border: 1px solid #333;
        }

        .item-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 8px;
            border: 2px solid #333;
        }

        .item-info {
            flex: 1;
        }

        .item-brand {
            color: #ffd700;
            font-size: 0.9rem;
            font-weight: 600;
            margin-bottom: 5px;
        }

        .item-name {
            color: #f8f9fa;
            font-size: 1.1rem;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .item-price {
            color: #888;
        }

        .item-quantity {
            color: #ffd700;
            font-weight: 600;
        }

        /* Payment Section */
        .payment-method {
            background: #0f0f0f;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .payment-option {
            background: #1a1a1a;
            border: 2px solid #333;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .payment-option:hover {
            border-color: #ffd700;
        }

        .payment-option.selected {
            border-color: #ffd700;
            background: rgba(255, 215, 0, 0.1);
        }

        .payment-option input[type="radio"] {
            width: 20px;
            height: 20px;
            margin-right: 15px;
            cursor: pointer;
        }

        .payment-option label {
            cursor: pointer;
            margin: 0;
            font-weight: 600;
            color: #f8f9fa;
            font-size: 1.05rem;
        }

        .payment-description {
            color: #888;
            font-size: 0.9rem;
            margin-top: 10px;
            padding-left: 35px;
            line-height: 1.5;
        }

        .payment-highlight {
            color: #ffd700;
            font-weight: 600;
        }

        .deposit-input-group {
            margin-top: 15px;
            padding-left: 35px;
            display: none;
        }

        .deposit-input-group.show {
            display: block;
        }

        .deposit-input-group input {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 10px;
            border-radius: 8px;
        }

        .deposit-input-group input:focus {
            border-color: #ffd700;
            outline: none;
            box-shadow: 0 0 0 0.2rem rgba(255, 215, 0, 0.25);
        }

        .deposit-hint {
            color: #888;
            font-size: 0.85rem;
            margin-top: 5px;
        }

        /* Order Summary */
        .order-summary {
            background: #0f0f0f;
            padding: 25px;
            border-radius: 10px;
            position: sticky;
            top: 20px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #333;
        }

        .summary-row:last-child {
            border-bottom: none;
            padding-top: 20px;
            margin-top: 15px;
            border-top: 2px solid #444;
        }

        .summary-label {
            color: #888;
            font-weight: 600;
        }

        .summary-value {
            color: #f8f9fa;
            font-weight: 600;
        }

        .summary-value.total {
            color: #ffd700;
            font-size: 1.5rem;
        }

        .btn-place-order {
            width: 100%;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 700;
            font-size: 1.1rem;
            padding: 15px;
            border-radius: 10px;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .btn-place-order:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
        }

        .btn-back {
            width: 100%;
            background: #333;
            border: 1px solid #555;
            color: #e0e0e0;
            font-weight: 600;
            padding: 12px;
            border-radius: 8px;
            margin-top: 10px;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background: #444;
            border-color: #666;
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
    <div class="container">
        <h1><i class="fas fa-credit-card"></i> Thanh Toán</h1>
    </div>
</div>

<!-- Main Content -->
<div class="container mb-5">
    <!-- Error Messages -->
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
        <div class="row">
            <!-- Left Column -->
            <div class="col-lg-8">
                <!-- User Information -->
                <div class="checkout-container mb-4">
                    <h3 class="section-title">
                        <i class="fas fa-user"></i> Thông Tin Người Mua
                    </h3>
                    <div class="user-info-card">
                        <div class="info-row">
                            <span class="info-label">Họ và tên:</span>
                            <span class="info-value">${user.name}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Email:</span>
                            <span class="info-value">${user.email}</span>
                        </div>
                        <c:if test="${not empty user.phone}">
                            <div class="info-row">
                                <span class="info-label">Số điện thoại:</span>
                                <span class="info-value">${user.phone}</span>
                            </div>
                        </c:if>
                        <c:if test="${not empty user.address}">
                            <div class="info-row">
                                <span class="info-label">Địa chỉ:</span>
                                <span class="info-value">${user.address}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Order Items -->
                <div class="checkout-container mb-4">
                    <h3 class="section-title">
                        <i class="fas fa-shopping-cart"></i> Sản Phẩm
                    </h3>
                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-item">
                            <c:choose>
                                <c:when test="${not empty item.car.imageUrl}">
                                    <img src="${item.car.imageUrl}" alt="${item.car.name}" class="item-image">
                                </c:when>
                                <c:otherwise>
                                    <img src="https://via.placeholder.com/100?text=No+Image" alt="${item.car.name}" class="item-image">
                                </c:otherwise>
                            </c:choose>

                            <div class="item-info">
                                <div class="item-brand">${item.car.brandName}</div>
                                <div class="item-name">${item.car.name}</div>
                                <div class="item-price">
                                    <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="₫"/>
                                    × <span class="item-quantity">${item.quantity}</span>
                                </div>
                            </div>

                            <div style="text-align: right; color: #ffd700; font-weight: 700; font-size: 1.1rem;">
                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Payment Method -->
                <div class="checkout-container">
                    <h3 class="section-title">
                        <i class="fas fa-credit-card"></i> Hình Thức Thanh Toán
                    </h3>
                    <div class="payment-method">
                        <!-- Showroom Payment -->
                        <div class="payment-option" onclick="selectPayment('SHOWROOM', this)">
                            <div class="d-flex align-items-center">
                                <input type="radio" name="paymentType" id="showroomPayment" value="SHOWROOM" required>
                                <label for="showroomPayment">
                                    <i class="fas fa-store"></i> Thanh Toán Tại Showroom
                                </label>
                            </div>
                            <div class="payment-description">
                                Đặt xe ngay, thanh toán sau khi đến showroom.
                                <br><span class="payment-highlight">✓ Không cần thanh toán trước</span>
                                <br><span class="payment-highlight">✓ Kiểm tra xe trực tiếp trước khi thanh toán</span>
                                <br>Chúng tôi sẽ liên hệ để xác nhận và hẹn lịch đến showroom.
                            </div>
                        </div>

                        <!-- Full Payment -->
                        <div class="payment-option" onclick="selectPayment('FULL', this)">
                            <div class="d-flex align-items-center">
                                <input type="radio" name="paymentType" id="fullPayment" value="FULL" required>
                                <label for="fullPayment">
                                    <i class="fas fa-money-bill-wave"></i> Thanh Toán Toàn Bộ
                                </label>
                            </div>
                            <div class="payment-description">
                                Thanh toán 100% giá trị đơn hàng ngay.
                                <br><span class="payment-highlight">✓ Đơn hàng được xử lý ưu tiên</span>
                                <br><span class="payment-highlight">✓ Giao xe nhanh chóng</span>
                            </div>
                        </div>

                        <!-- Deposit Payment -->
                        <div class="payment-option" onclick="selectPayment('DEPOSIT', this)">
                            <div class="d-flex align-items-center">
                                <input type="radio" name="paymentType" id="depositPayment" value="DEPOSIT" required>
                                <label for="depositPayment">
                                    <i class="fas fa-hand-holding-usd"></i> Đặt Cọc
                                </label>
                            </div>
                            <div class="payment-description">
                                Đặt cọc tối thiểu 20% giá trị đơn hàng. Phần còn lại thanh toán khi nhận xe.
                                <br><span class="payment-highlight">✓ Giữ chỗ xe ưa thích</span>
                                <br><span class="payment-highlight">✓ Linh hoạt thanh toán</span>
                            </div>
                            <div class="deposit-input-group" id="depositInputGroup">
                                <label class="form-label">Số tiền đặt cọc:</label>
                                <input type="number"
                                       class="form-control"
                                       name="depositAmount"
                                       id="depositAmount"
                                       placeholder="Nhập số tiền đặt cọc"
                                       min="${total * 0.2}"
                                       max="${total}"
                                       step="1000000">
                                <div class="deposit-hint">
                                    Tối thiểu: <fmt:formatNumber value="${total * 0.2}" type="currency" currencySymbol="₫"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column - Order Summary -->
            <div class="col-lg-4">
                <div class="order-summary">
                    <h3 class="section-title">
                        <i class="fas fa-receipt"></i> Tóm Tắt Đơn Hàng
                    </h3>

                    <div class="summary-row">
                        <span class="summary-label">Tổng số lượng:</span>
                        <span class="summary-value">
                            <c:set var="totalItems" value="0"/>
                            <c:forEach var="item" items="${cartItems}">
                                <c:set var="totalItems" value="${totalItems + item.quantity}"/>
                            </c:forEach>
                            ${totalItems} xe
                        </span>
                    </div>

                    <div class="summary-row">
                        <span class="summary-label">Tạm tính:</span>
                        <span class="summary-value">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <div class="summary-row">
                        <span class="summary-label" style="font-size: 1.2rem;">Tổng cộng:</span>
                        <span class="summary-value total">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <button type="submit" class="btn btn-place-order">
                        <i class="fas fa-check-circle"></i> Đặt Hàng
                    </button>

                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-back">
                        <i class="fas fa-arrow-left"></i> Quay Lại Giỏ Hàng
                    </a>

                    <div class="mt-3 text-center" style="color: #888; font-size: 0.85rem;">
                        <i class="fas fa-shield-alt"></i> Giao dịch được bảo mật
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function selectPayment(type, element) {
        // Remove selected class from all options
        document.querySelectorAll('.payment-option').forEach(opt => {
            opt.classList.remove('selected');
        });

        // Add selected class to clicked option
        element.classList.add('selected');

        // Handle radio buttons and deposit input
        if (type === 'SHOWROOM') {
            document.getElementById('showroomPayment').checked = true;
            document.getElementById('depositInputGroup').classList.remove('show');
            document.getElementById('depositAmount').removeAttribute('required');
        } else if (type === 'FULL') {
            document.getElementById('fullPayment').checked = true;
            document.getElementById('depositInputGroup').classList.remove('show');
            document.getElementById('depositAmount').removeAttribute('required');
        } else if (type === 'DEPOSIT') {
            document.getElementById('depositPayment').checked = true;
            document.getElementById('depositInputGroup').classList.add('show');
            document.getElementById('depositAmount').setAttribute('required', 'required');
        }
    }

    // Form validation
    document.getElementById('checkoutForm').addEventListener('submit', function(e) {
        const paymentType = document.querySelector('input[name="paymentType"]:checked');

        if (!paymentType) {
            e.preventDefault();
            alert('Vui lòng chọn hình thức thanh toán!');
            return false;
        }

        if (paymentType.value === 'DEPOSIT') {
            const depositAmount = parseFloat(document.getElementById('depositAmount').value);
            const minDeposit = ${total} * 0.2;
            const maxDeposit = ${total};

            if (isNaN(depositAmount) || depositAmount < minDeposit) {
                e.preventDefault();
                alert('Số tiền đặt cọc phải ít nhất ' + minDeposit.toLocaleString('vi-VN') + ' ₫');
                return false;
            }

            if (depositAmount > maxDeposit) {
                e.preventDefault();
                alert('Số tiền đặt cọc không được vượt quá tổng giá trị đơn hàng!');
                return false;
            }
        }

        // Confirmation message based on payment type
        let confirmMessage = '';
        if (paymentType.value === 'SHOWROOM') {
            confirmMessage = 'Xác nhận đặt xe? Bạn sẽ thanh toán khi đến showroom.';
        } else if (paymentType.value === 'FULL') {
            confirmMessage = 'Xác nhận thanh toán toàn bộ đơn hàng?';
        } else {
            confirmMessage = 'Xác nhận đặt cọc?';
        }

        return confirm(confirmMessage);
    });
</script>
</body>
</html>