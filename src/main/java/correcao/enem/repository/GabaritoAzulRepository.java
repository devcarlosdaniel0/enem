package correcao.enem.repository;

import correcao.enem.entity.GabaritoAzul;
import correcao.enem.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GabaritoAzulRepository extends JpaRepository<GabaritoAzul, Long> {
    List<GabaritoAzul> findByLanguage(Language language);

    List<GabaritoAzul> findByLanguageIsNull();
}
