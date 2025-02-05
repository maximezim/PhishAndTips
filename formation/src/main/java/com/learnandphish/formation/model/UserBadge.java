package com.learnandphish.formation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@IdClass(UserBadge.UserBadgeId.class)
public class UserBadge implements Serializable {
    @Id
    private String user_email;

    @Id
    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @Data
    public static class UserBadgeId implements Serializable {
        private String user_email;
        private Long badge_id;
    }
}