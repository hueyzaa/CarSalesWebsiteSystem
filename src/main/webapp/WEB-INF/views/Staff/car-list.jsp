<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 10/28/2025
  Time: 10:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    .staff-table-container {
        background: #1a1a1a;
        padding: 30px;
        border-radius: 15px;
        border: 1px solid #333;
        color: #f8f9fa;
        margin-top: 10px;
    }

    .staff-table-container h4 {
        color: #ffd700;
        font-weight: bold;
    }

    .table-dark th {
        background-color: #2a2a2a !important;
        color: #ffd700 !important;
    }

    .table-dark td {
        color: #f8f9fa;
        vertical-align: middle;
    }

    .badge {
        padding: 8px 14px;
        font-size: 0.9rem;
        font-weight: 600;
        border-radius: 10px;
    }

    .bg-success {
        background-color: #28a745 !important;
    }

    .bg-danger {
        background-color: #dc3545 !important;
    }

    .action-buttons i {
        margin-right: 5px;
    }

    .action-buttons .btn {
        margin-right: 5px;
        border-radius: 8px;
    }
</style>

<div class="staff-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-car"></i> Danh sách xe</h4>
    </div>

    <table class="table table-dark table-striped table-hover align-middle text-center">
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
        <tbody>
        <c:forEach var="car" items="${cars}">
            <tr>
                <td>${car.id}</td>
                <td class="text-start">${car.name}</td>
                <td>${car.brandName}</td>
                <td>
                    <fmt:formatNumber value="${car.price}" type="number" groupingUsed="true"/> ₫
                </td>
                <td>${car.stock}</td>
                <td>
                    <span class="badge ${car.status == 'AVAILABLE' ? 'bg-success' : 'bg-danger'}">
                            ${car.status == 'AVAILABLE' ? 'Còn hàng' : 'Hết hàng'}
                    </span>
                </td>
                <td class="action-buttons">
                    <a href="${pageContext.request.contextPath}/staff/update-car?id=${car.id}"
                       class="btn btn-warning btn-sm">
                        <i class="fas fa-edit"></i> Cập nhật
                    </a>
                    <form action="${pageContext.request.contextPath}/staff/car-detail"
                          method="get" style="display:inline;">
                        <input type="hidden" name="id" value="${car.id}">
                        <button type="submit" class="btn btn-danger btn-sm">
                            <i class="fas fa-trash"></i> Xem chi tiết
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>