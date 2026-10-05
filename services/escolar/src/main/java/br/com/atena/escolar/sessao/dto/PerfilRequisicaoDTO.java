package br.com.atena.escolar.sessao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PerfilRequisicaoDTO(
        @Size(max = 30) String telefone,
        @Size(max = 500) String endereco,
        @Size(max = 12) String cep,
        @Size(max = 150) String pai,
        @Size(max = 150) String mae,
        @Size(max = 40) String estadoCivil,
        @Size(max = 30) String sexo,
        @Size(max = 30) String telefoneResponsavel,
        @Email @Size(max = 180) String emailResponsavel,
        LocalDate nascimento) {}
