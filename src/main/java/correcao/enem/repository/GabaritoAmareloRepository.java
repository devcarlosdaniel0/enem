package correcao.enem.repository;

import correcao.enem.entity.GabaritoAmarelo;
import correcao.enem.entity.GabaritoAzul;
import correcao.enem.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GabaritoAmareloRepository extends JpaRepository<GabaritoAmarelo, Long> {
    List<GabaritoAmarelo> findByLanguage(Language language);

    List<GabaritoAmarelo> findByLanguageIsNull();
}
