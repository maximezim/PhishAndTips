package com.learnandphish.authentication;

import com.learnandphish.authentication.mail.EmailSender;
import com.learnandphish.authentication.user.Roles;
import com.learnandphish.authentication.user.UserData;
import com.learnandphish.authentication.user.UserDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.javamail.JavaMailSender;
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

	@Autowired
	private JavaMailSender mailSender;

	final Logger logger = LoggerFactory.getLogger(AuthenticationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeDefaultAdmin() {
		return args -> {
			if (userDataRepository.findByEmail("admin@example.com").isEmpty()) {
				UserData defaultUser = new UserData();
				defaultUser.setFirstName("Admin");
				defaultUser.setLastName("Admin");
				defaultUser.setEmail(System.getenv("DEFAULT_ADMIN_EMAIL"));
				defaultUser.setPasswordHash(passwordEncoder.encode(System.getenv("DEFAULT_ADMIN_PASSWORD")));
				defaultUser.setRole(Roles.ADMIN);
				defaultUser.setPosition("Administrator");
				defaultUser.setChangePassword(true);
				userDataRepository.save(defaultUser);
				logger.info("Default admin user created.");
			} else {
				logger.info("Default admin user already exists.");
			}
		};
	}

	@Bean
	public CommandLineRunner checkSMTPStatus() {
		return args -> {
			EmailSender emailSender = new EmailSender(mailSender);
			if (emailSender.isSmtpAvailable()) {
				logger.info("SMTP server is up and running.");
			} else {
				logger.error("SMTP server is down.");
			}
		};
	}
}
