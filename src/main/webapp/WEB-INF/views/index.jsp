<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome - Car Showroom</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow-x: hidden;
        }

        .welcome-container {
            max-width: 1200px;
            padding: 40px 20px;
            text-align: center;
        }

        .hero-section {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            padding: 60px 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            backdrop-filter: blur(10px);
        }

        .logo {
            font-size: 80px;
            margin-bottom: 20px;
            animation: bounce 2s infinite;
        }

        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-10px); }
        }

        h1 {
            font-size: 48px;
            color: #333;
            margin-bottom: 15px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .subtitle {
            font-size: 20px;
            color: #666;
            margin-bottom: 40px;
        }

        .cta-buttons {
            display: flex;
            gap: 20px;
            justify-content: center;
            flex-wrap: wrap;
            margin-bottom: 50px;
        }

        .btn {
            padding: 15px 40px;
            font-size: 18px;
            font-weight: 600;
            border: none;
            border-radius: 50px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #667eea;
            color: white;
            transform: translateY(-3px);
        }

        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
            margin-top: 50px;
        }

        .feature-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }

        .feature-card:hover {
            transform: translateY(-10px);
        }

        .feature-icon {
            font-size: 50px;
            margin-bottom: 15px;
        }

        .feature-card h3 {
            color: #333;
            margin-bottom: 10px;
            font-size: 20px;
        }

        .feature-card p {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
        }

        .stats-section {
            display: flex;
            justify-content: space-around;
            margin-top: 40px;
            flex-wrap: wrap;
            gap: 20px;
        }

        .stat-item {
            text-align: center;
            padding: 20px;
        }

        .stat-number {
            font-size: 48px;
            font-weight: bold;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .stat-label {
            color: #666;
            font-size: 16px;
            margin-top: 5px;
        }

        .divider {
            height: 2px;
            background: linear-gradient(to right, transparent, #ddd, transparent);
            margin: 40px 0;
        }

        @media (max-width: 768px) {
            h1 {
                font-size: 36px;
            }

            .subtitle {
                font-size: 16px;
            }

            .btn {
                padding: 12px 30px;
                font-size: 16px;
            }

            .hero-section {
                padding: 40px 20px;
            }
        }
    </style>
</head>
<body>
<div class="welcome-container">
    <div class="hero-section">
        <div class="logo">🚗</div>
        <h1>Car Showroom</h1>
        <p class="subtitle">Hệ thống quản lý và bán xe hơi cao cấp</p>

        <div class="cta-buttons">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                🔐 Đăng Nhập
            </a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">
                📝 Đăng Ký Ngay
            </a>
        </div>

        <div class="stats-section">
            <div class="stat-item">
                <div class="stat-number">500+</div>
                <div class="stat-label">Xe Có Sẵn</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">50+</div>
                <div class="stat-label">Thương Hiệu</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">10K+</div>
                <div class="stat-label">Khách Hàng</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">99%</div>
                <div class="stat-label">Hài Lòng</div>
            </div>
        </div>

        <div class="divider"></div>

        <div class="features">
            <div class="feature-card">
                <div class="feature-icon">🏆</div>
                <h3>Chất Lượng Cao</h3>
                <p>Đa dạng các dòng xe từ các thương hiệu hàng đầu thế giới với chất lượng được kiểm định</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">💎</div>
                <h3>Giá Tốt Nhất</h3>
                <p>Chính sách giá cạnh tranh nhất thị trường với nhiều ưu đãi hấp dẫn</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">🎁</div>
                <h3>Khuyến Mãi</h3>
                <p>Nhiều chương trình khuyến mãi và quà tặng giá trị cho khách hàng</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">🛡️</div>
                <h3>Bảo Hành</h3>
                <p>Chính sách bảo hành toàn diện và hỗ trợ sau bán hàng chuyên nghiệp</p>
            </div>
        </div>
    </div>
</div>

<script>
    // Check if user is already logged in
    <% if (session != null && session.getAttribute("user") != null) { %>
    window.location.href = '${pageContext.request.contextPath}/home';
    <% } %>
</script>
</body>
</html>