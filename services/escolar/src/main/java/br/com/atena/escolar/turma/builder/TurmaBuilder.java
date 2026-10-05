package br.com.atena.escolar.turma.builder;

import br.com.atena.escolar.turma.dominio.Turma;
import br.com.atena.escolar.turma.dto.TurmaRequisicaoDTO;
import br.com.atena.escolar.turma.dto.TurmaRespostaDTO;

public final class TurmaBuilder {

    private TurmaBuilder() {}

    public static Turma dominio(TurmaRequisicaoDTO requisicao, Long identificador) {
        return new Turma(
                identificador,
                requisicao.cursoId,
                requisicao.serieId,
                requisicao.ano,
                requisicao.nome);
    }

    public static TurmaRespostaDTO resposta(Turma registro) {
        return new TurmaRespostaDTO(
                registro.id(),
                registro.cursoId(),
                registro.serieId(),
                registro.ano(),
                registro.nome());
    }
}
