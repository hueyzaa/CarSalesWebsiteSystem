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
        <h4><i class="fas fa-user-tie"></i> Danh sách người dùng</h4>
        <a href="${pageContext.request.contextPath}/Admin/add-user" class="btn btn-warning btn-sm">
            <i class="fas fa-plus-circle"></i> Thêm người dùng
        </a>
    </div>

    <table class="table table-dark table-striped table-hover text-center align-middle">
        <thead>
        <tr>
            <th>ID</th>
            <th>Vai trò</th>
            <th>Tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Địa chỉ</th>
            <th>Ngày tạo</th>
            <th>Tình trạng</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${userList}">
            <tr>
                <td>${user.userId}</td>
                <td>${user.role}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>${user.address}</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <span class="badge ${user.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'}">
                            ${user.status}
                    </span>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/Admin/update-user?id=${user.userId}"
                       class="btn btn-warning btn-sm">
                        <i class="fas fa-edit"></i> Cập nhật
                    </a>
                    <form action="${pageContext.request.contextPath}/Admin/delete-user"
                          method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${user.userId}">
                        <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Xóa người dùng này?');">
                            <i class="fas fa-trash"></i> Xoá
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


