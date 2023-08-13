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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
//        LoginResponse loginResponse = authenticationService.authenticate(request);
//        if(loginResponse != null){
//            String jwtToken = loginResponse.getAccessToken();
//            Cookie jwtCookie = new Cookie("jwt", jwtToken);
//            jwtCookie.setHttpOnly(true);
//            jwtCookie.setMaxAge((int) (jwtExpiration/1000));
//            jwtCookie.setPath("/");
//            response.addCookie(jwtCookie);
            return ResponseEntity.ok(authenticationService.authenticate(request));
//        }else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect account or password");
//        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("User Logout Successfully.");
    }
}
