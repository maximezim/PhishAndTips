package com.learnandphish.formation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formation {

    @Id
    private Integer id;

    private String name;

    private String description;

}
