package com.learnandphish.authentication.mail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MailTemplates {


    private static final String DEFAULT_RESET_TEMPLATE = "<html><body><p>Votre mot de passe <strong>Phish&amp;Tips</strong> a été réinitialisé.</p><p>Votre nouveau mot de passe est : <strong>{{NEW_PASSWORD}}</strong></p><p>Vous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe.</p><p>Veuillez changer votre mot de passe dès votre première connexion.</p><p>Cordialement,</p><p>L'équipe <strong>Phish&amp;Tips</strong> © {{CURRENT_YEAR}}</p></body></html>";

    private static final String DEFAULT_REGISTERED_TEMPLATE = "<html><body><p>Votre compte <strong>Phish&amp;Tips</strong> a été créé.</p><p>Votre mot de passe est : <strong>{{NEW_PASSWORD}}</strong></p><p>Vous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe.</p><p>Veuillez changer votre mot de passe dès votre première connexion.</p><p>Cordialement,</p><p>L'équipe <strong>Phish&amp;Tips</strong> © {{CURRENT_YEAR}}</p></body></html>";

    public static String loadPasswordResetTemplate(String newPassword) {
        try {
            Path templatePath = Paths.get("/var/authentication/templates/reset.html");
            return Files.readString(templatePath)
                    .replace("{{CURRENT_YEAR}}", String.valueOf(java.time.Year.now()))
                    .replace("{{NEW_PASSWORD}}", newPassword);
        } catch (Exception e) {
            return DEFAULT_RESET_TEMPLATE
                    .replace("{{CURRENT_YEAR}}", String.valueOf(java.time.Year.now()))
                    .replace("{{NEW_PASSWORD}}", newPassword);
        }
    }

    public static String loadRegisteredTemplate(String newPassword) {
        try {
            Path templatePath = Paths.get("/var/authentication/templates/registered.html");
            return Files.readString(templatePath)
                    .replace("{{CURRENT_YEAR}}", String.valueOf(java.time.Year.now()))
                    .replace("{{NEW_PASSWORD}}", newPassword);
        } catch (Exception e) {
            return DEFAULT_REGISTERED_TEMPLATE
                    .replace("{{CURRENT_YEAR}}", String.valueOf(java.time.Year.now()))
                    .replace("{{NEW_PASSWORD}}", newPassword);
        }
    }
}
