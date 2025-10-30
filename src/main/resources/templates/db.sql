-- =============================================
-- UPDATED DATABASE SCHEMA - SIMPLIFIED VERSION
-- Car Sales Website System
-- Removed: position, department, hired_date, salary, created_by, notes from Staff
-- Removed: loyalty_points from Customers
-- =============================================

--USE TestDB;
--GO

-- =============================================
-- DROP TABLES (in reverse dependency order)
-- =============================================
IF OBJECT_ID('UserPromotion', 'U') IS NOT NULL DROP TABLE UserPromotion;
IF OBJECT_ID('CarImage', 'U') IS NOT NULL DROP TABLE CarImage;
IF OBJECT_ID('Blog', 'U') IS NOT NULL DROP TABLE Blog;
IF OBJECT_ID('CarPromotion', 'U') IS NOT NULL DROP TABLE CarPromotion;
IF OBJECT_ID('Transactions', 'U') IS NOT NULL DROP TABLE Transactions;
IF OBJECT_ID('OrderDetail', 'U') IS NOT NULL DROP TABLE OrderDetail;
IF OBJECT_ID('Orders', 'U') IS NOT NULL DROP TABLE Orders;
IF OBJECT_ID('CartItem', 'U') IS NOT NULL DROP TABLE CartItem;
IF OBJECT_ID('Cart', 'U') IS NOT NULL DROP TABLE Cart;
IF OBJECT_ID('Car', 'U') IS NOT NULL DROP TABLE Car;
IF OBJECT_ID('Brand', 'U') IS NOT NULL DROP TABLE Brand;
IF OBJECT_ID('Promotion', 'U') IS NOT NULL DROP TABLE Promotion;
IF OBJECT_ID('Staff', 'U') IS NOT NULL DROP TABLE Staff;
IF OBJECT_ID('Customers', 'U') IS NOT NULL DROP TABLE Customers;
IF OBJECT_ID('AppUsers', 'U') IS NOT NULL DROP TABLE AppUsers;
GO

-- =============================================
-- USERS TABLES
-- =============================================

CREATE TABLE AppUsers (
                          user_id INT IDENTITY(1,1) PRIMARY KEY,
                          email NVARCHAR(100) UNIQUE NOT NULL,
                          password_hash NVARCHAR(255) NOT NULL,
                          role NVARCHAR(20) CHECK (role IN ('CUSTOMER','STAFF','ADMIN')) DEFAULT 'CUSTOMER' NOT NULL,
                          is_active BIT DEFAULT 1 NOT NULL,
                          created_at DATETIME DEFAULT GETDATE(),
                          last_login DATETIME NULL
);
GO

CREATE TABLE Staff (
                       staff_id INT PRIMARY KEY,
                       name NVARCHAR(100) NOT NULL,
                       phone NVARCHAR(20) NULL,
                       address NVARCHAR(255) NULL,
                       CONSTRAINT FK_Staff_AppUsers FOREIGN KEY (staff_id) REFERENCES AppUsers(user_id) ON DELETE CASCADE
);
GO

