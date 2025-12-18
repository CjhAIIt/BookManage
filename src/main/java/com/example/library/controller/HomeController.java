package com.example.library.controller;

import com.example.library.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<Map<String, Object>> home() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "图书管理系统 API");
        info.put("version", "1.0.0");
        info.put("description", "这是一个基于 Spring Boot 的图书管理系统后端 API");
        info.put("endpoints", new String[]{
            "/api/books - 图书相关接口",
            "/api/user - 用户相关接口",
            "/api/admin - 管理员相关接口",
            "/api/borrow - 借阅相关接口"
        });
        
        return ApiResponse.success("欢迎使用图书管理系统 API", info);
    }
}