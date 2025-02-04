package com.learnandphish.scoring.factory;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationDatabaseFactory {
    @Value("${auth.datasource.url:jdbc:postgresql://postgres:5432/authentication_db}")
    private String url;
    
    @Value("${auth.datasource.username:postgres}")
    private String username;
    
    @Value("${auth.datasource.password:password}")
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
