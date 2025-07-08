package correcao.enem.service;

import correcao.enem.exceptions.ParsingTextFromPdfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ExtractorPdf {
    private static final Pattern EXAM_CORRECT_ANSWERS_PATTERN = Pattern.compile("(?i)^\\d+\\s+(A|B|C|D|E|Anulado)$");

    public String extractContentFromPdf(MultipartFile multipartFile) {
        try (PDDocument document = PDDocument.load(multipartFile.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception ex) {
            log.error("Error parsing PDF", ex);
            throw new ParsingTextFromPdfException("Error while trying to parse text from PDF");
        }
    }

    public Map<Integer, String> extractCorrectAnswersFromPdfText(String text) {
        Map<Integer, String> answers = new LinkedHashMap<>();
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (EXAM_CORRECT_ANSWERS_PATTERN.matcher(line).matches()) {
                String[] parts = line.split("\\s+");

                int questionNumber = Integer.parseInt(parts[0]);
                String answer = parts[1];
                answers.put(questionNumber, answer);
            }
        }
        return answers;
    }
}
