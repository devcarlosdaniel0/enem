package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.exceptions.QuestionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExamGrader {
    private static final String CANCELED_ANSWER = "Anulado";

    public ResultResponse grade(Map<Integer, String> userAnswers, Map<Integer, String> answerKey) {
        int correctCount = 0;
        int wrongCount = 0;

        Map<Integer, String> wrongAnswers = new LinkedHashMap<>();
        Map<Integer, String> expectedAnswers = new LinkedHashMap<>();
        Map<Integer, String> cancelledQuestions = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer questionNumber = entry.getKey();
            String userAnswer = entry.getValue();

            String correctAnswer = answerKey.get(questionNumber);

            if (correctAnswer == null) {
                throw new QuestionNotFoundException(
                        String.format("The question with that number could not be found: %s", entry));
            }

            if (isCanceled(correctAnswer)) {
                cancelledQuestions.put(questionNumber, correctAnswer);
                continue;
            }

            if (isCorrect(userAnswer, correctAnswer)) {
                correctCount++;
            } else {
                wrongCount++;
                wrongAnswers.put(questionNumber, userAnswer);
                expectedAnswers.put(questionNumber, correctAnswer);
            }
        }

        int totalCanceled = cancelledQuestions.size();
        int totalQuestions = answerKey.size() - totalCanceled;
        int totalAnswered = userAnswers.size();

        return buildResultResponse(correctCount, wrongCount, totalAnswered, totalQuestions, totalCanceled, wrongAnswers, expectedAnswers, cancelledQuestions);
    }

    private boolean isCanceled(String correctAnswer) {
        return correctAnswer.equalsIgnoreCase(CANCELED_ANSWER);
    }

    private boolean isCorrect(String userAnswer, String correctAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    private ResultResponse buildResultResponse(int correctCount, int wrongCount, int totalAnswered, int totalQuestions, int totalCanceled, Map<Integer, String> wrongAnswers, Map<Integer, String> expectedAnswers, Map<Integer, String> cancelledQuestions) {
        return ResultResponse.builder()
                .correctCount(correctCount)
                .wrongCount(wrongCount)
                .totalAnswered(totalAnswered)
                .totalQuestions(totalQuestions)
                .totalCanceled(totalCanceled)
                .wrongAnswers(wrongAnswers)
                .expectedAnswers(expectedAnswers)
                .cancelledQuestions(cancelledQuestions)
                .build();
    }
}
