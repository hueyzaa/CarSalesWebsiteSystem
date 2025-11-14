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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            background-color: #0a0a0a;
            color: #e0e0e0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .page-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            padding: 2rem 0;
            margin-bottom: 2rem;
            border-bottom: 2px solid #ffd700;
        }

        .page-header h1 {
            color: #f8f9fa;
            font-weight: 700;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .page-header .subtitle {
            color: #888;
            font-size: 1rem;
        }

        .main-container {
            display: flex;
            gap: 2rem;
            margin-bottom: 3rem;
        }

        /* Filter Sidebar */
        .filter-sidebar {
            width: 280px;
            flex-shrink: 0;
            background: #1a1a1a;
            border-radius: 12px;
            padding: 1.5rem;
            border: 1px solid #333;
            position: sticky;
            top: 20px;
            height: fit-content;
            max-height: calc(100vh - 100px);
            overflow-y: auto;
        }

        .filter-sidebar::-webkit-scrollbar {
            width: 6px;
        }

        .filter-sidebar::-webkit-scrollbar-track {
            background: #0a0a0a;
            border-radius: 3px;
        }

        .filter-sidebar::-webkit-scrollbar-thumb {
            background: #ffd700;
            border-radius: 3px;
        }

        .filter-sidebar h5 {
            color: #ffd700;
            font-weight: 700;
            margin-bottom: 1.5rem;
            padding-bottom: 0.75rem;
            border-bottom: 2px solid #333;
        }

        .filter-group {
            margin-bottom: 1.5rem;
        }

        .filter-group label {
            color: #b0b0b0;
            font-weight: 600;
            font-size: 0.9rem;
            margin-bottom: 0.5rem;
            display: block;
        }

        .filter-group .form-control,
        .filter-group .form-select {
            background: #0a0a0a;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 0.6rem 0.8rem;
            border-radius: 6px;
        }

        .filter-group .form-control:focus,
        .filter-group .form-select:focus {
            background: #1a1a1a;
            border-color: #ffd700;
            color: #e0e0e0;
            box-shadow: 0 0 0 0.15rem rgba(255, 215, 0, 0.15);
        }

        .filter-group .form-select option {
            background: #1a1a1a;
            color: #e0e0e0;
        }

        /* Price Slider */
        .price-slider-container {
            padding: 1rem 0;
        }

        .price-values {
            display: flex;
            justify-content: space-between;
            margin-bottom: 1.5rem;
            gap: 0.5rem;
        }

        .price-value {
            background: #0a0a0a;
            border: 1px solid #333;
            color: #ffd700;
            padding: 0.6rem;
            border-radius: 6px;
            font-size: 0.85rem;
            font-weight: 600;
            text-align: center;
            flex: 1;
        }

        .price-value span {
            display: block;
            color: #888;
            font-size: 0.7rem;
            margin-bottom: 0.25rem;
            text-transform: uppercase;
        }

        /* noUiSlider Custom */
        .noUi-target {
            background: #333;
            border: none;
            box-shadow: none;
            height: 6px;
            border-radius: 3px;
        }

        .noUi-connect {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
        }

        .noUi-handle {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            background: #ffd700;
            border: 3px solid #1a1a1a;
            box-shadow: 0 2px 8px rgba(255, 215, 0, 0.4);
            cursor: pointer;
            top: -7px;
        }

        .noUi-handle:before,
        .noUi-handle:after {
            display: none;
        }

        .noUi-handle:hover {
            background: #ffed4e;
            transform: scale(1.1);
        }

        .noUi-handle:active {
            transform: scale(1.2);
        }

        /* Price Suggestions */
        .price-suggestions {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 0.5rem;
            margin-top: 1rem;
        }

        .price-suggestion-btn {
            background: #0a0a0a;
            border: 1px solid #333;
            color: #b0b0b0;
            padding: 0.5rem;
            border-radius: 6px;
            font-size: 0.75rem;
            cursor: pointer;
            transition: all 0.3s;
        }

        .price-suggestion-btn:hover {
            background: #1a1a1a;
            border-color: #ffd700;
            color: #ffd700;
        }

        .btn-filter {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 0.65rem;
            width: 100%;
            border-radius: 6px;
            transition: all 0.3s;
        }

        .btn-filter:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
        }

        .btn-reset {
            background: #2a2a2a;
            border: 1px solid #444;
            color: #b0b0b0;
            font-weight: 600;
            padding: 0.65rem;
            width: 100%;
            border-radius: 6px;
            margin-top: 0.5rem;
            text-align: center;
            display: block;
            text-decoration: none;
        }

        .btn-reset:hover {
            background: #333;
            color: #e0e0e0;
        }

        /* Content Area */
        .content-area {
            flex: 1;
        }

        .results-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding: 1rem 1.25rem;
            background: #1a1a1a;
            border-radius: 10px;
            border: 1px solid #333;
        }

        .results-count {
            color: #ffd700;
            font-weight: 600;
        }

        .sort-dropdown {
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .sort-dropdown label {
            color: #b0b0b0;
            margin: 0;
        }

        .sort-dropdown select {
            background: #0a0a0a;
            border: 1px solid #333;
            color: #e0e0e0;
            padding: 0.5rem 0.75rem;
            border-radius: 6px;
        }

        /* Active Filters */
        .active-filters {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
            margin-bottom: 1rem;
        }

        .filter-badge {
            background: #2a2a2a;
            color: #ffd700;
            padding: 0.4rem 0.8rem;
            border-radius: 15px;
            font-size: 0.85rem;
            border: 1px solid #333;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .filter-badge i {
            cursor: pointer;
        }

        .filter-badge i:hover {
            color: #ff6b6b;
        }

        /* Car Grid */
        .car-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 1.5rem;
        }

        .car-card {
            background: #1a1a1a;
            border: 1px solid #2a2a2a;
            border-radius: 12px;
            overflow: hidden;
            transition: all 0.3s;
        }

        .car-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 30px rgba(255, 215, 0, 0.25);
            border-color: #ffd700;
        }

        .car-card-img-wrapper {
            position: relative;
            height: 200px;
            background: #0a0a0a;
            overflow: hidden;
        }

        .car-card img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.4s;
        }

        .car-card:hover img {
            transform: scale(1.08);
        }

        .car-status-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            padding: 0.35rem 0.8rem;
            border-radius: 15px;
            font-size: 0.75rem;
            font-weight: 600;
            background: rgba(76, 175, 80, 0.95);
            color: white;
        }

        .car-status-badge.unavailable {
            background: rgba(220, 53, 69, 0.95);
        }

        .car-card .card-body {
            padding: 1.25rem;
        }

        .car-brand {
            color: #ffd700;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
            margin-bottom: 0.5rem;
        }

        .car-card .card-title {
            color: #f8f9fa;
            font-size: 1.1rem;
            font-weight: 700;
            margin-bottom: 0.75rem;
            min-height: 48px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        .car-specs {
            display: flex;
            gap: 0.75rem;
            margin: 0.75rem 0;
            padding: 0.75rem 0;
            border-top: 1px solid #2a2a2a;
            border-bottom: 1px solid #2a2a2a;
        }

        .car-spec-item {
            display: flex;
            align-items: center;
            gap: 0.35rem;
            color: #888;
            font-size: 0.85rem;
        }

        .car-spec-item i {
            color: #ffd700;
        }

        .car-price {
            color: #ffd700;
            font-size: 1.35rem;
            font-weight: 700;
            margin: 0.75rem 0;
        }

        .btn-view {
            width: 100%;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #1a1a1a;
            font-weight: 600;
            padding: 0.65rem;
            border-radius: 6px;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            transition: all 0.3s;
        }

        .btn-view:hover {
            box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
            transform: translateY(-2px);
            color: #000;
        }

        /* No Results */
        .no-results {
            text-align: center;
            padding: 4rem 2rem;
            background: #1a1a1a;
            border-radius: 12px;
            border: 1px solid #333;
            grid-column: 1 / -1;
        }

        .no-results i {
            font-size: 4rem;
            color: #444;
            margin-bottom: 1.5rem;
        }

        .no-results h3 {
            color: #888;
            margin-bottom: 0.75rem;
        }

        .no-results p {
            color: #666;
            margin-bottom: 1.5rem;
        }

        /* Responsive */
        @media (max-width: 1200px) {
            .main-container {
                flex-direction: column;
            }

            .filter-sidebar {
                width: 100%;
                position: relative;
                top: 0;
            }
        }

        @media (max-width: 768px) {
            .car-grid {
                grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            }

            .results-header {
                flex-direction: column;
                gap: 1rem;
            }

            .sort-dropdown {
                width: 100%;
            }

            .price-suggestions {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<!-- Page Header -->
<div class="page-header">
    <div class="container">
        <h1><i class="fas fa-car"></i> Danh Sách Xe</h1>
        <p class="subtitle">Khám phá bộ sưu tập xe hơi đa dạng với mức giá phù hợp</p>
    </div>
</div>

<div class="container">
    <div class="main-container">
        <!-- Filter Sidebar -->
        <aside class="filter-sidebar">
            <h5><i class="fas fa-filter"></i> Bộ Lọc</h5>
            <form action="${pageContext.request.contextPath}/cars" method="get" id="filterForm">

                <!-- Search -->
                <div class="filter-group">
                    <label><i class="fas fa-search"></i> Tìm kiếm</label>
                    <input type="text" class="form-control" name="search"
                           placeholder="Tên xe, hãng..." value="${searchKeyword}">
                </div>

                <!-- Brand Filter -->
                <div class="filter-group">
                    <label><i class="fas fa-tag"></i> Hãng xe</label>
                    <select class="form-select" name="brand">
                        <option value="">Tất cả</option>
                        <c:forEach var="brand" items="${brandList}">
                            <option value="${brand.brandId}" ${selectedBrand == brand.brandId ? 'selected' : ''}>
                                    ${brand.brandName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Price Range Slider -->
                <div class="filter-group">
                    <label><i class="fas fa-dollar-sign"></i> Khoảng giá</label>

                    <div class="price-slider-container">
                        <!-- Price Display -->
                        <div class="price-values">
                            <div class="price-value">
                                <span>Tối thiểu</span>
                                <div id="minPriceDisplay">0 ₫</div>
                            </div>
                            <div class="price-value">
                                <span>Tối đa</span>
                                <div id="maxPriceDisplay">5 Tỷ</div>
                            </div>
                        </div>

                        <!-- Slider -->
                        <div id="priceSlider"></div>

                        <!-- Hidden inputs -->
                        <input type="hidden" name="minPrice" id="minPriceInput" value="${minPrice}">
                        <input type="hidden" name="maxPrice" id="maxPriceInput" value="${maxPrice}">

                        <!-- Price Suggestions -->
                        <div class="price-suggestions">
                            <button type="button" class="price-suggestion-btn" onclick="setPriceRange(0, 500000000)">
                                Dưới 500 triệu
                            </button>
                            <button type="button" class="price-suggestion-btn" onclick="setPriceRange(500000000, 700000000)">
                                500 - 700 triệu
                            </button>
                            <button type="button" class="price-suggestion-btn" onclick="setPriceRange(700000000, 1000000000)">
                                700 triệu - 1 tỷ
                            </button>
                            <button type="button" class="price-suggestion-btn" onclick="setPriceRange(1000000000, 5000000000)">
                                Trên 1 tỷ
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Buttons -->
                <button type="submit" class="btn btn-filter">
                    <i class="fas fa-search"></i> Tìm Kiếm
                </button>
                <a href="${pageContext.request.contextPath}/cars" class="btn btn-reset">
                    <i class="fas fa-redo"></i> Đặt Lại
                </a>
            </form>
        </aside>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Active Filters -->
            <c:if test="${not empty searchKeyword or not empty selectedBrand or not empty minPrice or not empty maxPrice}">
                <div class="active-filters">
                    <c:if test="${not empty searchKeyword}">
                        <span class="filter-badge">
                            "${searchKeyword}"
                            <i class="fas fa-times" onclick="removeFilter('search')"></i>
                        </span>
                    </c:if>
                    <c:if test="${not empty selectedBrand}">
                        <span class="filter-badge">
                            <c:forEach var="brand" items="${brandList}">
                                <c:if test="${brand.brandId == selectedBrand}">${brand.brandName}</c:if>
                            </c:forEach>
                            <i class="fas fa-times" onclick="removeFilter('brand')"></i>
                        </span>
                    </c:if>
                    <c:if test="${not empty minPrice or not empty maxPrice}">
                        <span class="filter-badge">
                            <fmt:formatNumber value="${minPrice != null ? minPrice : 0}" pattern="#,##0"/> -
                            <fmt:formatNumber value="${maxPrice != null ? maxPrice : 5000000000}" pattern="#,##0"/> ₫
                            <i class="fas fa-times" onclick="removeFilter('price')"></i>
                        </span>
                    </c:if>
                </div>
            </c:if>

            <!-- Results Header -->
            <div class="results-header">
                <div class="results-count">
                    <i class="fas fa-car"></i> Tìm thấy <strong>${totalCars}</strong> xe
                </div>
                <div class="sort-dropdown">
                    <label>Sắp xếp:</label>
                    <select class="form-select" onchange="applySort(this.value)">
                        <option value="">Mặc định</option>
                        <option value="newest" ${sortBy == 'newest' ? 'selected' : ''}>Mới nhất</option>
                        <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                        <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                        <option value="name_asc" ${sortBy == 'name_asc' ? 'selected' : ''}>Tên A-Z</option>
                        <option value="name_desc" ${sortBy == 'name_desc' ? 'selected' : ''}>Tên Z-A</option>
                        <option value="year_desc" ${sortBy == 'year_desc' ? 'selected' : ''}>Năm mới nhất</option>
                    </select>
                </div>
            </div>

            <!-- Car Grid -->
            <div class="car-grid">
                <c:choose>
                    <c:when test="${not empty carList}">
                        <c:forEach var="car" items="${carList}">
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
                                            <img src="${mainImage}" alt="${car.name}">
                                        </c:when>
                                        <c:when test="${not empty car.imageUrl}">
                                            <img src="${car.imageUrl}" alt="${car.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://via.placeholder.com/300x200?text=No+Image" alt="${car.name}">
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
                                        <fmt:formatNumber value="${car.price}" pattern="#,##0" /> ₫
                                    </div>

                                    <!-- View Button -->
                                    <a href="${pageContext.request.contextPath}/car-detail?id=${car.id}" class="btn-view">
                                        <i class="fas fa-eye"></i> Xem Chi Tiết
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-results">
                            <i class="fas fa-car-crash"></i>
                            <h3>Không tìm thấy xe nào</h3>
                            <p>Vui lòng thử lại với các tiêu chí khác</p>
                            <a href="${pageContext.request.contextPath}/cars" class="btn btn-filter">
                                Xem Tất Cả Xe
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>

<script>
    // Initialize slider with current values
    const minPrice = ${minPrice != null ? minPrice : 0};
    const maxPrice = ${maxPrice != null ? maxPrice : 5000000000};

    const priceSlider = document.getElementById('priceSlider');

    noUiSlider.create(priceSlider, {
        start: [minPrice, maxPrice],
        connect: true,
        step: 10000000, // 10 million VND
        range: {
            'min': 0,
            'max': 5000000000  // 5 billion VND
        },
        format: {
            to: function(value) {
                return Math.round(value);
            },
            from: function(value) {
                return Number(value);
            }
        }
    });

    // Update display and inputs when slider changes
    priceSlider.noUiSlider.on('update', function(values, handle) {
        const min = parseInt(values[0]);
        const max = parseInt(values[1]);

        document.getElementById('minPriceDisplay').textContent = formatPrice(min);
        document.getElementById('maxPriceDisplay').textContent = formatPrice(max);

        document.getElementById('minPriceInput').value = min;
        document.getElementById('maxPriceInput').value = max;
    });

    // Format price to Vietnamese format
    function formatPrice(price) {
        if (price >= 1000000000) {
            return (price / 1000000000).toFixed(1).replace('.0', '') + ' Tỷ';
        } else if (price >= 1000000) {
            return (price / 1000000).toFixed(0) + ' Triệu';
        } else if (price === 0) {
            return '0 ₫';
        } else {
            return new Intl.NumberFormat('vi-VN').format(price) + ' ₫';
        }
    }

    // Set price range from suggestion buttons
    function setPriceRange(min, max) {
        priceSlider.noUiSlider.set([min, max]);
    }

    // Apply Sort
    function applySort(sortValue) {
        const url = new URL(window.location.href);
        if (sortValue) {
            url.searchParams.set('sort', sortValue);
        } else {
            url.searchParams.delete('sort');
        }
        window.location.href = url.toString();
    }

    // Remove Filter
    function removeFilter(filterType) {
        const url = new URL(window.location.href);
        switch(filterType) {
            case 'search':
                url.searchParams.delete('search');
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
</script>
</body>
</html>