package br.com.atena.escolar.web;
import br.com.atena.escolar.infra.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/jobs")
public class BatchController {
 final JobLauncher launcher;final Job job;final JobExplorer explorer;final TurmaRepository turmas;final Access access;
 public BatchController(JobLauncher l,@Qualifier("inicializarNotas") Job j,JobExplorer e,TurmaRepository t,Access a){launcher=l;job=j;explorer=e;turmas=t;access=a;}
 public record Request(@NotNull Long turmaId,@NotBlank @Size(max=100) String requestId){}
 @PostMapping("/inicializar-notas") @ResponseStatus(HttpStatus.ACCEPTED) public Map<String,Object> launch(@Valid @RequestBody Request r,Authentication a)throws Exception{access.admin(a);if(!turmas.existsById(r.turmaId()))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Turma não encontrada");try{return view(launcher.run(job,new JobParametersBuilder().addLong("turmaId",r.turmaId()).addString("requestId",r.requestId()).toJobParameters()));}catch(org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException|org.springframework.batch.core.repository.JobExecutionAlreadyRunningException e){throw new ResponseStatusException(HttpStatus.CONFLICT,"Execução já concluída ou em andamento");}}
 @GetMapping("/{id}") public Map<String,Object> get(@PathVariable Long id,Authentication a){access.admin(a);JobExecution e=explorer.getJobExecution(id);if(e==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Execução não encontrada");return view(e);}
 static Map<String,Object> view(JobExecution e){return Map.of("id",e.getId(),"status",e.getStatus().name(),"exitCode",e.getExitStatus().getExitCode(),"readCount",e.getStepExecutions().stream().mapToLong(StepExecution::getReadCount).sum(),"writeCount",e.getStepExecutions().stream().mapToLong(StepExecution::getWriteCount).sum());}
}
