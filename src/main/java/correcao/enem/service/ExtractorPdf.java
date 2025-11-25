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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExtractorPdf {
    public static final Pattern ANSWER_PATTERN = Pattern.compile("(?i)^[a-e]$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}");
    public static final String CANCELED_ANSWER = "Anulado";
    public static final String START_WITH_CANCELLED_ANSWER = "ANULAD";

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

        Map<Integer, String> answers = new TreeMap<>();

        LinkedList<String> tokens = Arrays.stream(text.split("\\s+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedList::new));

        while (!tokens.isEmpty()) {
            String token = tokens.poll();

            if (isQuestionNumber(token)) {
                int questionNumber = Integer.parseInt(token);

                if (questionNumber < 1 || questionNumber > 180) continue;

                List<String> candidates = extractAnswerCandidates(tokens);

                if (!candidates.isEmpty()) {
                    String finalAnswer = resolveAnswer(questionNumber, candidates, languageOption, isOldYear);

                    answers.put(questionNumber, finalAnswer);
                }
            }
        }

        return answers;
    }

    /**
     * Olha para frente na fila e captura sequências de respostas (A, B...) ou Anulado.
     * Remove da fila os tokens que forem consumidos como candidatos.
     */
    private List<String> extractAnswerCandidates(LinkedList<String> tokens) {
        List<String> candidates = new ArrayList<>();

        String first = tokens.peek();

        if (first == null) return Collections.emptyList();

        if (first.toUpperCase().startsWith(START_WITH_CANCELLED_ANSWER) || CANCELED_ANSWER.equalsIgnoreCase(first)) {
            tokens.poll();
            return List.of(CANCELED_ANSWER);
        }

        while (tokens.peek() != null && ANSWER_PATTERN.matcher(tokens.peek()).matches()) {
            candidates.add(tokens.poll());
        }

        return candidates;
    }

    /**
     * Lógica de Negócio Pura: Dado um cenário (ex: Questão 2, Candidatos [A, B]), qual a resposta?
     */
    private String resolveAnswer(int questionNumber, List<String> candidates, LanguageOption languageOption, boolean isOldYear) {
        if (candidates.get(0).equals(CANCELED_ANSWER)) {
            return CANCELED_ANSWER;
        }

        if (candidates.size() == 2 && shouldSelectSecondOption(questionNumber, languageOption, isOldYear)) {
            return candidates.get(1);
        }

        return candidates.get(0);
    }

    private boolean shouldSelectSecondOption(int questionNumber, LanguageOption languageOption, boolean isOldYear) {
        if (!LanguageOption.ESPANHOL.equals(languageOption)) {
            return false;
        }

        if (isOldYear) {
            return questionNumber >= 91 && questionNumber <= 95;
        } else {
            return questionNumber >= 1 && questionNumber <= 5;
        }
    }

    private boolean isQuestionNumber(String token) {
        return token.matches("^\\d+$");
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
