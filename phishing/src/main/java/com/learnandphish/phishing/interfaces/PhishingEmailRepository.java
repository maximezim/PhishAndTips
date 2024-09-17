package com.learnandphish.phishing.interfaces;

import com.learnandphish.phishing.PhishingEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhishingEmailRepository extends JpaRepository<PhishingEmail, Long> {
}
