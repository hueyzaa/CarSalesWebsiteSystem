USE [master]
GO
/****** Object:  Database [CarSalesWebsite]    Script Date: 09/10/2025 2:51:26 CH ******/
CREATE DATABASE [CarSalesWebsite]
 CONTAINMENT = NONE
 ON  PRIMARY
( NAME = N'CarSalesWebsite', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.HUYNG\MSSQL\DATA\CarSalesWebsite.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON
( NAME = N'CarSalesWebsite_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.HUYNG\MSSQL\DATA\CarSalesWebsite_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [CarSalesWebsite] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [CarSalesWebsite].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [CarSalesWebsite] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [CarSalesWebsite] SET ANSI_NULLS OFF
GO
ALTER DATABASE [CarSalesWebsite] SET ANSI_PADDING OFF
GO
ALTER DATABASE [CarSalesWebsite] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [CarSalesWebsite] SET ARITHABORT OFF
GO
ALTER DATABASE [CarSalesWebsite] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [CarSalesWebsite] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [CarSalesWebsite] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [CarSalesWebsite] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [CarSalesWebsite] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [CarSalesWebsite] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [CarSalesWebsite] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [CarSalesWebsite] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [CarSalesWebsite] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [CarSalesWebsite] SET  DISABLE_BROKER
GO
ALTER DATABASE [CarSalesWebsite] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [CarSalesWebsite] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [CarSalesWebsite] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [CarSalesWebsite] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [CarSalesWebsite] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [CarSalesWebsite] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [CarSalesWebsite] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [CarSalesWebsite] SET RECOVERY SIMPLE
GO
ALTER DATABASE [CarSalesWebsite] SET  MULTI_USER
GO
ALTER DATABASE [CarSalesWebsite] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [CarSalesWebsite] SET DB_CHAINING OFF
GO
ALTER DATABASE [CarSalesWebsite] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF )
GO
ALTER DATABASE [CarSalesWebsite] SET TARGET_RECOVERY_TIME = 60 SECONDS
GO
ALTER DATABASE [CarSalesWebsite] SET DELAYED_DURABILITY = DISABLED
GO
ALTER DATABASE [CarSalesWebsite] SET ACCELERATED_DATABASE_RECOVERY = OFF
GO
ALTER DATABASE [CarSalesWebsite] SET QUERY_STORE = ON
GO
ALTER DATABASE [CarSalesWebsite] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [CarSalesWebsite]
GO
/****** Object:  Table [dbo].[AppUsers]    Script Date: 09/10/2025 2:51:26 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[AppUsers](
    [user_id] [int] IDENTITY(1,1) NOT NULL,
    [name] [nvarchar](100) NOT NULL,
    [email] [nvarchar](100) NOT NULL,
    [password_hash] [nvarchar](255) NULL,
    [role] [nvarchar](20) NULL,
    [oauth_provider] [nvarchar](50) NULL,
    [created_at] [datetime] NULL,
    [phone] [nvarchar](20) NULL,
    [address] [nvarchar](255) NULL,
    PRIMARY KEY CLUSTERED
(
[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    UNIQUE NONCLUSTERED
(
[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Blog]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Blog](
    [blog_id] [int] IDENTITY(1,1) NOT NULL,
    [title] [nvarchar](200) NULL,
    [content] [nvarchar](max) NULL,
    [author_id] [int] NULL,
    [created_at] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[blog_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Brand]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Brand](
    [brand_id] [int] IDENTITY(1,1) NOT NULL,
    [brand_name] [nvarchar](100) NOT NULL,
    PRIMARY KEY CLUSTERED
(
[brand_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
    UNIQUE NONCLUSTERED
(
[brand_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Car]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Car](
    [car_id] [int] IDENTITY(1,1) NOT NULL,
    [brand_id] [int] NULL,
    [model] [nvarchar](100) NULL,
    [price] [decimal](15, 2) NULL,
    [status] [nvarchar](20) NULL,
    [description] [nvarchar](max) NULL,
    PRIMARY KEY CLUSTERED
(
[car_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[CarImage]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[CarImage](
    [image_id] [int] IDENTITY(1,1) NOT NULL,
    [car_id] [int] NOT NULL,
    [image_url] [nvarchar](255) NOT NULL,
    [is_primary] [bit] NULL,
    [created_at] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[image_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[CarPromotion]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[CarPromotion](
    [car_id] [int] NOT NULL,
    [promotion_id] [int] NOT NULL,
     PRIMARY KEY CLUSTERED
    (
    [car_id] ASC,
[promotion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Cart]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Cart](
    [cart_id] [int] IDENTITY(1,1) NOT NULL,
    [user_id] [int] NULL,
    [created_at] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[cart_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[CartItem]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[CartItem](
    [cart_item_id] [int] IDENTITY(1,1) NOT NULL,
    [cart_id] [int] NULL,
    [car_id] [int] NULL,
    [quantity] [int] NULL,
    PRIMARY KEY CLUSTERED
(
[cart_item_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[OrderDetail]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[OrderDetail](
    [order_detail_id] [int] IDENTITY(1,1) NOT NULL,
    [order_id] [int] NULL,
    [car_id] [int] NULL,
    [price] [decimal](15, 2) NULL,
    [quantity] [int] NULL,
    PRIMARY KEY CLUSTERED
(
[order_detail_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Orders]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Orders](
    [order_id] [int] IDENTITY(1,1) NOT NULL,
    [user_id] [int] NULL,
    [status] [nvarchar](20) NULL,
    [created_at] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Promotion]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Promotion](
    [promotion_id] [int] IDENTITY(1,1) NOT NULL,
    [title] [nvarchar](100) NULL,
    [description] [nvarchar](max) NULL,
    [start_date] [date] NULL,
    [end_date] [date] NULL,
    PRIMARY KEY CLUSTERED
(
[promotion_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
    GO
/****** Object:  Table [dbo].[Transactions]    Script Date: 09/10/2025 2:51:26 CH ******/
    SET ANSI_NULLS ON
    GO
    SET QUOTED_IDENTIFIER ON
    GO
CREATE TABLE [dbo].[Transactions](
    [transaction_id] [int] IDENTITY(1,1) NOT NULL,
    [order_id] [int] NULL,
    [amount] [decimal](15, 2) NULL,
    [type] [nvarchar](20) NULL,
    [created_at] [datetime] NULL,
    PRIMARY KEY CLUSTERED
(
[transaction_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
ALTER TABLE [dbo].[AppUsers] ADD  DEFAULT ('CUSTOMER') FOR [role]
    GO
ALTER TABLE [dbo].[AppUsers] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[Blog] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[Car] ADD  DEFAULT ('AVAILABLE') FOR [status]
    GO
ALTER TABLE [dbo].[CarImage] ADD  DEFAULT ((0)) FOR [is_primary]
    GO
ALTER TABLE [dbo].[CarImage] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[Cart] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[CartItem] ADD  DEFAULT ((1)) FOR [quantity]
    GO
ALTER TABLE [dbo].[OrderDetail] ADD  DEFAULT ((1)) FOR [quantity]
    GO
ALTER TABLE [dbo].[Orders] ADD  DEFAULT ('PENDING') FOR [status]
    GO
ALTER TABLE [dbo].[Orders] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[Transactions] ADD  DEFAULT (getdate()) FOR [created_at]
    GO
ALTER TABLE [dbo].[Blog]  WITH NOCHECK ADD FOREIGN KEY([author_id])
    REFERENCES [dbo].[AppUsers] ([user_id])
    GO
ALTER TABLE [dbo].[Car]  WITH NOCHECK ADD FOREIGN KEY([brand_id])
    REFERENCES [dbo].[Brand] ([brand_id])
    GO
ALTER TABLE [dbo].[CarImage]  WITH NOCHECK ADD FOREIGN KEY([car_id])
    REFERENCES [dbo].[Car] ([car_id])
    ON DELETE CASCADE
GO
ALTER TABLE [dbo].[CarPromotion]  WITH NOCHECK ADD FOREIGN KEY([car_id])
    REFERENCES [dbo].[Car] ([car_id])
    GO
ALTER TABLE [dbo].[CarPromotion]  WITH NOCHECK ADD FOREIGN KEY([promotion_id])
    REFERENCES [dbo].[Promotion] ([promotion_id])
    GO
ALTER TABLE [dbo].[Cart]  WITH NOCHECK ADD FOREIGN KEY([user_id])
    REFERENCES [dbo].[AppUsers] ([user_id])
    GO
ALTER TABLE [dbo].[CartItem]  WITH NOCHECK ADD FOREIGN KEY([car_id])
    REFERENCES [dbo].[Car] ([car_id])
    GO
ALTER TABLE [dbo].[CartItem]  WITH NOCHECK ADD FOREIGN KEY([cart_id])
    REFERENCES [dbo].[Cart] ([cart_id])
    GO
ALTER TABLE [dbo].[OrderDetail]  WITH NOCHECK ADD FOREIGN KEY([car_id])
    REFERENCES [dbo].[Car] ([car_id])
    GO
ALTER TABLE [dbo].[OrderDetail]  WITH NOCHECK ADD FOREIGN KEY([order_id])
    REFERENCES [dbo].[Orders] ([order_id])
    GO
ALTER TABLE [dbo].[Orders]  WITH NOCHECK ADD FOREIGN KEY([user_id])
    REFERENCES [dbo].[AppUsers] ([user_id])
    GO
ALTER TABLE [dbo].[Transactions]  WITH NOCHECK ADD FOREIGN KEY([order_id])
    REFERENCES [dbo].[Orders] ([order_id])
    GO
ALTER TABLE [dbo].[AppUsers]  WITH NOCHECK ADD CHECK  (([role]='ADMIN' OR [role]='STAFF' OR [role]='CUSTOMER' OR [role]='GUEST'))
    GO
ALTER TABLE [dbo].[Car]  WITH NOCHECK ADD CHECK  (([status]='UNAVAILABLE' OR [status]='AVAILABLE'))
    GO
ALTER TABLE [dbo].[Orders]  WITH NOCHECK ADD CHECK  (([status]='COMPLETED' OR [status]='CANCELLED' OR [status]='APPROVED' OR [status]='PENDING'))
    GO
ALTER TABLE [dbo].[Transactions]  WITH NOCHECK ADD CHECK  (([type]='DEPOSIT' OR [type]='FULL'))
    GO
    USE [master]
    GO
ALTER DATABASE [CarSalesWebsite] SET  READ_WRITE
GO
