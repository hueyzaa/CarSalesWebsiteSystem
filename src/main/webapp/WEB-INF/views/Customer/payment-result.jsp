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
            background-color: #0f0f0f;
            color: #e0e0e0;
        }

        .result-container {
            max-width: 650px;
            margin: 60px auto 40px;
            padding: 0 20px;
        }

        .result-card {
            background: #1a1a1a;
            border-radius: 15px;
            border: 1px solid #333;
            padding: 40px;
            text-align: center;
        }

        .result-icon {
            width: 90px;
            height: 90px;
            margin: 0 auto 25px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 45px;
        }

        .result-icon.success {
            background: #28a745;
            color: white;
            animation: successPulse 2s infinite;
        }

        .result-icon.failed {
            background: #dc3545;
            color: white;
            animation: errorShake 0.5s;
        }

        @keyframes successPulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }

        @keyframes errorShake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-8px); }
            75% { transform: translateX(8px); }
        }

        .result-title {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .result-title.success {
            color: #28a745;
        }

        .result-title.failed {
            color: #dc3545;
        }

        .result-message {
            font-size: 1rem;
            color: #999;
            margin-bottom: 30px;
        }

        .info-box {
            background: #0f0f0f;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 25px;
            text-align: left;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #2a2a2a;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #888;
            font-weight: 500;
            font-size: 0.95rem;
        }

        .info-value {
            color: #f8f9fa;
            font-weight: 600;
            text-align: right;
            font-size: 0.95rem;
        }

        .info-value.highlight {
            color: #ffd700;
            font-size: 1.1rem;
        }

        .btn-action {
            width: 100%;
            padding: 12px;
            font-size: 1rem;
            font-weight: 600;
            border-radius: 10px;
            border: none;
            margin-bottom: 10px;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary-custom {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a1a;
        }

        .btn-primary-custom:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-secondary-custom {
            background: #333;
            color: #e0e0e0;
            border: 1px solid #555;
        }

        .btn-secondary-custom:hover {
            background: #444;
            border-color: #666;
            color: #fff;
        }

        footer {
            margin-top: auto;
        }

        @media (max-width: 768px) {
            .result-card {
                padding: 30px 20px;
            }

            .result-icon {
                width: 80px;
                height: 80px;
                font-size: 40px;
            }

            .result-title {
                font-size: 1.5rem;
            }

            .info-row {
                flex-direction: column;
                gap: 5px;
            }

            .info-value {
                text-align: left;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="container result-container">
    <div class="result-card">
        <c:choose>
            <c:when test="${param.success == 'true' || sessionScope.paymentSuccess == true}">
                <!-- SUCCESS -->
                <div class="result-icon success">
                    <i class="fas fa-check"></i>
                </div>

                <h1 class="result-title success">Thanh toán thành công!</h1>
                <p class="result-message">
                        ${not empty sessionScope.paymentMessage ? sessionScope.paymentMessage : 'Đơn hàng của bạn đã được thanh toán thành công.'}
                </p>

                <div class="info-box">
                    <c:if test="${not empty sessionScope.paymentOrderId || not empty param.orderId}">
                        <div class="info-row">
                            <span class="info-label">Mã đơn hàng</span>
                            <span class="info-value">#${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}</span>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.paymentAmount}">
                        <div class="info-row">
                            <span class="info-label">Số tiền đã thanh toán</span>
                            <span class="info-value highlight">
                                <fmt:formatNumber value="${sessionScope.paymentAmount}" pattern="#,##0" /> ₫
                            </span>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.paymentTransactionNo}">
                        <div class="info-row">
                            <span class="info-label">Mã giao dịch VNPay</span>
                            <span class="info-value">${sessionScope.paymentTransactionNo}</span>
                        </div>
                    </c:if>

                    <div class="info-row">
                        <span class="info-label">Thời gian</span>
                        <span class="info-value">
                            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/order-detail?id=${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}"
                   class="btn btn-action btn-primary-custom">
                    <i class="fas fa-file-invoice"></i> Xem chi tiết đơn hàng
                </a>

                <a href="${pageContext.request.contextPath}/orders" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-list"></i> Danh sách đơn hàng
                </a>

                <a href="${pageContext.request.contextPath}/home" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>

            </c:when>
            <c:otherwise>
                <!-- FAILED -->
                <div class="result-icon failed">
                    <i class="fas fa-times"></i>
                </div>

                <h1 class="result-title failed">Thanh toán thất bại!</h1>
                <p class="result-message">
                        ${not empty sessionScope.paymentMessage ? sessionScope.paymentMessage : 'Giao dịch không thành công. Vui lòng thử lại.'}
                </p>

                <div class="info-box">
                    <c:if test="${not empty sessionScope.paymentOrderId || not empty param.orderId}">
                        <div class="info-row">
                            <span class="info-label">Mã đơn hàng</span>
                            <span class="info-value">#${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}</span>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.paymentResponseCode}">
                        <div class="info-row">
                            <span class="info-label">Mã lỗi</span>
                            <span class="info-value">${sessionScope.paymentResponseCode}</span>
                        </div>
                    </c:if>

                    <div class="info-row">
                        <span class="info-label">Thời gian</span>
                        <span class="info-value">
                            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>

                <!-- Retry payment button if order exists -->
                <c:if test="${not empty sessionScope.paymentOrderId || not empty param.orderId}">
                    <form method="post" action="${pageContext.request.contextPath}/checkout">
                        <input type="hidden" name="retryOrderId" value="${not empty sessionScope.paymentOrderId ? sessionScope.paymentOrderId : param.orderId}">
                        <button type="submit" class="btn btn-action btn-primary-custom">
                            <i class="fas fa-credit-card"></i> Thanh toán lại
                        </button>
                    </form>
                </c:if>

                <a href="${pageContext.request.contextPath}/orders" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-list"></i> Xem đơn hàng
                </a>

                <a href="${pageContext.request.contextPath}/home" class="btn btn-action btn-secondary-custom">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>

            </c:otherwise>
        </c:choose>
    </div>
</div>

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
