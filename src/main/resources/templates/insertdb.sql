-- =============================================
-- INSERT DATA FOR CAR SALES WEBSITE
-- Compatible with NEW Simplified Database Schema
-- =============================================

--USE TestDB;
--GO

PRINT '========================================';
PRINT 'BẮT ĐẦU INSERT DỮ LIỆU';
PRINT '========================================';

-- =============================================
-- 1. INSERT BRANDS
-- =============================================
PRINT '';
PRINT 'BƯỚC 1: Insert Brands...';

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

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Kia')
    INSERT INTO Brand (brand_name) VALUES (N'Kia');

IF NOT EXISTS (SELECT 1 FROM Brand WHERE brand_name = N'Volkswagen')
    INSERT INTO Brand (brand_name) VALUES (N'Volkswagen');

PRINT '✅ Đã insert 10 brands';

-- Kiểm tra brands
SELECT brand_id, brand_name FROM Brand ORDER BY brand_name;
GO

-- =============================================
-- 2. INSERT CARS
-- =============================================
PRINT '';
PRINT 'BƯỚC 2: Insert Cars...';

-- Lấy brand IDs
DECLARE @toyotaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Toyota');
DECLARE @hondaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Honda');
DECLARE @mercedesId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Mercedes-Benz');
DECLARE @bmwId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'BMW');
DECLARE @hyundaiId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Hyundai');
DECLARE @mazdaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Mazda');
DECLARE @fordId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Ford');
DECLARE @audiId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Audi');
DECLARE @kiaId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Kia');
DECLARE @vwId INT = (SELECT brand_id FROM Brand WHERE brand_name = N'Volkswagen');

-- Toyota Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@toyotaId, N'Camry 2.5Q', 1235000000, 'AVAILABLE',
     N'Sedan cao cấp với động cơ 2.5L mạnh mẽ, nội thất sang trọng, trang bị an toàn hiện đại TSS 2.0',
     2024, N'Trắng Ngọc Trai', 5),

    (@toyotaId, N'Vios 1.5G', 598000000, 'AVAILABLE',
     N'Sedan cỡ B tiết kiệm nhiên liệu, phù hợp với gia đình trẻ, động cơ 1.5L bền bỉ',
     2024, N'Bạc', 10),

    (@toyotaId, N'Corolla Cross 1.8V', 820000000, 'AVAILABLE',
     N'SUV cỡ B thời thượng, động cơ Dual VVT-i 1.8L, Toyota Safety Sense, thiết kế thể thao',
     2024, N'Đỏ', 8),

    (@toyotaId, N'Veloz Cross 1.5', 658000000, 'AVAILABLE',
     N'MPV 7 chỗ đa dụng, động cơ 1.5L tiết kiệm, không gian rộng rãi, phù hợp gia đình',
     2024, N'Xám', 12);

-- Honda Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@hondaId, N'City RS', 569000000, 'AVAILABLE',
     N'Sedan thể thao với thiết kế trẻ trung, Honda Sensing, động cơ 1.5L DOHC i-VTEC',
     2024, N'Đỏ', 8),

    (@hondaId, N'Civic Type R', 2399000000, 'AVAILABLE',
     N'Hot hatch hiệu suất cao 330 mã lực, động cơ 2.0L Turbo, hộp số sàn 6 cấp',
     2024, N'Đỏ Championship', 2),

    (@hondaId, N'CR-V 1.5L Turbo', 1029000000, 'AVAILABLE',
     N'SUV 5 chỗ cao cấp, động cơ 1.5L Turbo, Honda Sensing, nội thất da cao cấp',
     2024, N'Trắng', 6),

    (@hondaId, N'Accord 1.5L Turbo', 1319000000, 'AVAILABLE',
     N'Sedan hạng D sang trọng, động cơ 1.5L Turbo 190PS, công nghệ Honda Sensing',
     2024, N'Đen', 4);

-- Mercedes-Benz Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@mercedesId, N'C-Class C200', 1699000000, 'AVAILABLE',
     N'Sedan hạng sang đẳng cấp Đức, MBUX thông minh, động cơ 1.5L mild hybrid',
     2024, N'Đen', 3),

    (@mercedesId, N'E-Class E300', 2949000000, 'AVAILABLE',
     N'Sedan sang trọng với công nghệ tiên tiến, hệ thống treo khí nén, nội thất da Nappa',
     2024, N'Xanh Cavansite', 2),

    (@mercedesId, N'GLC 300 4MATIC', 2799000000, 'AVAILABLE',
     N'SUV hạng sang 5 chỗ, động cơ 2.0L Turbo, hệ dẫn động 4 bánh toàn thời gian',
     2024, N'Trắng', 3),

    (@mercedesId, N'S-Class S500', 6799000000, 'AVAILABLE',
     N'Sedan hạng sang đỉnh cao, động cơ V6 3.0L, công nghệ tự lái Level 3',
     2024, N'Đen Obsidian', 1);

