<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 10/29/2025
  Time: 2:19 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bảng điều khiển Staff</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #0f0f0f;
            color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }

        .sidebar {
            width: 260px;
            background: #1c1c1c;
            flex-shrink: 0;
            padding: 25px 20px;
            border-right: 1px solid #333;
        }

        .sidebar h5 {
            color: #ffd700;
            margin-bottom: 25px;
            font-weight: bold;
            font-size: 1.3rem;
        }

        .sidebar .menu-section {
            margin-bottom: 30px;
        }

        .sidebar .menu-section h6 {
            font-size: 0.95rem;
            text-transform: uppercase;
            color: #bbb;
            margin-bottom: 12px;
        }

        .sidebar .menu-item {
            display: block;
            padding: 12px 14px;
            color: #f0f0f0;
            text-decoration: none;
            border-radius: 10px;
            transition: all 0.2s;
            font-size: 0.95rem;
        }

        .sidebar .menu-item:hover {
            background-color: #333;
            color: #ffd700;
        }

        .main-content {
            flex-grow: 1;
            background: #121212;
            padding: 40px 50px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #1a1a1a;
            padding: 15px 25px;
            border-radius: 10px;
            margin-bottom: 40px;
        }

        .topbar h4 {
            font-size: 1.6rem;
            color: #fff;
            font-weight: bold;
        }

        .home-link {
            color: #ffd700;
            font-weight: 600;
            text-decoration: none;
            display: flex;
            align-items: center;
            font-size: 1rem;
            transition: all 0.2s ease;
        }

        .home-link i {
            margin-right: 8px;
        }

        .home-link:hover {
            color: #ffeb3b;
            transform: scale(1.05);
        }

        .card-dashboard {
            background: #1f1f1f;
            border: 1px solid #333;
            border-radius: 14px;
            padding: 30px 20px;
            text-align: center;
            transition: transform 0.3s ease, border-color 0.3s ease;
            height: 180px;
        }

        .card-dashboard:hover {
            transform: translateY(-6px);
            border-color: #ffd700;
        }

        .card-dashboard h3 {
            color: #ffd700;
            font-size: 2.4rem;
            margin-bottom: 10px;
        }

        .card-dashboard p {
            color: #ccc;
            font-size: 1rem;
            margin-bottom: 15px;
        }

        .btn-stat {
            font-size: 0.9rem;
            border-radius: 10px;
            font-weight: 500;
            padding: 6px 16px;
        }

        #staffDynamicContent {
            display: none;
            width: 100%;
            margin: 0 auto;
            background: #1a1a1a;
            padding: 25px;
            border-radius: 10px;
        }

        @media (max-width: 768px) {
            .card-dashboard {
                height: auto;
                padding: 20px;
            }
            .card-dashboard h3 {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>

<div class="sidebar">
    <h5><i class="fas fa-car"></i> Staff Panel</h5>

    <div class="menu-section">
        <h6>Quản lý hệ thống</h6>
        <a href="#" class="menu-item" id="btnManageCars"><i class="fas fa-car"></i> Quản lý xe</a>
        <a href="#" class="menu-item" id="btnManageUsers"><i class="fas fa-users"></i> Quản lý người dùng</a>
    </div>

    <div class="menu-section">
        <h6>Kinh doanh</h6>
        <a href="#" class="menu-item" id="btnManagePromotion"><i class="fas fa-bullhorn"></i> Xem khuyến mãi</a>
    </div>

    <div class="menu-section">
        <h6>Khác</h6>
        <a href="${pageContext.request.contextPath}/logout" class="menu-item text-danger"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
    </div>
</div>

<div class="main-content">
    <div class="topbar">
        <h4>Bảng điều khiển Staff</h4>
        <a href="${pageContext.request.contextPath}/home" class="home-link">
            <i class="fas fa-home"></i> Trang chủ
        </a>
    </div>


    <div id="overviewSection">
        <div class="container-fluid">
            <hr class="my-4 border-secondary">
            <div class="row g-4">
                <div class="col-lg-3 col-md-6">
                    <div class="card-dashboard">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="staffDynamicContent"></div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const dynamicContent = document.getElementById("staffDynamicContent");
        const overviewSection = document.getElementById("overviewSection");

        function loadSection(url) {
            overviewSection.style.display = "none";
            dynamicContent.style.display = "block";
            dynamicContent.innerHTML = "<div class='text-center text-light py-4'><i class='fas fa-spinner fa-spin'></i> Đang tải...</div>";

            fetch(url)
                .then(response => {
                    if (!response.ok) throw new Error("Không thể tải nội dung.");
                    return response.text();
                })
                .then(html => {
                    dynamicContent.innerHTML = html;
                })
                .catch(err => {
                    dynamicContent.innerHTML = `<div class='alert alert-danger text-center'>${err.message}</div>`;
                });
        }

        loadSection("${pageContext.request.contextPath}/staff/car-list");

        document.getElementById("btnManageCars").addEventListener("click", function(e) {
            e.preventDefault();
            loadSection("${pageContext.request.contextPath}/staff/car-list");
        });

        const params = new URLSearchParams(window.location.search);

        if(params.get("section") === "cars"){
            loadSection("${pageContext.request.contextPath}/staff/car-list");
        }
    });
</script>

</body>
</html>

