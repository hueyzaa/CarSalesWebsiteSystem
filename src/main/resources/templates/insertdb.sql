-- =============================================
-- INSERT CAR AND IMAGES DATA
-- Car Sales Website System
-- Using Real Unsplash Images
-- =============================================

-- =============================================
-- 1. INSERT BRANDS (Nếu chưa có)
-- =============================================
IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Toyota')
    INSERT INTO Brand (brand_name) VALUES (N'Toyota');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Honda')
    INSERT INTO Brand (brand_name) VALUES (N'Honda');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Mercedes-Benz')
    INSERT INTO Brand (brand_name) VALUES (N'Mercedes-Benz');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'BMW')
    INSERT INTO Brand (brand_name) VALUES (N'BMW');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Hyundai')
    INSERT INTO Brand (brand_name) VALUES (N'Hyundai');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Mazda')
    INSERT INTO Brand (brand_name) VALUES (N'Mazda');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Ford')
    INSERT INTO Brand (brand_name) VALUES (N'Ford');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Audi')
    INSERT INTO Brand (brand_name) VALUES (N'Audi');

-- Kiểm tra brands đã insert
SELECT * FROM Brand;
GO

-- =============================================
-- 2. INSERT CARS
-- =============================================
DECLARE @toyotaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Toyota');
DECLARE @hondaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Honda');
DECLARE @mercedesId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Mercedes-Benz');
DECLARE @bmwId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'BMW');
DECLARE @hyundaiId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Hyundai');
DECLARE @mazdaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Mazda');
DECLARE @fordId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Ford');
DECLARE @audiId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Audi');

-- Toyota Camry 2.5Q
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @toyotaId,
           N'Camry 2.5Q',
           1235000000,
           'AVAILABLE',
           N'Sedan cao cấp với động cơ 2.5L mạnh mẽ, nội thất sang trọng, trang bị an toàn hiện đại TSS 2.0',
           2024,
           N'Trắng Ngọc Trai',
           5
       );

-- Toyota Vios 1.5G
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @toyotaId,
           N'Vios 1.5G',
           598000000,
           'AVAILABLE',
           N'Sedan cỡ B tiết kiệm nhiên liệu, phù hợp với gia đình trẻ, động cơ 1.5L bền bỉ',
           2024,
           N'Bạc',
           10
       );

-- Honda City RS
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @hondaId,
           N'City RS',
           569000000,
           'AVAILABLE',
           N'Sedan thể thao với thiết kế trẻ trung, Honda Sensing, động cơ 1.5L DOHC i-VTEC',
           2024,
           N'Đỏ',
           8
       );

-- Honda Civic Type R
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @hondaId,
           N'Civic Type R',
           2399000000,
           'AVAILABLE',
           N'Hot hatch hiệu suất cao 330 mã lực, động cơ 2.0L Turbo, hộp số sàn 6 cấp',
           2024,
           N'Đỏ Championship',
           2
       );

-- Mercedes-Benz C-Class C200
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @mercedesId,
           N'C-Class C200',
           1699000000,
           'AVAILABLE',
           N'Sedan hạng sang đẳng cấp Đức, MBUX thông minh, động cơ 1.5L mild hybrid',
           2024,
           N'Đen',
           3
       );

-- Mercedes-Benz E-Class E300
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @mercedesId,
           N'E-Class E300',
           2949000000,
           'AVAILABLE',
           N'Sedan sang trọng với công nghệ tiên tiến, hệ thống treo khí nén, nội thất da Nappa',
           2024,
           N'Xanh Cavansite',
           2
       );

-- BMW 320i M Sport
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @bmwId,
           N'320i M Sport',
           2199000000,
           'AVAILABLE',
           N'Sedan thể thao đậm chất BMW, gói M Sport, động cơ 2.0L TwinPower Turbo 184 mã lực',
           2024,
           N'Trắng Alpine',
           4
       );

-- BMW X5 xDrive40i
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @bmwId,
           N'X5 xDrive40i',
           4399000000,
           'AVAILABLE',
           N'SUV hạng sang 7 chỗ, động cơ 3.0L 6 xi-lanh thẳng hàng, công nghệ xDrive',
           2024,
           N'Đen Sapphire',
           2
       );

-- Hyundai Tucson 2.0 Đặc Biệt
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @hyundaiId,
           N'Tucson 2.0 Đặc Biệt',
           899000000,
           'AVAILABLE',
           N'SUV 5 chỗ với thiết kế Parametric Dynamics ấn tượng, SmartSense, động cơ 2.0L',
           2024,
           N'Đỏ',
           10
       );

-- Mazda CX-5 2.5 Signature Premium
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @mazdaId,
           N'CX-5 2.5 Signature Premium',
           1149000000,
           'AVAILABLE',
           N'SUV 5 chỗ thiết kế Kodo Soul of Motion, nội thất da Nappa, động cơ Skyactiv-G 2.5L',
           2024,
           N'Xám',
           6
       );

-- Ford Ranger Raptor
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @fordId,
           N'Ranger Raptor',
           1399000000,
           'AVAILABLE',
           N'Bán tải hiệu suất cao, động cơ twin-turbo V6 3.0L 397 mã lực, hệ thống treo FOX',
           2024,
           N'Cam',
           3
       );

