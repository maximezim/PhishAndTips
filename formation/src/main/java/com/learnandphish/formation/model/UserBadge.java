package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_badge")
@IdClass(UserBadgeId.class)
public class UserBadge {
    @Id
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Id
    @Column(name = "badge_id", nullable = false)
    private Long badgeId;
}

