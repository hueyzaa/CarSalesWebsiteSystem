-- =============================================
-- COMPLETE DATABASE SCHEMA - FINAL VERSION
-- Car Sales Website System
-- Includes SHOWROOM Payment Support
-- =============================================

-- Optional: Drop existing database (⚠️ Deletes all data!)
-- USE master;
-- IF EXISTS (SELECT * FROM sys.databases WHERE name = 'CarSalesWebsite')
-- BEGIN
--     ALTER DATABASE CarSalesWebsite SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
--     DROP DATABASE CarSalesWebsite;
-- END;
-- GO

-- CREATE DATABASE CarSalesWebsite;
-- GO
-- USE CarSalesWebsite;
-- GO

-- =============================================
-- DROP TABLES (in reverse dependency order)
-- =============================================
IF OBJECT_ID('CarImage', 'U') IS NOT NULL DROP TABLE CarImage;
IF OBJECT_ID('Blog', 'U') IS NOT NULL DROP TABLE Blog;
IF OBJECT_ID('CarPromotion', 'U') IS NOT NULL DROP TABLE CarPromotion;
IF OBJECT_ID('Promotion', 'U') IS NOT NULL DROP TABLE Promotion;
IF OBJECT_ID('Transactions', 'U') IS NOT NULL DROP TABLE Transactions;
IF OBJECT_ID('OrderDetail', 'U') IS NOT NULL DROP TABLE OrderDetail;
IF OBJECT_ID('Orders', 'U') IS NOT NULL DROP TABLE Orders;
IF OBJECT_ID('CartItem', 'U') IS NOT NULL DROP TABLE CartItem;
IF OBJECT_ID('Cart', 'U') IS NOT NULL DROP TABLE Cart;
IF OBJECT_ID('Car', 'U') IS NOT NULL DROP TABLE Car;
IF OBJECT_ID('Brand', 'U') IS NOT NULL DROP TABLE Brand;
IF OBJECT_ID('AppUsers', 'U') IS NOT NULL DROP TABLE AppUsers;
GO

-- =============================================
-- USERS TABLE
-- =============================================
CREATE TABLE AppUsers (
                          user_id INT IDENTITY(1,1) PRIMARY KEY,
                          name NVARCHAR(100) NOT NULL,
                          email NVARCHAR(100) UNIQUE NOT NULL,
                          password_hash NVARCHAR(255),
                          role NVARCHAR(20) CHECK (role IN ('GUEST','CUSTOMER','STAFF','ADMIN')) DEFAULT 'CUSTOMER',
                          oauth_provider NVARCHAR(50),
                          created_at DATETIME DEFAULT GETDATE(),
                          phone NVARCHAR(20),
                          address NVARCHAR(255)
);
GO

-- =============================================
-- BRAND & CAR TABLES
-- =============================================
CREATE TABLE Brand (
                       brand_id INT IDENTITY(1,1) PRIMARY KEY,
                       brand_name NVARCHAR(100) UNIQUE NOT NULL
);
GO

CREATE TABLE Car (
                     car_id INT IDENTITY(1,1) PRIMARY KEY,
                     brand_id INT,
                     model NVARCHAR(100),
                     price DECIMAL(15,2),
                     status NVARCHAR(20) CHECK (status IN ('AVAILABLE','UNAVAILABLE')) DEFAULT 'AVAILABLE',
                     description NVARCHAR(MAX),
                     year INT,
                     color NVARCHAR(50),
                     stock INT DEFAULT 0,
                     FOREIGN KEY (brand_id) REFERENCES Brand(brand_id)
);
GO

-- =============================================
-- CART TABLES
-- =============================================
CREATE TABLE Cart (
                      cart_id INT IDENTITY(1,1) PRIMARY KEY,
                      user_id INT,
                      created_at DATETIME DEFAULT GETDATE(),
                      FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);
GO

CREATE TABLE CartItem (
                          cart_item_id INT IDENTITY(1,1) PRIMARY KEY,
                          cart_id INT,
                          car_id INT,
                          quantity INT DEFAULT 1,
                          FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
                          FOREIGN KEY (car_id) REFERENCES Car(car_id)
);
GO

-- =============================================
-- ORDERS & DETAILS TABLES
-- =============================================
CREATE TABLE Orders (
                        order_id INT IDENTITY(1,1) PRIMARY KEY,
                        user_id INT,
                        status NVARCHAR(20) CHECK (status IN ('PENDING','APPROVED','CANCELLED','COMPLETED')) DEFAULT 'PENDING',
                        created_at DATETIME DEFAULT GETDATE(),
                        payment_type NVARCHAR(20) CHECK (payment_type IN ('FULL','DEPOSIT','SHOWROOM')),
                        deposit_amount DECIMAL(15,2),
                        remaining_amount DECIMAL(15,2),
                        notes NVARCHAR(MAX),
                        FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);
