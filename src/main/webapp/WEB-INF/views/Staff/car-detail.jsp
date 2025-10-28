<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 10/28/2025
  Time: 10:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${car.name} - Chi Tiết Xe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #0f0f0f;
            color: #e0e0e0;
            padding: 40px 0;
        }

        .car-detail-container {
            background: #1a1a1a;
            border-radius: 20px;
            padding: 40px;
            border: 1px solid #333;
            box-shadow: 0 10px 40px rgba(0,0,0,0.5);
        }

        .main-image {
            width: 100%;
            height: 500px;
            object-fit: cover;
            border-radius: 15px;
            border: 2px solid #333;
        }

        .thumbnail-gallery {
            display: flex;
            gap: 15px;
            margin-top: 15px;
        }

        .thumbnail {
            width: 120px;
            height: 90px;
            border-radius: 10px;
            overflow: hidden;
            cursor: pointer;
            border: 3px solid transparent;
            transition: all 0.3s;
        }

        .thumbnail.active, .thumbnail:hover {
            border-color: #ffd700;
            transform: translateY(-3px);
        }

        .thumbnail img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .status-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            background: #4caf50;
            color: white;
            padding: 8px 18px;
            border-radius: 30px;
            font-weight: 600;
        }

        .status-badge.unavailable {
            background: #dc3545;
        }

        .brand-name {
            color: #ffd700;
            text-transform: uppercase;
            font-weight: 600;
            margin-bottom: 5px;
        }

        .car-title {
            font-size: 2rem;
            font-weight: 700;
        }

        .price {
            color: #ffd700;
            font-size: 1.8rem;
            font-weight: 700;
            margin: 15px 0;
        }

        .specs-section {
            background: #0f0f0f;
            padding: 25px;
            border-radius: 15px;
            margin-top: 25px;
            border: 1px solid #333;
        }

        .specs-title {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .spec-item {
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #333;
            padding: 10px 0;
        }

        .description-section {
            margin-top: 25px;
            background: #0f0f0f;
            padding: 25px;
            border-radius: 15px;
            border: 1px solid #333;
        }

        .description-title {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 15px;
        }

        .description-text {
            color: #b0b0b0;
            line-height: 1.8;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="car-detail-container">
        <div class="row">
            <!-- Left -->
            <div class="col-lg-7 mb-4 position-relative">
                <img src="${car.imageUrl}" alt="${car.name}" class="main-image" id="mainImage">
                <span class="status-badge ${car.status == 'AVAILABLE' ? '' : 'unavailable'}">
                    ${car.status == 'AVAILABLE' ? 'Còn Hàng' : 'Hết Hàng'}
                </span>

                <c:if test="${not empty car.images and car.images.size() > 1}">
                    <div class="thumbnail-gallery mt-3">
                        <c:forEach var="img" items="${car.images}" varStatus="loop">
                            <div class="thumbnail ${loop.first ? 'active' : ''}" onclick="changeImage('${img.imageURL}', this)">
                                <img src="${img.imageURL}" alt="Thumbnail">
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${not empty car.description}">
                    <div class="description-section">
                        <h3 class="description-title"><i class="fas fa-file-alt"></i> Mô Tả Chi Tiết</h3>
                        <p class="description-text">${car.description}</p>
                    </div>
                </c:if>
            </div>

            <!-- Right -->
            <div class="col-lg-5">
                <div class="brand-name">${car.brandName}</div>
                <h1 class="car-title">${car.name}</h1>

                <p class="price"><fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/></p>

                <div class="specs-section">
                    <h4 class="specs-title"><i class="fas fa-info-circle"></i> Thông Số Kỹ Thuật</h4>
                    <div class="spec-item"><span>Năm Sản Xuất</span><span>${car.year}</span></div>
                    <div class="spec-item"><span>Màu Sắc</span><span>${car.color}</span></div>
                    <div class="spec-item"><span>Tình Trạng</span><span>${car.status == 'AVAILABLE' ? 'Còn Hàng' : 'Hết Hàng'}</span></div>
                    <div class="spec-item"><span>Số Lượng Có Sẵn</span><span>${car.stock} xe</span></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function changeImage(url, thumb) {
        document.getElementById("mainImage").src = url;
        document.querySelectorAll(".thumbnail").forEach(t => t.classList.remove("active"));
        thumb.classList.add("active");
    }
</script>

</body>
</html>
