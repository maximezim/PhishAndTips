package com.learnandphish.formation.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Data
@Getter
@Setter
@Check(constraints = "score >= 0.00 AND score <= 1.00")
public class UserQuizScore {
    @EmbeddedId
    private UserQuizId userQuizId;

    @Column(nullable = false)
    private Float score;
}
