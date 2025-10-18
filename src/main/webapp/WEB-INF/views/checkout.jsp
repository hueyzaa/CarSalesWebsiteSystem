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

        .checkout-container {
            background: #1a1a1a;
            border-radius: 15px;
            padding: 30px;
            border: 1px solid #333;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
            margin-bottom: 30px;
        }

        .section-title {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #333;
        }

        .payment-option {
            background: #0f0f0f;
            border: 2px solid #333;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
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
            cursor: pointer;
            margin: 0;
        }

        .payment-option label {
            margin: 0 0 0 10px;
            cursor: pointer;
            flex: 1;
            font-size: 1rem;
        }

        .promotion-option-card {
            background: #0f0f0f;
            border: 2px solid #333;
            border-radius: 12px;
            padding: 20px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: start;
            gap: 15px;
            margin-bottom: 15px;
        }

        .promotion-option-card:hover {
            border-color: #ffd700;
            transform: translateX(5px);
        }

        .promotion-option-card.selected {
            border-color: #ffd700;
            background: rgba(255, 215, 0, 0.1);
            box-shadow: 0 5px 20px rgba(255, 215, 0, 0.3);
        }

        .promotion-option-card input[type="radio"] {
            width: 20px;
            height: 20px;
            cursor: pointer;
            margin-top: 3px;
            flex-shrink: 0;
        }

        .promotion-option-card label {
            margin: 0;
            cursor: pointer;
            flex: 1;
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

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-credit-card"></i> Thanh Toán</h1>
    </div>
</div>

<div class="container mb-5">
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
        <div class="row">
            <div class="col-lg-8">
                <!-- User Info -->
                <div class="checkout-container">
                    <h3 class="section-title"><i class="fas fa-user"></i> Thông Tin Người Mua</h3>
                    <div style="background: #0f0f0f; padding: 20px; border-radius: 10px;">
                        <div style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #333;">
                            <span style="color: #888;">Họ và tên:</span>
                            <span style="color: #f8f9fa;">${user.name}</span>
                        </div>
                        <div style="display: flex; justify-content: space-between; padding: 10px 0;">
                            <span style="color: #888;">Email:</span>
                            <span style="color: #f8f9fa;">${user.email}</span>
                        </div>
                    </div>
                </div>

                <!-- Cart Items -->
                <div class="checkout-container">
                    <h3 class="section-title"><i class="fas fa-shopping-cart"></i> Sản Phẩm</h3>
                    <c:forEach var="item" items="${cartItems}">
                        <div style="display: flex; gap: 15px; padding: 15px; background: #0f0f0f;
                             border-radius: 10px; margin-bottom: 15px; border: 1px solid #333;">
                            <img src="${item.car.imageUrl}" alt="${item.car.name}"
                                 style="width: 100px; height: 100px; object-fit: cover; border-radius: 8px;">
                            <div style="flex: 1;">
                                <div style="color: #ffd700; font-size: 0.9rem; font-weight: 600;">
                                        ${item.car.brandName}
                                </div>
                                <div style="color: #f8f9fa; font-size: 1.1rem; font-weight: 600;">
                                        ${item.car.name}
                                </div>
                                <div style="color: #888;">
                                    <fmt:formatNumber value="${item.car.price}" type="currency" currencySymbol="₫"/>
                                    × ${item.quantity}
                                </div>
                            </div>
                            <div style="text-align: right; color: #ffd700; font-weight: 700; font-size: 1.1rem;">
                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Promotion Selection -->
                <c:if test="${not empty availablePromotions}">
                    <div class="checkout-container">
                        <h3 class="section-title"><i class="fas fa-ticket-alt"></i> Áp Dụng Khuyến Mãi</h3>

                        <div style="background: rgba(255, 215, 0, 0.1); border: 2px solid #ffd700;
                             border-radius: 12px; padding: 20px; margin-bottom: 20px;">
                            <div style="color: #ffd700; font-weight: 600;">
                                <i class="fas fa-info-circle"></i>
                                Bạn có ${availablePromotions.size()} khuyến mãi có thể sử dụng
                            </div>
                        </div>

                        <!-- No Promotion Option -->
                        <div class="promotion-option-card selected" onclick="selectPromotion(null, this)">
                            <input type="radio" name="promotionSelection" value="" id="promo-none" checked>
                            <label for="promo-none">
                                <div style="font-weight: 600; color: #f8f9fa;">
                                    <i class="fas fa-times-circle"></i> Không sử dụng khuyến mãi
                                </div>
                                <div style="color: #888; font-size: 0.9rem;">Thanh toán với giá gốc</div>
                            </label>
                        </div>

                        <!-- Available Promotions -->
                        <c:forEach var="promo" items="${availablePromotions}" varStatus="status">
                            <div class="promotion-option-card" onclick="selectPromotion(${promo.promotionId}, this)">
                                <input type="radio" name="promotionSelection" value="${promo.promotionId}"
                                       id="promo-${promo.promotionId}">
                                <label for="promo-${promo.promotionId}">
                                    <div style="display: flex; justify-content: space-between; flex-wrap: wrap; gap: 10px;">
                                        <div style="flex: 1;">
                                            <div style="font-weight: 600; color: #ffd700; font-size: 1.1rem;">
                                                    ${promo.title}
                                            </div>
                                            <div style="color: #b0b0b0; font-size: 0.9rem; margin: 8px 0;">
                                                    ${promo.description}
                                            </div>
                                        </div>
                                        <div style="background: linear-gradient(135deg, #dc3545, #c82333);
                                             color: white; padding: 10px 18px; border-radius: 20px; font-weight: 700;
                                             display: flex; align-items: center; justify-content: center;
                                             min-width: 80px; text-align: center;">
                                            <c:choose>
                                                <c:when test="${promo.discountPercentage > 0}">
                                                    -${promo.discountPercentage}%
                                                </c:when>
                                                <c:otherwise>
                                                    -<fmt:formatNumber value="${promo.discountAmount}" type="number"/>₫
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Payment Method -->
                <div class="checkout-container">
                    <h3 class="section-title"><i class="fas fa-credit-card"></i> Hình Thức Thanh Toán</h3>

                    <div class="payment-option selected" onclick="selectPayment('DEPOSIT', this)">
                        <input type="radio" name="paymentType" value="DEPOSIT" id="payment-deposit" required checked>
                        <label for="payment-deposit">
                            <i class="fas fa-hand-holding-usd"></i> Đặt Cọc 10% - Thanh Toán Online
                        </label>
                    </div>

                    <div class="payment-option" onclick="selectPayment('SHOWROOM', this)">
                        <input type="radio" name="paymentType" value="SHOWROOM" id="payment-showroom" required>
                        <label for="payment-showroom">
                            <i class="fas fa-store"></i> Thanh Toán Tại Showroom
                        </label>
                    </div>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="col-lg-4">
                <div style="background: #0f0f0f; padding: 25px; border-radius: 10px; position: sticky; top: 20px;">
                    <h3 class="section-title"><i class="fas fa-receipt"></i> Tóm Tắt Đơn Hàng</h3>

                    <div style="display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #333;">
                        <span style="color: #888;">Tổng giá trị:</span>
                        <span style="color: #f8f9fa; font-weight: 600;">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <div id="discountRow" style="display: none; color: #28a745; padding: 12px 0;
                         border-bottom: 1px solid #333;"></div>

                    <div style="display: flex; justify-content: space-between; padding: 20px 0;
                         border-top: 2px solid #444; margin-top: 15px;">
                        <span style="color: #888; font-weight: 600;">Tổng thanh toán:</span>
                        <span class="summary-value total" style="color: #ffd700; font-size: 1.5rem; font-weight: 700;">
                            <fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>

                    <button type="submit" class="btn-place-order" id="placeOrderBtn">
                        <i class="fas fa-check-circle"></i> Đặt Cọc Ngay
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const originalTotal = ${total};
    const depositPercentage = ${depositPercentage} / 100;
    let selectedPromotionId = null;

    const promotionDiscounts = {
    <c:forEach var="promo" items="${availablePromotions}" varStatus="status">
    ${promo.promotionId}: {
        percentage: ${promo.discountPercentage},
        amount: ${promo.discountAmount}
    }${!status.last ? ',' : ''}
    </c:forEach>
    };

    function selectPromotion(promotionId, element) {
        selectedPromotionId = promotionId;

        // Remove selected class from all cards
        document.querySelectorAll('.promotion-option-card').forEach(card => {
            card.classList.remove('selected');
        });

        // Add selected class to clicked card
        element.classList.add('selected');

        // Check the radio button
        const radio = element.querySelector('input[type="radio"]');
        if (radio) {
            radio.checked = true;
        }

        // Update or create hidden input for form submission
        let hiddenInput = document.getElementById('selectedPromotionInput');
        if (!hiddenInput) {
            hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.id = 'selectedPromotionInput';
            hiddenInput.name = 'promotionId';
            document.getElementById('checkoutForm').appendChild(hiddenInput);
        }
        hiddenInput.value = promotionId || '';

        updateTotals();
    }

    function selectPayment(type, element) {
        // Remove selected class from all payment options
        document.querySelectorAll('.payment-option').forEach(opt => {
            opt.classList.remove('selected');
        });

        // Add selected class to clicked option
        element.classList.add('selected');

        // Check the radio button
        const radio = element.querySelector('input[type="radio"]');
        if (radio) {
            radio.checked = true;
        }

        updateTotals();
    }

    function updateTotals() {
        let discount = 0;

        if (selectedPromotionId && promotionDiscounts[selectedPromotionId]) {
            const promo = promotionDiscounts[selectedPromotionId];
            if (promo.percentage > 0) {
                discount = originalTotal * (promo.percentage / 100);
            } else if (promo.amount > 0) {
                discount = promo.amount;
            }
        }

        const finalTotal = originalTotal - discount;

        // Update discount row
        let discountRow = document.getElementById('discountRow');
        if (discount > 0) {
            discountRow.innerHTML =
                '<span>Giảm giá:</span>' +
                '<span>-' + new Intl.NumberFormat('vi-VN').format(discount) + '₫</span>';
            discountRow.style.display = 'flex';
            discountRow.style.justifyContent = 'space-between';
        } else {
            discountRow.style.display = 'none';
        }

        // Update total
        document.querySelector('.summary-value.total').textContent =
            new Intl.NumberFormat('vi-VN').format(finalTotal) + '₫';

        // Update button text based on payment type
        const paymentType = document.querySelector('input[name="paymentType"]:checked');
        if (paymentType) {
            if (paymentType.value === 'DEPOSIT') {
                const depositAmount = finalTotal * depositPercentage;
                document.getElementById('placeOrderBtn').innerHTML =
                    '<i class="fas fa-check-circle"></i> Đặt Cọc ' +
                    new Intl.NumberFormat('vi-VN').format(depositAmount) + '₫';
            } else {
                document.getElementById('placeOrderBtn').innerHTML =
                    '<i class="fas fa-check-circle"></i> Xác Nhận Đặt Xe';
            }
        }
    }

    // Add event listeners to radio buttons for accessibility
    document.querySelectorAll('input[name="paymentType"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const parent = this.closest('.payment-option');
            selectPayment(this.value, parent);
        });
    });

    document.querySelectorAll('input[name="promotionSelection"]').forEach(radio => {
        radio.addEventListener('change', function() {
            const parent = this.closest('.promotion-option-card');
            const promoId = this.value === '' ? null : parseInt(this.value);
            selectPromotion(promoId, parent);
        });
    });

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', function() {
        updateTotals();
    });
</script>
</body>
</html>