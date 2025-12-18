package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.entity.Admin;
import com.example.library.entity.User;
import com.example.library.service.AdminService;
import com.example.library.service.UserService;
import com.example.library.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/admin", produces = "application/json;charset=UTF-8")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            
            String token = adminService.login(username, password);
            Admin admin = adminService.findByUsername(username);
            admin.setPassword(null);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("admin", admin);
            
            return ApiResponse.success("登录成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ApiResponse<Admin> register(@RequestBody Admin admin) {
        try {
            Admin registeredAdmin = adminService.addAdmin(admin);
            registeredAdmin.setPassword(null);
            return ApiResponse.success("注册成功", registeredAdmin);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Admin> getAdminById(@PathVariable Integer id) {
        try {
            Admin admin = adminService.findById(id);
            if (admin != null) {
                admin.setPassword(null);
                return ApiResponse.success(admin);
            } else {
                return ApiResponse.error("管理员不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    public ApiResponse<List<Admin>> getAllAdmins() {
        try {
            List<Admin> admins = adminService.findAll();
            admins.forEach(admin -> admin.setPassword(null));
            return ApiResponse.success(admins);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Admin> updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        try {
            admin.setId(id);
            Admin updatedAdmin = adminService.updateAdmin(admin);
            updatedAdmin.setPassword(null);
            return ApiResponse.success("更新成功", updatedAdmin);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public ApiResponse<Admin> getCurrentAdmin(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                Admin admin = adminService.findByUsername(username);
                if (admin != null) {
                    admin.setPassword(null);
                    return ApiResponse.success(admin);
                }
            }
            return ApiResponse.error(401, "未授权");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteAdmin(@PathVariable Integer id) {
        try {
            adminService.deleteAdmin(id);
            return ApiResponse.success("删除成功", "管理员已删除");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 用户管理端点
    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        try {
            List<User> users = userService.findAll();
            users.forEach(user -> user.setPassword(null));
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/users/{id}")
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
    
    @PutMapping("/users/{id}")
    public ApiResponse<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            updatedUser.setPassword(null);
            return ApiResponse.success("更新用户成功", updatedUser);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ApiResponse.success("删除成功", "用户已删除");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 临时调试端点，用于查看管理员账户信息
    @GetMapping("/debug/admin-info")
    public ApiResponse<Admin> getAdminDebugInfo() {
        try {
            Admin admin = adminService.findByUsername("admin");
            if (admin != null) {
                // 不隐藏密码，用于调试
                return ApiResponse.success(admin);
            } else {
                return ApiResponse.error("管理员账户不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 临时调试端点，用于测试密码验证
    @PostMapping("/debug/test-password")
    public ApiResponse<Map<String, Object>> testPassword(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            Admin admin = adminService.findByUsername(username);
            if (admin == null) {
                return ApiResponse.error("管理员账户不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("username", username);
            result.put("inputPassword", password);
            result.put("storedPassword", admin.getPassword());
            
            // 测试密码匹配
            boolean matches = passwordEncoder.matches(password, admin.getPassword());
            result.put("passwordMatches", matches);
            
            // 测试编码新密码
            String newEncodedPassword = passwordEncoder.encode(password);
            result.put("newEncodedPassword", newEncodedPassword);
            
            return ApiResponse.success("密码验证测试", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    // 简单的密码测试端点
    @GetMapping("/debug/simple-test")
    public ApiResponse<String> simpleTest() {
        try {
            Admin admin = adminService.findByUsername("admin");
            if (admin == null) {
                return ApiResponse.error("管理员账户不存在");
            }
            
            boolean matches = passwordEncoder.matches("admin123", admin.getPassword());
            return ApiResponse.success("密码匹配结果: " + matches);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}