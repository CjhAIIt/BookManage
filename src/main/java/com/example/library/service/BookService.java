package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    
    @Autowired
    private BookMapper bookMapper;
    
    public Book addBook(Book book) {
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new RuntimeException("ISBN不能为空");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new RuntimeException("书名不能为空");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new RuntimeException("作者不能为空");
        }
        if (book.getStock() == null || book.getStock() < 0) {
            throw new RuntimeException("库存不能为负数");
        }
        
        bookMapper.insertBook(book);
        return book;
    }
    
    public Book findById(Integer id) {
        Book book = bookMapper.findById(id);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        return book;
    }
    
    public List<Book> findAll() {
        return bookMapper.findAll();
    }
    
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return findAll();
        }
        
        List<Book> booksByTitle = bookMapper.findByTitle(keyword);
        List<Book> booksByAuthor = bookMapper.findByAuthor(keyword);
        
        booksByTitle.removeAll(booksByAuthor);
        booksByTitle.addAll(booksByAuthor);
        
        return booksByTitle;
    }
    
    public List<Book> findByGenre(String genre) {
        return bookMapper.findByGenre(genre);
    }
    
    public Book updateBook(Book book) {
        Book existingBook = bookMapper.findById(book.getId());
        if (existingBook == null) {
            throw new RuntimeException("图书不存在");
        }
        
        if (book.getStock() != null && book.getStock() < 0) {
            throw new RuntimeException("库存不能为负数");
        }
        
        bookMapper.updateBook(book);
        return bookMapper.findById(book.getId());
    }
    
    public void deleteBook(Integer id) {
        Book book = bookMapper.findById(id);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        bookMapper.deleteBook(id);
    }
    
    public void updateStock(Integer id, Integer stock) {
        if (stock < 0) {
            throw new RuntimeException("库存不能为负数");
        }
        bookMapper.updateStock(id, stock);
    }
}