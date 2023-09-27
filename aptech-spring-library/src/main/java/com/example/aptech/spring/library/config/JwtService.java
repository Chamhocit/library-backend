package com.example.aptech.spring.library.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;



    private Key geSignInKey()  {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(geSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public List<String> extractUserRole(String token){
        Claims claims = extractAllClaims(token);
        return (List<String>)  claims.get("roles");
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public String generateToken(UserDetails userDetails){
//        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        Set<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return Jwts.builder()
                .claim("roles", roles)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+7*24*60*60*1000))
                .signWith(geSignInKey())
                .compact();
    }


    public String generateRefreshToken(UserDetails userDetails){
//        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return Jwts.builder()
                .claim("roles", roles)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(7+2)*24*60*60*1000))
                .signWith(geSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean checkTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(geSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            return true;
        }
    }

    public String getJwt(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            return Arrays.stream(cookies)
                    .filter(x->x.getName().equals(cookieName))
                    .map(Cookie::getValue)
                    .findFirst().orElse(null);
        }
        return null;
    }



}
