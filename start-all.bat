@echo off
echo 正在启动图书管理系统...
echo.

echo 启动后端服务器...
start "后端服务器" cmd /k "mvn spring-boot:run"

echo 等待后端启动...
timeout /t 10 /nobreak > nul

echo 启动前端服务器...
cd frontend
start "前端服务器" cmd /k "node server.js"

echo.
echo 前后端服务器启动中...
echo 后端地址: http://localhost:8082
echo 前端地址: http://localhost:8081
echo.
echo 请在浏览器中访问 http://localhost:8081/ 使用系统
pause