package br.com.atena.relatorios.relatorio.builder;

import br.com.atena.relatorios.relatorio.dominio.ExecucaoRelatorio;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.relatorios.relatorio.dominio.TipoRelatorioEnum;
import br.com.atena.relatorios.relatorio.dto.RelatorioRequisicaoDTO;
import br.com.atena.relatorios.relatorio.dto.RelatorioRespostaDTO;

public final class RelatorioBuilder {
    private RelatorioBuilder() {}

    public static SolicitacaoRelatorio solicitacao(RelatorioRequisicaoDTO entrada) {
        return new SolicitacaoRelatorio(
                entrada.alunoId(),
                entrada.ownerId(),
                entrada.professorId(),
                entrada.releasedOnly(),
                TipoRelatorioEnum.valueOf(entrada.tipo()),
                entrada.requestId());
    }

    public static RelatorioRespostaDTO resposta(Relatorio relatorio, ExecucaoRelatorio execucao) {
        SolicitacaoRelatorio solicitacao = relatorio.solicitacao();
        return new RelatorioRespostaDTO(
                relatorio.id(),
                solicitacao.ownerId(),
                solicitacao.alunoId(),
                solicitacao.tipo().name(),
                execucao == null ? "STARTING" : execucao.status(),
                relatorio.executionId());
    }
}
