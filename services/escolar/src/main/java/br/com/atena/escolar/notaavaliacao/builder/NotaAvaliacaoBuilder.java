package br.com.atena.escolar.notaavaliacao.builder;

import br.com.atena.escolar.notaavaliacao.dominio.NotaAvaliacao;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRequisicaoDTO;
import br.com.atena.escolar.notaavaliacao.dto.NotaAvaliacaoRespostaDTO;

public final class NotaAvaliacaoBuilder {

    private NotaAvaliacaoBuilder() {}

    public static NotaAvaliacao dominio(NotaAvaliacaoRequisicaoDTO requisicao, Long identificador) {
        return new NotaAvaliacao(
                identificador, requisicao.avaliacaoId, requisicao.alunoId, requisicao.valor);
    }

    public static NotaAvaliacaoRespostaDTO resposta(NotaAvaliacao registro) {
        return new NotaAvaliacaoRespostaDTO(
                registro.id(), registro.avaliacaoId(), registro.alunoId(), registro.valor());
    }
}
