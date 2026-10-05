package br.com.atena.escolar.relatorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SolicitarRelatorioDTO(
        Long alunoId,
        @NotBlank @Pattern(regexp = "HISTORICO|BOLETIM|LISTA_ALUNOS|LISTA_PROFESSORES|LISTA_TURMAS")
                String tipo,
        @NotBlank @Size(max = 100) String requestId) {}
