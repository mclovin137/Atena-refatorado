package br.com.atena.escolar.lote.builder;

import br.com.atena.escolar.lote.dominio.ExecucaoLote;
import br.com.atena.escolar.lote.dto.ExecucaoLoteRespostaDTO;

public final class ExecucaoLoteBuilder {
    private ExecucaoLoteBuilder() {}

    public static ExecucaoLoteRespostaDTO resposta(ExecucaoLote execucao) {
        return new ExecucaoLoteRespostaDTO(
                execucao.identificador(),
                execucao.estado(),
                execucao.codigoSaida(),
                execucao.quantidadeLida(),
                execucao.quantidadeEscrita());
    }
}
