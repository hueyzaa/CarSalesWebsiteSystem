<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* Navbar with Search & Cart */
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

    .search-navbar {
        position: relative;
        max-width: 600px;
        width: 100%;
    }

    .search-navbar input {
        padding: 8px 45px 8px 15px;
        border: 1px solid #444;
        border-radius: 25px;
        width: 100%;
        background: #2a2a2a;
        color: #fff;
    }

    .search-navbar input::placeholder {
        color: #888;
    }

    .search-navbar input:focus {
        outline: none;
        border-color: #ffd700;
        background: #333;
        box-shadow: 0 0 10px rgba(255, 215, 0, 0.2);
    }

    .search-navbar button {
        position: absolute;
        right: 5px;
        top: 50%;
        transform: translateY(-50%);
        background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        border: none;
        border-radius: 50%;
        width: 35px;
        height: 35px;
        color: #1a1a1a;
    }

    .search-navbar button:hover {
        background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
        box-shadow: 0 0 15px rgba(255, 215, 0, 0.5);
    }

    .cart-btn-nav {
        position: relative;
        background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        color: #1a1a1a;
        border: none;
        border-radius: 25px;
        padding: 8px 20px;
        font-weight: 600;
        margin-left: 15px;
        transition: all 0.3s;
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

    .nav-link {
        color: #e0e0e0 !important;
        margin: 0 10px;
        font-weight: 500;
        transition: all 0.3s;
    }

    .nav-link:hover {
        color: #ffd700 !important;
        text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
    }

    .dropdown-menu {
        background: #2a2a2a;
        border: 1px solid #444;
    }

    .dropdown-item {
        color: #e0e0e0;
        transition: all 0.3s;
    }

    .dropdown-item:hover {
        background: #333;
        color: #ffd700;
    }

    .dropdown-divider {
        border-color: #444;
    }
</style>

<!-- Navbar with Search & Cart -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid px-4">
        <!-- Brand - Sát bên trái -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <i class="fas fa-car"></i> Car Showroom
        </a>

        <!-- Menu items cạnh brand -->
        <ul class="navbar-nav me-auto mb-0 d-none d-lg-flex">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/cars">Xem Xe</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/about">Giới Thiệu</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/contact">Liên Hệ</a>
            </li>
        </ul>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Mobile Menu -->
            <ul class="navbar-nav d-lg-none mb-3">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cars">Xem Xe</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/about">Giới Thiệu</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/contact">Liên Hệ</a>
                </li>
            </ul>

            <!-- Search Box - Giữa -->
            <form action="${pageContext.request.contextPath}/cars" method="get" class="d-flex search-navbar mx-auto">
                <input type="text"
                       name="search"
                       class="form-control"
                       placeholder="Tìm kiếm xe...">
                <button type="submit">
                    <i class="fas fa-search"></i>
                </button>
            </form>

            <!-- Right side - Cart & User -->
            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <!-- Cart Button -->
                <a href="${pageContext.request.contextPath}/cart" class="cart-btn-nav">
                    <i class="fas fa-shopping-cart"></i> Giỏ Hàng
                    <span class="cart-badge-nav" id="cartBadge">0</span>
                </a>

                <!-- User Menu - Sát bên phải -->
                <ul class="navbar-nav ms-3 mb-0">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.name}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">

                                    <!-- Nếu là ADMIN -->
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">

                                        <!-- Quản lý hệ thống -->
                                        <li><h6 class="dropdown-header">Quản lý hệ thống</h6></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/cars">
                                            <i class="fas fa-car"></i> Quản lý xe
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/brands">
                                            <i class="fas fa-warehouse"></i> Quản lý hãng xe
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/users">
                                            <i class="fas fa-users"></i> Quản lý người dùng
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/staff">
                                            <i class="fas fa-user-tie"></i> Quản lý nhân viên
                                        </a></li>

                                        <li><hr class="dropdown-divider"></li>

                                        <!-- Kinh doanh -->
                                        <li><h6 class="dropdown-header">Kinh doanh</h6></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/orders">
                                            <i class="fas fa-file-invoice-dollar"></i> Quản lý đơn hàng
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/promotions">
                                            <i class="fas fa-percent"></i> Quản lý khuyến mãi
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/statistics">
                                            <i class="fas fa-chart-line"></i> Thống kê doanh thu
                                        </a></li>

                                        <li><hr class="dropdown-divider"></li>

                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                        </a></li>

                                    </c:if>

                                    <!-- Nếu là USER bình thường -->
                                    <c:if test="${sessionScope.user.role != 'ADMIN'}">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                            <i class="fas fa-receipt"></i> Đơn hàng
                                        </a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                            <i class="fas fa-user-circle"></i> Thông tin cá nhân
                                        </a></li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                        </a></li>
                                    </c:if>

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
    // Load cart count from session
    document.addEventListener('DOMContentLoaded', function() {
        const cartCount = ${sessionScope.cartCount != null ? sessionScope.cartCount : 0};
        const badge = document.getElementById('cartBadge');
        if (badge) {
            if (cartCount > 0) {
                badge.textContent = cartCount;
            } else {
                badge.style.display = 'none';
            }
        }
    });
</script>