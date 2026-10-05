package br.com.atena.escolar.presenca.builder;

import br.com.atena.escolar.presenca.dominio.Presenca;
import br.com.atena.escolar.presenca.dto.PresencaRequisicaoDTO;
import br.com.atena.escolar.presenca.dto.PresencaRespostaDTO;

public final class PresencaBuilder {

    private PresencaBuilder() {}

    public static Presenca dominio(PresencaRequisicaoDTO requisicao, Long identificador) {
        return new Presenca(
                identificador, requisicao.aulaId, requisicao.alunoId, requisicao.faltas);
    }

    public static PresencaRespostaDTO resposta(Presenca registro) {
        return new PresencaRespostaDTO(
                registro.id(), registro.aulaId(), registro.alunoId(), registro.faltas());
    }
}
