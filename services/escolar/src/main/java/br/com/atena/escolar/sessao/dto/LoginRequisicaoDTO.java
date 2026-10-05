package br.com.atena.escolar.sessao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequisicaoDTO(@NotBlank @Email String email, @NotBlank String senha) {}
