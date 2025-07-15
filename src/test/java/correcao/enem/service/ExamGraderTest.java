package correcao.enem.service;

import correcao.enem.dto.ResultResponse;
import correcao.enem.exceptions.QuestionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ExamGraderTest {

    @InjectMocks
    private ExamGrader examGrader;

    @Nested
    class grade {
        @Test
        @DisplayName("Should grade correct and wrong answers")
        void shouldGradeCorrectAndWrongAnswers() {
            // Arrange
            Map<Integer, String> userAnswers = Map.of(
                    1, "A",
                    2, "C",
                    3, "D"
            );

            Map<Integer, String> answerKey = Map.of(
                    1, "D",
                    2, "C",
                    3, "B",
                    4, "D"
            );

            // Act
            ResultResponse result = examGrader.grade(userAnswers, answerKey);

            // Assert
            assertEquals(1, result.correctCount());
            assertEquals(2, result.wrongCount());
            assertEquals(3, result.totalAnswered());
            assertEquals(4, result.totalQuestions());
            assertEquals(0, result.totalCanceled());

            assertEquals(2, result.wrongAnswers().size());
            assertEquals(2, result.expectedAnswers().size());
            assertEquals(0, result.cancelledQuestions().size());

            assertEquals("D", result.expectedAnswers().get(1));
            assertEquals("B", result.expectedAnswers().get(3));

            assertEquals(answerKey.get(2), userAnswers.get(2));
        }

        @Test
        @DisplayName("Should handle canceled questions")
        void shouldHandleCanceledQuestions() {
            // Arrange
            Map<Integer, String> userAnswers = Map.of(
                    1, "A",
                    2, "B",
                    3, "C"
            );

            Map<Integer, String> answerKey = Map.of(
                    1, "Anulado",
                    2, "C",
                    3, "C"
            );

            // Act
            ResultResponse result = examGrader.grade(userAnswers, answerKey);

            // Assert
            assertEquals(1, result.correctCount());
            assertEquals(1, result.wrongCount());
            assertEquals(3, result.totalAnswered());
            assertEquals(2, result.totalQuestions());
            assertEquals(1, result.totalCanceled());

            assertEquals("Anulado", result.cancelledQuestions().get(1));
        }

        @Test
        @DisplayName("Should throw exception when question is not found")
        void shouldThrowExceptionWhenQuestionIsNotFound() {
            // Arrange
            Map<Integer, String> userAnswers = Map.of(
                    91, "A",
                    92, "B",
                    93, "C"
            );

            Map<Integer, String> answerKey = Map.of(
                    1, "A",
                    2, "B",
                    3, "C"
            );

            // Act & Assert
            assertThrows(QuestionNotFoundException.class, () -> examGrader.grade(userAnswers, answerKey));
        }

        @DisplayName("Should treat answers case insensitive")
        @Test
        void shouldTreatAnswersCaseInsensitive() {
            Map<Integer, String> userAnswers = Map.of(
                    1, "a",
                    2, "B"
            );
            Map<Integer, String> answerKey = Map.of(
                    1, "A",
                    2, "b"
            );

            ResultResponse result = examGrader.grade(userAnswers, answerKey);
            assertEquals(2, result.correctCount());
        }
    }
}