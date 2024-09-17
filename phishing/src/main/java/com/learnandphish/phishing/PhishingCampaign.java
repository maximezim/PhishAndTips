package com.learnandphish.phishing;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phishing_campaign")
public class PhishingCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_name", nullable = false)
    private String campaignName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

