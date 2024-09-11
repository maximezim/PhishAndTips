package com.learnandphish.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey SECRET;

    public JWTUtil() {
        this.SECRET = generateRandomSecretKey();
    }

    private SecretKey generateRandomSecretKey() {
        byte[] keyBytes = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token ) {
        return this.getALlClaims(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(10, ChronoUnit.HOURS)))
                .signWith(SECRET)
                .compact();
    }

}