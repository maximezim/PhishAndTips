package com.learnandphish.formation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.FormationRepository;
import com.learnandphish.formation.repository.QuizRepository;
import com.learnandphish.formation.repository.VideoRepository;
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
    private QuizRepository quizRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private VideoRepository videoRepository;

    // For each file in data/quiz folder, create a quiz if name of file (quiz id) does not exist in database
    @Bean
    public CommandLineRunner initializeQuiz() {
        return args -> {
            File folder = new File("/var/formation/data/quiz");
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null && listOfFiles.length > 0) {
                    Gson gson = new Gson();
                    for (File file : listOfFiles) {
                        String quizId = file.getName();
                        // Make sure the file's name is a number
                        if (!quizId.matches("\\d+")) {
                            log.error("File name is not a number");
                        }else {
                            Quiz quiz = quizRepository.findById(Integer.parseInt(quizId)).orElse(null);
                            if (quiz == null) {
                                String content = Files.readString(file.toPath());
                                String json = gson.toJson(new GsonJsonParser().parseMap(content));
                                Quiz newquiz = new Quiz();
                                newquiz.setId(Integer.parseInt(quizId));
                                newquiz.setJson(json);
                                quizRepository.save(newquiz);
                            }
                        }
                    }
                } else {
                    log.error("No quiz found in data/quiz folder");
                }
            } else {
                log.error("Folder data/quiz does not exist or is not a directory");
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
                    log.error("No quiz found in data/formation folder");
                }
            } else {
                log.error("Folder data/formation does not exist or is not a directory");
            }
        };
    }

    @Bean
    public CommandLineRunner initializeVideo(){
        return args -> {
            File folder = new File("/var/formation/data/videos");
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null && listOfFiles.length > 0) {
                    for (File file : listOfFiles) {
                        String videoId = file.getName();
                        // Make sure the file's name is a number
                        if (!videoId.matches("\\d+")) {
                            log.error("File name is not a number");
                        }else {
                            String content = Files.readString(file.toPath());
                            JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                            String videoTitle = obj.get("title").getAsString();
                            String videoDescription = obj.get("description").getAsString();
                            String videoUrl = obj.get("url").getAsString();
                            Video video = new Video();
                            video.setId(Integer.parseInt(videoId));
                            video.setTitle(videoTitle);
                            video.setDescription(videoDescription);
                            video.setUrl(videoUrl);
                            videoRepository.save(video);

                        }
                    }
                } else {
                    log.error("No video found in data/formation folder");
                }
            } else {
                log.error("Folder data/videos does not exist or is not a directory");
            }
        };
    }

}
