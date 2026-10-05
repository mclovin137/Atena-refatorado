package br.com.atena.relatorios.lote.config;

import br.com.atena.relatorios.linha.dao.ILinhaDAO;
import br.com.atena.relatorios.lote.dao.GravadorLinhas;
import br.com.atena.relatorios.lote.dao.LeitorSnapshot;
import br.com.atena.relatorios.lote.dto.LinhaLoteDTO;
import br.com.atena.relatorios.snapshot.actor.PrepararSnapshotActor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RelatorioLoteConfiguracao {
    @Bean
    ThreadPoolTaskExecutor batchExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new ContextPropagatingTaskDecorator());
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("batch-relatorio-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Primary
    JobLauncher asyncJobLauncher(JobRepository repository, ThreadPoolTaskExecutor batchExecutor)
            throws Exception {
        TaskExecutorJobLauncher lancador = new TaskExecutorJobLauncher();
        lancador.setJobRepository(repository);
        lancador.setTaskExecutor(batchExecutor);
        lancador.afterPropertiesSet();
        return lancador;
    }

    @Bean
    @StepScope
    ItemReader<LinhaLoteDTO> snapshotReader(
            PrepararSnapshotActor snapshots, @Value("#{jobParameters['reportId']}") Long reportId) {
        return new LeitorSnapshot(snapshots, reportId);
    }

    @Bean
    ItemWriter<LinhaLoteDTO> linesWriter(ILinhaDAO linhas) {
        return new GravadorLinhas(linhas);
    }

    @Bean
    Step consolidarHistoricoStep(
            JobRepository repository,
            PlatformTransactionManager transacoes,
            ItemReader<LinhaLoteDTO> snapshotReader,
            ItemWriter<LinhaLoteDTO> linesWriter) {
        return new StepBuilder("consolidarHistoricoStep", repository)
                .<LinhaLoteDTO, LinhaLoteDTO>chunk(50, transacoes)
                .reader(snapshotReader)
                .writer(linesWriter)
                .build();
    }

    @Bean
    Job consolidarHistorico(JobRepository repository, Step consolidarHistoricoStep) {
        return new JobBuilder("consolidarHistorico", repository)
                .start(consolidarHistoricoStep)
                .build();
    }
}