-- Audi A4 45 TFSI quattro
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES (
           @audiId,
           N'A4 45 TFSI quattro',
           2099000000,
           'AVAILABLE',
           N'Sedan hạng D sang trọng, hệ dẫn động 4 bánh quattro, động cơ 2.0L TFSI 261 mã lực',
           2024,
           N'Xanh Dương',
           4
       );

-- Kiểm tra cars đã insert
SELECT c.car_id, b.brand_name, c.model, c.price, c.year, c.color, c.stock, c.status
FROM Car c
         JOIN Brand b ON c.brand_id = b.brand_id
ORDER BY c.car_id DESC;
GO

-- =============================================
-- 3. INSERT CAR IMAGES (Using Unsplash URLs)
-- =============================================
DECLARE @camryId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Camry 2.5Q' ORDER BY car_id DESC);
DECLARE @viosId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Vios 1.5G' ORDER BY car_id DESC);
DECLARE @cityId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'City RS' ORDER BY car_id DESC);
DECLARE @typeRId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Civic Type R' ORDER BY car_id DESC);
DECLARE @cClassId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'C-Class C200' ORDER BY car_id DESC);
DECLARE @eClassId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'E-Class E300' ORDER BY car_id DESC);
DECLARE @bmw320Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'320i M Sport' ORDER BY car_id DESC);
DECLARE @bmwX5Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'X5 xDrive40i' ORDER BY car_id DESC);
DECLARE @tucsonId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Tucson 2.0 Đặc Biệt' ORDER BY car_id DESC);
DECLARE @cx5Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'CX-5 2.5 Signature Premium' ORDER BY car_id DESC);
DECLARE @raptorId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Ranger Raptor' ORDER BY car_id DESC);
DECLARE @audiA4Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'A4 45 TFSI quattro' ORDER BY car_id DESC);

-- Toyota Camry Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@camryId, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800', 1),
                                                         (@camryId, 'https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=800', 0),
                                                         (@camryId, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800&q=90', 0);

-- Toyota Vios Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@viosId, 'https://images.unsplash.com/photo-1590362891991-f776e747a588?w=800', 1),
                                                         (@viosId, 'https://images.unsplash.com/photo-1590362891991-f776e747a588?w=800&q=90', 0);

