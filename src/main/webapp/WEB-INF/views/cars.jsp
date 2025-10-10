<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Xe - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0f0f0f;
            color: #e0e0e0;
        }

        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 40px 0;
            margin-bottom: 30px;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #f8f9fa;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .page-header .subtitle {
            color: #888;
            font-size: 1.1rem;
        }

        /* Filter Section */
        .filter-section {
            background: #1a1a1a;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 30px;
            border: 1px solid #333;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
        }

        .filter-section h5 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .filter-section .form-label {
            color: #b0b0b0;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .filter-section .form-control,
        .filter-section .form-select {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 10px 15px;
            border-radius: 8px;
        }

        .filter-section .form-control:focus,
        .filter-section .form-select:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            color: #e0e0e0;
            box-shadow: 0 0 0 0.2rem rgba(255, 215, 0, 0.25);
        }

        .filter-section .form-select option {
            background: #1a1a1a;
            color: #e0e0e0;
        }

        .btn-filter {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 10px 30px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-filter:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }

        .btn-reset {
            background: #333;
            border: 1px solid #555;
            color: #e0e0e0;
            font-weight: 600;
            padding: 10px 30px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-reset:hover {
            background: #444;
            border-color: #666;
            color: #fff;
        }

        /* Results Header */
        .results-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding: 20px;
            background: #1a1a1a;
            border-radius: 10px;
            border: 1px solid #333;
        }

        .results-count {
            color: #ffd700;
            font-size: 1.1rem;
            font-weight: 600;
        }

        .sort-dropdown {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .sort-dropdown label {
            color: #b0b0b0;
            margin: 0;
            white-space: nowrap;
        }

        .sort-dropdown select {
            background: #0f0f0f;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 8px 15px;
            border-radius: 8px;
            min-width: 200px;
        }

        /* Car Cards */
        .car-card {
            transition: transform 0.3s, box-shadow 0.3s;
            height: 100%;
            border: none;
            border-radius: 15px;
            overflow: hidden;
            background: #1a1a1a;
            border: 1px solid #333;
        }

        .car-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.3);
            border-color: #ffd700;
        }

        .car-card-img-wrapper {
            position: relative;
            overflow: hidden;
            height: 240px;
        }

        .car-card img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s;
        }

        .car-card:hover img {
            transform: scale(1.1);
        }

        .car-status-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
            color: white;
            box-shadow: 0 3px 10px rgba(0,0,0,0.3);
        }

        .car-status-badge.unavailable {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
        }

        .car-card .card-body {
            padding: 20px;
            background: #1a1a1a;
        }

        .car-brand {
            color: #ffd700;
            font-size: 0.9rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .car-card .card-title {
            color: #f8f9fa;
            font-size: 1.2rem;
            font-weight: 700;
            margin-bottom: 10px;
            min-height: 50px;
        }

        .car-specs {
            display: flex;
            gap: 15px;
            margin: 15px 0;
            padding: 10px 0;
            border-top: 1px solid #333;
            border-bottom: 1px solid #333;
        }

        .car-spec-item {
            display: flex;
            align-items: center;
            gap: 5px;
            color: #888;
            font-size: 0.9rem;
        }

        .car-spec-item i {
            color: #ffd700;
        }

        .car-price {
            color: #ffd700;
            font-size: 1.5rem;
            font-weight: 700;
            margin: 15px 0;
        }

        .car-card .btn-view {
            width: 100%;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 10px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .car-card .btn-view:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }

        /* No Results */
        .no-results {
            text-align: center;
            padding: 80px 20px;
            background: #1a1a1a;
            border-radius: 15px;
            border: 1px solid #333;
        }

        .no-results i {
            font-size: 5rem;
            color: #555;
            margin-bottom: 20px;
        }

        .no-results h3 {
            color: #888;
            margin-bottom: 10px;
        }

        .no-results p {
            color: #666;
        }

        /* Active Filter Badge */
        .active-filters {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        .filter-badge {
            background: #2a2a2a;
            color: #ffd700;
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.9rem;
            display: flex;
            align-items: center;
            gap: 8px;
            border: 1px solid #333;
        }

        .filter-badge i {
            cursor: pointer;
        }

        footer {
            margin-top: auto;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-car"></i> Danh Sách Xe</h1>
        <p class="subtitle">Khám phá bộ sưu tập xe hơi đa dạng với mức giá phù hợp</p>
    </div>
</div>

<!-- Main Content -->
<div class="container mb-5">
    <!-- Filter Section -->
    <div class="filter-section">
        <h5><i class="fas fa-filter"></i> Bộ Lọc Tìm Kiếm</h5>
        <form action="${pageContext.request.contextPath}/cars" method="get" id="filterForm">
            <div class="row g-3">
                <!-- Search -->
                <div class="col-md-4">
                    <label class="form-label">Tìm kiếm</label>
                    <input type="text"
                           class="form-control"
                           name="search"
                           placeholder="Nhập tên xe, hãng xe..."
                           value="${searchKeyword}">
                </div>

                <!-- Brand Filter -->
                <div class="col-md-3">
                    <label class="form-label">Hãng xe</label>
                    <select class="form-select" name="brand">
                        <option value="">Tất cả hãng</option>
                        <c:forEach var="brand" items="${brandList}">
                            <option value="${brand.brandId}"
                                ${selectedBrand == brand.brandId ? 'selected' : ''}>
                                    ${brand.brandName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Min Price -->
                <div class="col-md-2">
                    <label class="form-label">Giá từ (₫)</label>
                    <input type="number"
                           class="form-control"
                           name="minPrice"
                           placeholder="0"
                           value="${minPrice}"
                           min="0"
                           step="1000000">
                </div>

                <!-- Max Price -->
                <div class="col-md-2">
                    <label class="form-label">Giá đến (₫)</label>
                    <input type="number"
                           class="form-control"
                           name="maxPrice"
                           placeholder="9999999999"
                           value="${maxPrice}"
                           min="0"
                           step="1000000">
                </div>

                <!-- Buttons -->
                <div class="col-md-12">
                    <button type="submit" class="btn btn-filter">
                        <i class="fas fa-search"></i> Tìm Kiếm
                    </button>
                    <a href="${pageContext.request.contextPath}/cars" class="btn btn-reset ms-2">
                        <i class="fas fa-redo"></i> Đặt Lại
                    </a>
                </div>
            </div>
        </form>
    </div>

    <!-- Active Filters Display -->
    <c:if test="${not empty searchKeyword or not empty selectedBrand or not empty minPrice or not empty maxPrice}">
        <div class="active-filters">
            <c:if test="${not empty searchKeyword}">
                <span class="filter-badge">
                    Tìm kiếm: "${searchKeyword}"
                    <i class="fas fa-times" onclick="removeFilter('search')"></i>
                </span>
            </c:if>
            <c:if test="${not empty selectedBrand}">
                <span class="filter-badge">
                    Hãng:
                    <c:forEach var="brand" items="${brandList}">
                        <c:if test="${brand.brandId == selectedBrand}">${brand.brandName}</c:if>
                    </c:forEach>
                    <i class="fas fa-times" onclick="removeFilter('brand')"></i>
                </span>
            </c:if>
            <c:if test="${not empty minPrice or not empty maxPrice}">
                <span class="filter-badge">
                    Giá: <fmt:formatNumber value="${minPrice != null ? minPrice : 0}" type="number"/> -
                    <fmt:formatNumber value="${maxPrice != null ? maxPrice : 9999999999}" type="number"/> ₫
                    <i class="fas fa-times" onclick="removeFilter('price')"></i>
                </span>
            </c:if>
        </div>
    </c:if>

    <!-- Results Header -->
    <div class="results-header">
        <div class="results-count">
            <i class="fas fa-car"></i>
            Tìm thấy <strong>${totalCars}</strong> xe
        </div>
        <div class="sort-dropdown">
            <label>Sắp xếp:</label>
            <select class="form-select" onchange="applySort(this.value)">
                <option value="">Mặc định</option>
                <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Mới nhất</option>
                <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Giá thấp đến cao</option>
                <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Giá cao đến thấp</option>
                <option value="name_asc" ${sortBy == 'name_asc' ? 'selected' : ''}>Tên A-Z</option>
                <option value="name_desc" ${sortBy == 'name_desc' ? 'selected' : ''}>Tên Z-A</option>
                <option value="year_desc" ${sortBy == 'year_desc' ? 'selected' : ''}>Năm mới nhất</option>
            </select>
        </div>
    </div>

    <!-- Car List -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty carList}">
                <c:forEach var="car" items="${carList}">
                    <div class="col-lg-3 col-md-4 col-sm-6">
                        <div class="card car-card">
                            <div class="car-card-img-wrapper">
                                <!-- Car Image -->
                                <c:set var="mainImage" value=""/>
                                <c:forEach var="img" items="${car.images}">
                                    <c:if test="${img.mainImage}">
                                        <c:set var="mainImage" value="${img.imageURL}"/>
                                    </c:if>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${not empty mainImage}">
                                        <img src="${mainImage}" class="card-img-top" alt="${car.name}">
                                    </c:when>
                                    <c:when test="${not empty car.imageUrl}">
                                        <img src="${car.imageUrl}" class="card-img-top" alt="${car.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/300x240?text=No+Image" class="card-img-top" alt="${car.name}">
                                    </c:otherwise>
                                </c:choose>

                                <!-- Status Badge -->
                                <span class="car-status-badge ${car.status == 'AVAILABLE' ? '' : 'unavailable'}">
                                        ${car.status == 'AVAILABLE' ? 'Còn Hàng' : 'Hết Hàng'}
                                </span>
                            </div>

                            <div class="card-body">
                                <!-- Brand -->
                                <div class="car-brand">
                                    <i class="fas fa-award"></i> ${car.brandName}
                                </div>

                                <!-- Car Name -->
                                <h5 class="card-title">${car.name}</h5>

                                <!-- Specs -->
                                <div class="car-specs">
                                    <c:if test="${not empty car.year and car.year > 0}">
                                        <div class="car-spec-item">
                                            <i class="fas fa-calendar"></i>
                                            <span>${car.year}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty car.color}">
                                        <div class="car-spec-item">
                                            <i class="fas fa-palette"></i>
                                            <span>${car.color}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${car.stock > 0}">
                                        <div class="car-spec-item">
                                            <i class="fas fa-warehouse"></i>
                                            <span>${car.stock}</span>
                                        </div>
                                    </c:if>
                                </div>

                                <!-- Price -->
                                <div class="car-price">
                                    <fmt:formatNumber value="${car.price}" type="currency" currencySymbol="₫"/>
                                </div>

                                <!-- View Button -->
                                <a href="${pageContext.request.contextPath}/car-detail?id=${car.id}"
                                   class="btn btn-view">
                                    <i class="fas fa-eye"></i> Xem Chi Tiết
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="no-results">
                        <i class="fas fa-car-crash"></i>
                        <h3>Không tìm thấy xe nào</h3>
                        <p>Vui lòng thử lại với các tiêu chí khác</p>
                        <a href="${pageContext.request.contextPath}/cars" class="btn btn-filter mt-3">
                            <i class="fas fa-redo"></i> Xem Tất Cả Xe
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Apply sorting
    function applySort(sortValue) {
        const url = new URL(window.location.href);
        if (sortValue) {
            url.searchParams.set('sort', sortValue);
        } else {
            url.searchParams.delete('sort');
        }
        window.location.href = url.toString();
    }

    // Remove individual filter
    function removeFilter(filterType) {
        const url = new URL(window.location.href);

        switch(filterType) {
            case 'search':
                url.searchParams.delete('search');
                url.searchParams.delete('keyword');
                break;
            case 'brand':
                url.searchParams.delete('brand');
                break;
            case 'price':
                url.searchParams.delete('minPrice');
                url.searchParams.delete('maxPrice');
                break;
        }

        window.location.href = url.toString();
    }

    // Validate price range
    document.getElementById('filterForm').addEventListener('submit', function(e) {
        const minPrice = parseFloat(document.querySelector('input[name="minPrice"]').value) || 0;
        const maxPrice = parseFloat(document.querySelector('input[name="maxPrice"]').value) || Infinity;

        if (minPrice > maxPrice) {
            e.preventDefault();
            alert('Giá từ phải nhỏ hơn hoặc bằng Giá đến!');
            return false;
        }
    });
</script>
</body>
</html>