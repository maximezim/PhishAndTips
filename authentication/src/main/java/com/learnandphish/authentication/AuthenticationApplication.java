package com.learnandphish.authentication;

import com.learnandphish.authentication.user.Roles;
import com.learnandphish.authentication.user.UserData;
import com.learnandphish.authentication.user.UserDataRepository;
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
	public CommandLineRunner initializeDefaultAdmin() {
		return args -> {
			if (userDataRepository.findByEmail("admin@example.com").isEmpty()) {
				UserData defaultUser = new UserData();
				defaultUser.setFirstName("Test");
				defaultUser.setLastName("Admin");
				defaultUser.setEmail("admin@example.com");
				defaultUser.setPasswordHash(passwordEncoder.encode("password"));
				defaultUser.setRole(Roles.ADMIN);
				defaultUser.setPosition("Administrator");
				defaultUser.setChangePassword(false);
				userDataRepository.save(defaultUser);
				System.out.println("Test admin user created.");
			} else {
				System.out.println("Test admin user already exists.");
			}
		};
	}

	@Bean
	public CommandLineRunner initializeDefaultUser() {
		return args -> {
			if (userDataRepository.findByEmail("user@example.com").isEmpty()) {
				UserData defaultUser = new UserData();
				defaultUser.setFirstName("Test");
				defaultUser.setLastName("User");
				defaultUser.setEmail("user@example.com");
				defaultUser.setPasswordHash(passwordEncoder.encode("password"));
				defaultUser.setRole(Roles.USER);
				defaultUser.setPosition("User");
				defaultUser.setChangePassword(false);
				userDataRepository.save(defaultUser);
				System.out.println("Test user user created.");
			} else {
				System.out.println("Test user already exists.");
			}
		};
	}

	@Bean
	public CommandLineRunner initializeKarineUser() {
		return args -> {
			if (userDataRepository.findByEmail("karine@example.com").isEmpty()) {
				UserData karineUser = new UserData();
				karineUser.setFirstName("Karine");
				karineUser.setLastName("Lafontaine");
				karineUser.setEmail("karine@example.com");
				karineUser.setPasswordHash(passwordEncoder.encode("password"));
				karineUser.setRole(Roles.USER);
				karineUser.setPosition("G.O.A.T.");
				karineUser.setChangePassword(false);
				userDataRepository.save(karineUser);
				System.out.println("Karine user user created.");
			} else {
				System.out.println("Karine user already exists.");
			}
		};
	}
}
