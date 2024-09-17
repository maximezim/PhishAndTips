package com.learnandphish.phishing;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phishing_email")
public class PhishingEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "clicked_at")
    private LocalDateTime clickedAt;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private PhishingCampaign campaign;

    public void setOpenedAt(LocalDateTime now) {
        this.openedAt = now;
    }
}
