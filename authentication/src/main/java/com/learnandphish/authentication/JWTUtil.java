package com.learnandphish.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for JWT token operations
 *
 * @author Maxime Zimmermann
 */
@Component
public class JWTUtil {

    /**
     * Secret key used for token generation.
     */
    private final SecretKey SECRET;

    /**
     * Constructor. Generates a random secret key.
     */
    public JWTUtil() {
        this.SECRET = generateRandomSecretKey();
    }

    /**
     * Generates a random secret key.
     *
     * @return generated secret key
     */
    private SecretKey generateRandomSecretKey() {
        byte[] keyBytes = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parses the JWT token and returns all claims.
     *
     * @param token JWT token string
     * @return Claims object containing all JWT claims
     */
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if the JWT is expired.
     *
     * @param token JWT token string
     * @return true if the JWT is expired, false if not
     */
    private boolean isTokenExpired(String token) {
        return this.getAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Checks if the JWT is invalid. Currently, this only checks for expiration.
     *
     * @param token JWT token string
     * @return true if the JWT is invalid, false if not
     */
    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    /**
     * Get the secret key.
     *
     * @return the secret key
     */
    public SecretKey getSecret() {
        return SECRET;
    }

}