-- Honda City RS Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cityId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800', 1),
                                                         (@cityId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&q=90', 0);

-- Honda Civic Type R Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@typeRId, 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=800', 1),
                                                         (@typeRId, 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=800&q=90', 0),
                                                         (@typeRId, 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800', 0);

-- Mercedes C-Class Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cClassId, 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800', 1),
                                                         (@cClassId, 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800&q=90', 0),
                                                         (@cClassId, 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800', 0);

-- Mercedes E-Class Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@eClassId, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=800', 1),
                                                         (@eClassId, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=800&q=90', 0);

-- BMW 320i M Sport Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800', 1),
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800&q=90', 0),
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1617469767053-d3b523a0b982?w=800', 0);

-- BMW X5 xDrive40i Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmwX5Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800', 1),
                                                         (@bmwX5Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&q=90', 0);

-- Hyundai Tucson Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800', 1),
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&q=90', 0),
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800', 0);

-- Mazda CX-5 Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cx5Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800', 1),
                                                         (@cx5Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&q=90', 0);

-- Ford Ranger Raptor Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@raptorId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800', 1),
                                                         (@raptorId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&q=90', 0);

-- Audi A4 Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800', 1),
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800&q=90', 0),
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1614200187524-dc4b892acf16?w=800', 0);

-- Kiểm tra images đã insert
SELECT
    ci.image_id,
    c.model,
    ci.image_url,
    ci.is_primary,
    ci.created_at
FROM CarImage ci
         JOIN Car c ON ci.car_id = c.car_id
ORDER BY c.car_id, ci.is_primary DESC, ci.image_id;
GO

-- =============================================
-- 4. CREATE ADMIN USER (Nếu chưa có)
-- =============================================
IF NOT EXISTS (SELECT 1 FROM AppUsers WHERE role = 'ADMIN')
BEGIN
INSERT INTO AppUsers (name, email, password_hash, role, phone, address) VALUES
    (N'Admin User', 'admin@carshowroom.com', 'hashed_password_here', 'ADMIN', '0123456789', N'Cần Thơ, Việt Nam');
END
GO

-- =============================================
-- 5. VERIFICATION - Kiểm tra kết quả
-- =============================================
PRINT '========================================';
PRINT '✅ DỮ LIỆU ĐÃ ĐƯỢC INSERT THÀNH CÔNG!';
PRINT '========================================';

-- Thống kê tổng quan
SELECT
    (SELECT COUNT(*) FROM Brand) AS TotalBrands,
    (SELECT COUNT(*) FROM Car) AS TotalCars,
    (SELECT COUNT(*) FROM CarImage) AS TotalImages,
    (SELECT COUNT(*) FROM Car WHERE status = 'AVAILABLE') AS AvailableCars;

-- Chi tiết xe và số lượng hình ảnh
SELECT
    b.brand_name AS N'Hãng xe',
    c.model AS N'Mẫu xe',
    c.year AS N'Năm',
    c.color AS N'Màu',
    FORMAT(c.price, 'N0') + ' VND' AS N'Giá',
    c.stock AS N'Tồn kho',
    c.status AS N'Trạng thái',
    COUNT(ci.image_id) AS N'Số hình ảnh'
FROM Car c
         JOIN Brand b ON c.brand_id = b.brand_id
         LEFT JOIN CarImage ci ON c.car_id = ci.car_id
GROUP BY b.brand_name, c.model, c.year, c.color, c.price, c.stock, c.status
ORDER BY b.brand_name, c.model;

-- Hiển thị sample images
SELECT TOP 5
    b.brand_name + ' ' + c.model AS N'Xe',
    ci.image_url AS N'URL Hình ảnh',
    CASE WHEN ci.is_primary = 1 THEN N'Chính' ELSE N'Phụ' END AS N'Loại'
FROM CarImage ci
         JOIN Car c ON ci.car_id = c.car_id
         JOIN Brand b ON c.brand_id = b.brand_id
ORDER BY ci.car_id, ci.is_primary DESC;
GO

-- =============================================
-- 4. INSERT PROMOTIONS
-- =============================================
PRINT '';
PRINT 'BƯỚC 4: Insert Promotions...';

-- Xóa dữ liệu cũ
DELETE FROM UserPromotion;
DELETE FROM CarPromotion;
DELETE FROM Promotion;

-- Promotion 1: Ưu Đãi Học Sinh - Sinh Viên
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage, discount_amount)
VALUES (
           N'Ưu Đãi Học Sinh - Sinh Viên Mua Xe',
           N'Car Showroom đồng hành cùng thế hệ trẻ. Giảm ngay 50 triệu đồng khi xuất trình thẻ sinh viên còn hiệu lực. Hỗ trợ vay 90% giá trị xe, không cần chứng minh thu nhập (bảo lãnh bởi phụ huynh). Lãi suất ưu đãi 7.5%/năm, ân hạn nợ gốc 12 tháng đầu. Sinh viên xuất sắc có GPA từ 3.5 trở lên sẽ được giảm thêm 20 triệu đồng. Tặng thêm bộ phụ kiện thể thao trị giá 15 triệu đồng.',
           '2025-01-01',
           '2025-12-31',
           0,
           0
       );

-- Promotion 2: Khuyến Mãi Tết
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage, discount_amount)
VALUES (
           N'Khuyến Mãi Tết Nguyên Đán 2025',
           N'Mừng xuân Ất Tỵ, giảm giá đến 15% cho tất cả các dòng xe. Tặng kèm gói bảo hiểm VIP trị giá 30 triệu đồng. Miễn phí bảo dưỡng 2 năm đầu. Hỗ trợ trả góp lãi suất 0% trong 6 tháng đầu tiên. Tặng thêm phụ kiện cao cấp trị giá 25 triệu đồng bao gồm: camera hành trình, cảm biến lùi, thảm lót sàn 3D.',
           '2025-01-15',
           '2025-02-28',
           15.00,
           0
       );

-- Promotion 3: Ưu Đãi Cuối Năm
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage, discount_amount)
VALUES (
           N'Ưu Đãi Mua Xe Cuối Năm',
           N'Giảm ngay 50 triệu đồng cho khách hàng mua xe trong tháng 12. Tặng kèm phụ kiện cao cấp trị giá 20 triệu. Hỗ trợ trả góp 0% lãi suất trong 12 tháng đầu. Bảo hành mở rộng 5 năm hoặc 100.000km. Miễn phí đăng ký và đăng kiểm lần đầu. Tặng thêm 1 năm bảo hiểm thân vỏ.',
           '2024-12-01',
           '2024-12-31',
           0,
           0
       );

-- Promotion 4: Flash Sale
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage, discount_amount)
VALUES (
           N'Flash Sale Cuối Tuần',
           N'Giảm sốc 10% cho tất cả các dòng xe chỉ trong 3 ngày cuối tuần. Áp dụng từ thứ 6 đến chủ nhật hàng tuần. Số lượng có hạn, khách hàng đặt cọc trước sẽ được ưu tiên. Tặng kèm bộ phụ kiện thể thao trị giá 10 triệu đồng. Miễn phí phủ ceramic 1 lần.',
           '2025-10-01',
           '2025-10-31',
           10.00,
           0
       );

-- Promotion 5: Tri Ân
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage, discount_amount)
VALUES (
           N'Tri Ân Khách Hàng Thân Thiết',
           N'Dành riêng cho khách hàng đã mua xe tại showroom. Giảm giá đặc biệt khi giới thiệu bạn bè mua xe. Tặng voucher bảo dưỡng miễn phí trị giá 5 triệu đồng. Ưu đãi mua phụ kiện giảm đến 30%. Hỗ trợ thu cũ đổi mới với giá cao nhất thị trường.',
           '2025-01-01',
           '2025-12-31',
           5.00,
           0
       );

PRINT '✅ Đã insert 5 promotions';
GO

-- =============================================
-- 5. INSERT CAR PROMOTIONS (với discount riêng)
-- =============================================
PRINT '';
PRINT 'BƯỚC 5: Insert Car Promotions...';

DECLARE @promo1Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Học Sinh%');
DECLARE @promo2Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Tết%');
DECLARE @promo3Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Cuối Năm%');
DECLARE @promo4Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Flash Sale%');
DECLARE @promo5Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Tri Ân%');

-- Promotion 1: Ưu Đãi Học Sinh (Xe giá rẻ - trung bình)
INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo1Id, 8.00, 0 FROM Car WHERE model = N'Vios 1.5G';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo1Id, 0, 30000000 FROM Car WHERE model = N'City RS';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo1Id, 7.00, 0 FROM Car WHERE model LIKE N'%Tucson%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo1Id, 0, 40000000 FROM Car WHERE model LIKE N'%CX-5%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo1Id, 6.00, 0 FROM Car WHERE model LIKE N'%Camry%';

PRINT '  ✓ Promotion 1: 5 xe';

-- Promotion 2: Tết (TẤT CẢ XE giảm 15%)
INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo2Id, 15.00, 0 FROM Car;

PRINT '  ✓ Promotion 2: Tất cả xe (15%)';

-- Promotion 3: Cuối Năm (Xe cao cấp)
INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 0, 80000000 FROM Car WHERE model LIKE N'%C-Class%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 0, 150000000 FROM Car WHERE model LIKE N'%E-Class%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 18.00, 0 FROM Car WHERE model LIKE N'%320i%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 20.00, 0 FROM Car WHERE model LIKE N'%X5%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 0, 120000000 FROM Car WHERE model LIKE N'%A4%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo3Id, 15.00, 0 FROM Car WHERE model LIKE N'%Type R%';

PRINT '  ✓ Promotion 3: 6 xe cao cấp';

-- Promotion 4: Flash Sale (Một số xe hot)
INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo4Id, 10.00, 0 FROM Car WHERE model LIKE N'%Camry%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo4Id, 10.00, 0 FROM Car WHERE model = N'City RS';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo4Id, 10.00, 0 FROM Car WHERE model LIKE N'%CX-5%';

INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo4Id, 10.00, 0 FROM Car WHERE model LIKE N'%320i%';

PRINT '  ✓ Promotion 4: 4 xe hot (10%)';

-- Promotion 5: Tri Ân (Tất cả xe giảm 5%)
INSERT INTO CarPromotion (car_id, promotion_id, discount_percentage, discount_amount)
SELECT car_id, @promo5Id, 5.00, 0 FROM Car;

PRINT '  ✓ Promotion 5: Tất cả xe (5%)';
PRINT '✅ Đã insert car promotions';
GO
-- =============================================
-- INSERT BLOGS
-- =============================================
PRINT '';
PRINT '========================================';
PRINT 'ĐANG INSERT BLOGS...';
PRINT '========================================';

-- Lấy admin user ID (sử dụng admin có sẵn)
DECLARE @adminId INT = (SELECT TOP 1 user_id FROM AppUsers WHERE role = 'ADMIN' ORDER BY user_id);

IF @adminId IS NULL
BEGIN
    PRINT '❌ KHÔNG TÌM THẤY ADMIN USER!';
    PRINT 'Vui lòng tạo admin user trước khi chạy script này.';
    RETURN;
END

PRINT '  ℹ️ Sử dụng admin user ID: ' + CAST(@adminId AS VARCHAR);

-- Xóa blogs cũ nếu có
DELETE FROM Blog WHERE author_id = @adminId;

-- Blog 1: Hướng dẫn chọn xe
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'5 Tiêu Chí Quan Trọng Khi Chọn Mua Xe Ô Tô',
           N'<h2>Giới thiệu</h2>
       <p>Mua xe ô tô là một quyết định quan trọng trong cuộc sống. Để chọn được chiếc xe phù hợp, bạn cần cân nhắc nhiều yếu tố khác nhau.</p>

       <h3>1. Ngân sách và khả năng tài chính</h3>
       <p>Đây là yếu tố quan trọng nhất. Bạn cần xác định rõ:</p>
       <ul>
       <li>Tổng số tiền có thể chi trả</li>
       <li>Hình thức thanh toán: Trả thẳng hay trả góp</li>
       <li>Chi phí nuôi xe hàng tháng (xăng, bảo dưỡng, bảo hiểm)</li>
       </ul>

       <h3>2. Nhu cầu sử dụng</h3>
       <p>Cần xác định rõ mục đích sử dụng xe:</p>
       <ul>
       <li>Đi làm trong thành phố: Nên chọn xe sedan cỡ nhỏ hoặc hatchback</li>
       <li>Gia đình đông người: SUV 7 chỗ là lựa chọn phù hợp</li>
       <li>Kinh doanh, công việc: Chọn xe pickup hoặc bán tải</li>
       </ul>

       <h3>3. Thương hiệu và độ tin cậy</h3>
       <p>Một số thương hiệu uy tín tại Việt Nam:</p>
       <ul>
       <li>Toyota, Honda: Bền bỉ, giá trị bán lại cao</li>
       <li>Mercedes-Benz, BMW: Đẳng cấp, công nghệ cao</li>
       <li>Hyundai, Mazda: Thiết kế đẹp, giá cả hợp lý</li>
       </ul>

       <h3>4. Tính năng an toàn</h3>
       <p>Các tính năng an toàn cần có:</p>
       <ul>
       <li>Túi khí (ít nhất 6 túi)</li>
       <li>Hệ thống chống bó cứng phanh ABS, EBD</li>
       <li>Cảm biến lùi, camera 360</li>
       <li>Hệ thống cảnh báo điểm mù</li>
       </ul>

       <h3>5. Chi phí bảo dưỡng</h3>
       <p>Cần tìm hiểu:</p>
       <ul>
       <li>Chi phí bảo dưỡng định kỳ</li>
       <li>Giá phụ tùng thay thế</li>
       <li>Mức tiêu hao nhiên liệu</li>
       <li>Chế độ bảo hành của hãng</li>
       </ul>

       <h2>Kết luận</h2>
       <p>Việc chọn mua xe cần được cân nhắc kỹ lưỡng. Hãy đến showroom của chúng tôi để được tư vấn chi tiết và trải nghiệm lái thử miễn phí!</p>',
           @adminId,
           GETDATE()
       );

-- Blog 2: So sánh sedan vs SUV
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'Sedan Hay SUV - Nên Chọn Loại Xe Nào Cho Gia Đình?',
           N'<h2>Giới thiệu</h2>
       <p>Sedan và SUV là hai dòng xe phổ biến nhất tại Việt Nam. Mỗi loại xe có những ưu điểm riêng phù hợp với nhu cầu khác nhau.</p>

       <h3>Ưu điểm của Sedan</h3>
       <h4>1. Tiết kiệm nhiên liệu</h4>
       <p>Sedan có trọng lượng nhẹ hơn SUV nên tiêu hao nhiên liệu thấp hơn khoảng 20-30%.</p>

       <h4>2. Vận hành êm ái</h4>
       <p>Thiết kế thấp, trọng tâm thấp giúp sedan vận hành ổn định, êm ái hơn.</p>

       <h4>3. Giá thành hợp lý</h4>
       <p>Với cùng phân khúc, sedan thường rẻ hơn SUV 100-200 triệu đồng.</p>

       <h3>Ưu điểm của SUV</h3>
       <h4>1. Không gian rộng rãi</h4>
       <p>SUV có không gian nội thất và khoang hành lý lớn hơn, phù hợp gia đình đông người.</p>

       <h4>2. Tầm nhìn tốt</h4>
       <p>Vị trí lái cao giúp tài xế có tầm nhìn tốt hơn, lái xe an toàn hơn.</p>

       <h4>3. Khả năng vượt địa hình</h4>
       <p>Gầm cao, hệ dẫn động 4 bánh giúp SUV vượt địa hình tốt hơn sedan.</p>

       <h3>Nên chọn loại xe nào?</h3>
       <h4>Chọn Sedan nếu:</h4>
       <ul>
       <li>Sử dụng chủ yếu trong thành phố</li>
       <li>Gia đình 2-4 người</li>
       <li>Ưu tiên tiết kiệm nhiên liệu</li>
       <li>Ngân sách hạn chế</li>
       </ul>

       <h4>Chọn SUV nếu:</h4>
       <ul>
       <li>Gia đình đông người (5-7 người)</li>
       <li>Thường xuyên đi xa, du lịch</li>
       <li>Cần chở nhiều hành lý</li>
       <li>Đi địa hình phức tạp</li>
       </ul>

       <h2>Kết luận</h2>
       <p>Cả sedan và SUV đều có ưu điểm riêng. Hãy đến showroom để được tư vấn và trải nghiệm thực tế!</p>',
           @adminId,
           DATEADD(day, -7, GETDATE())
       );

-- Blog 3: Bảo dưỡng xe
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'Hướng Dẫn Bảo Dưỡng Xe Ô Tô Định Kỳ',
           N'<h2>Tầm quan trọng của bảo dưỡng định kỳ</h2>
       <p>Bảo dưỡng định kỳ giúp xe hoạt động ổn định, kéo dài tuổi thọ và đảm bảo an toàn khi lưu thông.</p>

       <h3>Lịch bảo dưỡng theo km</h3>
       <h4>5.000 - 10.000 km đầu tiên</h4>
       <ul>
       <li>Thay dầu máy và lọc dầu</li>
       <li>Kiểm tra phanh</li>
       <li>Kiểm tra áp suất lốp</li>
       <li>Vệ sinh điều hòa</li>
       </ul>

       <h4>20.000 km</h4>
       <ul>
       <li>Thay dầu máy và lọc dầu</li>
       <li>Thay lọc gió động cơ</li>
       <li>Thay lọc điều hòa</li>
       <li>Kiểm tra hệ thống phanh</li>
       </ul>

       <h4>40.000 km</h4>
       <ul>
       <li>Thay dầu hộp số</li>
       <li>Thay dầu phanh</li>
       <li>Kiểm tra hệ thống treo</li>
       <li>Cân bằng và định vị bánh xe</li>
       </ul>

       <h3>Bảo dưỡng theo thời gian</h3>
       <h4>Hàng tháng</h4>
       <ul>
       <li>Kiểm tra áp suất lốp</li>
       <li>Rửa xe, vệ sinh nội thất</li>
       <li>Kiểm tra đèn chiếu sáng</li>
       </ul>

       <h4>Mỗi 6 tháng</h4>
       <ul>
       <li>Thay dầu máy (nếu chạy ít)</li>
       <li>Kiểm tra ắc quy</li>
       <li>Vệ sinh buồng đốt</li>
       </ul>

       <h3>Lưu ý quan trọng</h3>
       <ol>
       <li>Sử dụng phụ tùng chính hãng</li>
       <li>Bảo dưỡng tại garage uy tín</li>
       <li>Ghi chép lịch bảo dưỡng</li>
       <li>Không bỏ qua các dấu hiệu bất thường</li>
       </ol>

       <h2>Khuyến mãi đặc biệt</h2>
       <p>Showroom chúng tôi đang có chương trình MIỄN PHÍ kiểm tra tổng quát và giảm 20% chi phí bảo dưỡng định kỳ. Liên hệ ngay để đặt lịch!</p>',
           @adminId,
           DATEADD(day, -14, GETDATE())
       );

-- Blog 4: Xe điện
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'Xe Điện - Xu Hướng Tất Yếu Của Tương Lai',
           N'<h2>Tại sao xe điện đang phát triển mạnh?</h2>
       <p>Xe điện đang trở thành xu hướng toàn cầu nhờ nhiều ưu điểm vượt trội về môi trường, chi phí vận hành và công nghệ.</p>

       <h3>Ưu điểm của xe điện</h3>
       <h4>1. Thân thiện môi trường</h4>
       <ul>
       <li>Không phát thải khí CO2</li>
       <li>Không gây ô nhiễm tiếng ồn</li>
       <li>Giảm hiệu ứng nhà kính</li>
       </ul>

       <h4>2. Tiết kiệm chi phí</h4>
       <ul>
       <li>Chi phí sạc điện chỉ bằng 1/3 chi phí xăng</li>
       <li>Ít chi tiết cần bảo dưỡng hơn</li>
       <li>Tuổi thọ động cơ điện cao hơn</li>
       </ul>

       <h4>3. Vận hành êm ái</h4>
       <ul>
       <li>Động cơ điện hoạt động êm</li>
       <li>Tăng tốc mượt mà</li>
       <li>Ít rung động</li>
       </ul>

       <h3>Thách thức của xe điện tại Việt Nam</h3>
       <h4>1. Hạ tầng sạc</h4>
       <p>Hệ thống trạm sạc còn hạn chế, chưa đáp ứng nhu cầu.</p>

       <h4>2. Giá thành</h4>
       <p>Xe điện hiện vẫn đắt hơn xe xăng cùng phân khúc 20-30%.</p>

       <h4>3. Thời gian sạc</h4>
       <p>Sạc nhanh mất 30-60 phút, sạc chậm mất 6-8 giờ.</p>

       <h3>Dự báo thị trường</h3>
       <p>Theo các chuyên gia, đến năm 2030:</p>
       <ul>
       <li>50% xe bán ra sẽ là xe điện</li>
       <li>Hạ tầng sạc sẽ phát triển mạnh</li>
       <li>Giá xe điện sẽ ngang bằng xe xăng</li>
       <li>Pin sẽ có dung lượng cao hơn, sạc nhanh hơn</li>
       </ul>

       <h2>Kết luận</h2>
       <p>Xe điện là tương lai không thể tránh khỏi. Hãy theo dõi showroom để cập nhật những mẫu xe điện mới nhất!</p>',
           @adminId,
           DATEADD(day, -21, GETDATE())
       );

