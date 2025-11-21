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
    private final PdfValidator pdfValidator;

    public ResultResponse correctExam(MultipartFile file, UserAnswersRequest userAnswersRequest) {
        boolean isPdf = pdfValidator.validatePDF(file);

        if (!isPdf) {
            throw new IllegalArgumentException("The file uploaded is not a PDF. Please upload a valid PDF.");
        }

        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> answerKey = extractorPdf.extractCorrectAnswersFromPdfText(text, userAnswersRequest.languageOption());

        Map<Integer, String> userAnswers = userAnswersRequest.answers();

        return examGrader.grade(userAnswers, answerKey);
    }

}
