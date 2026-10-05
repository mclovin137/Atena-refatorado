package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="nota_avaliacao")
public class NotaAvaliacao { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long avaliacaoId;
 @NotNull
 public Long alunoId;
 @NotNull @DecimalMin("0") @DecimalMax("100")
 public Double valor;
}
