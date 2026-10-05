package br.com.atena.escolar.lote.dao;

import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.lote.dominio.ExecucaoLote;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InicializacaoNotasDAO implements IInicializacaoNotasDAO {
    private final JobLauncher executor;
    private final Job tarefa;
    private final JobExplorer execucoes;

    public InicializacaoNotasDAO(
            JobLauncher executor,
            @Qualifier("inicializarNotas") Job tarefa,
            JobExplorer execucoes) {
        this.executor = executor;
        this.tarefa = tarefa;
        this.execucoes = execucoes;
    }

    @Override
    public ExecucaoLote iniciar(Long turmaId, String requisicaoId) {
        try {
            var parametros =
                    new JobParametersBuilder()
                            .addLong("turmaId", turmaId)
                            .addString("requestId", requisicaoId)
                            .toJobParameters();
            return dominio(executor.run(tarefa, parametros));
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.CONFLITO,
                    "Execução já concluída ou em andamento",
                    erro);
        } catch (Exception erro) {
            throw new ErroAplicacao(
                    ErroAplicacao.CategoriaEnum.FALHA_INTERNA,
                    "Não foi possível iniciar o lote",
                    erro);
        }
    }

    @Override
    public Optional<ExecucaoLote> consultar(Long identificador) {
        return Optional.ofNullable(execucoes.getJobExecution(identificador)).map(this::dominio);
    }

    private ExecucaoLote dominio(JobExecution execucao) {
        return new ExecucaoLote(
                execucao.getId(),
                execucao.getStatus().name(),
                execucao.getExitStatus().getExitCode(),
                execucao.getStepExecutions().stream().mapToLong(StepExecution::getReadCount).sum(),
                execucao.getStepExecutions().stream()
                        .mapToLong(StepExecution::getWriteCount)
                        .sum());
    }
}
