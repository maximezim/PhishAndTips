package com.learnandphish.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.Base64;

@RestController
public class PublicKeyController {

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/public-key")
    public String getPublicKey() {
        PublicKey publicKey = jwtUtil.getPublicKey();
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return encodedKey;
    }
}