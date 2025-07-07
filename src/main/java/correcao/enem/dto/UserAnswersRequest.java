package correcao.enem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record UserAnswersRequest(Map<@NotNull @Max(value = 180) @Min(value = 1) Integer, @NotBlank String> answers) {
}