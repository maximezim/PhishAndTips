package com.learnandphish.formation.service;


import com.learnandphish.formation.dto.QuizzRequest;
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

    public Quizz createQuizz(QuizzRequest quizzRequest) {
        Quizz quizz = new Quizz(quizzRequest.id(),
                quizzRequest.formationId(),
                quizzRequest.json());
        log.info("Quizz {} created successfully", quizz);
        return quizzRepository.save(quizz);
    }

    public Quizz getQuizzById(Long id) {
        return quizzRepository.findById(id).orElseThrow(() -> new RuntimeException("Quizz not found"));
    }

    public Iterable<Quizz> getQuizzByFormationId(Long formationId) {
        return quizzRepository.findByFormationId(formationId);
    }

    public void deleteQuizz(Long id) {
        quizzRepository.deleteById(id);
    }

    public Quizz updateQuizz(QuizzRequest quizzRequest) {
        Quizz quizz = quizzRepository.findById(quizzRequest.id())
                .orElseThrow(() -> new RuntimeException("Quizz not found"));
        quizz.setFormationId(quizzRequest.formationId());
        quizz.setJson(quizzRequest.json());
        return quizzRepository.save(quizz);
    }

    public Iterable<Quizz> getAllQuizzs() {
        return quizzRepository.findAll();
    }
}
