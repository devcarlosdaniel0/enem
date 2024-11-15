package correcao.enem.service;

import correcao.enem.entity.GabaritoAzul;
import correcao.enem.repository.GabaritoAzulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GabaritoService {
    private final GabaritoAzulRepository gabaritoAzulRepository;

    public List<GabaritoAzul> getNumero() {
        return gabaritoAzulRepository.findAll();
    }
}
