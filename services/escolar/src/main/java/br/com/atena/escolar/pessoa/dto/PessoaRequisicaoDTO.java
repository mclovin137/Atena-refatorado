package br.com.atena.escolar.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Contrato de entrada explícito, independente da entidade JPA. */
public class PessoaRequisicaoDTO {
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean avatarPresente;
}
