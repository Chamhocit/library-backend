package com.example.aptech.spring.library.controller;

import com.example.aptech.spring.library.Service.BookService;
import com.example.aptech.spring.library.config.JwtService;
import com.example.aptech.spring.library.entity.Book;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    @GetMapping("/authenticate")
    public ResponseEntity<?> getJwtAuthenticate( HttpServletRequest request){
      Cookie[] cookies = request.getCookies();
      Cookie jwt = Arrays.stream(cookies).filter(x->x.getName().equals("jwt")).findFirst().orElse(null);
//      Arrays.stream(cookies).forEach(x->System.out.println(x.getName()));
        String jwtToken = jwt.getValue();
        if(jwt==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("JWT not found in cookie");
        }else {
            return ResponseEntity.ok().body(jwtToken);
        }
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
