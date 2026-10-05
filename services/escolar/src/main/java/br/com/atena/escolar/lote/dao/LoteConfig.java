package br.com.atena.escolar.lote.dao;

import br.com.atena.escolar.lote.actor.PrepararNotasMatriculaActor;
import br.com.atena.escolar.matricula.dao.IMatriculaDAO;
import br.com.atena.escolar.matricula.dominio.Matricula;

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
public class LoteConfig {
    @Bean
    public ThreadPoolTaskExecutor batchExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new ContextPropagatingTaskDecorator());
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("batch-escolar-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Primary
    public JobLauncher asyncJobLauncher(
            JobRepository repository, ThreadPoolTaskExecutor batchExecutor) throws Exception {
        var executor = new TaskExecutorJobLauncher();
        executor.setJobRepository(repository);
        executor.setTaskExecutor(batchExecutor);
        executor.afterPropertiesSet();
        return executor;
    }

    @Bean
    @StepScope
    public ItemReader<Matricula> matriculasReader(
            IMatriculaDAO matriculas, @Value("#{jobParameters['turmaId']}") Long turmaId) {
        return new LeitorMatriculas(matriculas, turmaId);
    }

    @Bean
    public ItemWriter<Matricula> notasWriter(PrepararNotasMatriculaActor preparar) {
        return new EscritorNotas(preparar);
    }

    @Bean
    public Step inicializarNotasStep(
            JobRepository repository,
            PlatformTransactionManager transacoes,
            ItemReader<Matricula> matriculasReader,
            ItemWriter<Matricula> notasWriter) {
        return new StepBuilder("inicializarNotasStep", repository)
                .<Matricula, Matricula>chunk(50, transacoes)
                .reader(matriculasReader)
                .writer(notasWriter)
                .build();
    }

    @Bean
    public Job inicializarNotas(JobRepository repository, Step inicializarNotasStep) {
        return new JobBuilder("inicializarNotas", repository).start(inicializarNotasStep).build();
    }
}
