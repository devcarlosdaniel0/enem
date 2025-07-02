package correcao.enem.service;

import correcao.enem.dto.UserAnswers;
import correcao.enem.utils.ExtractorPdf;
import correcao.enem.utils.ValidateJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamCorrectionService {
    private final ExtractorPdf extractorPdf;
    private final ValidateJson validateJson;

    public String correctExam(MultipartFile file, String data) {
        UserAnswers userAnswers = validateJson.validateJsonFromString(data);
        validateIfAnswerItsPossible(userAnswers);

        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> gabarito = extractAnswersFromText(text);

        return text;
    }

    private void validateIfAnswerItsPossible(UserAnswers userAnswers) {
        for (String answer : userAnswers.answers().values()) {
            if (!isValidAnswer(answer)) {
                throw new RuntimeException("The answers must be [A,B,C,D,E] only");
            }
        }
    }

    private boolean isValidAnswer(String answer) {
        return answer.equalsIgnoreCase("A") ||
                answer.equalsIgnoreCase("B") ||
                answer.equalsIgnoreCase("C") ||
                answer.equalsIgnoreCase("D") ||
                answer.equalsIgnoreCase("E");
    }

    private Map<Integer, String> extractAnswersFromText(String text) {
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

}
