package com.learnandphish.formation.dto;

import lombok.Data;

@Data
public class UserQuizScoreDTO {
    String user_email;
    Integer quiz_id;
    double score;
}
