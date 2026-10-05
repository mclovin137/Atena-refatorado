package br.com.atena.escolar.materia.builder;

import br.com.atena.escolar.materia.dominio.Materia;
import br.com.atena.escolar.materia.dto.MateriaRequisicaoDTO;
import br.com.atena.escolar.materia.dto.MateriaRespostaDTO;

public final class MateriaBuilder {

    private MateriaBuilder() {}

    public static Materia dominio(MateriaRequisicaoDTO requisicao, Long identificador) {
        return new Materia(identificador, requisicao.nome);
    }

    public static MateriaRespostaDTO resposta(Materia registro) {
        return new MateriaRespostaDTO(registro.id(), registro.nome());
    }
}
