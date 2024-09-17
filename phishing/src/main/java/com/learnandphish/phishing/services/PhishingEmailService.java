package com.learnandphish.phishing.services;

import com.learnandphish.phishing.PhishingEmail;
import com.learnandphish.phishing.interfaces.PhishingEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhishingEmailService {
    @Autowired
    private PhishingEmailRepository emailRepository;

    public PhishingEmail createPhishingEmail(PhishingEmail email) {
        return emailRepository.save(email);
    }

    public void recordEmailOpened(Long emailId) {
        Optional<PhishingEmail> emailOpt = emailRepository.findById(emailId);
        if (emailOpt.isPresent()) {
            PhishingEmail email = emailOpt.get();
            email.setOpenedAt(LocalDateTime.now());
            emailRepository.save(email);
        }
    }
}