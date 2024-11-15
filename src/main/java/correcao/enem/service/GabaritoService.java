package correcao.enem.service;

import correcao.enem.dto.AnswerRequest;
import correcao.enem.entity.GabaritoAzul;
import correcao.enem.entity.Language;
import correcao.enem.repository.GabaritoAzulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GabaritoService {
    private final GabaritoAzulRepository gabaritoAzulRepository;

    public String corrigir(AnswerRequest answerRequest) {
        Map<Integer, Character> userAnswers = answerRequest.getAnswers();
        Language language = answerRequest.getLanguage();

        List<GabaritoAzul> questions = gabaritoAzulRepository.findByLanguage(language);

        List<GabaritoAzul> generalQuestions = gabaritoAzulRepository.findByLanguageIsNull();

        questions.addAll(generalQuestions);

        int correct = 0;
        int totalQuestions = questions.size();

        for (GabaritoAzul gabaritoAzul : questions) {
            Character userAnswer = userAnswers.get(gabaritoAzul.getQuestionNumber());

            if (userAnswer != null && userAnswer.toString().equalsIgnoreCase(gabaritoAzul.getCorrectAnswer().toString())) {
                correct++;
            }
        }

        return result(correct, totalQuestions);
    }

    public String result(int corret, int totalQuestions) {
        return String.format("Você acertou %d questões de %d ", corret, totalQuestions);
    }
}