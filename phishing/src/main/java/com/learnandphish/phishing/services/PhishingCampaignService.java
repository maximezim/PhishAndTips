package com.learnandphish.phishing.services;

import com.learnandphish.phishing.PhishingCampaign;
import com.learnandphish.phishing.interfaces.PhishingCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhishingCampaignService {
    @Autowired
    private PhishingCampaignRepository campaignRepository;

    public PhishingCampaign createCampaign(PhishingCampaign campaign) {
        return campaignRepository.save(campaign);
    }

    public List<PhishingCampaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }
}
