package com.learnandphish.formation.service;


import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.model.UserQuizScore;
import com.learnandphish.formation.model.UserQuizId;
import com.learnandphish.formation.repository.QuizRepository;
import com.learnandphish.formation.repository.UserQuizScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserQuizScoreRepository quizScoreRepository;

    // Get a quiz by id
    public Quiz getQuizById(Integer id) {
        return quizRepository.findById(id).orElse(null);
    }

    // Get all quizzes
    public Iterable<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }

    // Update a quiz
    public Quiz updateQuiz(Integer quizId, Quiz quiz) {
        Quiz updatedQuiz = quizRepository.findById(quizId).orElse(null);
        if (updatedQuiz != null) {
            updatedQuiz.setJson(quiz.getJson());
            quizRepository.save(updatedQuiz);
        }
        return updatedQuiz;
    }

    // Save user score
    public void saveUserScore(String userEmail, Integer quizId, Float score) {
        UserQuizScore userQuizScore = quizScoreRepository.findById(new UserQuizId(userEmail, quizId)).orElse(new UserQuizScore());
        if (userQuizScore.getUserQuizId() == null || userQuizScore.getUserQuizId().getUserEmail() == null) {
            userQuizScore.setUserQuizId(new UserQuizId(userEmail, quizId));
        }
        userQuizScore.setScore(score);
        quizScoreRepository.save(userQuizScore);
    }

    // Get user scores for all quizzes
    public Iterable<UserQuizScore> getUserScores(String userEmail) {
        return quizScoreRepository.findAllByUserQuizIdUserEmail(userEmail);
    }


    // Get user score for a quiz
    public Float getUserScoreForQuiz(String user_email, Integer quiz_id){
        UserQuizScore userQuizScore = quizScoreRepository.findById(new UserQuizId(user_email, quiz_id)).orElse(null);
        return userQuizScore != null ? userQuizScore.getScore() : null;
    }

}
