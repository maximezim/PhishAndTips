package com.learnandphish.scoring.service;

import com.learnandphish.scoring.dto.QuizScoreDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AverageScoreService {
    private final FormationService formationService;

    public AverageScoreService(FormationService formationService) {
        this.formationService = formationService;
    }

    public double getUserAverageScore(String email) {
        List<QuizScoreDTO> scores = formationService.getUserQuizScores(email);
        if(scores.isEmpty()){
            return 0;
        }
        double totalScore = scores.stream().mapToDouble(QuizScoreDTO::getScore).sum();
        return totalScore / scores.size() * 10;
    }
}
