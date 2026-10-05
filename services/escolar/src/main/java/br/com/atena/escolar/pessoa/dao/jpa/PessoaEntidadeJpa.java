package br.com.atena.escolar.pessoa.dao.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity(name = "PessoaEntidadeJpa")
@Table(name = "pessoa")
public class PessoaEntidadeJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 150)
    public String nome;

    @NotBlank
    @Email
    @Size(max = 180)
    public String email;

    @NotBlank
    @Size(max = 40)
    public String matricula;

    @NotBlank
    @Pattern(regexp = "ALUNO|PROFESSOR|SECRETARIA")
    public String perfil;

    @JsonIgnore public String senhaHash;

    public boolean ativo = true;

    @Size(max = 30)
    public String telefone;

    @Size(max = 500)
    public String endereco;

    @Size(max = 40)
    public String documento;

    public LocalDate nascimento;

    @Size(max = 12)
    public String cep;

    @Size(max = 150)
    public String pai;

    @Size(max = 150)
    public String mae;

    @Size(max = 40)
    public String estadoCivil;

    @Size(max = 30)
    public String sexo;

    @Size(max = 30)
    public String telefoneResponsavel;

    @Email
    @Size(max = 180)
    public String emailResponsavel;

    @JsonIgnore public String avatar;

    @JsonProperty(value = "avatarPresente", access = JsonProperty.Access.READ_ONLY)
    public boolean avatarPresente() {
        return avatar != null;
    }

    @Column(name = "avatar_conteudo", columnDefinition = "bytea")
    public byte[] avatarConteudo;

    public PessoaEntidadeJpa() {}

    public PessoaEntidadeJpa(
            Long id,
            String nome,
            String email,
            String matricula,
            String perfil,
            String senhaHash,
            boolean ativo,
            String telefone,
            String endereco,
            String documento,
            LocalDate nascimento,
            String cep,
            String pai,
            String mae,
            String estadoCivil,
            String sexo,
            String telefoneResponsavel,
            String emailResponsavel,
            String avatar) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.perfil = perfil;
        this.senhaHash = senhaHash;
        this.ativo = ativo;
        this.telefone = telefone;
        this.endereco = endereco;
        this.documento = documento;
        this.nascimento = nascimento;
        this.cep = cep;
        this.pai = pai;
        this.mae = mae;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.telefoneResponsavel = telefoneResponsavel;
        this.emailResponsavel = emailResponsavel;
        this.avatar = avatar;
    }
}
