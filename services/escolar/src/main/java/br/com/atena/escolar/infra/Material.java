package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="material")
public class Material { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotNull
 public Long disciplinaId;
 @NotBlank @Size(max=150)
 public String nome;

 public String tipo;
 @com.fasterxml.jackson.annotation.JsonIgnore
 public String arquivo;

 public Long autorId;
}
