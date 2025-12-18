package com.example.library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FrontendAutoStarter implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(FrontendAutoStarter.class);
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 检查是否已经运行了前端服务器
        try {
            // 简单检查端口8081是否被占用
            java.net.Socket socket = new java.net.Socket("localhost", 8081);
            socket.close();
            logger.info("前端服务器已在端口8081上运行");
            return;
        } catch (Exception e) {
            // 端口未被占用，继续启动前端服务器
        }
        
        logger.info("正在启动前端服务器...");
        
        try {
            // 获取前端目录路径
            String frontendPath = System.getProperty("user.dir") + File.separator + "frontend";
            File frontendDir = new File(frontendPath);
            
            if (!frontendDir.exists()) {
                logger.error("前端目录不存在: {}", frontendPath);
                return;
            }
            
            // 在新线程中启动前端服务器
            Thread frontendThread = new Thread(() -> {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("node", "server.js");
                    processBuilder.directory(frontendDir);
                    
                    // 启动进程
                    Process process = processBuilder.start();
                    
                    // 读取输出
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info("前端服务器: {}", line);
                    }
                    
                } catch (Exception e) {
                    logger.error("启动前端服务器失败", e);
                }
            });
            
            frontendThread.setDaemon(true);
            frontendThread.start();
            
            logger.info("前端服务器启动命令已执行，请访问 http://localhost:8081");
            
        } catch (Exception e) {
            logger.error("启动前端服务器时出错", e);
        }
    }
}