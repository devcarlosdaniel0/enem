package correcao.enem.service;

import correcao.enem.dto.UserAnswers;
import correcao.enem.utils.ExtractorPdf;
import correcao.enem.utils.ValidateJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ExamCorrectionService {
    private final ExtractorPdf extractorPdf;
    private final ValidateJson validateJson;

    public String correctExam(MultipartFile file, String data) {
        UserAnswers userAnswers = validateJson.validateJsonFromString(data);
        String text = extractorPdf.extractContentFromPdf(file);

        validateIfAwnserItsPossible(userAnswers);

        return userAnswers.answers().values().toString();
    }

    private void validateIfAwnserItsPossible(UserAnswers userAnswers) {
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
}
