package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="matricula")
public class Matricula { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long turmaId;
 @NotNull
 public Long alunoId;

 public boolean dependencia;
}
