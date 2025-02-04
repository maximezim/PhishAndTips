package com.learnandphish.formation.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class UserQuizScore {
    @EmbeddedId
    private UserQuizzId userQuizzId;

    private double score;
}
