package br.com.atena.escolar.infra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="pessoa")
public class Pessoa { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=150)
 public String nome;
 @NotBlank @Email @Size(max=180)
 public String email;
 @NotBlank @Size(max=40)
 public String matricula;
 @NotBlank @Pattern(regexp="ALUNO|PROFESSOR|SECRETARIA")
 public String perfil;
 @com.fasterxml.jackson.annotation.JsonIgnore
 public String senhaHash;

 public boolean ativo = true;
 @Size(max=30)
 public String telefone;
 @Size(max=500)
 public String endereco;
 @Size(max=40)
 public String documento;

 public java.time.LocalDate nascimento;
 @Size(max=12) public String cep;
 @Size(max=150) public String pai;
 @Size(max=150) public String mae;
 @Size(max=40) public String estadoCivil;
 @Size(max=30) public String sexo;
 @Size(max=30) public String telefoneResponsavel;
 @Email @Size(max=180) public String emailResponsavel;
 @com.fasterxml.jackson.annotation.JsonIgnore public String avatar;
 @com.fasterxml.jackson.annotation.JsonProperty(value="avatarPresente",access=com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY) public boolean avatarPresente(){return avatar!=null;}
}
