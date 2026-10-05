package br.com.atena.escolar.aula.builder;

import br.com.atena.escolar.aula.dominio.Aula;
import br.com.atena.escolar.aula.dto.AulaRequisicaoDTO;
import br.com.atena.escolar.aula.dto.AulaRespostaDTO;

public final class AulaBuilder {

    private AulaBuilder() {}

    public static Aula dominio(AulaRequisicaoDTO requisicao, Long identificador) {
        return new Aula(
                identificador,
                requisicao.disciplinaId,
                requisicao.data,
                requisicao.conteudo,
                requisicao.planejamento,
                requisicao.horas);
    }

    public static AulaRespostaDTO resposta(Aula registro) {
        return new AulaRespostaDTO(
                registro.id(),
                registro.disciplinaId(),
                registro.data(),
                registro.conteudo(),
                registro.planejamento(),
                registro.horas());
    }
}
