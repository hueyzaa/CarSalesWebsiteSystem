<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 11/3/2025
  Time: 9:54 AM
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

    .btn-add-brand {
        background-color: #ffd700;
        color: #1a1a1a;
        font-weight: 600;
        border: none;
        padding: 10px 18px;
        border-radius: 8px;
        transition: all 0.2s ease;
    }

    .btn-add-brand:hover {
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
        <h4><i class="fas fa-tags"></i> Danh sách hãng xe</h4>
        <a href="${pageContext.request.contextPath}/Admin/add-brand" class="btn btn-add-brand">
            <i class="fas fa-plus-circle"></i> Thêm hãng xe
        </a>
    </div>

    <table class="table table-dark table-striped table-hover align-middle text-center">
        <thead>
        <tr>
            <th>ID</th>
            <th>Tên hãng</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="brand" items="${brands}">
            <tr>
                <td>${brand.brandId}</td>
                <td>${brand.brandName}</td>
                <td class="action-buttons">
                    <a href="${pageContext.request.contextPath}/Admin/update-brand?id=${brand.brandId}"
                       class="btn btn-warning btn-sm">
                        <i class="fas fa-edit"></i> Sửa
                    </a>
                    <form action="${pageContext.request.contextPath}/Admin/delete-brand"
                          method="post" style="display:inline;">
                        <input type="hidden" name="brandId" value="${brand.brandId}">
                        <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Bạn có chắc muốn xóa hãng xe này?');">
                            <i class="fas fa-trash"></i> Xóa
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty brands}">
        <div class="alert alert-warning text-center mt-3">
            Chưa có hãng xe nào trong hệ thống.
        </div>
    </c:if>
</div>

