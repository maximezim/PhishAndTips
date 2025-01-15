package com.learnandphish.authentication.mail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(System.getenv("SPRING_MAIL_HOST"));
        mailSender.setPort(Integer.parseInt(System.getenv("SPRING_MAIL_PORT")));

        mailSender.setUsername(System.getenv("SPRING_MAIL_USERNAME"));
        mailSender.setPassword(System.getenv("SPRING_MAIL_PASSWORD"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", System.getenv("SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE"));
        props.put("mail.debug", "true");

        return mailSender;
    }
}
