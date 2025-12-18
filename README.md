# 图书馆管理系统

## 项目概述

这是一个基于Spring Boot和Node.js前后端分离架构的图书馆管理系统，提供完整的图书借阅、用户管理和系统管理功能。系统采用MySQL数据库存储数据，支持用户注册登录、图书查询借阅、管理员后台管理等核心功能。

## 核心功能

### 用户功能
- 用户注册与登录（JWT认证）
- 图书查询与浏览（支持按书名、作者搜索）
- 图书借阅与归还
- 个人借阅记录查看（包含完整的借阅人、书名、作者、借阅时间等信息）
- 图书库存查询
- 用户信息管理

### 管理员功能
- 管理员登录认证
- 图书信息管理（增删改查）
- 用户信息管理（查看、编辑、删除用户）
- 借阅记录管理（查看所有用户的借阅记录）
- 图书库存管理
- 系统数据统计

## 技术栈

### 后端技术
- **后端框架**: Spring Boot 2.7.14
- **数据库**: MySQL 8.0
- **持久层**: MyBatis 2.3.1
- **安全认证**: JWT (JSON Web Token)
- **构建工具**: Maven
- **开发环境**: Java 11+

### 前端技术
- **前端框架**: 原生JavaScript
- **前端服务器**: Node.js
- **样式**: CSS3
- **页面结构**: HTML5

## 环境要求

### 后端环境
- **JDK**: 11 或更高版本
- **MySQL**: 8.0 或更高版本
- **Maven**: 3.6 或更高版本
- **IDE**: IntelliJ IDEA 或 Eclipse

### 前端环境
- **Node.js**: 14.0 或更高版本
- **npm**: 6.0 或更高版本

## 安装与配置

### 1. 数据库配置

1. 创建MySQL数据库：
   ```sql
   CREATE DATABASE library_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 修改数据库连接配置（application.properties）：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=123456
   ```

### 2. 项目初始化

1. 克隆项目到本地
2. 进入项目目录
3. 执行数据库初始化脚本：
   ```bash
   mysql -u root -p library_db < src/main/resources/init-mysql-with-db.sql
   ```

### 3. 项目启动

#### 方式一：一键启动（推荐）
Windows用户可以直接运行批处理文件：
```bash
启动系统.bat
```

#### 方式二：手动启动

1. 启动后端服务器：
   ```bash
   mvn spring-boot:run
   ```
   后端将在 http://localhost:8082 启动

2. 启动前端服务器（新开终端）：
   ```bash
   cd frontend
   node server.js
   ```
   前端将在 http://localhost:8081 启动

3. 访问应用程序：
   - 前端界面: http://localhost:8081
   - 后端API: http://localhost:8082

## API接口文档

### 用户相关接口

#### 用户注册
- **URL**: POST /api/user/register
- **请求体**:
  ```json
  {
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }
  ```

#### 用户登录
- **URL**: POST /api/user/login
- **请求体**:
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "user": {
        "id": 1,
        "username": "testuser",
        "role": "USER",
        "email": "test@example.com"
      },
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
  }
  ```

#### 获取用户信息
- **URL**: GET /api/user/profile
- **认证**: 需要JWT Token

### 图书相关接口

#### 获取所有图书
- **URL**: GET /api/books
- **认证**: 需要JWT Token

#### 根据ID获取图书
- **URL**: GET /api/books/{id}
- **认证**: 需要JWT Token

#### 搜索图书
- **URL**: GET /api/books/search?keyword=Java
- **认证**: 需要JWT Token

#### 添加图书（管理员）
- **URL**: POST /api/books
- **请求体**:
  ```json
  {
    "isbn": "978-7-111-21308-8",
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "publisher": "机械工业出版社",
    "publishDate": "2007-06-01",
    "category": "计算机",
    "description": "Java编程经典教材",
    "totalQuantity": 10
  }
  ```
- **认证**: 需要管理员JWT Token

#### 更新图书（管理员）
- **URL**: PUT /api/books/{id}
- **认证**: 需要管理员JWT Token

#### 删除图书（管理员）
- **URL**: DELETE /api/books/{id}
- **认证**: 需要管理员JWT Token

#### 更新图书库存（管理员）
- **URL**: PUT /api/books/{id}/stock
- **请求体**:
  ```json
  {
    "availableQuantity": 5
  }
  ```
- **认证**: 需要管理员JWT Token

### 借阅相关接口

#### 借阅图书
- **URL**: POST /api/borrow/{bookId}
- **认证**: 需要JWT Token

#### 归还图书
- **URL**: PUT /api/borrow/return/{recordId}
- **认证**: 需要JWT Token

#### 获取当前用户借阅记录
- **URL**: GET /api/borrow/user
- **认证**: 需要JWT Token
- **响应**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "userName": "testuser",
        "bookId": 1,
        "bookTitle": "Java编程思想",
        "bookAuthor": "Bruce Eckel",
        "bookIsbn": "978-7-111-21308-8",
        "borrowDate": "2025-12-17",
        "returnDate": "2025-12-17",
        "dueDate": "2026-01-16",
        "status": "RETURNED"
      }
    ]
  }
  ```

