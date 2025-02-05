package com.learnandphish.formation.repository;

import com.learnandphish.formation.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
    List<UserBadge> findAllByUser_email(String email);
    boolean existsByUser_emailAndBadge(String email, Integer badgeId);
}
