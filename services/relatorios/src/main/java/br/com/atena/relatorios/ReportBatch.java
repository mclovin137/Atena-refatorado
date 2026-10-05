package br.com.atena.relatorios;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestClient;
@Configuration
public class ReportBatch {
 public record Item(Long reportId,int numero,Map<String,Object> conteudo){}
 @Bean ThreadPoolTaskExecutor batchExecutor(){var e=new ThreadPoolTaskExecutor();e.setTaskDecorator(new org.springframework.core.task.support.ContextPropagatingTaskDecorator());e.setCorePoolSize(2);e.setMaxPoolSize(2);e.setQueueCapacity(10);e.setThreadNamePrefix("batch-relatorio-");e.initialize();return e;}
 @Bean @Primary JobLauncher asyncJobLauncher(JobRepository repository,ThreadPoolTaskExecutor batchExecutor)throws Exception{var l=new TaskExecutorJobLauncher();l.setJobRepository(repository);l.setTaskExecutor(batchExecutor);l.afterPropertiesSet();return l;}
 @Bean @StepScope ItemReader<Item> snapshotReader(RelatorioRepository reports,ObjectMapper mapper,RestClient.Builder builder,@Value("#{jobParameters['reportId']}") Long reportId,@Value("${atena.school-url}")String school,@Value("${atena.service-token}")String token){
  return new ItemReader<>(){Iterator<Map<String,Object>> lines;int index=0;
   @SuppressWarnings("unchecked")public Item read() throws Exception {if(lines==null){Relatorio r=reports.findById(reportId).orElseThrow();var factory=new org.springframework.http.client.JdkClientHttpRequestFactory(java.net.http.HttpClient.newBuilder().connectTimeout(java.time.Duration.ofSeconds(5)).build());factory.setReadTimeout(java.time.Duration.ofSeconds(30));RestClient client=builder.clone().baseUrl(school).requestFactory(factory).defaultHeader("X-Service-Token",token).build();String path=r.tipo.startsWith("LISTA_")?"/api/internal/listas/"+r.tipo:"/api/internal/snapshot/"+r.alunoId+"?releasedOnly="+r.releasedOnly+(r.professorId==null?"":"&professorId="+r.professorId);Map<String,Object> snapshot=r.snapshot==null?client.get().uri(path).retrieve().body(Map.class):mapper.readValue(r.snapshot,Map.class);if(r.snapshot==null && r.tipo.equals("BOLETIM")){List<Map<String,Object>> all=(List<Map<String,Object>>)snapshot.get("linhas");int latest=all.stream().mapToInt(x->((Number)x.get("ano")).intValue()).max().orElse(0);snapshot.put("linhas",all.stream().filter(x->((Number)x.get("ano")).intValue()==latest).toList());}if(r.snapshot==null)reports.snapshot(r.id,snapshot.get("nome").toString(),mapper.writeValueAsString(snapshot));lines=((List<Map<String,Object>>)snapshot.get("linhas")).iterator();}return lines.hasNext()?new Item(reportId,index++,lines.next()):null;}
  };
 }
 @Bean ItemWriter<Item> linesWriter(LinhaRepository lines,ObjectMapper mapper){return chunk->{for(Item i:chunk){Linha l=lines.findByRelatorioIdAndNumero(i.reportId(),i.numero()).orElseGet(Linha::new);l.relatorioId=i.reportId();l.numero=i.numero();l.conteudo=mapper.writeValueAsString(i.conteudo());lines.save(l);}};}
 @Bean Step consolidarHistoricoStep(JobRepository repository,PlatformTransactionManager tx,ItemReader<Item> snapshotReader,ItemWriter<Item> linesWriter){return new StepBuilder("consolidarHistoricoStep",repository).<Item,Item>chunk(50,tx).reader(snapshotReader).writer(linesWriter).build();}
 @Bean Job consolidarHistorico(JobRepository repository,Step consolidarHistoricoStep){return new JobBuilder("consolidarHistorico",repository).start(consolidarHistoricoStep).build();}
}
