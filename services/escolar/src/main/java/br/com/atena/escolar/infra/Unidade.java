package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="unidade")
public class Unidade { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=150)
 public String nome;
 @Size(max=500)
 public String endereco;
 @Size(max=30)
 public String telefone;
 @Size(max=18) public String cnpj;
}
