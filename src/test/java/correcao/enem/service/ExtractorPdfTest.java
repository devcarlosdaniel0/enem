package correcao.enem.service;

import correcao.enem.enums.LanguageOption;
import correcao.enem.exceptions.ExamYearNotFoundException;
import correcao.enem.exceptions.InvalidYearException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExtractorPdfTest {

    @InjectMocks
    private ExtractorPdf extractorPdf;

    @Nested
    class ExtractCorrectAnswersFromPdfText {
        @Test
        @DisplayName("Should get the second answer when language option is spanish in range of 5 questions when later than 2016")
        void shouldGetTheSecondAnswerWhenLanguageOptionIsSpanishInRangeOf5QuestionsWhenLaterThan2016() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 A B
                    2 C D
                    3 B A
                    4 D C
                    5 A E
                    6 C
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("B", result.get(1));
            assertEquals("D", result.get(2));
            assertEquals("A", result.get(3));
            assertEquals("C", result.get(4));
            assertEquals("E", result.get(5));
            assertEquals("C", result.get(6));
        }

        @Test
        @DisplayName("Should get the second answer when language option is spanish in range of 91 to 95 when 2015 to 2016")
        void shouldGetTheSecondAnswerWhenLanguageOptionIsSpanishInRangeOf91to95When2015to2016() {
            // Arrange
            String text = """
                    ENEM 2015
                    91 A B
                    92 C D
                    93 B A
                    94 D C
                    95 A E
                    96 C
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("B", result.get(91));
            assertEquals("D", result.get(92));
            assertEquals("A", result.get(93));
            assertEquals("C", result.get(94));
            assertEquals("E", result.get(95));
            assertEquals("C", result.get(96));
        }

        @Test
        @DisplayName("Should throw exception when exam year is lower than 2015")
        void shouldThrowExceptionWhenExamYearIsLowerThan2015() {
            // Arrange
            String text = """
                    ENEM 2010
                    91 A B
                    92 C D
                    93 B A
                    94 D C
                    95 A E
                    96 C
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            assertThrows(InvalidYearException.class, () -> extractorPdf.extractCorrectAnswersFromPdfText(text, spanish));

        }

        @Test
        @DisplayName("Should throw exception when exam year is not found")
        void shouldThrowExceptionWhenExamYearIsNotFound() {
            // Arrange
            String text = """
                    91 A B
                    92 C D
                    93 B A
                    94 D C
                    95 A E
                    96 C
                    """;

            LanguageOption english = LanguageOption.INGLES;

            // Act
            assertThrows(ExamYearNotFoundException.class, () -> extractorPdf.extractCorrectAnswersFromPdfText(text, english));

        }

        @Test
        @DisplayName("Should get the first answer when language option is english")
        void shouldGetTheFirstAnswerWhenLanguageOptionIsEnglish() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 A B
                    2 C D
                    3 B A
                    4 D C
                    5 A E
                    6 C
                    """;

            LanguageOption english = LanguageOption.INGLES;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, english);

            // Assert
            assertEquals("A", result.get(1));
            assertEquals("C", result.get(2));
            assertEquals("B", result.get(3));
            assertEquals("D", result.get(4));
            assertEquals("A", result.get(5));
            assertEquals("C", result.get(6));
        }

        @Test
        @DisplayName("Should get the first answer even if spanish when question number is greater than 5 when later than 2016")
        void shouldGetTheFirstAnswerEvenIfSpanishWhenQuestionNumberIsGreaterThan5WhenLaterThan2016() {
            // Arrange
            String text = """
                    ENEM 2024
                    5 D E
                    6 A D
                    7 B E
                    8 C A
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("E", result.get(5));

            assertEquals("A", result.get(6));
            assertEquals("B", result.get(7));
            assertEquals("C", result.get(8));
        }

        @Test
        @DisplayName("Should get the first answer even if spanish when question number is greater than 95 when 2015 to 2016")
        void shouldGetTheFirstAnswerEvenIfSpanishWhenQuestionNumberIsGreaterThan95When2015To2016() {
            // Arrange
            String text = """
                    ENEM 2016
                    95 D E
                    96 A D
                    97 B E
                    98 C A
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("E", result.get(95));

            assertEquals("A", result.get(96));
            assertEquals("B", result.get(97));
            assertEquals("C", result.get(98));
        }

        @Test
        @DisplayName("Should get the first answer when second answer does not matches with RegEx")
        void shouldGetTheFirstAnswerWhenSecondAnswerDoesNotMatchesWithRegEx() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 A Portugues
                    2 B CC
                    3 C B123
                    4 D E
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertFalse(ExtractorPdf.ANSWER_PATTERN.matcher("Portugues").matches());
            assertFalse(ExtractorPdf.ANSWER_PATTERN.matcher("CC").matches());
            assertFalse(ExtractorPdf.ANSWER_PATTERN.matcher("B123").matches());

            assertEquals("A", result.get(1));
            assertEquals("B", result.get(2));
            assertEquals("C", result.get(3));

            assertEquals("E", result.get(4));
        }

        @Test
        @DisplayName("Should get the first answer when language option is null")
        void shouldGetTheFirstAnswerWhenLanguageOptionIsNull() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 A C
                    2 B D
                    3 C E
                    """;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, null);

            // Assert
            assertEquals("A", result.get(1));
            assertEquals("B", result.get(2));
            assertEquals("C", result.get(3));
        }

        @Test
        @DisplayName("Should get the first answer when there is more than 2 answers for the same question even if is spanish")
        void shouldGetTheFirstAnswerWhenThereIsMoreThan2AnswersForTheSameQuestionEvenIfIsSpanish() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 A B C portugues
                    2 D E A matematica
                    3 B C D fisica
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("A", result.get(1));
            assertEquals("D", result.get(2));
            assertEquals("B", result.get(3));
        }

        @Test
        @DisplayName("Should get the first answer option when cancelled")
        void shouldGetTheFirstAnswerOptionWhenCancelled() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 Anulada b
                    2 anulado c 
                    3 Anulado portugues
                    """;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, null);

            // Assert
            assertEquals(ExtractorPdf.CANCELED_ANSWER, result.get(1));
            assertEquals(ExtractorPdf.CANCELED_ANSWER, result.get(2));
            assertEquals(ExtractorPdf.CANCELED_ANSWER, result.get(3));
        }

        @Test
        @DisplayName("Should transforms nulls in cancelled")
        void shouldTransformsNullsInCancelled() {
            // Arrange
            String text = """
                    ENEM 2024
                    1 
                    2
                    """;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, null);

            // Assert
            assertEquals(ExtractorPdf.CANCELED_ANSWER, result.get(1));
            assertEquals(ExtractorPdf.CANCELED_ANSWER, result.get(2));
        }
    }
}