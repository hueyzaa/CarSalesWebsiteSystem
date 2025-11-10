<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 12:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${car.name} - Chi tiết</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        :root{ --bg:#0f0f0f; --panel:#151515; --line:#2a2a2a; --gold:#ffd700; --muted:#b9b9b9; }
        body{background:var(--bg); color:#eaeaea}

        /* khung trang */
        main.container{max-width:1200px; margin:24px auto}

        /* ------- LEFT: image & thumbs ------- */
        .main-image-container{
            position:relative; background:#101010; border:1px solid var(--line);
            border-radius:14px; padding:8px
        }
        .main-image{
            width:100%; height:480px; object-fit:cover; border-radius:10px; display:block
        }
        .status-badge{
            position:absolute; top:10px; left:10px; font-size:.9rem;
            background:rgba(0,0,0,.55); border:1px solid rgba(255,215,0,.45);
            border-radius:999px; padding:6px 10px; color:#fff; font-weight:700
        }
        .status-badge.unavailable{ border-color:#555; background:rgba(120,120,120,.4) }

        .thumbnail-gallery{
            display:flex; gap:10px; margin-top:10px; overflow-x:auto; padding-bottom:4px
        }
        .thumbnail{
            flex:0 0 auto; width:100px; height:74px; border-radius:10px; overflow:hidden;
            border:2px solid transparent; cursor:pointer; transition:border-color .2s
        }
        .thumbnail img{ width:100%; height:100%; object-fit:cover; display:block }
        .thumbnail.active, .thumbnail:hover{ border-color:rgba(255,215,0,.7) }

        /* ------- RIGHT: info ------- */
        .brand-name{ color:var(--gold); font-weight:700; letter-spacing:.3px; margin-bottom:4px }
        .car-title{ margin:0 0 10px; font-weight:800; line-height:1.2 }

        .price-section .price-label{opacity:.9}
        .price{ font-size:1.6rem; color:var(--gold); font-weight:800; margin:6px 0 14px }

        .specs-section{ border:1px solid var(--line); background:var(--panel); border-radius:12px; padding:12px 14px; margin-bottom:16px }
        .specs-title{ font-size:1.05rem; margin:0 0 8px }
        .spec-item{ display:flex; justify-content:space-between; gap:10px; padding:8px 0; border-bottom:1px dashed #252525 }
        .spec-item:last-child{ border-bottom:none }
        .spec-label{ color:#e5e5e5 }
        .spec-value{ color:#bfbfbf }

        /* description */
        .description-section{ margin-top:14px }
        .description-title{ font-size:1.05rem; margin-bottom:8px }
        .description-text{ color:#d0d0d0; border:1px solid var(--line); background:#0f0f0f; border-radius:12px; padding:14px }

        /* responsive */
        @media (max-width: 991.98px){
            .main-image{ height:360px }
        }

        /* actions (Back / Update) */
        .actions-section{
            border:1px solid var(--line);
            background:#121212;
            border-radius:12px;
            padding:14px;
            margin-top:16px
        }
        .actions-title{ font-size:1.05rem; margin:0 0 10px }
        .actions{
            display:flex; gap:10px; flex-wrap:wrap
        }
        .btn-action{
            display:inline-flex; align-items:center; gap:8px; font-weight:700;
            border:none; border-radius:999px; padding:10px 16px; text-decoration:none
        }
        .btn-back{
            background:#1f1f1f; color:#fff; border:1px solid var(--line)
        }
        .btn-update{
            background:linear-gradient(135deg,#ffd700 0%,#bfa100 100%); color:#000
        }
        .btn-action:focus{ outline:2px solid rgba(255,215,0,.35); outline-offset:2px }

    </style>

</head>
<body>

<main class="container">
    <!-- Messages -->
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle" aria-hidden="true"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle" aria-hidden="true"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <!-- Car Detail -->
    <article class="car-detail-container">
        <div class="row">
            <!-- Left: Images & Description -->
            <div class="col-lg-7 mb-4">
                <!-- Main Image -->
                <div class="main-image-container">
                    <c:choose>
                        <c:when test="${not empty car.images}">
                            <c:set var="mainImage" value="" />
                            <c:forEach var="img" items="${car.images}">
                                <c:if test="${img.mainImage}">
                                    <c:set var="mainImage" value="${img.imageURL}" />
                                </c:if>
                            </c:forEach>
                            <c:if test="${empty mainImage and not empty car.images}">
                                <c:set var="mainImage" value="${car.images[0].imageURL}" />
                            </c:if>
                            <img src="${mainImage}"
                                 alt="Hình ảnh xe ${car.name}"
                                 class="main-image"
                                 id="mainImage"
                                 loading="eager">
                        </c:when>
                        <c:when test="${not empty car.imageUrl}">
                            <img src="${car.imageUrl}"
                                 alt="Hình ảnh xe ${car.name}"
                                 class="main-image"
                                 id="mainImage"
                                 loading="eager">
                        </c:when>
                        <c:otherwise>
                            <img src="https://via.placeholder.com/800x500?text=No+Image"
                                 alt="Không có hình ảnh"
                                 class="main-image"
                                 id="mainImage"
                                 loading="eager">
                        </c:otherwise>
                    </c:choose>

                    <span class="status-badge ${car.status == 'AVAILABLE' ? '' : 'unavailable'}"
                          role="status">
                        <c:choose>
                            <c:when test="${car.status == 'AVAILABLE'}">
                                <i class="fas fa-check-circle" aria-hidden="true"></i> Còn Hàng
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-times-circle" aria-hidden="true"></i> Hết Hàng
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <!-- Thumbnails -->
                <c:if test="${not empty car.images and car.images.size() > 1}">
                    <div class="thumbnail-gallery" role="list">
                        <c:forEach var="img" items="${car.images}" varStatus="status">
                            <div class="thumbnail ${status.first ? 'active' : ''}"
                                 onclick="changeImage('${img.imageURL}', this)"
                                 role="button"
                                 tabindex="0"
                                 aria-label="Xem hình ${status.index + 1}"
                                 title="Click để phóng to">
                                <img src="${img.imageURL}"
                                     alt="Hình ${status.index + 1}"
                                     loading="lazy">
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Description -->
                <c:if test="${not empty car.description}">
                    <section class="description-section">
                        <h3 class="description-title">
                            <i class="fas fa-file-alt" aria-hidden="true"></i> Mô Tả Chi Tiết
                        </h3>
                        <p class="description-text">${car.description}</p>
                    </section>
                </c:if>
            </div>

            <!-- Right: Info & Purchase -->
            <div class="col-lg-5">
                <!-- Brand & Title -->
                <div class="brand-name">
                    <i class="fas fa-award" aria-hidden="true"></i> ${car.brandName}
                </div>
                <h1 class="car-title">${car.name}</h1>

                <!-- Price -->
                <section class="price-section">
                    <div class="price-label">Giá Xe</div>
                    <p class="price">
                        <fmt:formatNumber value="${car.price}" pattern="#,##0" /> ₫
                    </p>
                </section>

                <!-- Specs -->
                <section class="specs-section">
                    <h3 class="specs-title">
                        <i class="fas fa-info-circle" aria-hidden="true"></i> Thông Số Kỹ Thuật
                    </h3>
                    <c:if test="${not empty car.year}">
                        <div class="spec-item">
                            <span class="spec-label">
                                <i class="fas fa-calendar" aria-hidden="true"></i> Năm Sản Xuất
                            </span>
                            <span class="spec-value">${car.year}</span>
                        </div>
                    </c:if>
                    <c:if test="${not empty car.color}">
                        <div class="spec-item">
                            <span class="spec-label">
                                <i class="fas fa-palette" aria-hidden="true"></i> Màu Sắc
                            </span>
                            <span class="spec-value">${car.color}</span>
                        </div>
                    </c:if>
                    <div class="spec-item">
                        <span class="spec-label">
                            <i class="fas fa-box" aria-hidden="true"></i> Tình Trạng
                        </span>
                        <span class="spec-value">${car.status == 'AVAILABLE' ? 'Còn Hàng' : 'Hết Hàng'}</span>
                    </div>
                    <c:if test="${car.stock > 0}">
                        <div class="spec-item">
                            <span class="spec-label">
                                <i class="fas fa-warehouse" aria-hidden="true"></i> Số Lượng
                            </span>
                            <span class="spec-value">${car.stock} xe</span>
                        </div>
                    </c:if>
                </section>
                <!-- Actions: Back & Update -->
                <section class="actions-section" aria-label="Thao tác nhanh">
                    <h3 class="actions-title"><i class="fas fa-wrench" aria-hidden="true"></i> Thao Tác</h3>
                    <div class="actions">
                        <!-- Quay lại -->
                        <a class="btn-action btn-back"
                                href="${pageContext.request.contextPath}/staff/dashboard"
                                title="Quay lại trang trước"
                                aria-label="Quay lại">
                            <i class="fas fa-arrow-left" aria-hidden="true"></i> Quay lại
                        </a>

                        <!-- Cập nhật (đi tới trang edit cho staff) -->
                        <a class="btn-action btn-update"
                           href="${pageContext.request.contextPath}/staff/update-car?id=${car.id}"
                           title="Cập nhật thông tin xe"
                           aria-label="Cập nhật">
                            <i class="fas fa-pen-to-square" aria-hidden="true"></i> Cập nhật
                        </a>
                    </div>
                </section>
            </div>
        </div>
    </article>
</main>
</body>
</html>