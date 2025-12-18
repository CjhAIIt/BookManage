@echo off
echo ========================================
echo    图书管理系统 - 编译和启动
echo ========================================
echo.

echo 正在编译Java源文件...

REM 创建输出目录
if not exist target\classes mkdir target\classes

REM 使用Maven编译项目
call mvn clean compile

if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo 编译成功！
echo.

echo 启动后端和前端服务器...
echo 后端地址: http://localhost:8082
echo 前端地址: http://localhost:8081
echo.

REM 启动后端服务器（最小化窗口）
start "后端服务器" /min cmd /c "mvn spring-boot:run && pause"

REM 等待后端启动
echo 等待后端启动...
timeout /t 8 /nobreak > nul

REM 启动前端服务器（最小化窗口）
cd frontend
start "前端服务器" /min cmd /c "node server.js && pause"

echo.
echo 正在打开浏览器...
start http://localhost:8081

echo.
echo ========================================
echo 系统启动完成！
echo 前端界面: http://localhost:8081
echo 后端API: http://localhost:8082
echo ========================================
echo.

pause