GO

CREATE TABLE OrderDetail (
                             order_detail_id INT IDENTITY(1,1) PRIMARY KEY,
                             order_id INT,
                             car_id INT,
                             price DECIMAL(15,2),
                             quantity INT DEFAULT 1,
                             FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                             FOREIGN KEY (car_id) REFERENCES Car(car_id)
);
GO

-- =============================================
-- TRANSACTIONS TABLE
-- =============================================
CREATE TABLE Transactions (
                              transaction_id INT IDENTITY(1,1) PRIMARY KEY,
                              order_id INT,
                              amount DECIMAL(15,2),
                              type NVARCHAR(20) CHECK (type IN ('FULL','DEPOSIT','SHOWROOM')),
                              payment_status NVARCHAR(20) CHECK (payment_status IN ('PENDING','PAID','CANCELLED')) DEFAULT 'PENDING',
                              created_at DATETIME DEFAULT GETDATE(),
                              FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);
GO

-- =============================================
-- PROMOTION TABLES
-- =============================================
CREATE TABLE Promotion (
                           promotion_id INT IDENTITY(1,1) PRIMARY KEY,
                           title NVARCHAR(100),
                           description NVARCHAR(MAX),
                           start_date DATE,
                           end_date DATE
);
GO

CREATE TABLE CarPromotion (
                              car_id INT,
                              promotion_id INT,
                              PRIMARY KEY(car_id, promotion_id),
                              FOREIGN KEY (car_id) REFERENCES Car(car_id),
                              FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);
GO

-- =============================================
-- BLOG TABLE
-- =============================================
CREATE TABLE Blog (
                      blog_id INT IDENTITY(1,1) PRIMARY KEY,
                      title NVARCHAR(200),
                      content NVARCHAR(MAX),
                      author_id INT,
                      created_at DATETIME DEFAULT GETDATE(),
                      FOREIGN KEY (author_id) REFERENCES AppUsers(user_id)
);
GO

-- =============================================
-- CAR IMAGE TABLE:wq:wq
-- =============================================
CREATE TABLE CarImage (
                          image_id INT IDENTITY(1,1) PRIMARY KEY,
                          car_id INT NOT NULL,
                          image_url NVARCHAR(255) NOT NULL,
                          is_primary BIT DEFAULT 0,
                          created_at DATETIME DEFAULT GETDATE(),
                          FOREIGN KEY (car_id) REFERENCES Car(car_id) ON DELETE CASCADE
);
GO

-- =============================================
-- TRIGGERS
-- =============================================
IF OBJECT_ID('trg_UpdateCarStatus', 'TR') IS NOT NULL
DROP TRIGGER trg_UpdateCarStatus;
GO

CREATE TRIGGER trg_UpdateCarStatus
    ON Car
    AFTER INSERT, UPDATE
                      AS
BEGIN
    SET NOCOUNT ON;

UPDATE Car
SET status = 'UNAVAILABLE'
    FROM Car c
    INNER JOIN inserted i ON c.car_id = i.car_id
WHERE i.stock <= 0 AND c.status = 'AVAILABLE';

UPDATE Car
SET status = 'AVAILABLE'
    FROM Car c
    INNER JOIN inserted i ON c.car_id = i.car_id
WHERE i.stock > 0 AND c.status = 'UNAVAILABLE';
END;
GO
-- Drop trigger nếu đã tồn tại
IF OBJECT_ID('trg_UpdateRemainingAmount_AfterInsert', 'TR') IS NOT NULL
DROP TRIGGER trg_UpdateRemainingAmount_AfterInsert;
GO

-- Tạo trigger mới
CREATE TRIGGER trg_UpdateRemainingAmount_AfterInsert
    ON Transactions
    AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Update remainingAmount cho các orders bị ảnh hưởng
UPDATE o
SET o.remaining_amount = (
                             -- Tính total từ OrderDetail
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             -- Trừ đi tổng số tiền đã thanh toán (status = PAID)
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id
                               AND t.payment_status = 'PAID'
                         )
    FROM Orders o
    INNER JOIN inserted i ON o.order_id = i.order_id;

PRINT 'Trigger: Updated remaining_amount after INSERT transaction';
END;
GO
-- Drop trigger nếu đã tồn tại
IF OBJECT_ID('trg_UpdateRemainingAmount_AfterUpdate', 'TR') IS NOT NULL
DROP TRIGGER trg_UpdateRemainingAmount_AfterUpdate;
GO

-- Tạo trigger mới
CREATE TRIGGER trg_UpdateRemainingAmount_AfterUpdate
    ON Transactions
    AFTER UPDATE
              AS
BEGIN
    SET NOCOUNT ON;

    -- Chỉ update khi payment_status thay đổi
    IF UPDATE(payment_status)
BEGIN
        -- Update remainingAmount cho các orders bị ảnh hưởng
