package correcao.enem.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ResultResponse(Integer correctCount,
                             Integer wrongCount,
                             Integer totalQuestions,
                             Integer totalCanceled
//                             Map<Integer, String> wrongAnswers,
//                             Map<Integer, String> correctAnswers
                             ) {
}
