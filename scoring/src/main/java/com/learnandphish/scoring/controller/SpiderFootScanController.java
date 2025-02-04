package com.learnandphish.scoring.controller;

import com.learnandphish.scoring.service.SpiderFootScanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class SpiderFootScanController {

    private final SpiderFootScanService spiderFootScanService;

    public SpiderFootScanController(SpiderFootScanService spiderFootScanService) {
        this.spiderFootScanService = spiderFootScanService;
    }

    @GetMapping("/scoring/results")
    public Map<String, Object> getResultCount(@RequestHeader("email") String email) {
        int count = spiderFootScanService.countResults(email);
        return Map.of("email", email, "resultCount", count);
    }
}
