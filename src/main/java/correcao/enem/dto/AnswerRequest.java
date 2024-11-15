package correcao.enem.dto;

import correcao.enem.entity.Language;
import correcao.enem.entity.TestColor;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class AnswerRequest {
    @NotNull
    private TestColor testColor;

    @NotNull
    private Language language;

    @NotNull
    private Map<Integer, Character> answers;
}