-- BMW Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@bmwId, N'320i M Sport', 2199000000, 'AVAILABLE',
     N'Sedan thể thao đậm chất BMW, gói M Sport, động cơ 2.0L TwinPower Turbo 184 mã lực',
     2024, N'Trắng Alpine', 4),

    (@bmwId, N'X5 xDrive40i', 4399000000, 'AVAILABLE',
     N'SUV hạng sang 7 chỗ, động cơ 3.0L 6 xi-lanh thẳng hàng, công nghệ xDrive',
     2024, N'Đen Sapphire', 2),

    (@bmwId, N'530i M Sport', 3239000000, 'AVAILABLE',
     N'Sedan hạng sang cỡ E, động cơ 2.0L TwinPower Turbo, nội thất Vernasca',
     2024, N'Xanh Dương', 3),

    (@bmwId, N'X3 xDrive30i', 2799000000, 'AVAILABLE',
     N'SUV hạng sang cỡ trung, động cơ 2.0L Turbo, hệ dẫn động 4 bánh xDrive',
     2024, N'Xám', 4);

-- Hyundai Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@hyundaiId, N'Tucson 2.0 Đặc Biệt', 899000000, 'AVAILABLE',
     N'SUV 5 chỗ với thiết kế Parametric Dynamics ấn tượng, SmartSense, động cơ 2.0L',
     2024, N'Đỏ', 10),

    (@hyundaiId, N'Santa Fe 2.5 Turbo', 1340000000, 'AVAILABLE',
     N'SUV 7 chỗ cao cấp, động cơ 2.5L Turbo 281PS, hệ thống an toàn SmartSense',
     2024, N'Trắng', 6),

    (@hyundaiId, N'Accent 1.5 AT Đặc Biệt', 569000000, 'AVAILABLE',
     N'Sedan cỡ B hiện đại, động cơ 1.5L tiết kiệm, thiết kế trẻ trung',
     2024, N'Bạc', 15),

    (@hyundaiId, N'Elantra 2.0 AT', 729000000, 'AVAILABLE',
     N'Sedan cỡ C thể thao, động cơ 2.0L Nu Smartstream, thiết kế Parametric',
     2024, N'Xanh', 8);

-- Mazda Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@mazdaId, N'CX-5 2.5 Signature Premium', 1149000000, 'AVAILABLE',
     N'SUV 5 chỗ thiết kế Kodo Soul of Motion, nội thất da Nappa, động cơ Skyactiv-G 2.5L',
     2024, N'Xám', 6),

    (@mazdaId, N'Mazda3 2.0 Luxury', 799000000, 'AVAILABLE',
     N'Sedan cỡ C cao cấp, động cơ Skyactiv-G 2.0L, nội thất da, i-Activsense',
     2024, N'Đỏ Soul', 9),

    (@mazdaId, N'CX-8 Premium AWD', 1359000000, 'AVAILABLE',
     N'SUV 7 chỗ cao cấp, động cơ Skyactiv-G 2.5L Turbo, dẫn động 4 bánh i-Activ AWD',
     2024, N'Trắng', 4),

    (@mazdaId, N'CX-30 2.0 Premium', 849000000, 'AVAILABLE',
     N'SUV cỡ B sang trọng, động cơ Skyactiv-G 2.0L, thiết kế Kodo độc đáo',
     2024, N'Xanh', 7);

-- Ford Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@fordId, N'Ranger Raptor', 1399000000, 'AVAILABLE',
     N'Bán tải hiệu suất cao, động cơ twin-turbo V6 3.0L 397 mã lực, hệ thống treo FOX',
     2024, N'Cam', 3),

    (@fordId, N'Everest Titanium 4x4', 1399000000, 'AVAILABLE',
     N'SUV 7 chỗ địa hình, động cơ Bi-Turbo 2.0L, dẫn động 4 bánh điện tử',
     2024, N'Đen', 5),

    (@fordId, N'Territory Titanium X', 959000000, 'AVAILABLE',
     N'SUV 5 chỗ thông minh, động cơ 1.5L EcoBoost Turbo, công nghệ Ford Co-Pilot360',
     2024, N'Trắng', 8),

    (@fordId, N'Ranger XLT 4x4', 899000000, 'AVAILABLE',
     N'Bán tải đa dụng, động cơ Bi-Turbo 2.0L 213PS, dẫn động 4 bánh bán thời gian',
     2024, N'Xám', 6);

