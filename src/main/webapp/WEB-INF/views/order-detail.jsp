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
            padding: 15px;
            background: #0f0f0f;
            border-radius: 10px;
            margin-bottom: 10px;
            border: 1px solid #333;
        }

        .transaction-info i {
            color: #4caf50;
            margin-right: 10px;
        }

        .transaction-type {
            color: #ffd700;
            font-weight: 600;
        }

        .transaction-date {
            color: #888;
            font-size: 0.9rem;
        }

        .transaction-amount {
            color: #4caf50;
            font-size: 1.2rem;
            font-weight: 700;
        }

        /* Actions */
        .action-buttons {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }

        .btn-payment {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 700;
            padding: 12px 30px;
            border-radius: 10px;
            transition: all 0.3s;
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
        }

        .btn-back:hover {
            background: #444;
            border-color: #666;
        }

        footer {
            margin-top: auto;
        }

        .alert {
            border-radius: 10px;
            border: none;
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
                <div class="info-label"><i class="fas fa-credit-card"></i> Trạng thái thanh toán</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${order.fullyPaid}">
                            <span style="color: #4caf50;">Đã thanh toán đủ</span>
                        </c:when>
                        <c:when test="${order.paidAmount > 0}">
                            <span style="color: #ff9800;">Đã thanh toán một phần</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #f44336;">Chưa thanh toán</span>
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
                            <div>
                                <div class="transaction-info">
                                    <i class="fas fa-check-circle"></i>
                                    <span class="transaction-type">${transaction.typeDisplay}</span>
                                </div>
                                <div class="transaction-date">
                                    <fmt:formatDate value="${transaction.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </div>
                            </div>
                            <div class="transaction-amount">
                                +${transaction.formattedAmount}
                            </div>
                        </div>
                    </c:forEach>
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

                <div class="summary-row">
                    <span class="summary-label">Tổng cộng:</span>
                    <span class="summary-value">${order.formattedTotal}</span>
                </div>

                <c:if test="${order.paidAmount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Đã thanh toán:</span>
                        <span class="summary-value paid">-${order.formattedPaid}</span>
                    </div>
                </c:if>

                <c:if test="${order.remainingAmount > 0}">
                    <div class="summary-row">
                        <span class="summary-label">Còn lại:</span>
                        <span class="summary-value remaining">${order.formattedRemaining}</span>
                    </div>
                </c:if>

                <div class="summary-row" style="margin-top: 20px; padding-top: 20px; border-top: 2px solid #444;">
                    <span class="summary-label" style="font-size: 1.2rem;">
                        <c:choose>
                            <c:when test="${order.fullyPaid}">Đã thanh toán:</c:when>
                            <c:otherwise>Cần thanh toán:</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="summary-value total">
                        <c:choose>
                            <c:when test="${order.fullyPaid}">${order.formattedTotal}</c:when>
                            <c:otherwise>${order.formattedRemaining}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons mt-4">
                    <c:if test="${!order.fullyPaid && !order.cancelled}">
                        <button type="button" class="btn btn-payment w-100" onclick="showPaymentModal()">
                            <i class="fas fa-credit-card"></i> Thanh Toán
                        </button>
                    </c:if>

                    <c:if test="${order.canBeCancelled()}">
                        <form method="post" action="${pageContext.request.contextPath}/order-cancel" class="w-100">
                            <input type="hidden" name="orderId" value="${order.orderId}">
                            <button type="submit" class="btn btn-cancel-order w-100"
                                    onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')">
                                <i class="fas fa-times"></i> Hủy Đơn Hàng
                            </button>
                        </form>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-back w-100">
                        <i class="fas fa-arrow-left"></i> Quay Lại
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Payment Modal -->
<div class="modal fade" id="paymentModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content" style="background: #1a1a1a; color: #e0e0e0; border: 1px solid #333;">
            <div class="modal-header" style="border-bottom: 1px solid #333;">
                <h5 class="modal-title" style="color: #ffd700;">
                    <i class="fas fa-credit-card"></i> Thanh Toán
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" style="filter: invert(1);"></button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/payment">
                <div class="modal-body">
                    <input type="hidden" name="orderId" value="${order.orderId}">

                    <div class="mb-3">
                        <label class="form-label">Số tiền thanh toán:</label>
                        <input type="number"
                               class="form-control"
                               name="amount"
                               value="${order.remainingAmount}"
                               min="1"
                               max="${order.remainingAmount}"
                               step="1000000"
                               required
                               style="background: #0f0f0f; border: 1px solid #333; color: #e0e0e0;">
                        <small class="text-muted">
                            Số tiền còn lại: ${order.formattedRemaining}
                        </small>
                    </div>
                </div>
                <div class="modal-footer" style="border-top: 1px solid #333;">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-payment">
                        <i class="fas fa-check"></i> Xác Nhận Thanh Toán
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function showPaymentModal() {
        const modal = new bootstrap.Modal(document.getElementById('paymentModal'));
        modal.show();
    }
</script>
</body>
</html>