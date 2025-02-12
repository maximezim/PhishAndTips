package com.learnandphish.formation.service;

import com.learnandphish.formation.model.UserVideoId;
import com.learnandphish.formation.model.UserVideoWatched;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.UserVideoWatchedRepository;
import com.learnandphish.formation.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserVideoWatchedRepository userVideoWatchedRepository;
    private final MinioService minioService;

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

    // Delete a video
    public void deleteVideo(Integer videoId) {
        videoRepository.deleteById(videoId);
    }

    // Create a video with file upload
    public Video createVideo(Video video, MultipartFile videoFile, MultipartFile thumbnailFile, MultipartFile captionFile) {
        if (videoRepository.existsById(video.getId())) {
            return null;
        }
        try {
            String videoUrl = minioService.uploadFileFromFront(videoFile);
            String thumbnailUrl = minioService.uploadFileFromFront(thumbnailFile);
            String captionUrl = minioService.uploadFileFromFront(captionFile);
            if (videoUrl == null || thumbnailUrl == null || captionUrl == null) {
                return null;
            }
            video.setVideoUrl(videoUrl);
            video.setThumbnailUrl(thumbnailUrl);
            video.setCaptionUrl(captionUrl);
            videoRepository.save(video);
            return video;
        } catch (Exception e) {
            return null;
        }
    }
}
