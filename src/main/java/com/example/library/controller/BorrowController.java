package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.dto.BorrowRecordDTO;
import com.example.library.entity.BorrowRecord;
import com.example.library.service.BorrowService;
import com.example.library.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/borrow", produces = "application/json;charset=UTF-8")
public class BorrowController {
    
    @Autowired
    private BorrowService borrowService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @PostMapping("/{bookId}")
    public ApiResponse<BorrowRecord> borrowBook(@PathVariable Integer bookId, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Integer userId = jwtTokenUtil.getUserIdFromToken(token);
                BorrowRecord record = borrowService.borrowBook(userId, bookId);
                return ApiResponse.success("借书成功", record);
            } else {
                return ApiResponse.error(401, "未提供有效的认证令牌");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/return/{recordId}")
    public ApiResponse<String> returnBook(@PathVariable Integer recordId, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Integer userId = jwtTokenUtil.getUserIdFromToken(token);
                
                // 验证借阅记录是否属于当前用户
                BorrowRecord record = borrowService.findRecordById(recordId);
                if (!record.getUserId().equals(userId)) {
                    return ApiResponse.error(403, "无权归还他人的图书");
                }
                
                borrowService.returnBook(recordId);
                return ApiResponse.success("还书成功", "图书已归还");
            } else {
                return ApiResponse.error(401, "未提供有效的认证令牌");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/user")
    public ApiResponse<List<BorrowRecordDTO>> getCurrentUserBorrowRecords(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Integer userId = jwtTokenUtil.getUserIdFromToken(token);
                return ApiResponse.success(borrowService.findUserBorrowRecordsWithDetails(userId));
            } else {
                return ApiResponse.error(401, "未提供有效的认证令牌");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ApiResponse<List<BorrowRecord>> getBorrowRecordsByUser(@PathVariable Integer userId) {
        try {
            return ApiResponse.success(borrowService.findByUserId(userId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/record/{recordId}")
    public ApiResponse<BorrowRecord> getBorrowRecordById(@PathVariable Integer recordId) {
        try {
            return ApiResponse.success(borrowService.findRecordById(recordId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/all")
    public ApiResponse<List<BorrowRecordDTO>> getAllBorrowRecords() {
        try {
            return ApiResponse.success(borrowService.findAllBorrowRecordsWithDetails());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}