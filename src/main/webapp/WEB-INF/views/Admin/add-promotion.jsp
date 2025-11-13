<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Khuyến Mãi Mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #0f0f0f;
            color: #f8f9fa;
            min-height: 100vh;
            padding: 20px;
        }

        .page-container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .card {
            background-color: #1c1c1c;
            border: 1px solid #333;
            border-radius: 15px;
            box-shadow: 0 0 30px rgba(255, 215, 0, 0.15);
            overflow: hidden;
        }

        .card-header {
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            border-bottom: 2px solid #ffd700;
            padding: 25px 30px;
        }

        .card-header h3 {
            color: #ffd700;
            margin: 0;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .card-body {
            padding: 30px;
        }

        .form-label {
            color: #ddd;
            font-weight: 600;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .required-mark {
            color: #dc3545;
        }

        .form-control, .form-select, textarea {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #444;
            padding: 12px 15px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .form-control:focus, .form-select:focus, textarea:focus {
            border-color: #ffd700;
            box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.1);
            background-color: #2a2a2a;
            color: #fff;
        }

        .form-control::placeholder {
            color: #666;
        }

        .form-text {
            color: #888;
            font-size: 0.875rem;
            margin-top: 6px;
        }

        /* Car Selection Section */
        .car-selection-section {
            background: rgba(255, 215, 0, 0.05);
            border: 2px solid #ffd700;
            border-radius: 12px;
            padding: 25px;
            margin: 25px 0;
        }

        .section-title {
            color: #ffd700;
            font-weight: 700;
            font-size: 1.1rem;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .selected-count {
            background: #ffd700;
            color: #000;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 700;
        }

        .selection-controls {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
            flex-wrap: wrap;
        }

        .btn-selection {
            background: #2a2a2a;
            border: 1px solid #444;
            color: #ffd700;
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 0.9rem;
            transition: all 0.3s;
        }

        .btn-selection:hover {
            background: #ffd700;
            color: #000;
            border-color: #ffd700;
        }

        .search-box {
            position: relative;
            margin-bottom: 15px;
        }

        .search-box input {
            padding-left: 40px;
        }

        .search-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #888;
        }

        .car-list-container {
            max-height: 500px;
            overflow-y: auto;
            background: #0f0f0f;
            border: 1px solid #333;
            border-radius: 10px;
            padding: 15px;
        }

        /* Custom Scrollbar */
        .car-list-container::-webkit-scrollbar {
            width: 8px;
        }

        .car-list-container::-webkit-scrollbar-track {
            background: #1a1a1a;
            border-radius: 10px;
        }

        .car-list-container::-webkit-scrollbar-thumb {
            background: #ffd700;
            border-radius: 10px;
        }

        .car-list-container::-webkit-scrollbar-thumb:hover {
            background: #e5c100;
        }

        .car-item {
            background: #1a1a1a;
            border: 2px solid #333;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 12px;
            transition: all 0.3s;
            cursor: pointer;
        }

        .car-item:hover {
            border-color: #ffd700;
            background: #242424;
            transform: translateX(5px);
        }

        .car-item.selected {
            border-color: #ffd700;
            background: rgba(255, 215, 0, 0.1);
        }

        .car-item .form-check {
            display: flex;
            align-items: flex-start;
            gap: 15px;
            margin: 0;
        }

        .car-item .form-check-input {
            width: 22px;
            height: 22px;
            margin-top: 3px;
            cursor: pointer;
            flex-shrink: 0;
        }

        .car-item .form-check-input:checked {
            background-color: #ffd700;
            border-color: #ffd700;
        }

        .car-item .form-check-label {
            cursor: pointer;
            flex: 1;
            width: 100%;
        }

        .car-info {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 20px;
        }

        .car-details {
            flex: 1;
        }

        .car-name {
            color: #fff;
            font-weight: 700;
            font-size: 1.05rem;
            margin-bottom: 8px;
        }

        .car-specs {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            font-size: 0.9rem;
            color: #aaa;
            margin-top: 8px;
        }

        .car-spec-item {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .car-spec-item i {
            color: #ffd700;
            font-size: 0.85rem;
        }

        .car-price {
            text-align: right;
            flex-shrink: 0;
        }

        .price-label {
            color: #888;
            font-size: 0.85rem;
            margin-bottom: 4px;
        }

        .price-value {
            color: #ffd700;
            font-size: 1.3rem;
            font-weight: 700;
            white-space: nowrap;
        }

        .badge {
            padding: 4px 10px;
            border-radius: 5px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .badge-available {
            background: rgba(40, 167, 69, 0.2);
            color: #28a745;
            border: 1px solid #28a745;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }

        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #444;
        }

        .empty-state p {
            font-size: 1.1rem;
            margin: 0;
        }

        /* Alert */
        .alert {
            border-radius: 10px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 25px;
        }

        .alert-danger {
            background: rgba(220, 53, 69, 0.15);
            border: 1px solid rgba(220, 53, 69, 0.3);
            color: #ff6b6b;
        }

        .alert-warning {
            background: rgba(255, 193, 7, 0.15);
            border: 1px solid rgba(255, 193, 7, 0.3);
            color: #ffc107;
        }

        /* Buttons */
        .btn-primary-gold {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            border: none;
            color: #000;
            font-weight: 700;
            padding: 12px 35px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-primary-gold:hover {
            background: linear-gradient(135deg, #ffed4e 0%, #ffd700 100%);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.4);
            color: #000;
        }

        .btn-secondary-dark {
            background: #2a2a2a;
            border: 1px solid #444;
            color: #ddd;
            padding: 12px 35px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .btn-secondary-dark:hover {
            background: #333;
            border-color: #555;
            color: #fff;
        }

        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
            padding-top: 25px;
            border-top: 1px solid #333;
        }

        /* Info Box */
        .info-box {
            background: rgba(23, 162, 184, 0.1);
            border-left: 4px solid #17a2b8;
            padding: 15px 20px;
            border-radius: 8px;
            margin-top: 15px;
        }

        .info-box p {
            margin: 0;
            color: #17a2b8;
            font-size: 0.95rem;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .car-info {
                flex-direction: column;
            }

            .car-price {
                text-align: left;
            }

            .selection-controls {
                flex-direction: column;
            }

            .btn-selection {
                width: 100%;
            }
        }
    </style>
</head>
<body>

<div class="page-container">
    <div class="card">
        <!-- Header -->
        <div class="card-header">
            <h3>
                <i class="fas fa-gift"></i>
                Thêm Khuyến Mãi Mới
            </h3>
        </div>

        <!-- Body -->
        <div class="card-body">
            <!-- Error Alert -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <!-- Form -->
            <form method="post" action="${pageContext.request.contextPath}/Admin/add-promotion" id="promotionForm">

                <!-- Basic Info Section -->
                <div class="row mb-3">
                    <div class="col-md-8">
                        <label class="form-label">
                            <i class="fas fa-heading"></i>
                            Tên khuyến mãi
                            <span class="required-mark">*</span>
                        </label>
                        <input type="text"
                               name="title"
                               class="form-control"
                               placeholder="VD: Giảm giá mùa hè 2025"
                               value="${param.title}"
                               required
                               minlength="3"
                               maxlength="100">
                        <div class="form-text">
                            <i class="fas fa-info-circle"></i> Từ 3-100 ký tự
                        </div>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">
                            <i class="fas fa-percent"></i>
                            Phần trăm giảm giá
                            <span class="required-mark">*</span>
                        </label>
                        <input type="number"
                               step="0.01"
                               name="discountPercentage"
                               id="discountPercentage"
                               class="form-control"
                               placeholder="VD: 15"
                               value="${param.discountPercentage}"
                               min="1"
                               max="100"
                               required>
                        <div class="form-text">
                            <i class="fas fa-info-circle"></i> Từ 1% - 100%
                        </div>
                    </div>
                </div>

                <!-- Description -->
                <div class="mb-3">
                    <label class="form-label">
                        <i class="fas fa-align-left"></i>
                        Mô tả
                    </label>
                    <textarea name="description"
                              class="form-control"
                              rows="3"
                              maxlength="500"
                              placeholder="Nhập mô tả chi tiết về chương trình khuyến mãi...">${param.description}</textarea>
                    <div class="form-text">
                        <i class="fas fa-info-circle"></i> Tối đa 500 ký tự (không bắt buộc)
                    </div>
                </div>

                <!-- Date Range -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label">
                            <i class="fas fa-calendar-alt"></i>
                            Ngày bắt đầu
                            <span class="required-mark">*</span>
                        </label>
                        <input type="date"
                               name="startDate"
                               id="startDate"
                               class="form-control"
                               value="${param.startDate}"
                               required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label">
                            <i class="fas fa-calendar-check"></i>
                            Ngày kết thúc
                            <span class="required-mark">*</span>
                        </label>
                        <input type="date"
                               name="endDate"
                               id="endDate"
                               class="form-control"
                               value="${param.endDate}"
                               required>
                    </div>
                </div>

                <!-- Car Selection Section -->
                <div class="car-selection-section">
                    <div class="section-title">
                        <span>
                            <i class="fas fa-car"></i>
                            Chọn xe áp dụng khuyến mãi
                            <span class="required-mark">*</span>
                        </span>
                        <span class="selected-count" id="selectedCount">0 xe được chọn</span>
                    </div>

                    <!-- Selection Controls -->
                    <div class="selection-controls">
                        <button type="button" class="btn-selection" id="selectAllBtn">
                            <i class="fas fa-check-double"></i> Chọn tất cả
                        </button>
                        <button type="button" class="btn-selection" id="deselectAllBtn">
                            <i class="fas fa-times"></i> Bỏ chọn tất cả
                        </button>
                        <button type="button" class="btn-selection" id="selectAvailableBtn">
                            <i class="fas fa-check-circle"></i> Chọn xe có sẵn
                        </button>
                    </div>

                    <!-- Search Box -->
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text"
                               id="carSearchInput"
                               class="form-control"
                               placeholder="Tìm kiếm xe theo tên, hãng, màu...">
                    </div>

                    <!-- Car List -->
                    <div class="car-list-container" id="carListContainer">
                        <c:choose>
                            <c:when test="${not empty allCars}">
                                <c:forEach var="car" items="${allCars}">
                                    <c:if test="${car.status eq 'AVAILABLE'}">
                                        <div class="car-item" data-car-id="${car.id}">
                                            <div class="form-check">
                                                <input class="form-check-input car-checkbox"
                                                       type="checkbox"
                                                       name="carIds"
                                                       value="${car.id}"
                                                       id="car-${car.id}"
                                                       data-brand="${car.brandName}"
                                                       data-model="${car.name}"
                                                       data-color="${car.color}"
                                                       data-year="${car.year}">
                                                <label class="form-check-label" for="car-${car.id}">
                                                    <div class="car-info">
                                                        <div class="car-details">
                                                            <div class="car-name">
                                                                    ${car.brandName} ${car.name}
                                                            </div>
                                                            <div class="car-specs">
                                                                <div class="car-spec-item">
                                                                    <i class="fas fa-calendar"></i>
                                                                    <span>Năm ${car.year}</span>
                                                                </div>
                                                                <div class="car-spec-item">
                                                                    <i class="fas fa-palette"></i>
                                                                    <span>${car.color}</span>
                                                                </div>
                                                                <div class="car-spec-item">
                                                                    <i class="fas fa-box"></i>
                                                                    <span>Kho: ${car.stock}</span>
                                                                </div>
                                                                <div class="car-spec-item">
                                                                    <span class="badge badge-available">
                                                                        <i class="fas fa-check-circle"></i> Có sẵn
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="car-price">
                                                            <div class="price-label">Giá hiện tại</div>
                                                            <div class="price-value">
                                                                <fmt:formatNumber value="${car.price}"
                                                                                  type="number"
                                                                                  maxFractionDigits="0"/>₫
                                                            </div>
                                                        </div>
                                                    </div>
                                                </label>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <i class="fas fa-car-side"></i>
                                    <p>Không có xe nào có sẵn trong hệ thống</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Info Box -->
                    <div class="info-box">
                        <p>
                            <i class="fas fa-info-circle"></i>
                            <strong>Lưu ý:</strong> Chỉ hiển thị các xe có trạng thái "Có sẵn".
                            Bạn phải chọn ít nhất 1 xe để tạo khuyến mãi.
                        </p>
                    </div>
                </div>

                <!-- Buttons -->
                <div class="button-group">
                    <button type="submit" class="btn-primary-gold">
                        <i class="fas fa-save"></i> Lưu Khuyến Mãi
                    </button>
                    <a href="${pageContext.request.contextPath}/Admin/promotion-list"
                       class="btn-secondary-dark">
                        <i class="fas fa-arrow-left"></i> Quay Lại
                    </a>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // ============================================
    // CONSTANTS & ELEMENTS
    // ============================================
    const selectedCountEl = document.getElementById('selectedCount');
    const carCheckboxes = document.querySelectorAll('.car-checkbox');
    const carItems = document.querySelectorAll('.car-item');
    const searchInput = document.getElementById('carSearchInput');

    // ============================================
    // UPDATE SELECTED COUNT
    // ============================================
    function updateSelectedCount() {
        const checkedBoxes = document.querySelectorAll('.car-checkbox:checked');
        const count = checkedBoxes.length;
        selectedCountEl.textContent = count + ' xe được chọn';

        // Update car item styling
        carItems.forEach(item => {
            const checkbox = item.querySelector('.car-checkbox');
            if (checkbox && checkbox.checked) {
                item.classList.add('selected');
            } else {
                item.classList.remove('selected');
            }
        });
    }

    // ============================================
    // SELECT ALL
    // ============================================
    document.getElementById('selectAllBtn').addEventListener('click', function() {
        const visibleCheckboxes = Array.from(carCheckboxes).filter(cb => {
            const carItem = cb.closest('.car-item');
            return carItem.style.display !== 'none';
        });

        visibleCheckboxes.forEach(cb => cb.checked = true);
        updateSelectedCount();
    });

    // ============================================
    // DESELECT ALL
    // ============================================
    document.getElementById('deselectAllBtn').addEventListener('click', function() {
        carCheckboxes.forEach(cb => cb.checked = false);
        updateSelectedCount();
    });

    // ============================================
    // SELECT AVAILABLE (all visible cars)
    // ============================================
    document.getElementById('selectAvailableBtn').addEventListener('click', function() {
        const visibleCheckboxes = Array.from(carCheckboxes).filter(cb => {
            const carItem = cb.closest('.car-item');
            return carItem.style.display !== 'none';
        });

        visibleCheckboxes.forEach(cb => cb.checked = true);
        updateSelectedCount();
    });

    // ============================================
    // CHECKBOX CHANGE EVENT
    // ============================================
    carCheckboxes.forEach(cb => {
        cb.addEventListener('change', updateSelectedCount);
    });

    // ============================================
    // CAR ITEM CLICK (Toggle checkbox)
    // ============================================
    carItems.forEach(item => {
        item.addEventListener('click', function(e) {
            // Don't toggle if clicking on checkbox or label
            if (e.target.classList.contains('form-check-input') ||
                e.target.classList.contains('form-check-label')) {
                return;
            }

            const checkbox = this.querySelector('.car-checkbox');
            if (checkbox) {
                checkbox.checked = !checkbox.checked;
                updateSelectedCount();
            }
        });
    });

    // ============================================
    // SEARCH FUNCTIONALITY
    // ============================================
    searchInput.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase().trim();

        carItems.forEach(item => {
            const checkbox = item.querySelector('.car-checkbox');
            if (!checkbox) return;

            const brand = checkbox.dataset.brand.toLowerCase();
            const model = checkbox.dataset.model.toLowerCase();
            const color = checkbox.dataset.color.toLowerCase();
            const year = checkbox.dataset.year;

            const searchString = brand + ' ' + model + ' ' + color + ' ' + year;

            if (searchString.includes(searchTerm)) {
                item.style.display = '';
            } else {
                item.style.display = 'none';
            }
        });

        updateSelectedCount();
    });

    // ============================================
    // DATE VALIDATION
    // ============================================
    document.addEventListener('DOMContentLoaded', function() {
        const today = new Date().toISOString().split('T')[0];
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');

        startDateInput.setAttribute('min', today);
        endDateInput.setAttribute('min', today);

        startDateInput.addEventListener('change', function() {
            endDateInput.setAttribute('min', this.value);

            if (endDateInput.value && endDateInput.value < this.value) {
                endDateInput.value = '';
            }
        });
    });

    // ============================================
    // FORM VALIDATION
    // ============================================
    document.getElementById('promotionForm').addEventListener('submit', function(e) {
        // Validate selected cars
        const checkedCars = document.querySelectorAll('.car-checkbox:checked');

        if (checkedCars.length === 0) {
            e.preventDefault();
            alert('Vui lòng chọn ít nhất một xe để áp dụng khuyến mãi!');

            // Scroll to car section
            document.querySelector('.car-selection-section').scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });
            return false;
        }

        // Validate dates
        const startDate = new Date(document.getElementById('startDate').value);
        const endDate = new Date(document.getElementById('endDate').value);

        if (endDate < startDate) {
            e.preventDefault();
            alert('Ngày kết thúc phải sau ngày bắt đầu!');
            return false;
        }

        // Validate discount percentage
        const discount = parseFloat(document.getElementById('discountPercentage').value);

        if (isNaN(discount) || discount < 1 || discount > 100) {
            e.preventDefault();
            alert('Phần trăm giảm giá phải từ 1-100%!');
            return false;
        }

        // Final confirmation
        const confirmMessage = `Bạn đã chọn ${checkedCars.length} xe.\n\n` +
            `Giảm giá: ${discount}%\n` +
            `Từ: ${startDate.toLocaleDateString('vi-VN')}\n` +
            `Đến: ${endDate.toLocaleDateString('vi-VN')}\n\n` +
            `Xác nhận tạo khuyến mãi?`;

        if (!confirm(confirmMessage)) {
            e.preventDefault();
            return false;
        }

        return true;
    });

    // ============================================
    // INITIALIZE
    // ============================================
    updateSelectedCount();
</script>

</body>
</html>