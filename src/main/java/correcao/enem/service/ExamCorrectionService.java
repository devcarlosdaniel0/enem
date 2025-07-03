package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import correcao.enem.utils.ExtractorPdf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamCorrectionService {
    private final ExtractorPdf extractorPdf;

    public ResultResponse correctExam(MultipartFile file, UserAnswersRequest userAnswersRequest) {
        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> gabarito = extractCorrectAnswersFromText(text);

        Map<Integer, String> userAnswers = userAnswersRequest.answers();
        validateUserAnswerValues(userAnswers);

        int correctCount = 0;
        int wrongCount = 0;
        int totalCanceled = countCanceledQuestions(gabarito);
        int totalQuestions = gabarito.size() - totalCanceled;

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer number = entry.getKey();
            String userAnswer = entry.getValue();

            if (gabarito.get(number) == null) {
                continue;
            }

            if (gabarito.get(number).equalsIgnoreCase("Anulado")) {
                continue;
            }

            if (gabarito.get(number).equalsIgnoreCase(userAnswer)) {
                correctCount++;
            } else {
                wrongCount++;
            }
        }

        return ResultResponse.builder()
                .correctCount(correctCount)
                .wrongCount(wrongCount)
                .totalQuestions(totalQuestions)
                .totalCanceled(totalCanceled)
                .build();
    }

    private Map<Integer, String> extractCorrectAnswersFromText(String text) {
        Map<Integer, String> answers = new LinkedHashMap<>();
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            // Regex to capture: (ignore case) number + space + letters (A,B,C,D,E or Anulado)
            if (line.matches("(?i)^\\d+\\s+(A|B|C|D|E|Anulado)$")) {
                String[] parts = line.split("\\s+");

                int questionNumber = Integer.parseInt(parts[0]);
                String answer = parts[1];
                answers.put(questionNumber, answer);
            }
        }
        return answers;
    }

    private void validateUserAnswerValues(Map<Integer, String> answers) {
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            String value = entry.getValue();
            // Regex to capture: (ignore case) letters (A,B,C,D or E)
            if (!value.matches("(?i)[a-e]")) {
                throw new IllegalArgumentException("Invalid answer in question " + entry.getKey() + ": " + value);
            }
        }
    }

    private int countCanceledQuestions(Map<Integer, String> gabarito) {
        int count = 0;
        for (String answer : gabarito.values()) {
            if (answer.equalsIgnoreCase("Anulado")) {
                count++;
            }
        }
        return count;
    }
}
