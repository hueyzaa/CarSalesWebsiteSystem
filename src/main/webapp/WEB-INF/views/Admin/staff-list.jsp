<%--
  Created by IntelliJ IDEA.
  User: HungNB
  Date: 10/18/2025
  Time: 8:55 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="admin-table-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-user-tie"></i> Danh sách nhân viên</h4>
        <a href="${pageContext.request.contextPath}/Admin/add-staff" class="btn btn-warning btn-sm">
            <i class="fas fa-plus-circle"></i> Thêm nhân viên
        </a>
    </div>

    <table class="table table-dark table-striped table-hover text-center align-middle">
        <thead>
        <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Địa chỉ</th>
            <th>Ngày tạo</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="staff" items="${staffList}">
            <tr>
                <td>${staff.userId}</td>
                <td>${staff.name}</td>
                <td>${staff.email}</td>
                <td>${staff.phone}</td>
                <td>${staff.address}</td>
                <td><fmt:formatDate value="${staff.createdAt}" pattern="dd/MM/yyyy"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/Admin/update-staff?id=${staff.userId}"
                       class="btn btn-warning btn-sm"><i class="fas fa-edit"></i>Cập nhật</a>
                    <form action="${pageContext.request.contextPath}/Admin/delete-staff"
                          method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${staff.userId}">
                        <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Xóa nhân viên này?');">
                            <i class="fas fa-trash"></i>Xoá
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
