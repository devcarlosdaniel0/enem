package correcao.enem.dto;

import jakarta.validation.constraints.*;

import java.util.Map;

public record UserAnswersRequest(Map<@NotNull @Max(value = 180) @Min(value = 1) Integer,
        @NotBlank @Pattern(regexp = "(?i)[a-e]", message = "Answers must be only [A,B,C,D,E]") String> answers) {

    public UserAnswersRequest {
        if (answers != null) {
            answers.replaceAll((key, value) -> value != null ? value.trim().toUpperCase() : null);
        }
    }
}