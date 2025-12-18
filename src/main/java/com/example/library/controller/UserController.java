package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.example.library.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            registeredUser.setPassword(null);
            return ApiResponse.success("注册成功", registeredUser);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            String token = userService.login(username, password);
            User user = userService.findByUsername(username);
            user.setPassword(null);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", user);
            
            return ApiResponse.success("登录成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                user.setPassword(null);
                return ApiResponse.success(user);
            } else {
                return ApiResponse.error("用户不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Integer id, @RequestBody User user, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                User currentUser = userService.findByUsername(username);
                
                if (!currentUser.getId().equals(id) && !"admin".equals(currentUser.getRole())) {
                    return ApiResponse.error(403, "无权修改其他用户信息");
                }
            }
            
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            updatedUser.setPassword(null);
            return ApiResponse.success("更新成功", updatedUser);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public ApiResponse<User> getCurrentUser(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                User user = userService.findByUsername(username);
                if (user != null) {
                    user.setPassword(null);
                    return ApiResponse.success(user);
                }
            }
            return ApiResponse.error(401, "未授权");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}