<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Quản Trị</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            background-color: #0f0f0f;
            color: #f8f9fa;
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }

        /* Sidebar */
        .sidebar {
            width: 250px;
            background: #1c1c1c;
            flex-shrink: 0;
            padding: 20px;
            border-right: 1px solid #333;
        }

        .sidebar h5 {
            color: #ffd700;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .sidebar .menu-section {
            margin-bottom: 25px;
        }

        .sidebar .menu-section h6 {
            font-size: 0.9rem;
            text-transform: uppercase;
            color: #bbb;
            margin-bottom: 10px;
        }

        .sidebar .menu-item {
            display: block;
            padding: 10px 12px;
            color: #f0f0f0;
            text-decoration: none;
            border-radius: 8px;
            transition: all 0.2s;
        }

        .sidebar .menu-item:hover {
            background-color: #333;
            color: #ffd700;
        }

        /* Main content */
        .main-content {
            flex-grow: 1;
            background: #121212;
            padding: 20px;
        }

        /* Topbar */
        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #1a1a1a;
            padding: 10px 20px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .topbar .admin-info {
            font-weight: bold;
            color: #ffd700;
        }

        /* Cards */
        .stat-card {
            background: #1f1f1f;
            border: 1px solid #333;
            border-radius: 12px;
            padding: 20px;
            text-align: center;
            transition: transform 0.3s;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            border-color: #ffd700;
        }

        .stat-card h3 {
            color: #ffd700;
            font-size: 2rem;
        }

        .chart-container {
            background: #1f1f1f;
            border-radius: 12px;
            padding: 20px;
            border: 1px solid #333;
        }
    </style>
</head>
<body>

<div class="sidebar">
    <h5><i class="fas fa-car"></i> Admin Panel</h5>
    <div class="menu-section">
        <h6>Quản lý hệ thống</h6>
        <a href="${pageContext.request.contextPath}/cars" class="menu-item">
            <i class="fas fa-car"></i> Quản lý xe
        </a>
        <a href="${pageContext.request.contextPath}/admin/manage-brands" class="menu-item">
            <i class="fas fa-tags"></i> Quản lý hãng xe
        </a>
        <a href="${pageContext.request.contextPath}/admin/manage-users" class="menu-item">
            <i class="fas fa-users"></i> Quản lý người dùng
        </a>
        <a href="${pageContext.request.contextPath}/admin/manage-staff" class="menu-item">
            <i class="fas fa-user-tie"></i> Quản lý nhân viên
        </a>
    </div>


    <div class="menu-section">
        <h6>Kinh doanh</h6>
        <a href="${pageContext.request.contextPath}/admin/manage-orders" class="menu-item">
            <i class="fas fa-shopping-cart"></i> Quản lý đơn hàng
        </a>
        <a href="${pageContext.request.contextPath}/admin/manage-promotions" class="menu-item">
            <i class="fas fa-bullhorn"></i> Quản lý khuyến mãi
        </a>
        <a href="${pageContext.request.contextPath}/admin/revenue" class="menu-item">
            <i class="fas fa-chart-line"></i> Thống kê doanh thu
        </a>
    </div>


    <div class="menu-section">
        <h6>Khác</h6>
        <a href="${pageContext.request.contextPath}/logout" class="menu-item text-danger">
            <i class="fas fa-sign-out-alt"></i> Đăng xuất
        </a>
    </div>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Topbar -->
    <div class="topbar">
        <h4>Bảng điều khiển quản trị</h4>
        <div class="admin-info">
            Xin chào, <c:out value="${sessionScope.user.name}" />
        </div>
    </div>

    <!-- Dashboard Stats -->
    <div class="row g-4 mb-4">
        <div class="col-md-3">
            <div class="stat-card">
                <h3><c:out value="${carCount}" /></h3>
                <p>Xe hiện có</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3><c:out value="${userCount}" /></h3>
                <p>Người dùng</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3><c:out value="${orderCount}" /></h3>
                <p>Đơn hàng</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <h3><fmt:formatNumber value="${revenue}" type="currency" currencySymbol="₫"/></h3>
                <p>Doanh thu</p>
            </div>
        </div>
    </div>

    <!-- Chart -->
    <div class="chart-container mt-4">
        <h5 class="mb-3"><i class="fas fa-chart-line"></i> Thống kê doanh thu tháng</h5>
        <canvas id="revenueChart" height="100"></canvas>
    </div>
</div>

<!-- Chart.js demo -->
<script>
    const ctx = document.getElementById('revenueChart');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6'],
            datasets: [{
                label: 'Doanh thu (triệu ₫)',
                data: [120, 150, 180, 130, 220, 250],
                borderColor: '#ffd700',
                backgroundColor: 'rgba(255, 215, 0, 0.2)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            scales: {
                x: { grid: { color: '#333' } },
                y: { grid: { color: '#333' } }
            },
            plugins: {
                legend: { labels: { color: '#fff' } }
            }
        }
    });
</script>

</body>
</html>
