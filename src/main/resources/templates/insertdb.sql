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