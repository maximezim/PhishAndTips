package com.learnandphish.formation.dto;

import lombok.Data;

@Data
public class UserQuizScoreDTO {
    String userEmail;
    Integer quizId;
    Float score;
}
