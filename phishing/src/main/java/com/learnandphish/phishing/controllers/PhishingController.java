package com.learnandphish.phishing.controllers;

import com.learnandphish.phishing.PhishingCampaign;
import com.learnandphish.phishing.services.PhishingCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phishing")
public class PhishingController {
    @Autowired
    private PhishingCampaignService campaignService;

    @PostMapping("/campaigns")
    public ResponseEntity<PhishingCampaign> createCampaign(@RequestBody PhishingCampaign campaign) {
        PhishingCampaign savedCampaign = campaignService.createCampaign(campaign);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCampaign);
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<PhishingCampaign>> getAllCampaigns() {
        List<PhishingCampaign> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }
}