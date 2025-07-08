package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.exceptions.QuestionNumberNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExamGrader {
    private static final String CANCELED_ANSWER = "Anulado";

    public ResultResponse grade(Map<Integer, String> userAnswers, Map<Integer, String> answerKey) {
        int correctCount = 0;
        int wrongCount = 0;
        int canceledCount = countCanceledQuestions(answerKey);
        int totalQuestions = answerKey.size() - canceledCount;
        Map<Integer, String> wrongAnswers = new LinkedHashMap<>();
        Map<Integer, String> correctedAnswers = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer questionNumber = entry.getKey();
            String userAnswer = entry.getValue();

            String correctAnswer = answerKey.get(questionNumber);

            if (correctAnswer == null) {
                throw new QuestionNumberNotFoundException(
                        String.format("The question with that number could not be found: %s", entry));
            }

            if (correctAnswer.equalsIgnoreCase(CANCELED_ANSWER)) {
                continue;
            }

            if (correctAnswer.equalsIgnoreCase(userAnswer)) {
                correctCount++;
            } else {
                wrongCount++;
                wrongAnswers.put(questionNumber, userAnswer);
                correctedAnswers.put(questionNumber, correctAnswer);
            }
        }

        return buildResultResponse(correctCount, wrongCount, totalQuestions, canceledCount, wrongAnswers, correctedAnswers);
    }

    private ResultResponse buildResultResponse(int correctCount, int wrongCount, int totalQuestions, int totalCanceled, Map<Integer, String> wrongAnswers, Map<Integer, String> correctedAnswers) {
        return ResultResponse.builder()
                .correctCount(correctCount)
                .wrongCount(wrongCount)
                .totalQuestions(totalQuestions)
                .totalCanceled(totalCanceled)
                .wrongAnswers(wrongAnswers)
                .correctedAnswers(correctedAnswers)
                .build();
    }

    private int countCanceledQuestions(Map<Integer, String> answerKey) {
        return (int) answerKey.values().stream()
                .filter(answer -> answer.equalsIgnoreCase(CANCELED_ANSWER))
                .count();
    }
}
