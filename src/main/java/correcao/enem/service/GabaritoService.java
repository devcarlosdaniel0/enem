package correcao.enem.service;

import correcao.enem.dto.AnswerRequest;
import correcao.enem.entity.*;
import correcao.enem.repository.GabaritoAmareloRepository;
import correcao.enem.repository.GabaritoAzulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GabaritoService {
    private final GabaritoAzulRepository gabaritoAzulRepository;
    private final GabaritoAmareloRepository gabaritoAmareloRepository;

    public String corrigir(AnswerRequest answerRequest) {
        TestColor testColor = answerRequest.getTestColor();
        Map<Integer, Character> userAnswers = answerRequest.getAnswers();
        Language language = answerRequest.getLanguage();

        List<GabaritoBase> questions = new ArrayList<>();
        List<GabaritoBase> generalQuestions = new ArrayList<>();

        switch (testColor) {
            case AZUL:
                questions.addAll(gabaritoAzulRepository.findByLanguage(language));
                generalQuestions.addAll(gabaritoAzulRepository.findByLanguageIsNull());
                break;
            case AMARELA:
                questions.addAll(gabaritoAmareloRepository.findByLanguage(language));
                generalQuestions.addAll(gabaritoAmareloRepository.findByLanguageIsNull());
                break;
        }

        questions.addAll(generalQuestions);

        int correct = 0;
        int totalQuestions = questions.size();

        for (GabaritoBase gabaritoBase : questions) {
            Character userAnswer = userAnswers.get(gabaritoBase.getQuestionNumber());

            if (userAnswer != null && userAnswer.toString().equalsIgnoreCase(gabaritoBase.getCorrectAnswer().toString())) {
                correct++;
            }
        }

        return result(correct, totalQuestions);
    }

    public String result(int corret, int totalQuestions) {
        return String.format("Você acertou %d questões de %d ", corret, totalQuestions);
    }
}