<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/28/2025
  Time: 7:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/28/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-shopping-cart text-warning"></i> Danh sách đơn hàng</h4>
    </div>

    <style>
        .admin-table-container {
            background: #1a1a1a;
            padding: 30px;
            border-radius: 15px;
            border: 1px solid #333;
            color: #f8f9fa;
        }

        h4 {
            color: #ffd700;
            font-weight: bold;
        }

        .table td, .table th {
            vertical-align: middle;
        }

        .action-buttons .btn {
            margin: 2px;
            border-radius: 8px;
        }
    </style>

    <!-- Thông báo -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success text-center">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger text-center">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <table class="table table-dark table-striped table-hover text-center align-middle">
        <thead>
        <tr>
            <th>Mã đơn</th>
            <th>Khách hàng</th>
            <th>Ngày tạo</th>
            <th>Thanh toán</th>
            <th>Trạng thái</th>
            <th>Tiền cọc</th>
            <th>Tiền còn lại</th>
            <th>Ghi chú</th>
            <th>Hành động</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>#${order.orderId}</td>
                <td>${order.userId}</td>
                <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>

                <td>
                    <c:choose>
                        <c:when test="${order.paymentType eq 'CASH'}">Tiền mặt</c:when>
                        <c:when test="${order.paymentType eq 'BANK'}">Chuyển khoản</c:when>
                        <c:otherwise>${order.paymentType}</c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <span class="badge
                        ${order.status eq 'PENDING' ? 'bg-secondary' :
                          (order.status eq 'APPROVED' ? 'bg-info' :
                          (order.status eq 'COMPLETED' ? 'bg-success' :
                          (order.status eq 'CANCELLED' ? 'bg-danger' : 'bg-warning')))}">

                        <c:choose>
                            <c:when test="${order.status eq 'PENDING'}">Chờ duyệt</c:when>
                            <c:when test="${order.status eq 'APPROVED'}">Đã duyệt</c:when>
                            <c:when test="${order.status eq 'COMPLETED'}">Hoàn tất</c:when>
                            <c:when test="${order.status eq 'CANCELLED'}">Đã hủy</c:when>
                            <c:otherwise>${order.status}</c:otherwise>
                        </c:choose>
                    </span>
                </td>

                <td><fmt:formatNumber value="${order.depositAmount}" type="number" groupingUsed="true"/> ₫</td>
                <td><fmt:formatNumber value="${order.remainingAmount}" type="number" groupingUsed="true"/> ₫</td>

                <td class="text-start">${order.notes}</td>

                <td class="action-buttons">

                    <!-- DUYỆT -->
                    <form action="${pageContext.request.contextPath}/Admin/order-list" method="post" class="d-inline">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <input type="hidden" name="action" value="approve">
                        <button class="btn btn-sm btn-success"
                                title="Duyệt đơn hàng"
                                onclick="return confirm('Bạn có chắc muốn duyệt đơn hàng này?');">
                            <i class="fas fa-check"></i>
                        </button>
                    </form>

                    <!-- HỦY -->
                    <form action="${pageContext.request.contextPath}/Admin/order-list" method="post" class="d-inline">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <input type="hidden" name="action" value="cancel">
                        <button class="btn btn-sm btn-danger"
                                title="Hủy đơn hàng"
                                onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?');">
                            <i class="fas fa-times"></i>
                        </button>
                    </form>

                    <!-- HOÀN TẤT -->
                    <form action="${pageContext.request.contextPath}/Admin/order-list" method="post" class="d-inline">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <input type="hidden" name="action" value="complete">
                        <button class="btn btn-sm btn-warning"
                                title="Xác nhận hoàn tất đơn hàng"
                                onclick="return confirm('Xác nhận hoàn tất đơn hàng này?');">
                            <i class="fas fa-flag-checkered"></i>
                        </button>
                    </form>

                    <!-- Cập nhật trạng thái -->
                    <form action="${pageContext.request.contextPath}/Admin/order-list" method="post" class="d-inline">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <input type="hidden" name="action" value="update">

                        <select name="status"
                                class="form-select form-select-sm d-inline w-auto bg-dark text-light border-secondary"
                                onchange="this.form.submit()">

                            <option disabled selected>Chọn trạng thái</option>
                            <option value="PENDING">Chờ duyệt</option>
                            <option value="APPROVED">Đã duyệt</option>
                            <option value="COMPLETED">Hoàn tất</option>
                            <option value="CANCELLED">Đã hủy</option>
                        </select>
                    </form>

                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty orders}">
            <tr>
                <td colspan="9" class="text-center text-muted py-3">
                    Không có đơn hàng nào trong hệ thống.
                </td>
            </tr>
        </c:if>

        </tbody>
    </table>
</div>


