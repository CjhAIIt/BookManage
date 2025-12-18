package com.example.library.service;

import com.example.library.entity.Admin;
import com.example.library.mapper.AdminMapper;
import com.example.library.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    public String login(String username, String password) {
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("管理员用户名或密码错误");
        }
        
        return jwtTokenUtil.generateToken(username, "admin", admin.getId());
    }
    
    public Admin addAdmin(Admin admin) {
        if (adminMapper.findByUsername(admin.getUsername()) != null) {
            throw new RuntimeException("管理员用户名已存在");
        }
        
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminMapper.insertAdmin(admin);
        return admin;
    }
    
    public Admin findById(Integer id) {
        return adminMapper.findById(id);
    }
    
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }
    
    public Admin updateAdmin(Admin admin) {
        adminMapper.updateAdmin(admin);
        return adminMapper.findById(admin.getId());
    }
    
    public void deleteAdmin(Integer id) {
        adminMapper.deleteAdmin(id);
    }
    
    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }
}