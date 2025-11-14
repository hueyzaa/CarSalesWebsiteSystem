<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/10/2025
  Time: 4:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<style>
    :root {
        --panel: #141414;
        --line: #2a2a2a;
        --text: #f1f1f1;
        --muted: #b9b9b9;
    }

    .list-wrap {
        background: linear-gradient(180deg, #141414, #121212);
        border: 1px solid var(--line);
        border-radius: 14px;
        padding: 14px;
        width: 100%;              /* 🔹 chiếm hết chiều ngang parent */
        box-sizing: border-box;   /* 🔹 tính luôn padding vào 100% */
    }

    th, td {
        padding: 10px 12px;
        border-bottom: 1px solid var(--line);
        text-align: center;
    }

    th {
        color: var(--muted);
        font-weight: 600;
    }

    .status {
        padding: 2px 8px;
        border-radius: 999px;
        border: 1px solid var(--line);
        font-size: 12px;
    }

    table {
        width: 100%;              /* 🔹 bảng trải hết trong .list-wrap */
        border-collapse: collapse;
    }

    .status-true {
        color: #22c55e;
    }

    .status-false {
        color: #f43f5e;
    }
</style>
</head>
<body>
<div class="list-wrap">
    <div class="list-head">
        <h4>Danh sách người dùng</h4>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success text-center">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">${error}</div>
    </c:if>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Vai trò</th>
            <th>Tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Địa chỉ</th>
            <th>Ngày tạo</th>
            <th>Đăng nhập gần nhất</th>
            <th>Trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${customerList}">
            <tr>
                <td>${c.userId}</td>
                <td>${c.role}</td>
                <td>${c.name}</td>
                <td>${c.email}</td>
                <td>${c.phone}</td>
                <td>${c.address}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty c.createdAt}">
                            <fmt:formatDate value="${c.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty c.lastLogin}">
                            <fmt:formatDate value="${c.lastLogin}" pattern="dd/MM/yyyy HH:mm"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td>
                        <span class="status ${c.active ? 'status-true' : 'status-false'}">
                                ${c.active ? 'Hoạt động' : 'Bị khóa'}
                        </span>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty customerList}">
        <div class="alert alert-info text-center mt-3">
            Không có người dùng nào trong hệ thống.
        </div>
    </c:if>
</div>
</body>