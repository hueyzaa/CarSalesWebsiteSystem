<%--
  Created by IntelliJ IDEA.
  User: hungn
  Date: 10/28/2025
  Time: 7:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-users"></i> Danh sách người dùng</h4>
    </div>

    <!-- Alerts -->
    <c:if test="${not empty success}">
        <div class="alert alert-success text-center">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <!-- User Table -->
    <table class="table table-dark table-striped table-hover text-center align-middle">
        <thead>
        <tr>
            <th>STT</th>
            <th>Vai trò</th>
            <th>Tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Địa chỉ</th>
            <th>Ngày tạo</th>
            <th>Đăng nhập gần nhất</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${userList}" varStatus="loop">
            <tr>
                <td>${loop.count}</td>

                <!-- Role Badge -->
                <td>
                    <c:choose>
                        <c:when test="${user.role eq 'STAFF'}">
                            <span class="badge bg-primary">Nhân viên</span>
                        </c:when>
                        <c:when test="${user.role eq 'CUSTOMER'}">
                            <span class="badge bg-success">Khách hàng</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger">${user.role}</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>${user.address}</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty user.lastLogin}">
                            <fmt:formatDate value="${user.lastLogin}" pattern="dd/MM/yyyy"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>

                <!-- Status Badge -->
                <td>
                    <c:choose>
                        <c:when test="${user.active}">
                            <span class="badge bg-success">Hoạt động</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Bị khóa</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <!-- Action Buttons -->
                <td>
                    <!-- Update Button -->
                    <a href="${pageContext.request.contextPath}/Admin/update-user?id=${user.userId}"
                       class="btn btn-sm btn-warning" title="Cập nhật">
                        <i class="fas fa-edit"></i>
                    </a>



                    <!-- Delete Form -->
                    <form action="${pageContext.request.contextPath}/Admin/delete-user"
                          method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${user.userId}">
                        <button type="submit" class="btn btn-sm btn-danger"
                                onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này không?');"
                                title="Xóa người dùng">
                            <i class="fas fa-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty userList}">
        <div class="alert alert-info text-center mt-3">
            Không có người dùng nào trong hệ thống.
        </div>
    </c:if>
</div>



