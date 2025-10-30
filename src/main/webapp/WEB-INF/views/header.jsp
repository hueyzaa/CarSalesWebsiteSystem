<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* Navbar */
    .navbar {
        background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
        padding: 15px 0;
        box-shadow: 0 4px 20px rgba(0,0,0,0.5);
        border-bottom: 1px solid #333;
    }

    .navbar-brand {
        font-size: 1.5rem;
        font-weight: bold;
        color: #f8f9fa !important;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .navbar-brand i {
        color: #ffd700;
    }

    /* Search Box */
    .search-navbar {
        position: relative;
        max-width: 600px;
        width: 100%;
    }

    .search-navbar input {
        padding: 10px 20px;
        border: 2px solid #555;
        border-radius: 25px;
        width: 100%;
        background: #3a3a3a;
        color: #f0f0f0;
        font-size: 0.95rem;
        font-weight: 400;
        transition: all 0.3s;
    }

    .search-navbar input::placeholder {
        color: #bbb;
        font-weight: 300;
    }

    .search-navbar input:focus {
        outline: none;
        border-color: #0d6efd;
        background: #2a2a2a;
        box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.25);
        color: #ffffff;
    }

    /* Cart Button */
    .cart-btn-nav {
        position: relative;
        background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        color: #1a1a1a;
        border: none;
        border-radius: 25px;
        padding: 8px 16px;
        font-weight: 600;
        font-size: 0.9rem;
        margin-left: 15px;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }

    .cart-btn-nav:hover {
        background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        color: #1a1a1a;
        text-decoration: none;
    }

    .cart-badge-nav {
        position: absolute;
        top: -8px;
        right: -8px;
        background: #dc3545;
        color: white;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 11px;
        font-weight: bold;
    }

    /* Nav Links */
    .nav-link {
        color: #e0e0e0 !important;
        margin: 0 10px;
        font-weight: 500;
        font-size: 0.95rem;
        transition: all 0.3s;
    }

    .nav-link:hover {
        color: #ffd700 !important;
        text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
    }

    /* Dropdown */
    .dropdown-menu {
        background: #2a2a2a;
        border: 1px solid #444;
    }

    .dropdown-item {
        color: #e0e0e0;
        transition: all 0.3s;
        font-size: 0.9rem;
    }

    .dropdown-item:hover {
        background: #333;
        color: #ffd700;
    }

    .dropdown-divider {
        border-color: #444;
    }

    /* Responsive */
    @media (max-width: 991px) {
        .cart-btn-nav {
            width: 100%;
            justify-content: center;
            margin-left: 0;
            margin-top: 10px;
        }
    }
</style>

<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <i class="fas fa-car"></i> Car Showroom
        </a>

        <ul class="navbar-nav me-auto mb-0 d-none d-lg-flex">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                    <i class="fas fa-car"></i> Xem Xe
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/promotions">
                    <i class="fas fa-gift"></i> Khuyến Mãi
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/blog">
                    <i class="fas fa-newspaper"></i> Tin Tức
                </a>
            </li>
        </ul>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav d-lg-none mb-3">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">
                        <i class="fas fa-car"></i> Xem Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/promotions">
                        <i class="fas fa-gift"></i> Khuyến Mãi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/blog">
                        <i class="fas fa-newspaper"></i> Tin Tức
                    </a>
                </li>
            </ul>

            <form action="${pageContext.request.contextPath}/cars" method="get" class="d-flex search-navbar mx-auto">
                <input type="text"
                       name="search"
                       class="form-control"
                       placeholder="Tìm kiếm xe...">
            </form>

            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <a href="${pageContext.request.contextPath}/cart" class="cart-btn-nav">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Giỏ Hàng</span>
                    <span class="cart-badge-nav" id="cartBadge">0</span>
                </a>

                <ul class="navbar-nav ms-3 mb-0">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.name}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                            <i class="fas fa-user-circle"></i> Hồ Sơ
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                            <i class="fas fa-receipt"></i> Đơn Hàng
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/my-promotions">
                                            <i class="fas fa-gift"></i> Khuyến Mãi Của Tôi
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                            <i class="fas fa-sign-out-alt"></i> Đăng Xuất
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-sign-in-alt"></i> Đăng Nhập
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</nav>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const cartCount = ${sessionScope.cartCount != null ? sessionScope.cartCount : 0};
        const badge = document.getElementById('cartBadge');
        if (badge && cartCount > 0) {
            badge.textContent = cartCount;
        } else if (badge) {
            badge.style.display = 'none';
        }
    });
</script>
