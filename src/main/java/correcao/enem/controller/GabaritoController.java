package correcao.enem.controller;

import correcao.enem.entity.GabaritoAzul;
import correcao.enem.service.GabaritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gabarito")
@RequiredArgsConstructor
public class GabaritoController {

    private final GabaritoService gabaritoService;

    public GabaritoAzul getAll() {
        return (GabaritoAzul) gabaritoService.getNumero();
    }
}
