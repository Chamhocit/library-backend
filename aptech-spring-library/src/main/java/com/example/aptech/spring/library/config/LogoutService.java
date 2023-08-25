package com.example.aptech.spring.library.config;

import com.example.aptech.spring.library.dao.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        final String authHeader = request.getHeader("Authorization");
//        final  String jwt;
//        if(authHeader==null || !authHeader.startsWith("Bearer ")){
//            return;
//        }
//        jwt = authHeader.substring(7);
        final Cookie[] cookies = request.getCookies();
        String jwt = null;
        if(Arrays.stream(cookies).map(Cookie::getName).anyMatch(x->x.equals("jwt"))){
            Cookie jwtToken = Arrays.stream(cookies).filter(x->x.getName().equals("jwt")).collect(Collectors.toList()).get(0);
            jwt= jwtToken.getValue();
            System.out.println(jwt);
        }else {
            try {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Logout failed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        var storedToken = tokenRepository.findByToken(jwt).orElseThrow(null);
        if(storedToken!=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