-- Audi Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@audiId, N'A4 45 TFSI quattro', 2099000000, 'AVAILABLE',
     N'Sedan hạng D sang trọng, hệ dẫn động 4 bánh quattro, động cơ 2.0L TFSI 261 mã lực',
     2024, N'Xanh Dương', 4),

    (@audiId, N'Q5 Sportback 45 TFSI', 2799000000, 'AVAILABLE',
     N'SUV Coupe 5 chỗ thể thao, động cơ 2.0L TFSI, quattro ultra, nội thất da Milano',
     2024, N'Đen', 3),

    (@audiId, N'A6 55 TFSI quattro', 3299000000, 'AVAILABLE',
     N'Sedan hạng E cao cấp, động cơ V6 3.0L TFSI 340PS, hệ treo khí nén',
     2024, N'Xám', 2),

    (@audiId, N'Q7 55 TFSI quattro', 4199000000, 'AVAILABLE',
     N'SUV hạng sang 7 chỗ, động cơ V6 3.0L TFSI, công nghệ Matrix LED',
     2024, N'Trắng', 2);

-- Kia Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@kiaId, N'Seltos 1.5 Luxury', 759000000, 'AVAILABLE',
     N'SUV cỡ B hiện đại, động cơ 1.5L Smartstream, thiết kế thể thao',
     2024, N'Đỏ', 12),

    (@kiaId, N'Sorento 2.5 Signature', 1399000000, 'AVAILABLE',
     N'SUV 7 chỗ cao cấp, động cơ 2.5L Smartstream Turbo, công nghệ Kia Drive Wise',
     2024, N'Trắng', 5),

    (@kiaId, N'K3 2.0 Premium', 669000000, 'AVAILABLE',
     N'Sedan cỡ C thể thao, động cơ 2.0L Nu Smartstream, thiết kế Tiger Nose',
     2024, N'Bạc', 10),

    (@kiaId, N'Carnival 2.2 Signature', 1519000000, 'AVAILABLE',
     N'MPV cao cấp 7-8 chỗ, động cơ diesel 2.2L, nội thất da Nappa, cửa trượt điện',
     2024, N'Đen', 4);

-- Volkswagen Cars
INSERT INTO Car (brand_id, model, price, status, description, year, color, stock)
VALUES
    (@vwId, N'Tiguan Allspace Luxury', 1849000000, 'AVAILABLE',
     N'SUV 7 chỗ châu Âu, động cơ 2.0L TSI, hệ dẫn động 4Motion, nội thất cao cấp',
     2024, N'Xám', 4),

    (@vwId, N'Passat BlueMotion High', 1469000000, 'AVAILABLE',
     N'Sedan hạng D sang trọng, động cơ 1.8L TSI, công nghệ BlueMotion tiết kiệm',
     2024, N'Đen', 3),

    (@vwId, N'Teramont X', 2399000000, 'AVAILABLE',
     N'SUV Coupe 5 chỗ cao cấp, động cơ V6 3.6L, dẫn động 4Motion, nội thất Vienna',
     2024, N'Trắng', 2);

PRINT '✅ Đã insert 43 xe từ 10 hãng';

-- Kiểm tra cars đã insert
SELECT
    b.brand_name,
    COUNT(*) as total_cars,
    SUM(stock) as total_stock
FROM Car c
         JOIN Brand b ON c.brand_id = b.brand_id
GROUP BY b.brand_name
ORDER BY b.brand_name;
GO

-- =============================================
-- 3. INSERT CAR IMAGES
-- =============================================
PRINT '';
PRINT 'BƯỚC 3: Insert Car Images...';

-- Lấy car IDs
DECLARE @camryId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Camry 2.5Q' ORDER BY car_id DESC);
DECLARE @viosId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Vios 1.5G' ORDER BY car_id DESC);
DECLARE @corollaCrossId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Corolla Cross 1.8V' ORDER BY car_id DESC);
DECLARE @velozId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Veloz Cross 1.5' ORDER BY car_id DESC);

