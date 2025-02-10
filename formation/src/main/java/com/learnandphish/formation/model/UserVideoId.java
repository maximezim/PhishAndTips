package com.learnandphish.formation.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserVideoId implements Serializable {
    private String userEmail;
    private Integer videoId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserVideoId that = (UserVideoId) o;
        return userEmail.equals(that.userEmail) && videoId.equals(that.videoId);
    }

    @Override
    public int hashCode() {
        return userEmail.hashCode() + videoId.hashCode();
    }
}
