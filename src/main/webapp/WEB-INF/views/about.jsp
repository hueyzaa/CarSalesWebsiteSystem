<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 09/10/2025
  Time: 2:00 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Về Chúng Tôi - Car Showroom</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Header -->
<jsp:include page="header.jsp" />

<div class="container my-5">
    <div class="text-center mb-5">
        <h1 class="display-4"><i class="fas fa-building"></i> Về Car Showroom</h1>
        <p class="lead">Đối tác tin cậy trong hành trình sở hữu xe hơi của bạn</p>
    </div>

    <div class="row mb-5">
        <div class="col-lg-6 mb-4">
            <img src="https://images.unsplash.com/photo-1562141960-ddb427c27e52?auto=format&fit=crop&w=800&q=80"
                 class="img-fluid rounded shadow" alt="Car Showroom">
        </div>
        <div class="col-lg-6">
            <h2 class="mb-4">Câu Chuyện Của Chúng Tôi</h2>
            <p>
                Car Showroom được thành lập với sứ mệnh mang đến cho khách hàng những trải nghiệm
                mua sắm xe hơi tuyệt vời nhất. Với hơn 10 năm kinh nghiệm trong ngành, chúng tôi
                tự hào là một trong những đại lý xe hơi uy tín hàng đầu tại Việt Nam.
            </p>
            <p>
                Chúng tôi cung cấp đa dạng các dòng xe từ các thương hiệu nổi tiếng thế giới,
                đảm bảo chất lượng và giá cả cạnh tranh. Đội ngũ nhân viên chuyên nghiệp của
                chúng tôi luôn sẵn sàng tư vấn và hỗ trợ bạn tìm được chiếc xe phù hợp nhất.
            </p>
        </div>
    </div>

    <div class="row text-center mb-5">
        <div class="col-md-4 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <i class="fas fa-award fa-3x text-primary mb-3"></i>
                    <h3 class="card-title">Chất Lượng Đảm Bảo</h3>
                    <p class="card-text">
                        Tất cả xe đều được kiểm tra kỹ lưỡng và có chế độ bảo hành uy tín
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <i class="fas fa-users fa-3x text-success mb-3"></i>
                    <h3 class="card-title">Dịch Vụ Tận Tâm</h3>
                    <p class="card-text">
                        Đội ngũ chuyên nghiệp luôn sẵn sàng tư vấn và hỗ trợ 24/7
                    </p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <i class="fas fa-dollar-sign fa-3x text-warning mb-3"></i>
                    <h3 class="card-title">Giá Cả Hợp Lý</h3>
                    <p class="card-text">
                        Cam kết giá tốt nhất thị trường với nhiều chương trình ưu đãi hấp dẫn
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-5">
        <div class="col-12">
            <h2 class="text-center mb-4">Tại Sao Chọn Chúng Tôi?</h2>
        </div>
        <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Đa dạng các dòng xe từ nhiều thương hiệu
                </li>
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Tư vấn miễn phí và chuyên nghiệp
                </li>
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Hỗ trợ vay vốn ngân hàng lãi suất ưu đãi
                </li>
            </ul>
        </div>
        <div class="col-md-6">
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Bảo hành chính hãng và dịch vụ hậu mãi tốt
                </li>
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Giao xe tận nơi miễn phí
                </li>
                <li class="list-group-item">
                    <i class="fas fa-check-circle text-success"></i>
                    Đội ngũ kỹ thuật viên giàu kinh nghiệm
                </li>
            </ul>
        </div>
    </div>

    <div class="text-center bg-light p-5 rounded">
        <h2 class="mb-4">Sẵn Sàng Tìm Chiếc Xe Mơ Ước?</h2>
        <p class="lead mb-4">
            Hãy để chúng tôi giúp bạn tìm được chiếc xe hoàn hảo cho nhu cầu của bạn!
        </p>
        <div class="d-flex gap-3 justify-content-center">
            <a href="${pageContext.request.contextPath}/cars" class="btn btn-primary btn-lg">
                <i class="fas fa-car"></i> Xem Xe
            </a>
            <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-primary btn-lg">
                <i class="fas fa-phone"></i> Liên Hệ Ngay
            </a>
        </div>
    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
