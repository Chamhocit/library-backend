package com.example.aptech.spring.library.Auth;

import com.example.aptech.spring.library.Request.UserRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request, HttpServletResponse response){
        LoginResponse loginResponse = authenticationService.authenticate(request);
        String jwtToken = loginResponse.getAccessToken();
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(7*24*60*60);
        jwtCookie.setPath("/");
        jwtCookie.setDomain("localhost");
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }



}
