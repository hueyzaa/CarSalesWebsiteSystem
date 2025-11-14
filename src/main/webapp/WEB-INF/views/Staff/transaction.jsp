<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 1:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Transactions</title>
    <style>
        :root{ --line:#2a2a2a; --gold:#ffd700; --text:#f1f1f1; }
        body{background:#0b0b0b; font-family:system-ui,Segoe UI,Roboto,Arial; color:var(--text); margin:16px}
        .panel{background:linear-gradient(180deg,#151515,#131313); border:1px solid var(--line); border-radius:14px; padding:14px}
        .toolbox{display:flex; gap:10px; flex-wrap:wrap; margin-bottom:12px}
        .toolbox input, .toolbox select{background:#0f0f0f; color:#fff; border:1px solid var(--line); border-radius:10px; padding:8px 10px}
        .btn-gold{background:var(--gold); color:#111; font-weight:700; border:none; border-radius:10px; padding:8px 14px}
        .tablex{width:100%; border-collapse:separate; border-spacing:0; border:1px solid var(--line); border-radius:12px; overflow:hidden}
        .tablex thead th{background:#1a1a1a; color:var(--gold); text-transform:uppercase; font-size:12px; letter-spacing:.5px; padding:10px; border-bottom:1px solid var(--line)}
        .tablex tbody td{padding:12px; border-bottom:1px solid #1f1f1f; color:#eee; text-align:center}
        .chip{display:inline-block;padding:6px 10px;border-radius:999px;font-weight:700;font-size:.85rem}
        .chip.paid{background:rgba(34,197,94,.12); color:#b8ffce; border:1px solid rgba(34,197,94,.3)}
        .chip.pending{background:rgba(245,158,11,.12); color:#ffe1b3; border:1px solid rgba(245,158,11,.3)}
        .chip.cancel{background:rgba(239,68,68,.12); color:#ffb6b6; border:1px solid rgba(239,68,68,.3)}
        .chip.full{background:rgba(34,197,94,.12); color:#b8ffce; border:1px solid rgba(34,197,94,.3)}
        .chip.deposit{background:rgba(245,158,11,.12); color:#ffe1b3; border:1px solid rgba(245,158,11,.3)}
        .chip.showroom{background:rgba(239,68,68,.12); color:#ffb6b6; border:1px solid rgba(239,68,68,.3)}
        .badge-type{display:inline-block;padding:4px 8px;border-radius:999px;border:1px solid rgba(255,215,0,.45);background:rgba(255,215,0,.12);color:#fff;font-weight:700}
        .pager{display:flex; gap:6px; justify-content:flex-end; margin-top:10px}
        .pager a, .pager span{padding:6px 10px;border:1px solid var(--line);border-radius:8px;color:#ddd;text-decoration:none}
        .pager .act{background:rgba(255,215,0,.18); border-color:rgba(255,215,0,.5); color:#fff}
        .title{display:flex; align-items:center; gap:10px; margin-bottom:12px}
        .muted{color:#bbb; font-size:.95rem}
        .empty{padding:28px; text-align:center; color:#cfcfcf}
        .err{background:#2a0000; border:1px solid #550000; color:#ffcdcd; padding:10px 12px; border-radius:10px; margin-bottom:12px}
        .nowrap{white-space:nowrap}
    </style>
</head>
<body>

<div class="panel">
    <div class="title">
        <h2 style="margin:0;color:#fff">Transaction History</h2>
    </div>

    <c:set var="rows" value="${transactions}" />
    <c:choose>
        <c:when test="${empty rows}">
            <div class="empty">Chưa có giao dịch nào.</div>
        </c:when>
        <c:otherwise>
            <table class="tablex">
                <thead>
                <tr>
                    <th>Trans. ID</th>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Car</th>
                    <th>Datetime</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="it" items="${rows}">
                    <tr>
                        <td class="nowrap">#<c:out value="${it.transactionId}"/></td>
                        <td class="nowrap">#<c:out value="${it.orderId}"/></td>
                        <td><c:out value="${it.customerName}"/></td>
                        <td class="nowrap"><c:out value="${it.phone}"/></td>
                        <td><c:out value="${it.email}"/></td>
                        <td><c:out value="${empty it.carName ? '—' : it.carName}"/></td>
                        <td class="nowrap">
                            <fmt:formatDate value="${it.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <c:set var="st" value="${it.type}" />
                            <c:choose>
                                <c:when test="${st=='SHOWROOM'}">
                                    <span class="chip showroom">Cửa hàng</span>
                                </c:when>
                                <c:when test="${st=='FULL'}">
                                    <span class="chip full">Đầy đủ</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="chip deposit">Đặt cọc</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="nowrap">
                            <fmt:setLocale value="vi_VN"/>
                            <fmt:formatNumber value="${it.amount}" type="currency"/>
                        </td>
                        <td>
                            <c:set var="st" value="${it.paymentStatus}" />
                            <c:choose>
                                <c:when test="${st=='PAID'}">
                                    <span class="chip paid">PAID</span>
                                </c:when>
                                <c:when test="${st=='PENDING'}">
                                    <span class="chip pending">PENDING</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="chip cancel">CANCELLED</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>