DECLARE @cityId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'City RS' ORDER BY car_id DESC);
DECLARE @typeRId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Civic Type R' ORDER BY car_id DESC);
DECLARE @crvId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'CR-V 1.5L Turbo' ORDER BY car_id DESC);
DECLARE @accordId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Accord 1.5L Turbo' ORDER BY car_id DESC);

DECLARE @cClassId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'C-Class C200' ORDER BY car_id DESC);
DECLARE @eClassId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'E-Class E300' ORDER BY car_id DESC);
DECLARE @glcId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'GLC 300 4MATIC' ORDER BY car_id DESC);
DECLARE @sClassId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'S-Class S500' ORDER BY car_id DESC);

DECLARE @bmw320Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'320i M Sport' ORDER BY car_id DESC);
DECLARE @bmwX5Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'X5 xDrive40i' ORDER BY car_id DESC);
DECLARE @bmw530Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'530i M Sport' ORDER BY car_id DESC);
DECLARE @bmwX3Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'X3 xDrive30i' ORDER BY car_id DESC);

DECLARE @tucsonId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Tucson 2.0 Đặc Biệt' ORDER BY car_id DESC);
DECLARE @santafeId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Santa Fe 2.5 Turbo' ORDER BY car_id DESC);
DECLARE @accentId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Accent 1.5 AT Đặc Biệt' ORDER BY car_id DESC);
DECLARE @elantraId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Elantra 2.0 AT' ORDER BY car_id DESC);

DECLARE @cx5Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'CX-5 2.5 Signature Premium' ORDER BY car_id DESC);
DECLARE @mazda3Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Mazda3 2.0 Luxury' ORDER BY car_id DESC);
DECLARE @cx8Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'CX-8 Premium AWD' ORDER BY car_id DESC);
DECLARE @cx30Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'CX-30 2.0 Premium' ORDER BY car_id DESC);

DECLARE @raptorId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Ranger Raptor' ORDER BY car_id DESC);
DECLARE @everestId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Everest Titanium 4x4' ORDER BY car_id DESC);
DECLARE @territoryId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Territory Titanium X' ORDER BY car_id DESC);
DECLARE @rangerXltId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Ranger XLT 4x4' ORDER BY car_id DESC);

DECLARE @audiA4Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'A4 45 TFSI quattro' ORDER BY car_id DESC);
DECLARE @q5Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Q5 Sportback 45 TFSI' ORDER BY car_id DESC);
DECLARE @audiA6Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'A6 55 TFSI quattro' ORDER BY car_id DESC);
DECLARE @q7Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Q7 55 TFSI quattro' ORDER BY car_id DESC);

DECLARE @seltosId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Seltos 1.5 Luxury' ORDER BY car_id DESC);
DECLARE @sorentoId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Sorento 2.5 Signature' ORDER BY car_id DESC);
DECLARE @k3Id INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'K3 2.0 Premium' ORDER BY car_id DESC);
DECLARE @carnivalId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Carnival 2.2 Signature' ORDER BY car_id DESC);

DECLARE @tiguanId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Tiguan Allspace Luxury' ORDER BY car_id DESC);
DECLARE @passatId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Passat BlueMotion High' ORDER BY car_id DESC);
DECLARE @teramontId INT = (SELECT TOP 1 car_id FROM Car WHERE model = N'Teramont X' ORDER BY car_id DESC);

