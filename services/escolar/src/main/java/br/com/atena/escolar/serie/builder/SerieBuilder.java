package br.com.atena.escolar.serie.builder;

import br.com.atena.escolar.serie.dominio.Serie;
import br.com.atena.escolar.serie.dto.SerieRequisicaoDTO;
import br.com.atena.escolar.serie.dto.SerieRespostaDTO;

public final class SerieBuilder {

    private SerieBuilder() {}

    public static Serie dominio(SerieRequisicaoDTO requisicao, Long identificador) {
        return new Serie(identificador, requisicao.nome);
    }

    public static SerieRespostaDTO resposta(Serie registro) {
        return new SerieRespostaDTO(registro.id(), registro.nome());
    }
}
