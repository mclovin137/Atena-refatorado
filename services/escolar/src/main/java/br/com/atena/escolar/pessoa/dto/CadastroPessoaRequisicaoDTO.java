package br.com.atena.escolar.pessoa.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroPessoaRequisicaoDTO(
        @Valid @NotNull PessoaRequisicaoDTO pessoa, @Size(min = 12, max = 100) String senha) {}
