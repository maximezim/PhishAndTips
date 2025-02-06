package com.learnandphish.scoring.service;

import org.springframework.stereotype.Service;

@Service
public class OsintScoringService {

    private final SpiderFootScanService spiderFootScanService;

    public OsintScoringService(SpiderFootScanService spiderFootScanService) {
        this.spiderFootScanService = spiderFootScanService;
    }

    public double getOsintScore(String email) {
        int count = spiderFootScanService.countResults(email);
        double score = Math.log10(count + 1) * 7;
        return Math.max(0, Math.min(10, score));
    }
}
