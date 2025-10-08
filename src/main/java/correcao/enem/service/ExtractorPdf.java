package correcao.enem.service;

import correcao.enem.enums.LanguageOption;
import correcao.enem.exceptions.ExamYearNotFoundException;
import correcao.enem.exceptions.InvalidYearException;
import correcao.enem.exceptions.ParsingTextFromPdfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExtractorPdf {
    private static final Pattern EXAM_LINE_PATTERN_DIGIT_AND_ANSWER = Pattern.compile("^\\d+\\s+.*$");
    private static final Pattern EXAM_LINE_PATTERN_ONE_DIGIT_ONLY = Pattern.compile("^\\d+$");
    public static final Pattern EXAM_AE = Pattern.compile("(?i)([a-e])");
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(20\\d{2})\\b");
    public static final String CANCELED_ANSWER = "Anulado";

    public String extractContentFromPdf(MultipartFile multipartFile) {
        try (PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception ex) {
            log.error("Error parsing PDF", ex);
            throw new ParsingTextFromPdfException("Error while trying to parse text from PDF");
        }
    }

    public Map<Integer, String> extractCorrectAnswersFromPdfText(String text, LanguageOption languageOption) {
        int examYear = extractExamYearFromText(text);
        boolean isOldYear = checkIfOldYear(examYear);

        return Arrays.stream(text.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> EXAM_LINE_PATTERN_DIGIT_AND_ANSWER.matcher(line).matches() || EXAM_LINE_PATTERN_ONE_DIGIT_ONLY.matcher(line).matches())
                .collect(Collectors.toMap(
                        line -> Integer.parseInt(line.replaceAll(" .*$", "")),
                        line -> {
                            String[] parts = line.split("\\s+");

                            int questionNumber = Integer.parseInt(parts[0]);
                            String firstOp = parts.length >= 2 ? parts[1] : "";
                            String secondOp = parts.length >= 3 ? parts[2] : "";

                            if (parts.length == 1) {
                                return CANCELED_ANSWER;
                            }

                            if (parts.length == 3 &&
                                    LanguageOption.ESPANHOL.equals(languageOption) &&
                                    (
                                            (isOldYear && questionNumber >= 91 && questionNumber <= 95) ||
                                            (!isOldYear && questionNumber >= 1 && questionNumber <= 5)
                                    ) &&
                                    EXAM_AE.matcher(secondOp).matches())
                                    return secondOp;
                            else {
                                return firstOp;
                            }
                        },
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }

    private int extractExamYearFromText(String text) {
        Matcher matcher = YEAR_PATTERN.matcher(text);

        if (!matcher.find()) {
            throw new ExamYearNotFoundException("Error while finding exam year");
        }

        return Integer.parseInt(matcher.group(0));
    }

    private boolean checkIfOldYear(int examYear) {
        if (examYear < 2015) {
            throw new InvalidYearException("The correction of exams goes to 2015 to actual year.");
        }

        return examYear >= 2015 && examYear <= 2016;
    }
}
