package com.learnandphish.formation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.model.Quizz;
import com.learnandphish.formation.repository.FormationRepository;
import com.learnandphish.formation.repository.QuizzRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Files;

@Slf4j
@SpringBootApplication
public class FormationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormationApplication.class, args);
    }

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private FormationRepository formationRepository;

    // For each file in data/quizz folder, create a quizz if name of file (quizz id) does not exist in database
    @Bean
    public CommandLineRunner initializeQuizz() {
        return args -> {
            File folder = new File("/var/formation/data/quizz");
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null && listOfFiles.length > 0) {
                    Gson gson = new Gson();
                    for (File file : listOfFiles) {
                        String quizzId = file.getName();
                        // Make sure the file's name is a number
                        if (!quizzId.matches("\\d+")) {
                            log.error("File name is not a number");
                        }else {
                            Quizz quizz = quizzRepository.findById(Integer.parseInt(quizzId)).orElse(null);
                            if (quizz == null) {
                                String content = Files.readString(file.toPath());
                                String json = gson.toJson(new GsonJsonParser().parseMap(content));
                                Quizz newQuizz = new Quizz();
                                newQuizz.setId(Integer.parseInt(quizzId));
                                newQuizz.setJson(json);
                                quizzRepository.save(newQuizz);
                            }
                        }
                    }
                } else {
                    log.error("No quizz found in data/quizz folder");
                }
            } else {
                log.error("Folder data/quizz does not exist or is not a directory");
            }
        };
    }

    @Bean
    public CommandLineRunner initializeFormation(){
        return args -> {
            File folder = new File("/var/formation/data/formations");
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null && listOfFiles.length > 0) {
                    for (File file : listOfFiles) {
                        String formationId = file.getName();
                        // Make sure the file's name is a number
                        if (!formationId.matches("\\d+")) {
                            log.error("File name is not a number");
                        }else {
                            String content = Files.readString(file.toPath());
                            JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                            String formationName = obj.get("name").getAsString();
                            String formationDescription = obj.get("description").getAsString();
                            Formation formation = new Formation();
                            formation.setId(Integer.parseInt(formationId));
                            formation.setName(formationName);
                            formation.setDescription(formationDescription);
                            formationRepository.save(formation);
                        }
                    }
                } else {
                    log.error("No quizz found in data/formation folder");
                }
            } else {
                log.error("Folder data/formation does not exist or is not a directory");
            }
        };
    }

}
