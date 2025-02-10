package com.learnandphish.formation;

import com.google.gson.Gson;
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

    // For each object in data/formations/formations.json, create a formation if id does not exist in database
    @Bean
    public CommandLineRunner initializeFormation(){
        return args -> {
           File file = new File("/var/formation/data/formations/formations.json");
                if (file.exists() && file.isFile()) {
                    String content = Files.readString(file.toPath());
                    Gson gson = new Gson();
                    Formation[] formations = gson.fromJson(content, Formation[].class);
                    for (Formation formation : formations) {
                        Formation existingFormation = formationRepository.findById(formation.getId()).orElse(new Formation());
                        if (existingFormation.getId() == null) {
                            formationRepository.save(formation);
                        }
                    }
                } else {
                    log.error("File formations.json does not exist or is not a file");
                }
        };
    }

    // For each object in data/videos/videos.json, create a video if id does not exist in database
    @Bean
    public CommandLineRunner initializeVideo(){
        return args -> {
            File file = new File("/var/formation/data/videos/videos.json");
                if (file.exists() && file.isFile()) {
                    String content = Files.readString(file.toPath());
                    Video[] videos = new Gson().fromJson(content, Video[].class);
                    for (Video video : videos) {
                        videoRepository.findById(video.getId()).ifPresentOrElse(
                            existingVideo -> log.info("Video with id {} already exists", video.getId()),
                            () -> {
                                try {
                                    String videoUrl = minioService.uploadFile(new File(video.getVideoUrl()));
                                    String thumbnailUrl = minioService.uploadFile(new File(video.getThumbnailUrl()));
                                    String captionUrl = minioService.uploadFile(new File(video.getCaptionUrl()));
                                    if (videoUrl == null || thumbnailUrl == null || captionUrl == null) {
                                        throw new Exception("Video upload failed");
                                    }
                                    video.setVideoUrl(videoUrl);
                                    video.setThumbnailUrl(thumbnailUrl);
                                    video.setCaptionUrl(captionUrl);
                                    videoRepository.save(video);
                                } catch (Exception e) {
                                    log.error("Error uploading video to S3", e);
                                }
                            }
                        );
                    }
                } else {
                    log.error("File videos.json does not exist or is not a file");
                }
        };
    }

    // For each object in data/badges/badges.json, create a badge if id does not exist in database
    @Bean
    public CommandLineRunner initializeBadge(){
        return args -> {
            File file = new File("/var/formation/data/badges/badges.json");
            if (file.exists() && file.isFile()) {
                String content = Files.readString(file.toPath());
                Badge[] badges = new Gson().fromJson(content, Badge[].class);
                for (Badge badge : badges) {
                    badgeRepository.findById(badge.getId()).ifPresentOrElse(
                        existingBadge -> log.info("Badge with id {} already exists", badge.getId()),
                        () -> {
                            try {
                                String badgeImageUrl = minioService.uploadFile(new File(badge.getImageUrl()));
                                if (badgeImageUrl == null) {
                                    throw new Exception("Badge upload failed");
                                }
                                badge.setImageUrl(badgeImageUrl);
                                badgeRepository.save(badge);
                            } catch (Exception e) {
                                log.error("Error uploading badge to S3", e);
                            }
                        }
                    );
                }
            } else {
                log.error("File badges.json does not exist or is not a file");
            }
        };
    }

}
