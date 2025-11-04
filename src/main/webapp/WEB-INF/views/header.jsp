<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* ===================================
       NAVBAR STYLES
       =================================== */
    .navbar {
        background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
        padding: 15px 0;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
        border-bottom: 1px solid #333;
    }

    .navbar-brand {
        font-size: 1.5rem;
        font-weight: 700;
        color: #f8f9fa !important;
        text-transform: uppercase;
        letter-spacing: 1px;
        transition: color 0.3s ease;
        flex-shrink: 0;
    }

    .navbar-brand:hover {
        color: #ffd700 !important;
    }

    .navbar-brand i {
        color: #ffd700;
    }

    /* ===================================
       SEARCH BOX
       =================================== */
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
        transition: all 0.3s ease;
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

    /* ===================================
       CART BUTTON - FIXED TO NEVER SHRINK
       =================================== */
    .cart-btn-nav {
        position: relative;
        background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        color: #1a1a1a;
        border: none;
        border-radius: 25px;
        padding: 10px 20px;
        font-weight: 600;
        font-size: 0.95rem;
        margin-left: 15px;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        text-decoration: none;
        /* CRITICAL: Prevent shrinking */
        flex-shrink: 0;
        min-width: 130px;
        white-space: nowrap;
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
        min-width: 22px;
        height: 22px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 11px;
        font-weight: 700;
        padding: 0 5px;
    }

    /* ===================================
       NAV LINKS
       =================================== */
    .nav-link {
        color: #e0e0e0 !important;
        margin: 0 10px;
        font-weight: 500;
        font-size: 0.95rem;
        transition: all 0.3s ease;
    }

    .nav-link:hover {
        color: #ffd700 !important;
        text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
    }

    .nav-link i {
        margin-right: 5px;
    }

    /* ===================================
       DROPDOWN MENU
       =================================== */
    .dropdown-menu {
        background: #2a2a2a;
        border: 1px solid #444;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
    }

    .dropdown-item {
        color: #e0e0e0;
        transition: all 0.3s ease;
        font-size: 0.9rem;
        padding: 0.6rem 1.2rem;
    }

    .dropdown-item:hover {
        background: #333;
        color: #ffd700;
    }

    .dropdown-item i {
        margin-right: 8px;
        width: 16px;
        text-align: center;
    }

    .dropdown-divider {
        border-color: #444;
        margin: 0.5rem 0;
    }

    /* ===================================
       RIGHT SIDE MENU CONTAINER
       =================================== */
    .navbar-right-menu {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-shrink: 0;
    }

    /* ===================================
       RESPONSIVE
       =================================== */
    @media (max-width: 991px) {
        .cart-btn-nav {
            width: 100%;
            margin-left: 0;
            margin-top: 15px;
            min-width: unset;
        }

        .search-navbar {
            margin-bottom: 1rem;
        }

        .navbar-brand {
            font-size: 1.3rem;
        }

        .navbar-right-menu {
            flex-direction: column;
            width: 100%;
            gap: 0;
        }

        .navbar-right-menu .navbar-nav {
            width: 100%;
            margin: 0 !important;
        }
    }

    @media (max-width: 576px) {
        .navbar {
            padding: 10px 0;
        }

        .navbar-brand {
            font-size: 1.2rem;
        }

        .search-navbar input {
            font-size: 0.9rem;
            padding: 8px 16px;
        }

        .cart-btn-nav {
            font-size: 0.9rem;
            padding: 10px 18px;
        }
    }

    /* Extra small screens */
    @media (max-width: 380px) {
        .navbar-brand {
            font-size: 1.1rem;
        }

        .cart-btn-nav {
            font-size: 0.85rem;
            padding: 8px 16px;
            min-width: 110px;
        }
    }
</style>

