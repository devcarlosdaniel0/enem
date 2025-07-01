package correcao.enem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GabaritoRequestDTO (@NotNull int ano, @NotNull int dia, @NotBlank String cor) {
}
