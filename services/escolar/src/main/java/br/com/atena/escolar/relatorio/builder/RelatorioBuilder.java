package br.com.atena.escolar.relatorio.builder;

import br.com.atena.escolar.relatorio.dominio.Relatorio;
import br.com.atena.escolar.relatorio.dto.RelatorioRespostaDTO;

public final class RelatorioBuilder {
    private RelatorioBuilder() {}

    public static RelatorioRespostaDTO resposta(Relatorio relatorio) {
        return new RelatorioRespostaDTO(
                relatorio.id(),
                relatorio.ownerId(),
                relatorio.alunoId(),
                relatorio.tipo(),
                relatorio.status(),
                relatorio.executionId());
    }
}