#### 获取所有借阅记录（管理员）
- **URL**: GET /api/borrow/all
- **认证**: 需要管理员JWT Token

#### 获取指定用户借阅记录（管理员）
- **URL**: GET /api/borrow/user/{userId}
- **认证**: 需要管理员JWT Token

#### 获取借阅记录详情
- **URL**: GET /api/borrow/record/{recordId}
- **认证**: 需要JWT Token

### 管理员相关接口

#### 管理员登录
- **URL**: POST /api/admin/login
- **请求体**:
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```

#### 管理员注册
- **URL**: POST /api/admin/register
- **请求体**:
  ```json
  {
    "username": "admin",
    "password": "admin123",
    "email": "admin@example.com"
  }
  ```

#### 获取管理员信息
- **URL**: GET /api/admin/profile
- **认证**: 需要管理员JWT Token

#### 获取所有用户（管理员）
- **URL**: GET /api/admin/users
- **认证**: 需要管理员JWT Token

#### 根据ID获取用户（管理员）
- **URL**: GET /api/admin/users/{id}
- **认证**: 需要管理员JWT Token

#### 更新用户信息（管理员）
- **URL**: PUT /api/admin/users/{id}
- **认证**: 需要管理员JWT Token

#### 删除用户（管理员）
- **URL**: DELETE /api/admin/users/{id}
- **认证**: 需要管理员JWT Token

## 项目结构

```
src/main/java/com/example/library/
├── common/          # 通用类
│   └── ApiResponse.java
├── config/          # 配置类
│   ├── CorsConfig.java
│   ├── FrontendAutoStarter.java
│   └── PasswordConfig.java
├── controller/      # 控制器层
│   ├── AdminController.java
│   ├── BookController.java
│   ├── BorrowController.java
│   ├── HomeController.java
│   └── UserController.java
├── dto/             # 数据传输对象
│   └── BorrowRecordDTO.java
├── entity/          # 实体类
│   ├── Admin.java
│   ├── Book.java
│   ├── BorrowRecord.java
│   └── User.java
├── exception/       # 异常处理
│   └── GlobalExceptionHandler.java
├── mapper/          # MyBatis映射器
│   ├── AdminMapper.java
│   ├── BookMapper.java
│   ├── BorrowMapper.java
│   └── UserMapper.java
├── security/        # 安全配置
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   └── SecurityConfig.java
├── service/         # 服务层
│   ├── AdminService.java
│   ├── BookService.java
│   ├── BorrowService.java
│   └── UserService.java
├── util/            # 工具类
│   └── JwtTokenUtil.java
└── LibraryManagementSystemApplication.java  # 应用程序入口

src/main/resources/
├── application.properties   # 应用配置
├── init-mysql-with-db.sql   # 数据库初始化脚本
└── mapper/                  # MyBatis XML映射文件
    ├── AdminMapper.xml
    ├── BookMapper.xml
    ├── BorrowMapper.xml
    └── UserMapper.xml

