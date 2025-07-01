package correcao.enem.dto;

import java.util.Map;

public record UserAnswers (Map<Integer, String> answers) {
}