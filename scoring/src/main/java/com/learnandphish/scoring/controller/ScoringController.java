package com.learnandphish.scoring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scoring")
@RequiredArgsConstructor
public class ScoringController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
