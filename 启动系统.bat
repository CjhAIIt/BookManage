@echo off
title 图书管理系统启动器

echo ========================================
echo    图书管理系统 - 一键启动
echo ========================================
echo.

echo 正在检查Node.js环境...
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js，请先安装Node.js
    pause
    exit /b 1
)

echo 正在检查Maven环境...
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven
    pause
    exit /b 1
)

echo.
echo 正在启动图书管理系统...
echo 后端服务器: http://localhost:8082
echo 前端界面: http://localhost:8081
echo.

echo 启动后端服务器...
start "后端服务器" /min cmd /c "mvn spring-boot:run && pause"

echo 等待后端启动...
timeout /t 8 /nobreak > nul

echo 启动前端服务器...
cd frontend
start "前端服务器" /min cmd /c "node server.js && pause"

echo.
echo ========================================
echo 系统启动中，请稍候...
echo 前端地址: http://localhost:8081
echo 后端API: http://localhost:8082
echo ========================================
echo.

timeout /t 3 /nobreak > nul

echo 正在打开浏览器...
start http://localhost:8081

echo.
echo 启动完成！如果浏览器未自动打开，请手动访问 http://localhost:8081
pause