-- Blog 5: Mẹo lái xe an toàn
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'10 Mẹo Lái Xe An Toàn Trong Thành Phố',
           N'<h2>An toàn là ưu tiên hàng đầu</h2>
       <p>Lái xe an toàn không chỉ bảo vệ bản thân mà còn bảo vệ người xung quanh. Dưới đây là 10 mẹo quan trọng.</p>

       <h3>1. Luôn thắt dây an toàn</h3>
       <p>Dây an toàn giảm 50% nguy cơ tử vong trong tai nạn. Hãy thắt dây ngay cả khi đi quãng đường ngắn.</p>

       <h3>2. Giữ khoảng cách an toàn</h3>
       <p>Quy tắc 3 giây: Khi xe trước qua một điểm, đếm 3 giây rồi xe bạn mới đến điểm đó.</p>

       <h3>3. Không sử dụng điện thoại</h3>
       <p>Nghe gọi khi lái xe làm giảm 40% khả năng phản ứng. Hãy dừng xe nếu cần gọi điện.</p>

       <h3>4. Quan sát kỹ điểm mù</h3>
       <p>Luôn quay đầu kiểm tra điểm mù trước khi chuyển làn, đặc biệt với xe máy.</p>

       <h3>5. Tuân thủ tốc độ</h3>
       <ul>
       <li>Trong khu dân cư: 40-50 km/h</li>
       <li>Đường phố thành phố: 60 km/h</li>
       <li>Đường cao tốc: 80-120 km/h</li>
       </ul>

       <h3>6. Bật đèn khi trời tối</h3>
       <p>Bật đèn không chỉ để bạn nhìn rõ mà còn để người khác thấy bạn.</p>

       <h3>7. Kiểm tra xe định kỳ</h3>
       <ul>
       <li>Áp suất lốp: mỗi tuần</li>
       <li>Dầu máy: mỗi tháng</li>
       <li>Phanh: khi có dấu hiệu bất thường</li>
       </ul>

       <h3>8. Không lái xe khi mệt</h3>
       <p>Buồn ngủ khi lái xe nguy hiểm như uống rượu. Hãy nghỉ ngơi 15-20 phút mỗi 2 giờ lái xe.</p>

       <h3>9. Nhường đường xe ưu tiên</h3>
       <p>Xe cứu thương, cứu hỏa, cảnh sát luôn được ưu tiên. Hãy tránh sang bên và giảm tốc độ.</p>

       <h3>10. Lùi xe cẩn thận</h3>
       <p>Kiểm tra xung quanh trước khi lùi. Trẻ em và vật cản nhỏ có thể không nhìn thấy qua gương.</p>

       <h2>Trang bị an toàn nên có</h2>
       <ul>
       <li>Túi khí: Càng nhiều càng tốt (ít nhất 6 túi)</li>
       <li>Camera 360: Giúp quan sát toàn cảnh</li>
       <li>Cảm biến va chạm: Cảnh báo chướng ngại vật</li>
       <li>Hệ thống cân bằng điện tử ESP</li>
       <li>Cảnh báo điểm mù BSM</li>
       </ul>

       <h2>Kết luận</h2>
       <p>An toàn giao thông bắt đầu từ ý thức của mỗi người. Hãy lái xe văn minh, an toàn vì bản thân và cộng đồng!</p>

       <p><strong>Showroom chúng tôi tổ chức MIỄN PHÍ khóa học lái xe an toàn cho khách hàng mua xe. Liên hệ ngay để đăng ký!</strong></p>',
           @adminId,
           DATEADD(day, -28, GETDATE())
       );

