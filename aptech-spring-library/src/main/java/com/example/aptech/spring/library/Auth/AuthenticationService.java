package com.example.aptech.spring.library.Auth;

import com.example.aptech.spring.library.Request.UserRequest;
import com.example.aptech.spring.library.config.JwtService;
import com.example.aptech.spring.library.dao.RoleRepository;
import com.example.aptech.spring.library.dao.TokenRepository;
import com.example.aptech.spring.library.dao.UserRepository;
import com.example.aptech.spring.library.dao.UserRoleRepository;
import com.example.aptech.spring.library.entity.Role;
import com.example.aptech.spring.library.entity.Token;
import com.example.aptech.spring.library.entity.User;
import com.example.aptech.spring.library.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public RegisterResponse register(UserRequest request){
        var user = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        Set<Role> roles = request.getRoles();
        for(Role role: roles){
            UserRole userRole = new UserRole();
            userRole.setUser(savedUser);
            Role persistedRole = roleRepository.findById(role.getId()).get();
            userRole.setRole(persistedRole);
            userRoleRepository.save(userRole);
        }
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .phone(savedUser.getPhone())
                .email(savedUser.getEmail())
                .roles(roles).build();
    }

    public LoginResponse authenticate(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        //set role
//        List<Role> roles = roleRepository.findAllRoleByUser(user.getId());
//        roles.forEach(x->System.out.println(x.getName()));
//        Collection<SimpleGrantedAuthority> authorityCollections = new ArrayList<>();
//        Set<Role> roleSet = new HashSet<>();
//        roles.stream().forEach(c->roleSet.add(c));
//        roleSet.stream().forEach(i->authorityCollections.add(new SimpleGrantedAuthority(i.getName())));
//        user.setRole(roleSet);
        //lwu token


        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) user.getAuthorities();
        authorities.forEach(x->System.out.println(x));
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }




}
