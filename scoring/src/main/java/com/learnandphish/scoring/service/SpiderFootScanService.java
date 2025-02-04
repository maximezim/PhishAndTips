package com.learnandphish.scoring.service;

import com.learnandphish.scoring.factory.AuthenticationDatabaseFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpiderFootScanService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SpiderFootScanService(AuthenticationDatabaseFactory authDbFactory) {
        this.jdbcTemplate = new JdbcTemplate(authDbFactory.getDataSource());
    }

    public int countResults(String target) {
        String sql = "SELECT result FROM spiderfoot_scans WHERE target = ?";
        // Assume there's only one row per target.
        String jsonResult = jdbcTemplate.queryForObject(sql, String.class, target);
        if (jsonResult == null || jsonResult.isEmpty()) {
            return 0;
        }
        try {
            JsonNode root = objectMapper.readTree(jsonResult);
            JsonNode parsedData = root.path("parsed_data");
            JsonNode results = parsedData.path("results");
            if (results.isArray()) {
                return results.size();
            }
        } catch (Exception e) {
            // handle exception or rethrow as runtime exception
            throw new RuntimeException("Error parsing JSON result", e);
        }
        return 0;
    }
}
