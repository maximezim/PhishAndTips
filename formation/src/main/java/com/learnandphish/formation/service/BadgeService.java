package com.learnandphish.formation.service;

import com.learnandphish.formation.model.Badge;
import com.learnandphish.formation.model.UserBadge;
import com.learnandphish.formation.repository.BadgeRepository;
import com.learnandphish.formation.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public Badge getBadgeById(Integer id) {
        return badgeRepository.findById(id).orElseThrow(() -> new RuntimeException("Badge not found"));
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public Badge createBadge(Badge badge) {
        return badgeRepository.save(badge);
    }

    public Badge claimBadge(Integer badgeId, String email) {
        Badge badge = getBadgeById(badgeId);
        if (badge == null) {
            throw new RuntimeException("Badge not found");
        }
        if (userBadgeRepository.existsByUser_emailAndBadge(email, badgeId)) {
            throw new RuntimeException("Badge already claimed by user");
        }
        UserBadge userBadge = new UserBadge();
        userBadge.setUser_email(email);
        userBadge.setBadge(badge);
        userBadgeRepository.save(userBadge);
        return badge;
    }

    public List<Badge> getBadgesByUser(String email) {
        return userBadgeRepository.findAllByUser_email(email).stream()
            .map(UserBadge::getBadge)
            .collect(Collectors.toList());
    }
}
