package com.learnandphish.formation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.learnandphish.formation.model.Badge;
import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.FormationRepository;
import com.learnandphish.formation.repository.QuizRepository;
import com.learnandphish.formation.repository.VideoRepository;
import com.learnandphish.formation.repository.BadgeRepository;
import com.learnandphish.formation.service.MinioService;
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

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private MinioService minioService;

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
                            Quiz quiz = quizRepository.findById(Integer.parseInt(quizId)).orElse(new Quiz());
                            // If the quiz doesn't already exist in the db, read the content of the file and create a quiz
                            if (quiz.getId() == null) {
                                String content = Files.readString(file.toPath());
                                String json = gson.toJson(new GsonJsonParser().parseMap(content));
                                quiz.setId(Integer.parseInt(quizId));
                                quiz.setJson(json);
                                quizRepository.save(quiz);
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

    // For each file in data/formation folder, create a formation if name of file (formation id) does not exist in database
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
                            Formation formation = formationRepository.findById(Integer.parseInt(formationId)).orElse(new Formation());
                            // If the formation doesn't already exist in the db, read the content of the file and create a formation
                            if (formation.getId() == null) {
                                String content = Files.readString(file.toPath());
                                JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                                String formationName = obj.get("name").getAsString();
                                String formationDescription = obj.get("description").getAsString();
                                formation.setId(Integer.parseInt(formationId));
                                formation.setName(formationName);
                                formation.setDescription(formationDescription);
                                formationRepository.save(formation);
                            }
                        }
                    }
                } else {
                    log.error("No formation found in data/formation folder");
                }
            } else {
                log.error("Folder data/formation does not exist or is not a directory");
            }
        };
    }

    // For each file in data/videos folder, create a video if name of file (video id) does not exist in database
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
                            Video video = videoRepository.findById(Integer.parseInt(videoId)).orElse(new Video());
                            // If the video doesn't already exist in the db, read the content of the file and create a video
                            if (video.getId() == null) {
                                String content = Files.readString(file.toPath());
                                JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                                String videoTitle = obj.get("title").getAsString();
                                String videoDescription = obj.get("description").getAsString();
                                String videoUrl = obj.get("url").getAsString();
                                video.setId(Integer.parseInt(videoId));
                                video.setTitle(videoTitle);
                                video.setDescription(videoDescription);
                                video.setUrl(videoUrl);
                                videoRepository.save(video);
                            }
                        }
                    }
                } else {
                    log.error("No video found in data/videos folder");
                }
            } else {
                log.error("Folder data/videos does not exist or is not a directory");
            }
        };
    }

    @Bean
    public CommandLineRunner initializeBadge(){
        return args -> {
            File folder = new File("/var/formation/data/badges");
            if (folder.exists() && folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null && listOfFiles.length > 0) {
                    for (File file : listOfFiles) {
                        String badgeId = file.getName(); // File name = badge id
                        if (!badgeId.matches("\\d+")) {
                            log.error("File name not a number");
                        }else {
                            Badge badge = badgeRepository.findById(Integer.parseInt(badgeId)).orElse(new Badge());
                            if (badge.getId() == null) {
                                String content = Files.readString(file.toPath());
                                JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
                                String badgeName = obj.get("name").getAsString();
                                String badgeDescription = obj.get("description").getAsString();
                                String badgeImageUrl = minioService.uploadFile(new File(obj.get("imageUrl").getAsString()));
                                if (badgeImageUrl == null) {
                                    log.error("Error uploading image to S3");
                                    continue;
                                }
                                badge.setId(Long.parseLong(badgeId));
                                badge.setName(badgeName);
                                badge.setDescription(badgeDescription);
                                badge.setImageUrl(badgeImageUrl);
                                badgeRepository.save(badge);
                            }
                        }
                    }
                } else {
                    log.error("No badge found in data/badges/ folder");
                }
            } else {
                log.error("Folder data/badges/ does not exist or is not a directory");
            }
        };
    }

}
