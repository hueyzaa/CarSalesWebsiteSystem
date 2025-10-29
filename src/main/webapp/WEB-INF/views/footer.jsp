<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .footer {
        background: linear-gradient(135deg, #1a1a1a 0%, #0f0f0f 100%);
        color: #b0b0b0;
        padding: 40px 0 20px;
        margin-top: auto;
        border-top: 2px solid #ffd700;
    }

    .footer h5 {
        color: #ffd700;
        font-weight: 600;
        margin-bottom: 18px;
        font-size: 1rem;
    }

    .footer .brand-footer {
        color: #fff;
        font-size: 1.2rem;
        margin-bottom: 12px;
        font-weight: 600;
    }

    .footer .brand-footer i {
        color: #ffd700;
    }

    .footer p {
        font-size: 0.9rem;
        line-height: 1.7;
        color: #999;
    }

    .footer ul {
        list-style: none;
        padding: 0;
    }

    .footer ul li {
        margin-bottom: 8px;
    }

    .footer ul li a {
        color: #b0b0b0;
        text-decoration: none;
        transition: color 0.3s;
        font-size: 0.9rem;
    }

    .footer ul li a:hover {
        color: #ffd700;
    }

    .footer .contact-info i {
        color: #ffd700;
        width: 18px;
        margin-right: 8px;
    }

    .footer .contact-info p {
        margin-bottom: 10px;
    }

    .footer-bottom {
        text-align: center;
        padding-top: 25px;
        margin-top: 30px;
        border-top: 1px solid #333;
    }

    .footer-bottom p {
        margin: 5px 0;
        font-size: 0.85rem;
        color: #888;
    }

    .footer-bottom a {
        color: #888;
        text-decoration: none;
        transition: color 0.3s;
    }

    .footer-bottom a:hover {
        color: #ffd700;
    }

    .social-links {
        margin-top: 15px;
        display: flex;
        gap: 10px;
    }

    .social-links a {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #2a2a2a;
        transition: all 0.3s;
        border: 1px solid #333;
    }

    .social-links a img {
        width: 22px;
        height: 22px;
        object-fit: contain;
        transition: transform 0.3s;
    }

    .social-links a:hover {
        transform: translateY(-3px);
        border-color: #ffd700;
    }

    .social-links a.facebook:hover {
        background: #ffffff;
    }

    .social-links a.instagram:hover {
        background: linear-gradient(45deg, #f09433 0%, #e6683c 25%, #dc2743 50%, #cc2366 75%, #bc1888 100%);
    }

    .social-links a.twitter:hover {
        background: #000000;
    }

    .social-links a.youtube:hover {
        background: #ff0000;
    }

    .social-links a.tiktok:hover {
        background: #000000;
    }

    .social-links a.zalo:hover {
        background: #0068ff;
    }

    .social-links a:hover img {
        transform: scale(1.1);
    }

    @media (max-width: 768px) {
        .footer {
            padding: 30px 0 20px;
        }

        .footer h5 {
            font-size: 0.95rem;
        }

        .social-links {
            justify-content: center;
        }
    }
</style>

<footer class="footer">
    <div class="container">
        <div class="row mb-3">
            <!-- About Section -->
            <div class="col-lg-4 col-md-6 mb-4 mb-lg-0">
                <h5 class="brand-footer">
                    <i class="fas fa-car"></i> Car Showroom
                </h5>
                <p>Khám phá bộ sưu tập xe hơi đẳng cấp với giá tốt nhất. Chúng tôi cam kết mang đến trải nghiệm mua sắm xe hơi tuyệt vời nhất.</p>
                <div class="social-links">
                    <a href="https://facebook.com" target="_blank" title="Facebook" class="facebook">
                        <img src="${pageContext.request.contextPath}/images/facebook-logo.png" alt="Facebook">
                    </a>
                    <a href="https://instagram.com" target="_blank" title="Instagram" class="instagram">
                        <img src="${pageContext.request.contextPath}/images/ins-logo.png" alt="Instagram">
                    </a>
                    <a href="https://twitter.com" target="_blank" title="Twitter" class="twitter">
                        <img src="${pageContext.request.contextPath}/images/x-logo.png" alt="Twitter">
                    </a>
                    <a href="https://youtube.com" target="_blank" title="YouTube" class="youtube">
                        <img src="${pageContext.request.contextPath}/images/youtube-logo.png" alt="YouTube">
                    </a>
                    <a href="https://tiktok.com" target="_blank" title="TikTok" class="tiktok">
                        <img src="${pageContext.request.contextPath}/images/tiktok-logo.png" alt="TikTok">
                    </a>
                    <a href="https://zalo.me" target="_blank" title="Zalo" class="zalo">
                        <img src="${pageContext.request.contextPath}/images/zalo-logo.png" alt="Zalo">
                    </a>
                </div>
            </div>

            <!-- Quick Links -->
            <div class="col-lg-2 col-md-6 mb-4 mb-lg-0">
                <h5>Liên kết</h5>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/cars">Xem xe</a></li>
                    <li><a href="${pageContext.request.contextPath}/promotions">Khuyến mãi</a></li>
                    <li><a href="${pageContext.request.contextPath}/blog">Tin tức</a></li>
                </ul>
            </div>

            <!-- Customer Service -->
            <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
                <h5>Dịch vụ</h5>
                <ul>
                    <li><a href="#">Chính sách bảo mật</a></li>
                    <li><a href="#">Điều khoản dịch vụ</a></li>
                    <li><a href="#">Chính sách đổi trả</a></li>
                    <li><a href="#">Hỗ trợ khách hàng</a></li>
                </ul>
            </div>

            <!-- Contact Info -->
            <div class="col-lg-3 col-md-6">
                <h5>Liên hệ</h5>
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
                </div>
            </div>
        </div>

        <!-- Footer Bottom -->
        <div class="footer-bottom">
            <p>&copy; 2025 <strong style="color: #ffd700;">Car Showroom</strong>. Bảo lưu mọi quyền.</p>
        </div>
    </div>
</footer>
