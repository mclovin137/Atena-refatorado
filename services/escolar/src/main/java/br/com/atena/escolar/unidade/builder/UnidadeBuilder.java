package br.com.atena.escolar.unidade.builder;

import br.com.atena.escolar.unidade.dominio.Unidade;
import br.com.atena.escolar.unidade.dto.UnidadeRequisicaoDTO;
import br.com.atena.escolar.unidade.dto.UnidadeRespostaDTO;

public final class UnidadeBuilder {

    private UnidadeBuilder() {}

    public static Unidade dominio(UnidadeRequisicaoDTO requisicao, Long identificador) {
        return new Unidade(
                identificador,
                requisicao.nome,
                requisicao.endereco,
                requisicao.telefone,
                requisicao.cnpj);
    }

    public static UnidadeRespostaDTO resposta(Unidade registro) {
        return new UnidadeRespostaDTO(
                registro.id(),
                registro.nome(),
                registro.endereco(),
                registro.telefone(),
                registro.cnpj());
    }
}
