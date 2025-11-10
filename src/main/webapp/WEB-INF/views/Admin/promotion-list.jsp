<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 11/9/2025
  Time: 8:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-percent"></i> Danh sách khuyến mãi</h4>
        <a href="${pageContext.request.contextPath}/Admin/add-promotion" class="btn btn-warning btn-sm">
            <i class="fas fa-plus-circle"></i> Thêm khuyến mãi
        </a>
    </div>
    <table class="table table-dark table-striped table-hover text-center align-middle">
        <thead>
        <tr>
            <th>STT</th>
            <th>Tên khuyến mãi</th>
            <th>Mô tả</th>
            <th>Ngày bắt đầu</th>
            <th>Ngày kết thúc</th>
            <th>Giảm (%)</th>
            <th>Hành động</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="promo" items="${promotions}" varStatus="loop">
            <tr>
                <td>${loop.count}</td>
                <td class="fw-bold">${promo.title}</td>
                <td class="text-start" style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                    <c:out value="${promo.description}"/>
                </td>
                <td><fmt:formatDate value="${promo.startDate}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${promo.endDate}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <span class="badge bg-success">${promo.discountPercentage}%</span>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/Admin/update-promotion?id=${promo.promotionId}"
                       class="btn btn-sm btn-warning" title="Chỉnh sửa">
                        <i class="fas fa-edit"></i>
                    </a>

                    <form action="${pageContext.request.contextPath}/Admin/delete-promotion"
                          method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${promo.promotionId}">
                        <button type="submit" class="btn btn-sm btn-danger"
                                onclick="return confirm('Bạn có chắc chắn muốn xóa khuyến mãi này không?');"
                                title="Xóa khuyến mãi">
                            <i class="fas fa-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <c:if test="${empty promotions}">
        <div class="alert alert-info text-center mt-3">
            Không có khuyến mãi nào trong hệ thống.
        </div>
    </c:if>
</div>

