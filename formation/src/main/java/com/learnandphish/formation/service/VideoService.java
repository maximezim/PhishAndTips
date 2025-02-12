package com.learnandphish.formation.service;

import com.learnandphish.formation.model.UserVideoId;
import com.learnandphish.formation.model.UserVideoWatched;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.UserVideoWatchedRepository;
import com.learnandphish.formation.repository.VideoRepository;
import jakarta.transaction.Transactional;
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

    private final List<String> ALLOWED_VIDEO_TYPES = List.of("video/mp4", "video/webm");
    private final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png");
    private final List<String> ALLOWED_CAPTION_TYPES = List.of("text/vtt", "text/srt");

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
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));
        try{
            // Delete associated files from MinioService
            minioService.deleteFile(video.getVideoUrl());
            minioService.deleteFile(video.getThumbnailUrl());
            minioService.deleteFile(video.getCaptionUrl());

            // Delete associated userVideoWatched entries
            userVideoWatchedRepository.deleteByUserVideoIdVideoId(videoId);

            // Delete video
            videoRepository.deleteById(videoId);
        } catch (Exception e) {
            log.error("Failed to delete video", e);
            throw new RuntimeException("Failed to delete video", e);
        }
    }

    // Create a video with file upload
    @Transactional
    public Video createVideo(Video video, MultipartFile videoFile, MultipartFile thumbnailFile, MultipartFile captionFile) {
        // Validate files
        validateFile(videoFile, "video", ALLOWED_VIDEO_TYPES);
        validateFile(thumbnailFile, "thumbnail", ALLOWED_IMAGE_TYPES);
        validateFile(captionFile, "caption", ALLOWED_CAPTION_TYPES);


        if (video.getId()!=null && videoRepository.existsById(video.getId())) {
            throw new IllegalArgumentException("Video with id " + video.getId() + " already exists");
        }

        String videoUrl = null;
        String thumbnailUrl = null;
        String captionUrl = null;

        try {
            videoUrl = minioService.uploadFile(videoFile);
            thumbnailUrl = minioService.uploadFile(thumbnailFile);
            captionUrl = minioService.uploadFile(captionFile);

            video.setVideoUrl(videoUrl);
            video.setThumbnailUrl(thumbnailUrl);
            video.setCaptionUrl(captionUrl);
            return videoRepository.save(video);
        } catch (Exception e) {
            // Cleanup any uploaded files on failure
            cleanupUploadedFiles(videoUrl, thumbnailUrl, captionUrl);
            log.error("Failed to create video", e);
            throw new RuntimeException("Failed to create video", e);
        }
    }

    private void validateFile(MultipartFile file, String type, List<String> allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(type + " file is required");
        }
        String contentType = file.getContentType();
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported " + type + " file type: " + contentType);
        }
    }

    private void cleanupUploadedFiles(String... urls) {
        for (String url : urls) {
            if (url != null) {
                try {
                    minioService.deleteFile(url);
                } catch (Exception e) {
                    log.error("Failed to cleanup file: {}", url, e);
                }
            }
        }
    }
}
