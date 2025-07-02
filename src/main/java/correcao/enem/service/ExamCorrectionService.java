package correcao.enem.service;

import correcao.enem.dto.UserAnswers;
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

    public String correctExam(MultipartFile file, UserAnswers data) {
        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> gabarito = extractAnswersFromText(text);

        validateAnswerValues(data.answers());

        return data.answers().toString();
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

    private void validateAnswerValues(Map<Integer, String> answers) {
        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            String value = entry.getValue();
            if (!value.matches("(?i)[a-e]")) {
                throw new IllegalArgumentException("Resposta inválida na questão " + entry.getKey() + ": " + value);
            }
        }
    }
}
