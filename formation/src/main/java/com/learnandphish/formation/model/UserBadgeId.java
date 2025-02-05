package com.learnandphish.formation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBadgeId implements Serializable {
    private String userEmail;
    private Long badgeId;
}
