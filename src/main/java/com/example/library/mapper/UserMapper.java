package com.example.library.mapper;

import com.example.library.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    
    User findByUsername(@Param("username") String username);
    
    User findById(@Param("id") Integer id);
    
    List<User> findAll();
    
    int updateUser(User user);
    
    int deleteUser(@Param("id") Integer id);
}