-- Blog 6: Tư vấn tài chính mua xe trả góp
INSERT INTO Blog (title, content, author_id, created_at)
VALUES (
           N'Hướng Dẫn Vay Mua Xe Trả Góp - Những Điều Cần Biết',
           N'<h2>Giới thiệu</h2>
       <p>Vay mua xe trả góp là giải pháp tài chính thông minh giúp bạn sở hữu xe mơ ước mà không cần trả hết một lần.</p>

       <h3>Các hình thức vay mua xe</h3>
       <h4>1. Vay qua ngân hàng</h4>
       <p><strong>Ưu điểm:</strong></p>
       <ul>
       <li>Lãi suất thấp (7-9%/năm)</li>
       <li>Thời gian vay dài (5-7 năm)</li>
       <li>Vay được mức cao (80-90% giá trị xe)</li>
       </ul>
       <p><strong>Nhược điểm:</strong></p>
       <ul>
       <li>Thủ tục phức tạp</li>
       <li>Cần chứng minh thu nhập</li>
       <li>Thời gian duyệt lâu (5-7 ngày)</li>
       </ul>

       <h4>2. Vay qua công ty tài chính</h4>
       <p><strong>Ưu điểm:</strong></p>
       <ul>
       <li>Thủ tục đơn giản</li>
       <li>Duyệt nhanh (1-2 ngày)</li>
       <li>Điều kiện dễ dàng hơn</li>
       </ul>
       <p><strong>Nhược điểm:</strong></p>
       <ul>
       <li>Lãi suất cao hơn (9-13%/năm)</li>
       <li>Thời gian vay ngắn hơn (3-5 năm)</li>
       <li>Vay được ít hơn (70-80%)</li>
       </ul>

       <h3>Điều kiện vay mua xe</h3>
       <h4>Điều kiện chung:</h4>
       <ul>
       <li>Công dân Việt Nam từ 20-65 tuổi</li>
       <li>Có thu nhập ổn định</li>
       <li>Không nợ xấu tại các tổ chức tín dụng</li>
       </ul>

       <h4>Hồ sơ cần thiết:</h4>
       <ol>
       <li>CMND/CCCD (bản sao công chứng)</li>
       <li>Hộ khẩu (bản sao)</li>
       <li>Giấy xác nhận thu nhập</li>
       <li>Sao kê tài khoản 6 tháng gần nhất</li>
       <li>Hợp đồng lao động (với ngân hàng)</li>
       </ol>

       <h3>Cách tính lãi suất và khoản vay</h3>
       <h4>Ví dụ cụ thể:</h4>
       <p>Mua xe Toyota Camry giá 1,235,000,000 VNĐ</p>
       <ul>
       <li>Trả trước 30%: 370,500,000 VNĐ</li>
       <li>Vay 70%: 864,500,000 VNĐ</li>
       <li>Lãi suất: 8.5%/năm</li>
       <li>Thời gian: 5 năm (60 tháng)</li>
       </ul>

       <p><strong>Số tiền phải trả mỗi tháng: ~17,800,000 VNĐ</strong></p>
       <p><strong>Tổng lãi phải trả: ~203,500,000 VNĐ</strong></p>

       <h3>Lưu ý quan trọng</h3>
       <ol>
       <li><strong>Đánh giá khả năng tài chính:</strong> Khoản trả góp không nên vượt quá 40% thu nhập hàng tháng</li>
       <li><strong>So sánh lãi suất:</strong> Hỏi ít nhất 3 ngân hàng/công ty tài chính</li>
       <li><strong>Đọc kỹ hợp đồng:</strong> Chú ý điều khoản phạt trả nợ trước hạn</li>
       <li><strong>Chi phí phát sinh:</strong> Phí thẩm định, phí giải ngân, bảo hiểm</li>
       <li><strong>Bảo hiểm:</strong> Phải mua bảo hiểm vật chất xe trong thời gian vay</li>
       </ol>

       <h3>Gói hỗ trợ tài chính của Showroom</h3>
       <p>Chúng tôi hợp tác với các ngân hàng/công ty tài chính uy tín:</p>
       <ul>
       <li>Lãi suất ưu đãi từ 7.5%/năm</li>
       <li>Vay lên đến 90% giá trị xe</li>
       <li>Thủ tục nhanh chóng, duyệt trong 24h</li>
       <li>Tư vấn miễn phí, hỗ trợ làm hồ sơ</li>
       <li>Nhiều chương trình 0% lãi suất đặc biệt</li>
       </ul>

       <h2>Kết luận</h2>
       <p>Vay mua xe trả góp là giải pháp thông minh nếu bạn biết cách tính toán. Hãy đến showroom để được tư vấn chi tiết về các gói vay ưu đãi!</p>

       <p><strong>Hotline tư vấn: 1900-xxxx (miễn phí)</strong></p>',
           @adminId,
           DATEADD(day, -35, GETDATE())
       );

