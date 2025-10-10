<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Về Chúng Tôi - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
        }

        /* Hero Section */
        .hero-about {
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
            url('https://images.unsplash.com/photo-1562141960-ddb427c27e52?auto=format&fit=crop&w=1500&q=80') center/cover;
            padding: 100px 0;
            margin-bottom: 50px;
            text-align: center;
            color: white;
            box-shadow: inset 0 0 100px rgba(0,0,0,0.5);
        }

        .hero-about h1 {
            font-size: 3rem;
            font-weight: 700;
            margin-bottom: 20px;
            color: #ffd700;
        }

        .hero-about p {
            font-size: 1.3rem;
            color: #e0e0e0;
        }

        /* Story Section */
        .story-section {
            background: #1a1a1a;
            border-radius: 15px;
            padding: 40px;
            margin-bottom: 50px;
            border: 1px solid #333;
        }

        .story-section h2 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 25px;
        }

        .story-section p {
            color: #b0b0b0;
            line-height: 1.8;
            font-size: 1.05rem;
        }

        .story-image {
            border-radius: 15px;
            border: 3px solid #333;
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.2);
            transition: all 0.3s;
        }

        .story-image:hover {
            transform: scale(1.02);
            border-color: #ffd700;
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.3);
        }

        /* Feature Cards */
        .feature-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 40px 30px;
            height: 100%;
            transition: all 0.3s;
        }

        .feature-card:hover {
            transform: translateY(-10px);
            border-color: #ffd700;
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.3);
        }

        .feature-card i {
            font-size: 3rem;
            margin-bottom: 20px;
        }

        .feature-card h3 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 15px;
            font-size: 1.5rem;
        }

        .feature-card p {
            color: #888;
            margin: 0;
            line-height: 1.6;
        }

        .icon-quality {
            color: #ffd700;
        }

        .icon-service {
            color: #4caf50;
        }

        .icon-price {
            color: #ff9800;
        }

        /* Why Choose Section */
        .why-choose-section {
            background: #1a1a1a;
            border-radius: 15px;
            padding: 50px 40px;
            margin-bottom: 50px;
            border: 1px solid #333;
        }

        .why-choose-section h2 {
            color: #ffd700;
            font-weight: 700;
            text-align: center;
            margin-bottom: 40px;
        }

        .list-group-item {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 15px 20px;
            margin-bottom: 10px;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .list-group-item:hover {
            background: #1a1a1a;
            border-color: #ffd700;
            transform: translateX(10px);
        }

        .list-group-item i {
            margin-right: 10px;
        }

        /* CTA Section */
        .cta-section {
            background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
            border: 2px solid #ffd700;
            padding: 60px 40px;
            border-radius: 20px;
            text-align: center;
            box-shadow: 0 10px 40px rgba(255, 215, 0, 0.2);
        }

        .cta-section h2 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .cta-section p {
            color: #b0b0b0;
            font-size: 1.2rem;
            margin-bottom: 30px;
        }

        .btn-cta-primary {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 15px 40px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-cta-primary:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-cta-outline {
            background: transparent;
            border: 2px solid #ffd700;
            color: #ffd700;
            padding: 15px 40px;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-cta-outline:hover {
            background: #ffd700;
            color: #1a1a1a;
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.4);
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Hero Section -->
<div class="hero-about">
    <div class="container">
        <h1><i class="fas fa-building"></i> Về Car Showroom</h1>
        <p>Đối tác tin cậy trong hành trình sở hữu xe hơi của bạn</p>
    </div>
</div>

<div class="container my-5">
    <!-- Story Section -->
    <div class="row mb-5 align-items-center">
        <div class="col-lg-6 mb-4">
            <img src="https://images.unsplash.com/photo-1562141960-ddb427c27e52?auto=format&fit=crop&w=800&q=80"
                 class="img-fluid story-image" alt="Car Showroom">
        </div>
        <div class="col-lg-6">
            <div class="story-section">
                <h2><i class="fas fa-book-open"></i> Câu Chuyện Của Chúng Tôi</h2>
                <p>
                    Car Showroom được thành lập với sứ mệnh mang đến cho khách hàng những trải nghiệm
                    mua sắm xe hơi tuyệt vời nhất. Với hơn 10 năm kinh nghiệm trong ngành, chúng tôi
                    tự hào là một trong những đại lý xe hơi uy tín hàng đầu tại Việt Nam.
                </p>
                <p>
                    Chúng tôi cung cấp đa dạng các dòng xe từ các thương hiệu nổi tiếng thế giới,
                    đảm bảo chất lượng và giá cả cạnh tranh. Đội ngũ nhân viên chuyên nghiệp của
                    chúng tôi luôn sẵn sàng tư vấn và hỗ trợ bạn tìm được chiếc xe phù hợp nhất.
                </p>
            </div>
        </div>
    </div>

    <!-- Feature Cards -->
    <div class="row text-center mb-5">
        <div class="col-md-4 mb-4">
            <div class="feature-card">
                <i class="fas fa-award icon-quality"></i>
                <h3>Chất Lượng Đảm Bảo</h3>
                <p>
                    Tất cả xe đều được kiểm tra kỹ lưỡng và có chế độ bảo hành uy tín
                </p>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="feature-card">
                <i class="fas fa-users icon-service"></i>
                <h3>Dịch Vụ Tận Tâm</h3>
                <p>
                    Đội ngũ chuyên nghiệp luôn sẵn sàng tư vấn và hỗ trợ 24/7
                </p>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="feature-card">
                <i class="fas fa-dollar-sign icon-price"></i>
                <h3>Giá Cả Hợp Lý</h3>
                <p>
                    Cam kết giá tốt nhất thị trường với nhiều chương trình ưu đãi hấp dẫn
                </p>
            </div>
        </div>
    </div>

    <!-- Why Choose Us -->
    <div class="why-choose-section">
        <h2><i class="fas fa-star"></i> Tại Sao Chọn Chúng Tôi?</h2>
        <div class="row">
            <div class="col-md-6 mb-3">
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Đa dạng các dòng xe từ nhiều thương hiệu
                </div>
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Tư vấn miễn phí và chuyên nghiệp
                </div>
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Hỗ trợ vay vốn ngân hàng lãi suất ưu đãi
                </div>
            </div>
            <div class="col-md-6">
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Bảo hành chính hãng và dịch vụ hậu mãi tốt
                </div>
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Giao xe tận nơi miễn phí
                </div>
                <div class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Đội ngũ kỹ thuật viên giàu kinh nghiệm
                </div>
            </div>
        </div>
    </div>

    <!-- CTA Section -->
    <div class="cta-section">
        <h2><i class="fas fa-heart"></i> Sẵn Sàng Tìm Chiếc Xe Mơ Ước?</h2>
        <p>
            Hãy để chúng tôi giúp bạn tìm được chiếc xe hoàn hảo cho nhu cầu của bạn!
        </p>
        <div class="d-flex gap-3 justify-content-center flex-wrap">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-cta-primary">
                <i class="fas fa-car"></i> Xem Xe
            </a>
            <a href="${pageContext.request.contextPath}/contact" class="btn btn-cta-outline">
                <i class="fas fa-phone"></i> Liên Hệ Ngay
            </a>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>