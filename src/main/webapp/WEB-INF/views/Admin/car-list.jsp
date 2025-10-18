<%--
  Created by IntelliJ IDEA.
  User: HungNB
  Date: 10/15/2025
  Time: 8:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    .admin-table-container {
        background: #1a1a1a;
        padding: 30px;
        border-radius: 15px;
        border: 1px solid #333;
        color: #f8f9fa;
        margin-top: 10px;
    }

    .admin-table-container h4 {
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

    .btn-add-car {
        background-color: #ffd700;
        color: #1a1a1a;
        font-weight: 600;
        border: none;
        padding: 10px 18px;
        border-radius: 8px;
        transition: all 0.2s ease;
    }

    .btn-add-car:hover {
        background-color: #ffed4e;
        transform: translateY(-2px);
    }

    .action-buttons i {
        margin-right: 5px;
    }

    .action-buttons .btn {
        margin-right: 5px;
        border-radius: 8px;
    }
</style>

<div class="admin-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-car"></i> Danh sách xe</h4>
        <a href="${pageContext.request.contextPath}/Admin/add-car" class="btn btn-add-car">
            <i class="fas fa-plus-circle"></i> Thêm xe mới
        </a>
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
                    <a href="${pageContext.request.contextPath}/Admin/update-car?id=${car.id}"
                       class="btn btn-warning btn-sm">
                        <i class="fas fa-edit"></i> Cập nhật
                    </a>
                    <form action="${pageContext.request.contextPath}/Admin/delete-car"
                          method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${car.id}">
                        <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Bạn có chắc muốn xóa xe này?');">
                            <i class="fas fa-trash"></i> Xóa
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
