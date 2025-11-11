<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm khuyến mãi mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #0f0f0f;
            color: #f8f9fa;
            padding: 40px;
        }

        .card {
            background-color: #1c1c1c;
            border: 1px solid #333;
            border-radius: 14px;
            padding: 30px;
            max-width: 900px;
            margin: auto;
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.1);
        }

        .card h3 {
            color: #ffd700;
            margin-bottom: 25px;
            text-align: center;
        }

        .form-label {
            color: #ddd;
            font-weight: 600;
        }

        .required-mark {
            color: #dc3545;
            margin-left: 4px;
        }

        .form-control, .form-select {
            background-color: #2a2a2a;
            color: #fff;
            border: 1px solid #444;
        }

        .form-control:focus, .form-select:focus {
            border-color: #ffd700;
            box-shadow: 0 0 6px #ffd700;
            background-color: #2a2a2a;
            color: #fff;
        }

        .form-text {
            color: #999;
            font-size: 0.875rem;
        }

        /* NEW: Apply to all cars section */
        .apply-to-all-section {
            background: rgba(255, 215, 0, 0.05);
            border: 2px solid #ffd700;
            border-radius: 10px;
            padding: 20px;
            margin-top: 25px;
            margin-bottom: 25px;
        }

        .form-check-input {
            width: 20px;
            height: 20px;
            cursor: pointer;
        }

        .form-check-input:checked {
            background-color: #ffd700;
            border-color: #ffd700;
        }

        .form-check-label {
            cursor: pointer;
            color: #ffd700;
            font-weight: 600;
            margin-left: 8px;
        }

        .info-box {
            background: rgba(255, 215, 0, 0.1);
            border-left: 4px solid #ffd700;
            padding: 12px 15px;
            margin-top: 12px;
            border-radius: 4px;
        }

        .info-box ul {
            margin-bottom: 0;
            padding-left: 20px;
        }

        .info-box li {
            color: #ccc;
            margin-bottom: 5px;
        }

        .btn-primary {
            background-color: #ffd700;
            border: none;
            color: #000;
            font-weight: bold;
            padding: 10px 30px;
        }

        .btn-primary:hover {
            background-color: #e5c100;
            color: #000;
        }

        .btn-secondary {
            background-color: #444;
            border: none;
            padding: 10px 30px;
        }

        .btn-secondary:hover {
            background-color: #555;
        }

        .alert {
            text-align: center;
            font-weight: bold;
            border-radius: 8px;
        }
    </style>
</head>
<body>

