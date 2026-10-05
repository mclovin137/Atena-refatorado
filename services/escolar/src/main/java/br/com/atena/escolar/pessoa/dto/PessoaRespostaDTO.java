package br.com.atena.escolar.pessoa.dto;

import java.time.LocalDate;

/** Projeção pública explícita: campos privados nunca participam da serialização. */
public record PessoaRespostaDTO(
        Long id,
        String nome,
        String email,
        String matricula,
        String perfil,
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
        boolean avatarPresente) {}
