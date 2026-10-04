package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="presenca")
public class Presenca { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long aulaId;
 @NotNull
 public Long alunoId;
 @Min(0) @Max(12)
 public int faltas;
}
