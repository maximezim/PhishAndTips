package com.learnandphish.phishing.controllers;

import com.learnandphish.phishing.services.PhishingEmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/phishing")
public class TrackingController {
    @Autowired
    private PhishingEmailService emailService;

    @GetMapping("/track/{emailId}")
    public void trackEmail(@PathVariable Long emailId, HttpServletResponse response) throws IOException {
        emailService.recordEmailOpened(emailId);

        response.setContentType("image/png");
        // TODO: write a 1x1 pixel transparent image to the response
        byte[] pixel = new byte[]{/* PNG data for 1x1 pixel image */};
        response.getOutputStream().write(pixel);
        response.getOutputStream().flush();
    }
}
