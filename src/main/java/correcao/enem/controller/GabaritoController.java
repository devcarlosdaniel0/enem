package correcao.enem.controller;

import correcao.enem.dto.AnswerRequest;
import correcao.enem.service.GabaritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gabarito")
@RequiredArgsConstructor
public class GabaritoController {

    private final GabaritoService gabaritoService;

    @PostMapping("/corrigir")
    public String corrigir(@RequestBody AnswerRequest answerRequest) {
        return gabaritoService.corrigir(answerRequest);
    }
}
