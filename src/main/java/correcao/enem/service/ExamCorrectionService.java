package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamCorrectionService {
    private final ExtractorPdf extractorPdf;
    private final ExamGrader examGrader;

    public ResultResponse correctExam(MultipartFile file, UserAnswersRequest userAnswersRequest) {
        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> answerKey = extractorPdf.extractCorrectAnswersFromPdfText(text);

        Map<Integer, String> userAnswers = userAnswersRequest.answers();

        return examGrader.grade(userAnswers, answerKey);
    }

}
