<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 1:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    :root{ --line:#2a2a2a; --gold:#ffd700; --text:#f1f1f1; }
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
    .badge-type{display:inline-block;padding:4px 8px;border-radius:999px;border:1px solid rgba(255,215,0,.45);background:rgba(255,215,0,.12);color:#fff;font-weight:700}
    .pager{display:flex; gap:6px; justify-content:flex-end; margin-top:10px}
    .pager a, .pager span{padding:6px 10px;border:1px solid var(--line);border-radius:8px;color:#ddd;text-decoration:none}
    .pager .act{background:rgba(255,215,0,.18); border-color:rgba(255,215,0,.5); color:#fff}
</style>

<div class="panel">
    <form class="toolbox" method="get" action="${pageContext.request.contextPath}/staff/transactions">
        <input type="date" name="from" value="${q.from}">
        <input type="date" name="to" value="${q.to}">
        <select name="status">
            <option value="">Status</option>
            <option value="PENDING"  ${q.status=='PENDING'?'selected':''}>PENDING</option>
            <option value="PAID"     ${q.status=='PAID'?'selected':''}>PAID</option>
            <option value="CANCELLED"${q.status=='CANCELLED'?'selected':''}>CANCELLED</option>
        </select>
        <select name="type">
            <option value="">Type</option>
            <option value="FULL"     ${q.type=='FULL'?'selected':''}>FULL</option>
            <option value="DEPOSIT"  ${q.type=='DEPOSIT'?'selected':''}>DEPOSIT</option>
            <option value="SHOWROOM" ${q.type=='SHOWROOM'?'selected':''}>SHOWROOM</option>
        </select>
        <select name="sort">
            <option value="t.created_at DESC" ${q.sort=='t.created_at DESC'?'selected':''}>New → Old</option>
            <option value="t.created_at ASC"  ${q.sort=='t.created_at ASC'?'selected':''}>Old → New</option>
            <option value="t.amount DESC"     ${q.sort=='t.amount DESC'?'selected':''}>Amount ↓</option>
            <option value="t.amount ASC"      ${q.sort=='t.amount ASC'?'selected':''}>Amount ↑</option>
        </select>
        <input type="text" name="q" placeholder="TransactionID / OrderID…" value="${q.keyword}">
        <input type="hidden" name="page" value="1">
        <button class="btn-gold" type="submit">Apply</button>
    </form>

    <table class="tablex">
        <thead>
        <tr>
            <th>Txn ID</th>
            <th>Order</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Created</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${txns}">
            <tr>
                <td>${t.transactionId}</td>
                <td>${t.orderId}</td>
                <td><span class="badge-type">${t.type}</span></td>
                <td><fmt:formatNumber value="${t.amount}" type="currency" currencySymbol="₫"/></td>
                <td>
                    <c:choose>
                        <c:when test="${t.paymentStatus=='PAID'}"><span class="chip paid">PAID</span></c:when>
                        <c:when test="${t.paymentStatus=='PENDING'}"><span class="chip pending">PENDING</span></c:when>
                        <c:otherwise><span class="chip cancel">CANCELLED</span></c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${t.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty txns}">
            <tr><td colspan="6" style="color:#bbb">No data</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="pager">
        <c:forEach begin="1" end="${totalPages}" var="i">
            <c:choose>
                <c:when test="${i == page}">
                    <span class="act">${i}</span>
                </c:when>
                <c:otherwise>
                    <a href="?page=${i}&size=${size}&status=${q.status}&type=${q.type}&from=${q.from}&to=${q.to}&q=${q.keyword}&sort=${q.sort}">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</div>

