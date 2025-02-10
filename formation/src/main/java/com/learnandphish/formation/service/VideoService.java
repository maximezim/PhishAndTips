package com.learnandphish.formation.service;

import com.learnandphish.formation.model.UserVideoId;
import com.learnandphish.formation.model.UserVideoWatched;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.UserVideoWatchedRepository;
import com.learnandphish.formation.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserVideoWatchedRepository userVideoWatchedRepository;

    // Get a video by id
    public Video getVideoById(Integer id) {
        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
    }

    // Get all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // Set isWatched to true for a user and a video
    public void setIsWatched(String userEmail, Integer videoId) {
        userVideoWatchedRepository.findById(new UserVideoId(userEmail, videoId)).ifPresentOrElse(
                userVideoWatched -> {
                    userVideoWatched.setIsWatched(true);
                    userVideoWatchedRepository.save(userVideoWatched);
                },
                () -> {
                    UserVideoWatched userVideoWatched = new UserVideoWatched();
                    userVideoWatched.setUserVideoId(new UserVideoId(userEmail, videoId));
                    userVideoWatched.setIsWatched(true);
                    userVideoWatchedRepository.save(userVideoWatched);
                }
        );
    }

    public List<Video> getVideosWatchedByUser(String email) {
        List<UserVideoWatched> userVideosWatched = userVideoWatchedRepository.findByUserVideoIdUserEmailAndIsWatchedTrue(email);
        return videoRepository.findAllById(userVideosWatched.stream().map(UserVideoWatched::getUserVideoId).map(UserVideoId::getVideoId).toList());
    }
}
