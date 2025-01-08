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

    private String title;

    private String description;

    private String url;

    private Integer duration;

    private Integer difficulty;

    public Video(Long id, String title, String description, String url, Integer duration, Integer difficulty) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.duration = duration;
        this.difficulty = difficulty;
    }
}
