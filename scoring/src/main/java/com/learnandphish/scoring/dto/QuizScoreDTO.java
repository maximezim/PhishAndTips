package com.learnandphish.scoring.dto;

import lombok.Data;

@Data
public class QuizScoreDTO {
    private Integer quiz_id;
    private String user_email;
    private Double score;
}
