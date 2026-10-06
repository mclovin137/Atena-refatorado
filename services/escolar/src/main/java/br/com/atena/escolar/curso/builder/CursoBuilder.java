package br.com.atena.escolar.curso.builder;

import br.com.atena.escolar.curso.dominio.Curso;
import br.com.atena.escolar.curso.dto.CursoRequisicaoDTO;
import br.com.atena.escolar.curso.dto.CursoRespostaDTO;

public final class CursoBuilder {

    private CursoBuilder() {}

    public static Curso dominio(CursoRequisicaoDTO requisicao, Long identificador) {
        return new Curso(identificador, requisicao.nome, requisicao.faculdadeId);
    }

    public static CursoRespostaDTO resposta(Curso registro) {
        return new CursoRespostaDTO(
                registro.id(),
                registro.nome(),
                registro.faculdadeId(),
                registro.faculdadeId() == null);
    }
}
