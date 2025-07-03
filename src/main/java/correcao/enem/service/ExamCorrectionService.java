package correcao.enem.service;

import correcao.enem.dto.UserAnswers;
import correcao.enem.utils.ExtractorPdf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamCorrectionService {
    private final ExtractorPdf extractorPdf;

    public String correctExam(MultipartFile file, UserAnswers data) {
        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> gabarito = extractCorrectAnswersFromText(text);

        Map<Integer, String> userAnswers = data.answers();
        validateUserAnswerValues(userAnswers);

        int correctCount = 0;
        int wrongCount = 0;

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer number = entry.getKey();
            String userAnswer = entry.getValue();

            if (gabarito.get(number) == null) {
                continue;
            }

            if (!gabarito.get(number).equalsIgnoreCase(userAnswer)) {
                wrongCount++;
            } else {
                correctCount++;
            }
        }

        return String.format("Errou: %d Acertou: %d", wrongCount, correctCount);
    }

    private Map<Integer, String> extractCorrectAnswersFromText(String text) {
        Map<Integer, String> answers = new LinkedHashMap<>();
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            // Regex para capturar: número + espaço + letras (A,B,C,D,E ou Anulado)
            if (line.matches("^\\d+\\s+(A|B|C|D|E|Anulado)$")) {
                String[] parts = line.split("\\s+");

                try {
                    int questionNumber = Integer.parseInt(parts[0]);
                    String answer = parts[1];
                    answers.put(questionNumber, answer);
                } catch (NumberFormatException e) {
                    // ignora linhas que não começam com número
                }
            }
        }
        return answers;
    }

    private void validateUserAnswerValues(Map<Integer, String> answers) {
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            String value = entry.getValue();
            if (!value.matches("(?i)[a-e]")) {
                throw new IllegalArgumentException("Resposta inválida na questão " + entry.getKey() + ": " + value);
            }
        }
    }
}
