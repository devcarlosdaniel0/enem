package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import correcao.enem.exceptions.QuestionNumberNotFoundException;
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

    private static final String CANCELED_ANSWER = "Anulado";

    public ResultResponse correctExam(MultipartFile file, UserAnswersRequest userAnswersRequest) {
        String text = extractorPdf.extractContentFromPdf(file);
        Map<Integer, String> gabarito = extractorPdf.extractCorrectAnswersFromPdfText(text);

        Map<Integer, String> userAnswers = userAnswersRequest.answers();

        int correctCount = 0;
        int wrongCount = 0;
        int totalCanceled = countCanceledQuestions(gabarito);
        int totalQuestions = gabarito.size() - totalCanceled;
        Map<Integer, String> wrongAnswers = new LinkedHashMap<>();
        Map<Integer, String> correctedAnswers = new LinkedHashMap<>();

        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            Integer number = entry.getKey();
            String userAnswer = entry.getValue();

            if (gabarito.get(number) == null) {
                throw new QuestionNumberNotFoundException(String.format("The question with that number could not be found: %s", entry));
            }

            if (gabarito.get(number).equalsIgnoreCase(CANCELED_ANSWER)) {
                continue;
            }

            if (gabarito.get(number).equalsIgnoreCase(userAnswer)) {
                correctCount++;
            } else {
                wrongCount++;
                wrongAnswers.put(number, userAnswer);
                correctedAnswers.put(number, gabarito.get(number));
            }
        }

        return buildResultResponse(correctCount, wrongCount, totalQuestions, totalCanceled, wrongAnswers, correctedAnswers);
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

    private int countCanceledQuestions(Map<Integer, String> gabarito) {
        int count = 0;
        for (String answer : gabarito.values()) {
            if (answer.equalsIgnoreCase(CANCELED_ANSWER)) {
                count++;
            }
        }
        return count;
    }
}
