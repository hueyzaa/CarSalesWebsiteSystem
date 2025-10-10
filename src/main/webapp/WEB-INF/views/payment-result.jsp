<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết Quả Thanh Toán - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background: linear-gradient(135deg, #0f0f0f 0%, #1a1a1a 100%);
            color: #e0e0e0;
        }

        .result-container {
            max-width: 700px;
            margin: 80px auto;
            padding: 40px;
            background: #1a1a1a;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.5);
            border: 1px solid #333;
        }

        .result-icon {
            width: 100px;
            height: 100px;
            margin: 0 auto 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 50px;
        }

        .result-icon.success {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            color: white;
            animation: successPulse 2s infinite;
        }

        .result-icon.failed {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            animation: errorShake 0.5s;
        }

        @keyframes successPulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }

        @keyframes errorShake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        .result-title {
            font-size: 2rem;
            font-weight: 700;
            text-align: center;
            margin-bottom: 10px;
        }

        .result-title.success {
            color: #28a745;
        }

        .result-title.failed {
            color: #dc3545;
        }

        .result-message {
            text-align: center;
            font-size: 1.1rem;
            color: #888;
            margin-bottom: 40px;
        }

        .info-box {
            background: #0f0f0f;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 25px;
            border: 1px solid #333;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
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
            font-weight: 600;
            text-align: right;
        }

        .info-value.highlight {
            color: #ffd700;
            font-size: 1.2rem;
        }

        .btn-action {
            width: 100%;
            padding: 15px;
            font-size: 1.1rem;
            font-weight: 700;
            border-radius: 10px;
            border: none;
            margin-bottom: 10px;
            transition: all 0.3s;
        }

        .btn-primary-custom {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
        }

        .btn-primary-custom:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
        }

        .btn-secondary-custom {
            background: #333;
            color: #e0e0e0;
            border: 1px solid #555;
        }

        .btn-secondary-custom:hover {
            background: #444;
            border-color: #666;
        }

        footer {
            margin-top: auto;
        }

        .loading-spinner {
            border: 4px solid #333;
            border-top: 4px solid #ffd700;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Main Content -->
<div class="container">
    <div class="result-container">
        <!-- Check if payment was successful -->
        <c:choose>
            <c:when test="${param.success == 'true' || sessionScope.paymentSuccess == true}">
                <!-- SUCCESS -->
                <div class="result-icon success">
                    <i class="fas fa-check"></i>
                </div>

                <h1 class="result-title success">Thanh Toán Thành Công!</h1>
                <p class="result-message">
                        ${not empty sessionScope.paymentMessage ? sessionScope.paymentMessage : 'Đơn hàng của bạn đã được thanh toán thành công.'}
                </p>

                <div class="info-box">
                    <c:if test="${not empty sessionScope.paymentOrderId || not empty param.orderId}">
                        <div class="info-row">
                            <span class="info-label"><i class="fas fa-hashtag"></i> Mã đơn hàng:</span>
                            <span class="info-value">#${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}</span>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.paymentAmount}">
                        <div class="info-row">
                            <span class="info-label"><i class="fas fa-money-bill-wave"></i> Số tiền đã thanh toán:</span>
                            <span class="info-value highlight">
                                <fmt:formatNumber value="${sessionScope.paymentAmount}" type="currency" currencySymbol="₫"/>
                            </span>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.paymentTransactionNo}">
                        <div class="info-row">
                            <span class="info-label"><i class="fas fa-receipt"></i> Mã giao dịch VNPay:</span>
                            <span class="info-value">${sessionScope.paymentTransactionNo}</span>
                        </div>
                    </c:if>

                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-clock"></i> Thời gian:</span>
                        <span class="info-value">
                            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/order-detail?id=${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}"
                   class="btn btn-action btn-primary-custom">
                    <i class="fas fa-file-invoice"></i> Xem Chi Tiết Đơn Hàng
                </a>

                <a href="${pageContext.request.contextPath}/orders" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-list"></i> Xem Tất Cả Đơn Hàng
                </a>

                <a href="${pageContext.request.contextPath}/home" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-home"></i> Về Trang Chủ
                </a>

            </c:when>
            <c:otherwise>
                <!-- FAILED -->
                <div class="result-icon failed">
                    <i class="fas fa-times"></i>
                </div>

                <h1 class="result-title failed">Thanh Toán Thất Bại!</h1>
                <p class="result-message">
                        ${not empty sessionScope.paymentMessage ? sessionScope.paymentMessage : 'Giao dịch không thành công. Vui lòng thử lại.'}
                </p>

                <div class="info-box">
                    <c:if test="${not empty sessionScope.paymentResponseCode}">
                        <div class="info-row">
                            <span class="info-label"><i class="fas fa-exclamation-triangle"></i> Mã lỗi:</span>
                            <span class="info-value">${sessionScope.paymentResponseCode}</span>
                        </div>
                    </c:if>

                    <div class="info-row">
                        <span class="info-label"><i class="fas fa-clock"></i> Thời gian:</span>
                        <span class="info-value">
                            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-action btn-primary-custom">
                    <i class="fas fa-redo"></i> Thử Lại
                </a>

                <a href="${pageContext.request.contextPath}/cart" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-shopping-cart"></i> Quay Lại Giỏ Hàng
                </a>

                <a href="${pageContext.request.contextPath}/home" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-home"></i> Về Trang Chủ
                </a>

            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Clear session attributes after displaying -->
<c:if test="${not empty sessionScope.paymentSuccess}">
    <c:remove var="paymentSuccess" scope="session"/>
    <c:remove var="paymentMessage" scope="session"/>
    <c:remove var="paymentOrderId" scope="session"/>
    <c:remove var="paymentAmount" scope="session"/>
    <c:remove var="paymentTransactionNo" scope="session"/>
    <c:remove var="paymentResponseCode" scope="session"/>
</c:if>

</body>
</html>