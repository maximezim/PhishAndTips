package com.learnandphish.scoring.service;

import com.learnandphish.scoring.factory.AuthenticationDatabaseFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpiderFootScanService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(SpiderFootScanService.class);

    public SpiderFootScanService(AuthenticationDatabaseFactory authDbFactory) {
        this.jdbcTemplate = new JdbcTemplate(authDbFactory.getDataSource());
    }

    public int countResults(String target) {
        String sql = "SELECT result FROM spiderfoot_scans WHERE target = ?";
        String jsonResult;
        try {
            jsonResult = jdbcTemplate.queryForObject(sql, String.class, target);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
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
            logger.error("Error parsing JSON result", e);
        }
        return 0;
    }
}
