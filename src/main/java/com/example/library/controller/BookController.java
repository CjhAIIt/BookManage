package com.example.library.controller;

import com.example.library.common.ApiResponse;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/books", produces = "application/json;charset=UTF-8")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ApiResponse<List<Book>> getAllBooks() {
        try {
            return ApiResponse.success(bookService.findAll());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Book> getBookById(@PathVariable Integer id) {
        try {
            return ApiResponse.success(bookService.findById(id));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ApiResponse<List<Book>> searchBooks(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) String genre) {
        try {
            if (genre != null && !genre.isEmpty()) {
                return ApiResponse.success(bookService.findByGenre(genre));
            } else {
                return ApiResponse.success(bookService.searchBooks(keyword));
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PostMapping
    public ApiResponse<Book> addBook(@RequestBody Book book) {
        try {
            return ApiResponse.success("添加图书成功", bookService.addBook(book));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Book> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        try {
            book.setId(id);
            return ApiResponse.success("更新图书成功", bookService.updateBook(book));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBook(@PathVariable Integer id) {
        try {
            bookService.deleteBook(id);
            return ApiResponse.success("删除图书成功", "图书已删除");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}/stock")
    public ApiResponse<String> updateBookStock(@PathVariable Integer id, @RequestBody Map<String, Integer> stockMap) {
        try {
            Integer stock = stockMap.get("stock");
            bookService.updateStock(id, stock);
            return ApiResponse.success("更新库存成功", "库存已更新");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}