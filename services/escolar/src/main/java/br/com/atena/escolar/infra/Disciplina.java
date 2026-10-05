package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="disciplina")
public class Disciplina { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long turmaId;
 @NotNull
 public Long materiaId;

 public Long professorId;
 @Min(1) @Max(2000)
 public int cargaHoraria;
}
