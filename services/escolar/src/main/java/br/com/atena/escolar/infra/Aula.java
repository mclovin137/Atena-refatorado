package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="aula")
public class Aula { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long disciplinaId;
 @NotNull
 public java.time.LocalDate data;
 @NotBlank @Size(max=4000)
 public String conteudo;
 @Size(max=4000)
 public String planejamento;
 @Min(1) @Max(12)
 public int horas;
}
