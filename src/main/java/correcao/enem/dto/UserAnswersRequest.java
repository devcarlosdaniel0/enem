package correcao.enem.dto;

import correcao.enem.enums.LanguageOption;
import correcao.enem.exceptions.InvalidYearException;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Map;

public record UserAnswersRequest(LanguageOption languageOption,
                                 Integer manualExamYear,
                                 @NotBlank Map<@Min(value = 1) @Max(value = 180) Integer,
                                         @Pattern(regexp = "(?i)[a-e]") String> answers) {

    public UserAnswersRequest {
        validateManualExamYear(manualExamYear);

        answers.replaceAll((key, value) -> value.trim().toUpperCase());
    }

    public void validateManualExamYear(Integer manualExamYear) {
        if (manualExamYear != null && manualExamYear > LocalDateTime.now().getYear() + 1) {
            throw new InvalidYearException("The exam year is greater than actual year.");
        }
    }
}