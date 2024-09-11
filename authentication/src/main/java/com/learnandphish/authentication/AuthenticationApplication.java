package com.learnandphish.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Robin Lafontaine
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.learnandphish.authentication")
public class AuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

}