<div class="card">
    <h3><i class="fas fa-gift"></i> Thêm Khuyến Mãi Mới</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i> ${error}
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/Admin/add-promotion" id="promotionForm">

        <!-- Title -->
        <div class="mb-3">
            <label class="form-label">
                <i class="fas fa-heading"></i> Tên khuyến mãi
                <span class="required-mark">*</span>
            </label>
            <input type="text"
                   name="title"
                   class="form-control"
                   placeholder="VD: Giảm giá mùa hè 2025"
                   required
                   minlength="3"
                   maxlength="100">
            <div class="form-text">
                <i class="fas fa-info-circle"></i> Từ 3-100 ký tự
            </div>
        </div>

        <!-- Description -->
        <div class="mb-3">
            <label class="form-label">
                <i class="fas fa-align-left"></i> Mô tả
            </label>
            <textarea name="description"
                      class="form-control"
                      rows="3"
                      maxlength="500"
                      placeholder="Nhập mô tả chi tiết về chương trình khuyến mãi..."></textarea>
            <div class="form-text">
                <i class="fas fa-info-circle"></i> Tối đa 500 ký tự (không bắt buộc)
            </div>
        </div>

        <!-- Date Range -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="form-label">
                    <i class="fas fa-calendar-alt"></i> Ngày bắt đầu
                    <span class="required-mark">*</span>
                </label>
                <input type="date"
                       name="startDate"
                       id="startDate"
                       class="form-control"
                       required>
            </div>
            <div class="col-md-6">
                <label class="form-label">
                    <i class="fas fa-calendar-check"></i> Ngày kết thúc
                    <span class="required-mark">*</span>
                </label>
                <input type="date"
                       name="endDate"
                       id="endDate"
                       class="form-control"
                       required>
            </div>
        </div>

        <!-- Discount Percentage -->
        <div class="mb-4">
            <label class="form-label">
                <i class="fas fa-percent"></i> Phần trăm giảm giá
                <span class="required-mark">*</span>
            </label>
            <input type="number"
                   step="0.01"
                   name="discountPercentage"
                   id="discountPercentage"
                   class="form-control"
                   placeholder="VD: 15.50"
                   min="1"
                   max="100"
                   required>
            <div class="form-text">
                <i class="fas fa-info-circle"></i> Từ 1% đến 100%
            </div>
        </div>

        <!-- NEW: Apply to All Cars Section -->
        <div class="apply-to-all-section">
            <div class="form-check">
                <input class="form-check-input"
                       type="checkbox"
                       id="applyToAllCars"
                       name="applyToAllCars"
                       value="true"
                       checked>
                <label class="form-check-label" for="applyToAllCars">
                    <i class="fas fa-car"></i> Áp dụng cho tất cả xe trong showroom
                </label>
            </div>

            <div class="info-box">
                <div style="color: #ffd700; font-weight: 600; margin-bottom: 8px;">
                    <i class="fas fa-lightbulb"></i> Hướng dẫn:
                </div>
                <ul>
                    <li>
                        <strong>✅ Bật (khuyến nghị):</strong>
                        Khuyến mãi sẽ tự động áp dụng cho tất cả xe có trạng thái "AVAILABLE".
                        Phù hợp với các chương trình giảm giá chung.
                    </li>
                    <li>
                        <strong>❌ Tắt:</strong>
                        Bạn sẽ được chuyển đến trang chọn xe cụ thể.
                        Phù hợp khi muốn khuyến mãi chỉ áp dụng cho một số xe nhất định.
                    </li>
                </ul>
            </div>
        </div>

        <!-- Buttons -->
        <div class="text-center">
            <button type="submit" class="btn btn-primary me-2">
                <i class="fas fa-save"></i> Lưu Khuyến Mãi
            </button>
            <a href="${pageContext.request.contextPath}/Admin/promotion-list" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay Lại
            </a>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Set minimum date to today
    document.addEventListener('DOMContentLoaded', function() {
        const today = new Date().toISOString().split('T')[0];
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');

        startDateInput.setAttribute('min', today);
        endDateInput.setAttribute('min', today);

        // Update end date min when start date changes
        startDateInput.addEventListener('change', function() {
            endDateInput.setAttribute('min', this.value);

            // If end date is before start date, clear end date
            if (endDateInput.value && endDateInput.value < this.value) {
                endDateInput.value = '';
            }
        });
    });

    // Form validation before submit
    document.getElementById('promotionForm').addEventListener('submit', function(e) {
        const startDate = new Date(document.getElementById('startDate').value);
        const endDate = new Date(document.getElementById('endDate').value);
        const discount = parseFloat(document.getElementById('discountPercentage').value);

        // Validate dates
        if (endDate < startDate) {
            e.preventDefault();
            alert('❌ Ngày kết thúc phải sau ngày bắt đầu!');
            return false;
        }

        // Validate discount percentage
        if (isNaN(discount) || discount < 1 || discount > 100) {
            e.preventDefault();
            alert('❌ Phần trăm giảm giá phải từ 1-100%!');
            return false;
        }

        // Confirm before submit
        const applyToAll = document.getElementById('applyToAllCars').checked;
        const confirmMessage = applyToAll
            ? '✅ Khuyến mãi sẽ được áp dụng cho TẤT CẢ xe. Xác nhận tạo khuyến mãi?'
            : '⚠️ Sau khi tạo, bạn sẽ cần chọn xe cụ thể cho khuyến mãi. Tiếp tục?';

        if (!confirm(confirmMessage)) {
            e.preventDefault();
            return false;
        }
    });
</script>

</body>
</html>
