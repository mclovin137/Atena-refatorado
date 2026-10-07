package br.com.atena.escolar.gradecurricular.builder;

import br.com.atena.escolar.gradecurricular.dominio.GradeCurricular;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRequisicaoDTO;
import br.com.atena.escolar.gradecurricular.dto.GradeCurricularRespostaDTO;

public final class GradeCurricularBuilder {
    private GradeCurricularBuilder() {}

    public static GradeCurricular dominio(
            GradeCurricularRequisicaoDTO requisicao, Long identificador) {
        return new GradeCurricular(identificador, requisicao.nome, requisicao.cursoId);
    }

    public static GradeCurricularRespostaDTO resposta(GradeCurricular registro) {
        return new GradeCurricularRespostaDTO(registro.id(), registro.nome(), registro.cursoId());
    }
}
