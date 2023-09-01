package com.example.aptech.spring.library.controller;

import com.example.aptech.spring.library.Request.ReviewRequest;
import com.example.aptech.spring.library.Service.ReviewService;
import com.example.aptech.spring.library.config.JwtService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private JwtService jwtService;

    public ReviewController(ReviewService reviewService, JwtService jwtService) {
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    @GetMapping("/secure/user/book")
    public  Boolean reviewBookByUser(@CookieValue(name = "jwt") String token,
                                     @RequestParam Long bookId)throws Exception{

        String userEmail = jwtService.extractUsername(token);
        if(userEmail == null){
            throw new Exception("User email is missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
    }

    @PostMapping("/secure")
    public void postReview(@CookieValue(name = "jwt") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception{


        String userEmail = jwtService.extractUsername(token);
        if(userEmail == null){
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }

}
