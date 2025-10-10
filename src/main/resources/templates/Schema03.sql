-- =============================================
-- COMPLETE DATABASE SCHEMA - UPDATED VERSION
-- Car Sales Website System
-- Includes SHOWROOM Payment Support
-- =============================================

-- Drop existing database if needed (CAUTION: This will delete all data!)
-- USE master;
-- GO
-- IF EXISTS (SELECT * FROM sys.databases WHERE name = 'CarSalesWebsite')
-- BEGIN
--     ALTER DATABASE CarSalesWebsite SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
--     DROP DATABASE CarSalesWebsite;
-- END
-- GO

-- Create database
-- CREATE DATABASE CarSalesWebsite;
-- GO



-- =============================================
-- USERS TABLE
-- =============================================
IF OBJECT_ID('AppUsers', 'U') IS NOT NULL
    DROP TABLE AppUsers;
GO

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
IF OBJECT_ID('Brand', 'U') IS NOT NULL
    DROP TABLE Brand;
GO

CREATE TABLE Brand (
    brand_id INT IDENTITY(1,1) PRIMARY KEY,
    brand_name NVARCHAR(100) UNIQUE NOT NULL
);
GO

IF OBJECT_ID('Car', 'U') IS NOT NULL
    DROP TABLE Car;
GO

CREATE TABLE Car (
    car_id INT IDENTITY(1,1) PRIMARY KEY,
    brand_id INT,
    model NVARCHAR(100),
    price DECIMAL(15,2),
    status NVARCHAR(20) CHECK (status IN ('AVAILABLE','UNAVAILABLE')) DEFAULT 'AVAILABLE',
    description NVARCHAR(MAX),
    year INT,
    color NVARCHAR(50) NULL,
    stock INT DEFAULT 0,
    FOREIGN KEY (brand_id) REFERENCES Brand(brand_id)
);
GO

-- =============================================
-- CART TABLES
-- =============================================
IF OBJECT_ID('Cart', 'U') IS NOT NULL
    DROP TABLE Cart;
GO

CREATE TABLE Cart (
    cart_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);
GO

IF OBJECT_ID('CartItem', 'U') IS NOT NULL
    DROP TABLE CartItem;
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
-- ORDERS & DETAILS TABLES (UPDATED)
-- =============================================
IF OBJECT_ID('Orders', 'U') IS NOT NULL
    DROP TABLE Orders;
GO

CREATE TABLE Orders (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT,
    status NVARCHAR(20) CHECK (status IN ('PENDING','APPROVED','CANCELLED','COMPLETED')) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT GETDATE(),
    -- NEW PAYMENT FIELDS
    payment_type NVARCHAR(20) CHECK (payment_type IN ('FULL','DEPOSIT','SHOWROOM')),
    deposit_amount DECIMAL(15,2) NULL,
    remaining_amount DECIMAL(15,2) NULL,
    notes NVARCHAR(MAX) NULL,
    FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);
GO

IF OBJECT_ID('OrderDetail', 'U') IS NOT NULL
    DROP TABLE OrderDetail;
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
-- TRANSACTIONS TABLE (UPDATED)
-- =============================================
IF OBJECT_ID('Transactions', 'U') IS NOT NULL
    DROP TABLE Transactions;
GO

CREATE TABLE Transactions (
    transaction_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    amount DECIMAL(15,2),
    type NVARCHAR(20) CHECK (type IN ('FULL','DEPOSIT','SHOWROOM')),  -- UPDATED
    payment_status NVARCHAR(20) CHECK (payment_status IN ('PENDING','PAID','CANCELLED')) DEFAULT 'PENDING',  -- NEW
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);
GO

-- =============================================
-- PROMOTION TABLES
-- =============================================
IF OBJECT_ID('Promotion', 'U') IS NOT NULL
    DROP TABLE Promotion;
GO

CREATE TABLE Promotion (
    promotion_id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(100),
    description NVARCHAR(MAX),
    start_date DATE,
    end_date DATE
);
GO

IF OBJECT_ID('CarPromotion', 'U') IS NOT NULL
    DROP TABLE CarPromotion;
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
IF OBJECT_ID('Blog', 'U') IS NOT NULL
    DROP TABLE Blog;
GO

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
-- CAR IMAGE TABLE
-- =============================================
IF OBJECT_ID('CarImage', 'U') IS NOT NULL
    DROP TABLE CarImage;
GO

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

