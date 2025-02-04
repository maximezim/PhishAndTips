package com.learnandphish.formation.service;


import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.model.UserQuizScore;
import com.learnandphish.formation.model.UserQuizzId;
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
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // Get all quizzes
    public Iterable<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }

    // Save user score
    public void saveUserScore(String user_email, Integer quiz_id, double score){
        UserQuizScore userQuizScore = new UserQuizScore();
        userQuizScore.setUserQuizzId(new UserQuizzId(user_email, quiz_id));
        userQuizScore.setScore(score);
        quizScoreRepository.save(userQuizScore);
    }

    // Get user scores
    public double getUserScores(String user_email){
        double score = 0;
        int count = 0;
        Iterable<UserQuizScore> userQuizScores = quizScoreRepository.findAll();
        for (UserQuizScore userQuizScore : userQuizScores) {
            if (userQuizScore.getUserQuizzId().getUser_email().equals(user_email)) {
                score += userQuizScore.getScore();
                count++;
            }
        }
        return count != 0 ? score / count : 0;
    }

    // Get user score for a quiz
    public double getUserScoreForQuiz(String user_email, Integer quiz_id){
        UserQuizScore userQuizScore = quizScoreRepository.findById(new UserQuizzId(user_email, quiz_id)).orElse(null);
        return userQuizScore != null ? userQuizScore.getScore() : 0;
    }

}
