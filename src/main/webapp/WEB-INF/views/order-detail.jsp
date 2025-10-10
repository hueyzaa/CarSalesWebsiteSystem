<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Đơn Hàng #${order.orderId} - Car Showroom</title>
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
            margin-bottom: 10px;
        }

        .breadcrumb {
            background: transparent;
            padding: 0;
            margin-bottom: 10px;
        }

        .breadcrumb-item a {
            color: #ffd700;
            text-decoration: none;
        }

        .breadcrumb-item.active {
            color: #888;
        }

        /* Order Info Card */
        .order-info-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 20px;
        }

        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 20px;
            border-bottom: 2px solid #333;
        }

        .order-id {
            color: #ffd700;
            font-size: 1.5rem;
            font-weight: 700;
        }

        .status-badge {
            padding: 10px 25px;
            border-radius: 25px;
            font-weight: 700;
            font-size: 1rem;
        }

        .status-badge.pending {
            background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
            color: white;
        }

        .status-badge.approved {
            background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%);
            color: white;
        }

        .status-badge.completed {
            background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
            color: white;
        }

        .status-badge.cancelled {
            background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
            color: white;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .info-item {
            padding: 15px;
            background: #0f0f0f;
            border-radius: 10px;
        }

        .info-label {
            color: #888;
            font-size: 0.9rem;
            margin-bottom: 5px;
        }

        .info-value {
            color: #f8f9fa;
            font-size: 1.1rem;
            font-weight: 600;
        }

        /* Section Title */
        .section-title {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #333;
        }

        /* Order Items */
        .order-items {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
        }

        .order-item {
            display: flex;
            gap: 20px;
            padding: 20px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-bottom: 15px;
            border: 1px solid #333;
        }

        .item-image {
            width: 120px;
            height: 120px;
            object-fit: cover;
            border-radius: 10px;
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
            font-size: 1.2rem;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .item-specs {
            color: #888;
            font-size: 0.9rem;
        }

        .item-specs i {
            color: #ffd700;
            margin-right: 5px;
        }

        .item-pricing {
            text-align: right;
        }

        .item-price {
            color: #888;
            font-size: 0.9rem;
        }

        .item-quantity {
            color: #ffd700;
            font-weight: 600;
            margin: 5px 0;
        }

        .item-subtotal {
            color: #f8f9fa;
            font-size: 1.2rem;
            font-weight: 700;
            margin-top: 10px;
        }

        /* Payment Summary */
        .payment-summary {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
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

        .summary-value.paid {
            color: #4caf50;
        }

        .summary-value.remaining {
            color: #ff9800;
        }

        /* Transactions */
        .transactions-section {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
        }

        .transaction-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-bottom: 10px;
            border: 1px solid #333;
        }

        .transaction-info {
            flex: 1;
        }

        .transaction-info i {
            color: #4caf50;
            margin-right: 10px;
        }

        .transaction-type {
            color: #ffd700;
            font-weight: 600;
            font-size: 1.1rem;
        }

        .transaction-status {
            font-size: 0.85rem;
            margin-top: 3px;
        }

        .transaction-date {
            color: #888;
            font-size: 0.9rem;
            margin-top: 5px;
        }

        .transaction-amount {
            color: #4caf50;
            font-size: 1.2rem;
            font-weight: 700;
            text-align: right;
        }

        /* Actions */
        .action-buttons {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .btn-payment {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 700;
            padding: 15px 30px;
            border-radius: 10px;
            transition: all 0.3s;
            width: 100%;
        }

        .btn-payment:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-cancel-order {
            background: transparent;
            border: 2px solid #e74c3c;
            color: #e74c3c;
            font-weight: 600;
            padding: 12px 30px;
            border-radius: 10px;
            transition: all 0.3s;
            width: 100%;
        }

        .btn-cancel-order:hover {
            background: #e74c3c;
            color: white;
        }

        .btn-back {
            background: #333;
            border: 1px solid #555;
            color: #e0e0e0;
            font-weight: 600;
            padding: 12px 30px;
            border-radius: 10px;
            transition: all 0.3s;
            width: 100%;
        }

        .btn-back:hover {
            background: #444;
            border-color: #666;
        }

        .alert {
            border-radius: 10px;
            border: none;
        }

        .payment-badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .payment-badge.success {
            background: rgba(76, 175, 80, 0.2);
            color: #4caf50;
            border: 1px solid rgba(76, 175, 80, 0.3);
        }

        .payment-badge.warning {
            background: rgba(255, 152, 0, 0.2);
            color: #ff9800;
            border: 1px solid rgba(255, 152, 0, 0.3);
        }

        .payment-badge.danger {
            background: rgba(244, 67, 54, 0.2);
            color: #f44336;
            border: 1px solid rgba(244, 67, 54, 0.3);
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
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang Chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders">Đơn Hàng</a></li>
                <li class="breadcrumb-item active">Chi Tiết #${order.orderId}</li>
            </ol>
        </nav>
        <h1><i class="fas fa-file-invoice"></i> Chi Tiết Đơn Hàng</h1>
    </div>
</div>

<!-- Main Content -->
<div class="container mb-5">
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

    <!-- Order Information -->
    <div class="order-info-card">
        <div class="order-header">
            <div class="order-id">
                <i class="fas fa-receipt"></i> Đơn Hàng #${order.orderId}
            </div>
            <span class="status-badge ${order.statusColor}">
                ${order.statusDisplay}
            </span>
        </div>

        <div class="info-grid">
            <div class="info-item">
                <div class="info-label"><i class="far fa-calendar"></i> Ngày đặt hàng</div>
                <div class="info-value">
                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </div>

            <div class="info-item">
                <div class="info-label"><i class="fas fa-box"></i> Tổng số lượng</div>
                <div class="info-value">${order.totalItems} xe</div>
            </div>

            <div class="info-item">
                <div class="info-label"><i class="fas fa-money-bill-wave"></i> Tổng giá trị</div>
                <div class="info-value" style="color: #ffd700;">${order.formattedTotal}</div>
            </div>

            <div class="info-item">
                <div class="info-label"><i class="fas fa-wallet"></i> Hình thức thanh toán</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${order.paymentType == 'FULL'}">
                            <span style="color: #2196f3;">Thanh toán toàn bộ</span>
                        </c:when>
                        <c:when test="${order.paymentType == 'DEPOSIT'}">
                            <span style="color: #ff9800;">Đặt cọc</span>
                        </c:when>
                        <c:when test="${order.paymentType == 'SHOWROOM'}">
                            <span style="color: #9c27b0;">Thanh toán tại showroom</span>
                        </c:when>
                        <c:otherwise>
                            ${order.paymentType}
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="info-item">
                <div class="info-label"><i class="fas fa-credit-card"></i> Trạng thái thanh toán</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${order.fullyPaid}">
                            <span class="payment-badge success">
                                <i class="fas fa-check-circle"></i> Đã thanh toán toàn bộ
                            </span>
                        </c:when>
                        <c:when test="${order.paidAmount > 0}">
                            <c:choose>
                                <c:when test="${order.paymentType == 'DEPOSIT'}">
                                    <span class="payment-badge warning">
                                        <i class="fas fa-check-circle"></i> Đã đặt cọc
                                    </span>
                                    <br>
                                    <small style="color: #888; font-size: 0.85rem;">
                                        Còn nợ: <strong style="color: #ff9800;">
                                        <c:choose>
                                            <c:when test="${order.remainingAmount != null && order.remainingAmount > 0}">
                                                <fmt:formatNumber value="${order.remainingAmount}" type="currency" currencySymbol="₫"/>
                                            </c:when>
                                            <c:otherwise>
                                                ${order.formattedRemaining}
                                            </c:otherwise>
                                        </c:choose>
                                    </strong>
                                    </small>
                                </c:when>
                                <c:otherwise>
                                    <span class="payment-badge warning">
                                        <i class="fas fa-clock"></i> Đã thanh toán một phần
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <span class="payment-badge danger">
                                <i class="fas fa-exclamation-circle"></i> Chưa thanh toán
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <!-- Left Column -->
        <div class="col-lg-8">
            <!-- Order Items -->
            <div class="order-items">
                <h3 class="section-title">
                    <i class="fas fa-car"></i> Sản Phẩm
                </h3>

                <c:forEach var="detail" items="${order.orderDetails}">
                    <div class="order-item">
                        <c:choose>
                            <c:when test="${not empty detail.car.imageUrl}">
                                <img src="${detail.car.imageUrl}" alt="${detail.car.name}" class="item-image">
                            </c:when>
                            <c:otherwise>
                                <img src="https://via.placeholder.com/120?text=No+Image" alt="No Image" class="item-image">
                            </c:otherwise>
                        </c:choose>

                        <div class="item-info">
                            <div class="item-brand">${detail.car.brandName}</div>
                            <div class="item-name">${detail.car.name}</div>
                            <div class="item-specs">
                                <c:if test="${not empty detail.car.year}">
                                    <i class="fas fa-calendar"></i> ${detail.car.year}
                                </c:if>
                                <c:if test="${not empty detail.car.color}">
                                    | <i class="fas fa-palette"></i> ${detail.car.color}
                                </c:if>
                            </div>
                        </div>

                        <div class="item-pricing">
                            <div class="item-price">
                                <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="₫"/>
                            </div>
                            <div class="item-quantity">× ${detail.quantity}</div>
                            <div class="item-subtotal">
                                <fmt:formatNumber value="${detail.subtotal}" type="currency" currencySymbol="₫"/>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Transactions History -->
            <c:if test="${not empty order.transactions}">
            <div class="transactions-section">
                <h3 class="section-title">
                    <i class="fas fa-history"></i> Lịch Sử Giao Dịch
                </h3>

                <c:forEach var="transaction" items="${order.transactions}">
                <div class="transaction-item">
                    <div class="transaction-info">
                        <div>
                            <c:choose>
                                <c:when test="${transaction.paymentStatus == 'PAID'}">
                                    <i class="fas fa-check-circle" style="color: #4caf50;"></i>
                                </c:when>
                                <c:when test="${transaction.paymentStatus == 'PENDING'}">
                                    <i class="fas fa-clock" style="color: #ff9800;"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-times-circle" style="color: #f44336;"></i>
                                </c:otherwise>
                            </c:choose>
                            <span class="transaction-type">
                                        <c:choose>
                                            <c:when test="${transaction.type == 'FULL'}">Thanh toán toàn bộ</c:when>
                                            <c:when test="${transaction.type == 'DEPOSIT'}">Đặt cọc</c:when>
                                            <c:when test="${transaction.type == 'SHOWROOM'}">Thanh toán showroom</c:when>
                                            <c:otherwise>${transaction.type}</c:otherwise>
                                        </c:choose>
                                    </span>
                        </div>
                        <div class="transaction-status">
                            <c:choose>
                            <c:when test="${transaction.paymentStatus == 'PAID'}">
                            <span style="color: #4caf50;">✓ Đã thanh toán</span>
                            </c:when>
                                <c:when test="${transaction.paymentStatus == 'PENDING'}">
                                    <span style="color: #ff9800;">⏳ Chờ thanh toán</span>
                                </c:when>
                                <c:when test="${transaction.paymentStatus == 'CANCELLED'}">
                                    <span style="color: #f44336;">✗ Đã hủy</span>
                                </c:when>
                            </c:choose>
                        </div>
                        <div class="transaction-date">
                            <i class="far fa-clock"></i>
                            <fmt:formatDate value="${transaction.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </div>
                    </div>
                    <div class="transaction-amount">
                        <c:choose>
                            <c:when test="${transaction.paymentStatus == 'PAID'}">
                                <fmt:formatNumber value="${transaction.amount}" type="currency" currencySymbol="₫"/>
                            </c:when>
                            <c:otherwise>
<span style="color: #888;">
<fmt:formatNumber value="${transaction.amount}" type="currency" currencySymbol="₫"/>
</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                </c:forEach>
            </div>
            </c:if>
            <!-- Order Notes -->
            <c:if test="${not empty order.notes}">
                <div class="order-items">
                    <h3 class="section-title">
                        <i class="fas fa-sticky-note"></i> Ghi Chú
                    </h3>
                    <div style="padding: 15px; background: #0f0f0f; border-radius: 10px; color: #e0e0e0;">
                            ${order.notes}
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Right Column -->
        <div class="col-lg-4">
            <!-- Payment Summary -->
            <div class="payment-summary">
                <h3 class="section-title">
                    <i class="fas fa-calculator"></i> Thanh Toán
                </h3>

                <!-- Payment Type -->
                <div class="summary-row">
                    <span class="summary-label">Hình thức:</span>
                    <span class="summary-value">
                        ${order.paymentTypeDisplay}
                    </span>
                </div>

                <!-- Total Amount -->
                <div class="summary-row">
                    <span class="summary-label">Tổng cộng:</span>
                    <span class="summary-value">${order.formattedTotal}</span>
                </div>

                <!-- Deposit Amount (for DEPOSIT type) -->
                <c:if test="${order.paymentType == 'DEPOSIT' && order.depositAmount != null && order.depositAmount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Số tiền đặt cọc:</span>
                        <span class="summary-value" style="color: #2196f3;">
                        <fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="₫"/>
                    </span>
                    </div>
                </c:if>

                <!-- Paid Amount -->
                <c:if test="${order.paidAmount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Đã thanh toán:</span>
                        <span class="summary-value paid">
                        -${order.formattedPaid}
                    </span>
                    </div>
                </c:if>

                <!-- Remaining Amount -->
                <c:if test="${!order.fullyPaid}">
                    <div class="summary-row">
                    <span class="summary-label">
                        <c:choose>
                            <c:when test="${order.paymentType == 'DEPOSIT'}">Còn nợ:</c:when>
                            <c:when test="${order.paymentType == 'SHOWROOM'}">Thanh toán tại showroom:</c:when>
                            <c:otherwise>Còn lại:</c:otherwise>
                        </c:choose>
                    </span>
                        <span class="summary-value remaining">
                        <c:choose>
                            <c:when test="${order.remainingAmount != null && order.remainingAmount > 0}">
                                <fmt:formatNumber value="${order.remainingAmount}" type="currency" currencySymbol="₫"/>
                            </c:when>
                            <c:otherwise>
                                ${order.formattedRemaining}
                            </c:otherwise>
                        </c:choose>
                    </span>
                    </div>
                </c:if>

                <!-- Final Amount Display -->
                <div class="summary-row" style="margin-top: 20px; padding-top: 20px; border-top: 2px solid #444;">
                <span class="summary-label" style="font-size: 1.2rem;">
                    <c:choose>
                        <c:when test="${order.fullyPaid}">
                            <i class="fas fa-check-circle" style="color: #4caf50;"></i> Đã thanh toán:
                        </c:when>
                        <c:when test="${order.paymentType == 'DEPOSIT' && order.paidAmount > 0}">
                            <i class="fas fa-exclamation-circle" style="color: #ff9800;"></i> Còn phải trả:
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-clock" style="color: #ff9800;"></i> Cần thanh toán:
                        </c:otherwise>
                    </c:choose>
                </span>
                    <span class="summary-value total">
                    <c:choose>
                        <c:when test="${order.fullyPaid}">${order.formattedTotal}</c:when>
                        <c:when test="${order.remainingAmount != null && order.remainingAmount > 0}">
                            <fmt:formatNumber value="${order.remainingAmount}" type="currency" currencySymbol="₫"/>
                        </c:when>
                        <c:otherwise>${order.formattedRemaining}</c:otherwise>
                    </c:choose>
                </span>
                </div>

                <!-- Payment Notes -->
                <c:if test="${!order.fullyPaid && !order.cancelled}">
                    <div class="alert alert-info mt-3" style="background: rgba(33, 150, 243, 0.1); border: 1px solid rgba(33, 150, 243, 0.3); color: #2196f3;">
                        <i class="fas fa-info-circle"></i>
                        <c:choose>
                            <c:when test="${order.paymentType == 'DEPOSIT'}">
                                Vui lòng thanh toán số tiền còn lại khi nhận xe.
                            </c:when>
                            <c:when test="${order.paymentType == 'SHOWROOM'}">
                                Vui lòng đến showroom để thanh toán và nhận xe. Chúng tôi sẽ liên hệ với bạn sớm nhất.
                            </c:when>
                            <c:when test="${order.paymentType == 'FULL' && order.paidAmount == 0}">
                                Vui lòng hoàn tất thanh toán để xử lý đơn hàng.
                            </c:when>
                        </c:choose>
                    </div>
                </c:if>

                <!-- Success Message -->
                <c:if test="${order.fullyPaid}">
                    <div class="alert alert-success mt-3" style="background: rgba(76, 175, 80, 0.1); border: 1px solid rgba(76, 175, 80, 0.3); color: #4caf50;">
                        <i class="fas fa-check-circle"></i>
                        Đơn hàng đã được thanh toán đầy đủ. Cảm ơn bạn đã mua hàng!
                    </div>
                </c:if>

                <!-- Action Buttons -->
                <div class="action-buttons mt-4">
                    <!-- Payment Button -->
                    <c:if test="${!order.fullyPaid && !order.cancelled}">
                        <form method="post" action="${pageContext.request.contextPath}/payment">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <button type="submit" class="btn btn-payment">
                                <i class="fas fa-credit-card"></i>
                                <c:choose>
                                    <c:when test="${order.paidAmount > 0}">
                                        Thanh toán phần còn lại
                                        <c:if test="${order.remainingAmount != null && order.remainingAmount > 0}">
                                            (<fmt:formatNumber value="${order.remainingAmount}" type="currency" currencySymbol="₫"/>)
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        Thanh toán ngay
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </form>
                    </c:if>

                    <!-- Cancel Order Button -->
                    <c:if test="${order.canBeCancelled()}">
                        <form method="post" action="${pageContext.request.contextPath}/order-cancel">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <button type="submit" class="btn btn-cancel-order"
                                    onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')">
                                <i class="fas fa-times"></i> Hủy Đơn Hàng
                            </button>
                        </form>
                    </c:if>

                    <!-- Back Button -->
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-back">
                        <i class="fas fa-arrow-left"></i> Quay Lại Danh Sách
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Footer -->
<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto dismiss alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert-dismissible');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
</script>
</body>
</html>