UPDATE o
SET o.remaining_amount = (
                             -- Tính total từ OrderDetail
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             -- Trừ đi tổng số tiền đã thanh toán (status = PAID)
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id
                               AND t.payment_status = 'PAID'
                         )
    FROM Orders o
        INNER JOIN inserted i ON o.order_id = i.order_id;

PRINT 'Trigger: Updated remaining_amount after UPDATE transaction status';
END;
END;
GO
-- Drop trigger nếu đã tồn tại
IF OBJECT_ID('trg_UpdateRemainingAmount_AfterDelete', 'TR') IS NOT NULL
DROP TRIGGER trg_UpdateRemainingAmount_AfterDelete;
GO

-- Tạo trigger mới
CREATE TRIGGER trg_UpdateRemainingAmount_AfterDelete
    ON Transactions
    AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;

    -- Update remainingAmount cho các orders bị ảnh hưởng
UPDATE o
SET o.remaining_amount = (
                             -- Tính total từ OrderDetail
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             -- Trừ đi tổng số tiền đã thanh toán (status = PAID)
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id
                               AND t.payment_status = 'PAID'
                         )
    FROM Orders o
    INNER JOIN deleted d ON o.order_id = d.order_id;

PRINT 'Trigger: Updated remaining_amount after DELETE transaction';
END;
GO
--Sau khi tạo trigger, chạy script này để fix tất cả orders cũ
-- Update remaining_amount cho TẤT CẢ orders hiện tại
UPDATE o
SET o.remaining_amount = (
                             -- Tính total từ OrderDetail
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             -- Trừ đi tổng số tiền đã thanh toán (status = PAID)
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id
                               AND t.payment_status = 'PAID'
                         )
    FROM Orders o;

-- Kiểm tra kết quả
SELECT
    o.order_id,
    o.payment_type,
    (SELECT SUM(od.price * od.quantity) FROM OrderDetail od WHERE od.order_id = o.order_id) as total,
    (SELECT COALESCE(SUM(t.amount), 0) FROM Transactions t WHERE t.order_id = o.order_id AND t.payment_status = 'PAID') as paid,
    o.remaining_amount,
    o.deposit_amount,
    o.status
FROM Orders o
ORDER BY o.order_id DESC;
-- =============================================
-- INDEXES
-- =============================================
CREATE NONCLUSTERED INDEX IX_Orders_UserId ON Orders(user_id);
CREATE NONCLUSTERED INDEX IX_Orders_Status ON Orders(status);
CREATE NONCLUSTERED INDEX IX_Orders_PaymentType ON Orders(payment_type);
CREATE NONCLUSTERED INDEX IX_Transactions_OrderId ON Transactions(order_id);
CREATE NONCLUSTERED INDEX IX_Transactions_Type ON Transactions(type);
CREATE NONCLUSTERED INDEX IX_Transactions_PaymentStatus ON Transactions(payment_status);
CREATE NONCLUSTERED INDEX IX_Car_BrandId ON Car(brand_id);
CREATE NONCLUSTERED INDEX IX_Car_Status ON Car(status);
GO

-- =============================================
-- VIEWS
-- =============================================
IF OBJECT_ID('vw_PendingShowroomOrders', 'V') IS NOT NULL
DROP VIEW vw_PendingShowroomOrders;
GO

CREATE VIEW vw_PendingShowroomOrders AS
SELECT
    o.order_id,
    o.user_id,
    u.name AS customer_name,
    u.email AS customer_email,
    u.phone AS customer_phone,
    o.status,
    o.payment_type,
    o.notes,
    o.created_at,
    ISNULL((SELECT SUM(price * quantity)
            FROM OrderDetail
            WHERE order_id = o.order_id), 0) AS total_amount
FROM Orders o
         JOIN AppUsers u ON o.user_id = u.user_id
WHERE o.payment_type = 'SHOWROOM' AND o.status = 'PENDING';
GO

IF OBJECT_ID('vw_OrderDetailsWithCar', 'V') IS NOT NULL
DROP VIEW vw_OrderDetailsWithCar;
GO

CREATE VIEW vw_OrderDetailsWithCar AS
SELECT
    od.order_detail_id,
    od.order_id,
    od.car_id,
    od.price,
    od.quantity,
    od.price * od.quantity AS subtotal,
    c.model AS car_model,
    c.year AS car_year,
    c.color AS car_color,
    b.brand_name,
    ci.image_url
FROM OrderDetail od
         JOIN Car c ON od.car_id = c.car_id
         JOIN Brand b ON c.brand_id = b.brand_id
         LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1;
GO

-- =============================================
-- VERIFICATION
-- =============================================
PRINT '========================================';
PRINT '✅ DATABASE SCHEMA CREATED SUCCESSFULLY!';
PRINT '========================================';
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE';
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS;
GO
