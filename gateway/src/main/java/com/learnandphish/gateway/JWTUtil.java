package com.learnandphish.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for JWT token operations
 *
 * @author Maxime Zimmermann
 */
@Component
public class JWTUtil {

    private PublicKey publicKey;
    private final RestTemplate restTemplate;

    /**
     * Constructor. Initializes the RestTemplate.
     */
    public JWTUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the public key from the authentication service.
     */
    public void fetchPublicKey() {
        if (publicKey == null) {
            try {
                String publicKeyString = restTemplate.getForObject("http://authentication-service:8082/public-key", String.class);
                byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                this.publicKey = keyFactory.generatePublic(keySpec);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch public key", e);
            }
        }
    }

    /**
     * Parses the JWT token and returns all claims.
     *
     * @param token JWT token string
     * @return Claims object containing all JWT claims
     */
    public Claims getAllClaims(String token) {
        fetchPublicKey();
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()  // Build the parser before parsing the token
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token JWT token string
     * @return username extracted from the token
     */
    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    /**
     * Checks if the JWT is expired.
     *
     * @param token JWT token string
     * @return true if the JWT is expired, false if not
     */
    public boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Checks if the JWT is invalid. Currently, this only checks for expiration.
     *
     * @param token JWT token string
     * @return true if the JWT is invalid, false if not
     */
    public boolean isInvalid(String token) {
        return isTokenExpired(token);
    }
}