package br.com.atena.escolar.avaliacao.builder;

import br.com.atena.escolar.avaliacao.dominio.Avaliacao;
import br.com.atena.escolar.avaliacao.dto.AvaliacaoRequisicaoDTO;
import br.com.atena.escolar.avaliacao.dto.AvaliacaoRespostaDTO;

public final class AvaliacaoBuilder {

    private AvaliacaoBuilder() {}

    public static Avaliacao dominio(AvaliacaoRequisicaoDTO requisicao, Long identificador) {
        return new Avaliacao(
                identificador,
                requisicao.disciplinaId,
                requisicao.nome,
                requisicao.bimestre,
                requisicao.valor,
                requisicao.data,
                requisicao.tipo);
    }

    public static AvaliacaoRespostaDTO resposta(Avaliacao registro) {
        return new AvaliacaoRespostaDTO(
                registro.id(),
                registro.disciplinaId(),
                registro.nome(),
                registro.bimestre(),
                registro.valor(),
                registro.data(),
                registro.tipo());
    }
}
