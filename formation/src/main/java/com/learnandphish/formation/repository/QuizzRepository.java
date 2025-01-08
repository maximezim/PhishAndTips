package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, Integer> {
}
