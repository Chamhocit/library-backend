package com.example.aptech.spring.library.controller;


import com.example.aptech.spring.library.Service.BookService;
import com.example.aptech.spring.library.config.JwtService;
import com.example.aptech.spring.library.entity.Book;
import com.example.aptech.spring.library.response.ShelfCurrentLoansResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



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

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@CookieValue(name = "jwt") String token) throws Exception{
        String userEmail = jwtService.extractUsername(token);
        return bookService.currentLoans(userEmail);
    }



    @GetMapping("/secure/ischeckout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId, @CookieValue(name = "jwt") String token){

        String userEmail = jwtService.extractUsername(token);
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkout(@RequestParam Long bookId, @CookieValue(name = "jwt") String token) throws Exception{
        String userEmail = jwtService.extractUsername(token);
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@CookieValue(name = "jwt") String token) {
        String userEmail = jwtService.extractUsername(token);
        return bookService.currentLoansCount(userEmail);
    }

    @PutMapping("/secure/return")
    public void returnBook(@CookieValue(name = "jwt") String token, @RequestParam Long bookId) throws Exception{
        String userEmail = jwtService.extractUsername(token);
        bookService.returnBook(userEmail, bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@CookieValue(name = "jwt") String token, @RequestParam Long bookId) throws Exception{
        String userEmail = jwtService.extractUsername(token);
        bookService.renewLoan(userEmail, bookId);
    }

}
