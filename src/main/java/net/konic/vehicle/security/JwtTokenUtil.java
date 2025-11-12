package net.konic.vehicle.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expiration; // in milliseconds

    // Generate JWT token with username only
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // username as subject
                .setIssuedAt(new Date()) // token issue time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // token expiration
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // sign the token
                .compact();
    }

    // Extract username from token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid or expired token
        }
    }
}
