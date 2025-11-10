<%--
  Created by IntelliJ IDEA.
  User: AkatsukiYui
  Date: 11/7/2025
  Time: 12:49 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bảng điều khiển Staff</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        :root{
            --bg:#0f0f0f; --panel:#151515; --panel2:#1b1b1b; --line:#2a2a2a;
            --text:#f2f2f2; --muted:#b7b7b7; --gold:#ffd700; --gold-soft:#ffe37a;
        }
        html,body{background:var(--bg);color:var(--text);height:100%;margin:0}
        a{text-decoration:none}
        .wrap{min-height:100dvh; display:grid; grid-template-columns:1fr 260px}
        .topbar{
            position:sticky; top:0; z-index:20; grid-column:1 / -1;
            display:flex; align-items:center; justify-content:space-between; gap:16px;
            padding:14px 20px; background:linear-gradient(180deg,#0d0d0d,#111);
            border-bottom:1px solid var(--line);
        }
        .brand{display:flex; align-items:center; gap:10px}
        .brand .logo{width:28px;height:28px;border-radius:50%;display:grid;place-items:center;background:#1a1a1a;border:1px solid var(--line);color:var(--gold)}
        .brand .txt{font-weight:700; letter-spacing:.3px; color:var(--gold)}
        .search{flex:1; display:flex; gap:8px; max-width:680px}
        .search input{flex:1; background:#121212; color:var(--text); border:1px solid var(--line); border-radius:10px; padding:10px 12px}
        .search input:focus{outline:none; box-shadow:0 0 0 2px rgba(255,215,0,.25)}
        .quick{display:flex; align-items:center; gap:10px}
        .pill{padding:8px 12px; border-radius:999px; background:var(--gold); color:#111; font-weight:700}
        .avatar{width:34px;height:34px;border-radius:50%;border:2px solid var(--gold); overflow:hidden}
        .avatar img{width:100%;height:100%;object-fit:cover}

        .content{padding:22px 20px 26px}
        .side{
            border-left:1px solid var(--line); padding:18px 14px; background:linear-gradient(180deg,#131313,#0f0f0f);
        }

        .kpis{display:grid; grid-template-columns:repeat(4,minmax(180px,1fr)); gap:16px}
        .kpi{
            background:linear-gradient(180deg,var(--panel),var(--panel2));
            border:1px solid var(--line); border-radius:14px; padding:16px;
            transition:.25s transform, .25s border-color;
        }
        .kpi:hover{transform:translateY(-4px); border-color:rgba(255,215,0,.35)}
        .kpi h3{color:var(--gold); margin:0 0 6px}
        .kpi p{color:var(--muted); margin:0}
        .kpi .btn{border-radius:10px; font-weight:600}
        .section-title{display:flex; align-items:center; gap:10px; margin:18px 0 8px}
        .dot{width:10px;height:10px;border-radius:50%;background:var(--gold); box-shadow:0 0 0 3px rgba(255,215,0,.2)}

        .nav-title{color:#cfcfcf; text-transform:uppercase; font-size:.8rem; letter-spacing:.6px; margin:6px 0 10px}
        .nav a{
            display:block; padding:10px 12px; margin:6px 0; border-radius:999px;
            color:#eee; background:#141414; border:1px solid #202020; transition:.2s;
        }
        .nav a:hover{border-color:rgba(255,215,0,.4); color:#fff}
        .nav a.active{background:linear-gradient(90deg, rgba(255,215,0,.18), transparent 70%); border-color:rgba(255,215,0,.6); color:#fff}

        .panel{
            background:linear-gradient(180deg,var(--panel),var(--panel2));
            border:1px solid var(--line); border-radius:14px; padding:16px;
        }
        .mini{font-size:.85rem; color:var(--muted); padding-top:6px}
        /* Tắt gạch chân cho link trong sidebar ở mọi trạng thái */
        .nav a,
        .nav a:hover,
        .nav a:focus,
        .nav a:active,
        .nav a.active {
            text-decoration: none !important;
            outline: none;            /* tránh viền focus của browser */
            box-shadow: none;         /* tránh bóng focus của bootstrap */
        }

    </style>
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="logo"><i class="fa-solid fa-bolt"></i></div>
        <div class="txt">CarSale • Staff</div>
    </div>
<%--    <form class="search" action="${pageContext.request.contextPath}/staff/search" method="get">--%>
<%--        <input type="text" name="q" placeholder="Tìm đơn hàng, khách, xe…">--%>
<%--    </form>--%>
    <div class="quick">
<%--        <div class="avatar">--%>
<%--            <img src="${sessionScope.staffAvatar != null ? sessionScope.staffAvatar : pageContext.request.contextPath.concat('/assets/img/avatar.png')}" alt="">--%>
<%--        </div>--%>
        <a class="text-warning fw-bold" href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-right-from-bracket"></i></a>
    </div>
</div>

<div class="wrap">
    <!-- Content -->
    <div class="content">
        <!-- KPIs -->
<%--        <div class="kpis">--%>
<%--            <div class="kpi">--%>
<%--                <h3><i class="fa-solid fa-cart-shopping"></i> 132</h3>--%>
<%--                <p>Đơn hôm nay</p>--%>
<%--                <a class="btn btn-sm btn-outline-warning mt-2" href="${pageContext.request.contextPath}/staff/orders">Xem chi tiết</a>--%>
<%--            </div>--%>
<%--            <div class="kpi">--%>
<%--                <h3><i class="fa-solid fa-car"></i> 58</h3>--%>
<%--                <p>Xe còn hàng</p>--%>
<%--                <span class="mini">Cập nhật 5 phút trước</span>--%>
<%--            </div>--%>
<%--            <div class="kpi">--%>
<%--                <h3><i class="fa-solid fa-percent"></i> 7</h3>--%>
<%--                <p>Khuyến mãi đang chạy</p>--%>
<%--            </div>--%>
<%--            <div class="kpi">--%>
<%--                <h3><i class="fa-solid fa-users"></i> 24</h3>--%>
<%--                <p>Khách mới</p>--%>
<%--            </div>--%>
<%--        </div>--%>

        <!-- Khu vực render động -->
        <div class="section-title"><span class="dot"></span><h5 class="m-0">Danh sách</h5></div>
        <div id="overviewSection" class="panel mb-3">
            <div id="dynamicContent">Đang tải…</div>
        </div>
    </div>

    <!-- Right sidebar -->
    <aside class="side">
        <div class="nav-title">Điều hướng</div>
        <nav class="nav">
            <a href="#" id="btnManageCars" class="active"><i class="fas fa-car"></i> &nbsp;Quản lý xe</a>
            <a href="#" id="btnTransactions"><i class="fas fa-receipt"></i> &nbsp;Lịch sử giao dịch</a>
            <a href="#" id="btnCustomers"><i class="fas fa-user-group"></i> &nbsp;Khách hàng</a>
            <a href="#" id="btnPromotion"><i class="fas fa-bullhorn"></i> &nbsp;Khuyến mãi</a>
        </nav>


        <div class="nav-title mt-3">Hệ thống</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/home"><i class="fas fa-house"></i> &nbsp;Trang chủ</a>
        </nav>
    </aside>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const dynamicContent = document.getElementById("dynamicContent");

        // Buttons
        const btnCars   = document.getElementById("btnManageCars");
        const btnPromo  = document.getElementById("btnPromotion");
        const btnTrans  = document.getElementById("btnTransactions");
        const btnCust   = document.getElementById("btnCustomers");
        const allNav    = document.querySelectorAll('.nav a');

        // Endpoints (trả fragment khi có header X-Requested-With: fetch)
        const URL_CARS   = "${pageContext.request.contextPath}/staff/car-list";
        const URL_PROMO  = "${pageContext.request.contextPath}/staff/promotions";
        const URL_TRANS  = "${pageContext.request.contextPath}/staff/transactions";
        const URL_CUST   = "${pageContext.request.contextPath}/staff/customers";

        function setActive(el){
            allNav.forEach(a => a.classList.remove('active'));
            el.classList.add('active');
        }

        function loadSection(url, activeEl, pushSection){
            dynamicContent.innerHTML = "<div class='text-center py-4'><i class='fas fa-spinner fa-spin'></i> Đang tải...</div>";
            fetch(url, { headers: { "X-Requested-With": "fetch" } })
                .then(r => { if(!r.ok) throw new Error("Không thể tải nội dung."); return r.text(); })
                .then(html => {
                    dynamicContent.innerHTML = html;
                    if (activeEl) setActive(activeEl);
                    if (typeof pushSection === "string") {
                        const u = new URL(location.href);
                        u.searchParams.set("section", pushSection);
                        history.pushState({section: pushSection}, "", u.toString());
                    }
                })
                .catch(err => dynamicContent.innerHTML = `<div class='alert alert-danger text-center'>${err.message}</div>`);
        }

        // Click handlers
        btnCars .addEventListener("click", e => { e.preventDefault(); loadSection(URL_CARS , btnCars , "cars"); });
        btnPromo.addEventListener("click", e => { e.preventDefault(); loadSection(URL_PROMO, btnPromo, "promotions"); });
        btnTrans.addEventListener("click", e => { e.preventDefault(); loadSection(URL_TRANS, btnTrans, "transactions"); });
        btnCust .addEventListener("click", e => { e.preventDefault(); loadSection(URL_CUST , btnCust , "customers"); });

        // Router theo URL ?section=
        const section = new URLSearchParams(location.search).get("section");
        switch (section) {
            case "promotions":  loadSection(URL_PROMO, btnPromo, null); break;
            case "transactions":loadSection(URL_TRANS, btnTrans, null); break;
            case "customers":   loadSection(URL_CUST , btnCust , null); break;
            case "cars":
            default:            loadSection(URL_CARS , btnCars , null); break;
        }

        // Hỗ trợ Back/Forward
        window.addEventListener("popstate", (ev) => {
            const s = (ev.state && ev.state.section) || new URLSearchParams(location.search).get("section");
            switch (s) {
                case "promotions":  loadSection(URL_PROMO, btnPromo, null); break;
                case "transactions":loadSection(URL_TRANS, btnTrans, null); break;
                case "customers":   loadSection(URL_CUST , btnCust , null); break;
                case "cars":
                default:            loadSection(URL_CARS , btnCars , null); break;
            }
        });
    });
</script>
</body>
</html>
