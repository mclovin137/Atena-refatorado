package br.com.atena.escolar.horario.builder;

import br.com.atena.escolar.horario.dominio.Horario;
import br.com.atena.escolar.horario.dto.HorarioRequisicaoDTO;
import br.com.atena.escolar.horario.dto.HorarioRespostaDTO;

public final class HorarioBuilder {

    private HorarioBuilder() {}

    public static Horario dominio(HorarioRequisicaoDTO requisicao, Long identificador) {
        return new Horario(
                identificador,
                requisicao.disciplinaId,
                requisicao.diaSemana,
                requisicao.inicio,
                requisicao.fim);
    }

    public static HorarioRespostaDTO resposta(Horario registro) {
        return new HorarioRespostaDTO(
                registro.id(),
                registro.disciplinaId(),
                registro.diaSemana(),
                registro.inicio(),
                registro.fim());
    }
}
