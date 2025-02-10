package com.learnandphish.formation.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class UserVideoWatched {
    @EmbeddedId
    private UserVideoId userVideoId;

    @Column(nullable = false)
    private Boolean isWatched;
}
