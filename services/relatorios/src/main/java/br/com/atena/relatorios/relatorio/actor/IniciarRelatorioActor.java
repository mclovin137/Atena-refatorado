package br.com.atena.relatorios.relatorio.actor;

import br.com.atena.relatorios.relatorio.builder.RelatorioBuilder;
import br.com.atena.relatorios.relatorio.dao.IExecucaoRelatorioDAO;
import br.com.atena.relatorios.relatorio.dao.IRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.ExecucaoRelatorio;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dominio.SolicitacaoRelatorio;
import br.com.atena.relatorios.relatorio.dto.RelatorioRequisicaoDTO;
import br.com.atena.relatorios.relatorio.dto.RelatorioRespostaDTO;

import org.springframework.stereotype.Component;

@Component
public class IniciarRelatorioActor {
    private final IRelatorioDAO relatorios;
    private final IExecucaoRelatorioDAO execucoes;

    public IniciarRelatorioActor(IRelatorioDAO relatorios, IExecucaoRelatorioDAO execucoes) {
        this.relatorios = relatorios;
        this.execucoes = execucoes;
    }

    public RelatorioRespostaDTO iniciar(RelatorioRequisicaoDTO entrada) {
        SolicitacaoRelatorio solicitacao = RelatorioBuilder.solicitacao(entrada);
        Relatorio relatorio =
                relatorios
                        .buscarSolicitacao(solicitacao.ownerId(), solicitacao.requestId())
                        .orElseGet(() -> relatorios.criar(solicitacao));
        ExecucaoRelatorio anterior = execucoes.buscar(relatorio.executionId());
        if (anterior != null && !anterior.falhou()) {
            return RelatorioBuilder.resposta(relatorio, anterior);
        }
        ExecucaoRelatorio execucao = execucoes.iniciar(relatorio.id());
        relatorios.registrarExecucao(relatorio.id(), execucao.id());
        Relatorio atualizado =
                new Relatorio(
                        relatorio.id(),
                        relatorio.solicitacao(),
                        execucao.id(),
                        relatorio.nome(),
                        relatorio.snapshot());
        return RelatorioBuilder.resposta(atualizado, execucao);
    }
}
