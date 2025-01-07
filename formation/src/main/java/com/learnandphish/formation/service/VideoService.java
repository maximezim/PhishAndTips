package com.learnandphish.formation.service;

import com.learnandphish.formation.dto.VideoRequest;
import com.learnandphish.formation.model.Video;
import com.learnandphish.formation.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

    public Video createVideo(VideoRequest videoRequest) {
        Video video = new Video(videoRequest.id(),
                videoRequest.formationId(),
                videoRequest.title(),
                videoRequest.description(),
                videoRequest.url(),
                videoRequest.duration(),
                videoRequest.quizId());
        log.info("Video {} created successfully", video);
        return videoRepository.save(video);
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
    }

    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }

    public Video updateVideo(VideoRequest videoRequest) {
        Video video = videoRepository.findById(videoRequest.id())
                .orElseThrow(() -> new RuntimeException("Video not found"));
        video.setFormationId(videoRequest.formationId());
        video.setTitle(videoRequest.title());
        video.setDescription(videoRequest.description());
        video.setUrl(videoRequest.url());
        video.setDuration(videoRequest.duration());
        video.setQuizId(videoRequest.quizId());
        return videoRepository.save(video);
    }

    public Iterable<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Iterable<Video> getVideosByFormationId(String formationId) {
        return videoRepository.findByFormationId(formationId);
    }
}
