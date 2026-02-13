package correcao.enem.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ResultResponse(Integer correctCount,
                             Integer wrongCount,
                             Integer totalAnswered,
                             Integer totalQuestions,
                             Integer totalCanceled,
                             BigDecimal percentageCorrect,
                             Map<Integer, String> wrongAnswers,
                             Map<Integer, String> expectedAnswers,
                             Map<Integer, String> cancelledQuestions
                             ) {
}
