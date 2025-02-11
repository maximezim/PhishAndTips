package com.learnandphish.scoring.service;

import com.learnandphish.scoring.dto.QuizScoreDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Service
public class FormationService {


    private final Logger logger = LoggerFactory.getLogger(FormationService.class);

    public List<QuizScoreDTO> getUserQuizScores(String email) {
        String url = "http://formation-service:8089/formation/quizzes/scores";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        headers.set("email", email);
        ResponseEntity<List<QuizScoreDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            logger.error("Failed to fetch user formations for email: {}", email);
            return List.of();
        }
    }
}
