package com.example.aptech.spring.library.Auth;

import com.example.aptech.spring.library.Request.UserRequest;
import com.example.aptech.spring.library.config.LogoutService;
import com.example.aptech.spring.library.dao.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final LogoutService logoutService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response){
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutService.logout(request, response, authentication);
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setDomain("localhost");
        response.addCookie(jwtCookie);
        return ResponseEntity.ok("User Logout Successfully.");
    }
}
