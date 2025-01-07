package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Long> {
}
