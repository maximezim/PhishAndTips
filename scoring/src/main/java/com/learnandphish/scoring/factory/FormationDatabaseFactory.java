package com.learnandphish.scoring.factory;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class FormationDatabaseFactory {
    @Value("${formation.datasource.url:jdbc:postgresql://postgres-formation:5432/formation_db}")
    private String url;
    
    @Value("${formation.datasource.username:postgres}")
    private String username;
    
    @Value("${formation.datasource.password:password}")
    private String password;

    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
