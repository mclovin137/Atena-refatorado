package br.com.atena.escolar.curriculo.builder;

import br.com.atena.escolar.curriculo.dominio.Curriculo;
import br.com.atena.escolar.curriculo.dto.CurriculoRequisicaoDTO;
import br.com.atena.escolar.curriculo.dto.CurriculoRespostaDTO;

public final class CurriculoBuilder {

    private CurriculoBuilder() {}

    public static Curriculo dominio(CurriculoRequisicaoDTO requisicao, Long identificador) {
        return new Curriculo(
                identificador,
                requisicao.nome,
                requisicao.materiaId,
                requisicao.cargaHoraria,
                requisicao.gradeId);
    }

    public static CurriculoRespostaDTO resposta(Curriculo registro) {
        return new CurriculoRespostaDTO(
                registro.id(),
                registro.nome(),
                registro.materiaId(),
                registro.cargaHoraria(),
                registro.gradeId());
    }
}
