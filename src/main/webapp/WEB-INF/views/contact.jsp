<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liên Hệ - Car Showroom</title>
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
        .hero-contact {
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)),
            url('https://images.unsplash.com/photo-1423666639041-f56000c27a9a?auto=format&fit=crop&w=1500&q=80') center/cover;
            padding: 80px 0;
            margin-bottom: 50px;
            text-align: center;
            color: white;
            box-shadow: inset 0 0 100px rgba(0,0,0,0.5);
        }

        .hero-contact h1 {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 15px;
            color: #ffd700;
        }

        .hero-contact p {
            font-size: 1.1rem;
            color: #e0e0e0;
        }

        /* Form Section */
        .contact-form-section {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 40px;
            height: 100%;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
        }

        .contact-form-section h2 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 30px;
        }

        .form-label {
            color: #e0e0e0;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .form-label i {
            color: #ffd700;
            margin-right: 8px;
        }

        .form-control, textarea.form-control {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #fff;
            padding: 12px 15px;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .form-control:focus, textarea.form-control:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
            color: #fff;
        }

        .form-control::placeholder {
            color: #666;
        }

        .btn-send {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            padding: 12px;
            font-weight: 600;
            border-radius: 10px;
            transition: all 0.3s;
            font-size: 1.1rem;
        }

        .btn-send:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .alert-success {
            background: rgba(46, 204, 113, 0.1);
            border: 1px solid rgba(46, 204, 113, 0.3);
            color: #2ecc71;
            border-radius: 10px;
        }

        /* Info Cards */
        .info-card {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
            transition: all 0.3s;
        }

        .info-card:hover {
            border-color: #ffd700;
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(255, 215, 0, 0.2);
        }

        .info-card h5 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .info-card h5 i {
            margin-right: 10px;
            font-size: 1.2rem;
        }

        .info-card p {
            color: #b0b0b0;
            margin: 0;
            line-height: 1.8;
        }

        .info-card a {
            color: #ffd700;
            text-decoration: none;
            transition: all 0.3s;
        }

        .info-card a:hover {
            color: #ffed4e;
            text-decoration: underline;
        }

        /* Social Section */
        .social-section {
            background: #1a1a1a;
            border: 1px solid #333;
            border-radius: 15px;
            padding: 30px;
            text-align: center;
        }

        .social-section h5 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .social-btn {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            border: 2px solid;
            transition: all 0.3s;
            text-decoration: none;
        }

        .social-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.3);
        }

        .social-btn.facebook {
            border-color: #3b5998;
            color: #3b5998;
        }

        .social-btn.facebook:hover {
            background: #3b5998;
            color: white;
        }

        .social-btn.twitter {
            border-color: #1da1f2;
            color: #1da1f2;
        }

        .social-btn.twitter:hover {
            background: #1da1f2;
            color: white;
        }

        .social-btn.instagram {
            border-color: #e1306c;
            color: #e1306c;
        }

        .social-btn.instagram:hover {
            background: #e1306c;
            color: white;
        }

        .social-btn.youtube {
            border-color: #ff0000;
            color: #ff0000;
        }

        .social-btn.youtube:hover {
            background: #ff0000;
            color: white;
        }

        footer {
            margin-top: auto;
        }

        .icon-address { color: #e74c3c; }
        .icon-phone { color: #2ecc71; }
        .icon-email { color: #3498db; }
        .icon-clock { color: #f39c12; }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Hero Section -->
<div class="hero-contact">
    <div class="container">
        <h1><i class="fas fa-envelope"></i> Liên Hệ Với Chúng Tôi</h1>
        <p>Chúng tôi luôn sẵn sàng lắng nghe và hỗ trợ bạn</p>
    </div>
</div>

<div class="container my-5">
    <div class="row">
        <!-- Contact Form -->
        <div class="col-lg-6 mb-4">
            <div class="contact-form-section">
                <h2><i class="fas fa-paper-plane"></i> Gửi Tin Nhắn</h2>

                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success" role="alert">
                        <i class="fas fa-check-circle"></i> ${sessionScope.success}
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/contact">
                    <div class="mb-3">
                        <label for="name" class="form-label">
                            <i class="fas fa-user"></i> Họ và Tên
                        </label>
                        <input type="text" class="form-control" id="name" name="name"
                               required placeholder="Nhập họ và tên của bạn">
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">
                            <i class="fas fa-envelope"></i> Email
                        </label>
                        <input type="email" class="form-control" id="email" name="email"
                               required placeholder="example@email.com">
                    </div>

                    <div class="mb-3">
                        <label for="subject" class="form-label">
                            <i class="fas fa-tag"></i> Chủ Đề
                        </label>
                        <input type="text" class="form-control" id="subject" name="subject"
                               required placeholder="Nhập chủ đề">
                    </div>

                    <div class="mb-3">
                        <label for="message" class="form-label">
                            <i class="fas fa-comment"></i> Tin Nhắn
                        </label>
                        <textarea class="form-control" id="message" name="message"
                                  rows="5" required placeholder="Nhập tin nhắn của bạn..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-send w-100">
                        <i class="fas fa-paper-plane"></i> Gửi Tin Nhắn
                    </button>
                </form>
            </div>
        </div>

        <!-- Contact Info -->
        <div class="col-lg-6">
            <div class="info-card">
                <h5>
                    <i class="fas fa-map-marker-alt icon-address"></i> Địa Chỉ
                </h5>
                <p>
                    Cần Thơ, Việt Nam
                </p>
            </div>

            <div class="info-card">
                <h5>
                    <i class="fas fa-phone icon-phone"></i> Điện Thoại
                </h5>
                <p>
                    <a href="tel:0123456789">0123 456 789</a><br>
                    <a href="tel:0987654321">0987 654 321</a>
                </p>
            </div>

            <div class="info-card">
                <h5>
                    <i class="fas fa-envelope icon-email"></i> Email
                </h5>
                <p>
                    <a href="mailto:info@carshowroom.com">info@carshowroom.com</a><br>
                    <a href="mailto:support@carshowroom.com">support@carshowroom.com</a>
                </p>
            </div>

            <div class="info-card">
                <h5>
                    <i class="fas fa-clock icon-clock"></i> Giờ Làm Việc
                </h5>
                <p>
                    <strong>Thứ 2 - Thứ 6:</strong> 8:00 - 17:00<br>
                    <strong>Thứ 7:</strong> 8:00 - 12:00<br>
                    <strong>Chủ Nhật:</strong> Nghỉ
                </p>
            </div>

            <div class="social-section">
                <h5><i class="fas fa-share-alt"></i> Theo Dõi Chúng Tôi</h5>
                <div class="d-flex gap-3 justify-content-center">
                    <a href="#" class="social-btn facebook" title="Facebook">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" class="social-btn twitter" title="Twitter">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#" class="social-btn instagram" title="Instagram">
                        <i class="fab fa-instagram"></i>
                    </a>
                    <a href="#" class="social-btn youtube" title="YouTube">
                        <i class="fab fa-youtube"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>