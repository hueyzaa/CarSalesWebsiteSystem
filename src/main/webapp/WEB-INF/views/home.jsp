<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Car Showroom</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }

        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .navbar-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar-brand {
            font-size: 24px;
            font-weight: bold;
        }

        .navbar-menu {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .navbar-menu a {
            color: white;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 5px;
            transition: background 0.3s;
        }

        .navbar-menu a:hover {
            background: rgba(255,255,255,0.2);
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-name {
            font-weight: 500;
        }

        .user-badge {
            background: rgba(255,255,255,0.3);
            padding: 4px 12px;
            border-radius: 15px;
            font-size: 12px;
        }

        .btn-logout {
            background: rgba(255,255,255,0.2);
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s;
        }

        .btn-logout:hover {
            background: rgba(255,255,255,0.3);
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .welcome-section {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .welcome-section h1 {
            color: #333;
            margin-bottom: 10px;
        }

        .welcome-section p {
            color: #666;
            font-size: 16px;
        }

        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }

        .feature-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s;
        }

        .feature-card:hover {
            transform: translateY(-5px);
        }

        .feature-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }

        .feature-card h3 {
            color: #333;
            margin-bottom: 10px;
        }

        .feature-card p {
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-brand">🚗 Car Showroom</div>
        <div class="navbar-menu">
            <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
            <a href="${pageContext.request.contextPath}/cars">Xe Hơi</a>
            <a href="${pageContext.request.contextPath}/cart">Giỏ Hàng</a>
            <a href="${pageContext.request.contextPath}/orders">Đơn Hàng</a>
            <div class="user-info">
                <span class="user-name">👤 ${sessionScope.userName}</span>
                <span class="user-badge">${sessionScope.userRole}</span>
                <a href="${pageContext.request.contextPath}/logout">
                    <button class="btn-logout">Đăng Xuất</button>
                </a>
            </div>
        </div>
    </div>
</nav>

<div class="container">
    <div class="welcome-section">
        <h1>Chào mừng, ${sessionScope.userName}!</h1>
        <p>Khám phá bộ sưu tập xe hơi cao cấp của chúng tôi</p>
    </div>

    <div class="feature-grid">
        <div class="feature-card">
            <div class="feature-icon">🚙</div>
            <h3>Xe Hơi Chất Lượng</h3>
            <p>Đa dạng các dòng xe từ các thương hiệu hàng đầu thế giới</p>
        </div>

        <div class="feature-card">
            <div class="feature-icon">💰</div>
            <h3>Giá Cả Hợp Lý</h3>
            <p>Chính sách giá tốt nhất thị trường với nhiều ưu đãi hấp dẫn</p>
        </div>

        <div class="feature-card">
            <div class="feature-icon">🎁</div>
            <h3>Khuyến Mãi</h3>
            <p>Nhiều chương trình khuyến mãi và quà tặng giá trị</p>
        </div>

        <div class="feature-card">
            <div class="feature-icon">🛡️</div>
            <h3>Bảo Hành</h3>
            <p>Chính sách bảo hành toàn diện và hỗ trợ sau bán hàng</p>
        </div>
    </div>
</div>
</body>
</html>