-- Drop trigger if exists
IF OBJECT_ID('trg_UpdateCarStatus', 'TR') IS NOT NULL
    DROP TRIGGER trg_UpdateCarStatus;
GO

-- Trigger to auto-update car status based on stock
CREATE TRIGGER trg_UpdateCarStatus
ON Car
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Update status to UNAVAILABLE when stock <= 0
    UPDATE Car
    SET status = 'UNAVAILABLE'
    FROM Car c
    INNER JOIN inserted i ON c.car_id = i.car_id
    WHERE i.stock <= 0 AND c.status = 'AVAILABLE';

    -- Update status to AVAILABLE when stock > 0
    UPDATE Car
    SET status = 'AVAILABLE'
    FROM Car c
    INNER JOIN inserted i ON c.car_id = i.car_id
    WHERE i.stock > 0 AND c.status = 'UNAVAILABLE';

    -- Log changes
    IF EXISTS (SELECT * FROM inserted WHERE stock <= 0)
    BEGIN
        PRINT 'Updated car(s) to UNAVAILABLE due to 0 stock';
    END

    IF EXISTS (SELECT * FROM inserted WHERE stock > 0)
    BEGIN
        PRINT 'Updated car(s) to AVAILABLE due to stock replenished';
    END
END;
GO

-- =============================================
-- INDEXES FOR PERFORMANCE
-- =============================================

-- Index on frequently queried columns
CREATE NONCLUSTERED INDEX IX_Orders_UserId
ON Orders(user_id);
GO

CREATE NONCLUSTERED INDEX IX_Orders_Status
ON Orders(status);
GO

CREATE NONCLUSTERED INDEX IX_Orders_PaymentType
ON Orders(payment_type);
GO

CREATE NONCLUSTERED INDEX IX_Transactions_OrderId
ON Transactions(order_id);
GO

CREATE NONCLUSTERED INDEX IX_Transactions_Type
ON Transactions(type);
GO

CREATE NONCLUSTERED INDEX IX_Transactions_PaymentStatus
ON Transactions(payment_status);
GO

CREATE NONCLUSTERED INDEX IX_Car_BrandId
ON Car(brand_id);
GO

CREATE NONCLUSTERED INDEX IX_Car_Status
ON Car(status);
GO

-- =============================================
-- VIEWS (OPTIONAL - USEFUL FOR REPORTING)
-- =============================================

-- View for pending showroom orders
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
    ISNULL((SELECT SUM(price * quantity) FROM OrderDetail WHERE order_id = o.order_id), 0) AS total_amount
FROM Orders o
JOIN AppUsers u ON o.user_id = u.user_id
WHERE o.payment_type = 'SHOWROOM'
AND o.status = 'PENDING';
GO

-- View for order details with car info
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
-- SAMPLE DATA (OPTIONAL)
-- =============================================

-- Insert sample brands
-- INSERT INTO Brand (brand_name) VALUES
-- ('BMW'), ('Mercedes-Benz'), ('Audi'), ('Toyota'), ('Honda');

-- Insert sample admin user
-- INSERT INTO AppUsers (name, email, password_hash, role) VALUES
-- ('Admin', 'admin@carsales.com', 'hashed_password_here', 'ADMIN');

-- =============================================
-- VERIFICATION QUERIES
-- =============================================

PRINT '========================================';
PRINT 'DATABASE SCHEMA CREATED SUCCESSFULLY!';
PRINT '========================================';
PRINT '';
PRINT 'Tables Created:';
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;
GO

PRINT '';
PRINT 'Views Created:';
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS
ORDER BY TABLE_NAME;
GO

PRINT '';
PRINT 'Indexes Created:';
SELECT
    OBJECT_NAME(object_id) AS TableName,
    name AS IndexName,
    type_desc AS IndexType
FROM sys.indexes
WHERE object_id IN (
    SELECT object_id
    FROM sys.objects
    WHERE type = 'U'
)
AND name IS NOT NULL
ORDER BY TableName, IndexName;
GO

PRINT '';
PRINT 'Key Features:';
PRINT '✓ Support for 3 payment types: FULL, DEPOSIT, SHOWROOM';
PRINT '✓ Transaction payment status tracking: PENDING, PAID, CANCELLED';
PRINT '✓ Order payment information: payment_type, deposit_amount, remaining_amount, notes';
PRINT '✓ Automatic car status update based on stock';
PRINT '✓ Performance indexes on key columns';
PRINT '✓ Useful views for reporting';
PRINT '';
PRINT '========================================';
GO