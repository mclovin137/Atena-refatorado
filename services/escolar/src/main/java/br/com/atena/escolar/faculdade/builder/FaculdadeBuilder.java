package br.com.atena.escolar.faculdade.builder;

import br.com.atena.escolar.faculdade.dominio.Faculdade;
import br.com.atena.escolar.faculdade.dto.FaculdadeRequisicaoDTO;
import br.com.atena.escolar.faculdade.dto.FaculdadeRespostaDTO;

public final class FaculdadeBuilder {
    private FaculdadeBuilder() {}

    public static Faculdade dominio(FaculdadeRequisicaoDTO requisicao, Long identificador) {
        return new Faculdade(identificador, requisicao.nome);
    }

    public static FaculdadeRespostaDTO resposta(Faculdade registro) {
        return new FaculdadeRespostaDTO(registro.id(), registro.nome());
    }
}
