package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.mapper.UserMapper;
import com.example.library.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    public User register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userMapper.insertUser(user);
        return user;
    }
    
    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        return jwtTokenUtil.generateToken(username, user.getRole(), user.getId());
    }
    
    public User findById(Integer id) {
        return userMapper.findById(id);
    }
    
    public List<User> findAll() {
        return userMapper.findAll();
    }
    
    public User updateUser(User user) {
        userMapper.updateUser(user);
        return userMapper.findById(user.getId());
    }
    
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }
    
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}