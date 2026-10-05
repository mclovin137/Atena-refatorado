package br.com.atena.escolar.disciplina.builder;

import br.com.atena.escolar.disciplina.dominio.Disciplina;
import br.com.atena.escolar.disciplina.dto.DisciplinaRequisicaoDTO;
import br.com.atena.escolar.disciplina.dto.DisciplinaRespostaDTO;

public final class DisciplinaBuilder {

    private DisciplinaBuilder() {}

    public static Disciplina dominio(DisciplinaRequisicaoDTO requisicao, Long identificador) {
        return new Disciplina(
                identificador,
                requisicao.turmaId,
                requisicao.materiaId,
                requisicao.professorId,
                requisicao.cargaHoraria);
    }

    public static DisciplinaRespostaDTO resposta(Disciplina registro) {
        return new DisciplinaRespostaDTO(
                registro.id(),
                registro.turmaId(),
                registro.materiaId(),
                registro.professorId(),
                registro.cargaHoraria());
    }
}
