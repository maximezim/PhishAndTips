package com.learnandphish.scoring.service;

import org.springframework.stereotype.Service;

@Service
public class TotalScoreService {

    private final GophishActionService gophishActionService;
    private final OsintScoringService osintScoringService;
    private final AverageScoreService averageScoreService;

    public TotalScoreService(GophishActionService gophishActionService,
                             OsintScoringService osintScoringService,
                             AverageScoreService averageScoreService) {
        this.gophishActionService = gophishActionService;
        this.osintScoringService = osintScoringService;
        this.averageScoreService = averageScoreService;
    }

    public double getTotalScore(String email) {
        double gophishScore = gophishActionService.getUserOverallScore(email) * 10; 
        double osintScore = osintScoringService.getOsintScore(email) * 10;         
        double formationScore = 100 - averageScoreService.getUserAverageScore(email) * 10;     

        return gophishScore * 0.5 + formationScore * 0.3 + osintScore * 0.2;
    }
}
