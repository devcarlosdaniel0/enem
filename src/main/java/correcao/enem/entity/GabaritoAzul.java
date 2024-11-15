package correcao.enem.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_azul")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GabaritoAzul {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer numero;

    @NotNull
    private Character resposta;

    @Nullable
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
}
