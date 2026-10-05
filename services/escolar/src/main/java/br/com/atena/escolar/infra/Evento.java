package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="evento")
public class Evento { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=150)
 public String titulo;
 @NotNull
 public java.time.LocalDate inicio;
 @NotNull
 public java.time.LocalDate fim;
 @Size(max=4000)
 public String descricao;
}
