package com.learnandphish.authentication.user;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserUtilsService {

    @Autowired
    private UserDataRepository userDataRepository;

    final Logger logger = LoggerFactory.getLogger(UserUtilsService.class);

    public byte[] exportUsersToCsv() throws IOException {
        List<UserData> users = userDataRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("First Name,Last Name,Email,Position\n");

        for (UserData user : users) {
            csvContent.append(String.format("%s,%s,%s,%s\n",
                    escapeSpecialCharacters(user.getFirstName()),
                    escapeSpecialCharacters(user.getLastName()),
                    escapeSpecialCharacters(user.getEmail()),
                    escapeSpecialCharacters(user.getPosition())));
        }

        return csvContent.toString().getBytes();
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public List<GophishUserDTO> convertToGophishUsersDTO(List<UserData> userData) {
        return userData.stream().map(user -> {
            GophishUserDTO gophishUserDTO = new GophishUserDTO();
            gophishUserDTO.setFirst_name(user.getFirstName());
            gophishUserDTO.setLast_name(user.getLastName());
            gophishUserDTO.setEmail(user.getEmail());
            gophishUserDTO.setPosition(user.getPosition());
            return gophishUserDTO;
        }).toList();
    }

    public List<RegisterRequest> importUsersFromCsv(MultipartFile file) {
        List<RegisterRequest> registerRequests = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length != 4) {
                    logger.error("Missing fields in CSV file: {}", (Object) values);
                    logger.info("Make sure the separator is a comma and the file is not corrupted");
                    throw new IllegalArgumentException("CSV file has missing fields");
                }
                if (!values[2].matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { // Check if email is valid, if not skip (header check)
                    logger.warn("Invalid email format: {}", values[2]);
                    continue;
                }
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setFirstName(StringUtils.capitalize(values[0].trim()));
                registerRequest.setLastName(StringUtils.capitalize(values[1].trim()));
                registerRequest.setEmail(values[2].trim());
                registerRequest.setPosition(StringUtils.capitalize(values[3].trim()));
                registerRequests.add(registerRequest);
            }
        } catch (IOException e) {
            logger.error("Error while reading CSV file", e);
            throw new RuntimeException("Error while reading CSV file", e);
        }
        return registerRequests;
    }

    public Boolean emailDomainAllowed(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        String allowedDomainsEnv = System.getenv("ALLOWED_EMAIL_DOMAINS");
        if (StringUtils.isBlank(allowedDomainsEnv)) {
            return true;
        }
        List<String> allowedDomains = List.of(allowedDomainsEnv.split(","));
        return allowedDomains.contains(domain);
    }
}