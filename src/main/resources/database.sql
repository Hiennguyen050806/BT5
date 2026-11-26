-- ========================================================
-- CO SO DU LIEU CHO BAI TAP 5 (SPRING BOOT 4 + JSP/JSTL)
-- Tuong thich 100% voi database webst2 tu folder D:\web
-- ========================================================

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'webst2')
BEGIN
    CREATE DATABASE webst2;
END
GO

USE webst2;
GO

-- 1. Bang users
IF OBJECT_ID('dbo.users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.users (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        email VARCHAR(100) NOT NULL,
        fullname NVARCHAR(100) NULL,
        phone VARCHAR(20) NULL,
        avatar NVARCHAR(255) NULL,
        roleid INT NOT NULL DEFAULT 3, -- 1: Admin, 2: Manager, 3: User
        status INT NOT NULL DEFAULT 1, -- 1: Hoat dong, 0: Khoa
        otpCode VARCHAR(10) NULL,
        otpExpiry DATETIME2 NULL,
        createdDate DATETIME2 NULL DEFAULT GETDATE()
    );
END
GO

-- 2. Bang categories
IF OBJECT_ID('dbo.categories', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.categories (
        CategoryId INT IDENTITY(1,1) PRIMARY KEY,
        CategoryName NVARCHAR(255) NOT NULL,
        Images NVARCHAR(500) NULL,
        Status INT NOT NULL DEFAULT 1 -- 1: Hoat dong, 0: Khoa
    );
END
GO

-- 3. Bang Videos (giu nguyen quan he tu BaiTap2_JPA trong folder D:\web)
IF OBJECT_ID('dbo.Videos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Videos (
        VideoId VARCHAR(255) PRIMARY KEY,
        Active INT NULL,
        Description NVARCHAR(500) NULL,
        Poster NVARCHAR(500) NULL,
        Title NVARCHAR(500) NULL,
        Views INT NULL,
        CategoryId INT NULL,
        userId INT NULL,
        CONSTRAINT FK_videos_categories FOREIGN KEY (CategoryId) REFERENCES dbo.categories(CategoryId),
        CONSTRAINT FK_videos_users FOREIGN KEY (userId) REFERENCES dbo.users(id)
    );
END
GO

-- Du lieu mau Admin (mat khau: 123 - SHA-256: a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3)
IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE username = 'admin')
BEGIN
    INSERT INTO dbo.users(username, password, email, fullname, phone, avatar, roleid, status, createdDate)
    VALUES ('admin', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'admin@iotstar.vn', N'Nguyễn Hữu Trung (Admin)', '0908617108', 'avatar.png', 1, 1, GETDATE());
END
GO
