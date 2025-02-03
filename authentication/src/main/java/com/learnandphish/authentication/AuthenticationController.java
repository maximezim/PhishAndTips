package com.learnandphish.authentication;

import com.learnandphish.authentication.jwt.JWTUtil;
import com.learnandphish.authentication.user.ScanResult;
import com.learnandphish.authentication.jwt.JwtRequest;
import com.learnandphish.authentication.jwt.JwtResponse;
import com.learnandphish.authentication.config.RestTemplateConfig;
import com.learnandphish.authentication.jwt.JwtUserDetailsService;
import com.learnandphish.authentication.user.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;

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

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserScanRepository userScanRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Updated base URL with proper API prefix
    private final String spiderfootApiUrl = "http://spiderfoot-api:8000/internal/spiderfoot";

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
     * @throws BadCredentialsException if authentication fails.
     */
    private void authenticate(String username, String password) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
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

        EmailSender emailSender = new EmailSender(mailSender);

        //TODO: Fix content, use emailTemplate
        String subject = "Votre mot de passe Phish&Tips";
        String emailContent = "<html>" +
                "<body>" +
                "<p>Votre mot de passe <strong>Phish&amp;Tips</strong> a été réinitialisé.</p>" +
                "<p>Votre nouveau mot de passe est : <strong>" + password + "</strong></p>" +
                "<p>Vous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe.</p>" +
                "<p>Veuillez changer votre mot de passe dès votre première connexion.</p>" +
                "<p>Cordialement,</p>" +
                "<p>L'équipe <strong>Phish&amp;Tips</strong></p>" +
                "</body>" +
                "</html>";

        try {
            emailSender.sendEmail(email, subject, emailContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("Password reset successfully");
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/need-change-password")
    public ResponseEntity<Boolean> needChangePassword() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(user.getChangePassword());
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

        EmailSender emailSender = new EmailSender(mailSender);

        String subject = "Votre compte Phish&Tips";
        String emailContent = "<html>" +
                "<body>" +
                "<p>Votre compte <strong>Phish&amp;Tips</strong> a été créé.</p>" +
                "<p>Votre mot de passe est : <strong>" + password + "</strong></p>" +
                "<p>Vous pouvez vous connecter à l'application avec votre adresse email professionnel et ce mot de passe.</p>" +
                "<p>Veuillez changer votre mot de passe dès votre première connexion.</p>" +
                "<p>Cordialement,</p>" +
                "<p>L'équipe <strong>Phish&amp;Tips</strong></p>" +
                "</body>" +
                "</html>";

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
        String extractedToken = token.substring(7);
        String email = jwtUtil.extractUsername(extractedToken);
        UserData user = userDataRepository.findByEmail(email)
            .stream()
            .findFirst()
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPosition(user.getPosition());
        userDTO.setRole(user.getRole());
        return ResponseEntity.ok(userDTO);
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-all-users")
    public ResponseEntity<List<GophishUserDTO>> getAllUsers() {
        List<UserData> users = userDataRepository.findAll();
        return ResponseEntity.ok(userExportService.convertToGophishUsersDTO(users));
    }

    // Endpoint for users to retrieve their own scan result using JWT extracted email
    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/my-scan")
    public ResponseEntity<?> getMyScan() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ScanResult scan = userScanRepository.findScanByEmail(email);
        if (scan == null) {
            return ResponseEntity.status(404).body("No scan result found for " + email);
        }
        return ResponseEntity.ok(scan);
    }

    // Endpoint for admin to retrieve a scan result for any provided email
    @RolesAllowed("ADMIN")
    @PostMapping("/admin/scan")
    public ResponseEntity<?> getScanByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email must be provided");
        }
        ScanResult scan = userScanRepository.findScanByEmail(email);
        if (scan == null) {
            return ResponseEntity.status(404).body("No scan result found for " + email);
        }
        return ResponseEntity.ok(scan);
    }

    // Endpoint for users to initiate a new scan (email extracted from JWT)
    @RolesAllowed({"USER", "ADMIN"})
    @PostMapping("/my-scan/new")
    public ResponseEntity<?> startMyScan() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> payload = new HashMap<>();
        payload.put("target", email);
        payload.put("modules", ""); // set default modules if needed

        ResponseEntity<?> response = restTemplate.postForEntity(spiderfootApiUrl + "/scan", payload, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Endpoint for admin to initiate a new scan for any provided email
    @RolesAllowed("ADMIN")
    @PostMapping("/admin/scan/new")
    public ResponseEntity<?> startScanForEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email must be provided");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("target", email);
        payload.put("modules", ""); // set default modules if needed

        ResponseEntity<?> response = restTemplate.postForEntity(spiderfootApiUrl + "/scan", payload, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
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

