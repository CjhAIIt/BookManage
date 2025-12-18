package com.example.library.service;

import com.example.library.dto.BorrowRecordDTO;
import com.example.library.entity.BorrowRecord;
import com.example.library.mapper.BorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {
    
    @Autowired
    private BorrowMapper borrowMapper;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;
    
    public BorrowRecord borrowBook(Integer userId, Integer bookId) {
        // 检查用户是否存在
        if (userService.findById(userId) == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查图书是否存在
        if (bookService.findById(bookId) == null) {
            throw new RuntimeException("图书不存在");
        }
        
        // 检查图书是否有可借数量
        if (bookService.findById(bookId).getStock() <= 0) {
            throw new RuntimeException("图书库存不足");
        }
        
        // 检查用户是否已经借阅了这本书且未归还
        List<BorrowRecord> existingRecords = borrowMapper.findByUserIdAndBookId(userId, bookId);
        for (BorrowRecord record : existingRecords) {
            if ("BORROWED".equals(record.getStatus())) {
                throw new RuntimeException("您已借阅此书且尚未归还");
            }
        }
        
        // 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(30)); // 借阅期限30天
        record.setStatus("BORROWED");
        
        borrowMapper.insertRecord(record);
        
        // 更新图书库存
        bookService.updateStock(bookId, bookService.findById(bookId).getStock() - 1);
        
        return record;
    }
    
    public void returnBook(Integer recordId) {
        BorrowRecord record = borrowMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        
        if ("RETURNED".equals(record.getStatus())) {
            throw new RuntimeException("此书已归还");
        }
        
        // 更新借阅记录
        record.setReturnDate(LocalDate.now());
        record.setStatus("RETURNED");
        borrowMapper.updateRecord(record);
        
        // 更新图书库存
        bookService.updateStock(record.getBookId(), bookService.findById(record.getBookId()).getStock() + 1);
    }
    
    public List<BorrowRecord> findByUserId(Integer userId) {
        return borrowMapper.findByUserId(userId);
    }
    
    // 获取用户的借阅记录，包含图书和用户信息
    public List<BorrowRecordDTO> findUserBorrowRecordsWithDetails(Integer userId) {
        return borrowMapper.findUserBorrowRecordsWithDetails(userId);
    }
    
    public BorrowRecord findRecordById(Integer recordId) {
        return borrowMapper.findById(recordId);
    }
    
    public List<BorrowRecord> findAllRecords() {
        return borrowMapper.findAll();
    }
    
    // 获取所有借阅记录，包含图书和用户信息
    public List<BorrowRecordDTO> findAllBorrowRecordsWithDetails() {
        return borrowMapper.findAllBorrowRecordsWithDetails();
    }
}