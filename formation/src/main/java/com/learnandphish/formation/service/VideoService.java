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

    public Video getVideoById(Integer id) {
        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    /*public Iterable<Video> getVideosByFormationId(String formationId) {
        return videoRepository.findByFormationId(formationId);
    }*/
}
