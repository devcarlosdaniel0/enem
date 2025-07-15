package correcao.enem.dto;

import correcao.enem.enums.LanguageOption;
import jakarta.validation.constraints.*;

import java.util.Map;

public record UserAnswersRequest(LanguageOption languageOption,
                                 @NotEmpty
                                 Map<@NotNull @Max(value = 180) @Min(value = 1) Integer,
                @NotBlank @Pattern(regexp = "(?i)[a-e]", message = "Answers must be only [A,B,C,D,E]") String> answers) {

    public UserAnswersRequest {
        answers.replaceAll((key, value) -> value != null ? value.trim().toUpperCase() : null);
    }
}