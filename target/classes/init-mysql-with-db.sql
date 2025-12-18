-- 创建数据库 - 兼容MySQL 5.7
CREATE DATABASE IF NOT EXISTS library_db CHARACTER SET utf8 COLLATE utf8_general_ci;

-- 使用数据库
USE library_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建管理员表
CREATE TABLE IF NOT EXISTS admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建图书表 - 兼容MySQL 5.7
CREATE TABLE IF NOT EXISTS book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    publish_date DATE,
    category VARCHAR(50),
    description TEXT,
    total_quantity INT NOT NULL DEFAULT 1,
    available_quantity INT NOT NULL DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建借阅记录表
CREATE TABLE IF NOT EXISTS borrow_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP NULL,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'BORROWED',
    FOREIGN KEY (user_id) REFERENCES `user`(id),
    FOREIGN KEY (book_id) REFERENCES book(id)
);

-- 数据库初始化脚本（包含表结构和默认数据）

-- 插入默认管理员账户
REPLACE INTO admin (username, password, email) VALUES ('admin', '$2a$10$h9H6L/FV4WBxuLzaj/.vYOZEDgErC2aZecShCGT605fiOlhBDpEEO', 'admin@library.com');

-- 插入一些示例图书数据
INSERT IGNORE INTO book (isbn, title, author, publisher, publish_date, category, description, total_quantity, available_quantity) VALUES
('978-7-111-38554-1', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', '2007-06-01', '计算机', 'Java编程经典教材', 5, 5),
('978-7-115-27946-0', 'Spring实战', 'Craig Walls', '人民邮电出版社', '2012-09-01', '计算机', 'Spring框架实战指南', 3, 3),
('978-7-121-23255-8', 'MySQL必知必会', 'Ben Forta', '电子工业出版社', '2013-05-01', '计算机', 'MySQL入门教程', 4, 4);