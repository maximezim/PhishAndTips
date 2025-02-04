package com.learnandphish.formation.service;

import com.learnandphish.formation.model.Video;
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

    // Get a video by id
    public Video getVideoById(Integer id) {
        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
    }

    // Get all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}
