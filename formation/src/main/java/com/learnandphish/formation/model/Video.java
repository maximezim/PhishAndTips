package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formationId;

    private String title;

    private String description;

    private String url;

    private Integer duration;

    private String quizId;

    public Video(Long id, String formationId, String title, String description, String url, Integer duration, String quizId) {
        this.id = id;
        this.formationId = formationId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.duration = duration;
        this.quizId = quizId;
    }
}
