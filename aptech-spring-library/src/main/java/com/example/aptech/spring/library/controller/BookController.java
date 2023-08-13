package com.example.aptech.spring.library.controller;

import com.example.aptech.spring.library.Service.BookService;
import com.example.aptech.spring.library.config.JwtService;
import com.example.aptech.spring.library.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;
    private JwtService jwtService;
    @Autowired
    public BookController(BookService bookService, JwtService jwtService) {
        this.bookService = bookService;
        this.jwtService = jwtService;
    }

    @GetMapping("/secure/ischeckout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId, @RequestHeader(value = "Authorization") String token){
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkout(@RequestParam Long bookId, @RequestHeader(value = "Authorization") String token) throws Exception{
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String jwt = token.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        return bookService.currentLoansCount(userEmail);
    }
}
