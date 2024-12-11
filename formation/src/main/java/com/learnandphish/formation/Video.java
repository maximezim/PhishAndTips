package com.learnandphish.formation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String formationId;

    private String title;

    private String description;

    private String url;

    private Integer duration;

    private String quizId;
}