-- Toyota Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@camryId, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800&auto=format', 1),
                                                         (@camryId, 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800&auto=format', 0),
                                                         (@camryId, 'https://images.unsplash.com/photo-1583121274602-3e2820c69888?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@viosId, 'https://images.unsplash.com/photo-1590362891991-f776e747a588?w=800&auto=format', 1),
                                                         (@viosId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@corollaCrossId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@corollaCrossId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@velozId, 'https://images.unsplash.com/photo-1464219789935-c2d9d9aba644?w=800&auto=format', 1),
                                                         (@velozId, 'https://images.unsplash.com/photo-1570733577667-ccb7f0bce597?w=800&auto=format', 0);

-- Honda Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cityId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 1),
                                                         (@cityId, 'https://images.unsplash.com/photo-1494905998402-395d579af36f?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@typeRId, 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=800&auto=format', 1),
                                                         (@typeRId, 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&auto=format', 0),
                                                         (@typeRId, 'https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@crvId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@crvId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES

                                                         (@accordId, 'https://images.unsplash.com/photo-1617654112368-307921291f42?w=800&auto=format', 1);

-- Mercedes Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cClassId, 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800&auto=format', 1),
                                                         (@cClassId, 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800&auto=format', 0),
                                                         (@cClassId, 'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@eClassId, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=800&auto=format', 1),
                                                         (@eClassId, 'https://images.unsplash.com/photo-1617469767053-d3b523a0b982?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@glcId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@glcId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@sClassId, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800&auto=format', 1),
                                                         (@sClassId, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=800&auto=format', 0);

-- BMW Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800&auto=format', 1),
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1617469767053-d3b523a0b982?w=800&auto=format', 0),
                                                         (@bmw320Id, 'https://images.unsplash.com/photo-1556189250-72ba954cfc2b?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmwX5Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@bmwX5Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmw530Id, 'https://images.unsplash.com/photo-1617469767053-d3b523a0b982?w=800&auto=format', 1),
                                                         (@bmw530Id, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@bmwX3Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@bmwX3Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 0);

-- Hyundai Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 1),
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800&auto=format', 0),
                                                         (@tucsonId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@santafeId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@santafeId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@accentId, 'https://images.unsplash.com/photo-1590362891991-f776e747a588?w=800&auto=format', 1),
                                                         (@accentId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@elantraId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 1),
                                                         (@elantraId, 'https://images.unsplash.com/photo-1617654112368-307921291f42?w=800&auto=format', 0);

-- Mazda Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cx5Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@cx5Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 0),
                                                         (@cx5Id, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@mazda3Id, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 1),
                                                         (@mazda3Id, 'https://images.unsplash.com/photo-1617654112368-307921291f42?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cx8Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@cx8Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@cx30Id, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 1),
                                                         (@cx30Id, 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800&auto=format', 0);

-- Ford Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@raptorId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 1),
                                                         (@raptorId, 'https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=800&auto=format', 0),
                                                         (@raptorId, 'https://images.unsplash.com/photo-1611859266238-4b98091d9d9b?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@everestId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@everestId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@territoryId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 1),
                                                         (@territoryId, 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@rangerXltId, 'https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=800&auto=format', 1),
                                                         (@rangerXltId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 0);

-- Audi Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800&auto=format', 1),
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1614200187524-dc4b892acf16?w=800&auto=format', 0),
                                                         (@audiA4Id, 'https://images.unsplash.com/photo-1617469767053-d3b523a0b982?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@q5Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@q5Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@audiA6Id, 'https://images.unsplash.com/photo-1614200187524-dc4b892acf16?w=800&auto=format', 1),
                                                         (@audiA6Id, 'https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@q7Id, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@q7Id, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

-- Kia Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@seltosId, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800&auto=format', 1),
                                                         (@seltosId, 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@sorentoId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@sorentoId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@k3Id, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 1),
                                                         (@k3Id, 'https://images.unsplash.com/photo-1617654112368-307921291f42?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@carnivalId, 'https://images.unsplash.com/photo-1464219789935-c2d9d9aba644?w=800&auto=format', 1),
                                                         (@carnivalId, 'https://images.unsplash.com/photo-1570733577667-ccb7f0bce597?w=800&auto=format', 0);

-- Volkswagen Images
INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@tiguanId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 1),
                                                         (@tiguanId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@passatId, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800&auto=format', 1),
                                                         (@passatId, 'https://images.unsplash.com/photo-1617654112368-307921291f42?w=800&auto=format', 0);

INSERT INTO CarImage (car_id, image_url, is_primary) VALUES
                                                         (@teramontId, 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?w=800&auto=format', 1),
                                                         (@teramontId, 'https://images.unsplash.com/photo-1581540222194-0def2dda95b8?w=800&auto=format', 0);

PRINT '✅ Đã insert hình ảnh cho tất cả xe';

-- Thống kê images
SELECT
    COUNT(*) as total_images,
    COUNT(DISTINCT car_id) as cars_with_images,
    SUM(CASE WHEN is_primary = 1 THEN 1 ELSE 0 END) as primary_images
FROM CarImage;
GO

-- =============================================
-- 4. INSERT PROMOTIONS (CHỈ discount_percentage)
-- =============================================
PRINT '';
PRINT 'BƯỚC 4: Insert Promotions...';

-- Xóa dữ liệu promotion cũ nếu có
DELETE FROM UserPromotion;
DELETE FROM CarPromotion;
DELETE FROM Promotion;

-- Promotion 1: Ưu Đãi Học Sinh - Sinh Viên (20%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Ưu Đãi Học Sinh - Sinh Viên Mua Xe',
           N'Car Showroom đồng hành cùng thế hệ trẻ. Giảm ngay 20% khi xuất trình thẻ sinh viên còn hiệu lực. Hỗ trợ vay 90% giá trị xe, không cần chứng minh thu nhập (bảo lãnh bởi phụ huynh). Lãi suất ưu đãi 7.5%/năm, ân hạn nợ gốc 12 tháng đầu. Sinh viên xuất sắc có GPA từ 3.5 trở lên sẽ được ưu đãi thêm. Tặng thêm bộ phụ kiện thể thao trị giá 15 triệu đồng.',
           '2025-01-01',
           '2026-12-31',
           20.00
       );

-- Promotion 2: Khuyến Mãi Tết (15%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Khuyến Mãi Tết Nguyên Đán 2025',
           N'Mừng xuân Ất Tỵ, giảm giá 15% cho tất cả các dòng xe. Tặng kèm gói bảo hiểm VIP trị giá 30 triệu đồng. Miễn phí bảo dưỡng 2 năm đầu. Hỗ trợ trả góp lãi suất 0% trong 6 tháng đầu tiên. Tặng thêm phụ kiện cao cấp trị giá 25 triệu đồng bao gồm: camera hành trình, cảm biến lùi, thảm lót sàn 3D.',
           '2025-01-15',
           '2026-02-28',
           15.00
       );

-- Promotion 3: Ưu Đãi Cuối Năm (12%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Ưu Đãi Mua Xe Cuối Năm',
           N'Giảm ngay 12% cho khách hàng mua xe trong tháng 12. Tặng kèm phụ kiện cao cấp trị giá 20 triệu. Hỗ trợ trả góp 0% lãi suất trong 12 tháng đầu. Bảo hành mở rộng 5 năm hoặc 100.000km. Miễn phí đăng ký và đăng kiểm lần đầu. Tặng thêm 1 năm bảo hiểm thân vỏ.',
           '2024-12-01',
           '2026-12-31',
           12.00
       );

-- Promotion 4: Flash Sale (10%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Flash Sale Cuối Tuần',
           N'Giảm sốc 10% cho tất cả các dòng xe chỉ trong 3 ngày cuối tuần. Áp dụng từ thứ 6 đến chủ nhật hàng tuần. Số lượng có hạn, khách hàng đặt cọc trước sẽ được ưu tiên. Tặng kèm bộ phụ kiện thể thao trị giá 10 triệu đồng. Miễn phí phủ ceramic 1 lần.',
           '2025-10-01',
           '2026-10-31',
           10.00
       );

-- Promotion 5: Tri Ân (5%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Tri Ân Khách Hàng Thân Thiết',
           N'Dành riêng cho khách hàng đã mua xe tại showroom. Giảm giá 5% khi giới thiệu bạn bè mua xe. Tặng voucher bảo dưỡng miễn phí trị giá 5 triệu đồng. Ưu đãi mua phụ kiện giảm đến 30%. Hỗ trợ thu cũ đổi mới với giá cao nhất thị trường.',
           '2025-01-01',
           '2026-12-31',
           5.00
       );

-- Promotion 6: Combo Family (18%)
INSERT INTO Promotion (title, description, start_date, end_date, discount_percentage)
VALUES (
           N'Combo Gia Đình - Giảm Đến 18%',
           N'Ưu đãi đặc biệt cho xe 7 chỗ và MPV. Giảm ngay 18% cho khách hàng mua xe gia đình. Tặng kèm ghế ngồi trẻ em cao cấp. Miễn phí bảo dưỡng 3 năm. Hỗ trợ vay lên đến 85% với lãi suất ưu đãi.',
           '2025-03-01',
           '2026-03-31',
           18.00
       );

PRINT '✅ Đã insert 6 promotions';

-- Xem các promotion vừa tạo
SELECT
    promotion_id,
    title,
    discount_percentage,
    start_date,
    end_date
FROM Promotion
ORDER BY discount_percentage DESC;
GO

-- =============================================
-- 5. INSERT CAR PROMOTIONS (SIMPLIFIED)
-- =============================================
PRINT '';
PRINT 'BƯỚC 5: Insert Car Promotions...';

DECLARE @promo1Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Học Sinh%');
DECLARE @promo2Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Tết%');
DECLARE @promo3Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Cuối Năm%');
DECLARE @promo4Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Flash Sale%');
DECLARE @promo5Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Tri Ân%');
DECLARE @promo6Id INT = (SELECT promotion_id FROM Promotion WHERE title LIKE N'%Combo%');

-- Promotion 1: Ưu Đãi Học Sinh (Xe giá rẻ - trung bình < 1 tỷ)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo1Id FROM Car WHERE price < 1000000000;
PRINT '  ✓ Promotion 1 (Học Sinh): Xe < 1 tỷ';

-- Promotion 2: Tết (TẤT CẢ XE)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo2Id FROM Car;
PRINT '  ✓ Promotion 2 (Tết): Tất cả xe';

-- Promotion 3: Cuối Năm (Xe cao cấp > 2 tỷ)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo3Id FROM Car WHERE price > 2000000000;
PRINT '  ✓ Promotion 3 (Cuối Năm): Xe > 2 tỷ';

-- Promotion 4: Flash Sale (Xe phổ biến 500tr - 1.5 tỷ)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo4Id FROM Car WHERE price BETWEEN 500000000 AND 1500000000;
PRINT '  ✓ Promotion 4 (Flash Sale): Xe 500tr-1.5 tỷ';

-- Promotion 5: Tri Ân (TẤT CẢ XE)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo5Id FROM Car;
PRINT '  ✓ Promotion 5 (Tri Ân): Tất cả xe';

-- Promotion 6: Combo Family (Xe 7 chỗ: Santa Fe, Sorento, CX-8, X5, Everest, Carnival, Tiguan Allspace, Q7)
INSERT INTO CarPromotion (car_id, promotion_id)
SELECT car_id, @promo6Id FROM Car
WHERE model LIKE N'%Santa Fe%'
   OR model LIKE N'%Sorento%'
   OR model LIKE N'%CX-8%'
   OR model LIKE N'%X5%'
   OR model LIKE N'%Everest%'
   OR model LIKE N'%Carnival%'
   OR model LIKE N'%Tiguan%'
   OR model LIKE N'%Q7%';
PRINT '  ✓ Promotion 6 (Combo Family): Xe 7 chỗ';

PRINT '✅ Đã insert car promotions';

-- Thống kê car promotions
SELECT
    p.title,
    p.discount_percentage,
    COUNT(cp.car_id) as total_cars
FROM Promotion p
         LEFT JOIN CarPromotion cp ON p.promotion_id = cp.promotion_id
GROUP BY p.title, p.discount_percentage
ORDER BY p.discount_percentage DESC;
GO

-- =============================================
-- 6. INSERT BLOGS
-- =============================================
PRINT '';
PRINT 'BƯỚC 6: Insert Blogs...';

-- Lấy admin user ID
DECLARE @adminId INT = (SELECT TOP 1 user_id FROM AppUsers WHERE role = 'ADMIN' ORDER BY user_id);

IF @adminId IS NULL
BEGIN
    PRINT '❌ KHÔNG TÌM THẤY ADMIN USER!';
    PRINT 'Bạn cần tạo admin user trước. Ví dụ:';
    PRINT 'INSERT INTO AppUsers (email, password_hash, role, is_active, email_verified)';
    PRINT 'VALUES (''admin@carshowroom.com'', ''hashed_password'', ''ADMIN'', 1, 1);';
END
ELSE
BEGIN
    PRINT '  ℹ️ Sử dụng admin user ID: ' + CAST(@adminId AS VARCHAR);

    -- Xóa blogs cũ
DELETE FROM Blog WHERE author_id = @adminId;

-- Blog 1: Hướng dẫn chọn xe
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           'https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=1200',
           GETDATE()
       );

-- Blog 2: So sánh sedan vs SUV
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           'https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=1200',
           DATEADD(day, -7, GETDATE())
       );

-- Blog 3: Bảo dưỡng xe
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           'https://images.unsplash.com/photo-1487754180451-c456f719a1fc?w=1200',
           DATEADD(day, -14, GETDATE())
       );

-- Blog 4: Xe điện
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           'https://images.unsplash.com/photo-1593941707882-a5bba14938c7?w=1200',
           DATEADD(day, -21, GETDATE())
       );

-- Blog 5: Mẹo lái xe an toàn
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           <p>An toàn giao thông bắt đầu từ ý thức của mỗi người. Hãy lái xe văn minh, an toàn vì bản thân và cộng đồng!</p>',
           @adminId,
           'https://autopro8.mediacdn.vn/134505113543774208/2024/3/1/chi-phi-phat-sinh-hoc-lai-xe-tuy-tung-co-so-dao-tao-va-nhu-cau-hoc-vien-11413550-1709253421434-17092534228441600435009.jpg',
           DATEADD(day, -28, GETDATE())
       );

-- Blog 6: Vay mua xe
INSERT INTO Blog (title, content, author_id, image_url, created_at)
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
           <p>Vay mua xe trả góp là giải pháp thông minh nếu bạn biết cách tính toán. Hãy đến showroom để được tư vấn chi tiết về các gói vay ưu đãi!</p>',
           @adminId,
           'https://www.vib.com.vn/wps/wcm/connect/16d53edf-e255-48a4-b299-d7b0b4c541d6/mua-xe-tra-gop-tra-truoc-bao-nhieu+%281%29.png.webp?MOD=AJPERES&CACHEID=ROOTWORKSPACE-16d53edf-e255-48a4-b299-d7b0b4c541d6-pecxjrp',
           DATEADD(day, -35, GETDATE())
       );

PRINT '✅ Đã insert 6 blogs';

    -- Thống kê blogs
SELECT
    blog_id,
    title,
    CASE WHEN image_url IS NOT NULL THEN 'Có' ELSE 'Không' END as has_image,
    LEN(content) as content_length,
    created_at
FROM Blog
WHERE author_id = @adminId
ORDER BY created_at DESC;
END
GO

-- =============================================
-- 7. VERIFICATION - Kiểm tra toàn bộ dữ liệu
-- =============================================
PRINT '';
PRINT '========================================';
PRINT 'TỔNG KẾT DỮ LIỆU';
PRINT '========================================';

-- Thống kê tổng quan
SELECT
    'Brands' AS Category,
    COUNT(*) AS Total
FROM Brand

UNION ALL

SELECT
    'Cars',
    COUNT(*)
FROM Car

UNION ALL

SELECT
    'Car Images',
    COUNT(*)
FROM CarImage

UNION ALL

SELECT
    'Promotions',
    COUNT(*)
FROM Promotion

UNION ALL

SELECT
    'Car Promotions',
    COUNT(*)
FROM CarPromotion

UNION ALL

SELECT
    'Blogs',
    COUNT(*)
FROM Blog;

-- Chi tiết xe theo brand
PRINT '';
PRINT 'CHI TIẾT XE THEO HÃNG:';
SELECT
    b.brand_name AS N'Hãng xe',
    COUNT(c.car_id) AS N'Số xe',
    SUM(c.stock) AS N'Tồn kho',
    FORMAT(AVG(c.price), 'N0') + ' VND' AS N'Giá trung bình',
    FORMAT(MIN(c.price), 'N0') + ' VND' AS N'Giá thấp nhất',
    FORMAT(MAX(c.price), 'N0') + ' VND' AS N'Giá cao nhất'
FROM Brand b
         LEFT JOIN Car c ON b.brand_id = c.brand_id
GROUP BY b.brand_name
ORDER BY COUNT(c.car_id) DESC;

-- View mẫu từ vw_CarsWithPromotions
PRINT '';
PRINT 'MẪU XE CÓ KHUYẾN MÃI TỐT NHẤT:';
SELECT TOP 5
    brand_name AS N'Hãng',
    model AS N'Mẫu xe',
    FORMAT(price, 'N0') + ' VND' AS N'Giá gốc',
    promotion_title AS N'Khuyến mãi',
    CAST(discount_percentage AS VARCHAR) + '%' AS N'Giảm giá',
    FORMAT(discounted_price, 'N0') + ' VND' AS N'Giá sau KM'
FROM vw_CarsWithPromotions
WHERE promotion_id IS NOT NULL
ORDER BY discount_percentage DESC;

PRINT '';
PRINT '========================================';
PRINT '✅ HOÀN TẤT INSERT DỮ LIỆU THÀNH CÔNG!';
PRINT '========================================';
PRINT '';
PRINT 'Dữ liệu đã được insert:';
PRINT '- 10 hãng xe';
PRINT '- 43 xe từ nhiều phân khúc';
PRINT '- ~100+ hình ảnh xe';
PRINT '- 6 chương trình khuyến mãi';
PRINT '- Mapping khuyến mãi cho các xe phù hợp';
PRINT '- 6 bài blog với hình ảnh';
PRINT '';
PRINT 'Database đã sẵn sàng sử dụng!';
GO