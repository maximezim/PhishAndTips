package com.learnandphish.authentication.user;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setFirstName(StringUtils.capitalize(data[0].trim()));
                    registerRequest.setLastName(StringUtils.capitalize(data[1].trim()));
                    registerRequest.setEmail(data[2].trim());
                    registerRequest.setPosition(StringUtils.capitalize(data[3].trim()));
                    registerRequests.add(registerRequest);
                }
            }
        } catch (IOException e) {
            logger.error("Error importing users from CSV file", e);
        }
        return registerRequests;
    }
}