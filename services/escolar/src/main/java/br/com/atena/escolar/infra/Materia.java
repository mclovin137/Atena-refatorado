package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="materia")
public class Materia { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=150)
 public String nome;
}
