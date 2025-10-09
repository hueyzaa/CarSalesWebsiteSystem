-- =========================================
-- 🚗 CAR SALES WEBSITE DATABASE (UPDATED)
-- =========================================

-- USERS TABLE
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

-- BRAND & CAR
CREATE TABLE Brand (
                       brand_id INT IDENTITY(1,1) PRIMARY KEY,
                       brand_name NVARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Car (
                     car_id INT IDENTITY(1,1) PRIMARY KEY,
                     brand_id INT,
                     model NVARCHAR(100),
                     price DECIMAL(15,2),
                     status NVARCHAR(20) CHECK (status IN ('AVAILABLE','UNAVAILABLE')) DEFAULT 'AVAILABLE',
                     description NVARCHAR(MAX),
                     FOREIGN KEY (brand_id) REFERENCES Brand(brand_id)
);

-- CART
CREATE TABLE Cart (
                      cart_id INT IDENTITY(1,1) PRIMARY KEY,
                      user_id INT,
                      created_at DATETIME DEFAULT GETDATE(),
                      FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);

CREATE TABLE CartItem (
                          cart_item_id INT IDENTITY(1,1) PRIMARY KEY,
                          cart_id INT,
                          car_id INT,
                          quantity INT DEFAULT 1,
                          FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
                          FOREIGN KEY (car_id) REFERENCES Car(car_id)
);

-- ORDERS & DETAILS
CREATE TABLE Orders (
                        order_id INT IDENTITY(1,1) PRIMARY KEY,
                        user_id INT,
                        status NVARCHAR(20) CHECK (status IN ('PENDING','APPROVED','CANCELLED','COMPLETED')) DEFAULT 'PENDING',
                        created_at DATETIME DEFAULT GETDATE(),
                        FOREIGN KEY (user_id) REFERENCES AppUsers(user_id)
);

CREATE TABLE OrderDetail (
                             order_detail_id INT IDENTITY(1,1) PRIMARY KEY,
                             order_id INT,
                             car_id INT,
                             price DECIMAL(15,2),
                             quantity INT DEFAULT 1,
                             FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                             FOREIGN KEY (car_id) REFERENCES Car(car_id)
);

-- TRANSACTIONS
CREATE TABLE Transactions (
                              transaction_id INT IDENTITY(1,1) PRIMARY KEY,
                              order_id INT,
                              amount DECIMAL(15,2),
                              type NVARCHAR(20) CHECK (type IN ('FULL','DEPOSIT')),
                              created_at DATETIME DEFAULT GETDATE(),
                              FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

-- PROMOTION
CREATE TABLE Promotion (
                           promotion_id INT IDENTITY(1,1) PRIMARY KEY,
                           title NVARCHAR(100),
                           description NVARCHAR(MAX),
                           start_date DATE,
                           end_date DATE
);

CREATE TABLE CarPromotion (
                              car_id INT,
                              promotion_id INT,
                              PRIMARY KEY(car_id, promotion_id),
                              FOREIGN KEY (car_id) REFERENCES Car(car_id),
                              FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);

-- BLOG
CREATE TABLE Blog (
                      blog_id INT IDENTITY(1,1) PRIMARY KEY,
                      title NVARCHAR(200),
                      content NVARCHAR(MAX),
                      author_id INT,
                      created_at DATETIME DEFAULT GETDATE(),
                      FOREIGN KEY (author_id) REFERENCES AppUsers(user_id)
);

-- CAR IMAGE
CREATE TABLE CarImage (
                          image_id INT IDENTITY(1,1) PRIMARY KEY,
                          car_id INT NOT NULL,
                          image_url NVARCHAR(255) NOT NULL,
                          is_primary BIT DEFAULT 0,
                          created_at DATETIME DEFAULT GETDATE(),
                          FOREIGN KEY (car_id) REFERENCES Car(car_id) ON DELETE CASCADE
);
