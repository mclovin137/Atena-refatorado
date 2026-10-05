package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="mensagem")
public class Mensagem { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;

 public Long remetenteId;
 @NotNull
 public Long destinatarioId;
 @NotBlank @Size(max=4000)
 public String texto;

 public java.time.LocalDateTime enviadaEm;
}
