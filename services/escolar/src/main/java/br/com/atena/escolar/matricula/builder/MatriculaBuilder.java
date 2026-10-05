package br.com.atena.escolar.matricula.builder;

import br.com.atena.escolar.matricula.dominio.Matricula;
import br.com.atena.escolar.matricula.dto.MatriculaRequisicaoDTO;
import br.com.atena.escolar.matricula.dto.MatriculaRespostaDTO;

public final class MatriculaBuilder {

    private MatriculaBuilder() {}

    public static Matricula dominio(MatriculaRequisicaoDTO requisicao, Long identificador) {
        return new Matricula(
                identificador, requisicao.turmaId, requisicao.alunoId, requisicao.dependencia);
    }

    public static MatriculaRespostaDTO resposta(Matricula registro) {
        return new MatriculaRespostaDTO(
                registro.id(), registro.turmaId(), registro.alunoId(), registro.dependencia());
    }
}
