package com.learnandphish.scoring.service;

import com.learnandphish.scoring.entity.FormationEntity;
import com.learnandphish.scoring.factory.AuthenticationDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormationService {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(FormationService.class);
    public FormationService(AuthenticationDatabaseFactory authDbFactory) {
        this.jdbcTemplate = new JdbcTemplate(authDbFactory.getDataSource());
    }

    public List<FormationEntity> getUserFormations(String email) {
    String sql = "SELECT * FROM formations WHERE email = ?"; //TODO: Change table name (make sure cols are correct)
    List<FormationEntity> formations = jdbcTemplate.query(sql, (rs, rowNum) -> {
        FormationEntity formation = new FormationEntity();
        formation.setQuizzId(rs.getInt("quizz_id"));
        formation.setEmail(rs.getString("email"));
        formation.setScore(rs.getFloat("score"));
        return formation;
    }, email);

    if (formations.isEmpty()) {
        logger.error("No formations found for the given email: {}", email);
    }

    return formations;
}
}
