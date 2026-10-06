package br.com.atena.escolar.importacao.actor;

import br.com.atena.escolar.autorizacao.actor.AutorizacaoEnsinoActor;
import br.com.atena.escolar.comum.dominio.ErroAplicacao;
import br.com.atena.escolar.importacao.dao.IFonteImportacaoDAO;
import br.com.atena.escolar.importacao.dao.IRegistroImportacaoDAO;
import br.com.atena.escolar.importacao.dominio.EstadoImportacaoEnum;
import br.com.atena.escolar.importacao.dto.ImportacaoRespostaDTO;
import br.com.atena.escolar.importacao.dto.VincularPessoaRequisicaoDTO;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ManterImportacaoActor {
    private static final int LIMITE_BYTES = 10 * 1024 * 1024;
    private final AutorizacaoEnsinoActor autorizacao;
    private final PrepararFonteImportacaoActor preparar;
    private final IFonteImportacaoDAO fontes;
    private final IRegistroImportacaoDAO registros;
    private final JobLauncher launcher;
    private final JobExplorer explorer;
    private final Job analisar;
    private final Job importar;
    private final Job reprocessar;

    public ManterImportacaoActor(
            AutorizacaoEnsinoActor autorizacao,
            PrepararFonteImportacaoActor preparar,
            IFonteImportacaoDAO fontes,
            IRegistroImportacaoDAO registros,
            JobLauncher launcher,
            JobExplorer explorer,
            @Qualifier("analisarLegado") Job analisar,
            @Qualifier("importarLegado") Job importar,
            @Qualifier("reprocessarLegado") Job reprocessar) {
        this.autorizacao = autorizacao;
        this.preparar = preparar;
        this.fontes = fontes;
        this.registros = registros;
        this.launcher = launcher;
        this.explorer = explorer;
        this.analisar = analisar;
        this.importar = importar;
        this.reprocessar = reprocessar;
    }

    public ImportacaoRespostaDTO consultar(UUID id) {
        autorizacao.exigirSecretaria();
        return fontes.consultar(id);
    }

    public ImportacaoRespostaDTO analisar(byte[] arquivo) {
        autorizacao.exigirSecretaria();
        if (arquivo.length == 0 || arquivo.length > LIMITE_BYTES) {
            throw ErroAplicacao.entrada("Arquivo deve conter até 10 MiB");
        }
        UUID candidato = UUID.randomUUID();
        UUID id = preparar.guardar(candidato, arquivo);
        if (id.equals(candidato)) {
            iniciarJob(id, analisar, parametros(id, false));
        }
        return fontes.consultar(id);
    }

    public ImportacaoRespostaDTO aplicar(UUID id) {
        autorizacao.exigirSecretaria();
        var atual = fontes.consultar(id);
        if (atual.status() == EstadoImportacaoEnum.CONCLUIDA
                || atual.status() == EstadoImportacaoEnum.CONCLUIDA_COM_PENDENCIAS) {
            return atual;
        }
        fontes.iniciar(id, "importarLegado", EstadoImportacaoEnum.PLANEJADA);
        iniciarJob(id, importar, parametros(id, false));
        return fontes.consultar(id);
    }

    public ImportacaoRespostaDTO retomar(UUID id) {
        autorizacao.exigirSecretaria();
        var atual = fontes.consultar(id);
        if (atual.status() != EstadoImportacaoEnum.FAILED) {
            throw ErroAplicacao.conflito("Importação não possui execução FAILED para retomar");
        }
        var anterior =
                atual.execucaoId() == null ? null : explorer.getJobExecution(atual.execucaoId());
        if (atual.execucaoId() != null && anterior == null) {
            throw ErroAplicacao.conflito("Execução anterior não encontrada");
        }
        String nome =
                anterior == null ? fontes.operacao(id) : anterior.getJobInstance().getJobName();
        Job job =
                nome.equals("analisarLegado")
                        ? analisar
                        : nome.equals("reprocessarLegado") ? reprocessar : importar;
        fontes.iniciar(id, nome, EstadoImportacaoEnum.FAILED);
        iniciarJob(
                id,
                job,
                anterior == null
                        ? parametros(id, nome.equals("reprocessarLegado"))
                        : anterior.getJobParameters());
        return fontes.consultar(id);
    }

    public ImportacaoRespostaDTO reprocessar(UUID id) {
        autorizacao.exigirSecretaria();
        fontes.iniciar(id, "reprocessarLegado", EstadoImportacaoEnum.CONCLUIDA_COM_PENDENCIAS);
        iniciarJob(id, reprocessar, parametros(id, true));
        return fontes.consultar(id);
    }

    public ImportacaoRespostaDTO vincular(UUID id, VincularPessoaRequisicaoDTO requisicao) {
        autorizacao.exigirSecretaria();
        var estado = fontes.consultar(id).status();
        if (estado != EstadoImportacaoEnum.PLANEJADA
                && estado != EstadoImportacaoEnum.CONCLUIDA_COM_PENDENCIAS) {
            throw ErroAplicacao.conflito("Importação não permite vínculo no estado atual");
        }
        registros.vincularPessoa(id, requisicao.identificadorLegado(), requisicao.pessoaId());
        return fontes.consultar(id);
    }

    private JobParameters parametros(UUID id, boolean reprocessar) {
        return new JobParametersBuilder()
                .addString("importacaoId", id.toString())
                .addString("reprocessar", Boolean.toString(reprocessar))
                .addString("tentativa", UUID.randomUUID().toString())
                .toJobParameters();
    }

    private void iniciarJob(UUID id, Job job, JobParameters parametros) {
        try {
            var execucao = launcher.run(job, parametros);
            fontes.execucao(id, execucao.getId());
            if (execucao.getStatus() == org.springframework.batch.core.BatchStatus.FAILED) {
                fontes.finalizar(id, EstadoImportacaoEnum.FAILED);
            }
        } catch (JobExecutionAlreadyRunningException
                | JobInstanceAlreadyCompleteException
                | JobRestartException
                | JobParametersInvalidException
                | TaskRejectedException erro) {
            fontes.finalizar(id, EstadoImportacaoEnum.FAILED);
            throw ErroAplicacao.conflito("Execução da importação não pôde ser iniciada");
        }
    }
}
