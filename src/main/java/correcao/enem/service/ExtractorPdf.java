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
    private static final Pattern EXAM_LINE_PATTERN_DIGIT_AND_ANSWER = Pattern.compile("^\\d+\\s.*$");
    private static final Pattern EXAM_LINE_PATTERN_ONE_DIGIT_ONLY = Pattern.compile("^\\d+$");
    private static final String CANCELED_ANSWER = "Anulado";

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
        return Arrays.stream(text.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> EXAM_LINE_PATTERN_DIGIT_AND_ANSWER.matcher(line).matches() || EXAM_LINE_PATTERN_ONE_DIGIT_ONLY.matcher(line).matches())
                .collect(Collectors.toMap(
                        line -> Integer.parseInt(line.replaceAll(" .*$", "")),
                        line -> {
                            String[] parts = line.split("\\s+");
                            if (parts.length == 1) {
                                return CANCELED_ANSWER;
                            } else if (parts.length == 2) {
                                return parts[1];
                            } else if (parts.length == 3) {
                                if (LanguageOption.ESPANHOL.equals(languageOption) && Integer.parseInt(parts[0]) <= 5) {
                                    return parts[2];
                                }
                                return parts[1];
                            }
                            return "Invalid";
                        },
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }
}
