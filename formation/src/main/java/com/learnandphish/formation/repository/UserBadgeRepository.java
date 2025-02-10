package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
    List<UserBadge> findAllByUserEmail(String email);
    boolean existsByUserEmailAndBadgeId(String email, Long badgeId);
}
