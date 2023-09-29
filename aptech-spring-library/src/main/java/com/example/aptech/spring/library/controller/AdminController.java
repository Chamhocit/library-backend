package com.example.aptech.spring.library.controller;


import com.example.aptech.spring.library.Request.AddBookRequest;
import com.example.aptech.spring.library.Service.AdminService;
import com.example.aptech.spring.library.dao.CheckoutRepository;
import com.example.aptech.spring.library.dao.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")

public class AdminController {
   private AdminService adminService;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }




    @PostMapping("/secure/add/book")
    public void postBook(@RequestBody AddBookRequest addBookRequest){
        adminService.postBook(addBookRequest);
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestParam Long bookId)throws Exception{
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestParam Long bookId)throws Exception{
        adminService.decreaseBookQuantity(bookId);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestParam Long bookId) throws Exception{
        adminService.deleteBook(bookId);
    }


}
