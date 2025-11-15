<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 12:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    :root{
        --panel:#151515;
        --line:#2a2a2a;
        --gold:#ffd700;
        --text:#f1f1f1;
        --muted:#b9b9b9;
    }
    .list-wrap{
        background:linear-gradient(180deg,#141414,#121212);
        border:1px solid var(--line);
        border-radius:14px;
        padding:14px;
    }

    .list-head h4{ margin:0; color:var(--gold); font-weight:800 }

    .tools input{
        background:#0f0f0f; color:var(--text);
        border:1px solid var(--line); border-radius:10px; padding:8px 10px;
    }

    .tablex{ width:100%; border-collapse:separate; border-spacing:0;
        overflow:hidden; border:1px solid var(--line); border-radius:12px }
    .tablex thead th{
        background:#1a1a1a; color:var(--gold);
        text-transform:uppercase; font-size:12px; letter-spacing:.5px;
        padding:10px 12px; border-bottom:1px solid var(--line);
        text-align:center;
    }
    .tablex tbody td{
        padding:12px; border-bottom:1px solid #1f1f1f;
        color:#eee; vertical-align:middle; text-align:center;
    }

    .chip{
        display:inline-block; padding:6px 10px; border-radius:999px;
        font-weight:700; font-size:.85rem;
    }
    .chip.ok{
        background:rgba(34,197,94,.12);
        color:#b8ffce; border:1px solid rgba(34,197,94,.3);
    }
    .chip.bad{
        background:rgba(239,68,68,.12);
        color:#ffb6b6; border:1px solid rgba(239,68,68,.3);
    }

    .btnx{
        display:inline-flex; align-items:center; gap:6px;
        padding:6px 12px; border-radius:999px;
        font-weight:700; font-size:.85rem; line-height:1;
        border:1px solid var(--line);
        background:#171717; color:#eee;
        text-decoration:none; transition:all .18s ease;
    }
    .btnx i{ color:inherit; }
    .btnx + .btnx{ margin-left:8px; }

    .btnx.update{
        background:rgba(255,215,0,.12);
        color:#ffe37a;
        border-color:rgba(255,215,0,.35);
    }
    .btnx.update:hover{
        background:rgba(255,215,0,.22);
        border-color:rgba(255,215,0,.55);
        box-shadow:0 0 0 3px rgba(255,215,0,.08);
        transform:translateY(-1px);
    }

    .btnx.view{

        background:rgba(59,130,246,.12);
        color:#b9d6ff;
        border-color:rgba(59,130,246,.35);
    }
    .btnx.view:hover{
        background:rgba(59,130,246,.22);
        border-color:rgba(59,130,246,.55);
        box-shadow:0 0 0 3px rgba(59,130,246,.08);
        transform:translateY(-1px);
    }

    .btnx:focus, .btnx:active { outline:none; box-shadow:none; text-decoration:none; }

</style>


<div class="list-wrap">

    <table class="tablex">
        <thead>
        <tr>
            <th>ID</th>
            <th>Tên xe</th>
            <th>Hãng</th>
            <th>Giá</th>
            <th>Tồn kho</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody id="carRows">
        <c:forEach var="car" items="${cars}">
            <tr>
                <td>${car.id}</td>
                <td class="text-start">${car.name}</td>
                <td>${car.brandName}</td>
                <td><fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/></td>
                <td>${car.stock}</td>
                <td>
          <span class="chip ${car.status == 'AVAILABLE' ? 'ok' : 'bad'}">
                  ${car.status == 'AVAILABLE' ? 'Còn hàng' : 'Hết hàng'}
          </span>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/staff/update-car?id=${car.id}"
                       class="btnx update">
                        <i class="fas fa-pen-to-square"></i> Cập nhật
                    </a>
                    <a href="${pageContext.request.contextPath}/staff/car-detail?id=${car.id}"
                       class="btnx view"
                       title="Xem chi tiết xe ${car.name}"
                       aria-label="Xem chi tiết xe ${car.name}">
                        <i class="fas fa-circle-info" aria-hidden="true"></i> Xem Chi Tiết
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>