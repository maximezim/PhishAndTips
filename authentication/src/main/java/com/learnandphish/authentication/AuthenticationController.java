package com.learnandphish.authentication;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserExportService userExportService;

    /**
     * Endpoint to authenticate users and provide JWT token.
     *
     * @param authenticationRequest The request body containing username and password.
     * @return A ResponseEntity with the JWT token if authentication is successful.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exist");
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials provided");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during authentication");
        }
    }

    /**
     * Method to authenticate the user using the AuthenticationManager.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @throws Exception if authentication fails.
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * Test endpoint to verify if JWT is valid and the user is authenticated.
     *
     * @return A simple message indicating the request was successful.
     */
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome! You are successfully authenticated.");
    }

    /**
     * Endpoint to change the user's password.
     *
     * @param request The request body containing current and new passwords.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        }

        if (!isValidPassword(request.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password does not meet security requirements");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setChangePassword(false);
        userDataRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody String email) {
        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String password = generatePassword();
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setChangePassword(true);
        userDataRepository.save(user);

        JavaMailSender mailSender = new JavaMailSenderImpl();
        EmailSender emailSender = new EmailSender(mailSender);

        String subject = "Réinitialisation de votre mot de passe Phish&Tips";
        String emailContent = "Votre mot de passe Phish&Tips a été réinitialisé.\nVotre nouveau mot de passe est : " + password
                + "\nVous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe."
                + "\nVeuillez changer votre mot de passe dès votre première connexion."
                + "\n\nCordialement,\nL'équipe Phish&Tips";

        try {
            emailSender.sendEmail(email, subject, emailContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("Password reset successfully");
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody String email) {
        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        userDataRepository.delete(user);

        return ResponseEntity.ok("User deleted successfully");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/export-users")
    @Async
    public CompletableFuture<ResponseEntity<byte[]>> exportUsers() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] csvContent = userExportService.exportUsersToCsv();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "users_export.csv");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(csvContent);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(("Error exporting users: " + e.getMessage()).getBytes());
            }
        });
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/test-user")
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("This is an user endpoint");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/test-admin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("This is an admin endpoint");
    }

    @GetMapping("/test-both")
    public ResponseEntity<String> testBoth() {
        return ResponseEntity.ok("This is an user & admin endpoint");
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (!userDataRepository.findByEmail(request.getEmail()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String password = generatePassword();

        UserData user = new UserData();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPosition(request.getPosition());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setChangePassword(true);
        userDataRepository.save(user);

        JavaMailSender mailSender = new JavaMailSenderImpl();
        EmailSender emailSender = new EmailSender(mailSender);

        String subject = "Votre compte Phish&Tips";
        String emailContent = "Votre compte Phish&Tips a été créé.\nVotre mot de passe est : " + password
                + "\nVous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe."
                + "\nVeuillez changer votre mot de passe dès votre première connexion."
                + "\n\nCordialement,\nL'équipe Phish&Tips";

        try {
            emailSender.sendEmail(request.getEmail(), subject, emailContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/get-user")
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token);
        UserData user = userDataRepository.findByEmail(email)
            .stream()
            .findFirst()
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPosition(), user.getRole());
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Validates the strength of the new password.
     *
     * @param password The new password to validate.
     * @return true if the password meets the criteria, false otherwise.
     */
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false; // At least one uppercase letter
        return password.matches(".*\\d.*");   // At least one digit
    }

    private String generatePassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom random = new SecureRandom();
        final int passwordLength = random.nextInt(12,18);

        return random.ints(passwordLength, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
                .toString();
    }
}

