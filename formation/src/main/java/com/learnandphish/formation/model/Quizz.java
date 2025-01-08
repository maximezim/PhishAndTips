package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quizz {
    @Id
    private Integer id;

    private String json;
}
