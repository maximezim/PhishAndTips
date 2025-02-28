package com.learnandphish.authentication.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class EmailSender {

    private final JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(System.getenv("MAIL_FROM"), "Support Phish&Tips");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        try {
            File image = new File("/var/authentication/templates/ptlogo.png");
            helper.addInline("ptlogo", image);
        } catch (Exception e) {
            logger.warn("Error while adding image to email, but still sent anyway", e);
        }

        mailSender.send(message);
    }

    public boolean isSmtpAvailable() {
        try {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            Session session = mailSenderImpl.getSession();

            Transport transport = session.getTransport("smtp");
            transport.connect(
                    mailSenderImpl.getHost(),
                    mailSenderImpl.getPort(),
                    mailSenderImpl.getUsername(),
                    mailSenderImpl.getPassword()
            );

            transport.close();
            return true;
        } catch (MessagingException e) {
            logger.error("SMTP connection failed: {}", e.getMessage());
            return false;
        }
    }
}