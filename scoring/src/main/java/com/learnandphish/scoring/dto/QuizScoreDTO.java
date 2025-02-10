package com.learnandphish.scoring.dto;

import lombok.Data;

@Data
public class QuizScoreDTO {
    private Integer quizId;
    private String userEmail;
    private Double score;
}
