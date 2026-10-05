package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="avaliacao")
public class Avaliacao { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long disciplinaId;
 @NotBlank @Size(max=150)
 public String nome;
 @Min(1) @Max(4)
 public int bimestre;
 @NotNull @DecimalMin("0.01") @DecimalMax("100")
 public Double valor;
 @NotNull
 public java.time.LocalDate data;
 @NotBlank @Pattern(regexp="QUANTITATIVA|QUALITATIVA")
 public String tipo;
}
