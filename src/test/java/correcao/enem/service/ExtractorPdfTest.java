package correcao.enem.service;

import correcao.enem.enums.LanguageOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ExtractorPdfTest {

    @InjectMocks
    private ExtractorPdf extractorPdf;

    @Nested
    class ExtractCorrectAnswersFromPdfText {
        @Test
        @DisplayName("Should get the second answer when language option is spanish")
        void shouldGetTheSecondAnswerWhenLanguageOptionIsSpanish() {
            // Arrange
            String text = """
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
        @DisplayName("Should get the first answer when language option is english")
        void shouldGetTheFirstAnswerWhenLanguageOptionIsEnglish() {
            // Arrange
            String text = """
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
        @DisplayName("Should get the first answer even if spanish when question number is greater than 5")
        void shouldGetTheFirstAnswerEvenIfSpanishWhenQuestionNumberIsGreaterThan5() {
            // Arrange
            String text = """
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
        @DisplayName("Should get the first answer when second answer does not matches with RegEx")
        void shouldGetTheFirstAnswerWhenSecondAnswerDoesNotMatchesWithRegEx() {
            // Arrange
            String text = """
                    1 A Portugues
                    2 B CC
                    3 C B123
                    4 D E
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            Pattern regEx = Pattern.compile("(?i)([a-e]|Anulado)");

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertFalse(regEx.matcher("Portugues").matches());
            assertFalse(regEx.matcher("CC").matches());
            assertFalse(regEx.matcher("B123").matches());

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
        @DisplayName("Should get the first answer when there is more than 2 answers")
        void shouldGetTheFirstAnswerWhenThereIsMoreThan2Answers() {
            // Arrange
            String text = """
                    1 A B C
                    2 D E A 
                    3 B C D
                    """;

            LanguageOption spanish = LanguageOption.ESPANHOL;

            // Act
            Map<Integer, String> result = extractorPdf.extractCorrectAnswersFromPdfText(text, spanish);

            // Assert
            assertEquals("A", result.get(1));
            assertEquals("D", result.get(2));
            assertEquals("B", result.get(3));
        }
    }
}