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

    public Formation getFormationById(Integer id) {
        return formationRepository.findById(id).orElseThrow(() -> new RuntimeException("Formation not found"));
    }

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

}
