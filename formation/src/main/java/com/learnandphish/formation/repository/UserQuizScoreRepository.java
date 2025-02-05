package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.UserQuizScore;
import com.learnandphish.formation.model.UserQuizId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizScoreRepository extends JpaRepository<UserQuizScore, UserQuizId> {
    Iterable<UserQuizScore> findAllByUserQuizIdUserEmail(String userEmail);
}
