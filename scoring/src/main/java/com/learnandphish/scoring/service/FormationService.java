package com.learnandphish.scoring.service;

import com.learnandphish.scoring.entity.FormationDTO;
import com.learnandphish.scoring.factory.AuthenticationDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FormationService {


    private final Logger logger = LoggerFactory.getLogger(FormationService.class);

    public List<FormationDTO> getUserFormations(String email) {
        String url = "http://formation-service:8089/formation/getUserScores?email=" + email;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<FormationDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FormationDTO>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            logger.error("Failed to fetch user formations for email: {}", email);
            return List.of();
        }
    }
}
