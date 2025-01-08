package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    //Iterable<Video> findByFormationId(String formationId);
}
