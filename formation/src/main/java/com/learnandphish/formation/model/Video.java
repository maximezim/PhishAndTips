package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    private Integer id;

    private String title;

    private String description;

    private String videoUrl;

    private String captionUrl;

    private String thumbnailUrl;
}
