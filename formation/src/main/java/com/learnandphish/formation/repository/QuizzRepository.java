package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizzRepository extends JpaRepository<Quizz, Long> {
    Iterable<Quizz> findByFormationId(Long formationId);
}
