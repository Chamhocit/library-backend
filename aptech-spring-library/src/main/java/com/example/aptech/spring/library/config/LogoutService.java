package com.example.aptech.spring.library.config;

import com.example.aptech.spring.library.dao.TokenRepository;
import com.example.aptech.spring.library.response.SetMessageResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SetMessageResponse setMessageResponse = new SetMessageResponse();
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        if(cookies==null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                setMessageResponse.SetMessage(response, "Logout failed: JWT cookie not found");
                return;
        }else {
            if (Arrays.stream(cookies).map(Cookie::getName).anyMatch(x -> x.equals("jwt"))) {
                Cookie jwtToken = Arrays.stream(cookies).filter(x->x.getName().equals("jwt")).collect(Collectors.toList()).get(0);
                jwt= jwtToken.getValue();
                var storedToken = tokenRepository.findByToken(jwt).orElseThrow(null);
                if(storedToken!=null) {
                    storedToken.setExpired(true);
                    storedToken.setRevoked(true);
                    tokenRepository.save(storedToken);
                    SecurityContextHolder.clearContext();
                    Cookie jwtCookie = new Cookie("jwt", null);
                    Cookie userEmail = new Cookie("userName", null);
                    userEmail.setMaxAge(0);
                    userEmail.setPath("/");
                    userEmail.setDomain("localhost");
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(0);
                    jwtCookie.setDomain("localhost");
                    response.addCookie(userEmail);
                    response.addCookie(jwtCookie);
                    response.setStatus(HttpServletResponse.SC_OK);
                    setMessageResponse.SetMessage(response, "User Logout Success.");
                }else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    setMessageResponse.SetMessage(response, "Logout failed: JWT cookie not found");
                    return;
                }
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                setMessageResponse.SetMessage(response, "Logout failed: JWT cookie not found");
                return;
            }
        }
    }
}
