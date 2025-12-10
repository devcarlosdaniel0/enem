package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import correcao.enem.enums.LanguageOption;
import correcao.enem.exceptions.FileNotPdfException;
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

    public ResultResponse correctExam(MultipartFile file, UserAnswersRequest request) {
        boolean isPdf = pdfValidator.validatePDF(file);

        if (!isPdf) {
            throw new FileNotPdfException("The file uploaded is not a PDF. Please upload a valid PDF.");
        }

        String text = extractorPdf.extractContentFromPdf(file);

        Map<Integer, String> answerKey = extractorPdf.extractCorrectAnswersFromPdfText(
                text,
                request.languageOption(),
                request.manualExamYear());

        Map<Integer, String> userAnswers = request.answers();

        return examGrader.grade(userAnswers, answerKey);
    }

}
