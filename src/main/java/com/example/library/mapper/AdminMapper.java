package com.example.library.mapper;

import com.example.library.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    int insertAdmin(Admin admin);
    
    Admin findByUsername(@Param("username") String username);
    
    Admin findById(@Param("id") Integer id);
    
    List<Admin> findAll();
    
    int updateAdmin(Admin admin);
    
    int deleteAdmin(@Param("id") Integer id);
}