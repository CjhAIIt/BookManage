package com.example.library.mapper;

import com.example.library.dto.BorrowRecordDTO;
import com.example.library.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowMapper {
    
    int insertRecord(BorrowRecord record);
    
    BorrowRecord findById(Integer id);
    
    List<BorrowRecord> findByUserId(Integer userId);
    
    List<BorrowRecord> findByUserIdAndBookId(Integer userId, Integer bookId);
    
    List<BorrowRecord> findAll();
    
    // 获取包含图书和用户信息的借阅记录
    List<BorrowRecordDTO> findUserBorrowRecordsWithDetails(Integer userId);
    
    List<BorrowRecordDTO> findAllBorrowRecordsWithDetails();
    
    int updateRecord(BorrowRecord record);
}