<!-- ========================================
NAVIGATION BAR
======================================== -->
<nav class="navbar navbar-expand-lg navbar-dark" role="navigation">
    <div class="container-fluid px-4">
        <!-- Brand Logo -->
        <a class="navbar-brand"
           href="${pageContext.request.contextPath}/"
           title="Về trang chủ"
           aria-label="Car Showroom - Trang chủ">
            <i class="fas fa-car" aria-hidden="true"></i> CAR SHOWROOM
        </a>

        <!-- Desktop Navigation Links -->
        <ul class="navbar-nav me-auto mb-0 d-none d-lg-flex">
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/cars"
                   title="Xem danh sách xe"
                   aria-label="Xem danh sách xe">
                    <i class="fas fa-car" aria-hidden="true"></i> Xem Xe
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/promotions"
                   title="Xem khuyến mãi"
                   aria-label="Xem khuyến mãi">
                    <i class="fas fa-gift" aria-hidden="true"></i> Khuyến Mãi
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link"
                   href="${pageContext.request.contextPath}/blog"
                   title="Xem tin tức"
                   aria-label="Xem tin tức">
                    <i class="fas fa-newspaper" aria-hidden="true"></i> Tin Tức
                </a>
            </li>
        </ul>

        <!-- Mobile Toggle Button -->
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarNav"
                aria-controls="navbarNav"
                aria-expanded="false"
                aria-label="Toggle navigation menu"
                title="Menu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Collapsible Content -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Mobile Navigation Links -->
            <ul class="navbar-nav d-lg-none mb-3">
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/cars"
                       title="Xem danh sách xe"
                       aria-label="Xem danh sách xe">
                        <i class="fas fa-car" aria-hidden="true"></i> Xem Xe
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/promotions"
                       title="Xem khuyến mãi"
                       aria-label="Xem khuyến mãi">
                        <i class="fas fa-gift" aria-hidden="true"></i> Khuyến Mãi
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/blog"
                       title="Xem tin tức"
                       aria-label="Xem tin tức">
                        <i class="fas fa-newspaper" aria-hidden="true"></i> Tin Tức
                    </a>
                </li>
            </ul>

            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/cars"
                  method="get"
                  class="d-flex search-navbar mx-auto"
                  role="search">
                <input type="text"
                       name="search"
                       class="form-control"
                       placeholder="Tìm kiếm xe..."
                       aria-label="Tìm kiếm xe"
                       title="Nhập tên xe để tìm kiếm">
            </form>

            <!-- Right Side Menu - FIXED WITH WRAPPER -->
            <div class="navbar-right-menu mt-3 mt-lg-0">
                <!-- Cart Button - Will never shrink -->
                <a href="${pageContext.request.contextPath}/cart"
                   class="cart-btn-nav"
                   title="Xem giỏ hàng"
                   aria-label="Giỏ hàng">
                    <i class="fas fa-shopping-cart" aria-hidden="true"></i>
                    <span>Giỏ Hàng</span>
                    <span class="cart-badge-nav"
                          id="cartBadge"
                          aria-label="Số lượng sản phẩm trong giỏ">0</span>
                </a>

                <!-- User Menu -->
                <ul class="navbar-nav ms-lg-3 mb-0">
                    <c:choose>
                        <%-- User is logged in --%>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle"
                                   href="#"
                                   id="userDropdown"
                                   role="button"
                                   data-bs-toggle="dropdown"
                                   aria-expanded="false"
                                   title="Menu người dùng"
                                   aria-label="Menu người dùng ${sessionScope.user.name}">
                                    <i class="fas fa-user" aria-hidden="true"></i> ${sessionScope.user.name}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end"
                                    aria-labelledby="userDropdown">
                                    <li>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/profile"
                                           title="Xem hồ sơ cá nhân">
                                            <i class="fas fa-user-circle" aria-hidden="true"></i> Hồ Sơ
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/orders"
                                           title="Xem đơn hàng của tôi">
                                            <i class="fas fa-receipt" aria-hidden="true"></i> Đơn Hàng
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/my-promotions"
                                           title="Xem khuyến mãi của tôi">
                                            <i class="fas fa-gift" aria-hidden="true"></i> Khuyến Mãi Của Tôi
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/logout"
                                           title="Đăng xuất khỏi tài khoản">
                                            <i class="fas fa-sign-out-alt" aria-hidden="true"></i> Đăng Xuất
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </c:when>

                        <%-- User is not logged in --%>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link"
                                   href="${pageContext.request.contextPath}/login"
                                   title="Đăng nhập vào tài khoản"
                                   aria-label="Đăng nhập">
                                    <i class="fas fa-sign-in-alt" aria-hidden="true"></i> Đăng Nhập
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</nav>

<!-- ========================================
CART BADGE UPDATE SCRIPT
======================================== -->
<script>
    /**
     * Update cart badge count on page load
     * Shows/hides badge based on cart count
     */
    document.addEventListener('DOMContentLoaded', function() {
        // Get cart count from session
        const cartCount = ${sessionScope.cartCount != null ? sessionScope.cartCount : 0};
        const badge = document.getElementById('cartBadge');

        if (badge) {
            if (cartCount > 0) {
                // Show badge with count
                badge.textContent = cartCount;
                badge.style.display = 'flex';
            } else {
                // Hide badge when cart is empty
                badge.style.display = 'none';
            }
        }
    });
</script>
