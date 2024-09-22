package com.learnandphish.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Robin Lafontaine
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.learnandphish.authentication")
@EnableAsync
public class AuthenticationApplication {

	@Autowired
	private UserDataRepository userDataRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeDefaultUser() {
		return args -> {
			if (userDataRepository.findByEmail("admin@example.com").isEmpty()) {
				UserData defaultUser = new UserData();
				defaultUser.setFirstName("Admin");
				defaultUser.setLastName("User");
				defaultUser.setEmail("admin@example.com");
				defaultUser.setPasswordHash(passwordEncoder.encode("password"));
				defaultUser.setRole(Roles.ADMIN);
				userDataRepository.save(defaultUser);
				System.out.println("Default admin user created.");
			} else {
				System.out.println("Default admin user already exists.");
			}
		};
	}
}
