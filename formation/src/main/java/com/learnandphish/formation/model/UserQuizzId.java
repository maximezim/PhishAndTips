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
public class UserQuizzId implements Serializable {
    private String user_email;
    private Integer quiz_id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserQuizzId that = (UserQuizzId) o;
        return Objects.equals(user_email, that.user_email) && Objects.equals(quiz_id, that.quiz_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_email, quiz_id);
    }
}
