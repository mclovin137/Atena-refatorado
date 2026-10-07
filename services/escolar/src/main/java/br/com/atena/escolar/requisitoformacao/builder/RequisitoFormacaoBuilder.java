package br.com.atena.escolar.requisitoformacao.builder;

import br.com.atena.escolar.requisitoformacao.dominio.RequisitoFormacao;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRequisicaoDTO;
import br.com.atena.escolar.requisitoformacao.dto.RequisitoFormacaoRespostaDTO;

public final class RequisitoFormacaoBuilder {
    private RequisitoFormacaoBuilder() {}

    public static RequisitoFormacao dominio(
            RequisitoFormacaoRequisicaoDTO requisicao, Long identificador) {
        return new RequisitoFormacao(
                identificador, requisicao.nome, requisicao.gradeId, requisicao.descricao);
    }

    public static RequisitoFormacaoRespostaDTO resposta(RequisitoFormacao registro) {
        return new RequisitoFormacaoRespostaDTO(
                registro.id(), registro.nome(), registro.gradeId(), registro.descricao());
    }
}
