package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="curriculo")
public class Curriculo { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=150)
 public String nome;
 @NotNull
 public Long materiaId;
 @Min(1) @Max(2000)
 public int cargaHoraria;
}
