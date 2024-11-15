package correcao.enem.entity;

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
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    @NotNull
    private Integer questionNumber;

    @NotNull
    private Character correctAnswer;
}