package br.com.atena.relatorios.relatorio.actor;

import br.com.atena.relatorios.relatorio.builder.RelatorioBuilder;
import br.com.atena.relatorios.relatorio.dao.IExecucaoRelatorioDAO;
import br.com.atena.relatorios.relatorio.dao.IRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.ErroRelatorio;
import br.com.atena.relatorios.relatorio.dominio.ExecucaoRelatorio;
import br.com.atena.relatorios.relatorio.dominio.Relatorio;
import br.com.atena.relatorios.relatorio.dto.RelatorioRespostaDTO;

import org.springframework.stereotype.Component;

@Component
public class ConsultarRelatorioActor {
    private final IRelatorioDAO relatorios;
    private final IExecucaoRelatorioDAO execucoes;

    public ConsultarRelatorioActor(IRelatorioDAO relatorios, IExecucaoRelatorioDAO execucoes) {
        this.relatorios = relatorios;
        this.execucoes = execucoes;
    }

    public Relatorio buscar(Long id) {
        return relatorios
                .buscar(id)
                .orElseThrow(
                        () ->
                                new ErroRelatorio(
                                        ErroRelatorio.CategoriaEnum.NAO_ENCONTRADO,
                                        "Relatório não encontrado"));
    }

    public RelatorioRespostaDTO consultar(Long id) {
        return resposta(buscar(id));
    }

    public RelatorioRespostaDTO resposta(Relatorio relatorio) {
        return RelatorioBuilder.resposta(relatorio, execucoes.buscar(relatorio.executionId()));
    }

    public Relatorio concluido(Long id) {
        Relatorio relatorio = buscar(id);
        ExecucaoRelatorio execucao = execucoes.buscar(relatorio.executionId());
        if (execucao == null || !execucao.concluida()) {
            throw new ErroRelatorio(
                    ErroRelatorio.CategoriaEnum.NAO_CONCLUIDO, "Relatório ainda não concluído");
        }
        return relatorio;
    }
}
