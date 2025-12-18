package com.example.library.mapper;

import com.example.library.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    int insertBook(Book book);
    
    Book findById(@Param("id") Integer id);
    
    List<Book> findAll();
    
    List<Book> findByTitle(@Param("title") String title);
    
    List<Book> findByAuthor(@Param("author") String author);
    
    List<Book> findByGenre(@Param("genre") String genre);
    
    int updateBook(Book book);
    
    int deleteBook(@Param("id") Integer id);
    
    int updateStock(@Param("id") Integer id, @Param("stock") Integer stock);
}