PRINT '✅ Đã insert 6 blogs';

-- Thống kê
SELECT
    blog_id,
    title,
    author_id,
    created_at,
    LEN(content) AS content_length
FROM Blog
ORDER BY created_at DESC;

PRINT '';
PRINT '========================================';
PRINT 'HOÀN TẤT INSERT BLOGS!';
PRINT '========================================';
      -- =============================================
-- 6. UPDATE ADMIN PASSWORD & INSERT SAMPLE USERS
-- =============================================
PRINT '';
PRINT '========================================';
PRINT 'BỔ SUNG NGƯỜI DÙNG MẪU';
PRINT '========================================';

UPDATE AppUsers
SET password_hash = '$2a$12$6.ZxYYwbV9MrbjKAsMhT1eqBhCBSdnNTK49GMQ7grjDZkFqDoYiqO'
WHERE role = 'ADMIN';

PRINT 'Đã cập nhật lại mật khẩu admin';

-- =============================================
-- 5 NHÂN VIÊN (STAFF)
-- =============================================
INSERT INTO AppUsers (name, email, password_hash, role, phone, address) VALUES
                                                                            (N'Nguyễn Minh Quân', 'quan.staff@carshowroom.com', 'staff1', 'STAFF', '0901122334', N'Hà Nội'),
                                                                            (N'Lê Thị Lan', 'lan.staff@carshowroom.com', 'staff2', 'STAFF', '0912233445', N'Hồ Chí Minh'),
                                                                            (N'Trần Đức Thắng', 'thang.staff@carshowroom.com', 'staff3', 'STAFF', '0933344556', N'Đà Nẵng'),
                                                                            (N'Phạm Hồng Nhung', 'nhung.staff@carshowroom.com', 'staff4', 'STAFF', '0944455667', N'Cần Thơ'),
                                                                            (N'Võ Anh Tuấn', 'tuan.staff@carshowroom.com', 'staff5', 'STAFF', '0955566778', N'Hải Phòng');

