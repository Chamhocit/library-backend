package com.example.aptech.spring.library.Auth;

import com.example.aptech.spring.library.Request.UserRequest;
import com.example.aptech.spring.library.config.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request, HttpServletResponse response){
        LoginResponse loginResponse = authenticationService.authenticate(request);
        //thêm cookie jwt http-only
        String jwtToken = loginResponse.getAccessToken();
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(7*24*60*60);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(true);
        jwtCookie.setDomain("localhost");
        //thêm cookie userMail
        Cookie userEmailCookie = new Cookie("userName", request.getEmail());
        userEmailCookie.setMaxAge(7*24*60*60);
        userEmailCookie.setPath("/");
        userEmailCookie.setSecure(true);
        userEmailCookie.setDomain("localhost");
        // add 2 cookie vào response
        response.addCookie(userEmailCookie);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> GetRole(@CookieValue(name = "jwt") String token, HttpServletRequest request){
        if(!jwtService.checkTokenExpired(token)){
            List<String> userRole = jwtService.extractUserRole(token);
            if(userRole.stream().anyMatch(x->x.equals("ROLE_ADMIN"))){
                return ResponseEntity.ok(true);
            }else {
                return ResponseEntity.ok(false);
            }

        }else {
            return ResponseEntity.badRequest().body("You need to login again to access the page.");
        }
    }

}
