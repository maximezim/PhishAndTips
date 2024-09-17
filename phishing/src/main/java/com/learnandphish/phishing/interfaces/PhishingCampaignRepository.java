package com.learnandphish.phishing.interfaces;

import com.learnandphish.phishing.PhishingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhishingCampaignRepository extends JpaRepository<PhishingCampaign, Long> {
}