PRINT 'Đã thêm 5 nhân viên mẫu';

-- =============================================
-- 5 KHÁCH HÀNG (CUSTOMER)
-- =============================================
INSERT INTO AppUsers (name, email, password_hash, role, phone, address) VALUES
                                                                            (N'Nguyễn Văn Nam', 'nam.customer@carshowroom.com', 'customer1', 'CUSTOMER', '0977788899', N'Hà Nội'),
                                                                            (N'Trần Thị Hạnh', 'hanh.customer@carshowroom.com', 'customer2', 'CUSTOMER', '0988899000', N'Hồ Chí Minh'),
                                                                            (N'Lê Quốc Bảo', 'bao.customer@carshowroom.com', 'customer3', 'CUSTOMER', '0909090909', N'Đà Nẵng'),
                                                                            (N'Phạm Thu Trang', 'trang.customer@carshowroom.com', 'customer4', 'CUSTOMER', '0919191919', N'Cần Thơ'),
                                                                            (N'Vũ Hoàng Dương', 'duong.customer@carshowroom.com', 'customer5', 'CUSTOMER', '0929292929', N'Nha Trang');

PRINT 'Đã thêm 5 khách hàng mẫu';

-- Kiểm tra kết quả
SELECT user_id, name, email, role, phone, address
FROM AppUsers
ORDER BY
    CASE WHEN role = 'ADMIN' THEN 1
         WHEN role = 'STAFF' THEN 2
         WHEN role = 'CUSTOMER' THEN 3
         ELSE 4 END,
    user_id;

PRINT '========================================';
PRINT 'ĐÃ HOÀN TẤT THÊM NGƯỜI DÙNG MẪU';
PRINT '========================================';
GO
