package com.example.aptech.spring.library.controller;

import com.example.aptech.spring.library.Request.AdminResponseQuestion;
import com.example.aptech.spring.library.Service.MessagesService;
import com.example.aptech.spring.library.config.JwtService;
import com.example.aptech.spring.library.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    private MessagesService messagesService;
    private JwtService jwtService;
    @Autowired
    public MessagesController(MessagesService messagesService, JwtService jwtService) {
        this.messagesService = messagesService;
        this.jwtService = jwtService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@CookieValue(name = "jwt") String token
            ,@RequestBody Message messageRequest){
        String userEmail = jwtService.extractUsername(token);
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@CookieValue(name = "jwt") String token, @RequestBody AdminResponseQuestion adminResponseQuestion)throws Exception{
        String userEmail = jwtService.extractUsername(token);
        messagesService.putMessage(adminResponseQuestion, userEmail);
    }


}
