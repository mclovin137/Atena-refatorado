package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="nota")
public class Nota { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long disciplinaId;
 @NotNull
 public Long alunoId;
 @Min(1) @Max(4)
 public int bimestre;
 @DecimalMin("0") @DecimalMax("100")
 public Double valor;
 @DecimalMin("0") @DecimalMax("60")
 public Double recuperacao;

 public java.time.LocalDate prazo;

 public boolean liberada;
}
