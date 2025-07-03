package correcao.enem.dto;

import java.util.Map;

public record UserAnswersRequest(Map<Integer, String> answers) {
}