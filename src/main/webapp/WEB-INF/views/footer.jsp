<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .footer {
        background: linear-gradient(135deg, #1a1a1a 0%, #0f0f0f 100%);
        color: #b0b0b0;
        padding: 60px 0 20px;
        margin-top: auto;
        border-top: 2px solid #ffd700;
        box-shadow: 0 -4px 20px rgba(0,0,0,0.5);
    }

    .footer h5 {
        color: #ffd700 !important;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 20px;
        font-size: 1.1rem;
    }

    .footer .brand-footer {
        color: #fff !important;
        font-size: 1.3rem;
        margin-bottom: 15px;
    }

    .footer .brand-footer i {
        color: #ffd700;
    }

    .footer p {
        font-size: 0.9rem;
        line-height: 1.8;
        color: #888;
    }

    .footer ul {
        list-style: none;
        padding: 0;
    }

    .footer ul li {
        margin-bottom: 10px;
    }

    .footer ul li a {
        color: #b0b0b0;
        text-decoration: none;
        transition: all 0.3s;
        display: inline-block;
    }

    .footer ul li a:hover {
        color: #ffd700;
        padding-left: 5px;
        text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
    }

    .footer .contact-info i {
        color: #ffd700;
        width: 20px;
        margin-right: 8px;
    }

    .footer hr {
        border-color: #333 !important;
        margin: 30px 0 20px;
    }

    .footer-bottom {
        text-align: center;
        padding-top: 20px;
        border-top: 1px solid #333;
    }

    .footer-bottom p {
        margin: 5px 0;
        font-size: 0.85rem;
    }

    .footer-bottom a {
        color: #888;
        text-decoration: none;
        transition: all 0.3s;
    }

    .footer-bottom a:hover {
        color: #ffd700;
    }

    .social-links {
        margin-top: 15px;
    }

    .social-links a {
        display: inline-block;
        width: 40px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        border-radius: 50%;
        background: #2a2a2a;
        color: #888;
        margin: 0 5px;
        transition: all 0.3s;
        border: 1px solid #333;
    }

    .social-links a:hover {
        background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        color: #1a1a1a;
        border-color: #ffd700;
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
    }
</style>

<footer class="footer">
    <div class="container">
        <div class="row mb-4">
            <!-- About Section -->
            <div class="col-lg-4 col-md-6 mb-4 mb-lg-0">
                <h5 class="brand-footer">
                    <i class="fas fa-car"></i> Car Showroom
                </h5>
                <p>Khám phá bộ sưu tập xe hơi đẳng cấp với giá tốt nhất. Chúng tôi cam kết mang đến trải nghiệm mua sắm xe hơi tuyệt vời nhất.</p>
                <div class="social-links">
                    <a href="#" title="Facebook"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" title="Instagram"><i class="fab fa-instagram"></i></a>
                    <a href="#" title="Twitter"><i class="fab fa-twitter"></i></a>
                    <a href="#" title="YouTube"><i class="fab fa-youtube"></i></a>
                </div>
            </div>

            <!-- Quick Links -->
            <div class="col-lg-2 col-md-6 mb-4 mb-lg-0">
                <h5>Liên Kết</h5>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/"><i class="fas fa-angle-right"></i> Trang Chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/cars"><i class="fas fa-angle-right"></i> Xem Xe</a></li>
                    <li><a href="${pageContext.request.contextPath}/about"><i class="fas fa-angle-right"></i> Giới Thiệu</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact"><i class="fas fa-angle-right"></i> Liên Hệ</a></li>
                </ul>
            </div>

            <!-- Customer Service -->
            <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
                <h5>Dịch Vụ</h5>
                <ul>
                    <li><a href="#"><i class="fas fa-angle-right"></i> Chính Sách Bảo Mật</a></li>
                    <li><a href="#"><i class="fas fa-angle-right"></i> Điều Khoản Dịch Vụ</a></li>
                    <li><a href="#"><i class="fas fa-angle-right"></i> Chính Sách Đổi Trả</a></li>
                    <li><a href="#"><i class="fas fa-angle-right"></i> Hỗ Trợ Khách Hàng</a></li>
                </ul>
            </div>

            <!-- Contact Info -->
            <div class="col-lg-3 col-md-6">
                <h5>Liên Hệ</h5>
                <div class="contact-info">
                    <p>
                        <i class="fas fa-map-marker-alt"></i> Cần Thơ, Việt Nam
                    </p>
                    <p>
                        <i class="fas fa-phone"></i> 0123 456 789
                    </p>
                    <p>
                        <i class="fas fa-envelope"></i> info@carshowroom.com
                    </p>
                    <p>
                        <i class="fas fa-clock"></i> T2-T7: 8:00 - 20:00<br>
                        <span style="padding-left: 28px;">CN: 9:00 - 18:00</span>
                    </p>
                </div>
            </div>
        </div>

        <!-- Footer Bottom -->
        <div class="footer-bottom">
            <hr>
            <p>&copy; 2025 <strong style="color: #ffd700;">Car Showroom</strong>. Bảo lưu mọi quyền.</p>
            <p>
                <a href="#">Chính sách bảo mật</a> |
                <a href="#">Điều khoản sử dụng</a> |
                <a href="#">Sitemap</a>
            </p>
        </div>
    </div>
</footer>