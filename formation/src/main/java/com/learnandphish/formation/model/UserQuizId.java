package com.learnandphish.formation.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserQuizId implements Serializable {
    private String userEmail;
    private Integer quizId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserQuizId that = (UserQuizId) o;
        return Objects.equals(userEmail, that.userEmail) && Objects.equals(quizId, that.quizId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, quizId);
    }
}
