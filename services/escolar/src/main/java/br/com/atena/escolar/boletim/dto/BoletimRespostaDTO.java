package br.com.atena.escolar.boletim.dto;

import java.util.List;

public record BoletimRespostaDTO(
        Long alunoId,
        String nome,
        String matricula,
        String documento,
        UnidadeBoletimDTO unidade,
        List<DisciplinaBoletimDTO> linhas) {}
