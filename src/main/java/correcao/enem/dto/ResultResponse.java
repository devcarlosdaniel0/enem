package correcao.enem.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ResultResponse(Integer correctCount,
                             Integer wrongCount,
                             Integer totalAnswered,
                             Integer totalQuestions,
                             Integer totalCanceled,
                             Map<Integer, String> wrongAnswers,
                             Map<Integer, String> expectedAnswers,
                             Map<Integer, String> cancelledQuestions
                             ) {
}
