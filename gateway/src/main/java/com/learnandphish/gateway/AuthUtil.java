package com.learnandphish.gateway;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for authentication-related operations.
 *
 * @author Maxime Zimmermann
 */
@Component
public class AuthUtil {

    private final JWTUtil jwtUtil;

    public AuthUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Generates a JWT token with username and role.
     *
     * @param email the email to include in the token
     * @param role  the role to include in the token
     * @return the generated JWT token
     */
    public String getToken(String email, String role) {
        // Combine email and role as the subject of the JWT
        String subject = email + ":" + role;
        return createToken(subject);
    }

    /**
     * Generates a new JWT for the provided username.
     * The token will expire in 10 hours.
     *
     * @param username the name to include in the JWT's subject
     * @return generated JWT token string
     */
    public String createToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("sub", username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(10, ChronoUnit.HOURS)))
                .signWith(jwtUtil.getSecret())
                .compact();
    }
}
