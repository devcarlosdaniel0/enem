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

        return userAnswers.answers().values().toString();
    }
}
