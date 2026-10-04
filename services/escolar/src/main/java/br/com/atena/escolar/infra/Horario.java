package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="horario")
public class Horario { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long disciplinaId;
 @Min(1) @Max(7)
 public int diaSemana;
 @NotNull
 public java.time.LocalTime inicio;
 @NotNull
 public java.time.LocalTime fim;
}
