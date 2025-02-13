package com.learnandphish.authentication;

import com.learnandphish.authentication.jwt.JWTUtil;
import com.learnandphish.authentication.mail.EmailSender;
import com.learnandphish.authentication.mail.MailTemplates;
import com.learnandphish.authentication.user.ScanResult;
import com.learnandphish.authentication.jwt.JwtRequest;
import com.learnandphish.authentication.jwt.JwtResponse;
import com.learnandphish.authentication.jwt.JwtUserDetailsService;
import com.learnandphish.authentication.user.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
    private UserUtilsService userUtilsService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserScanRepository userScanRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String spiderfootApiUrl = "http://spiderfoot-api:8001/internal/spiderfoot";

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

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
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

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
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String password = generatePassword();
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setChangePassword(true);
        userDataRepository.save(user);

        EmailSender emailSender = new EmailSender(mailSender);

        String subject = "[Phish&Tips] Réinitialisation de votre mot de passe";
        String emailContent = MailTemplates.loadPasswordResetTemplate(password);

        try {
            emailSender.sendEmail(email, subject, emailContent);
        } catch (Exception e) {
            logger.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("Password reset successfully");
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/need-change-password")
    public ResponseEntity<Boolean> needChangePassword() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            UserData user = userDataRepository.findByEmail(email)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(user.getChangePassword());
        } catch (Exception e) {
            logger.error("Error checking if user needs to change password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/update-user")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO) {
        try {

            if (userDTO.getRole() == null) {
                return ResponseEntity.badRequest().body("Role must be provided");
            }

            UserData user = userDataRepository.findByEmail(userDTO.getEmail())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPosition(userDTO.getPosition());
            user.setRole(userDTO.getRole());
            userDataRepository.save(user);

            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return ResponseEntity.badRequest().body("Role must be provided");
        }
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestHeader("email") String tokenEmail, @RequestBody String email) {
        UserData user = userDataRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (Objects.equals(user.getEmail(), tokenEmail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot delete your own account");
        }

        userDataRepository.delete(user);

        return ResponseEntity.ok("User deleted successfully");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/export-users")
    @Async
    public CompletableFuture<ResponseEntity<byte[]>> exportUsers() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] csvContent = userUtilsService.exportUsersToCsv();
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

    @RolesAllowed("ADMIN")
    @PostMapping("/import-users")
    public ResponseEntity<?> importUsers(@RequestBody MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        if (!Objects.equals(file.getContentType(), "text/csv") && !Objects.equals(file.getContentType(), "application/vnd.ms-excel")) {
            return ResponseEntity.badRequest().body("File must be a CSV");
        }

        List<RegisterRequest> usersToRegister = userUtilsService.importUsersFromCsv(file);

        if (usersToRegister.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid CSV file format");
        }

        for (RegisterRequest user : usersToRegister) {
            try {
                ResponseEntity<?> response = register(user);
                if (response.getStatusCode() != HttpStatus.OK) {
                    return response;
                }
            } catch (Exception e) {
                logger.error("Error importing user", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing users");
            }
        }
        return ResponseEntity.ok("Users imported successfully");
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        if (!userUtilsService.emailDomainAllowed(request.getEmail())) {
            logger.error("Email domain not allowed: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email domain not allowed");
        }

        if (!userDataRepository.findByEmail(request.getEmail()).isEmpty()) {
            logger.error("User already exists: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String password = generatePassword();

        UserData user = new UserData();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(Roles.USER);
        user.setPosition(request.getPosition());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setChangePassword(true);
        userDataRepository.save(user);

        logger.info("User registered: {}", user.getEmail());

        EmailSender emailSender = new EmailSender(mailSender);

        String subject = "[Phish&Tips] Votre compte a été créé";
        String emailContent = MailTemplates.loadRegisteredTemplate(password);

        try {
            emailSender.sendEmail(request.getEmail(), subject, emailContent);
            logger.info("Email sent to: {}", request.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/get-roles")
    public ResponseEntity<Roles[]> getRoles() {
        return ResponseEntity.ok(Roles.values());
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        String extractedToken = token.substring(7);
        String email = jwtUtil.extractUsername(extractedToken);
        UserData user = userDataRepository.findByEmail(email)
            .stream()
            .findFirst()
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UserDTO userDTO = new UserDTO(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPosition(),
            user.getRole()
        );
        return ResponseEntity.ok(userDTO);
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-all-gophish-users")
    public ResponseEntity<List<GophishUserDTO>> getAllGophishUsers() {
        List<UserData> users = userDataRepository.findAll();
        List<GophishUserDTO> usersDTO = userUtilsService.convertToGophishUsersDTO(users);
        return ResponseEntity.ok(usersDTO);
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping("/get-all-users")
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<UserDTO> assembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        Page<UserDTO> usersDTO = userDataRepository.findAll(pageable)
                .map(user -> new UserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosition(),
                        user.getRole()
                ));

        return ResponseEntity.ok(assembler.toModel(usersDTO));
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
        Map<String, Object> payload = Map.of(
                "target", email,
                "modules", ""
        );

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
       Map<String, Object> payload = Map.of(
            "target", email,
            "modules", ""
        );

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
        if (password.chars().noneMatch(Character::isUpperCase)) return false; // At least one uppercase letter
        return password.chars().anyMatch(Character::isDigit);   // At least one digit
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

