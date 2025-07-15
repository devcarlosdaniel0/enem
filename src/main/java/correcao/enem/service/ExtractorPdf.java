package correcao.enem.service;

import correcao.enem.enums.LanguageOption;
import correcao.enem.exceptions.ParsingTextFromPdfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExtractorPdf {
    private static final Pattern EXAM_CORRECT_ANSWERS_PATTERN =
            Pattern.compile("(?i)^\\d+\\s+([a-e]|Anulado)(\\s+([a-e]|Anulado))?(\\s+.*)?$");

    public String extractContentFromPdf(MultipartFile multipartFile) {
        try (PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception ex) {
            log.error("Error parsing PDF", ex);
            throw new ParsingTextFromPdfException("Error while trying to parse text from PDF");
        }
    }

    public Map<Integer, String> extractCorrectAnswersFromPdfText(String text, LanguageOption language) {
        LinkedHashMap<Integer, String> answerKey = Arrays.stream(text.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> EXAM_CORRECT_ANSWERS_PATTERN.matcher(line).matches())
                .map(line -> line.split("\\s+"))
                .collect(Collectors.toMap(
                        parts -> Integer.parseInt(parts[0]),
                        parts -> (parts.length == 3 &&
                                LanguageOption.ESPANHOL.equals(language) &&
                                Integer.parseInt(parts[0]) <= 5
                                ? parts[2]
                                : parts[1]),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));

        return answerKey;
    }
}
