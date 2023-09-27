package com.example.aptech.spring.library.controller;


import com.example.aptech.spring.library.Request.AddBookRequest;
import com.example.aptech.spring.library.Service.AdminService;
import com.example.aptech.spring.library.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")

public class AdminController {
   private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/secure/add/book")
    public void postBook(@RequestBody AddBookRequest addBookRequest){
        adminService.postBook(addBookRequest);
    }
}
