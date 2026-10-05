package br.com.atena.relatorios.lote.dao;

import br.com.atena.relatorios.relatorio.dao.IExecucaoRelatorioDAO;
import br.com.atena.relatorios.relatorio.dominio.ExecucaoRelatorio;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ExecucaoRelatorioDAO implements IExecucaoRelatorioDAO {
    private final JobLauncher lancador;
    private final Job processo;
    private final JobExplorer explorador;

    public ExecucaoRelatorioDAO(
            JobLauncher lancador,
            @Qualifier("consolidarHistorico") Job processo,
            JobExplorer explorador) {
        this.lancador = lancador;
        this.processo = processo;
        this.explorador = explorador;
    }

    @Override
    public ExecucaoRelatorio buscar(Long executionId) {
        if (executionId == null) {
            return null;
        }
        JobExecution execucao = explorador.getJobExecution(executionId);
        return execucao == null
                ? null
                : new ExecucaoRelatorio(execucao.getId(), execucao.getStatus().name());
    }

    @Override
    public ExecucaoRelatorio iniciar(Long reportId) {
        try {
            JobExecution execucao =
                    lancador.run(
                            processo,
                            new JobParametersBuilder()
                                    .addLong("reportId", reportId)
                                    .toJobParameters());
            return new ExecucaoRelatorio(execucao.getId(), execucao.getStatus().name());
        } catch (JobExecutionException erro) {
            throw new IllegalStateException("Não foi possível iniciar o relatório", erro);
        }
    }
}
