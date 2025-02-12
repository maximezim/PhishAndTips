package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.UserVideoId;
import com.learnandphish.formation.model.UserVideoWatched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVideoWatchedRepository extends JpaRepository<UserVideoWatched, UserVideoId> {
    List<UserVideoWatched> findByUserVideoIdUserEmailAndIsWatchedTrue(String email);
    void deleteByUserVideoIdVideoId(Integer videoId);
}
