package com.example.aptech.spring.library.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

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

    public Map<String, String> extractUserRole(String token){
        Claims claims = extractAllClaims(token);
        return (Map<String, String>)  claims.get("userRole");
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private String buildToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration){
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(geSignInKey(),  SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return buildToken(extraClaims,userDetails, jwtExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
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
                    .setSigningKey(secretKey)
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

    public boolean checkCookie(HttpServletRequest request){
        boolean f = true;
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            f = false;
        }else {
            if(!Arrays.stream(cookies).map(Cookie::getName).anyMatch(x -> x.equals("jwt"))){
                f = false;
            }else {
                String jwt = getJwt(request, "jwt");
                if(checkTokenExpired(jwt)){
                    f = false;
                }
            }
        }
        return f;
    }

}
