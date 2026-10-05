package br.com.atena.escolar.nota.builder;

import br.com.atena.escolar.nota.dominio.Nota;
import br.com.atena.escolar.nota.dto.NotaRequisicaoDTO;
import br.com.atena.escolar.nota.dto.NotaRespostaDTO;

public final class NotaBuilder {

    private NotaBuilder() {}

    public static Nota dominio(NotaRequisicaoDTO requisicao, Long identificador) {
        return new Nota(
                identificador,
                requisicao.disciplinaId,
                requisicao.alunoId,
                requisicao.bimestre,
                requisicao.valor,
                requisicao.recuperacao,
                requisicao.prazo,
                requisicao.liberada);
    }

    public static NotaRespostaDTO resposta(Nota registro) {
        return new NotaRespostaDTO(
                registro.id(),
                registro.disciplinaId(),
                registro.alunoId(),
                registro.bimestre(),
                registro.valor(),
                registro.recuperacao(),
                registro.prazo(),
                registro.liberada());
    }
}
