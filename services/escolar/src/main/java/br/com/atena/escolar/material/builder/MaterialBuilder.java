package br.com.atena.escolar.material.builder;

import br.com.atena.escolar.material.dominio.Material;
import br.com.atena.escolar.material.dto.MaterialRequisicaoDTO;
import br.com.atena.escolar.material.dto.MaterialRespostaDTO;

public final class MaterialBuilder {

    private MaterialBuilder() {}

    public static Material dominio(MaterialRequisicaoDTO requisicao, Long identificador) {
        return new Material(
                identificador,
                requisicao.disciplinaId,
                requisicao.nome,
                requisicao.tipo,
                null,
                requisicao.autorId,
                null);
    }

    public static MaterialRespostaDTO resposta(Material registro) {
        return new MaterialRespostaDTO(
                registro.id(),
                registro.disciplinaId(),
                registro.nome(),
                registro.tipo(),
                registro.autorId());
    }
}
