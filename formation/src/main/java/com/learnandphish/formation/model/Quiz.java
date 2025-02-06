package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    private Integer id;

    // Json of the SurveyJs quiz
    private String json;
}
