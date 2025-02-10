package com.learnandphish.formation.service;

import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.repository.FormationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormationService {
    private final FormationRepository formationRepository;

    // Get a formation by id
    public Formation getFormationById(Integer id) {
        return formationRepository.findById(id).orElse(null);
    }

    // Get all formations
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

}