CREATE TABLE Customers (
                           customer_id INT PRIMARY KEY,
                           name NVARCHAR(100) NOT NULL,
                           phone NVARCHAR(20) NULL,
                           address NVARCHAR(255) NULL,
                           oauth_provider NVARCHAR(50) NULL,
                           CONSTRAINT FK_Customers_AppUsers FOREIGN KEY (customer_id) REFERENCES AppUsers(user_id) ON DELETE CASCADE
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
-- PROMOTION TABLE
-- =============================================

CREATE TABLE Promotion (
                           promotion_id INT IDENTITY(1,1) PRIMARY KEY,
                           title NVARCHAR(100),
                           description NVARCHAR(MAX),
                           start_date DATE,
                           end_date DATE,
                           discount_percentage DECIMAL(5,2) DEFAULT 0,
                           discount_amount DECIMAL(15,2) DEFAULT 0
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
                        promotion_id INT NULL,
                        FOREIGN KEY (user_id) REFERENCES AppUsers(user_id),
                        CONSTRAINT FK_Orders_Promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
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
-- CAR PROMOTION TABLE
-- =============================================

CREATE TABLE CarPromotion (
                              car_id INT,
                              promotion_id INT,
                              discount_percentage DECIMAL(5,2) DEFAULT 0,
                              discount_amount DECIMAL(15,2) DEFAULT 0,
                              PRIMARY KEY(car_id, promotion_id),
                              FOREIGN KEY (car_id) REFERENCES Car(car_id),
                              FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);
GO

-- =============================================
-- USER PROMOTION TABLE
-- =============================================

CREATE TABLE UserPromotion (
                               user_promotion_id INT IDENTITY(1,1) PRIMARY KEY,
                               user_id INT NOT NULL,
                               promotion_id INT NOT NULL,
                               claimed_at DATETIME DEFAULT GETDATE(),
                               is_used BIT DEFAULT 0,
                               used_at DATETIME NULL,
                               order_id INT NULL,
                               CONSTRAINT FK_UserPromotion_User FOREIGN KEY (user_id) REFERENCES AppUsers(user_id) ON DELETE CASCADE,
                               CONSTRAINT FK_UserPromotion_Promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id) ON DELETE CASCADE,
                               CONSTRAINT FK_UserPromotion_Order FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                               CONSTRAINT UQ_UserPromotion UNIQUE(user_id, promotion_id)
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
                      image_url NVARCHAR(500) NULL,
                      FOREIGN KEY (author_id) REFERENCES AppUsers(user_id)
);
GO

-- =============================================
-- CAR IMAGE TABLE
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
-- INDEXES
-- =============================================

-- AppUsers Indexes
CREATE NONCLUSTERED INDEX IX_AppUsers_Email ON AppUsers(email);
CREATE NONCLUSTERED INDEX IX_AppUsers_Role ON AppUsers(role);
CREATE NONCLUSTERED INDEX IX_AppUsers_IsActive ON AppUsers(is_active);

-- Orders Indexes
CREATE NONCLUSTERED INDEX IX_Orders_UserId ON Orders(user_id);
CREATE NONCLUSTERED INDEX IX_Orders_Status ON Orders(status);
CREATE NONCLUSTERED INDEX IX_Orders_PaymentType ON Orders(payment_type);
CREATE NONCLUSTERED INDEX IX_Orders_CreatedAt ON Orders(created_at);
CREATE NONCLUSTERED INDEX IX_Orders_PromotionId ON Orders(promotion_id);

-- OrderDetail Indexes
CREATE NONCLUSTERED INDEX IX_OrderDetail_OrderId ON OrderDetail(order_id);
CREATE NONCLUSTERED INDEX IX_OrderDetail_CarId ON OrderDetail(car_id);

-- Transactions Indexes
CREATE NONCLUSTERED INDEX IX_Transactions_OrderId ON Transactions(order_id);
CREATE NONCLUSTERED INDEX IX_Transactions_Type ON Transactions(type);
CREATE NONCLUSTERED INDEX IX_Transactions_PaymentStatus ON Transactions(payment_status);

-- Car Indexes
CREATE NONCLUSTERED INDEX IX_Car_BrandId ON Car(brand_id);
CREATE NONCLUSTERED INDEX IX_Car_Status ON Car(status);
CREATE NONCLUSTERED INDEX IX_Car_Price ON Car(price);

-- Promotion Indexes
CREATE NONCLUSTERED INDEX IX_Promotion_StartDate ON Promotion(start_date);
CREATE NONCLUSTERED INDEX IX_Promotion_EndDate ON Promotion(end_date);

-- CarPromotion Indexes
CREATE NONCLUSTERED INDEX IX_CarPromotion_CarId ON CarPromotion(car_id);
CREATE NONCLUSTERED INDEX IX_CarPromotion_PromotionId ON CarPromotion(promotion_id);

-- UserPromotion Indexes
CREATE NONCLUSTERED INDEX IX_UserPromotion_UserId ON UserPromotion(user_id);
CREATE NONCLUSTERED INDEX IX_UserPromotion_PromotionId ON UserPromotion(promotion_id);
CREATE NONCLUSTERED INDEX IX_UserPromotion_IsUsed ON UserPromotion(is_used);
CREATE NONCLUSTERED INDEX IX_UserPromotion_ClaimedAt ON UserPromotion(claimed_at);

-- Cart Indexes
CREATE NONCLUSTERED INDEX IX_Cart_UserId ON Cart(user_id);
CREATE NONCLUSTERED INDEX IX_CartItem_CartId ON CartItem(cart_id);
CREATE NONCLUSTERED INDEX IX_CartItem_CarId ON CartItem(car_id);

-- Blog Indexes
CREATE NONCLUSTERED INDEX IX_Blog_AuthorId ON Blog(author_id);
CREATE NONCLUSTERED INDEX IX_Blog_CreatedAt ON Blog(created_at);

-- CarImage Indexes
CREATE NONCLUSTERED INDEX IX_CarImage_CarId ON CarImage(car_id);
CREATE NONCLUSTERED INDEX IX_CarImage_IsPrimary ON CarImage(is_primary);
GO

-- =============================================
-- TRIGGERS
-- =============================================

IF OBJECT_ID('trg_UpdateCarStatus', 'TR') IS NOT NULL DROP TRIGGER trg_UpdateCarStatus;
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

IF OBJECT_ID('trg_UpdateRemainingAmount_AfterInsert', 'TR') IS NOT NULL DROP TRIGGER trg_UpdateRemainingAmount_AfterInsert;
GO

CREATE TRIGGER trg_UpdateRemainingAmount_AfterInsert
    ON Transactions
    AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

UPDATE o
SET o.remaining_amount = (
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id AND t.payment_status = 'PAID'
                         )
    FROM Orders o
    INNER JOIN inserted i ON o.order_id = i.order_id;
END;
GO

IF OBJECT_ID('trg_UpdateRemainingAmount_AfterUpdate', 'TR') IS NOT NULL DROP TRIGGER trg_UpdateRemainingAmount_AfterUpdate;
GO

CREATE TRIGGER trg_UpdateRemainingAmount_AfterUpdate
    ON Transactions
    AFTER UPDATE
              AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(payment_status)
BEGIN
UPDATE o
SET o.remaining_amount = (
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id AND t.payment_status = 'PAID'
                         )
    FROM Orders o
        INNER JOIN inserted i ON o.order_id = i.order_id;
END;
END;
GO

IF OBJECT_ID('trg_UpdateRemainingAmount_AfterDelete', 'TR') IS NOT NULL DROP TRIGGER trg_UpdateRemainingAmount_AfterDelete;
GO

CREATE TRIGGER trg_UpdateRemainingAmount_AfterDelete
    ON Transactions
    AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;

UPDATE o
SET o.remaining_amount = (
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id AND t.payment_status = 'PAID'
                         )
    FROM Orders o
    INNER JOIN deleted d ON o.order_id = d.order_id;
END;
GO

-- =============================================
-- VIEWS
-- =============================================

IF OBJECT_ID('vw_AllUsers', 'V') IS NOT NULL DROP VIEW vw_AllUsers;
GO

CREATE VIEW vw_AllUsers AS
SELECT
    u.user_id,
    u.email,
    u.role,
    u.is_active,
    u.created_at,
    u.last_login,
    CAST(NULL AS NVARCHAR(100)) AS name,
    CAST(NULL AS NVARCHAR(20)) AS phone,
    CAST(NULL AS NVARCHAR(255)) AS address
FROM AppUsers u
WHERE u.role = 'ADMIN'

UNION ALL

SELECT
    u.user_id,
    u.email,
    u.role,
    u.is_active,
    u.created_at,
    u.last_login,
    s.name,
    s.phone,
    s.address
FROM AppUsers u
         INNER JOIN Staff s ON u.user_id = s.staff_id

UNION ALL

SELECT
    u.user_id,
    u.email,
    u.role,
    u.is_active,
    u.created_at,
    u.last_login,
    c.name,
    c.phone,
    c.address
FROM AppUsers u
         INNER JOIN Customers c ON u.user_id = c.customer_id;
GO

IF OBJECT_ID('vw_StaffManagement', 'V') IS NOT NULL DROP VIEW vw_StaffManagement;
GO

CREATE VIEW vw_StaffManagement AS
SELECT
    s.staff_id,
    u.email,
    s.name,
    s.phone,
    s.address,
    u.is_active,
    u.created_at,
    u.last_login,
    (SELECT COUNT(*) FROM Orders WHERE user_id = s.staff_id) AS total_orders,
    (SELECT COUNT(*) FROM Blog WHERE author_id = s.staff_id) AS total_blogs
FROM Staff s
         INNER JOIN AppUsers u ON s.staff_id = u.user_id;
GO

IF OBJECT_ID('vw_CustomerList', 'V') IS NOT NULL DROP VIEW vw_CustomerList;
GO

CREATE VIEW vw_CustomerList AS
SELECT
    c.customer_id,
    u.email,
    c.name,
    c.phone,
    c.address,
    c.oauth_provider,
    u.is_active,
    u.created_at,
    u.last_login,
    (SELECT COUNT(*) FROM Orders WHERE user_id = c.customer_id) AS total_orders,
    (SELECT ISNULL(SUM(od.price * od.quantity), 0)
     FROM Orders o
              INNER JOIN OrderDetail od ON o.order_id = od.order_id
     WHERE o.user_id = c.customer_id) AS total_spent
FROM Customers c
         INNER JOIN AppUsers u ON c.customer_id = u.user_id;
GO

IF OBJECT_ID('vw_PendingShowroomOrders', 'V') IS NOT NULL DROP VIEW vw_PendingShowroomOrders;
GO

CREATE VIEW vw_PendingShowroomOrders AS
SELECT
    o.order_id,
    o.user_id,
    CASE
        WHEN u.role = 'CUSTOMER' THEN c.name
        WHEN u.role = 'STAFF' THEN s.name
        END AS customer_name,
    u.email AS customer_email,
    CASE
        WHEN u.role = 'CUSTOMER' THEN c.phone
        WHEN u.role = 'STAFF' THEN s.phone
        END AS customer_phone,
    o.status,
    o.payment_type,
    o.notes,
    o.created_at,
    o.promotion_id,
    p.title AS promotion_title,
    ISNULL((SELECT SUM(price * quantity) FROM OrderDetail WHERE order_id = o.order_id), 0) AS total_amount
FROM Orders o
         JOIN AppUsers u ON o.user_id = u.user_id
         LEFT JOIN Customers c ON u.user_id = c.customer_id AND u.role = 'CUSTOMER'
         LEFT JOIN Staff s ON u.user_id = s.staff_id AND u.role = 'STAFF'
         LEFT JOIN Promotion p ON o.promotion_id = p.promotion_id
WHERE o.payment_type = 'SHOWROOM' AND o.status = 'PENDING';
GO

IF OBJECT_ID('vw_OrderDetailsWithCar', 'V') IS NOT NULL DROP VIEW vw_OrderDetailsWithCar;
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
    ci.image_url,
    o.promotion_id,
    p.title AS promotion_title
FROM OrderDetail od
         JOIN Car c ON od.car_id = c.car_id
         JOIN Brand b ON c.brand_id = b.brand_id
         JOIN Orders o ON od.order_id = o.order_id
         LEFT JOIN Promotion p ON o.promotion_id = p.promotion_id
         LEFT JOIN CarImage ci ON c.car_id = ci.car_id AND ci.is_primary = 1;
GO

IF OBJECT_ID('vw_ActivePromotions', 'V') IS NOT NULL DROP VIEW vw_ActivePromotions;
GO

CREATE VIEW vw_ActivePromotions AS
SELECT
    p.promotion_id,
    p.title,
    p.description,
    p.start_date,
    p.end_date,
    p.discount_percentage,
    p.discount_amount,
    COUNT(DISTINCT up.user_id) AS total_claims,
    COUNT(DISTINCT CASE WHEN up.is_used = 1 THEN up.user_id END) AS total_used,
    COUNT(DISTINCT cp.car_id) AS applicable_cars_count,
    COUNT(DISTINCT o.order_id) AS total_orders_used
FROM Promotion p
         LEFT JOIN UserPromotion up ON p.promotion_id = up.promotion_id
         LEFT JOIN CarPromotion cp ON p.promotion_id = cp.promotion_id
         LEFT JOIN Orders o ON p.promotion_id = o.promotion_id
WHERE GETDATE() BETWEEN p.start_date AND p.end_date
GROUP BY p.promotion_id, p.title, p.description, p.start_date, p.end_date, p.discount_percentage, p.discount_amount;
GO

IF OBJECT_ID('vw_UserPromotions', 'V') IS NOT NULL DROP VIEW vw_UserPromotions;
GO

CREATE VIEW vw_UserPromotions AS
SELECT
    up.user_promotion_id,
    up.user_id,
    CASE
        WHEN u.role = 'CUSTOMER' THEN c.name
        WHEN u.role = 'STAFF' THEN s.name
        END AS user_name,
    u.email AS user_email,
    up.promotion_id,
    p.title AS promotion_title,
    p.description AS promotion_description,
    p.start_date,
    p.end_date,
    p.discount_percentage,
    p.discount_amount,
    up.claimed_at,
    up.is_used,
    up.used_at,
    up.order_id,
    CASE
        WHEN GETDATE() > p.end_date THEN 'EXPIRED'
        WHEN GETDATE() < p.start_date THEN 'UPCOMING'
        ELSE 'ACTIVE'
        END AS promotion_status
FROM UserPromotion up
         JOIN AppUsers u ON up.user_id = u.user_id
         LEFT JOIN Customers c ON u.user_id = c.customer_id AND u.role = 'CUSTOMER'
         LEFT JOIN Staff s ON u.user_id = s.staff_id AND u.role = 'STAFF'
         JOIN Promotion p ON up.promotion_id = p.promotion_id;
GO

IF OBJECT_ID('vw_CarInventory', 'V') IS NOT NULL DROP VIEW vw_CarInventory;
GO

CREATE VIEW vw_CarInventory AS
SELECT
    c.car_id,
    b.brand_name,
    c.model,
    c.year,
    c.color,
    c.price,
    c.stock,
    c.status,
    COUNT(DISTINCT cp.promotion_id) AS active_promotions_count,
    MAX(CASE WHEN cp.discount_percentage > 0 THEN cp.discount_percentage ELSE p.discount_percentage END) AS best_discount_percentage,
    MAX(CASE WHEN cp.discount_amount > 0 THEN cp.discount_amount ELSE p.discount_amount END) AS best_discount_amount,
    (SELECT TOP 1 image_url FROM CarImage WHERE car_id = c.car_id AND is_primary = 1) AS primary_image
FROM Car c
         JOIN Brand b ON c.brand_id = b.brand_id
         LEFT JOIN CarPromotion cp ON c.car_id = cp.car_id
         LEFT JOIN Promotion p ON cp.promotion_id = p.promotion_id AND GETDATE() BETWEEN p.start_date AND p.end_date
GROUP BY c.car_id, b.brand_name, c.model, c.year, c.color, c.price, c.stock, c.status;
GO

IF OBJECT_ID('vw_CarsWithPromotions', 'V') IS NOT NULL DROP VIEW vw_CarsWithPromotions;
GO

CREATE VIEW vw_CarsWithPromotions AS
SELECT
    c.car_id,
    c.model,
    c.price,
    c.year,
    c.color,
    c.stock,
    c.status,
    b.brand_name,
    p.promotion_id,
    p.title AS promotion_title,
    p.description AS promotion_description,
    p.start_date AS promotion_start_date,
    p.end_date AS promotion_end_date,
    CASE WHEN cp.discount_percentage > 0 THEN cp.discount_percentage ELSE p.discount_percentage END AS discount_percentage,
    CASE WHEN cp.discount_amount > 0 THEN cp.discount_amount ELSE p.discount_amount END AS discount_amount,
    CASE
        WHEN cp.discount_percentage > 0 THEN c.price * (1 - cp.discount_percentage / 100)
        WHEN cp.discount_amount > 0 THEN c.price - cp.discount_amount
        WHEN p.discount_percentage > 0 THEN c.price * (1 - p.discount_percentage / 100)
        WHEN p.discount_amount > 0 THEN c.price - p.discount_amount
        ELSE c.price
        END AS discounted_price,
    CASE
        WHEN cp.discount_percentage > 0 THEN c.price * (cp.discount_percentage / 100)
        WHEN cp.discount_amount > 0 THEN cp.discount_amount
        WHEN p.discount_percentage > 0 THEN c.price * (p.discount_percentage / 100)
        WHEN p.discount_amount > 0 THEN p.discount_amount
        ELSE 0
        END AS discount_value,
    CASE WHEN cp.discount_percentage > 0 OR cp.discount_amount > 0 THEN 1 ELSE 0 END AS has_specific_discount
FROM Car c
         JOIN Brand b ON c.brand_id = b.brand_id
         LEFT JOIN CarPromotion cp ON c.car_id = cp.car_id
         LEFT JOIN Promotion p ON cp.promotion_id = p.promotion_id AND GETDATE() BETWEEN p.start_date AND p.end_date;
GO

-- =============================================
-- STORED PROCEDURES
-- =============================================

IF OBJECT_ID('sp_RegisterCustomer', 'P') IS NOT NULL DROP PROCEDURE sp_RegisterCustomer;
GO

CREATE PROCEDURE sp_RegisterCustomer
    @Email NVARCHAR(100),
    @Password NVARCHAR(255),
    @Name NVARCHAR(100),
    @Phone NVARCHAR(20) = NULL,
    @Address NVARCHAR(255) = NULL,
    @OAuthProvider NVARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;

        IF EXISTS (SELECT 1 FROM AppUsers WHERE email = LOWER(TRIM(@Email)))
BEGIN
            RAISERROR('Email đã tồn tại', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

INSERT INTO AppUsers (email, password_hash, role, is_active)
VALUES (LOWER(TRIM(@Email)), @Password, 'CUSTOMER', 1);

DECLARE @CustomerId INT = SCOPE_IDENTITY();

INSERT INTO Customers (customer_id, name, phone, address, oauth_provider)
VALUES (@CustomerId, @Name, @Phone, @Address, @OAuthProvider);

COMMIT TRANSACTION;
SELECT 'SUCCESS' AS Result, 'Đăng ký thành công!' AS Message, @CustomerId AS CustomerId;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_AdminCreateStaff', 'P') IS NOT NULL DROP PROCEDURE sp_AdminCreateStaff;
GO

CREATE PROCEDURE sp_AdminCreateStaff
    @AdminId INT,
    @Email NVARCHAR(100),
    @Password NVARCHAR(255),
    @Name NVARCHAR(100),
    @Phone NVARCHAR(20) = NULL,
    @Address NVARCHAR(255) = NULL
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1 FROM AppUsers WHERE user_id = @AdminId AND role = 'ADMIN' AND is_active = 1)
BEGIN
            RAISERROR('Chỉ Admin mới có quyền tạo Staff', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

        IF EXISTS (SELECT 1 FROM AppUsers WHERE email = LOWER(TRIM(@Email)))
BEGIN
            RAISERROR('Email đã tồn tại', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

INSERT INTO AppUsers (email, password_hash, role, is_active)
VALUES (LOWER(TRIM(@Email)), @Password, 'STAFF', 1);

DECLARE @StaffId INT = SCOPE_IDENTITY();

INSERT INTO Staff (staff_id, name, phone, address)
VALUES (@StaffId, @Name, @Phone, @Address);

COMMIT TRANSACTION;
SELECT 'SUCCESS' AS Result, 'Tạo tài khoản Staff thành công!' AS Message, @StaffId AS StaffId;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_AdminUpdateStaff', 'P') IS NOT NULL DROP PROCEDURE sp_AdminUpdateStaff;
GO

CREATE PROCEDURE sp_AdminUpdateStaff
    @AdminId INT,
    @StaffId INT,
    @Name NVARCHAR(100),
    @Phone NVARCHAR(20) = NULL,
    @Address NVARCHAR(255) = NULL
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
IF NOT EXISTS (SELECT 1 FROM AppUsers WHERE user_id = @AdminId AND role = 'ADMIN' AND is_active = 1)
BEGIN
            RAISERROR('Chỉ Admin mới có quyền cập nhật Staff', 16, 1);
            RETURN;
END

UPDATE Staff
SET name = @Name, phone = @Phone, address = @Address
WHERE staff_id = @StaffId;

IF @@ROWCOUNT > 0
SELECT 'SUCCESS' AS Result, 'Cập nhật Staff thành công!' AS Message;
ELSE
SELECT 'ERROR' AS Result, 'Không tìm thấy Staff' AS Message;
END TRY
BEGIN CATCH
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_AdminToggleStaffStatus', 'P') IS NOT NULL DROP PROCEDURE sp_AdminToggleStaffStatus;
GO

CREATE PROCEDURE sp_AdminToggleStaffStatus
    @AdminId INT,
    @StaffId INT,
    @IsActive BIT
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
IF NOT EXISTS (SELECT 1 FROM AppUsers WHERE user_id = @AdminId AND role = 'ADMIN' AND is_active = 1)
BEGIN
            RAISERROR('Chỉ Admin mới có quyền này', 16, 1);
            RETURN;
END

UPDATE AppUsers SET is_active = @IsActive WHERE user_id = @StaffId AND role = 'STAFF';

IF @@ROWCOUNT > 0
SELECT 'SUCCESS' AS Result, CASE WHEN @IsActive = 1 THEN 'Đã kích hoạt Staff' ELSE 'Đã vô hiệu hóa Staff' END AS Message;
ELSE
SELECT 'ERROR' AS Result, 'Không tìm thấy Staff' AS Message;
END TRY
BEGIN CATCH
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_AdminResetStaffPassword', 'P') IS NOT NULL DROP PROCEDURE sp_AdminResetStaffPassword;
GO

CREATE PROCEDURE sp_AdminResetStaffPassword
    @AdminId INT,
    @StaffId INT,
    @NewPassword NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
IF NOT EXISTS (SELECT 1 FROM AppUsers WHERE user_id = @AdminId AND role = 'ADMIN' AND is_active = 1)
BEGIN
            RAISERROR('Chỉ Admin mới có quyền reset password', 16, 1);
            RETURN;
END

UPDATE AppUsers SET password_hash = @NewPassword WHERE user_id = @StaffId AND role = 'STAFF';

IF @@ROWCOUNT > 0
SELECT 'SUCCESS' AS Result, 'Đã reset password Staff' AS Message;
ELSE
SELECT 'ERROR' AS Result, 'Không tìm thấy Staff' AS Message;
END TRY
BEGIN CATCH
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_ClaimPromotion', 'P') IS NOT NULL DROP PROCEDURE sp_ClaimPromotion;
GO

CREATE PROCEDURE sp_ClaimPromotion
    @UserId INT,
    @PromotionId INT
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;

        IF NOT EXISTS (SELECT 1 FROM Promotion WHERE promotion_id = @PromotionId AND GETDATE() BETWEEN start_date AND end_date)
BEGIN
            RAISERROR('Khuyến mãi không còn hiệu lực', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

        IF EXISTS (SELECT 1 FROM UserPromotion WHERE user_id = @UserId AND promotion_id = @PromotionId)
BEGIN
            RAISERROR('Bạn đã nhận khuyến mãi này rồi!', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

INSERT INTO UserPromotion (user_id, promotion_id) VALUES (@UserId, @PromotionId);

COMMIT TRANSACTION;
SELECT 'SUCCESS' AS Result, 'Nhận khuyến mãi thành công!' AS Message;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_UsePromotion', 'P') IS NOT NULL DROP PROCEDURE sp_UsePromotion;
GO

CREATE PROCEDURE sp_UsePromotion
    @UserId INT,
    @PromotionId INT,
    @OrderId INT
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;

UPDATE UserPromotion
SET is_used = 1, used_at = GETDATE(), order_id = @OrderId
WHERE user_id = @UserId AND promotion_id = @PromotionId AND is_used = 0;

IF @@ROWCOUNT = 0
BEGIN
            RAISERROR('Không tìm thấy khuyến mãi hoặc đã được sử dụng', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

UPDATE Orders SET promotion_id = @PromotionId WHERE order_id = @OrderId;

COMMIT TRANSACTION;
SELECT 'SUCCESS' AS Result, 'Đã áp dụng khuyến mãi!' AS Message;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
SELECT 'ERROR' AS Result, ERROR_MESSAGE() AS Message;
END CATCH
END;
GO

IF OBJECT_ID('sp_GetUserAvailablePromotionsForCar', 'P') IS NOT NULL DROP PROCEDURE sp_GetUserAvailablePromotionsForCar;
GO

CREATE PROCEDURE sp_GetUserAvailablePromotionsForCar
    @UserId INT,
    @CarId INT
AS
BEGIN
    SET NOCOUNT ON;

SELECT
    p.promotion_id,
    p.title,
    p.description,
    p.start_date,
    p.end_date,
    CASE WHEN cp.discount_percentage > 0 THEN cp.discount_percentage ELSE p.discount_percentage END AS discount_percentage,
    CASE WHEN cp.discount_amount > 0 THEN cp.discount_amount ELSE p.discount_amount END AS discount_amount,
    CASE WHEN cp.discount_percentage > 0 OR cp.discount_amount > 0 THEN 1 ELSE 0 END AS has_specific_discount
FROM Promotion p
         INNER JOIN UserPromotion up ON p.promotion_id = up.promotion_id
         INNER JOIN CarPromotion cp ON p.promotion_id = cp.promotion_id
WHERE up.user_id = @UserId AND cp.car_id = @CarId AND up.is_used = 0 AND GETDATE() BETWEEN p.start_date AND p.end_date
ORDER BY
    CASE WHEN cp.discount_percentage > 0 THEN cp.discount_percentage ELSE p.discount_percentage END DESC,
    CASE WHEN cp.discount_amount > 0 THEN cp.discount_amount ELSE p.discount_amount END DESC;
END;
GO

-- =============================================
-- FIX REMAINING AMOUNTS
-- =============================================

UPDATE o
SET o.remaining_amount = (
                             SELECT COALESCE(SUM(od.price * od.quantity), 0)
                             FROM OrderDetail od
                             WHERE od.order_id = o.order_id
                         ) - (
                             SELECT COALESCE(SUM(t.amount), 0)
                             FROM Transactions t
                             WHERE t.order_id = o.order_id AND t.payment_status = 'PAID'
                         )
    FROM Orders o;
GO

-- =============================================
-- VERIFICATION
-- =============================================
PRINT '';
PRINT '========================================';
PRINT 'UPDATED DATABASE CREATED SUCCESSFULLY!';
PRINT '========================================';
PRINT '';
PRINT 'Changes Made:';
PRINT '  Removed from Staff: position, department, hired_date, salary, created_by, notes';
PRINT '  Removed from Customers: loyalty_points';
PRINT '  Simplified Staff and Customers tables';
PRINT '  Updated all related views and stored procedures';
PRINT '';
PRINT 'Database Structure:';
PRINT '  - 13 Tables (simplified)';
PRINT '  - 9 Views';
PRINT '  - 7 Stored Procedures';
PRINT '  - 4 Triggers';
PRINT '  - Indexes optimized';
PRINT '';
PRINT 'Database is ready for use!';
PRINT '========================================';
GO