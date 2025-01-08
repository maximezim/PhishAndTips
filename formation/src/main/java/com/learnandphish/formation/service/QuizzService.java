package com.learnandphish.formation.service;


import com.learnandphish.formation.model.Quizz;
import com.learnandphish.formation.repository.QuizzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizzService {
    private final QuizzRepository quizzRepository;

    public Quizz getQuizzById(Integer id) {
        return quizzRepository.findById(id).orElseThrow(() -> new RuntimeException("Quizz not found"));
    }

    /*public Iterable<Quizz> getQuizzByFormationId(Long formationId) {
        return quizzRepository.findByFormationId(formationId);
    }*/


    public Iterable<Quizz> getAllQuizzs() {
        return quizzRepository.findAll();
    }
}
