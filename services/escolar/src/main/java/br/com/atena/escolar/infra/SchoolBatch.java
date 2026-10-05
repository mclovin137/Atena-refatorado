package br.com.atena.escolar.infra;
import java.util.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
@Configuration
public class SchoolBatch {
 @Bean public ThreadPoolTaskExecutor batchExecutor(){var e=new ThreadPoolTaskExecutor();e.setTaskDecorator(new org.springframework.core.task.support.ContextPropagatingTaskDecorator());e.setCorePoolSize(2);e.setMaxPoolSize(2);e.setQueueCapacity(10);e.setThreadNamePrefix("batch-escolar-");e.initialize();return e;}
 @Bean @Primary public JobLauncher asyncJobLauncher(JobRepository repository,ThreadPoolTaskExecutor batchExecutor)throws Exception{var l=new TaskExecutorJobLauncher();l.setJobRepository(repository);l.setTaskExecutor(batchExecutor);l.afterPropertiesSet();return l;}
 @Bean @StepScope public ItemReader<Matricula> matriculasReader(MatriculaRepository repository,@Value("#{jobParameters['turmaId']}") Long turma){
  return new ItemReader<>(){int page=0;Iterator<Matricula> current=Collections.emptyIterator();boolean ended=false;public Matricula read(){if(ended)return null;if(!current.hasNext()){var rows=repository.findByTurmaId(turma,PageRequest.of(page++,50,org.springframework.data.domain.Sort.by("id"))).getContent();if(rows.isEmpty()){ended=true;return null;}current=rows.iterator();}return current.next();}};
 }
 @Bean public ItemWriter<Matricula> notasWriter(DisciplinaRepository disciplines,NotaRepository grades){return chunk->{for(Matricula m:chunk){for(Disciplina d:disciplines.findByTurmaId(m.turmaId)){for(int b=1;b<=4;b++){if(!grades.existsByDisciplinaIdAndAlunoIdAndBimestre(d.id,m.alunoId,b)){Nota n=new Nota();n.disciplinaId=d.id;n.alunoId=m.alunoId;n.bimestre=b;n.liberada=false;grades.save(n);}}}}};}
 @Bean public Step inicializarNotasStep(JobRepository repository,PlatformTransactionManager tx,ItemReader<Matricula> matriculasReader,ItemWriter<Matricula> notasWriter){return new StepBuilder("inicializarNotasStep",repository).<Matricula,Matricula>chunk(50,tx).reader(matriculasReader).writer(notasWriter).build();}
 @Bean public Job inicializarNotas(JobRepository repository,Step inicializarNotasStep){return new JobBuilder("inicializarNotas",repository).start(inicializarNotasStep).build();}
}
