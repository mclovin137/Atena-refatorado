package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="turma")
public class Turma { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long cursoId;
 @NotNull
 public Long serieId;
 @Min(1900) @Max(2200)
 public int ano;
 @NotBlank @Size(max=150)
 public String nome;
}
