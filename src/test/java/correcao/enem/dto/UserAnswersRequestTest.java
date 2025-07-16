package correcao.enem.dto;

import correcao.enem.enums.LanguageOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserAnswersRequestTest {
    @Test
    @DisplayName("Should trim and uppercase when successfully")
    void shouldTrimAndUppercaseWhenSuccessfully() {
        // Arrange
        Map<Integer, String> answers = new HashMap<>(Map.of(
                1, "  a ",
                2, " B",
                3, "c",
                4, "E"));

        // Act
        UserAnswersRequest userAnswersRequest = new UserAnswersRequest(LanguageOption.INGLES, answers);

        // Assert
        assertEquals("A", userAnswersRequest.answers().get(1));
        assertEquals("B", userAnswersRequest.answers().get(2));
        assertEquals("C", userAnswersRequest.answers().get(3));
        assertEquals("E", userAnswersRequest.answers().get(4));
    }

    @Test
    @DisplayName("Should RegEx not matches when answer is different from A-E")
    void shouldRegExNotMatchesWhenAnswerIsDifferentFromAE() {
        // Arrange
        Map<Integer, String> answers = new HashMap<>(Map.of(
                1, "F",
                2, "ab",
                3, "A B",
                4, "1"));

        Pattern regEx = Pattern.compile("(?i)[a-e]");

        // Act
        UserAnswersRequest userAnswersRequest = new UserAnswersRequest(LanguageOption.INGLES, answers);

        // Assert
        assertFalse(regEx.matcher(userAnswersRequest.answers().get(1)).matches());
        assertFalse(regEx.matcher(userAnswersRequest.answers().get(2)).matches());
        assertFalse(regEx.matcher(userAnswersRequest.answers().get(3)).matches());
        assertFalse(regEx.matcher(userAnswersRequest.answers().get(4)).matches());
    }
}