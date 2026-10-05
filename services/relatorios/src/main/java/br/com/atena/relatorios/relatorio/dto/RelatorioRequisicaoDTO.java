package br.com.atena.relatorios.relatorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RelatorioRequisicaoDTO(
        Long alunoId,
        @NotNull Long ownerId,
        Long professorId,
        boolean releasedOnly,
        @NotBlank @Pattern(regexp = "HISTORICO|BOLETIM|LISTA_ALUNOS|LISTA_PROFESSORES|LISTA_TURMAS")
                String tipo,
        @NotBlank @Size(max = 100) String requestId) {}
