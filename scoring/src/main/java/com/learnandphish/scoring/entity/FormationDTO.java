package com.learnandphish.scoring.entity;

import lombok.Data;

@Data
public class FormationDTO {
    private Integer quiz_id;
    private String user_email;
    private Double score;
}