frontend/
├── css/                    # 样式文件
│   └── style.css
├── js/                     # JavaScript文件
│   ├── admin-dashboard.js
│   ├── admin-login.js
│   ├── api.js
│   ├── auth.js
│   └── user-dashboard.js
├── pages/                  # HTML页面
│   ├── admin-dashboard.html
│   ├── admin-login.html
│   └── user-dashboard.html
├── index.html              # 首页
└── server.js               # 前端服务器
```

## 使用说明

### 普通用户使用流程

1. **注册账户**: 使用用户名、密码和邮箱注册新账户
2. **登录系统**: 使用注册的用户名和密码登录
3. **浏览图书**: 查看系统中的所有图书信息，包括库存数量
4. **搜索图书**: 根据关键词搜索特定图书（支持按书名、作者搜索）
5. **借阅图书**: 选择想要借阅的图书进行借阅
6. **查看借阅记录**: 查看当前和历史借阅记录，包含完整的借阅人、书名、作者、借阅时间、归还时间等信息
7. **归还图书**: 归还已借阅的图书
8. **个人信息管理**: 查看和更新个人信息

### 管理员使用流程

1. **管理员登录**: 使用管理员账户登录系统
2. **图书管理**: 添加、修改、删除图书信息，管理图书库存
3. **用户管理**: 查看、编辑、删除系统中的用户信息
4. **借阅管理**: 查看和管理所有用户的借阅记录，包含完整的借阅人、书名、作者等信息
5. **系统统计**: 查看图书借阅统计等系统数据
6. **管理员注册**: 可以注册新的管理员账户

### 系统特点

- **前后端分离架构**: 后端使用Spring Boot，前端使用原生JavaScript和Node.js
- **JWT认证**: 安全的用户认证机制，支持用户和管理员不同角色
- **完整的借阅记录**: 包含借阅人、书名、作者、借阅时间、归还时间等完整信息
- **响应式设计**: 前端界面适配不同设备屏幕
- **一键启动**: Windows用户可以通过批处理文件一键启动整个系统

## 注意事项

1. **系统启动**: 使用`启动系统.bat`批处理文件可以一键启动整个系统，包括后端和前端服务器
2. **端口配置**: 后端运行在8082端口，前端运行在8081端口，确保这些端口未被占用
3. **数据库初始化**: 系统首次运行时会自动执行数据库初始化脚本
4. **用户密码**: 用户密码采用加密存储，确保安全性
5. **JWT Token**: JWT Token有效期为24小时，过期需要重新登录
6. **图书借阅**: 图书借阅期限默认为30天，到期前可进行续借
7. **借阅记录**: 借阅记录现在包含完整的借阅人、书名、作者、借阅时间、归还时间等信息
8. **前端服务器**: 前端使用Node.js服务器提供静态资源服务

## 系统架构

### 后端架构
- **Spring Boot**: 提供RESTful API服务
- **MyBatis**: ORM框架，处理数据库操作
- **JWT**: 用户认证和授权
- **Spring Security**: 安全框架

### 前端架构
- **Node.js**: 提供静态资源服务器
- **原生JavaScript**: 实现前端逻辑
- **AJAX**: 与后端API进行异步通信
- **LocalStorage**: 存储用户认证信息

## 常见问题

### Q: 如何重置管理员密码？
A: 可以直接通过数据库更新admin表中的password字段，或者使用系统提供的密码重置功能。

### Q: 如何修改系统端口？
A: 在application.properties文件中修改`server.port=端口号`配置。前端端口在frontend/server.js中修改。

### Q: 如何备份数据？
A: 定期使用MySQL的mysqldump工具备份library_db数据库。

### Q: 系统启动失败怎么办？
A: 请检查以下几项：
1. 确保MySQL服务已启动
2. 确保端口8081和8082未被占用
3. 确保Node.js和Maven环境已正确安装

### Q: 借阅记录显示不完整怎么办？
A: 系统已更新为显示完整的借阅记录信息，包括借阅人、书名、作者、借阅时间、归还时间等。如果仍有问题，请检查后端服务是否正常启动。

### Q: 如何添加测试数据？
A: 可以通过管理员登录后，在图书管理界面添加图书，或者直接在数据库中插入测试数据。

## 开发团队

本项目由图书馆管理系统开发团队开发和维护。

## 许可证

本项目采用MIT许可证，详情请参阅LICENSE文件。