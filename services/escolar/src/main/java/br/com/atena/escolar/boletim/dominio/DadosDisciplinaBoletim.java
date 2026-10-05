package br.com.atena.escolar.boletim.dominio;

import br.com.atena.escolar.nota.dominio.Nota;

import java.util.List;

/** Dados relacionados carregados antes de executar regras puras do boletim. */
public record DadosDisciplinaBoletim(
        String turma,
        int ano,
        String disciplina,
        Long disciplinaId,
        int cargaHoraria,
        boolean dependencia,
        Long professorId,
        List<Nota> notas,
        int faltas) {
    public DadosDisciplinaBoletim {
        notas = List.copyOf(notas);
    }
}
